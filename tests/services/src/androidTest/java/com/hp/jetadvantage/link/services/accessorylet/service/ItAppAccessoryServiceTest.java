package com.hp.jetadvantage.link.services.accessorylet.service;

import android.util.Log;

import com.hp.jetadvantage.link.api.accessory.AbstractAccessoryService;

import java.util.concurrent.CountDownLatch;

/**
 * Instrumented test app's AccessoryService
 * On behalf of a real 3rd party app, this test service is used for instrumented tests
 */

public class ItAppAccessoryServiceTest extends AbstractAccessoryService {
    private static final String TAG = "ItAppAccessoryServiceTest";
    private static CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart : Instrumented test app's AccessoryService");
        if (latch != null) {
            latch.countDown();
            latch = new CountDownLatch(1);
        }
    }

    public static boolean waitForOnStart(int i) {
        if (latch == null) {
            return false;
        }
        try {
            synchronized (latch) {
                return latch.await(i, java.util.concurrent.TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
