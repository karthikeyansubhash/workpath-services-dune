// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.accessory.hid;

import android.os.Parcel;

import com.hp.jetadvantage.link.api.accessory.AccessoryInfo;
import com.hp.jetadvantage.link.api.accessory.RegistrationType;

/**
 * Accessory details
 * @since API 3
 */
public class HIDAccessoryInfo extends AccessoryInfo {
    private int mVendorId;
    private int mProductId;
    private String mSerialNumber;
    private String mDescription;
    private String mManufacturer;
    private RegistrationType mRegistrationType;

    /**
     * @hide
     */
    public HIDAccessoryInfo(int vendorId, int productId, String serialNumber, String description, String manufacturer, RegistrationType registrationType) {
        this.mVendorId = vendorId;
        this.mProductId = productId;
        if("".equals(serialNumber)){
            this.mSerialNumber = null;
        } else {
            this.mSerialNumber = serialNumber;
        }
        this.mDescription = description;
        this.mManufacturer = manufacturer;
        this.mRegistrationType = registrationType;
    }

    /**
     * <p>Creates HIDAccessory information</p>
     *
     * @param vendorId        Accessory vendor id
     * @param productId       Accessory product id
     * @param serialNumber    Accessory serial number
     * @return an object of HIDAccessoryInfo, description and manufacturer will be initialized as null
     *
     * @since API 3
     */
    public HIDAccessoryInfo(int vendorId, int productId, String serialNumber) {
        this(vendorId, productId, serialNumber, null, null, null);
    }

    private HIDAccessoryInfo(Parcel in) {
        mVendorId = in.readInt();
        mProductId = in.readInt();
        mSerialNumber = in.readString();
        if("".equals(mSerialNumber)) {
            mSerialNumber = null;
        }
        mDescription = in.readString();
        mManufacturer = in.readString();
        mRegistrationType = (RegistrationType) in.readSerializable();
    }

    /**
     * Accessory class.
     *
     * @return accessory class
     *
     * @since API 3
     */
    @Override
    public AccessoryClass getAccessoryClass() {
        return AccessoryClass.HID;
    }

    /**
     * Registration type.
     *
     * @return registration type
     *
     * @since API 3
     */
    @Override
    public RegistrationType getRegistrationType() {
        return mRegistrationType;
    }

    /**
     * Vendor ID
     *
     * @return vendor id
     *
     * @since API 3
     */
    public int getVendorId() {
        return mVendorId;
    }

    /**
     * Product ID
     *
     * @return product id
     *
     * @since API 3
     */
    public int getProductId() {
        return mProductId;
    }

    /**
     * Serial number
     *
     * @return serial number
     *
     * @since API 3
     */
    public String getSerialNumber() {
        return mSerialNumber;
    }

    /**
     * Accessory description
     *
     * @since API 3
     *
     * @return description
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * Accessory manufacturer
     *
     * @since API 3
     *
     * @return manufacturer
     */
    public String getManufacturer() {
        return mManufacturer;
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mVendorId);
        dest.writeInt(mProductId);
        dest.writeString(mSerialNumber);
        dest.writeString(mDescription);
        dest.writeString(mManufacturer);
        dest.writeSerializable(mRegistrationType);
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @hide parcelable implementation
     */
    public static final Creator<HIDAccessoryInfo> CREATOR = new Creator<HIDAccessoryInfo>() {
        @Override
        public HIDAccessoryInfo createFromParcel(Parcel in) {
            return new HIDAccessoryInfo(in);
        }

        @Override
        public HIDAccessoryInfo[] newArray(int size) {
            return new HIDAccessoryInfo[size];
        }
    };

    /**
     * @hide This is hidden because it should be understood without documenting in the javadoc
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HIDAccessoryInfo)) return false;

        HIDAccessoryInfo that = (HIDAccessoryInfo) o;

        return that.mVendorId == mVendorId
                && that.mProductId == mProductId
                && mSerialNumber != null ? mSerialNumber.equals(that.mSerialNumber) : (that.mSerialNumber == null || "".equals(that.mSerialNumber));
    }

    /**
     * @hide This is hidden because it should be understood without documenting in the javadoc
     */
    @Override
    public int hashCode() {
        int result = mVendorId;
        result = 31 * result + mProductId;
        result = 31 * result + (mSerialNumber != null ? mSerialNumber.hashCode() : 0);
        return result;
    }

    /**
     * @hide This is hidden because it should be understood without documenting in the javadoc
     */
    @Override
    public String toString() {
        return "HIDAccessoryInfo{" +
                "VID=" + mVendorId + ",PID=" + mProductId + ",SN=" + mSerialNumber +
                '}';
    }
}
