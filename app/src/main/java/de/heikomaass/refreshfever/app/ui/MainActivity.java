package de.heikomaass.refreshfever.app.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import javax.inject.Inject;

import de.heikomaass.refreshfever.app.scheduler.Alarm;
import de.heikomaass.refreshfever.app.scheduler.AlarmFactory;
import de.heikomaass.refreshfever.app.scheduler.AlarmScheduler;
import de.heikomaass.refreshfever.app.R;


public class MainActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    AlarmScheduler alarmScheduler;

    @Inject
    AlarmFactory alarmFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        defaultSharedPreferences.registerOnSharedPreferenceChangeListener(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button startButton = (Button) findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createAlarm();
            }
        });

        final Button stopButton = (Button) findViewById(R.id.stop);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAlarm();
            }
        });

        final Button settingsButton = (Button) findViewById(R.id.settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
    }

    public void stopAlarm() {
        Toast toast = Toast.makeText(this, "Stop Service", Toast.LENGTH_SHORT);
        toast.show();
        alarmScheduler.cancelAlarm();
    }

    public void createAlarm() {
        final Alarm alarm = alarmFactory.createAlarm();
        Toast toast = Toast.makeText(this, "Start Service with alarm:" + alarm, Toast.LENGTH_LONG);
        toast.show();
        alarmScheduler.setAlarm(alarm);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (alarmScheduler.hasAlarm()) {
            this.stopAlarm();
            this.createAlarm();
        }
    }

    /**
     * For testing reasons
     * @param alarmScheduler
     */
    public void setAlarmScheduler(AlarmScheduler alarmScheduler) {
        this.alarmScheduler = alarmScheduler;
    }

    /**
     * For testing reasons
     * @param alarmFactory
     */
    public void setAlarmFactory(AlarmFactory alarmFactory) {
        this.alarmFactory = alarmFactory;
    }
}
