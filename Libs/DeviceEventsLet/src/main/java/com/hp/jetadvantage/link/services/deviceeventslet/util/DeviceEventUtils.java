package com.hp.jetadvantage.link.services.deviceeventslet.util;

import android.util.Log;

import com.google.gson.JsonObject;
import com.hp.jetadvantage.link.services.deviceeventslet.model.DeviceEvent;
import com.hp.jetadvantage.link.services.deviceeventslet.model.Timestamp;
import com.hp.ws.cdm.alert.Alert;
import com.hp.ws.cdm.pubsub.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.function.Consumer;

public class DeviceEventUtils {
    public static final String TAG = DeviceEventUtils.class.getSimpleName();

    // A map to save stateChangeType {add, remove}
    // <key, value> : <instanceId, stateChangeType)
    private final static HashMap<String, DeviceEvent> mStateChangeTypeMap = new HashMap<>();

    /***
     *
     * @param alerts : A list of device events from dune
     * @return converted device events in workpath format
     */
    public static List<DeviceEvent> convertDuneDeviceEventFormatToWorkpathFormat(List<Alert> alerts) {
        List<DeviceEvent> resultArray = new ArrayList<>();
        for (Alert alert : alerts) {
            Alert.Severity severity = alert.getSeverity();
            if (Alert.Severity.WARNING.equals(severity) ||
                    Alert.Severity.ERROR.equals(severity) ||
                    Alert.Severity.CRITICAL.equals(severity)) {
                resultArray.add(convertDuneDeviceEventFormatToWorkpathFormat(alert));
            }
        }
        return resultArray;
    }

    /**
     * A method to convert a Dune device event to a Jolt device event.
     *
     * @param alert : A device alert event from the DUNE.
     */
    public static DeviceEvent convertDuneDeviceEventFormatToWorkpathFormat(Alert alert) {
        // may be we need to implement a filter to filter out the events that are not needed.
        // e.g. category: jobStatus
        DeviceEvent deviceEvent = new DeviceEvent();
        String stateChangeType = "add";
        if (mStateChangeTypeMap.containsKey(Long.toString(alert.getId()))) {
            DeviceEvent event = mStateChangeTypeMap.get(Long.toString(alert.getId()));
            if (event != null) {
                stateChangeType = event.getStateChangeType();
            }
        }

        deviceEvent.setTitle(Long.toString(alert.getStringId()));
        deviceEvent.setStateChangeType(stateChangeType);
        setIfNotNull(alert.getInstanceId(), deviceEvent::setInstanceId);
        setIfNotNull(alert.getCategory(), category -> deviceEvent.setCategory(category.toString()));
        setIfNotNull(alert.getSeverity(), severity -> deviceEvent.setSeverity(severity.toString()));
        setIfNotNull(alert.getAlertCode(), alertCode -> deviceEvent.setEventCode(alertCode.toString()));
        setIfNotNull(alert.getId(), deviceEvent::setId);

        Timestamp timestamp = new Timestamp();
        timestamp.setTime(alert.getDateTime().toString());
        timestamp.setOffset(0);

        deviceEvent.setTimestamp(timestamp);

        //TODO : need to check the support information link
        deviceEvent.setSupportInformationLink("https://support.hp.com/printer");

        return deviceEvent;
    }

    public static DeviceEvent convertDuneDeviceEventFormatToWorkpathFormat(Long id, Message message) {
        DeviceEvent deviceEvent = new DeviceEvent();
        // message.getData() is JsonObject class. so we need add null check every cases.
        JsonObject data = message.getData();
        if (data != null) {
            setFieldIfPresent(data, "stringId", deviceEvent::setTitle);
            setFieldIfPresent(data, "instanceId", deviceEvent::setInstanceId);
            setFieldIfPresent(data, "category", deviceEvent::setCategory);
            setFieldIfPresent(data, "severity", deviceEvent::setSeverity);
            setFieldIfPresent(data, "alertCode", deviceEvent::setEventCode);
            deviceEvent.setId(id);
        }

        if (message.getMethod() != null) {
            Log.i(TAG, "message.getMethod() = " + message.getMethod().toString());
            deviceEvent.setStateChangeType(message.getMethod().toString());
        } else {
            // this case would not be happened because if the getMethod() returns null or empty
            // then it was already handled in the DeviceEventHandler.
            Log.i(TAG, "message.getMethod() is null.");
        }

        Timestamp timestamp = new Timestamp();
        timestamp.setTime(message.getData().get("dateTime").toString());
        timestamp.setOffset(0);
        deviceEvent.setTimestamp(timestamp);

        return deviceEvent;
    }

    private static void setFieldIfPresent(JsonObject data, String fieldName, Consumer<String> setter) {
        if (data.has(fieldName) && !data.get(fieldName).isJsonNull()) {
            setter.accept(data.get(fieldName).getAsString());
        }
    }

    private static <T> void setIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    public static HashMap<String, DeviceEvent> getStateChangeTypeMap() {
        return mStateChangeTypeMap;
    }

    public static String getCurrentTime() {
        String dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getDefault());

        Calendar calendar = Calendar.getInstance();
        return simpleDateFormat.format(calendar.getTime());
    }
}
