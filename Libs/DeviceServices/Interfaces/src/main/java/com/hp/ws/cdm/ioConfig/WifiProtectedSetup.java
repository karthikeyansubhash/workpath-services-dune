
package com.hp.ws.cdm.ioconfig;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Wifi Protected Setup (WPS) session info
 * 
 */
public class WifiProtectedSetup {

    /**
     * The Basic Service Set Identifier(SSID) of the wireless network (LAN)
     * 
     */
    @SerializedName("bssid")
    @Expose
    private String bssid = "";
    /**
     * Result of last WPS session
     * 
     */
    @SerializedName("lastResult")
    @Expose
    private WifiProtectedSetup.LastResult lastResult;
    /**
     * The time stamp of last result
     * 
     */
    @SerializedName("resultTimeStamp")
    @Expose
    private Float resultTimeStamp;
    /**
     * Session timeout in seconds
     * 
     */
    @SerializedName("sessionTimeout")
    @Expose
    private Integer sessionTimeout;
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
     *  8 digit PIN to be used for next WPS session
     * 
     */
    @SerializedName("wpsPin")
    @Expose
    private String wpsPin;
    /**
     * State of Wi-Fi Protected Setup
     * 
     */
    @SerializedName("wpsState")
    @Expose
    private WifiProtectedSetup.WpsState wpsState;
    /**
     * The type of Wi-Fi Protected Setup
     * (Required)
     * 
     */
    @SerializedName("wpsType")
    @Expose
    private WifiProtectedSetup.WpsType wpsType;

    /**
     * The Basic Service Set Identifier(SSID) of the wireless network (LAN)
     * 
     */
    public String getBssid() {
        return bssid;
    }

    /**
     * The Basic Service Set Identifier(SSID) of the wireless network (LAN)
     * 
     */
    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    /**
     * Result of last WPS session
     * 
     */
    public WifiProtectedSetup.LastResult getLastResult() {
        return lastResult;
    }

    /**
     * Result of last WPS session
     * 
     */
    public void setLastResult(WifiProtectedSetup.LastResult lastResult) {
        this.lastResult = lastResult;
    }

    /**
     * The time stamp of last result
     * 
     */
    public Float getResultTimeStamp() {
        return resultTimeStamp;
    }

    /**
     * The time stamp of last result
     * 
     */
    public void setResultTimeStamp(Float resultTimeStamp) {
        this.resultTimeStamp = resultTimeStamp;
    }

    /**
     * Session timeout in seconds
     * 
     */
    public Integer getSessionTimeout() {
        return sessionTimeout;
    }

    /**
     * Session timeout in seconds
     * 
     */
    public void setSessionTimeout(Integer sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

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
     *  8 digit PIN to be used for next WPS session
     * 
     */
    public String getWpsPin() {
        return wpsPin;
    }

    /**
     *  8 digit PIN to be used for next WPS session
     * 
     */
    public void setWpsPin(String wpsPin) {
        this.wpsPin = wpsPin;
    }

    /**
     * State of Wi-Fi Protected Setup
     * 
     */
    public WifiProtectedSetup.WpsState getWpsState() {
        return wpsState;
    }

    /**
     * State of Wi-Fi Protected Setup
     * 
     */
    public void setWpsState(WifiProtectedSetup.WpsState wpsState) {
        this.wpsState = wpsState;
    }

    /**
     * The type of Wi-Fi Protected Setup
     * (Required)
     * 
     */
    public WifiProtectedSetup.WpsType getWpsType() {
        return wpsType;
    }

    /**
     * The type of Wi-Fi Protected Setup
     * (Required)
     * 
     */
    public void setWpsType(WifiProtectedSetup.WpsType wpsType) {
        this.wpsType = wpsType;
    }


    /**
     * Result of last WPS session
     * 
     */
    public enum LastResult {

        @SerializedName("success")
        SUCCESS("success"),
        @SerializedName("cancelled")
        CANCELLED("cancelled"),
        @SerializedName("timeout")
        TIMEOUT("timeout"),
        @SerializedName("error")
        ERROR("error");
        private final String value;
        private final static Map<String, WifiProtectedSetup.LastResult> CONSTANTS = new HashMap<String, WifiProtectedSetup.LastResult>();

        static {
            for (WifiProtectedSetup.LastResult c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        LastResult(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static WifiProtectedSetup.LastResult fromValue(String value) {
            WifiProtectedSetup.LastResult constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * State of Wi-Fi Protected Setup
     * 
     */
    public enum WpsState {

        @SerializedName("ready")
        READY("ready"),
        @SerializedName("scanProcessing")
        SCAN_PROCESSING("scanProcessing"),
        @SerializedName("wpsInProgress")
        WPS_IN_PROGRESS("wpsInProgress"),
        @SerializedName("cancelProcessing")
        CANCEL_PROCESSING("cancelProcessing");
        private final String value;
        private final static Map<String, WifiProtectedSetup.WpsState> CONSTANTS = new HashMap<String, WifiProtectedSetup.WpsState>();

        static {
            for (WifiProtectedSetup.WpsState c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        WpsState(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static WifiProtectedSetup.WpsState fromValue(String value) {
            WifiProtectedSetup.WpsState constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * The type of Wi-Fi Protected Setup
     * 
     */
    public enum WpsType {

        @SerializedName("pushButton")
        PUSH_BUTTON("pushButton"),
        @SerializedName("pin")
        PIN("pin");
        private final String value;
        private final static Map<String, WifiProtectedSetup.WpsType> CONSTANTS = new HashMap<String, WifiProtectedSetup.WpsType>();

        static {
            for (WifiProtectedSetup.WpsType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        WpsType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static WifiProtectedSetup.WpsType fromValue(String value) {
            WifiProtectedSetup.WpsType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
