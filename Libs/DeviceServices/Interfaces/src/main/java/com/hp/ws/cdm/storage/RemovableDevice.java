
package com.hp.ws.cdm.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;
import com.hp.ws.cdm.commonglossary.Property;

public class RemovableDevice {

    /**
     * System assigned unique identifier for the storage accessory (used as a resource reference).
     * 
     */
    @SerializedName("driveId")
    @Expose
    private String driveId;
    /**
     * Volume name of the storage device
     * 
     */
    @SerializedName("volumeName")
    @Expose
    private String volumeName;
    /**
     * The USB port to which the device is connected 
     * 
     */
    @SerializedName("usbPortLocation")
    @Expose
    private RemovableDevice.UsbPortLocation usbPortLocation;
    /**
     * The total capacity of the storage device in MB
     * 
     */
    @SerializedName("totalSpace")
    @Expose
    private Integer totalSpace;
    /**
     * The used space of the storage device in MB
     * 
     */
    @SerializedName("usedSpace")
    @Expose
    private Integer usedSpace;
    /**
     * The available space of the storage device in MB
     * 
     */
    @SerializedName("availableSpace")
    @Expose
    private Integer availableSpace;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("encryptionStatus")
    @Expose
    private Property.FeatureEnabled encryptionStatus;
    /**
     * This descibes whether the storage device is USB or SD Card.
     * 
     */
    @SerializedName("deviceType")
    @Expose
    private RemovableDevice.DeviceType deviceType;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();

    /**
     * System assigned unique identifier for the storage accessory (used as a resource reference).
     * 
     */
    public String getDriveId() {
        return driveId;
    }

    /**
     * System assigned unique identifier for the storage accessory (used as a resource reference).
     * 
     */
    public void setDriveId(String driveId) {
        this.driveId = driveId;
    }

    /**
     * Volume name of the storage device
     * 
     */
    public String getVolumeName() {
        return volumeName;
    }

    /**
     * Volume name of the storage device
     * 
     */
    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    /**
     * The USB port to which the device is connected 
     * 
     */
    public RemovableDevice.UsbPortLocation getUsbPortLocation() {
        return usbPortLocation;
    }

    /**
     * The USB port to which the device is connected 
     * 
     */
    public void setUsbPortLocation(RemovableDevice.UsbPortLocation usbPortLocation) {
        this.usbPortLocation = usbPortLocation;
    }

    /**
     * The total capacity of the storage device in MB
     * 
     */
    public Integer getTotalSpace() {
        return totalSpace;
    }

    /**
     * The total capacity of the storage device in MB
     * 
     */
    public void setTotalSpace(Integer totalSpace) {
        this.totalSpace = totalSpace;
    }

    /**
     * The used space of the storage device in MB
     * 
     */
    public Integer getUsedSpace() {
        return usedSpace;
    }

    /**
     * The used space of the storage device in MB
     * 
     */
    public void setUsedSpace(Integer usedSpace) {
        this.usedSpace = usedSpace;
    }

    /**
     * The available space of the storage device in MB
     * 
     */
    public Integer getAvailableSpace() {
        return availableSpace;
    }

    /**
     * The available space of the storage device in MB
     * 
     */
    public void setAvailableSpace(Integer availableSpace) {
        this.availableSpace = availableSpace;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEncryptionStatus() {
        return encryptionStatus;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEncryptionStatus(Property.FeatureEnabled encryptionStatus) {
        this.encryptionStatus = encryptionStatus;
    }

    /**
     * This descibes whether the storage device is USB or SD Card.
     * 
     */
    public RemovableDevice.DeviceType getDeviceType() {
        return deviceType;
    }

    /**
     * This descibes whether the storage device is USB or SD Card.
     * 
     */
    public void setDeviceType(RemovableDevice.DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }


    /**
     * This descibes whether the storage device is USB or SD Card.
     * 
     */
    public enum DeviceType {

        @SerializedName("usb")
        USB("usb"),
        @SerializedName("sd")
        SD("sd");
        private final String value;
        private final static Map<String, RemovableDevice.DeviceType> CONSTANTS = new HashMap<String, RemovableDevice.DeviceType>();

        static {
            for (RemovableDevice.DeviceType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        DeviceType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static RemovableDevice.DeviceType fromValue(String value) {
            RemovableDevice.DeviceType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * The USB port to which the device is connected 
     * 
     */
    public enum UsbPortLocation {

        @SerializedName("frontUsb")
        FRONT_USB("frontUsb"),
        @SerializedName("rearUsb")
        REAR_USB("rearUsb");
        private final String value;
        private final static Map<String, RemovableDevice.UsbPortLocation> CONSTANTS = new HashMap<String, RemovableDevice.UsbPortLocation>();

        static {
            for (RemovableDevice.UsbPortLocation c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        UsbPortLocation(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static RemovableDevice.UsbPortLocation fromValue(String value) {
            RemovableDevice.UsbPortLocation constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
