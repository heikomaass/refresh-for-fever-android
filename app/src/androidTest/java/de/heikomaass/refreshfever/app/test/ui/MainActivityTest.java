package de.heikomaass.refreshfever.app.test.ui;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

import org.joda.time.LocalTime;
import org.joda.time.Period;

import de.heikomaass.refreshfever.app.scheduler.Alarm;
import de.heikomaass.refreshfever.app.scheduler.AlarmFactory;
import de.heikomaass.refreshfever.app.scheduler.AlarmScheduler;
import de.heikomaass.refreshfever.app.ui.MainActivity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hmaass on 20.06.14.
 */
public class MainActivityTest extends ActivityUnitTestCase<MainActivity> {

    AlarmScheduler mockAlarmScheduler;
    AlarmFactory mockAlarmFactory;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        // Starts the MainActivity of the target application
        startActivity(new Intent(getInstrumentation().getTargetContext(), MainActivity.class), null, null);

        mockAlarmScheduler = mock(AlarmScheduler.class);
        mockAlarmFactory = mock(AlarmFactory.class);
        getActivity().setAlarmFactory(mockAlarmFactory);
        getActivity().setAlarmScheduler(mockAlarmScheduler);
    }

    public void testCreateAlarm() {
        LocalTime localTime = new LocalTime();
        Period period = Period.hours(1);

        Alarm alarm = new Alarm(localTime,period);
        when(mockAlarmFactory.createAlarm()).thenReturn(alarm);
        getActivity().createAlarm();
        verify(mockAlarmScheduler,times(1)).setAlarm(alarm);
    }

    public void testStopAlarm() {
        getActivity().stopAlarm();
        verify(mockAlarmScheduler,times(1)).cancelAlarm();
    }

}
