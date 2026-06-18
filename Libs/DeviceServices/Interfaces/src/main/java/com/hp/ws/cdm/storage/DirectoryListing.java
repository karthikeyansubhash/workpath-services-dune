
package com.hp.ws.cdm.storage;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;

public class DirectoryListing {

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
     * Array containing 'fileSystemEntryInfo's
     * 
     */
    @SerializedName("fileSystemEntries")
    @Expose
    private List<FileSystemEntryInfo> fileSystemEntries = new ArrayList<FileSystemEntryInfo>();
    /**
     * The target directory
     * 
     */
    @SerializedName("directory")
    @Expose
    private String directory;
    /**
     * The drive the specified directory is on. From 'cdm/storageDevices/v1/removableDevices'.
     * 
     */
    @SerializedName("driveId")
    @Expose
    private String driveId;
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
     * Array containing 'fileSystemEntryInfo's
     * 
     */
    public List<FileSystemEntryInfo> getFileSystemEntries() {
        return fileSystemEntries;
    }

    /**
     * Array containing 'fileSystemEntryInfo's
     * 
     */
    public void setFileSystemEntries(List<FileSystemEntryInfo> fileSystemEntries) {
        this.fileSystemEntries = fileSystemEntries;
    }

    /**
     * The target directory
     * 
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * The target directory
     * 
     */
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * The drive the specified directory is on. From 'cdm/storageDevices/v1/removableDevices'.
     * 
     */
    public String getDriveId() {
        return driveId;
    }

    /**
     * The drive the specified directory is on. From 'cdm/storageDevices/v1/removableDevices'.
     * 
     */
    public void setDriveId(String driveId) {
        this.driveId = driveId;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}
