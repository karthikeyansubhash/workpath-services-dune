// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.accessory.hid;

import android.os.Parcel;
import android.os.Parcelable;

import com.hp.workpath.api.Convertor;

/**
 * HID information
 *
 * @since API 3
 */
public class HIDInfo implements Parcelable, Convertor {
    private int mFeatureReportLength;
    private int mInputReportLength;
    private int mOutputReportLength;
    private boolean mIsReading;

    /**
     * Creates new report
     *
     * @hide
     * @since API 3
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

    private HIDInfo(Object object) {
        if (object instanceof com.hp.jetadvantage.link.api.accessory.hid.HIDInfo) {
            mFeatureReportLength = ((com.hp.jetadvantage.link.api.accessory.hid.HIDInfo) object).getFeatureReportLength();
            mInputReportLength = ((com.hp.jetadvantage.link.api.accessory.hid.HIDInfo) object).getInputReportLength();
            mOutputReportLength = ((com.hp.jetadvantage.link.api.accessory.hid.HIDInfo) object).getOutputReportLength();
            mIsReading = ((com.hp.jetadvantage.link.api.accessory.hid.HIDInfo) object).isReading();
        }
    }

    /**
     * The length of the feature report for this accessory.
     *
     * @return length in bytes
     * @since API 3
     */
    public int getFeatureReportLength() {
        return mFeatureReportLength;
    }

    /**
     * The length of the input report for this accessory.
     *
     * @return length in bytes
     * @since API 3
     */
    public int getInputReportLength() {
        return mInputReportLength;
    }

    /**
     * The length of the output report for this accessory.
     *
     * @return length in bytes
     * @since API 3
     */
    public int getOutputReportLength() {
        return mOutputReportLength;
    }

    /**
     * True if the accessory is currently reading, otherwise false.
     *
     * @return true if reading
     * @since API 3
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
     * @hide trivial
     */
    public static final ConvertorCreator<HIDInfo> CREATOR_OBJ = new ConvertorCreator<HIDInfo>() {
        @Override
        public HIDInfo createFromObject(Object object) {
            return new HIDInfo(object);
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
