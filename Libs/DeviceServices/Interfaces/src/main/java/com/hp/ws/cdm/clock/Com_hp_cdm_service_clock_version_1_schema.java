
package com.hp.ws.cdm.clock;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * clock service schema definitions
 * 
 */
public class Com_hp_cdm_service_clock_version_1_schema {

    @SerializedName("configuration")
    @Expose
    private SupportedTimeZones configuration;
    /**
     * Resource to access the realtime clock configuration
     * 
     */
    @SerializedName("capabilities")
    @Expose
    private Configuration capabilities;
    @SerializedName("ntpServerDiscovery")
    @Expose
    private Capabilities ntpServerDiscovery;
    @SerializedName("supportedTimeZones")
    @Expose
    private NtpServerDiscovery supportedTimeZones;

    public SupportedTimeZones getConfiguration() {
        return configuration;
    }

    public void setConfiguration(SupportedTimeZones configuration) {
        this.configuration = configuration;
    }

    /**
     * Resource to access the realtime clock configuration
     * 
     */
    public Configuration getCapabilities() {
        return capabilities;
    }

    /**
     * Resource to access the realtime clock configuration
     * 
     */
    public void setCapabilities(Configuration capabilities) {
        this.capabilities = capabilities;
    }

    public Capabilities getNtpServerDiscovery() {
        return ntpServerDiscovery;
    }

    public void setNtpServerDiscovery(Capabilities ntpServerDiscovery) {
        this.ntpServerDiscovery = ntpServerDiscovery;
    }

    public NtpServerDiscovery getSupportedTimeZones() {
        return supportedTimeZones;
    }

    public void setSupportedTimeZones(NtpServerDiscovery supportedTimeZones) {
        this.supportedTimeZones = supportedTimeZones;
    }

}
