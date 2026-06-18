
package com.hp.ws.cdm.ioconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class Manual {

    /**
     * IPv6 address details
     * 
     */
    @SerializedName("address")
    @Expose
    private Ipv6Address address;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("enabled")
    @Expose
    private Property.FeatureEnabled enabled;

    /**
     * IPv6 address details
     * 
     */
    public Ipv6Address getAddress() {
        return address;
    }

    /**
     * IPv6 address details
     * 
     */
    public void setAddress(Ipv6Address address) {
        this.address = address;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEnabled() {
        return enabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEnabled(Property.FeatureEnabled enabled) {
        this.enabled = enabled;
    }

}
