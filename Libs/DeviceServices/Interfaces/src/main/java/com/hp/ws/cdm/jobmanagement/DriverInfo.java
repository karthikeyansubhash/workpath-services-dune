
package com.hp.ws.cdm.jobmanagement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * provides pjl job properties for print jobs originating from pjl protocol
 * 
 */
public class DriverInfo {

    /**
     * It is PJL attribute that captures the encoded string of all the settings used in the driver
     * 
     */
    @SerializedName("driverConfig")
    @Expose
    private String driverConfig;
    /**
     * pointer to driver identifier
     * 
     */
    @SerializedName("driverName")
    @Expose
    private String driverName;
    /**
     * pointer to driver version identifier
     * 
     */
    @SerializedName("driverVersion")
    @Expose
    private String driverVersion;
    /**
     * pointer to operating system identifier where job originated
     * 
     */
    @SerializedName("osName")
    @Expose
    private String osName;
    /**
     * pointer to operating system version where job originated
     * 
     */
    @SerializedName("osVersion")
    @Expose
    private String osVersion;

    /**
     * It is PJL attribute that captures the encoded string of all the settings used in the driver
     * 
     */
    public String getDriverConfig() {
        return driverConfig;
    }

    /**
     * It is PJL attribute that captures the encoded string of all the settings used in the driver
     * 
     */
    public void setDriverConfig(String driverConfig) {
        this.driverConfig = driverConfig;
    }

    /**
     * pointer to driver identifier
     * 
     */
    public String getDriverName() {
        return driverName;
    }

    /**
     * pointer to driver identifier
     * 
     */
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    /**
     * pointer to driver version identifier
     * 
     */
    public String getDriverVersion() {
        return driverVersion;
    }

    /**
     * pointer to driver version identifier
     * 
     */
    public void setDriverVersion(String driverVersion) {
        this.driverVersion = driverVersion;
    }

    /**
     * pointer to operating system identifier where job originated
     * 
     */
    public String getOsName() {
        return osName;
    }

    /**
     * pointer to operating system identifier where job originated
     * 
     */
    public void setOsName(String osName) {
        this.osName = osName;
    }

    /**
     * pointer to operating system version where job originated
     * 
     */
    public String getOsVersion() {
        return osVersion;
    }

    /**
     * pointer to operating system version where job originated
     * 
     */
    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

}
