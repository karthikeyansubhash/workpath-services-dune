
package com.hp.ws.cdm.jobticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class SharePointDestination {

    @SerializedName("path")
    @Expose
    private String path;
    /**
     * This is the uuid that represents a unique share point destination
     * 
     */
    @SerializedName("id")
    @Expose
    private String id;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("overWriteExistingFiles")
    @Expose
    private Property.FeatureEnabled overWriteExistingFiles;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("validateCertificate")
    @Expose
    private Property.FeatureEnabled validateCertificate;
    @SerializedName("credential")
    @Expose
    private Credential credential;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * This is the uuid that represents a unique share point destination
     * 
     */
    public String getId() {
        return id;
    }

    /**
     * This is the uuid that represents a unique share point destination
     * 
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getOverWriteExistingFiles() {
        return overWriteExistingFiles;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setOverWriteExistingFiles(Property.FeatureEnabled overWriteExistingFiles) {
        this.overWriteExistingFiles = overWriteExistingFiles;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getValidateCertificate() {
        return validateCertificate;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setValidateCertificate(Property.FeatureEnabled validateCertificate) {
        this.validateCertificate = validateCertificate;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

}
