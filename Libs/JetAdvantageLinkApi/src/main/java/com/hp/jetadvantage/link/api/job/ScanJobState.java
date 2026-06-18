// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.job;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

/**
 * Enumerations of the state for scan Job
 *
 * @since API 1
 */
public class ScanJobState implements JobInfo.JobState {
    /**
     * Returns the state of scan job
     * @since API 1
     */
    @Keep
    public enum State {
        /**
         * Job is still active (not yet complete).
         *
         * @since API 1
         */
        PENDING,
        /**
         * Job was scanned and delivered successfully.
         *
         * @since API 1
         */
        COMPLETED,
        /**
         * Job was canceled by user or the CancelJob method.
         *
         * @since API 1
         */
        CANCELED,
        /**
         * Job failed prior to completion.
         *
         * @since API 1
         */
        FAILED
    }

    /**
     * Returns the state of scan activity.
     * @since API 1
     */
    @Keep
    public enum ActivityState {
        /**
         * Activity is not started.
         *
         * @since API 1
         */
        NOT_STARTED,
        /**
         * Activity is started.
         *
         * @since API 1
         */
        STARTED,
        /**
         * Activity restarted.
         *
         * @since API 1
         */
        RESTARTED,
        /**
         * Activity is completed.
         *
         * @since API 1
         */
        COMPLETED
    }

    @Keep
    private State mState;
    @Keep
    private ActivityState mScanningState;
    @Keep
    private ActivityState mProcessingState;
    @Keep
    private ActivityState mTransmittingState;
    @Keep
    private ActivityState mCancelingState;

    /**
     * Constructor
     * @param state of job
     * @hide client should not create this class, only read received data
     */
    public ScanJobState(final State state) {
        this.mState = state;
    }

    /**
     * Returns the state of scan job
     * @return job state
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    @Keep
    public State getState() {
        return mState;
    }

    /**
     * Sets the state
     * @param state of job
     * @hide client should not change this class, only read received data
     */
    public void setState(State state) {
        mState = state;
    }

    /**
     * Returns the state of activity of scan job
     * @return scanning state
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    @Keep
    public ActivityState getScanningState() {
        return mScanningState;
    }

    /**
     * Sets the state
     * @param scanningState of job
     * @hide client should not change this class, only read received data
     */
    public void setScanningState(ActivityState scanningState) {
        mScanningState = scanningState;
    }

    /**
     * Returns the processing state of scan job
     * @return ActivityState processing state
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    @Keep
    public ActivityState getProcessingState() {
        return mProcessingState;
    }

    /**
     * Sets the state
     * @param processingState of job
     * @hide client should not change this class, only read received data
     */
    public void setProcessingState(ActivityState processingState) {
        mProcessingState = processingState;
    }

    /**
     * Returns the transmitting state of scan job
     * @return ActivityState transmitting state
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    @Keep
    public ActivityState getTransmittingState() {
        return mTransmittingState;
    }

    /**
     * Sets the state
     * @param transmittingState of job
     * @hide client should not change this class, only read received data
     */
    public void setTransmittingState(ActivityState transmittingState) {
        mTransmittingState = transmittingState;
    }

    /**
     * Returns the canceling state of scan job
     * @return ActivityState canceling state
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    @Keep
    public ActivityState getCancelingState() {
        return mCancelingState;
    }

    /**
     * Sets the state
     * @param cancelingState of job
     * @hide client should not change this class, only read received data
     */
    public void setCancelingState(ActivityState cancelingState) {
        mCancelingState = cancelingState;
    }

    private ScanJobState(Parcel in) {
        mState = (State) in.readSerializable();
        mScanningState = (ActivityState) in.readSerializable();
        mProcessingState = (ActivityState) in.readSerializable();
        mTransmittingState = (ActivityState) in.readSerializable();
        mCancelingState = (ActivityState) in.readSerializable();
    }

    /**
     * @hide internal
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @hide internal
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeSerializable(mState);
        out.writeSerializable(mScanningState);
        out.writeSerializable(mProcessingState);
        out.writeSerializable(mTransmittingState);
        out.writeSerializable(mCancelingState);
    }

    /**
     * @hide internal
     */
    public static final Parcelable.Creator<ScanJobState> CREATOR = new Parcelable.Creator<ScanJobState>() {
        @Override
        public ScanJobState createFromParcel(Parcel in) {
            return new ScanJobState(in);
        }

        @Override
        public ScanJobState[] newArray(int size) {
            return new ScanJobState[size];
        }
    };

    /**
     * @hide internal
     */
    @Override
    @Keep
    public String toString() {
        return getClass().getSimpleName() + "[" +
                "state=" + mState +
                ", scanningState=" + mScanningState +
                ", processingState=" + mProcessingState +
                ", transmittingState=" + mTransmittingState +
                ", cancelingState=" + mCancelingState +
                ']';
    }
}
