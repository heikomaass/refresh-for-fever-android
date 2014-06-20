package de.heikomaass.refreshfever.app.test.network;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.net.URI;

import de.heikomaass.refreshfever.app.Settings;
import de.heikomaass.refreshfever.app.network.UpdateResult;
import de.heikomaass.refreshfever.app.network.impl.UpdateManagerImpl;
import de.heikomaass.refreshfever.app.test.MockitoAwareInstrumentationTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by hmaass on 20.06.14.
 */
public class UpdateManagerImplTest extends MockitoAwareInstrumentationTestCase {

    UpdateManagerImpl cut;
    Settings mockSettings;
    HttpClient mockHttpClient;

    public void setUp() throws Exception {
        super.setUp();

        mockSettings = mock(Settings.class);
        mockHttpClient = mock(HttpClient.class);
        cut = new UpdateManagerImpl(mockHttpClient, mockSettings);
    }

    public void testUpdate_shouldAddRefreshQueryParam() throws IOException {
        ArgumentCaptor<HttpUriRequest> httpUriRequestArgumentCaptor = ArgumentCaptor.forClass(HttpUriRequest.class);
        HttpResponse response = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 200, "OK"));

        when(mockSettings.getFeverUrl()).thenReturn("http://domain.com/fever");
        when(mockHttpClient.execute(httpUriRequestArgumentCaptor.capture())).thenReturn(response);
        cut.updateFever();

        URI uri = httpUriRequestArgumentCaptor.getValue().getURI();
        assertThat(uri.toString(), is("http://domain.com/fever?refresh=true"));
    }

    public void testUpdate_shouldDoNothing_whenUrlIsInvalid() throws IOException {
        when(mockSettings.getFeverUrl()).thenReturn("http://");
        UpdateResult updateResult = cut.updateFever();
        assertThat(updateResult.isSuccessful(), is(false));
    }

}
