
package com.hp.ws.cdm.controlpanel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Com_hp_cdm_service_controlPanel_version_1_schema {

    @SerializedName("capabilities")
    @Expose
    private Capabilities capabilities;
    @SerializedName("configuration")
    @Expose
    private Configuration configuration;
    @SerializedName("homeScreen")
    @Expose
    private HomeScreen homeScreen;

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

    public HomeScreen getHomeScreen() {
        return homeScreen;
    }

    public void setHomeScreen(HomeScreen homeScreen) {
        this.homeScreen = homeScreen;
    }

}
