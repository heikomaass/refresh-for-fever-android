package de.heikomaass.refreshfever.app.network.impl;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;

import de.heikomaass.refreshfever.app.Settings;
import de.heikomaass.refreshfever.app.network.UpdateManager;
import de.heikomaass.refreshfever.app.network.UpdateResult;

/**
 * Created by hmaass on 08.04.14.
 */
public class UpdateManagerImpl implements UpdateManager {

    private final static String TAG = UpdateManager.class.getSimpleName();
    private Settings settings;
    private HttpClient httpClient;
    private ConnectivityManager connectivityManager;

    public UpdateManagerImpl(HttpClient httpClient, ConnectivityManager connectivityManager, Settings settings) {
        this.httpClient = httpClient;
        this.settings = settings;
        this.connectivityManager = connectivityManager;
    }

    @Override
    public UpdateResult updateFever() {
        String urlFromSettings = this.settings.getFeverUrl();
        Uri uri = Uri.parse(urlFromSettings);
        if (TextUtils.isEmpty(uri.getScheme()) || TextUtils.isEmpty(uri.getHost())) {
            return new UpdateResult(false, "URL is invalid");
        }

        if (!hasNetworkAccess()) {
            return new UpdateResult(false, "No internet connection");
        }

        Uri.Builder builder = uri.buildUpon().appendQueryParameter("refresh", "true");
        Uri uriWithQuery = builder.build();
        return updateFever(uriWithQuery);
    }

    private boolean hasNetworkAccess() {
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private UpdateResult updateFever(Uri uri) {
        HttpUriRequest httpUriRequest = new HttpGet(uri.toString());

        try {
            Log.i(TAG, "Executing request: " + httpUriRequest.getURI().toString());
            HttpResponse execute = httpClient.execute(httpUriRequest);
            StatusLine statusLine = execute.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                Log.i(TAG, statusLine.toString());

                // HttpClient must consume the content. Otherwise the connection won't be closed.
                HttpEntity entity = execute.getEntity();
                if (entity != null) {
                    entity.consumeContent();
                }
                return new UpdateResult(true, null);
            }
            return new UpdateResult(false, "StatusLine : " + statusLine);
        } catch (IOException e) {
            Log.e(TAG, "Exception while refreshing fever: " + e);
            return new UpdateResult(false, "Exception while refreshing fever");
        }
    }

}
