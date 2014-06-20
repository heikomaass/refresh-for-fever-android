package de.heikomaass.refreshfever.app.test;

import android.test.InstrumentationTestCase;

/**
 * Created by hmaass on 20.06.14.
 */
public class MockitoAwareInstrumentationTestCase extends InstrumentationTestCase {
    public void setUp() throws Exception {
        super.setUp();
        // @See https://code.google.com/p/dexmaker/issues/detail?id=2
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
    }
}
