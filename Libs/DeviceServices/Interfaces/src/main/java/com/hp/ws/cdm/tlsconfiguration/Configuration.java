
package com.hp.ws.cdm.tlsconfiguration;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class Configuration {

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
     * List of ciphers available for configuration
     * 
     */
    @SerializedName("ciphers")
    @Expose
    private List<String> ciphers = new ArrayList<String>();
    /**
     * Cipher strength as defined on the device
     * 
     */
    @SerializedName("cipherStrength")
    @Expose
    private com.hp.ws.cdm.tlsconfiguration.Cipher.CipherStrength cipherStrength;
    @SerializedName("minProtocolVersion")
    @Expose
    private ProtocolVersion minProtocolVersion;
    @SerializedName("maxProtocolVersion")
    @Expose
    private ProtocolVersion maxProtocolVersion;
    @SerializedName("ellipticCurve")
    @Expose
    private EllipticCurve ellipticCurve;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("fipsMode")
    @Expose
    private Property.FeatureEnabled fipsMode;
    /**
     * TLS debug configuration used for analysing field issues
     * 
     */
    @SerializedName("tlsDebugLogging")
    @Expose
    private TlsDebugLogging tlsDebugLogging;

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
     * List of ciphers available for configuration
     * 
     */
    public List<String> getCiphers() {
        return ciphers;
    }

    /**
     * List of ciphers available for configuration
     * 
     */
    public void setCiphers(List<String> ciphers) {
        this.ciphers = ciphers;
    }

    /**
     * Cipher strength as defined on the device
     * 
     */
    public com.hp.ws.cdm.tlsconfiguration.Cipher.CipherStrength getCipherStrength() {
        return cipherStrength;
    }

    /**
     * Cipher strength as defined on the device
     * 
     */
    public void setCipherStrength(com.hp.ws.cdm.tlsconfiguration.Cipher.CipherStrength cipherStrength) {
        this.cipherStrength = cipherStrength;
    }

    public ProtocolVersion getMinProtocolVersion() {
        return minProtocolVersion;
    }

    public void setMinProtocolVersion(ProtocolVersion minProtocolVersion) {
        this.minProtocolVersion = minProtocolVersion;
    }

    public ProtocolVersion getMaxProtocolVersion() {
        return maxProtocolVersion;
    }

    public void setMaxProtocolVersion(ProtocolVersion maxProtocolVersion) {
        this.maxProtocolVersion = maxProtocolVersion;
    }

    public EllipticCurve getEllipticCurve() {
        return ellipticCurve;
    }

    public void setEllipticCurve(EllipticCurve ellipticCurve) {
        this.ellipticCurve = ellipticCurve;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getFipsMode() {
        return fipsMode;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setFipsMode(Property.FeatureEnabled fipsMode) {
        this.fipsMode = fipsMode;
    }

    /**
     * TLS debug configuration used for analysing field issues
     * 
     */
    public TlsDebugLogging getTlsDebugLogging() {
        return tlsDebugLogging;
    }

    /**
     * TLS debug configuration used for analysing field issues
     * 
     */
    public void setTlsDebugLogging(TlsDebugLogging tlsDebugLogging) {
        this.tlsDebugLogging = tlsDebugLogging;
    }

}
