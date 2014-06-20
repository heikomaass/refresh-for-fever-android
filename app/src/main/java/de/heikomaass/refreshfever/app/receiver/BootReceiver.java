package de.heikomaass.refreshfever.app.receiver;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import javax.inject.Inject;

import de.heikomaass.refreshfever.app.RefreshFeverApp;
import de.heikomaass.refreshfever.app.scheduler.Alarm;
import de.heikomaass.refreshfever.app.scheduler.AlarmFactory;
import de.heikomaass.refreshfever.app.scheduler.AlarmScheduler;

/**
 * Created by hmaass on 20.04.14.
 */
public class BootReceiver extends BroadcastReceiver {
    private final String TAG = BootReceiver.class.getSimpleName();

    @Inject
    AlarmScheduler alarmScheduler;

    @Inject
    AlarmFactory alarmFactory;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: context:" + context);
        Application application = (Application) context.getApplicationContext();
        if (application instanceof RefreshFeverApp) {
            ((RefreshFeverApp) application).inject(this);

            handleIntent(context, intent);
        }
    }

    private void handleIntent(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            enableAlarm();
        }
    }

    private void enableAlarm() {
        Alarm configuredAlarm = alarmFactory.createAlarm();
        Log.i(TAG, "Enabling Alarm:" + configuredAlarm);
        alarmScheduler.setAlarm(configuredAlarm);
    }
}
