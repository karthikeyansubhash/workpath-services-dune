
package com.hp.ws.cdm.storage;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SystemDeviceDetail {

    /**
     * Disk Name
     * 
     */
    @SerializedName("name")
    @Expose
    private String name;
    /**
     * Disk Serial Number
     * 
     */
    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;
    /**
     * This indicates the disk type whether HDD, eMMC
     * 
     */
    @SerializedName("storageType")
    @Expose
    private SystemDeviceDetail.StorageType storageType;
    @SerializedName("storageDeviceStatus")
    @Expose
    private StorageDeviceStatus storageDeviceStatus;
    /**
     * Total Disk Space in MB
     * 
     */
    @SerializedName("totalSpace")
    @Expose
    private Integer totalSpace;
    /**
     * Total Used Space in MB
     * 
     */
    @SerializedName("usedSpace")
    @Expose
    private Integer usedSpace;
    /**
     * Total Free Space in MB 
     * 
     */
    @SerializedName("freeSpace")
    @Expose
    private Integer freeSpace;

    /**
     * Disk Name
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * Disk Name
     * 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Disk Serial Number
     * 
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Disk Serial Number
     * 
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * This indicates the disk type whether HDD, eMMC
     * 
     */
    public SystemDeviceDetail.StorageType getStorageType() {
        return storageType;
    }

    /**
     * This indicates the disk type whether HDD, eMMC
     * 
     */
    public void setStorageType(SystemDeviceDetail.StorageType storageType) {
        this.storageType = storageType;
    }

    public StorageDeviceStatus getStorageDeviceStatus() {
        return storageDeviceStatus;
    }

    public void setStorageDeviceStatus(StorageDeviceStatus storageDeviceStatus) {
        this.storageDeviceStatus = storageDeviceStatus;
    }

    /**
     * Total Disk Space in MB
     * 
     */
    public Integer getTotalSpace() {
        return totalSpace;
    }

    /**
     * Total Disk Space in MB
     * 
     */
    public void setTotalSpace(Integer totalSpace) {
        this.totalSpace = totalSpace;
    }

    /**
     * Total Used Space in MB
     * 
     */
    public Integer getUsedSpace() {
        return usedSpace;
    }

    /**
     * Total Used Space in MB
     * 
     */
    public void setUsedSpace(Integer usedSpace) {
        this.usedSpace = usedSpace;
    }

    /**
     * Total Free Space in MB 
     * 
     */
    public Integer getFreeSpace() {
        return freeSpace;
    }

    /**
     * Total Free Space in MB 
     * 
     */
    public void setFreeSpace(Integer freeSpace) {
        this.freeSpace = freeSpace;
    }


    /**
     * This indicates the disk type whether HDD, eMMC
     * 
     */
    public enum StorageType {

        @SerializedName("hdd")
        HDD("hdd"),
        @SerializedName("eMMC")
        E_MMC("eMMC"),
        @SerializedName("ssd")
        SSD("ssd"),
        @SerializedName("nand")
        NAND("nand"),
        @SerializedName("ramDisk")
        RAM_DISK("ramDisk");
        private final String value;
        private final static Map<String, SystemDeviceDetail.StorageType> CONSTANTS = new HashMap<String, SystemDeviceDetail.StorageType>();

        static {
            for (SystemDeviceDetail.StorageType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        StorageType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static SystemDeviceDetail.StorageType fromValue(String value) {
            SystemDeviceDetail.StorageType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
