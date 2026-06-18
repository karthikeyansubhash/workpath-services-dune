
package com.hp.ws.cdm.system;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * resource to retrieve system wise stat
 * 
 */
public class Statistics {

    /**
     * Available memory - in KB
     * 
     */
    @SerializedName("availableMemory")
    @Expose
    private Integer availableMemory;
    /**
     * Number of System Reboots
     * 
     */
    @SerializedName("powerCycleCount")
    @Expose
    private Integer powerCycleCount;
    /**
     * Total memory - in KB
     * 
     */
    @SerializedName("totalMemory")
    @Expose
    private Integer totalMemory;
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
     * Available memory - in KB
     * 
     */
    public Integer getAvailableMemory() {
        return availableMemory;
    }

    /**
     * Available memory - in KB
     * 
     */
    public void setAvailableMemory(Integer availableMemory) {
        this.availableMemory = availableMemory;
    }

    /**
     * Number of System Reboots
     * 
     */
    public Integer getPowerCycleCount() {
        return powerCycleCount;
    }

    /**
     * Number of System Reboots
     * 
     */
    public void setPowerCycleCount(Integer powerCycleCount) {
        this.powerCycleCount = powerCycleCount;
    }

    /**
     * Total memory - in KB
     * 
     */
    public Integer getTotalMemory() {
        return totalMemory;
    }

    /**
     * Total memory - in KB
     * 
     */
    public void setTotalMemory(Integer totalMemory) {
        this.totalMemory = totalMemory;
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

}
