package de.heikomaass.refreshfever.app.test.scheduler;

import org.joda.time.LocalTime;
import org.joda.time.Period;

import de.heikomaass.refreshfever.app.Settings;
import de.heikomaass.refreshfever.app.scheduler.Alarm;
import de.heikomaass.refreshfever.app.scheduler.AlarmFactory;
import de.heikomaass.refreshfever.app.test.MockitoAwareInstrumentationTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by hmaass on 20.06.14.
 */
public class AlarmFactoryTest extends MockitoAwareInstrumentationTestCase {
    AlarmFactory cut;
    Settings mockSettings;

    public void setUp() throws Exception {
        super.setUp();
        mockSettings = mock(Settings.class);

        cut = new AlarmFactory(mockSettings);
    }

    public void testCreateAlarm_shouldReturnNull_whenSettingsAreEmpty() {
        Alarm alarm = cut.createAlarm();
        assertThat(alarm, is(nullValue()));
    }

    public void testCreateAlarm_shouldFetchAlarmTimeFromSettings() {
        prepareSettings();
        Alarm alarm = cut.createAlarm();

        assertThat(alarm, is(notNullValue()));
        assertThat(alarm.getStart(), is(stubAlertTime()));
    }

    public void testCreateAlarm_shouldFetchPeriodFromSettings() {
        prepareSettings();
        Alarm alarm = cut.createAlarm();

        assertThat(alarm, is(notNullValue()));
        assertThat(alarm.periodBetweenNextAlarm(), is(stubPeriod()));
    }

    private LocalTime stubAlertTime() {
        String time = "01:00:00.000";
        return LocalTime.parse(time);
    }

    private Period stubPeriod() {
        long millis = 60 * 60 * 1000L;
        return new Period(millis);
    }

    private void prepareSettings() {
        when(mockSettings.getAlertTime()).thenReturn(stubAlertTime());
        when(mockSettings.getPeriod()).thenReturn(stubPeriod());
    }
}
