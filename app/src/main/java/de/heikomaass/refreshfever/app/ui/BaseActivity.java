package de.heikomaass.refreshfever.app.ui;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import de.heikomaass.refreshfever.app.RefreshFeverApp;

/**
 * Created by hmaass on 15.06.14.
 */
public abstract class BaseActivity extends Activity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Perform injection so that when this call returns all dependencies will be available for use.
        Application application = getApplication();
        if (application instanceof RefreshFeverApp) {
            ((RefreshFeverApp) application).inject(this);
        } // else: we are in a UnitTest, so injection please...
    }
}
