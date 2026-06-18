// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.accessory.hid;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * HID information
 *
 * @since API 3
 */
public class HIDInfo implements Parcelable {
    private int mFeatureReportLength;
    private int mInputReportLength;
    private int mOutputReportLength;
    private boolean mIsReading;

    /**
     * Creates new report
     *
     * @since API 3
     *
     * @hide
     */
    public HIDInfo(int mFeatureReportLength, int mInputReportLength, int mOutputReportLength, boolean mIsReading) {
        this.mFeatureReportLength = mFeatureReportLength;
        this.mInputReportLength = mInputReportLength;
        this.mOutputReportLength = mOutputReportLength;
        this.mIsReading = mIsReading;
    }

    /**
     * @hide parcelable implementation
     */
    private HIDInfo(Parcel in) {
        mFeatureReportLength = in.readInt();
        mInputReportLength = in.readInt();
        mOutputReportLength = in.readInt();
        mIsReading = in.readInt() != 0;
    }

    /**
     * The length of the feature report for this accessory.
     *
     * @since API 3
     *
     * @return length in bytes
     */
    public int getFeatureReportLength() {
        return mFeatureReportLength;
    }

    /**
     * The length of the input report for this accessory.
     *
     * @since API 3
     *
     * @return length in bytes
     */
    public int getInputReportLength() {
        return mInputReportLength;
    }

    /**
     * The length of the output report for this accessory.
     *
     * @since API 3
     *
     * @return length in bytes
     */
    public int getOutputReportLength() {
        return mOutputReportLength;
    }

    /**
     * True if the accessory is currently reading, otherwise false.
     *
     * @since API 3
     *
     * @return true if reading
     */
    public boolean isReading() {
        return mIsReading;
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mFeatureReportLength);
        dest.writeInt(mInputReportLength);
        dest.writeInt(mOutputReportLength);
        dest.writeInt(mIsReading ? 1 : 0);
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
    public static final Creator<HIDInfo> CREATOR = new Creator<HIDInfo>() {
        @Override
        public HIDInfo createFromParcel(Parcel in) {
            return new HIDInfo(in);
        }

        @Override
        public HIDInfo[] newArray(int size) {
            return new HIDInfo[size];
        }
    };

    /**
     * @hide This is hidden because it should be understood without documenting in the javadoc
     */
    @Override
    public String toString() {
        return "HIDReport{" +
                "featureReportLength=" + mFeatureReportLength +
                ", inputReportLength=" + mInputReportLength +
                ", outputReportLength=" + mOutputReportLength +
                ", isReading=" + mIsReading + '}';
    }
}
