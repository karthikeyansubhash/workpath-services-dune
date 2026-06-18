
package com.hp.ws.cdm.alert;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * List of alerts in order of priority/severity/sequence number
 * 
 */
public class Alerts {

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
     * A list of alerts in order of severity, priority and sequence
     * (Required)
     * 
     */
    @SerializedName("alerts")
    @Expose
    private List<Alert> alerts = new ArrayList<Alert>();

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
     * A list of alerts in order of severity, priority and sequence
     * (Required)
     * 
     */
    public List<Alert> getAlerts() {
        return alerts;
    }

    /**
     * A list of alerts in order of severity, priority and sequence
     * (Required)
     * 
     */
    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

}
