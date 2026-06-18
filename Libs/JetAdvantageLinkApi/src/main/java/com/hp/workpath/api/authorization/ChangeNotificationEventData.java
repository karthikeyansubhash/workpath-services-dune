// Copyright 2025 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.authorization;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * The ChangeNotificationEventData class provides information about change notification events.
 * This class includes the event code and the timestamp of the event.
 *
 * @since API 9
 */
public class ChangeNotificationEventData implements Parcelable {
    protected ChangeNotificationEventCode eventCode;
    protected Timestamp timestamp;

    public ChangeNotificationEventData() {
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @SuppressLint("RestrictedApi")
    private ChangeNotificationEventData(Parcel in) {
        eventCode = in.readParcelable(ChangeNotificationEventCode.class.getClassLoader());
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
    }

    /**
     * Returns the event code of the change notification event.
     *
     * @return The event code.
     * @since API 9
     */
    public String getEventCode() {
        return eventCode.value();
    }

    /**
     * Returns the timestamp of the change notification event.
     *
     * @return The timestamp.
     * @since API 9
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * @hide for internal use
     */
    public void setEventCode(ChangeNotificationEventCode eventCode) {
        this.eventCode = eventCode;
    }

    /**
     * @hide for internal use
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    public static final Parcelable.Creator<ChangeNotificationEventData> CREATOR = new Parcelable.Creator<ChangeNotificationEventData>() {

        @Override
        public ChangeNotificationEventData createFromParcel(final Parcel in) {
            return new ChangeNotificationEventData(in);
        }

        @Override
        public ChangeNotificationEventData[] newArray(final int size) {
            return new ChangeNotificationEventData[size];
        }
    };

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @Override
    public void writeToParcel(final Parcel parcel, int flags) {
        parcel.writeParcelable(eventCode, flags);
        parcel.writeParcelable(timestamp, flags);
    }

}
