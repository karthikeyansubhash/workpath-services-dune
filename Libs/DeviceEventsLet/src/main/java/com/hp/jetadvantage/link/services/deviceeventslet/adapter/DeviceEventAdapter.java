package com.hp.jetadvantage.link.services.deviceeventslet.adapter;

import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceEventService;
import com.hp.jetadvantage.link.services.deviceeventslet.handler.DeviceEventHandler;
import com.hp.jetadvantage.link.services.deviceeventslet.model.DeviceEvent;
import com.hp.jetadvantage.link.services.deviceeventslet.util.DeviceEventUtils;
import com.hp.workpath.api.deviceevents.DeviceEventslet;
import com.hp.ws.cdm.alert.Alert;
import com.hp.ws.cdm.pubsub.Message;

import java.util.List;

public class DeviceEventAdapter {

    private static final String TAG = DeviceEventslet.TAG + "/A";

    public static boolean isSupported(IDeviceEventService deviceEventService) {
        return deviceEventService.isSupported();
    }

    public static List<DeviceEvent> getDeviceEvents(IDeviceEventService deviceEventService) {
        DeviceEventUtils.getStateChangeTypeMap().clear();
        List<Alert> alerts = deviceEventService.getDeviceEvents().getAlerts();
        List<DeviceEvent> deviceEventsList = DeviceEventUtils.convertDuneDeviceEventFormatToWorkpathFormat(alerts);
        for (DeviceEvent deviceEvent : deviceEventsList) {
            DeviceEventUtils.getStateChangeTypeMap().put(String.valueOf(deviceEvent.getId()), deviceEvent);
        }
        return deviceEventsList;
    }

    public static void addCallback(IDeviceEventService deviceEventService, DeviceEventHandler handler) {
        deviceEventService.addCallback(reports -> {
            SLog.d(TAG, "onChangeEvent: " + reports.size());
            for (Message message : reports) {
                DeviceEvent deviceEvent = DeviceEventConverter.convert(message);
                if (deviceEvent != null)
                    handler.sendMessageToClient(deviceEvent);
            }
        });
    }
}
