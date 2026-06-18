
package com.hp.ws.cdm.pubsub;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Com_hp_cdm_service_pubsub_version_2_schema {

    @SerializedName("deviceReported")
    @Expose
    private DeviceReported deviceReported;
    @SerializedName("cloudDesired")
    @Expose
    private CloudDesired cloudDesired;
    /**
     * pubsub capabilities
     * <p>
     * List of resources which support eventing
     * 
     */
    @SerializedName("capabilities")
    @Expose
    private Capabilities capabilities;
    @SerializedName("subscriptions")
    @Expose
    private Subscriptions subscriptions;
    @SerializedName("deviceReportedOnDemand")
    @Expose
    private DeviceReportedOnDemand deviceReportedOnDemand;

    public DeviceReported getDeviceReported() {
        return deviceReported;
    }

    public void setDeviceReported(DeviceReported deviceReported) {
        this.deviceReported = deviceReported;
    }

    public CloudDesired getCloudDesired() {
        return cloudDesired;
    }

    public void setCloudDesired(CloudDesired cloudDesired) {
        this.cloudDesired = cloudDesired;
    }

    /**
     * pubsub capabilities
     * <p>
     * List of resources which support eventing
     * 
     */
    public Capabilities getCapabilities() {
        return capabilities;
    }

    /**
     * pubsub capabilities
     * <p>
     * List of resources which support eventing
     * 
     */
    public void setCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    public Subscriptions getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Subscriptions subscriptions) {
        this.subscriptions = subscriptions;
    }

    public DeviceReportedOnDemand getDeviceReportedOnDemand() {
        return deviceReportedOnDemand;
    }

    public void setDeviceReportedOnDemand(DeviceReportedOnDemand deviceReportedOnDemand) {
        this.deviceReportedOnDemand = deviceReportedOnDemand;
    }

}
