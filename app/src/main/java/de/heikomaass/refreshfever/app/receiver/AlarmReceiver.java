package de.heikomaass.refreshfever.app.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import de.heikomaass.refreshfever.app.service.FeverRefreshService;

/**
 * Created by hmaass on 07.04.14.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = AlarmReceiver.class.getSimpleName();
    public static final Class<FeverRefreshService> FEVER_REFRESH_SERVICE_CLASS = FeverRefreshService.class;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent refreshFeverIntent = new Intent(context, FEVER_REFRESH_SERVICE_CLASS);
        Log.i(TAG, "Starting wakeful service @ " + SystemClock.elapsedRealtime());
        startWakefulService(context, refreshFeverIntent);
    }
}
