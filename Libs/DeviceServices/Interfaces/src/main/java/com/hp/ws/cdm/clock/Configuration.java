
package com.hp.ws.cdm.clock;

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
 * Resource to access the realtime clock configuration
 * 
 */
public class Configuration {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    @SerializedName("systemTime")
    @Expose
    private Date systemTime;
    @SerializedName("systemTimeAuthority")
    @Expose
    private Configuration.SystemTimeAuthority systemTimeAuthority;
    @SerializedName("systemTimeSync")
    @Expose
    private Configuration.SystemTimeSync systemTimeSync;
    @SerializedName("ntpServer")
    @Expose
    private String ntpServer;
    @SerializedName("ntpLocalPortNumber")
    @Expose
    private Integer ntpLocalPortNumber;
    @SerializedName("ntpSyncFrequency")
    @Expose
    private Integer ntpSyncFrequency;
    @SerializedName("timeFormat")
    @Expose
    private Configuration.TimeFormat timeFormat;
    @SerializedName("dateFormat")
    @Expose
    private Configuration.DateFormat dateFormat;
    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    @SerializedName("localTime")
    @Expose
    private Date localTime;
    @SerializedName("timeZone")
    @Expose
    private String timeZone;
    @SerializedName("timeZoneDescription")
    @Expose
    private String timeZoneDescription;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("dstEnabled")
    @Expose
    private Property.FeatureEnabled dstEnabled;
    /**
     * Time localization settings (timezone & dst behavior)
     * 
     */
    @SerializedName("customTimeZone")
    @Expose
    private CustomTimeZone customTimeZone;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public Date getSystemTime() {
        return systemTime;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public void setSystemTime(Date systemTime) {
        this.systemTime = systemTime;
    }

    public Configuration.SystemTimeAuthority getSystemTimeAuthority() {
        return systemTimeAuthority;
    }

    public void setSystemTimeAuthority(Configuration.SystemTimeAuthority systemTimeAuthority) {
        this.systemTimeAuthority = systemTimeAuthority;
    }

    public Configuration.SystemTimeSync getSystemTimeSync() {
        return systemTimeSync;
    }

    public void setSystemTimeSync(Configuration.SystemTimeSync systemTimeSync) {
        this.systemTimeSync = systemTimeSync;
    }

    public String getNtpServer() {
        return ntpServer;
    }

    public void setNtpServer(String ntpServer) {
        this.ntpServer = ntpServer;
    }

    public Integer getNtpLocalPortNumber() {
        return ntpLocalPortNumber;
    }

    public void setNtpLocalPortNumber(Integer ntpLocalPortNumber) {
        this.ntpLocalPortNumber = ntpLocalPortNumber;
    }

    public Integer getNtpSyncFrequency() {
        return ntpSyncFrequency;
    }

    public void setNtpSyncFrequency(Integer ntpSyncFrequency) {
        this.ntpSyncFrequency = ntpSyncFrequency;
    }

    public Configuration.TimeFormat getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(Configuration.TimeFormat timeFormat) {
        this.timeFormat = timeFormat;
    }

    public Configuration.DateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(Configuration.DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public Date getLocalTime() {
        return localTime;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public void setLocalTime(Date localTime) {
        this.localTime = localTime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getTimeZoneDescription() {
        return timeZoneDescription;
    }

    public void setTimeZoneDescription(String timeZoneDescription) {
        this.timeZoneDescription = timeZoneDescription;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getDstEnabled() {
        return dstEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setDstEnabled(Property.FeatureEnabled dstEnabled) {
        this.dstEnabled = dstEnabled;
    }

    /**
     * Time localization settings (timezone & dst behavior)
     * 
     */
    public CustomTimeZone getCustomTimeZone() {
        return customTimeZone;
    }

    /**
     * Time localization settings (timezone & dst behavior)
     * 
     */
    public void setCustomTimeZone(CustomTimeZone customTimeZone) {
        this.customTimeZone = customTimeZone;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public enum DateFormat {

        @SerializedName("ddmmmyyyy")
        DDMMMYYYY("ddmmmyyyy"),
        @SerializedName("mmmddyyyy")
        MMMDDYYYY("mmmddyyyy"),
        @SerializedName("yyyymmmdd")
        YYYYMMMDD("yyyymmmdd");
        private final String value;
        private final static Map<String, Configuration.DateFormat> CONSTANTS = new HashMap<String, Configuration.DateFormat>();

        static {
            for (Configuration.DateFormat c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        DateFormat(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Configuration.DateFormat fromValue(String value) {
            Configuration.DateFormat constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum SystemTimeAuthority {

        @SerializedName("none")
        NONE("none"),
        @SerializedName("historical")
        HISTORICAL("historical"),
        @SerializedName("clientSw")
        CLIENT_SW("clientSw"),
        @SerializedName("dhcp")
        DHCP("dhcp"),
        @SerializedName("factoryRtc")
        FACTORY_RTC("factoryRtc"),
        @SerializedName("hpCloud")
        HP_CLOUD("hpCloud"),
        @SerializedName("customerNtp")
        CUSTOMER_NTP("customerNtp"),
        @SerializedName("adminSet")
        ADMIN_SET("adminSet");
        private final String value;
        private final static Map<String, Configuration.SystemTimeAuthority> CONSTANTS = new HashMap<String, Configuration.SystemTimeAuthority>();

        static {
            for (Configuration.SystemTimeAuthority c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        SystemTimeAuthority(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Configuration.SystemTimeAuthority fromValue(String value) {
            Configuration.SystemTimeAuthority constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum SystemTimeSync {

        @SerializedName("hp")
        HP("hp"),
        @SerializedName("ntp")
        NTP("ntp"),
        @SerializedName("none")
        NONE("none");
        private final String value;
        private final static Map<String, Configuration.SystemTimeSync> CONSTANTS = new HashMap<String, Configuration.SystemTimeSync>();

        static {
            for (Configuration.SystemTimeSync c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        SystemTimeSync(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Configuration.SystemTimeSync fromValue(String value) {
            Configuration.SystemTimeSync constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum TimeFormat {

        @SerializedName("hr12")
        HR_12("hr12"),
        @SerializedName("hr24")
        HR_24("hr24");
        private final String value;
        private final static Map<String, Configuration.TimeFormat> CONSTANTS = new HashMap<String, Configuration.TimeFormat>();

        static {
            for (Configuration.TimeFormat c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TimeFormat(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Configuration.TimeFormat fromValue(String value) {
            Configuration.TimeFormat constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
