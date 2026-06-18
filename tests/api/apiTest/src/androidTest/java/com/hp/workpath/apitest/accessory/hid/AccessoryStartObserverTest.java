package com.hp.workpath.apitest.accessory.hid;

import android.os.Handler;

import com.hp.workpath.api.accessory.AccessoryInfo;
import com.hp.workpath.api.accessory.hid.AccessoryService;
import com.hp.workpath.api.accessory.hid.EventCode;

import java.util.concurrent.CountDownLatch;

public class AccessoryStartObserverTest extends AccessoryService.AbstractAccessoryStartObserver {
    CountDownLatch mLatch = null;
    String mAccessoryContextId = null;
    AccessoryInfo mAccessoryInfo = null;

    public AccessoryStartObserverTest(Handler handler, CountDownLatch latch) {
        super(handler);
        mLatch = latch;
    }

    @Override
    public void onReady(AccessoryInfo accessoryInfo, EventCode eventCode, String s, String s1) {
        mAccessoryContextId = s1;
        mAccessoryInfo = accessoryInfo;
        mLatch.countDown();
    }

    public String getAccessoryContextId() {
        return mAccessoryContextId;
    }

    public AccessoryInfo getAccessoryInfo() {
        return mAccessoryInfo;
    }
}
