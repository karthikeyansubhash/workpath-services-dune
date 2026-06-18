// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.accessory.hid;

import androidx.annotation.NonNull;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Report data object to hold accessory data of HID type
 *
 * @since API 3
 */
public class HIDReport implements Parcelable {
    private HIDReportType mHidReportType;
    private byte[] mData;

    /**
     * Creates new report data
     *
     * @param hidReportType The type of report
     * @param data The data for reporting
     *
     * @since API 3
     */
    public HIDReport(@NonNull final HIDReportType hidReportType, @NonNull final byte[] data) {
        this.mHidReportType = hidReportType;
        this.mData = data;
    }

    /**
     * @hide parcelable implementation
     */
    private HIDReport(Parcel in) {
        mHidReportType = (HIDReportType) in.readSerializable();
        mData = in.createByteArray();
    }

    /**
     * Report type
     *
     * @since API 3
     *
     * @return HIDReportType report type
     */
    public HIDReportType getType() {
        return mHidReportType;
    }

    /**
     * Report data
     *
     * @since API 3
     *
     * @return report data
     */
    public byte[] getData() {
        return mData;
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(mHidReportType);
        dest.writeByteArray(mData);
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
    public static final Creator<HIDReport> CREATOR = new Creator<HIDReport>() {
        @Override
        public HIDReport createFromParcel(Parcel in) {
            return new HIDReport(in);
        }

        @Override
        public HIDReport[] newArray(int size) {
            return new HIDReport[size];
        }
    };

    /**
     * @hide This is hidden because it should be understood without documenting in the javadoc
     */
    @Override
    public String toString() {
        return "HIDReport{" +
                "hidReportType=" + mHidReportType +
                '}';
    }
}
