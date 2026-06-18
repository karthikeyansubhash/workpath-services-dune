// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.job;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

/**
 * Enumerations of the state for print Job
 *
 * @since API 1
 */
public class PrintJobState implements JobInfo.JobState {
    /**
     * The states of print job
     * @since API 1
     */
    @Keep
    public enum State {
        /**
         * Job is processing.
         *
         * @since API 1
         */
        PROCESSING,
        /**
         * Job was printed successfully.
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
         * Job is aborted.
         *
         * @since API 1
         */
        ABORTED,
        /**
         * Job processing is stopped.
         *
         * @since API 1
         */
        STOPPED,
        /**
         * Job is still active (not yet complete).
         *
         * @since API 1
         */
        PENDING,
        /**
         * Job is created, then it's held. It will not go to processing without release.
         *
         * @since API 1
         */
        HELD
    }

    @Keep
    private State mState;

    /**
     * Constructor
     * @param state of job
     * @hide client should not create this class, only read received data
     */
    public PrintJobState(final State state) {
        this.mState = state;
    }

    /**
     * Returns The state of print job
     * @return The current state of print job
     * @since API 1
     */
    @Keep
    public State getState() {
        return mState;
    }

    /**
     * Sets the state
     * @param state of job
     * @hide client should not change this class, only read received data
     */
    public void setState(PrintJobState.State state) {
        mState = state;
    }
    private PrintJobState(Parcel in) {
        mState = (State) in.readSerializable();
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
    }

    /**
     * @hide internal
     */
    public static final Parcelable.Creator<PrintJobState> CREATOR = new Parcelable.Creator<PrintJobState>() {
        @Override
        public PrintJobState createFromParcel(Parcel in) {
            return new PrintJobState(in);
        }

        @Override
        public PrintJobState[] newArray(int size) {
            return new PrintJobState[size];
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
                ']';
    }
}
