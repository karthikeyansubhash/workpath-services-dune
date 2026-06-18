// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.joblet.model;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.hp.jetadvantage.link.api.ErrorCode;
import com.hp.jetadvantage.link.api.copier.CopyAttributes;
import com.hp.jetadvantage.link.api.job.CopyJobData;
import com.hp.jetadvantage.link.api.job.CopyJobState;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.Joblet;
import com.hp.jetadvantage.link.api.job.PrintJobData;
import com.hp.jetadvantage.link.api.job.PrintJobState;
import com.hp.jetadvantage.link.api.job.ScanJobData;
import com.hp.jetadvantage.link.api.job.ScanJobState;
import com.hp.jetadvantage.link.api.printer.PrintAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.common.ssp.SpsCauseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to present job in JobDataMap.
 *
 * <b>Only for internal use and shouldn't be touched in JetAdvantageLink package.
 * Otherwise Parcelable compatibility might be broken.</b>.
 */
public final class JobData implements Parcelable {
    private static final String TAG = "Joblet";

    /**
     * For pending intents list
     */
    public static final String KEY_PENDING_INTENT_LIST = "pendingIntentList";

    public final List<Intent> mPendingIntents = new ArrayList<>(1);    // Created with at least 1
    public boolean mShowUi = false;
    public boolean mEmbeddedMonitor = true;
    public String mRid;
    public JobState mState = JobState.TL_ST_IDLE;
    private boolean mConfirmed = false; // Special flag to avoid UP inconsistency.
    public JobInfo.JobType mDeviceType;
    public JobInfo.JobState mDeviceJobState;
    public String mJobName;
    public String mJobId;
    public ScanAttributes.Duplex mScanPlex;
    public ScanAttributes.ScanSize mScanSize;
    public ScanAttributes.Destination mScanDestination;
    public PrintAttributes.Duplex mPrintPlex;
    public PrintAttributes.PaperSource mPaperSource;
    public PrintAttributes.PaperSize mPaperSize;
    public PrintAttributes.Source mPrintSource;
    public CopyAttributes.Duplex mCopyPlex;
    public CopyAttributes.ScanSize mCopySize;
    public CopyAttributes.JobExecutionMode mJobExecutionMode;
    public boolean mIsSuccess = false;
    public boolean mIsSingleSegmentScan = false;
    public int mNumCopies = 0;
    public int mScanImageCount = 0;
    public int mScanImageProcessedCount = 0;
    public int mScanImageTransmittedCount = 0;
    public int mPrintImageCount = 0;
    public int mSetCount = 0;
    public int mSheetCount = 0;
    public int mDestinationCount = 0;
    public int mDestinationTotal = 0;
    public boolean scanningCompleted = false;

    /**
     * Cached processing info
     */
    public List<ErrorCode> mProcessingCauses = null;
    /**
     * client which has submitted job, it can be null for API 1 and for jobs which doesn't belong to SDK
     */
    public String mClientPackage = null;
    /**
     * API Level version of client which has submitted job
     */
    public int mClientVersion = Sdk.VERSION_LEVEL.ONE;
    /**
     * Source, origin of this job, {@link JobSource#SDK} is highest priority value and can replace any other
     */
    public JobSource mSource = JobSource.UNKNOWN;
    /**
     * The job's "owner", i.e., the authenticated user that submitted the job request (the primary principal).
     * The principal to use for accounting purposes in the accountingPrincipal field.
     */
    public String mOwner;
    /**
     * The principal to use for accounting purposes. The primary principal is carried in the owner field.
     */
    public String mAccountingPrincipal;
    /**
     * Time of job start processing, effective 'start' time.
     * This is usually later than submit time, since processing start can take some time.
     * <p>
     * Note: it's in UTC and you need to convert to local timestamp if needed using android APIs.
     */
    public long mStartUTC;
    /**
     * Time of job complete time.
     * Shouldn't be used before completion callback.
     * <p>
     * Note: it's in UTC and you need to convert to local timestamp if needed using android APIs.
     */
    public long mCompleteUTC;
    /**
     * Up originated state, can be null, valid values should be ensured only for final callbacks
     */
    public String mUpState;

    /**
     * List of scanned file paths
     */
    public List<String> mFileNames;

