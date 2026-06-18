
package com.hp.ws.cdm.jobmanagement;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * provides the send job specific details
 * 
 */
public class SendInfo {

    /**
     * this provide list of destinations and destination results of send jobs
     * 
     */
    @SerializedName("destination")
    @Expose
    private List<SendDestination> destination = new ArrayList<SendDestination>();
    /**
     * this provide the file name of send job
     * 
     */
    @SerializedName("fileName")
    @Expose
    private String fileName;
    @SerializedName("fileType")
    @Expose
    private com.hp.ws.cdm.jobmanagement.ScanInfo.FileFormat fileType;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("signing")
    @Expose
    private Property.FeatureEnabled signing;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("encryption")
    @Expose
    private Property.FeatureEnabled encryption;

    /**
     * this provide list of destinations and destination results of send jobs
     * 
     */
    public List<SendDestination> getDestination() {
        return destination;
    }

    /**
     * this provide list of destinations and destination results of send jobs
     * 
     */
    public void setDestination(List<SendDestination> destination) {
        this.destination = destination;
    }

    /**
     * this provide the file name of send job
     * 
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * this provide the file name of send job
     * 
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public com.hp.ws.cdm.jobmanagement.ScanInfo.FileFormat getFileType() {
        return fileType;
    }

    public void setFileType(com.hp.ws.cdm.jobmanagement.ScanInfo.FileFormat fileType) {
        this.fileType = fileType;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getSigning() {
        return signing;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setSigning(Property.FeatureEnabled signing) {
        this.signing = signing;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEncryption() {
        return encryption;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEncryption(Property.FeatureEnabled encryption) {
        this.encryption = encryption;
    }

}
