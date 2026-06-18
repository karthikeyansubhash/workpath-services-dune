
package com.hp.ws.cdm.jobticket;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class FolderDestination {

    /**
     * This field specifies different protocol to be used for send to network folder
     * 
     */
    @SerializedName("folderType")
    @Expose
    private FolderProtocol folderType;
    /**
     * This is the uuid that represents a unique folder destination
     * 
     */
    @SerializedName("id")
    @Expose
    private String id;
    /**
     * This is the uuid that represents a contact in AddressBook
     * 
     */
    @SerializedName("recordId")
    @Expose
    private String recordId;
    @SerializedName("ftpSettings")
    @Expose
    private FtpSettings ftpSettings;
    @SerializedName("path")
    @Expose
    private String path;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("subDirectoryRestriction")
    @Expose
    private Property.FeatureEnabled subDirectoryRestriction;
    /**
     * This field specifies the name of sub folder which will be created in the given path
     * 
     */
    @SerializedName("customSubFolder")
    @Expose
    private String customSubFolder;
    @SerializedName("credential")
    @Expose
    private Credential credential;

    /**
     * This field specifies different protocol to be used for send to network folder
     * 
     */
    public FolderProtocol getFolderType() {
        return folderType;
    }

    /**
     * This field specifies different protocol to be used for send to network folder
     * 
     */
    public void setFolderType(FolderProtocol folderType) {
        this.folderType = folderType;
    }

    /**
     * This is the uuid that represents a unique folder destination
     * 
     */
    public String getId() {
        return id;
    }

    /**
     * This is the uuid that represents a unique folder destination
     * 
     */
    public void setId(String id) {
        this.id = id;
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

    public FtpSettings getFtpSettings() {
        return ftpSettings;
    }

    public void setFtpSettings(FtpSettings ftpSettings) {
        this.ftpSettings = ftpSettings;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getSubDirectoryRestriction() {
        return subDirectoryRestriction;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setSubDirectoryRestriction(Property.FeatureEnabled subDirectoryRestriction) {
        this.subDirectoryRestriction = subDirectoryRestriction;
    }

    /**
     * This field specifies the name of sub folder which will be created in the given path
     * 
     */
    public String getCustomSubFolder() {
        return customSubFolder;
    }

    /**
     * This field specifies the name of sub folder which will be created in the given path
     * 
     */
    public void setCustomSubFolder(String customSubFolder) {
        this.customSubFolder = customSubFolder;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }


    /**
     * This field specifies different protocol to be used for send to network folder
     * 
     */
    public enum FolderProtocol {

        @SerializedName("smb")
        SMB("smb");
        private final String value;
        private final static Map<String, FolderProtocol> CONSTANTS = new HashMap<String, FolderProtocol>();

        static {
            for (FolderProtocol c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        FolderProtocol(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static FolderProtocol fromValue(String value) {
            FolderProtocol constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
