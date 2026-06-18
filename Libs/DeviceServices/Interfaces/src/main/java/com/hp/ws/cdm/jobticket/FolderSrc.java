
package com.hp.ws.cdm.jobticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FolderSrc {

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
     * Shared network folder path
     * 
     */
    @SerializedName("networkFolderPath")
    @Expose
    private String networkFolderPath;
    @SerializedName("credential")
    @Expose
    private Credential credential;
    /**
     * This is the uuid that represents a contact in AddressBook
     * 
     */
    @SerializedName("recordId")
    @Expose
    private String recordId;
    /**
     * The full file path that a user wants to print out
     * 
     */
    @SerializedName("filePath")
    @Expose
    private String filePath;
    /**
     * The file name that a user wants to print out
     * 
     */
    @SerializedName("fileName")
    @Expose
    private String fileName;
    @SerializedName("fileFormat")
    @Expose
    private Usb.FileFormat fileFormat;

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
     * Shared network folder path
     * 
     */
    public String getNetworkFolderPath() {
        return networkFolderPath;
    }

    /**
     * Shared network folder path
     * 
     */
    public void setNetworkFolderPath(String networkFolderPath) {
        this.networkFolderPath = networkFolderPath;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    /**
     * This is the uuid that represents a contact in AddressBook
     * 
     */
    public String getRecordId() {
        return recordId;
    }

    /**
     * This is the uuid that represents a contact in AddressBook
     * 
     */
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    /**
     * The full file path that a user wants to print out
     * 
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * The full file path that a user wants to print out
     * 
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * The file name that a user wants to print out
     * 
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * The file name that a user wants to print out
     * 
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Usb.FileFormat getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(Usb.FileFormat fileFormat) {
        this.fileFormat = fileFormat;
    }

}
