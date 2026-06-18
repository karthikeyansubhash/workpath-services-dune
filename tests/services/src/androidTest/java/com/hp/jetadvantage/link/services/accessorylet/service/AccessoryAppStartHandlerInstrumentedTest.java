package com.hp.jetadvantage.link.services.accessorylet.service;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.EnumSet;

@RunWith(AndroidJUnit4.class)
public class AccessoryAppStartHandlerInstrumentedTest {
    private AccessoryAppStartHandlerTest mAppStartHandler;
    private HandlerThread mHandlerThread;

    @Test
    public void GivenAccessoryAppStartHandler_WhenSendAppStartMessageCalled_ThenStatusShouldBeNotOwnedForNotOwnedApp() throws InterruptedException {
        Context context = ApplicationProvider.getApplicationContext();
        String packageName = context.getPackageName();

        createAppStartHandler(context);
        mAppStartHandler.setAppHasOwnedAccessories(false);
        mAppStartHandler.sendAppStartMessage(packageName);

        // Wait for the status to change from STARTED or IN_PROGRESS
        int waitCount = 0;
        while(waitCount < 50) {
            Thread.sleep(100);
            if(mAppStartHandler.getProgress(packageName) != AccessoryAppStartHandler.AppStartHandlerStatus.STARTED &&
                    mAppStartHandler.getProgress(packageName) != AccessoryAppStartHandler.AppStartHandlerStatus.IN_PROGRESS) {
                break;
            }
            waitCount++;
        }

        assertEquals(AccessoryAppStartHandler.AppStartHandlerStatus.NOT_OWNED,
                mAppStartHandler.getProgress(packageName));
    }

    /**
     * Given AccessoryAppStartHandler
     * When SendAppStartMessage is called for an app that have owned accessories
     * Then AccessoryAppStartHandler should bind to the app's accessory service and send a start message.
     * after the start message is sent, the status of AccessoryAppStartHandler should be completed.
     * and the onStart method of the app's accessory service (ItAppAccessoryServiceTest) should be called by the
     * start message.
     */
    @Test
    public void GivenAccessoryAppStartHandler_WhenSendAppStartMessageCalled_ThenStatusShouldBeCompletedForOwnedApp() {
        Context context = ApplicationProvider.getApplicationContext();
        String packageName = context.getPackageName();
        boolean onStartResult = false;

        createAppStartHandler(context);
        mAppStartHandler.setAppHasOwnedAccessories(true);
        mAppStartHandler.sendAppStartMessage(packageName);

        EnumSet<AccessoryAppStartHandler.AppStartHandlerStatus> allowedStatuses =
                EnumSet.of(
                        AccessoryAppStartHandler.AppStartHandlerStatus.STARTED,
                        AccessoryAppStartHandler.AppStartHandlerStatus.IN_PROGRESS
                );

        assertTrue(allowedStatuses.contains(mAppStartHandler.getProgress(packageName)));

        try {
            onStartResult = ItAppAccessoryServiceTest.waitForOnStart(5);
        } catch (Exception e) {
        }
        assertTrue(onStartResult);
        assertEquals(AccessoryAppStartHandler.AppStartHandlerStatus.COMPLETED,
                mAppStartHandler.getProgress(packageName));
    }

    private void createAppStartHandler(Context context) {
        mHandlerThread = new HandlerThread("InstrumentedTest" + ":" + getClass().getSimpleName(),
                Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mAppStartHandler = new AccessoryAppStartHandlerTest(context, mHandlerThread.getLooper());
    }

    /**
     * AccessoryAppStartHandlerTest is a subclass of AccessoryAppStartHandler that overrides hasAppOwnedAccessories
     * to control the return value of the method for testing purposes.
     * This allows standalone tests to be run without needing to access the package manager database.
     */
    public class AccessoryAppStartHandlerTest extends AccessoryAppStartHandler {
        private boolean hasOwnedAccessories = false;

        public AccessoryAppStartHandlerTest(Context context, Looper looper) {
            super(context, looper);
        }

        public void setAppHasOwnedAccessories(Boolean owned) {
            Log.i("AccessoryAppStartHandlerTest", "setAppOwnedAccessories");
            hasOwnedAccessories = owned;
        }

        @Override
        protected boolean hasAppOwnedAccessories(String packageName) {
            Log.i("AccessoryAppStartHandlerTest", "hasAppOwnedAccessories return false");
            return hasOwnedAccessories;
        }
    }
}
