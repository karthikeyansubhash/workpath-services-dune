
package com.hp.ws.cdm.jobticket;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class Notification {

    @SerializedName("notificationCondition")
    @Expose
    private NotificationType notificationCondition;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("includeThumbnailOfFirstPageEnabled")
    @Expose
    private Property.FeatureEnabled includeThumbnailOfFirstPageEnabled;
    /**
     * The job completion or failure can be notified via email address. RFC5322 is followed.
     * 
     */
    @SerializedName("notificationAddress")
    @Expose
    private String notificationAddress;
    /**
     * Job notifications delivery method used, for all job types.
     * 
     */
    @SerializedName("notificationMode")
    @Expose
    private NotificationMode notificationMode;

    public NotificationType getNotificationCondition() {
        return notificationCondition;
    }

    public void setNotificationCondition(NotificationType notificationCondition) {
        this.notificationCondition = notificationCondition;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getIncludeThumbnailOfFirstPageEnabled() {
        return includeThumbnailOfFirstPageEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIncludeThumbnailOfFirstPageEnabled(Property.FeatureEnabled includeThumbnailOfFirstPageEnabled) {
        this.includeThumbnailOfFirstPageEnabled = includeThumbnailOfFirstPageEnabled;
    }

    /**
     * The job completion or failure can be notified via email address. RFC5322 is followed.
     * 
     */
    public String getNotificationAddress() {
        return notificationAddress;
    }

    /**
     * The job completion or failure can be notified via email address. RFC5322 is followed.
     * 
     */
    public void setNotificationAddress(String notificationAddress) {
        this.notificationAddress = notificationAddress;
    }

    /**
     * Job notifications delivery method used, for all job types.
     * 
     */
    public NotificationMode getNotificationMode() {
        return notificationMode;
    }

    /**
     * Job notifications delivery method used, for all job types.
     * 
     */
    public void setNotificationMode(NotificationMode notificationMode) {
        this.notificationMode = notificationMode;
    }


    /**
     * Job notifications delivery method used, for all job types.
     * 
     */
    public enum NotificationMode {

        @SerializedName("print")
        PRINT("print"),
        @SerializedName("email")
        EMAIL("email");
        private final String value;
        private final static Map<String, NotificationMode> CONSTANTS = new HashMap<String, NotificationMode>();

        static {
            for (NotificationMode c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        NotificationMode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static NotificationMode fromValue(String value) {
            NotificationMode constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum NotificationType {

        @SerializedName("never")
        NEVER("never"),
        @SerializedName("onJobCompleted")
        ON_JOB_COMPLETED("onJobCompleted"),
        @SerializedName("onJobFailed")
        ON_JOB_FAILED("onJobFailed");
        private final String value;
        private final static Map<String, NotificationType> CONSTANTS = new HashMap<String, NotificationType>();

        static {
            for (NotificationType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        NotificationType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static NotificationType fromValue(String value) {
            NotificationType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
