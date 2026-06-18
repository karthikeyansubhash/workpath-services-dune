// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.copier;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.common.Sdk;

/**
 * Stored job info from the printer.
 *
 * @since API 3
 */
public class StoredJobInfo implements Parcelable {
    /**
     * Version of info. Important to maintain to avoid Parcel breakage
     */
    private int mVersion;

    @Keep
    private final String mStoredJobId;

    @Keep
    private final String mStoredJobFolderName;

    @Keep
    private final String mStoredJobName;

    @Keep
    private final String mStoredJobUserName;

    @Keep
    private final JobCredentialsAttributes.PasswordType mStoredJobPasswordType;

    @Keep
    private final String mStoreJobTimestamp;

    @Keep
    private final int mCopies;

    @Keep
    private final CopyAttributes.ColorMode mColorMode;

    @Keep
    private final CopyAttributes.ScanSize mOriginalMediaSize;

    @Keep
    private final CopyAttributes.Duplex mOutputSides;

    @Keep
    private final int mTotalPages;

    /**
     * @hide
     */
    public StoredJobInfo(String mStoredJobId, String storedJobFolderName, String storedJobName, String storedJobUserName,
                         JobCredentialsAttributes.PasswordType storedJobPasswordType, String storeJobTimestamp, int copies,
                         CopyAttributes.ColorMode colorMode, CopyAttributes.ScanSize originalMediaSize,
                         CopyAttributes.Duplex outputSides, int totalPages) {
        this.mVersion = Sdk.VERSION.LEVEL;
        this.mStoredJobId = mStoredJobId;
        this.mStoredJobFolderName = storedJobFolderName;
        this.mStoredJobName = storedJobName;
        this.mStoredJobUserName = storedJobUserName;
        this.mStoredJobPasswordType = storedJobPasswordType;
        this.mStoreJobTimestamp = storeJobTimestamp;
        this.mCopies = copies;
        this.mColorMode = colorMode;
        this.mOriginalMediaSize = originalMediaSize;
        this.mOutputSides = outputSides;
        this.mTotalPages = totalPages;
    }

    @SuppressLint("RestrictedApi")
    private StoredJobInfo(Parcel in) {
        mVersion = in.readInt();
        Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);

        mTotalPages = in.readInt();
        mCopies = in.readInt();
        mStoredJobId = in.readString();
        mStoredJobFolderName = in.readString();
        mStoredJobName = in.readString();
        mStoredJobUserName = in.readString();
        mStoreJobTimestamp = in.readString();
        mStoredJobPasswordType = (JobCredentialsAttributes.PasswordType) in.readSerializable();
        mColorMode = (CopyAttributes.ColorMode) in.readSerializable();
        mOriginalMediaSize = (CopyAttributes.ScanSize) in.readSerializable();
        mOutputSides = (CopyAttributes.Duplex) in.readSerializable();
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mVersion);
        out.writeInt(mTotalPages);
        out.writeInt(mCopies);
        out.writeString(mStoredJobId);
        out.writeString(mStoredJobFolderName);
        out.writeString(mStoredJobName);
        out.writeString(mStoredJobUserName);
        out.writeString(mStoreJobTimestamp);
        out.writeSerializable(mStoredJobPasswordType);
        out.writeSerializable(mColorMode);
        out.writeSerializable(mOriginalMediaSize);
        out.writeSerializable(mOutputSides);
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
    public static final Creator<StoredJobInfo> CREATOR = new Creator<StoredJobInfo>() {
        @Override
        public StoredJobInfo createFromParcel(Parcel in) {
            return new StoredJobInfo(in);
        }

        @Override
        public StoredJobInfo[] newArray(int size) {
            return new StoredJobInfo[size];
        }
    };

    /**
     * Returns stored job id
     *
     * @return String stored job id
     * @since API 3
     */
    public String getStoredJobId() {
        return mStoredJobId;
    }

    /**
     * Returns stored job folder name
     *
     * @return String stored job folder name
     * @since API 3
     */
    public String getStoredJobFolderName() {
        return mStoredJobFolderName;
    }

    /**
     * Returns stored job name
     *
     * @return String stored job name
     * @since API 3
     */
    public String getStoredJobName() {
        return mStoredJobName;
    }

    /**
     * Returns username which creates stored job
     *
     * @return String username of stored job
     * @since API 3
     */
    public String getStoredJobUserName() {
        return mStoredJobUserName;
    }

    /**
     * Returns password type
     *
     * @return PasswordType password type of stored job
     * @since API 3
     */
    public JobCredentialsAttributes.PasswordType getStoredJobPasswordType() {
        return mStoredJobPasswordType;
    }

    /**
     * Returns timestampe of stored job
     *
     * @return String timestamp
     * @since API 3
     */
    public String getStoreJobTimestamp() {
        return mStoreJobTimestamp;
    }

    /**
     * Returns copies
     *
     * @return int copies
     * @since API 3
     */
    public int getCopies() {
        return mCopies;
    }

    /**
     * Returns color mode of stored job
     *
     * @return ColorMode color mode
     * @since API 3
     */
    public CopyAttributes.ColorMode getColorMode() {
        return mColorMode;
    }

    /**
     * Returns original media size
     *
     * @return ScanSize original media size
     * @since API 3
     */
    public CopyAttributes.ScanSize getOriginalMediaSize() {
        return mOriginalMediaSize;
    }

    /**
     * Returns output sides of stored job
     *
     * @return Duplex output sides
     * @since API 3
     */
    public CopyAttributes.Duplex getOutputSides() {
        return mOutputSides;
    }

    /**
     * Returns total pages of stored job
     *
     * @return int the number of total pages
     * @since API 3
     */
    public int getTotalPages() {
        return mTotalPages;
    }

    /**
     * @hide internal
     */
    @Override
    public String toString() {
        return "StoredJobInfo{" +
                "StoredJobId=" + mStoredJobId +
                ", StoreJobTimestamp=" + mStoreJobTimestamp +
                ", Copies=" + mCopies+
                ", ColorMode=" + mColorMode +
                ", OriginalMediaSize=" + mOriginalMediaSize +
                ", OutputSides=" + mOutputSides +
                ", TotalPages=" + mTotalPages +
                '}';
    }
}
