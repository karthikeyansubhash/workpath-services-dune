
package com.hp.ws.cdm.certificate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;
import com.hp.ws.cdm.commonglossary.Property;

public class CertificateInfo {

    @SerializedName("certificateId")
    @Expose
    private String certificateId;
    @SerializedName("certificateType")
    @Expose
    private CertificateInfo.CertificateType certificateType;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("issuer")
    @Expose
    private String issuer;
    @SerializedName("validity")
    @Expose
    private Validity validity;
    @SerializedName("certificateUsage")
    @Expose
    private List<CertificateUsage> certificateUsage = new ArrayList<CertificateUsage>();
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
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public CertificateInfo.CertificateType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(CertificateInfo.CertificateType certificateType) {
        this.certificateType = certificateType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Validity getValidity() {
        return validity;
    }

    public void setValidity(Validity validity) {
        this.validity = validity;
    }

    public List<CertificateUsage> getCertificateUsage() {
        return certificateUsage;
    }

    public void setCertificateUsage(List<CertificateUsage> certificateUsage) {
        this.certificateUsage = certificateUsage;
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

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public enum CertificateType {

        @SerializedName("identityCertificateCaSigned")
        IDENTITY_CERTIFICATE_CA_SIGNED("identityCertificateCaSigned"),
        @SerializedName("identityCertificateProductSigned")
        IDENTITY_CERTIFICATE_PRODUCT_SIGNED("identityCertificateProductSigned"),
        @SerializedName("intermediateCaCertificate")
        INTERMEDIATE_CA_CERTIFICATE("intermediateCaCertificate"),
        @SerializedName("rootCaCertificate")
        ROOT_CA_CERTIFICATE("rootCaCertificate"),
        @SerializedName("productSignedCaCertificate")
        PRODUCT_SIGNED_CA_CERTIFICATE("productSignedCaCertificate");
        private final String value;
        private final static Map<String, CertificateInfo.CertificateType> CONSTANTS = new HashMap<String, CertificateInfo.CertificateType>();

        static {
            for (CertificateInfo.CertificateType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        CertificateType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static CertificateInfo.CertificateType fromValue(String value) {
            CertificateInfo.CertificateType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
