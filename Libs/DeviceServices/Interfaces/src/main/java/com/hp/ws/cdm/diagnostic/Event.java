
package com.hp.ws.cdm.diagnostic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * system event
 * 
 */
public class Event {

    /**
     * unique identifier assigned to each system event instance
     * 
     */
    @SerializedName("id")
    @Expose
    private Long id;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("eventCode")
    @Expose
    private String eventCode;
    @SerializedName("eventData")
    @Expose
    private List<KeyValuePair> eventData = new ArrayList<KeyValuePair>();
    @SerializedName("eventReason")
    @Expose
    private Event.EventReason eventReason;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("firmwareVersion")
    @Expose
    private String firmwareVersion;
    /**
     * DeviceSetup(OOBE) state of the printer at the time when the event was triggered
     * 
     */
    @SerializedName("setupState")
    @Expose
    private Event.SetupState setupState;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("severity")
    @Expose
    private Event.Severity severity;
    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * (Required)
     * 
     */
    @SerializedName("timeStamp")
    @Expose
    private Date timeStamp;
    /**
     * the engine cycle count when the event occurred
     * 
     */
    @SerializedName("engineCycleCount")
    @Expose
    private Long engineCycleCount;
    /**
     * sequential number assigned to each generated system event (by severity/storage category).
     * 
     */
    @SerializedName("ordinalIndex")
    @Expose
    private Long ordinalIndex;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("alertGenerated")
    @Expose
    private Property.FeatureEnabled alertGenerated;
    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();

    /**
     * unique identifier assigned to each system event instance
     * 
     */
    public Long getId() {
        return id;
    }

    /**
     * unique identifier assigned to each system event instance
     * 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getEventCode() {
        return eventCode;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public List<KeyValuePair> getEventData() {
        return eventData;
    }

    public void setEventData(List<KeyValuePair> eventData) {
        this.eventData = eventData;
    }

    public Event.EventReason getEventReason() {
        return eventReason;
    }

    public void setEventReason(Event.EventReason eventReason) {
        this.eventReason = eventReason;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    /**
     * DeviceSetup(OOBE) state of the printer at the time when the event was triggered
     * 
     */
    public Event.SetupState getSetupState() {
        return setupState;
    }

    /**
     * DeviceSetup(OOBE) state of the printer at the time when the event was triggered
     * 
     */
    public void setSetupState(Event.SetupState setupState) {
        this.setupState = setupState;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Event.Severity getSeverity() {
        return severity;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setSeverity(Event.Severity severity) {
        this.severity = severity;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * (Required)
     * 
     */
    public Date getTimeStamp() {
        return timeStamp;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * (Required)
     * 
     */
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * the engine cycle count when the event occurred
     * 
     */
    public Long getEngineCycleCount() {
        return engineCycleCount;
    }

    /**
     * the engine cycle count when the event occurred
     * 
     */
    public void setEngineCycleCount(Long engineCycleCount) {
        this.engineCycleCount = engineCycleCount;
    }

    /**
     * sequential number assigned to each generated system event (by severity/storage category).
     * 
     */
    public Long getOrdinalIndex() {
        return ordinalIndex;
    }

    /**
     * sequential number assigned to each generated system event (by severity/storage category).
     * 
     */
    public void setOrdinalIndex(Long ordinalIndex) {
        this.ordinalIndex = ordinalIndex;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getAlertGenerated() {
        return alertGenerated;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setAlertGenerated(Property.FeatureEnabled alertGenerated) {
        this.alertGenerated = alertGenerated;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    public void setVersion(String version) {
        this.version = version;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public enum EventReason {

        @SerializedName("assert")
        ASSERT("assert"),
        @SerializedName("other")
        OTHER("other");
        private final String value;
        private final static Map<String, Event.EventReason> CONSTANTS = new HashMap<String, Event.EventReason>();

        static {
            for (Event.EventReason c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        EventReason(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Event.EventReason fromValue(String value) {
            Event.EventReason constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * DeviceSetup(OOBE) state of the printer at the time when the event was triggered
     * 
     */
    public enum SetupState {

        @SerializedName("pending")
        PENDING("pending"),
        @SerializedName("inProgress")
        IN_PROGRESS("inProgress"),
        @SerializedName("completed")
        COMPLETED("completed"),
        @SerializedName("unknown")
        UNKNOWN("unknown");
        private final String value;
        private final static Map<String, Event.SetupState> CONSTANTS = new HashMap<String, Event.SetupState>();

        static {
            for (Event.SetupState c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        SetupState(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Event.SetupState fromValue(String value) {
            Event.SetupState constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum Severity {

        @SerializedName("info")
        INFO("info"),
        @SerializedName("warning")
        WARNING("warning"),
        @SerializedName("error")
        ERROR("error"),
        @SerializedName("critical")
        CRITICAL("critical");
        private final String value;
        private final static Map<String, Event.Severity> CONSTANTS = new HashMap<String, Event.Severity>();

        static {
            for (Event.Severity c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Severity(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Event.Severity fromValue(String value) {
            Event.Severity constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
