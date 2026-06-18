
package com.hp.ws.cdm.ioconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;

public class WifiScan {

    @SerializedName("lastScanResult")
    @Expose
    private WifiScan.LastScanResult lastScanResult;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("scanType")
    @Expose
    private WifiScan.ScanType scanType;
    /**
     * The Service Set Identifier(SSID) aka name of the wireless network (LAN)
     * 
     */
    @SerializedName("ssid")
    @Expose
    private String ssid;
    @SerializedName("state")
    @Expose
    private WifiScan.State state;
    /**
     * The time in seconds since the last scan was completed
     * 
     */
    @SerializedName("timeSinceLastScan")
    @Expose
    private Float timeSinceLastScan;
    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;

    public WifiScan.LastScanResult getLastScanResult() {
        return lastScanResult;
    }

    public void setLastScanResult(WifiScan.LastScanResult lastScanResult) {
        this.lastScanResult = lastScanResult;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * 
     * (Required)
     * 
     */
    public WifiScan.ScanType getScanType() {
        return scanType;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setScanType(WifiScan.ScanType scanType) {
        this.scanType = scanType;
    }

    /**
     * The Service Set Identifier(SSID) aka name of the wireless network (LAN)
     * 
     */
    public String getSsid() {
        return ssid;
    }

    /**
     * The Service Set Identifier(SSID) aka name of the wireless network (LAN)
     * 
     */
    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public WifiScan.State getState() {
        return state;
    }

    public void setState(WifiScan.State state) {
        this.state = state;
    }

    /**
     * The time in seconds since the last scan was completed
     * 
     */
    public Float getTimeSinceLastScan() {
        return timeSinceLastScan;
    }

    /**
     * The time in seconds since the last scan was completed
     * 
     */
    public void setTimeSinceLastScan(Float timeSinceLastScan) {
        this.timeSinceLastScan = timeSinceLastScan;
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

    public enum LastScanResult {

        @SerializedName("completed")
        COMPLETED("completed"),
        @SerializedName("cancelled")
        CANCELLED("cancelled"),
        @SerializedName("timeout")
        TIMEOUT("timeout"),
        @SerializedName("error")
        ERROR("error");
        private final String value;
        private final static Map<String, WifiScan.LastScanResult> CONSTANTS = new HashMap<String, WifiScan.LastScanResult>();

        static {
            for (WifiScan.LastScanResult c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        LastScanResult(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static WifiScan.LastScanResult fromValue(String value) {
            WifiScan.LastScanResult constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum ScanType {

        @SerializedName("undirected")
        UNDIRECTED("undirected"),
        @SerializedName("directed")
        DIRECTED("directed");
        private final String value;
        private final static Map<String, WifiScan.ScanType> CONSTANTS = new HashMap<String, WifiScan.ScanType>();

        static {
            for (WifiScan.ScanType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ScanType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static WifiScan.ScanType fromValue(String value) {
            WifiScan.ScanType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum State {

        @SerializedName("readyToScan")
        READY_TO_SCAN("readyToScan"),
        @SerializedName("scanProcessing")
        SCAN_PROCESSING("scanProcessing"),
        @SerializedName("cancelProcessing")
        CANCEL_PROCESSING("cancelProcessing");
        private final String value;
        private final static Map<String, WifiScan.State> CONSTANTS = new HashMap<String, WifiScan.State>();

        static {
            for (WifiScan.State c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        State(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static WifiScan.State fromValue(String value) {
            WifiScan.State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
