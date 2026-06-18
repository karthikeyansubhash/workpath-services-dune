
package com.hp.ws.cdm.tlsconfiguration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Com_hp_cdm_service_tlsConfig_version_2_schema {

    @SerializedName("capabilities")
    @Expose
    private Capabilities capabilities;
    @SerializedName("configuration")
    @Expose
    private Configuration configuration;

    public Capabilities getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

}
