
package com.hp.ws.cdm.usbhost;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * USB host capabilities
 * 
 */
public class Capabilities {

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
     * USB ports in the device
     * 
     */
    @SerializedName("usbPorts")
    @Expose
    private List<UsbPort> usbPorts = new ArrayList<UsbPort>();

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
     * USB ports in the device
     * 
     */
    public List<UsbPort> getUsbPorts() {
        return usbPorts;
    }

    /**
     * USB ports in the device
     * 
     */
    public void setUsbPorts(List<UsbPort> usbPorts) {
        this.usbPorts = usbPorts;
    }

}
