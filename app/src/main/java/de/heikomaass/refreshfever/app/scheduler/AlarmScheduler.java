package de.heikomaass.refreshfever.app.scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import de.heikomaass.refreshfever.app.receiver.AlarmReceiver;
import de.heikomaass.refreshfever.app.receiver.BootReceiver;

public class AlarmScheduler {

    public final static String TAG = "AlarmScheduler";
    private Context context = null;

    public AlarmScheduler(Context context) {
        this.context = context;
    }

    public void setAlarm(Alarm alarm) {
        Log.i(TAG, "set alarm: " + alarm);
        AlarmManager alarmManager = getAlarmManager();
        PendingIntent pendingAlarmIndent = createPendingIntent();
        DateTime dateTime = alarm.getStart().toDateTimeToday();
        long currentTimeMillis = System.currentTimeMillis();
        long alarmMillis = dateTime.getMillis();
        Log.i(TAG, "alert in millis: " + (alarmMillis - currentTimeMillis));
        Duration duration = alarm.periodBetweenNextAlarm().toStandardDuration();

        Log.i(TAG, "period in millis: " + duration.getMillis());
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                alarmMillis,
                duration.getMillis(),
                pendingAlarmIndent);


        // Enable {@code BootReceiver} to automatically restart the alarm when the
        // device is rebooted.
        setBootReceiverEnabledSetting(PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
    }

    public void cancelAlarm() {
        Log.i(TAG, "cancel current alarm...");
        AlarmManager alarmManager = getAlarmManager();
        PendingIntent pendingAlarmIndent = getPendingIntent();

        if (pendingAlarmIndent != null) {
            Log.i(TAG, "found existing PendingIntent. Cancel it.");
            alarmManager.cancel(pendingAlarmIndent);
        }

        // Disable {@code BootReceiver}
        setBootReceiverEnabledSetting(PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
    }

    public boolean hasAlarm() {
        PendingIntent pendingIntent = getPendingIntent();
        return pendingIntent != null;
    }

    private void setBootReceiverEnabledSetting(int enabledSetting) {
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                enabledSetting,
                PackageManager.DONT_KILL_APP);
    }

    private PendingIntent createPendingIntent() {
        Intent alarmIndent = new Intent(context, AlarmReceiver.class);
        return PendingIntent.getBroadcast(context, 0, alarmIndent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getPendingIntent() {
        Intent alarmIndent = new Intent(context, AlarmReceiver.class);
        return PendingIntent.getBroadcast(context, 0, alarmIndent, PendingIntent.FLAG_NO_CREATE);

    }

    private AlarmManager getAlarmManager() {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }
}
