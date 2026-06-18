
package com.hp.ws.cdm.storage;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RemovableDevices {

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
     * Array containing removableDevice's 
     * 
     */
    @SerializedName("devices")
    @Expose
    private List<RemovableDevice> devices = new ArrayList<RemovableDevice>();

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
     * Array containing removableDevice's 
     * 
     */
    public List<RemovableDevice> getDevices() {
        return devices;
    }

    /**
     * Array containing removableDevice's 
     * 
     */
    public void setDevices(List<RemovableDevice> devices) {
        this.devices = devices;
    }

}
