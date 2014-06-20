package de.heikomaass.refreshfever.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.heikomaass.refreshfever.app.network.NetworkModule;
import de.heikomaass.refreshfever.app.scheduler.SchedulerModule;

/**
 * Created by hmaass on 15.06.14.
 */
@Module(includes = {NetworkModule.class, SchedulerModule.class},
        injects = {RefreshFeverApp.class})
public class AppModule {
    private RefreshFeverApp refreshFeverApp;

    public AppModule(RefreshFeverApp refreshFeverApp) {
        this.refreshFeverApp = refreshFeverApp;
    }

    @Provides
    @Singleton
    Application provideApplicationContext() {
        return refreshFeverApp;
    }

    @Provides
    @Singleton
    Settings provideSettings() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.refreshFeverApp);
        return new Settings(defaultSharedPreferences);
    }


}
