
package com.hp.ws.cdm.pubsub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceReportedOnDemand {

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
     * indicates if there are more reports to follow
     * 
     */
    @SerializedName("continuingReports")
    @Expose
    private DeviceReportedOnDemand.ContinuingReports continuingReports = DeviceReportedOnDemand.ContinuingReports.fromValue("true");
    @SerializedName("reports")
    @Expose
    private List<Message> reports = new ArrayList<Message>();

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
     * indicates if there are more reports to follow
     * 
     */
    public DeviceReportedOnDemand.ContinuingReports getContinuingReports() {
        return continuingReports;
    }

    /**
     * indicates if there are more reports to follow
     * 
     */
    public void setContinuingReports(DeviceReportedOnDemand.ContinuingReports continuingReports) {
        this.continuingReports = continuingReports;
    }

    public List<Message> getReports() {
        return reports;
    }

    public void setReports(List<Message> reports) {
        this.reports = reports;
    }


    /**
     * indicates if there are more reports to follow
     * 
     */
    public enum ContinuingReports {

        @SerializedName("true")
        TRUE("true"),
        @SerializedName("false")
        FALSE("false");
        private final String value;
        private final static Map<String, DeviceReportedOnDemand.ContinuingReports> CONSTANTS = new HashMap<String, DeviceReportedOnDemand.ContinuingReports>();

        static {
            for (DeviceReportedOnDemand.ContinuingReports c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ContinuingReports(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static DeviceReportedOnDemand.ContinuingReports fromValue(String value) {
            DeviceReportedOnDemand.ContinuingReports constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
