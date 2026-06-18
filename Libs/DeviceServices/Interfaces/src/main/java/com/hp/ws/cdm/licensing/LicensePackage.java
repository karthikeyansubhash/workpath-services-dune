
package com.hp.ws.cdm.licensing;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LicensePackage {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("singleUseToken")
    @Expose
    private String singleUseToken;
    @SerializedName("jwsLicenses")
    @Expose
    private List<String> jwsLicenses = new ArrayList<String>();

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

    public String getSingleUseToken() {
        return singleUseToken;
    }

    public void setSingleUseToken(String singleUseToken) {
        this.singleUseToken = singleUseToken;
    }

    public List<String> getJwsLicenses() {
        return jwsLicenses;
    }

    public void setJwsLicenses(List<String> jwsLicenses) {
        this.jwsLicenses = jwsLicenses;
    }

}
