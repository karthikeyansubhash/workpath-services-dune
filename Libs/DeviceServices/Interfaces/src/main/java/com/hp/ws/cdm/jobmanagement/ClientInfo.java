
package com.hp.ws.cdm.jobmanagement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * provides submitting client information
 * 
 */
public class ClientInfo {

    /**
     * The name of the application used to create the job
     * 
     */
    @SerializedName("applicationName")
    @Expose
    private String applicationName;
    /**
     * pointer to application version identifier
     * 
     */
    @SerializedName("applicationVersion")
    @Expose
    private String applicationVersion;
    /**
     * Host name of the client submitting the job
     * 
     */
    @SerializedName("hostName")
    @Expose
    private String hostName;
    /**
     * Unique jobId from the perspective of the client sending the job
     * 
     */
    @SerializedName("clientJobId")
    @Expose
    private String clientJobId;
    /**
     * provides pjl job properties for print jobs originating from pjl protocol
     * 
     */
    @SerializedName("driverInfo")
    @Expose
    private DriverInfo driverInfo;

    /**
     * The name of the application used to create the job
     * 
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * The name of the application used to create the job
     * 
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * pointer to application version identifier
     * 
     */
    public String getApplicationVersion() {
        return applicationVersion;
    }

    /**
     * pointer to application version identifier
     * 
     */
    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    /**
     * Host name of the client submitting the job
     * 
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Host name of the client submitting the job
     * 
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * Unique jobId from the perspective of the client sending the job
     * 
     */
    public String getClientJobId() {
        return clientJobId;
    }

    /**
     * Unique jobId from the perspective of the client sending the job
     * 
     */
    public void setClientJobId(String clientJobId) {
        this.clientJobId = clientJobId;
    }

    /**
     * provides pjl job properties for print jobs originating from pjl protocol
     * 
     */
    public DriverInfo getDriverInfo() {
        return driverInfo;
    }

    /**
     * provides pjl job properties for print jobs originating from pjl protocol
     * 
     */
    public void setDriverInfo(DriverInfo driverInfo) {
        this.driverInfo = driverInfo;
    }

}
