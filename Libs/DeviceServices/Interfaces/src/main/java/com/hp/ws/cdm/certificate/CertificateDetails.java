
package com.hp.ws.cdm.certificate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class CertificateDetails {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("certificateId")
    @Expose
    private String certificateId;
    @SerializedName("certificateVersion")
    @Expose
    private Integer certificateVersion;
    @SerializedName("certificateType")
    @Expose
    private com.hp.ws.cdm.certificate.CertificateInfo.CertificateType certificateType;
    @SerializedName("certificateUsage")
    @Expose
    private List<CertificateUsage> certificateUsage = new ArrayList<CertificateUsage>();
    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;
    @SerializedName("signatureAlgorithm")
    @Expose
    private SignatureAlgorithm signatureAlgorithm;
    @SerializedName("issuer")
    @Expose
    private String issuer;
    @SerializedName("issuerAttributes")
    @Expose
    private CertificateAttributes issuerAttributes;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("subjectAttributes")
    @Expose
    private CertificateAttributes subjectAttributes;
    @SerializedName("validity")
    @Expose
    private Validity validity;
    @SerializedName("keyInfo")
    @Expose
    private KeyInfo keyInfo;
    @SerializedName("x509Extensions")
    @Expose
    private X509Extensions x509Extensions;
    @SerializedName("thumbPrint")
    @Expose
    private String thumbPrint;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("privateKeyExportable")
    @Expose
    private Property.FeatureEnabled privateKeyExportable;
    @SerializedName("ownership")
    @Expose
    private CertificateDetails.Ownership ownership;

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

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public Integer getCertificateVersion() {
        return certificateVersion;
    }

    public void setCertificateVersion(Integer certificateVersion) {
        this.certificateVersion = certificateVersion;
    }

    public com.hp.ws.cdm.certificate.CertificateInfo.CertificateType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(com.hp.ws.cdm.certificate.CertificateInfo.CertificateType certificateType) {
        this.certificateType = certificateType;
    }

    public List<CertificateUsage> getCertificateUsage() {
        return certificateUsage;
    }

    public void setCertificateUsage(List<CertificateUsage> certificateUsage) {
        this.certificateUsage = certificateUsage;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public SignatureAlgorithm getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(SignatureAlgorithm signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public CertificateAttributes getIssuerAttributes() {
        return issuerAttributes;
    }

    public void setIssuerAttributes(CertificateAttributes issuerAttributes) {
        this.issuerAttributes = issuerAttributes;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public CertificateAttributes getSubjectAttributes() {
        return subjectAttributes;
    }

    public void setSubjectAttributes(CertificateAttributes subjectAttributes) {
        this.subjectAttributes = subjectAttributes;
    }

    public Validity getValidity() {
        return validity;
    }

    public void setValidity(Validity validity) {
        this.validity = validity;
    }

    public KeyInfo getKeyInfo() {
        return keyInfo;
    }

    public void setKeyInfo(KeyInfo keyInfo) {
        this.keyInfo = keyInfo;
    }

    public X509Extensions getX509Extensions() {
        return x509Extensions;
    }

    public void setX509Extensions(X509Extensions x509Extensions) {
        this.x509Extensions = x509Extensions;
    }

    public String getThumbPrint() {
        return thumbPrint;
    }

    public void setThumbPrint(String thumbPrint) {
        this.thumbPrint = thumbPrint;
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

    public CertificateDetails.Ownership getOwnership() {
        return ownership;
    }

    public void setOwnership(CertificateDetails.Ownership ownership) {
        this.ownership = ownership;
    }

    public enum Ownership {

        @SerializedName("factory")
        FACTORY("factory"),
        @SerializedName("user")
        USER("user");
        private final String value;
        private final static Map<String, CertificateDetails.Ownership> CONSTANTS = new HashMap<String, CertificateDetails.Ownership>();

        static {
            for (CertificateDetails.Ownership c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Ownership(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static CertificateDetails.Ownership fromValue(String value) {
            CertificateDetails.Ownership constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
