// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.printer;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.common.Sdk;

/**
 * Provides the tray info from device.
 *
 * @since API 1
 */
public class TrayInfo implements Parcelable {
    /**
     * Tray status (available to be used for printing or not)
     *
     * @since API 1
     */
    @Keep
    public enum Status {
        /**
         * Tray is available
         *
         * @since API 1
         */
        AVAILABLE,
        /**
         * Tray is unavailable
         *
         * @since API 1
         */
        UNAVAILABLE
    }

    @Keep
    private int mVersion;
    @Keep
    private PrintAttributes.PaperSource mPaperSource;
    @Keep
    private PrintAttributes.PaperSize mPaperSize;
    @Keep
    private PrintAttributes.PaperType mPaperType;
    @Keep
    private Status mStatus;
    @Keep
    private int mLevel;
    @Keep
    private int mCapacity;

    /**
     * @hide
     */
    public TrayInfo(PrintAttributes.PaperSource paperSource,
            PrintAttributes.PaperSize paperSize, PrintAttributes.PaperType paperType,
            Status status, int level, int capacity) {
        mVersion = Sdk.VERSION.LEVEL;
        mPaperSource = paperSource;
        mPaperSize = paperSize;
        mPaperType = paperType;
        mStatus = status;
        mLevel = level;
        mCapacity = capacity;
    }

    @SuppressLint("RestrictedApi")
    private TrayInfo(Parcel in) {
        mVersion = in.readInt();
        Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);

        mPaperSource = (PrintAttributes.PaperSource) in.readSerializable();
        mPaperSize = (PrintAttributes.PaperSize) in.readSerializable();
        mPaperType = (PrintAttributes.PaperType) in.readSerializable();
        mStatus = (Status) in.readSerializable();
        mLevel = in.readInt();
        mCapacity = in.readInt();
    }

    /**
     * Gets tray information for printing
     *
     * @return paper source
     * @since API 1
     */
    @SuppressWarnings("unused")
    @Keep
    public PrintAttributes.PaperSource getPaperSource() {
        return mPaperSource;
    }

    /**
     * Gets size of paper loaded in tray
     *
     * @return paper size
     * @since API 1
     */
    @SuppressWarnings("unused")
    @Keep
    public PrintAttributes.PaperSize getPaperSize() {
        return mPaperSize;
    }

    /**
     * Gets type of paper loaded in tray
     *
     * @return paper type
     * @since API 1
     */
    @SuppressWarnings("unused")
    @Keep
    public PrintAttributes.PaperType getPaperType() {
        return mPaperType;
    }

    /**
     * Gets tray status (Available or not)
     *
     * @return status
     * @since API 1
     */
    @SuppressWarnings("unused")
    @Keep
    public Status getStatus() {
        return mStatus;
    }

    /**
     * Gets current capacity of the tray sensed by device, returned as percentage.
     *
     * @return level
     * @since API 1
     */
    @SuppressWarnings("unused")
    @Keep
    public int getLevel() {
        return mLevel;
    }

    /**
     * Gets max capacity from a tray
     *
     * @return max capacity
     */
    @SuppressWarnings("unused")
    @Keep
    public int getCapacity() {
        return mCapacity;
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mVersion);
        dest.writeSerializable(mPaperSource);
        dest.writeSerializable(mPaperSize);
        dest.writeSerializable(mPaperType);
        dest.writeSerializable(mStatus);
        dest.writeInt(mLevel);
        dest.writeInt(mCapacity);
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
    public static final Creator<TrayInfo> CREATOR = new Creator<TrayInfo>() {
        @Override
        public TrayInfo createFromParcel(Parcel in) {
            return new TrayInfo(in);
        }

        @Override
        public TrayInfo[] newArray(int size) {
            return new TrayInfo[size];
        }
    };

    /**
     * @hide trivial
     */
    @Keep
    public String toString() {
        return new StringBuilder().append("[").append("IsOnline=").append(mStatus.name()).append("]").toString();
    }
}
