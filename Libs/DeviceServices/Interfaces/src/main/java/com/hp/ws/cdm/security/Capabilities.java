
package com.hp.ws.cdm.security;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Capabilities {

    /**
     * Contains the Secure Boot feature details
     * 
     */
    @SerializedName("secureBootSettings")
    @Expose
    private SecureBootSettings secureBootSettings;
    /**
     * Secure Print device capabilities
     * 
     */
    @SerializedName("securePrint")
    @Expose
    private SecurePrint securePrint;
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
     * Contains the Secure Boot feature details
     * 
     */
    public SecureBootSettings getSecureBootSettings() {
        return secureBootSettings;
    }

    /**
     * Contains the Secure Boot feature details
     * 
     */
    public void setSecureBootSettings(SecureBootSettings secureBootSettings) {
        this.secureBootSettings = secureBootSettings;
    }

    /**
     * Secure Print device capabilities
     * 
     */
    public SecurePrint getSecurePrint() {
        return securePrint;
    }

    /**
     * Secure Print device capabilities
     * 
     */
    public void setSecurePrint(SecurePrint securePrint) {
        this.securePrint = securePrint;
    }

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

}
