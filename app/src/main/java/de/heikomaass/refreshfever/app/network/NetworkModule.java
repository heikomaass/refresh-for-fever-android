package de.heikomaass.refreshfever.app.network;

import org.apache.http.impl.client.DefaultHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.heikomaass.refreshfever.app.Settings;
import de.heikomaass.refreshfever.app.network.impl.UpdateManagerImpl;
import de.heikomaass.refreshfever.app.service.FeverRefreshService;

/**
 * Created by hmaass on 19.06.14.
 */
@Module(library = true,
        complete = false,
        injects = {FeverRefreshService.class})
public class NetworkModule {
    @Provides
    @Singleton
    UpdateManager provideUpdateManager(Settings settings) {
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        return new UpdateManagerImpl(defaultHttpClient, settings);
    }
}
