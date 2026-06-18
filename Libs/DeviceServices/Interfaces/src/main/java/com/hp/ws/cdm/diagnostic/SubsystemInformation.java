
package com.hp.ws.cdm.diagnostic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Describes a subsystem
 * 
 */
public class SubsystemInformation {

    /**
     * Unsigned integer to sort the values of the subsystems
     * 
     */
    @SerializedName("subsystemIndex")
    @Expose
    private Long subsystemIndex;
    /**
     * Enumeration to identify which subsystem is testing or refers to
     * (Required)
     * 
     */
    @SerializedName("subsystem")
    @Expose
    private com.hp.ws.cdm.diagnostic.TestInformation.Subsystem subsystem;
    /**
     * Optional textual information in English
     * 
     */
    @SerializedName("subsystemDescription")
    @Expose
    private String subsystemDescription;

    /**
     * Unsigned integer to sort the values of the subsystems
     * 
     */
    public Long getSubsystemIndex() {
        return subsystemIndex;
    }

    /**
     * Unsigned integer to sort the values of the subsystems
     * 
     */
    public void setSubsystemIndex(Long subsystemIndex) {
        this.subsystemIndex = subsystemIndex;
    }

    /**
     * Enumeration to identify which subsystem is testing or refers to
     * (Required)
     * 
     */
    public com.hp.ws.cdm.diagnostic.TestInformation.Subsystem getSubsystem() {
        return subsystem;
    }

    /**
     * Enumeration to identify which subsystem is testing or refers to
     * (Required)
     * 
     */
    public void setSubsystem(com.hp.ws.cdm.diagnostic.TestInformation.Subsystem subsystem) {
        this.subsystem = subsystem;
    }

    /**
     * Optional textual information in English
     * 
     */
    public String getSubsystemDescription() {
        return subsystemDescription;
    }

    /**
     * Optional textual information in English
     * 
     */
    public void setSubsystemDescription(String subsystemDescription) {
        this.subsystemDescription = subsystemDescription;
    }

}
