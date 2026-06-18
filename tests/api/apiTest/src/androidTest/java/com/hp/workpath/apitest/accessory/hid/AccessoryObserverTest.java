package com.hp.workpath.apitest.accessory.hid;

import android.os.Handler;
import android.util.Log;

import com.hp.workpath.api.accessory.AccessoryInfo;
import com.hp.workpath.api.accessory.ReportEventInfo;
import com.hp.workpath.api.accessory.hid.AccessoryService;
import com.hp.workpath.api.accessory.hid.EventCode;

import java.util.concurrent.CountDownLatch;

public class AccessoryObserverTest extends AccessoryService.AbstractAccessoryObserver {
    CountDownLatch contextChangeLatch;
    AccessoryInfo onContextChangeAccessoryInfo;
    EventCode onContextChangeEventCode;
    String onContextChangeAccessoryContextId;
    EventCode observedEventCode;

    CountDownLatch onReceiveLatch;
    AccessoryInfo onReceiveAccessoryInfo;
    ReportEventInfo onReceiveReportEventInfo;

    public AccessoryObserverTest(Handler handler, CountDownLatch contextChangeLatch) {
        super(handler);
        this.contextChangeLatch = contextChangeLatch;
        this.onReceiveLatch = onReceiveLatch;
    }

    public AccessoryObserverTest(Handler handler, CountDownLatch contextChangeLatch, CountDownLatch onReceiveLatch) {
        super(handler);
        this.contextChangeLatch = contextChangeLatch;
        this.onReceiveLatch = onReceiveLatch;
    }

    public void setObservedEvent(EventCode eventCode) {
        observedEventCode = eventCode;
    }

    @Override
    public void onContextChange(AccessoryInfo accessoryInfo, EventCode eventCode, String s, String s1) {
        Log.i("APITEST", "AccessoryObserverTest.onContextChange : " + eventCode + ", accessoryId:" + s1);
        if (eventCode == observedEventCode) {
            onContextChangeAccessoryInfo = accessoryInfo;
            onContextChangeEventCode = eventCode;
            onContextChangeAccessoryContextId = s1;
            contextChangeLatch.countDown();
        }
    }

    @Override
    public void onReceive(AccessoryInfo accessoryInfo, ReportEventInfo reportEventInfo) {
        Log.i("APITEST", "AccessoryObserverTest.onReceive : " + accessoryInfo + "," + reportEventInfo);
        onReceiveAccessoryInfo = accessoryInfo;
        onReceiveReportEventInfo = reportEventInfo;
        if (onReceiveLatch != null)
            onReceiveLatch.countDown();
    }

    public AccessoryInfo getOnContextChangeAccessoryInfo() {
        return onContextChangeAccessoryInfo;
    }

    public EventCode getOnContextChangeEventCode() {
        return onContextChangeEventCode;
    }

    public String getOnContextChangeAccessoryContextId() {
        return onContextChangeAccessoryContextId;
    }

    public ReportEventInfo getOnReceiveReportEventInfo() {
        return onReceiveReportEventInfo;
    }

    public AccessoryInfo getOnReceiveAccessoryInfo() {
        return onReceiveAccessoryInfo;
    }
}
