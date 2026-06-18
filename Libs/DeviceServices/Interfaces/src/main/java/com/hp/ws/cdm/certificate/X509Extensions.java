
package com.hp.ws.cdm.certificate;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class X509Extensions {

    /**
     * SHA-1 hash identifier of the issuing Certificate Authority's public key.
     * 
     */
    @SerializedName("authorityKeyIdentifier")
    @Expose
    private String authorityKeyIdentifier;
    /**
     * SHA-1 hash identifier for this certificate's public key
     * 
     */
    @SerializedName("subjectKeyIdentifier")
    @Expose
    private String subjectKeyIdentifier;
    /**
     * Enum values for the Basic Constraints extension indicating if the certificate represents a Certificate Authority (CA) certificate
     * 
     */
    @SerializedName("basicConstraints")
    @Expose
    private String basicConstraints;
    @SerializedName("keyUsageList")
    @Expose
    private List<KeyUsageType> keyUsageList = new ArrayList<KeyUsageType>();
    /**
     * List of extended key usages
     * 
     */
    @SerializedName("extendedKeyUsageList")
    @Expose
    private List<ExtendedKeyUsageType> extendedKeyUsageList = new ArrayList<ExtendedKeyUsageType>();
    /**
     * Additional Extensions List
     * 
     */
    @SerializedName("additionalExtensionsList")
    @Expose
    private List<String> additionalExtensionsList = new ArrayList<String>();
    /**
     * The set of elements to be used as identifiers / alternative names for multiple network interfaces in certificate unification as described in RFC5280
     * 
     */
    @SerializedName("subjectAlternativeNameList")
    @Expose
    private List<String> subjectAlternativeNameList = new ArrayList<String>();

    /**
     * SHA-1 hash identifier of the issuing Certificate Authority's public key.
     * 
     */
    public String getAuthorityKeyIdentifier() {
        return authorityKeyIdentifier;
    }

    /**
     * SHA-1 hash identifier of the issuing Certificate Authority's public key.
     * 
     */
    public void setAuthorityKeyIdentifier(String authorityKeyIdentifier) {
        this.authorityKeyIdentifier = authorityKeyIdentifier;
    }

    /**
     * SHA-1 hash identifier for this certificate's public key
     * 
     */
    public String getSubjectKeyIdentifier() {
        return subjectKeyIdentifier;
    }

    /**
     * SHA-1 hash identifier for this certificate's public key
     * 
     */
    public void setSubjectKeyIdentifier(String subjectKeyIdentifier) {
        this.subjectKeyIdentifier = subjectKeyIdentifier;
    }

    /**
     * Enum values for the Basic Constraints extension indicating if the certificate represents a Certificate Authority (CA) certificate
     * 
     */
    public String getBasicConstraints() {
        return basicConstraints;
    }

    /**
     * Enum values for the Basic Constraints extension indicating if the certificate represents a Certificate Authority (CA) certificate
     * 
     */
    public void setBasicConstraints(String basicConstraints) {
        this.basicConstraints = basicConstraints;
    }

    public List<KeyUsageType> getKeyUsageList() {
        return keyUsageList;
    }

    public void setKeyUsageList(List<KeyUsageType> keyUsageList) {
        this.keyUsageList = keyUsageList;
    }

    /**
     * List of extended key usages
     * 
     */
    public List<ExtendedKeyUsageType> getExtendedKeyUsageList() {
        return extendedKeyUsageList;
    }

    /**
     * List of extended key usages
     * 
     */
    public void setExtendedKeyUsageList(List<ExtendedKeyUsageType> extendedKeyUsageList) {
        this.extendedKeyUsageList = extendedKeyUsageList;
    }

    /**
     * Additional Extensions List
     * 
     */
    public List<String> getAdditionalExtensionsList() {
        return additionalExtensionsList;
    }

    /**
     * Additional Extensions List
     * 
     */
    public void setAdditionalExtensionsList(List<String> additionalExtensionsList) {
        this.additionalExtensionsList = additionalExtensionsList;
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

}
