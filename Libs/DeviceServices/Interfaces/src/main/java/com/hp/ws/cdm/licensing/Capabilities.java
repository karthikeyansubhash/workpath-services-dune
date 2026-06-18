
package com.hp.ws.cdm.licensing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    @SerializedName("maxLicensesSupported")
    @Expose
    private Integer maxLicensesSupported;

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

    public Integer getMaxLicensesSupported() {
        return maxLicensesSupported;
    }

    public void setMaxLicensesSupported(Integer maxLicensesSupported) {
        this.maxLicensesSupported = maxLicensesSupported;
    }

}
