
package com.hp.ws.cdm.tlsconfiguration;

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
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("supportsCustomCipherSelection")
    @Expose
    private Property.FeatureEnabled supportsCustomCipherSelection;
    @SerializedName("supportedCiphers")
    @Expose
    private List<Cipher> supportedCiphers = new ArrayList<Cipher>();
    @SerializedName("supportedProtocolVersions")
    @Expose
    private List<ProtocolVersion> supportedProtocolVersions = new ArrayList<ProtocolVersion>();
    @SerializedName("supportedEllipticCurves")
    @Expose
    private List<EllipticCurve> supportedEllipticCurves = new ArrayList<EllipticCurve>();
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("supportsFips")
    @Expose
    private Property.FeatureEnabled supportsFips;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("supportsDebugLogging")
    @Expose
    private Property.FeatureEnabled supportsDebugLogging;

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
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getSupportsCustomCipherSelection() {
        return supportsCustomCipherSelection;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setSupportsCustomCipherSelection(Property.FeatureEnabled supportsCustomCipherSelection) {
        this.supportsCustomCipherSelection = supportsCustomCipherSelection;
    }

    public List<Cipher> getSupportedCiphers() {
        return supportedCiphers;
    }

    public void setSupportedCiphers(List<Cipher> supportedCiphers) {
        this.supportedCiphers = supportedCiphers;
    }

    public List<ProtocolVersion> getSupportedProtocolVersions() {
        return supportedProtocolVersions;
    }

    public void setSupportedProtocolVersions(List<ProtocolVersion> supportedProtocolVersions) {
        this.supportedProtocolVersions = supportedProtocolVersions;
    }

    public List<EllipticCurve> getSupportedEllipticCurves() {
        return supportedEllipticCurves;
    }

    public void setSupportedEllipticCurves(List<EllipticCurve> supportedEllipticCurves) {
        this.supportedEllipticCurves = supportedEllipticCurves;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getSupportsFips() {
        return supportsFips;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setSupportsFips(Property.FeatureEnabled supportsFips) {
        this.supportsFips = supportsFips;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getSupportsDebugLogging() {
        return supportsDebugLogging;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setSupportsDebugLogging(Property.FeatureEnabled supportsDebugLogging) {
        this.supportsDebugLogging = supportsDebugLogging;
    }

}
