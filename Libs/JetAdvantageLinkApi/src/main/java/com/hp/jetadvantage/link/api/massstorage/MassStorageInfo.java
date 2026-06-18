// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.massstorage;

import android.os.Parcel;
import android.os.Parcelable;

import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Provides attached mass storage details.
 *
 * @since API 2
 */
public class MassStorageInfo implements Parcelable {
    /**
     * Supported type of mass storage
     *
     * @return enum supported type
     * @since API 2
     */
    @DeviceApi
    public enum StorageType {
        USB,
        HDD
    }

    /**
     * Supported Protocol for accessing to mass storage
     *
     * @return enum supported protocol
     * @since API 2
     */
    @DeviceApi
    public enum Protocol {
        LOCAL
    }

    /**
     * Version of info. Important to maintain to avoid Parcel breakage
     */
    private int mVersion;
    private StorageType mType;
    private String mName;
    private String mVolumeName;
    private long mTotalSpace;
    private long mFreeSpace;
    private boolean mIsMounted;
    private String mExternalFileDirectory;
    private Protocol mProtocol;

    /**
     * @hide
     */
    public MassStorageInfo(StorageType mType, String mName, long mTotalSpace, long mFreeSpace,
                           boolean mIsMounted, String externalFileDirectory, Protocol protocol) {
        this(mType, mName, mTotalSpace, mFreeSpace, mIsMounted, externalFileDirectory, protocol, null);
    }

    /**
     * @hide
     */
    public MassStorageInfo(StorageType mType, String mName, long mTotalSpace, long mFreeSpace,
                           boolean mIsMounted, String externalFileDirectory, Protocol protocol, String volumeName) {
        this.mType = mType;
        this.mName = mName;
        this.mTotalSpace = mTotalSpace;
        this.mFreeSpace = mFreeSpace;
        this.mIsMounted = mIsMounted;
        this.mExternalFileDirectory = externalFileDirectory;
        this.mProtocol = protocol;
        this.mVolumeName = volumeName;
    }

    private MassStorageInfo(Parcel in) {
        //mVersion = in.readInt();
        //Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);
        mType = (StorageType) in.readSerializable();
        mName = in.readString();
        if (Sdk.VERSION.LEVEL >= Sdk.VERSION_LEVEL.THREE) {
            if (mName != null && mName.indexOf("(") > 0) {
                mVolumeName = mName.substring(mName.indexOf("(") + 1, mName.length() - 1);
                mName = mName.substring(0, mName.indexOf("("));
            }
        }
        mTotalSpace = in.readLong();
        mFreeSpace = in.readLong();
        mIsMounted = in.readByte() != 0;
        mExternalFileDirectory = in.readString();
        mProtocol = (Protocol) in.readSerializable();
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //dest.writeInt(Sdk.VERSION.LEVEL);
        dest.writeSerializable(mType);
        dest.writeString(mName);
        dest.writeLong(mTotalSpace);
        dest.writeLong(mFreeSpace);
        dest.writeByte((byte) (mIsMounted ? 1 : 0));
        dest.writeString(mExternalFileDirectory);
        dest.writeSerializable(mProtocol);
        //API LEVEL FIVE
        //dest.writeString(mVolumeName);
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
    public static final Creator<MassStorageInfo> CREATOR = new Creator<MassStorageInfo>() {
        @Override
        public MassStorageInfo createFromParcel(Parcel in) {
            return new MassStorageInfo(in);
        }

        @Override
        public MassStorageInfo[] newArray(int size) {
            return new MassStorageInfo[size];
        }
    };

    /**
     * Returns mass storage type
     *
     * @return StorageType supported type of mass storage
     * @since API 2
     */
    @DeviceApi
    public StorageType getType() {
        return mType;
    }

    /**
     * Returns mass storage name
     *
     * @return String identifier to access the target mass storage, usually it's matched with path.
     * @since API 2
     */
    @DeviceApi
    public String getName() {
        return mName;
    }

    /**
     * Returns the size of storage
     *
     * @return long size of storage
     * @since API 2
     */
    @DeviceApi
    public long getTotalSpace() {
        return mTotalSpace;
    }

    /**
     * Returns unoccupied space on the storage
     *
     * @return long unoccupied space of storage
     * @since API 2
     */
    @DeviceApi
    public long getFreeSpace() {
        return mFreeSpace;
    }

    /**
     * Returns whether the mass storage mounted successfully or not
     *
     * @return boolean True if massstorage is mounted and ready to use
     * @since API 2
     */
    @DeviceApi
    public boolean isMounted() {
        return mIsMounted;
    }

    /**
     * Returns storage directory
     *
     * @return String externalFileDirectory path
     * @since API 2
     */
    @DeviceApi
    public String getExternalFileDirectory() {
        return mExternalFileDirectory;
    }

    /**
     * Returns protocol to access files from {@link #getExternalFileDirectory()}
     *
     * @return Protocol protocol
     * @since API 2
     */
    @DeviceApi
    public Protocol getProtocol() {
        return mProtocol;
    }

    /**
     * <p>Returns volumeName of usb storage type</p>
     *
     * @return String volumeName
     * @since API 3
     */
    @DeviceApi
    public String getVolumeName() {
        return (mVolumeName == null) ? "" : mVolumeName;
    }

    /**
     * @hide internal
     */
    @Override
    public String toString() {
        return "MassStorageInfo{" +
                "type=" + mType +
                ", name=" + mName +
                ", volumeName=" + mVolumeName +
                ", totalSpace=" + mTotalSpace +
                ", freeSpace=" + mFreeSpace +
                ", isMounted=" + mIsMounted +
                ", externalFileDirectory=" + mExternalFileDirectory +
                ", protocol=" + mProtocol +
                '}';
    }
}
