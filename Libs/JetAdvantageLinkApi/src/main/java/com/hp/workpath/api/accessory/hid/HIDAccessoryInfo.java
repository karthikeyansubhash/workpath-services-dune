// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.accessory.hid;

import android.os.Parcel;
import android.os.Parcelable;

import com.hp.workpath.api.Convertor;
import com.hp.workpath.api.accessory.AccessoryInfo;
import com.hp.workpath.api.accessory.RegistrationType;

/**
 * Accessory details
 *
 * @since API 3
 */
public class HIDAccessoryInfo extends AccessoryInfo implements Convertor {
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
        this.mSerialNumber = serialNumber;
        this.mDescription = description;
        this.mManufacturer = manufacturer;
        this.mRegistrationType = registrationType;
    }

    /**
     * <p>Creates HIDAccessory information</p>
     *
     * @param vendorId     Accessory vendor id
     * @param productId    Accessory product id
     * @param serialNumber Accessory serial number
     * @return an object of HIDAccessoryInfo, description and manufacturer will be initialized as null
     * @since API 3
     */
    public HIDAccessoryInfo(int vendorId, int productId, String serialNumber) {
        this(vendorId, productId, serialNumber, null, null, null);
    }

    private HIDAccessoryInfo(Parcel in) {
        mVendorId = in.readInt();
        mProductId = in.readInt();
        mSerialNumber = in.readString();
        mDescription = in.readString();
        mManufacturer = in.readString();
        mRegistrationType = (RegistrationType) in.readSerializable();
    }

    private HIDAccessoryInfo(Object object) {
        if (object instanceof com.hp.jetadvantage.link.api.accessory.hid.HIDAccessoryInfo) {
            mVendorId = ((com.hp.jetadvantage.link.api.accessory.hid.HIDAccessoryInfo) object).getVendorId();
            mProductId = ((com.hp.jetadvantage.link.api.accessory.hid.HIDAccessoryInfo) object).getProductId();
            mSerialNumber = ((com.hp.jetadvantage.link.api.accessory.hid.HIDAccessoryInfo) object).getSerialNumber();
            mDescription = ((com.hp.jetadvantage.link.api.accessory.hid.HIDAccessoryInfo) object).getDescription();
            mManufacturer = ((com.hp.jetadvantage.link.api.accessory.hid.HIDAccessoryInfo) object).getManufacturer();

            com.hp.jetadvantage.link.api.accessory.RegistrationType registrationType = ((com.hp.jetadvantage.link.api.accessory.hid.HIDAccessoryInfo) object).getRegistrationType();
            if (registrationType != null) {
                mRegistrationType = RegistrationType.valueOf(registrationType.name());
            } else {
                mRegistrationType = RegistrationType.OWNED; //TODO Default is OWNED
            }
        }
    }

    /**
     * Accessory class.
     *
     * @return accessory class
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
     * @since API 3
     */
    public int getVendorId() {
        return mVendorId;
    }

    /**
     * Product ID
     *
     * @return product id
     * @since API 3
     */
    public int getProductId() {
        return mProductId;
    }

    /**
     * Serial number
     *
     * @return serial number
     * @since API 3
     */
    public String getSerialNumber() {
        return mSerialNumber;
    }

    /**
     * Accessory description
     *
     * @return description
     * @since API 3
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * Accessory manufacturer
     *
     * @return manufacturer
     * @since API 3
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
    public static final Parcelable.Creator<HIDAccessoryInfo> CREATOR = new Parcelable.Creator<HIDAccessoryInfo>() {
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
     * @hide trivial
     */
    public static final ConvertorCreator<HIDAccessoryInfo> CREATOR_OBJ = new ConvertorCreator<HIDAccessoryInfo>() {
        @Override
        public HIDAccessoryInfo createFromObject(Object object) {
            return new HIDAccessoryInfo(object);
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
                && mSerialNumber != null ? mSerialNumber.equals(that.mSerialNumber) : that.mSerialNumber == null;
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
                "VID=" + mVendorId +
                '}';
    }
}