    public void addPendingIntent(final Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey(Joblet.Keys.KEY_PENDING_INTENT)) {
                SLog.d(TAG, "Received bundle contains pending intent key.  Adding...");
                Intent pI = bundle.getParcelable(Joblet.Keys.KEY_PENDING_INTENT);
                addPendingIntent(pI);
            }
            if (bundle.containsKey(KEY_PENDING_INTENT_LIST)) {
                SLog.d(TAG, "Received bundle contains pending intent list key.  Adding...");
                List<Intent> piList = bundle.getParcelableArrayList(KEY_PENDING_INTENT_LIST);

                if (piList != null) {
                    for (final Intent pi : piList) {
                        addPendingIntent(pi);
                    }
                }
            }
        }
    }

    public void addPendingIntent(final Intent pi) {
        if (pi != null) {
            mPendingIntents.add(pi);
        }
    }

    public JobState updateState(final JobState state) {
        JobState oldState = mState;
        mState = state;
        SLog.d(TAG, "State changed from " + oldState.toString() + " -> " + mState.toString());
        return mState;
    }

    public void setConfirmed(final boolean confirm) {
        mConfirmed = confirm;
    }

    public JobState getState() {
        return mState;
    }

    public void log() {
        SLog.v(TAG, toString());
    }

    public String toString() {

        return "JobData ["
                + "showUi=" + mShowUi + ", "
                + "embeddedMonitor=" + mEmbeddedMonitor + ", "
                + "isSuccess=" + mIsSuccess + ", "
                + "jobState: " + ((mState == null) ? "null" : mState.name()) + ", "
                + "confirmed=" + mConfirmed + ", "
                + "deviceJobType=" + ((mDeviceType == null) ? "null" : mDeviceType.toString()) + ", "
                + "jobName=" + mJobName + ", "
                + "jobId=" + mJobId + ", "
                + "source=" + mSource.name() + ", "
                + "singleSegmentScan=" + mIsSingleSegmentScan + ", "
                + "mScanImageScannedCount=" + mScanImageCount + ", "
                + "mScanImageProcessedCount=" + mScanImageProcessedCount + ", "
                + "mScanImageTransmittedCount=" + mScanImageTransmittedCount + ", "
                + "mPrintImageCount=" + mPrintImageCount + ", "
                + "setCount=" + mSetCount + ", "
                + "sheetCount=" + mSheetCount + ", "
                + "destinationCount=" + mDestinationCount + ", "
                + "destinationTotal=" + mDestinationTotal + ", "
                + "scanningCompleted=" + scanningCompleted + ", "
                + "numCopies=" + mNumCopies + ", "
                + "ownerPrincipal=" + mOwner + ", "
                + "accountingPrincipal=" + mAccountingPrincipal + ", "
                + "startUTC=" + mStartUTC + ", "
                + "completeUTC=" + mCompleteUTC + ", "
                + "pendingIntents.size=" + mPendingIntents.size() + "]";
    }

    public List<Intent> getPendingIntents() {
        return mPendingIntents;
    }

    public void setProcessingCauses(final List<ErrorCode> causes) {
        if (mProcessingCauses == null) {
            mProcessingCauses = new ArrayList<>();
        }

        if (causes != null) {
            mProcessingCauses.addAll(causes);
            SLog.d(TAG, "Stored causes " + mProcessingCauses);
        }
    }

    public String getCause() {
        if (mProcessingCauses != null) {
            return SpsCauseHelper.generateJsonWithCauses(mProcessingCauses);
        }

        return null;
    }

    /**
     * Updates job source.
     * If source will be updated effectively depends on sources priorities.
     *
     * @param source to be updated with
     */
    public void updateSource(final JobSource source) {
        if (source == JobSource.SDK) {
            mSource = JobSource.SDK;
        } else if (source == JobSource.EXTERNAL) {
            if (mSource != JobSource.SDK) {
                mSource = source;
            }
        }
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeTypedList(mPendingIntents);
        dest.writeInt(mShowUi ? 1 : 0);
        dest.writeInt(mEmbeddedMonitor ? 1 : 0);
        dest.writeString(mState.name());
        dest.writeInt(mConfirmed ? 1 : 0);
        dest.writeSerializable(mDeviceType);
        dest.writeString(mJobName);
        dest.writeString(mJobId);
        dest.writeInt(mIsSuccess ? 1 : 0);
        dest.writeInt(mIsSingleSegmentScan ? 1 : 0);
        dest.writeInt(mNumCopies);
        dest.writeInt(mScanImageCount);
        dest.writeInt(mScanImageProcessedCount);
        dest.writeInt(mScanImageTransmittedCount);
        dest.writeInt(mPrintImageCount);
        dest.writeInt(mSetCount);
        dest.writeInt(mSheetCount);
        dest.writeInt(mDestinationCount);
        dest.writeInt(mDestinationTotal);
        dest.writeTypedList(mProcessingCauses);
        dest.writeString(mClientPackage);
        dest.writeString(mSource.name());
        dest.writeString(mOwner);
        dest.writeString(mAccountingPrincipal);
        dest.writeLong(mStartUTC);
        dest.writeLong(mCompleteUTC);
        dest.writeString(mUpState);

        dest.writeInt(scanningCompleted ? 1 : 0);
    }

    public static final Creator<JobData> CREATOR = new Creator<JobData>() {
        @Override
        public JobData createFromParcel(final Parcel source) {
            final JobData data = new JobData();

            source.readTypedList(data.mPendingIntents, Intent.CREATOR);
            data.mShowUi = (source.readInt() != 0);
            data.mEmbeddedMonitor = (source.readInt() != 0);
            data.mState = JobState.valueOf(source.readString());
            data.mConfirmed = (source.readInt() != 0);
            data.mDeviceType = (JobInfo.JobType) source.readSerializable();
            data.mJobName = source.readString();
            data.mJobId = source.readString();
            data.mIsSuccess = (source.readInt() != 0);
            data.mIsSingleSegmentScan = (source.readInt() != 0);
            data.mNumCopies = source.readInt();
            data.mScanImageCount = source.readInt();
            data.mScanImageProcessedCount = source.readInt();
            data.mScanImageTransmittedCount = source.readInt();
            data.mPrintImageCount = source.readInt();
            data.mSetCount = source.readInt();
            data.mSheetCount = source.readInt();
            data.mDestinationCount = source.readInt();
            data.mDestinationTotal = source.readInt();
            source.readTypedList(data.mProcessingCauses, ErrorCode.CREATOR);
            data.mClientPackage = source.readString();
            data.mSource = JobSource.valueOf(source.readString());
            data.mOwner = source.readString();
            data.mAccountingPrincipal = source.readString();
            data.mStartUTC = source.readLong();
            data.mCompleteUTC = source.readLong();
            data.mUpState = source.readString();

            data.scanningCompleted = (source.readInt() != 0);

            return data;
        }

        @Override
        public JobData[] newArray(final int size) {
            return new JobData[size];
        }
    };

    /**
     * Composes {@link JobInfo} based on this data
     *
     * @param jobData to compose from
     * @return {@link JobInfo}
     */
    public static JobInfo getJobInfo(final JobData jobData) {
        if (jobData == null) {
            return null;
        }

        final JobInfo info = new JobInfo();

        // Important! For objects sent to the client the client version must be used!
        info.setVersion(jobData.mClientVersion);

        info.setJobId(jobData.mJobId);

        info.setJobType(jobData.mDeviceType);
        if (jobData.mDeviceType == JobInfo.JobType.PRINT) {

            PrintJobData printJobData = new PrintJobData();

            // Important! For objects sent to the client the client version must be used!
            printJobData.setVersion(jobData.mClientVersion);

            printJobData.setImpressionsPrinted(jobData.mPrintImageCount);
            printJobData.setSheetsPrinted(jobData.mSheetCount);
            printJobData.setCopies(jobData.mNumCopies);
            printJobData.setDuplex(jobData.mPrintPlex);
            printJobData.setSource(jobData.mPrintSource);

            info.setJobData(printJobData);

        } else if (jobData.mDeviceType == JobInfo.JobType.SCAN) {
            ScanJobData scanJobData = new ScanJobData();

            // Important! For objects sent to the client the client version must be used!
            scanJobData.setVersion(jobData.mClientVersion);

            scanJobData.setImagesScanned(jobData.mScanImageCount);
            scanJobData.setImagesProcessed(jobData.mScanImageProcessedCount);
            scanJobData.setImagesTransmitted(jobData.mScanImageTransmittedCount);
            scanJobData.setDuplex(jobData.mScanPlex);
            scanJobData.setScanSize(jobData.mScanSize);
            scanJobData.setFileNames(jobData.mFileNames);
            scanJobData.setDestination(jobData.mScanDestination);

            info.setJobData(scanJobData);
        } else if (jobData.mDeviceType == JobInfo.JobType.COPY) {
            CopyJobData copyJobData = new CopyJobData();

            // Important! For objects sent to the client the client version must be used!
            copyJobData.setVersion(jobData.mClientVersion);

            copyJobData.setImagesScanned(jobData.mScanImageCount);
            copyJobData.setSheetsPrinted(jobData.mSheetCount);
            copyJobData.setDuplex(jobData.mCopyPlex);
            copyJobData.setScanSize(jobData.mCopySize);
            copyJobData.setJobExecutionMode(jobData.mJobExecutionMode);

            info.setJobData(copyJobData);
        }

        if (jobData.mDeviceType == JobInfo.JobType.PRINT) {
            PrintJobData data = info.getJobData();
            PrintJobState jobState = (PrintJobState) jobData.mDeviceJobState;
            data.setJobState(jobState);
        } else if (jobData.mDeviceType == JobInfo.JobType.SCAN) {
            ScanJobData data = info.getJobData();
            ScanJobState jobState = (ScanJobState) jobData.mDeviceJobState;
            data.setJobState(jobState);
        } else if (jobData.mDeviceType == JobInfo.JobType.COPY) {
            CopyJobData data = info.getJobData();
            CopyJobState jobState = (CopyJobState) jobData.mDeviceJobState;
            data.setJobState(jobState);
        }

        if (jobData.mJobName != null) {
            info.setJobName(jobData.mJobName);
        } else if (info.getJobType() != null) {
            switch (info.getJobType()) {
                case SCAN:
                    info.setJobName("Scan " + jobData.mJobId);
                    break;
                case PRINT:
                    info.setJobName("Print " + jobData.mJobId);
                    break;
                case COPY:
                    info.setJobName("Copy " + jobData.mJobId);
                    break;
            }
        }
        info.setOwner(jobData.mOwner);
        info.setStartTime(jobData.mStartUTC);
        info.setCompleteTime(jobData.mCompleteUTC);

        return info;
    }
}
