
package com.hp.ws.cdm.certificate;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Certificates {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("certificates")
    @Expose
    private List<CertificateInfo> certificates = new ArrayList<CertificateInfo>();

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

    public List<CertificateInfo> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<CertificateInfo> certificates) {
        this.certificates = certificates;
    }

}
