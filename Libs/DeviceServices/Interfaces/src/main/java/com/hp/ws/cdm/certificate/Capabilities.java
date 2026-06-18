
package com.hp.ws.cdm.certificate;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

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
    @SerializedName("supportedKeyTypesToStrengths")
    @Expose
    private List<KeyInfoCapabilities> supportedKeyTypesToStrengths = new ArrayList<KeyInfoCapabilities>();
    @SerializedName("supportedCertificateUsages")
    @Expose
    private List<CertificateUsage> supportedCertificateUsages = new ArrayList<CertificateUsage>();
    @SerializedName("supportedCertificateFormatForImportId")
    @Expose
    private List<CertificateFormat> supportedCertificateFormatForImportId = new ArrayList<CertificateFormat>();
    @SerializedName("supportedCertificateFormatForInstallId")
    @Expose
    private List<CertificateFormat> supportedCertificateFormatForInstallId = new ArrayList<CertificateFormat>();
    @SerializedName("supportedCertificateFormatForImportCa")
    @Expose
    private List<CertificateFormat> supportedCertificateFormatForImportCa = new ArrayList<CertificateFormat>();
    @SerializedName("supportedCertificateFormatForExportId")
    @Expose
    private List<CertificateFormat> supportedCertificateFormatForExportId = new ArrayList<CertificateFormat>();
    @SerializedName("supportedCertificateFormatForExportCa")
    @Expose
    private List<CertificateFormat> supportedCertificateFormatForExportCa = new ArrayList<CertificateFormat>();
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("supportsMultipleCa")
    @Expose
    private Property.FeatureEnabled supportsMultipleCa;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("supportsMultipleId")
    @Expose
    private Property.FeatureEnabled supportsMultipleId;
    /**
     * Maximum File Size for a Certificate that is allowed on a device
     * 
     */
    @SerializedName("maxCertFileSizeAllowed")
    @Expose
    private Integer maxCertFileSizeAllowed;
    /**
     * Maximum Validity (in number of days) for a cert that is allowed on a device
     * 
     */
    @SerializedName("maxValidityDate")
    @Expose
    private Integer maxValidityDate;
    /**
     * Maximum Timeout (in seconds) for a Certificate Createion that is allowed on a device
     * 
     */
    @SerializedName("maxCertCreationTimeout")
    @Expose
    private Integer maxCertCreationTimeout;

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

    public List<KeyInfoCapabilities> getSupportedKeyTypesToStrengths() {
        return supportedKeyTypesToStrengths;
    }

    public void setSupportedKeyTypesToStrengths(List<KeyInfoCapabilities> supportedKeyTypesToStrengths) {
        this.supportedKeyTypesToStrengths = supportedKeyTypesToStrengths;
    }

    public List<CertificateUsage> getSupportedCertificateUsages() {
        return supportedCertificateUsages;
    }

    public void setSupportedCertificateUsages(List<CertificateUsage> supportedCertificateUsages) {
        this.supportedCertificateUsages = supportedCertificateUsages;
    }

    public List<CertificateFormat> getSupportedCertificateFormatForImportId() {
        return supportedCertificateFormatForImportId;
    }

    public void setSupportedCertificateFormatForImportId(List<CertificateFormat> supportedCertificateFormatForImportId) {
        this.supportedCertificateFormatForImportId = supportedCertificateFormatForImportId;
    }

    public List<CertificateFormat> getSupportedCertificateFormatForInstallId() {
        return supportedCertificateFormatForInstallId;
    }

    public void setSupportedCertificateFormatForInstallId(List<CertificateFormat> supportedCertificateFormatForInstallId) {
        this.supportedCertificateFormatForInstallId = supportedCertificateFormatForInstallId;
    }

    public List<CertificateFormat> getSupportedCertificateFormatForImportCa() {
        return supportedCertificateFormatForImportCa;
    }

    public void setSupportedCertificateFormatForImportCa(List<CertificateFormat> supportedCertificateFormatForImportCa) {
        this.supportedCertificateFormatForImportCa = supportedCertificateFormatForImportCa;
    }

    public List<CertificateFormat> getSupportedCertificateFormatForExportId() {
        return supportedCertificateFormatForExportId;
    }

    public void setSupportedCertificateFormatForExportId(List<CertificateFormat> supportedCertificateFormatForExportId) {
        this.supportedCertificateFormatForExportId = supportedCertificateFormatForExportId;
    }

    public List<CertificateFormat> getSupportedCertificateFormatForExportCa() {
        return supportedCertificateFormatForExportCa;
    }

    public void setSupportedCertificateFormatForExportCa(List<CertificateFormat> supportedCertificateFormatForExportCa) {
        this.supportedCertificateFormatForExportCa = supportedCertificateFormatForExportCa;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getSupportsMultipleCa() {
        return supportsMultipleCa;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setSupportsMultipleCa(Property.FeatureEnabled supportsMultipleCa) {
        this.supportsMultipleCa = supportsMultipleCa;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getSupportsMultipleId() {
        return supportsMultipleId;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setSupportsMultipleId(Property.FeatureEnabled supportsMultipleId) {
        this.supportsMultipleId = supportsMultipleId;
    }

    /**
     * Maximum File Size for a Certificate that is allowed on a device
     * 
     */
    public Integer getMaxCertFileSizeAllowed() {
        return maxCertFileSizeAllowed;
    }

    /**
     * Maximum File Size for a Certificate that is allowed on a device
     * 
     */
    public void setMaxCertFileSizeAllowed(Integer maxCertFileSizeAllowed) {
        this.maxCertFileSizeAllowed = maxCertFileSizeAllowed;
    }

    /**
     * Maximum Validity (in number of days) for a cert that is allowed on a device
     * 
     */
    public Integer getMaxValidityDate() {
        return maxValidityDate;
    }

    /**
     * Maximum Validity (in number of days) for a cert that is allowed on a device
     * 
     */
    public void setMaxValidityDate(Integer maxValidityDate) {
        this.maxValidityDate = maxValidityDate;
    }

    /**
     * Maximum Timeout (in seconds) for a Certificate Createion that is allowed on a device
     * 
     */
    public Integer getMaxCertCreationTimeout() {
        return maxCertCreationTimeout;
    }

    /**
     * Maximum Timeout (in seconds) for a Certificate Createion that is allowed on a device
     * 
     */
    public void setMaxCertCreationTimeout(Integer maxCertCreationTimeout) {
        this.maxCertCreationTimeout = maxCertCreationTimeout;
    }

}
