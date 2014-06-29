package de.heikomaass.refreshfever.app.test.network;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;

import java.io.IOException;
import java.net.URI;

import de.heikomaass.refreshfever.app.Settings;
import de.heikomaass.refreshfever.app.network.UpdateResult;
import de.heikomaass.refreshfever.app.network.impl.UpdateManagerImpl;
import de.heikomaass.refreshfever.app.test.MockitoAwareInstrumentationTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hmaass on 20.06.14.
 */
public class UpdateManagerImplTest extends MockitoAwareInstrumentationTestCase {

    UpdateManagerImpl cut;
    Settings mockSettings;
    HttpClient mockHttpClient;
    private ConnectivityManager mockConnectivityManager;

    public void setUp() throws Exception {
        super.setUp();

        mockSettings = mock(Settings.class);
        mockHttpClient = mock(HttpClient.class);
        mockConnectivityManager = mock(ConnectivityManager.class);

        cut = new UpdateManagerImpl(mockHttpClient, mockConnectivityManager, mockSettings);
    }

    public void testUpdate_shouldAddRefreshQueryParam() throws IOException {
        mockNetwork(true);
        when(mockSettings.getFeverUrl()).thenReturn("http://domain.com/fever");
        when(mockSettings.getOnlyRefreshOnWlan()).thenReturn(true);
        ArgumentCaptor<HttpUriRequest> httpUriRequestArgumentCaptor = mockSuccessfulRequest();

        cut.updateFever();

        URI uri = httpUriRequestArgumentCaptor.getValue().getURI();
        assertThat(uri.toString(), is("http://domain.com/fever?refresh=true"));
    }



    public void testUpdate_shouldDoNothing_whenNoWlanIsAvailable_andSettingsPreventsIt() throws IOException {
        when(mockSettings.getFeverUrl()).thenReturn("http://domain.com/fever");
        when(mockSettings.getOnlyRefreshOnWlan()).thenReturn(true);
        NetworkInfo mockNetworkInfo = mockNetwork(true);
        when(mockNetworkInfo.getType()).thenReturn(ConnectivityManager.TYPE_MOBILE);

        UpdateResult updateResult = cut.updateFever();
        assertThat(updateResult.isSuccessful(), is(false));
        verify(mockHttpClient, never()).execute(Matchers.any(HttpUriRequest.class));
    }

    public void testUpdate_shouldSuccessup_whenNoWlanIsAvailable_butSettingsAllowsIt() throws IOException {
        when(mockSettings.getFeverUrl()).thenReturn("http://domain.com/fever");
        when(mockSettings.getOnlyRefreshOnWlan()).thenReturn(false);
        NetworkInfo mockNetworkInfo = mockNetwork(true);
        when(mockNetworkInfo.getType()).thenReturn(ConnectivityManager.TYPE_MOBILE);
        mockSuccessfulRequest();

        UpdateResult updateResult = cut.updateFever();
        assertThat(updateResult.isSuccessful(), is(true));

    }

    public void testUpdate_shouldDoNothing_whenNoInternetIsAvailable() throws IOException {
        when(mockSettings.getFeverUrl()).thenReturn("http://domain.com/fever");
        when(mockSettings.getOnlyRefreshOnWlan()).thenReturn(true);
        mockNetwork(false);

        UpdateResult updateResult = cut.updateFever();
        assertThat(updateResult.isSuccessful(), is(false));
        verify(mockHttpClient, never()).execute(Matchers.any(HttpUriRequest.class));
    }

    public void testUpdate_shouldDoNothing_whenUrlIsInvalid() throws IOException {
        when(mockSettings.getFeverUrl()).thenReturn("http://");
        UpdateResult updateResult = cut.updateFever();
        assertThat(updateResult.isSuccessful(), is(false));
    }

    private NetworkInfo mockNetwork(boolean online) {
        NetworkInfo mockNetworkInfo = mock(NetworkInfo.class);
        when(mockNetworkInfo.isConnectedOrConnecting()).thenReturn(online);
        when(mockNetworkInfo.getType()).thenReturn(ConnectivityManager.TYPE_WIFI);
        when(mockConnectivityManager.getActiveNetworkInfo()).thenReturn(mockNetworkInfo);
        return mockNetworkInfo;
    }

    private ArgumentCaptor<HttpUriRequest> mockSuccessfulRequest() throws IOException {
        HttpResponse response = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 200, "OK"));
        ArgumentCaptor<HttpUriRequest> httpUriRequestArgumentCaptor = ArgumentCaptor.forClass(HttpUriRequest.class);
        when(mockHttpClient.execute(httpUriRequestArgumentCaptor.capture())).thenReturn(response);
        return httpUriRequestArgumentCaptor;
    }
}
