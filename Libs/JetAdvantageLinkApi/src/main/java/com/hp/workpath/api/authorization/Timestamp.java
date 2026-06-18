// Copyright 2025 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.authorization;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * The Timestamp class represents a timestamp with a time value and an offset.
 * This class includes the time as a string and the offset as a short value.
 *
 * @since API 9
 */
public class Timestamp implements Parcelable {
    protected String time;
    protected short offset;

    public Timestamp() {
        super();
    }

    /**
     * Constructs a new Timestamp with the specified time and offset.
     *
     * @param time   The time value as a string.
     * @param offset The offset value as a short.
     * @since API 9
     */
    public Timestamp(String time, short offset) {
        this.time = time;
        this.offset = offset;
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @SuppressLint("RestrictedApi")
    private Timestamp(final Parcel in) {
        this.time = in.readString();
        this.offset = (short) in.readInt();
    }

    /**
     * Returns the time value of the timestamp.
     *
     * @return The time value.
     * @since API 9
     */
    public String getTime() {
        return time;
    }

    /**
     * Returns the offset value of the timestamp.
     *
     * @return The offset value.
     * @since API 9
     */
    public short getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("[").append("time=").append(time).append(", ").append("offset=").append(offset).append("]").toString();
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    public static final Parcelable.Creator<Timestamp> CREATOR = new Parcelable.Creator<Timestamp>() {

        @Override
        public Timestamp createFromParcel(final Parcel in) {
            return new Timestamp(in);
        }

        @Override
        public Timestamp[] newArray(final int size) {
            return new Timestamp[size];
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
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeString(time);
        parcel.writeInt(offset);
    }

}
