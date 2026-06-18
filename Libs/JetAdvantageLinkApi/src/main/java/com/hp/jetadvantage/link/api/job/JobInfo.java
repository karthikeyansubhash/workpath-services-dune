// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.job;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.common.Sdk;

/**
 * Provides the jobtype to distinguish print and scan, jobstate to get the current status and jobdata to retrieve job details.
 *
 * @since API 1
 */
public final class JobInfo implements Parcelable {
    /**
     * @hide
     */
    public JobInfo() {
        mVersion = Sdk.VERSION.LEVEL;
    }

    /**
     * Defines job types of submitted jobs
     *
     * @since API 1
     */
    @Keep
    public enum JobType {
        /**
         * Print job
         *
         * @since API 1
         */
        PRINT,
        /**
         * Scan job
         *
         * @since API 1
         */
        SCAN,
        /**
         * Copy job
         *
         * @since API 3
         */
        COPY
    }

    /**
     * Provides interface for specific job details
     *
     * @since API 1
     */
    @Keep
    public interface JobData extends Parcelable {

    }

    /**
     * Provides interface for specific job state
     *
     * @since API 1
     */
    @Keep
    public interface JobState extends Parcelable {

    }

    /**
     * Version of info. Important to maintain to avoid Parcel breakage
     */
    @Keep
    private int mVersion;

    /**
     * The job Id for which the Job Info is being provided
     *
     * @since API 1
     */
    @Keep
    private String mJobId;

    /**
     * The type of Job. </br>
     *
     * @since API 1
     */
    @Keep
    private JobType mJobType;

    /**
     * Jobs name, can be null
     *
     * @since API 1
     */
    @Keep
    private String mJobName;

    /**
     * Owner, who has submitted the job
     *
     * @since API 1
     */
    @Keep
    private String mOwner;

    /**
     * UTC start timestamp
     *
     * @since API 1
     */
    @Keep
    private long mStartTime;

    /**
     * UTC complete timestamp. Can hold invalid value before job completed really.
     *
     * @since API 1
     */
    @Keep
    private long mCompleteTime;

    /**
     * Job details specific for job type
     *
     * @since API 1
     */
    @Keep
    private JobData mJobData;

    /**
     * Returns jobId
     * @return String job id
     * @since API 1
     */
    @Keep
    public String getJobId() {
        return mJobId;
    }

    /**
     * Sets jobId
     * @param jobId job id
     * @hide for internal use.
     */
    public void setJobId(String jobId) {
        this.mJobId = jobId;
    }

    /**
     * Returns jobType : print or scan or copy
     * @return JobType job type
     * @since API 1
     */
    @Keep
    public JobType getJobType() {
        return mJobType;
    }

    /**
     * @param jobType job type
     * @hide for internal use.
     */
    public void setJobType(JobType jobType) {
        this.mJobType = jobType;
    }

    /**
     * Returns jobname
     * @return String job name
     * @since API 1
     */
    @Keep
    public String getJobName() {
        return mJobName;
    }

    /**
     * @param jobName job name
     * @hide for internal use.
     */
    public void setJobName(String jobName) {
        this.mJobName = jobName;
    }

    /**
     * Returns owner who starts a job
     * @return String job owner
     * @since API 1
     */
    @Keep
    public String getOwner() {
        return mOwner;
    }

    /**
     * @param owner job owner
     * @hide for internal use.
     */
    public void setOwner(String owner) {
        this.mOwner = owner;
    }

    /**
     * Returns time for starting a job
     * @return long time when job started (UTC)
     * @since API 1
     */
    @Keep
    public long getStartTime() {
        return mStartTime;
    }

    /**
     * @param startTime time when job started (UTC)
     * @hide for internal use.
     */
    public void setStartTime(long startTime) {
        this.mStartTime = startTime;
    }

    /**
     * Returns time after finishing a job
     * @return long time when job completed (UTC)
     * @since API 1
     */
    @Keep
    public long getCompleteTime() {
        return mCompleteTime;
    }

    /**
     * @param completeTime time when job completed (UTC)
     * @hide for internal use.
     */
    public void setCompleteTime(long completeTime) {
        this.mCompleteTime = completeTime;
    }

    /**
     * Returns job type specific details.<br>
     * Result is an instance of {@link PrintJobData} or {@link ScanJobData} or {@link CopyJobData}
     * @return JobData job specific details
     * @since API 1
     */
    @Keep
    public <T extends JobData> T getJobData() {
        return (T) mJobData;
    }

    /**
     * @param mJobData job specific details
     * @hide for internal use.
     */
    public void setJobData(JobData mJobData) {
        this.mJobData = mJobData;
    }

    /**
     * Internal parcelable version
     *
     * @return internal version
     * @hide for internal use
     */
    @SuppressWarnings({"unused"})
    public int getVersion() {
        return mVersion;
    }

    /**
     * Internal parcelable version
     * @param version version for client
     * @hide for internal use
     */
    @SuppressWarnings({"unused"})
    public void setVersion(int version) {
        mVersion = version;
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @Override
    public int describeContents() {
        return hashCode();
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeInt(mVersion);
        parcel.writeString(mJobId);
        parcel.writeSerializable(mJobType);
        parcel.writeString(mJobName);
        parcel.writeString(mOwner);
        parcel.writeLong(mStartTime);
        parcel.writeLong(mCompleteTime);
        parcel.writeParcelable(mJobData, 0);
    }

    /**
     * Parcelable constructor
     *
     * @param in Parcel to create data from
     */
    @SuppressLint("RestrictedApi")
    private JobInfo(Parcel in) {
        mVersion = in.readInt();
        Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);

        mJobId = in.readString();
        mJobType = (JobType) in.readSerializable();
        mJobName = in.readString();
        mOwner = in.readString();
        mStartTime = in.readLong();
        mCompleteTime = in.readLong();
        mJobData = in.readParcelable(JobData.class.getClassLoader());
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    public static final Creator<JobInfo> CREATOR = new Creator<JobInfo>() {
        @Override
        public JobInfo createFromParcel(Parcel in) {
            return new JobInfo(in);
        }

        @Override
        public JobInfo[] newArray(int size) {
            return new JobInfo[size];
        }
    };

    /**
     * @hide This is hidden because it should be understood without documenting in the javadoc
     */
    @Override
    @Keep
    public String toString() {
        return getClass().getSimpleName() + "["
                + "jobId=" + mJobId + ", "
                + "jobName=" + mJobName + ", "
                + "jobType=" + mJobType + ", "
                + "startTime=" + mStartTime + ", "
                + "completeTime=" + mCompleteTime
                + "]";
    }
}
