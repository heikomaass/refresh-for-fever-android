package de.heikomaass.refreshfever.app.scheduler;

import org.joda.time.LocalTime;
import org.joda.time.Period;

import de.heikomaass.refreshfever.app.Settings;

/**
 * Created by hmaass on 20.04.14.
 */
public class AlarmFactory {

    Settings settings;

    public AlarmFactory(Settings settings) {
        this.settings = settings;
    }

    public Alarm createAlarm() {
        LocalTime alertTime = settings.getAlertTime();
        Period alertPeriod = settings.getPeriod();
        if (alertTime == null || alertPeriod == null) {
            return null;
        }

        Alarm alarm = new Alarm(alertTime, alertPeriod);
        return alarm;
    }
}
