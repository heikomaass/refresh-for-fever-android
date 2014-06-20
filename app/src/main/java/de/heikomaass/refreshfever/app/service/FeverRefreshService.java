package de.heikomaass.refreshfever.app.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;

import javax.inject.Inject;

import de.heikomaass.refreshfever.app.RefreshFeverApp;
import de.heikomaass.refreshfever.app.R;
import de.heikomaass.refreshfever.app.network.UpdateManager;
import de.heikomaass.refreshfever.app.network.UpdateResult;
import de.heikomaass.refreshfever.app.receiver.AlarmReceiver;

/**
 * Created by hmaass on 07.04.14.
 */
public class FeverRefreshService extends IntentService {

    private static final String TAG = "FeverRefreshService";
    private static final int NOTIFICATION_ID = 1;
    private NotificationManager notificationManager;

    @Inject
    UpdateManager updateManager;

    public FeverRefreshService() {
        super("FeverRefreshService");
    }


    public void onCreate() {
        super.onCreate();
        ((RefreshFeverApp) this.getApplication()).inject(this);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.i(TAG, "onHandleIntent");
        updateFever();
        AlarmReceiver.completeWakefulIntent(workIntent);
    }

    public void updateFever() {
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Refresh in Progress")
                .setProgress(0, 0, true);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

        UpdateResult updateResult = updateManager.updateFever();
        String message;
        if (updateResult.isSuccessful()) {
            message = "Refresh was successful";
        } else {
            message = updateResult.getErrorMessage();
        }
        Date now = new Date();
        DateFormat dateFormat = DateFormat.getTimeInstance();
        String dateString = dateFormat.format(now);

        Notification.Builder finishedBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(message)
                .setContentText(dateString)
                .setProgress(0, 0, false);

        notificationManager.notify(NOTIFICATION_ID, finishedBuilder.build());
    }
}
