package de.heikomaass.refreshfever.app.scheduler;

import org.joda.time.LocalTime;
import org.joda.time.Period;


public class Alarm {

    private LocalTime start;
    private Period periodUntilRepeat;

    public Alarm(LocalTime start, Period periodBetweenNextAlarm) {
        this.start = start;
        this.periodUntilRepeat = periodBetweenNextAlarm;
    }

    public LocalTime getStart() {
        return start;
    }

    public Period periodBetweenNextAlarm() {
        return periodUntilRepeat;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "start=" + start +
                ", periodUntilRepeat=" + periodUntilRepeat +
                '}';
    }
}
