package de.heikomaass.refreshfever.app;

import android.app.Application;
import android.util.Log;

import net.danlew.android.joda.ResourceZoneInfoProvider;

import dagger.ObjectGraph;
import de.heikomaass.refreshfever.app.network.NetworkModule;
import de.heikomaass.refreshfever.app.scheduler.SchedulerModule;

/**
 * Created by hmaass on 21.04.14.
 */
public class RefreshFeverApp extends Application {
    private static final String TAG = RefreshFeverApp.class.getSimpleName();
    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Initializing ResourceZoneInfoProvider");
        ResourceZoneInfoProvider.init(this);
        objectGraph = ObjectGraph.create(new AppModule(this), new SchedulerModule(), new NetworkModule());
    }


    public void inject(Object object) {
        objectGraph.inject(object);
    }
}
