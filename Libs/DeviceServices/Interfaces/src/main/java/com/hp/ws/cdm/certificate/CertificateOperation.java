
package com.hp.ws.cdm.certificate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;
import com.hp.ws.cdm.commonglossary.Property;

public class CertificateOperation {

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
     * operations that can be performed on certificates .
     * 
     */
    @SerializedName("requestType")
    @Expose
    private CertificateOperation.CertificateOperationType requestType;
    @SerializedName("certificateId")
    @Expose
    private String certificateId;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("privateKeyExportable")
    @Expose
    private Property.FeatureEnabled privateKeyExportable;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("certificateFormat")
    @Expose
    private CertificateFormat certificateFormat;
    @SerializedName("certificateData")
    @Expose
    private String certificateData;
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

    /**
     * operations that can be performed on certificates .
     * 
     */
    public CertificateOperation.CertificateOperationType getRequestType() {
        return requestType;
    }

    /**
     * operations that can be performed on certificates .
     * 
     */
    public void setRequestType(CertificateOperation.CertificateOperationType requestType) {
        this.requestType = requestType;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CertificateFormat getCertificateFormat() {
        return certificateFormat;
    }

    public void setCertificateFormat(CertificateFormat certificateFormat) {
        this.certificateFormat = certificateFormat;
    }

    public String getCertificateData() {
        return certificateData;
    }

    public void setCertificateData(String certificateData) {
        this.certificateData = certificateData;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }


    /**
     * operations that can be performed on certificates .
     * 
     */
    public enum CertificateOperationType {

        @SerializedName("installId")
        INSTALL_ID("installId"),
        @SerializedName("importId")
        IMPORT_ID("importId"),
        @SerializedName("importCa")
        IMPORT_CA("importCa"),
        @SerializedName("exportId")
        EXPORT_ID("exportId"),
        @SerializedName("exportCa")
        EXPORT_CA("exportCa");
        private final String value;
        private final static Map<String, CertificateOperation.CertificateOperationType> CONSTANTS = new HashMap<String, CertificateOperation.CertificateOperationType>();

        static {
            for (CertificateOperation.CertificateOperationType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        CertificateOperationType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static CertificateOperation.CertificateOperationType fromValue(String value) {
            CertificateOperation.CertificateOperationType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
