package com.hp.jetadvantage.link.services.deviceeventslet.adapter;

import com.google.gson.JsonObject;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceEventService;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.services.deviceeventslet.model.DeviceEvent;
import com.hp.jetadvantage.link.services.deviceeventslet.util.DeviceEventUtils;
import com.hp.ws.cdm.alert.Alert;
import com.hp.ws.cdm.pubsub.Message;

public class DeviceEventConverter {

    private static final String TAG = Constants.TAG + "/Converter";

    public DeviceEventConverter() {
    }

    public static DeviceEvent convert(Message message) {
        // Filter device events types here
        // 1. GUN_ALERTS event type are accepted
        // 2. Severity is warning / error / critical
        // 3. Data is not null

        if (!StandardDeviceEventService.Gun.GUN_ALERTS.equals(message.getGun())) {
            return null;
        }

        JsonObject data = message.getData();
        if (data != null && data.get("severity") != null) {
            String severity = data.get("severity").getAsString();
            if (!isSeverityCritical(severity)) {
                SLog.d(TAG, "Skip the message because the severity is not warning / error / critical");
                return null;
            }
        }
        return convertToDeviceEvent(message);
    }

    private static boolean isSeverityCritical(String severity) {
        return Alert.Severity.WARNING.value().equals(severity) ||
                Alert.Severity.ERROR.value().equals(severity) ||
                Alert.Severity.CRITICAL.value().equals(severity);
    }

    private static DeviceEvent convertToDeviceEvent(Message message) {
        DeviceEvent deviceEvent;
        Message.Method method = message.getMethod();

        // If Data is null, get the id from resourcesId.value.
        String id = getIdFromReportData(message);
        if (id == null) {
            SLog.d(TAG, "Skip the message because the id is null");
            return null;
        }

        // Put a data from the message to the mDeviceEventMap
        // because the remove event does not have a data but we have to send a data to the client.
        if (com.hp.ws.cdm.pubsub.Message.Method.ADD.equals(method)
                || com.hp.ws.cdm.pubsub.Message.Method.UPDATE.equals(method)) {
            deviceEvent = DeviceEventUtils.convertDuneDeviceEventFormatToWorkpathFormat(Long.parseLong(id), message);
            DeviceEventUtils.getStateChangeTypeMap().put(id, deviceEvent);
        } else {
            deviceEvent = DeviceEventUtils.getStateChangeTypeMap().remove(id);
            if (deviceEvent != null) {
                deviceEvent.setStateChangeType((message.getMethod() != null ?
                        message.getMethod() : com.hp.ws.cdm.pubsub.Message.Method.REMOVE).toString());
                deviceEvent.getTimestamp().setTime(DeviceEventUtils.getCurrentTime());
            }
        }

        return deviceEvent;
    }

    private static String getIdFromReportData(Message message) {
        if (message.getData() != null && message.getData().get("id") != null) {
            return message.getData().get("id").toString();
        }
        return message.getResourceId() != null ? message.getResourceId().getValue() : null;
    }
}
