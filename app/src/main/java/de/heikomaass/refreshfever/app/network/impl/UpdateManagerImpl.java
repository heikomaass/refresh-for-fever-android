package de.heikomaass.refreshfever.app.network.impl;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

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
    private OkHttpClient httpClient;

    public UpdateManagerImpl(OkHttpClient httpClient, Settings settings) {
        this.httpClient = httpClient;
        this.settings = settings;
    }

    @Override
    public UpdateResult updateFever() {
        String urlFromSettings = this.settings.getFeverUrl();
        Uri uri = Uri.parse(urlFromSettings);
        if (TextUtils.isEmpty(uri.getScheme()) || TextUtils.isEmpty(uri.getHost())) {
            return new UpdateResult(false, "URL is invalid");
        }

        Uri.Builder builder = uri.buildUpon().appendQueryParameter("refresh", "true");
        Uri uriWithQuery = builder.build();
        return updateFever(uriWithQuery);
    }

    private UpdateResult updateFever(Uri uri) {
        Request request = new Request.Builder()
                .url(uri.toString())
                .build();

        try {
            Log.i(TAG, "Executing request: " + request);
            Response response = httpClient.newCall(request).execute();
            Log.i(TAG, response.message());
            if (response.isSuccessful()) {
                return new UpdateResult(true, null);
            }
            return new UpdateResult(false, response.message());
        } catch (IOException e) {
            Log.e(TAG, "Exception while refreshing fever: " + e);
            return new UpdateResult(false, "Exception while refreshing fever");
        }
    }

}
