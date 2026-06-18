
package com.hp.ws.cdm.storage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Removed support for partition attributes and added fileEraseModeValuesSupported for capabilities
 * 
 */
public class Com_hp_cdm_service_storageDevices_removableDevices_version_1_schema {

    @SerializedName("systemStorage")
    @Expose
    private SystemStorage systemStorage;
    @SerializedName("removableDevices")
    @Expose
    private RemovableDevices removableDevices;
    @SerializedName("removableDevice")
    @Expose
    private RemovableDevice removableDevice;
    @SerializedName("directoryListing")
    @Expose
    private DirectoryListing directoryListing;
    @SerializedName("format")
    @Expose
    private Format format;
    @SerializedName("secureErase")
    @Expose
    private SecureErase secureErase;
    @SerializedName("capabilities")
    @Expose
    private Capabilities capabilities;
    @SerializedName("systemDeviceDetail")
    @Expose
    private SystemDeviceDetail systemDeviceDetail;
    @SerializedName("configuration")
    @Expose
    private Configuration configuration;

    public SystemStorage getSystemStorage() {
        return systemStorage;
    }

    public void setSystemStorage(SystemStorage systemStorage) {
        this.systemStorage = systemStorage;
    }

    public RemovableDevices getRemovableDevices() {
        return removableDevices;
    }

    public void setRemovableDevices(RemovableDevices removableDevices) {
        this.removableDevices = removableDevices;
    }

    public RemovableDevice getRemovableDevice() {
        return removableDevice;
    }

    public void setRemovableDevice(RemovableDevice removableDevice) {
        this.removableDevice = removableDevice;
    }

    public DirectoryListing getDirectoryListing() {
        return directoryListing;
    }

    public void setDirectoryListing(DirectoryListing directoryListing) {
        this.directoryListing = directoryListing;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public SecureErase getSecureErase() {
        return secureErase;
    }

    public void setSecureErase(SecureErase secureErase) {
        this.secureErase = secureErase;
    }

    public Capabilities getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    public SystemDeviceDetail getSystemDeviceDetail() {
        return systemDeviceDetail;
    }

    public void setSystemDeviceDetail(SystemDeviceDetail systemDeviceDetail) {
        this.systemDeviceDetail = systemDeviceDetail;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

}
