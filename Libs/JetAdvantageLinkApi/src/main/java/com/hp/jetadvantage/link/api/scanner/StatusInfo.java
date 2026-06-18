// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.scanner;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.common.Sdk;

/**
 * Provides status details of the scanner
 *
 * @since API 1
 */
public class StatusInfo implements Parcelable {
    /**
     * Scanner status condition
     *
     * @since API 1
     */
    @Keep
    public enum StatusCondition {
        /**
         * Status condition is unknown
         *
         * @since API 1
         */
        UNKNOWN,

        /**
         * Status condition is presented
         *
         * @since API 1
         */
        TRUE,

        /**
         * Status condition is not presented
         *
         * @since API 1
         */
        FALSE
    }

    @Keep
    private int mVersion;
    @Keep
    private boolean mIsOnline;
    @Keep
    private boolean mIsBusy;
    @Keep
    private StatusCondition mIsAdfOutputBinFull;
    @Keep
    private StatusCondition mIsPaperInAdf;
    @Keep
    private StatusCondition mIsPaperInFlatbed;

    /**
     * @hide for internal use
     */
    public StatusInfo(boolean isOnline, boolean isBusy,
            StatusCondition isAdfOutputBinFull, StatusCondition isPaperInAdf,
            StatusCondition isPaperInFlatbed) {
        mVersion = Sdk.VERSION.LEVEL;
        mIsOnline = isOnline;
        mIsBusy = isBusy;
        mIsAdfOutputBinFull = isAdfOutputBinFull;
        mIsPaperInAdf = isPaperInAdf;
        mIsPaperInFlatbed = isPaperInFlatbed;
    }

    /**
     * Returns whether scanner service is online or not on device
     *
     * @return true if online
     * @since API 1
     */
    @SuppressWarnings("unused")
    @Keep
    public boolean isOnline() {
        return mIsOnline;
    }

    /**
     * Returns whether scanner is busy or not
     *
     * @return true if busy
     * @since API 1
     */
    @SuppressWarnings("unused")
    @Keep
    public boolean isBusy() {
        return mIsBusy;
    }

    /**
     * Returns whether ADF output bin is full or not
     *
     * @return true if full
     * @since API 1
     */
    @SuppressWarnings("unused")
    @Keep
    public StatusCondition isAdfOutputBinFull() {
        return mIsAdfOutputBinFull;
    }

    /**
     * Returns whether paper placed in ADF or not
     *
     * @return true if paper presents in ADF
     * @since API 1
     */
    @SuppressWarnings("unused")
    @Keep
    public StatusCondition isPaperInAdf() {
        return mIsPaperInAdf;
    }

    /**
     * Returns whether paper placed in Flatbed or not
     *
     * @return true if paper presents in Flatbed
     * @since API 1
     */
    @SuppressWarnings("unused")
    @Keep
    public StatusCondition isPaperInFlatbed() {
        return mIsPaperInFlatbed;
    }

    @SuppressLint("RestrictedApi")
    private StatusInfo(Parcel in) {
        mVersion = in.readInt();
        Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);

        mIsOnline = in.readByte() != 0;
        mIsBusy = in.readByte() != 0;
        mIsAdfOutputBinFull = (StatusCondition) in.readSerializable();
        mIsPaperInAdf = (StatusCondition) in.readSerializable();
        mIsPaperInFlatbed = (StatusCondition) in.readSerializable();
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mVersion);
        dest.writeByte((byte) (mIsOnline ? 1 : 0));
        dest.writeByte((byte) (mIsBusy ? 1 : 0));
        dest.writeSerializable(mIsAdfOutputBinFull);
        dest.writeSerializable(mIsPaperInAdf);
        dest.writeSerializable(mIsPaperInFlatbed);
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
    public static final Creator<StatusInfo> CREATOR = new Creator<StatusInfo>() {
        @Override
        public StatusInfo createFromParcel(Parcel in) {
            return new StatusInfo(in);
        }

        @Override
        public StatusInfo[] newArray(int size) {
            return new StatusInfo[size];
        }
    };

    /**
     * @hide This is hidden because it should be understood without documenting in the javadoc
     */
    @Override
    @Keep
    public String toString() {
        return new StringBuilder().append("[").append("IsOnline=").append(mIsOnline).append("]").toString();
    }
}
