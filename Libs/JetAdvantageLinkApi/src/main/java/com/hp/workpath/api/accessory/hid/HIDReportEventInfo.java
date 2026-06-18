// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.accessory.hid;

import android.os.Parcel;
import android.os.Parcelable;

import com.hp.workpath.api.Convertor;
import com.hp.workpath.api.accessory.ReportEventInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * HID report event information
 *
 * @since API 3
 */
public class HIDReportEventInfo extends ReportEventInfo implements Convertor {
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
     * @return ordinal number
     * @since API 3
     */
    public long getOrdinal() {
        return mOrdinal;
    }

    /**
     * Event timestamp
     *
     * @return timestamp
     * @since API 3
     */
    public String getTimestamp() {
        return mTimestamp;
    }

    /**
     * List of reports containing data
     *
     * @return list of reports
     * @since API 3
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

    private HIDReportEventInfo(Object object) {
        if (object instanceof com.hp.jetadvantage.link.api.accessory.hid.HIDReportEventInfo) {
            mOrdinal = ((com.hp.jetadvantage.link.api.accessory.hid.HIDReportEventInfo) object).getOrdinal();
            mTimestamp = ((com.hp.jetadvantage.link.api.accessory.hid.HIDReportEventInfo) object).getTimestamp();

            List<HIDReport> hidReportList = new ArrayList<>();
            List<com.hp.jetadvantage.link.api.accessory.hid.HIDReport> hidReports = ((com.hp.jetadvantage.link.api.accessory.hid.HIDReportEventInfo) object).getReports();
            for (com.hp.jetadvantage.link.api.accessory.hid.HIDReport hidReport : hidReports) {
                hidReportList.add(HIDReport.CREATOR_OBJ.createFromObject(hidReport));
            }
            mReports = hidReportList;
        }
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
    public static final Parcelable.Creator<HIDReportEventInfo> CREATOR = new Parcelable.Creator<HIDReportEventInfo>() {
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
     * @hide trivial
     */
    public static final ConvertorCreator<HIDReportEventInfo> CREATOR_OBJ = new ConvertorCreator<HIDReportEventInfo>() {
        @Override
        public HIDReportEventInfo createFromObject(Object object) {
            return new HIDReportEventInfo(object);
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
