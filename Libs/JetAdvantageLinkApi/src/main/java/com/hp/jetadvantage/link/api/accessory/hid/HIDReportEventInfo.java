// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.accessory.hid;

import android.os.Parcel;

import com.hp.jetadvantage.link.api.accessory.ReportEventInfo;

import java.util.List;

/**
 * HID report event information
 * @since API 3
 */
public class HIDReportEventInfo extends ReportEventInfo {
    private long mOrdinal;
    private String mTimestamp;
    private List<HIDReport> mReports;

    /**
     * @hide
     */
    public HIDReportEventInfo(long ordinal, String timestamp,
            List<HIDReport> hidReports) {
        mOrdinal = ordinal;
        mTimestamp = timestamp;
        mReports = hidReports;
    }

    /**
     * Event ordinal
     *
     * @since API 3
     *
     * @return ordinal number
     */
    public long getOrdinal() {
        return mOrdinal;
    }

    /**
     * Event timestamp
     *
     * @since API 3
     *
     * @return timestamp
     */
    public String getTimestamp() {
        return mTimestamp;
    }

    /**
     * List of reports containing data
     *
     * @since API 3
     *
     * @return list of reports
     */
    public List<HIDReport> getReports() {
        return mReports;
    }

    /**
     * @hide parcelable implementation
     */
    private HIDReportEventInfo(Parcel in) {
        mOrdinal = in.readLong();
        mTimestamp = in.readString();
        mReports = in.createTypedArrayList(HIDReport.CREATOR);
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mOrdinal);
        dest.writeString(mTimestamp);
        dest.writeTypedList(mReports);
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
    public static final Creator<HIDReportEventInfo> CREATOR = new Creator<HIDReportEventInfo>() {
        @Override
        public HIDReportEventInfo createFromParcel(Parcel in) {
            return new HIDReportEventInfo(in);
        }

        @Override
        public HIDReportEventInfo[] newArray(int size) {
            return new HIDReportEventInfo[size];
        }
    };

    /**
     * @hide This is hidden because it should be understood without documenting in the javadoc
     */
    @Override
    public String toString() {
        return "HIDReportEventInfo{" +
                "ordinal=" + mOrdinal +
                ", timestamp='" + mTimestamp + '\'' +
                '}';
    }
}
