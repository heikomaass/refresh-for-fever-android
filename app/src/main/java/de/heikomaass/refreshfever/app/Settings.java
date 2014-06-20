package de.heikomaass.refreshfever.app;

import android.content.SharedPreferences;

import org.joda.time.LocalTime;
import org.joda.time.Period;

/**
 * Created by hmaass on 19.06.14.
 */
public class Settings {
    private static final String KEY_PREF_TIME = "pref_time";
    private static final String KEY_PREF_URL = "pref_url";
    private static final String KEY_PREF_PERIOD = "pref_period";

    SharedPreferences sharedPreferences;

    public Settings(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public LocalTime getAlertTime() {
        String pref_time = this.sharedPreferences.getString(KEY_PREF_TIME, "01:00:00.000");
        return LocalTime.parse(pref_time);
    }

    public String getFeverUrl() {
        String pref_url = this.sharedPreferences.getString(KEY_PREF_URL, null);
        return pref_url;
    }

    public Period getPeriod() {
        long pref_period = this.sharedPreferences.getLong(KEY_PREF_PERIOD, 60 * 60 * 1000);
        return new Period(pref_period);
    }
}
