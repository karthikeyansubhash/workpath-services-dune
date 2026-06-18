// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.printer;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.common.Sdk;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides status details of the printer
 *
 * @since API 1
 */
public class StatusInfo implements Parcelable {
    /**
     * Printer status
     *
     * @since API 1
     */
    @Keep
    public enum Status {
        /**
         * Device is ready to accept new jobs
         *
         * @since API 1
         */
        IDLE,

        /**
         * Device is processing a job
         *
         * @since API 1
         */
        PROCESSING,

        /**
         * No jobs can be processed, intervention required
         *
         * @since API 1
         */
        STOPPED
    }

    /**
     * A collection of status from a device
     *
     * @since API 1
     */
    @Keep
    public enum StatusReason {
        /**
         * Status is unknown
         *
         * @since API 1
         */
        UNKNOWN,

        /**
         * Other reason
         *
         * @since API 1
         */
        OTHER,

        /**
         * No reasons
         *
         * @since API 1
         */
        NONE,

        /**
         * Cover is opened
         *
         * @since API 1
         */
        COVER_OPEN,

        /**
         * Door is opened
         *
         * @since API 1
         */
        DOOR_OPEN,

        /**
         * Toner is empty
         *
         * @since API 1
         */
        TONER_EMPTY,

        /**
         * Toner is low
         *
         * @since API 1
         */
        TONER_LOW,

        /**
         * No paper
         *
         * @since API 1
         */
        MEDIA_EMPTY,

        /**
         * Paper is low
         *
         * @since API 1
         */
        MEDIA_LOW,

        /**
         * Paper is required
         *
         * @since API 1
         */
        MEDIA_NEEDED,

        /**
         * Paper jam
         *
         * @since API 1
         */
        MEDIA_JAM
    }

    @Keep
    private int mVersion;
    @Keep
    private Status mStatus;
    @Keep
    private List<StatusReason> mStatusReasons;

    /**
     * @hide for internal use
     */
    public StatusInfo(Status status, List<StatusReason> statusReasons) {
        mVersion = Sdk.VERSION.LEVEL;
        mStatus = status;
        mStatusReasons = statusReasons;
    }

    /**
     * Returns printer status, whether printer is ready to new jobs
     *
     * @return Status printer status
     * @since API 1
     */
    @Keep
    public Status getStatus() {
        return mStatus;
    }

    /**
     * Returns list of printer status reasons
     *
     * @return List list of details status reasons
     * @since API 1
     */
    @SuppressWarnings("unused")
    @Keep
    public List<StatusReason> getStatusReasons() {
        return mStatusReasons;
    }

    @SuppressLint("RestrictedApi")
    private StatusInfo(Parcel in) {
        mVersion = in.readInt();
        Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);

        mStatus = (Status) in.readSerializable();
        mStatusReasons = new ArrayList<>();
        in.readList(mStatusReasons, StatusReason.class.getClassLoader());
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mVersion);
        dest.writeSerializable(mStatus);
        dest.writeList(mStatusReasons);
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
     * @hide trivial
     */
    @Keep
    public String toString() {
        return new StringBuilder().append("[").append("Status=").append(((mStatus != null && mStatus.name() != null)?mStatus.name().toString():"null")).append("]").toString();
    }
}
