package de.heikomaass.refreshfever.app.scheduler;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.heikomaass.refreshfever.app.Settings;
import de.heikomaass.refreshfever.app.ui.MainActivity;
import de.heikomaass.refreshfever.app.receiver.BootReceiver;

/**
 * Created by hmaass on 15.06.14.
 */
@Module(complete = false,
        library = true,
        injects = {BootReceiver.class, MainActivity.class})
public class SchedulerModule {

    @Provides
    @Singleton
    AlarmScheduler provideAlarmScheduler(Application application) {
        return new AlarmScheduler(application);
    }

    @Provides
    @Singleton
    AlarmFactory provideAlarmFactory(Settings settings) {
        return new AlarmFactory(settings);
    }


}
