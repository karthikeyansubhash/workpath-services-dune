package com.hp.jetadvantage.link.api.job;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

/**
 * Copy Job state details
 *
 * @since API 3
 */
public class CopyJobState implements JobInfo.JobState {
    /**
     * Copy specific job states
     * @since API 3
     */
    @Keep
    public enum State {
        /**
         * Job is processing.
         *
         * @since API 3
         */
        ACTIVE,
        /**
         * Job was copied successfully.
         *
         * @since API 3
         */
        COMPLETED,
        /**
         * Job was canceled by user or the CancelJob method.
         *
         * @since API 3
         */
        CANCELED,
        /**
         * Job failed prior to completion.
         *
         * @since API 3
         */
        FAILED
    }

    /**
     * Copy specific job sub-states
     * @since API 3
     */
    @Keep
    public enum ActivityState {
        /**
         * Activity is not started.
         *
         * @since API 3
         */
        NOT_STARTED,
        /**
         * Activity is started.
         *
         * @since API 3
         */
        STARTED,
        /**
         * Activity is completed.
         *
         * @since API 3
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
    private ActivityState mPrintingState;
    @Keep
    private ActivityState mCancelingState;

    /**
     * Constructor
     * @param state of job
     * @hide client should not create this class, only read received data
     */
    public CopyJobState(final State state) {
        this.mState = state;
    }

    /**
     * Return job state
     * @return job state
     * @since API 3
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
     * State of scanning phase of copy job
     * @return scanning state
     * @since API 3
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
     * State of processing phase of copy job
     * @return processing state
     * @since API 3
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
     * State of printing phase of copy job
     * @return printing state
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    @Keep
    public ActivityState getPrintingState() {
        return mPrintingState;
    }

    /**
     * Sets the state
     * @param printingState of job
     * @hide client should not change this class, only read received data
     */
    public void setPrintingState(ActivityState printingState) {
        mPrintingState = printingState;
    }

    /**
     * State of canceling phase of copy job
     * @return canceling state
     * @since API 3
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

    private CopyJobState(Parcel in) {
        mState = (State) in.readSerializable();
        mScanningState = (ActivityState) in.readSerializable();
        mProcessingState = (ActivityState) in.readSerializable();
        mPrintingState = (ActivityState) in.readSerializable();
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
        out.writeSerializable(mPrintingState);
        out.writeSerializable(mCancelingState);
    }

    /**
     * @hide internal
     */
    public static final Parcelable.Creator<CopyJobState> CREATOR = new Parcelable.Creator<CopyJobState>() {
        @Override
        public CopyJobState createFromParcel(Parcel in) {
            return new CopyJobState(in);
        }

        @Override
        public CopyJobState[] newArray(int size) {
            return new CopyJobState[size];
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
                ", printingState=" + mPrintingState +
                ", cancelingState=" + mCancelingState +
                ']';
    }
}