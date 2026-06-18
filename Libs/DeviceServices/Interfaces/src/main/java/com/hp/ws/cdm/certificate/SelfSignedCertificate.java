
package com.hp.ws.cdm.certificate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;
import com.hp.ws.cdm.commonglossary.Property;

public class SelfSignedCertificate {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("state")
    @Expose
    private SelfSignedCertificate.State state;
    @SerializedName("lastResult")
    @Expose
    private SelfSignedCertificate.LastResult lastResult;
    @SerializedName("lastResultTimeStamp")
    @Expose
    private String lastResultTimeStamp;
    @SerializedName("failureReason")
    @Expose
    private String failureReason;
    @SerializedName("certificateAttributes")
    @Expose
    private CertificateAttributes certificateAttributes;
    @SerializedName("keyInfo")
    @Expose
    private KeyInfo keyInfo;
    @SerializedName("signatureAlgorithm")
    @Expose
    private SignatureAlgorithm signatureAlgorithm;
    /**
     * The set of elements to be used as identifiers / alternative names for multiple network interfaces in certificate unification as described in RFC5280
     * 
     */
    @SerializedName("subjectAlternativeNameList")
    @Expose
    private List<String> subjectAlternativeNameList = new ArrayList<String>();
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("privateKeyExportable")
    @Expose
    private Property.FeatureEnabled privateKeyExportable;
    @SerializedName("validity")
    @Expose
    private Validity validity;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();

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

    public SelfSignedCertificate.State getState() {
        return state;
    }

    public void setState(SelfSignedCertificate.State state) {
        this.state = state;
    }

    public SelfSignedCertificate.LastResult getLastResult() {
        return lastResult;
    }

    public void setLastResult(SelfSignedCertificate.LastResult lastResult) {
        this.lastResult = lastResult;
    }

    public String getLastResultTimeStamp() {
        return lastResultTimeStamp;
    }

    public void setLastResultTimeStamp(String lastResultTimeStamp) {
        this.lastResultTimeStamp = lastResultTimeStamp;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public CertificateAttributes getCertificateAttributes() {
        return certificateAttributes;
    }

    public void setCertificateAttributes(CertificateAttributes certificateAttributes) {
        this.certificateAttributes = certificateAttributes;
    }

    public KeyInfo getKeyInfo() {
        return keyInfo;
    }

    public void setKeyInfo(KeyInfo keyInfo) {
        this.keyInfo = keyInfo;
    }

    public SignatureAlgorithm getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(SignatureAlgorithm signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    /**
     * The set of elements to be used as identifiers / alternative names for multiple network interfaces in certificate unification as described in RFC5280
     * 
     */
    public List<String> getSubjectAlternativeNameList() {
        return subjectAlternativeNameList;
    }

    /**
     * The set of elements to be used as identifiers / alternative names for multiple network interfaces in certificate unification as described in RFC5280
     * 
     */
    public void setSubjectAlternativeNameList(List<String> subjectAlternativeNameList) {
        this.subjectAlternativeNameList = subjectAlternativeNameList;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getPrivateKeyExportable() {
        return privateKeyExportable;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPrivateKeyExportable(Property.FeatureEnabled privateKeyExportable) {
        this.privateKeyExportable = privateKeyExportable;
    }

    public Validity getValidity() {
        return validity;
    }

    public void setValidity(Validity validity) {
        this.validity = validity;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public enum LastResult {

        @SerializedName("success")
        SUCCESS("success"),
        @SerializedName("failed")
        FAILED("failed"),
        @SerializedName("cancelled")
        CANCELLED("cancelled");
        private final String value;
        private final static Map<String, SelfSignedCertificate.LastResult> CONSTANTS = new HashMap<String, SelfSignedCertificate.LastResult>();

        static {
            for (SelfSignedCertificate.LastResult c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        LastResult(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static SelfSignedCertificate.LastResult fromValue(String value) {
            SelfSignedCertificate.LastResult constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum State {

        @SerializedName("idle")
        IDLE("idle"),
        @SerializedName("processing")
        PROCESSING("processing"),
        @SerializedName("cancelProcessing")
        CANCEL_PROCESSING("cancelProcessing");
        private final String value;
        private final static Map<String, SelfSignedCertificate.State> CONSTANTS = new HashMap<String, SelfSignedCertificate.State>();

        static {
            for (SelfSignedCertificate.State c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        State(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static SelfSignedCertificate.State fromValue(String value) {
            SelfSignedCertificate.State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
