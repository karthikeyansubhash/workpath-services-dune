
package com.hp.ws.cdm.system;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Adding supportContact, moving to 1.3.0-alpha.1
 * 
 */
public class Com_hp_cdm_service_system_version_1_schema {

    /**
     * resource to retrieve the device's identity information
     * 
     */
    @SerializedName("identity")
    @Expose
    private Identity identity;
    /**
     * resource to retrieve the device's configuration
     * 
     */
    @SerializedName("configuration")
    @Expose
    private Configuration configuration;
    /**
     * resource to retrieve the device's current status
     * 
     */
    @SerializedName("status")
    @Expose
    private Status status;
    /**
     * resource to retrieve the device's images
     * 
     */
    @SerializedName("images")
    @Expose
    private Images images;
    /**
     * resource for service configuration
     * 
     */
    @SerializedName("serviceConfig")
    @Expose
    private ServiceConfig serviceConfig;
    /**
     * resource to retrieve system wise stat
     * 
     */
    @SerializedName("statistics")
    @Expose
    private Statistics statistics;

    /**
     * resource to retrieve the device's identity information
     * 
     */
    public Identity getIdentity() {
        return identity;
    }

    /**
     * resource to retrieve the device's identity information
     * 
     */
    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    /**
     * resource to retrieve the device's configuration
     * 
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * resource to retrieve the device's configuration
     * 
     */
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * resource to retrieve the device's current status
     * 
     */
    public Status getStatus() {
        return status;
    }

    /**
     * resource to retrieve the device's current status
     * 
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * resource to retrieve the device's images
     * 
     */
    public Images getImages() {
        return images;
    }

    /**
     * resource to retrieve the device's images
     * 
     */
    public void setImages(Images images) {
        this.images = images;
    }

    /**
     * resource for service configuration
     * 
     */
    public ServiceConfig getServiceConfig() {
        return serviceConfig;
    }

    /**
     * resource for service configuration
     * 
     */
    public void setServiceConfig(ServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }

    /**
     * resource to retrieve system wise stat
     * 
     */
    public Statistics getStatistics() {
        return statistics;
    }

    /**
     * resource to retrieve system wise stat
     * 
     */
    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

}
