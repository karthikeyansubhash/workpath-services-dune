/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.copylet.service;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;

import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.copier.CopyAttributes;
import com.hp.jetadvantage.link.api.copier.CopyAttributesReader;
import com.hp.jetadvantage.link.api.copier.Copylet;
import com.hp.jetadvantage.link.api.copier.JobCredentialsAttributes;
import com.hp.jetadvantage.link.api.copier.StoredJobAttributes;
import com.hp.jetadvantage.link.api.copier.intent.BaseCopyRequestIntent;
import com.hp.jetadvantage.link.api.copier.intent.CopyToRequestIntent;
import com.hp.jetadvantage.link.api.copier.intent.ReleaseRequestIntent;
import com.hp.jetadvantage.link.api.job.CopyJobData;
import com.hp.jetadvantage.link.api.job.CopyJobState;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.Joblet;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceCopyJobService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceCopyJobService;
import com.hp.jetadvantage.link.services.copylet.adapter.CopyJobAdapter;
import com.hp.jetadvantage.link.services.copylet.adapter.StoredCopyJobAdapter;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceScanJobService;

import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.CreatingJobState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.EndState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.ReportErrorState;
import com.hp.jetadvantage.link.services.joblet.model.JobSource;
import com.hp.jetadvantage.link.services.joblet.service.JobletService;

/**
 * The CreatingCopyJobState class
 * This state is responsible for processing a Copy Job Intent and creating a
 * Copy Job.
 * It extends the CreatingJobState class and implements the initializeJob method
 * to handle the specific logic for initializing
 * and creating a copy job.
 * If any errors occur during the job initialization, the state transitions to
 * ReportErrorState.
 */
public class CreatingCopyJobState extends CreatingJobState {
    private final IDeviceCopyJobService copyJobService;
    private boolean testMode = false;

    protected CreatingCopyJobState(Intent jobIntent) {
        super(jobIntent);
        TAG = TAG + "/Copy";
        copyJobService = new StandardDeviceCopyJobService();
    }

    protected CreatingCopyJobState(boolean testMode, Intent jobIntent, IDeviceCopyJobService copyJobService) {
        super(jobIntent);
        TAG = TAG + "/Copy";
        this.testMode = testMode;
        this.copyJobService = copyJobService;
    }

    // ==================================================================
    // Override Methods from CreatingJobState
    // ==================================================================

    @Override
    protected BaseJobIntentServiceState initializeJob(Intent intent, BaseJobIntentServiceStateMachine stateMachine) {
        final Bundle extraParams = intent.getBundleExtra(CopyJobIntentService.EXTRA_PARAMS);
        final String mParamsType = intent.getStringExtra(CopyJobIntentService.PARAMS_TYPE);

        try {
            if (!setTargetPackage(stateMachine, extraParams)) {
                SLog.e(TAG, "initializeJob : Mandatory parameters (rid or target package name) not found");
                // finish service (we don't know target package - so nowhere to report
                return new EndState();
            }

            if (!isSupported(stateMachine.getContentResolver())) {
                SLog.e(TAG, "initializeJob : isSupported - false");
                return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED,
                        "The CopierService is not " +
                                "supported");
            }
            stateMachine.setDeviceDisconnectedOrJobFail(false);

            if (mParamsType == null) {
                SLog.e(TAG, "initializeJob : params type not found");
                return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED,
                        "Params type not found");
            }
            SLog.d(TAG, "initializeJob : ParamsType is  " + mParamsType);

            switch (mParamsType) {
                case CopyJobIntentService.PARAMS_TYPE_COPY:
                    return processCopyJobIntent(stateMachine, extraParams);
                case CopyJobIntentService.PARAMS_TYPE_RELEASE:
                    return processReleaseJobIntent(stateMachine, extraParams);
                default:
                    SLog.e(TAG, "Unknown params type : " + mParamsType);
                    return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM,
                            "Unknown params type");
            }
        } catch (BoundDeviceException e) {
            SLog.e(TAG, "initializeJob : BoundDeviceException " + e.getMessage(), e);
            stateMachine.setDeviceDisconnectedOrJobFail(true);
            return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.CONNECTION_ERROR,
                    "Device is not connected");
        } catch (Exception e) {
            SLog.e(TAG, "initializeJob : Failed to prepare job " + e.getMessage(), e);
            stateMachine.setDeviceDisconnectedOrJobFail(true);
            return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }

    // ==================================================================
    // Private Instance Methods
    // ==================================================================

    private BaseJobIntentServiceState createCopyJob(final BaseJobIntentServiceStateMachine stateMachine,
            final String packageName) throws Exception {
        String jobId = CopyJobAdapter.createCopyJob(copyJobService, packageName, stateMachine.getJobInfo(),
                stateMachine.getJobAttributesReader(CopyAttributesReader.class));
        if (jobId == null || jobId.isEmpty()) {
            SLog.e(TAG, "createCopyJob : Failed to create copy job");
            return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR,
                    "Failed to create copy job");
        }

        SLog.i(TAG, "createCopyJob : Job ID = " + jobId);
        stateMachine.setJobId(jobId);
        return new MonitoringCopyJobState(jobId);
    }

    /**
     * Creates a job bundle for Copy or Release operations.
     *
     * @param clientVersion        client API version
     * @param copyAttributesReader reader for copy attributes (nullable)
     */
    private Bundle createCopyJobBundle(int clientVersion, CopyAttributesReader copyAttributesReader) {
        Bundle mJobBundle = new Bundle();
        Result.pack(mJobBundle, Result.RESULT_OK);

        JobInfo mJobInfo = new JobInfo();
        mJobInfo.setJobType(JobInfo.JobType.COPY);

        CopyJobData mCopyJobData = new CopyJobData();

        if (copyAttributesReader != null) {
            if (copyAttributesReader.getJobExecutionMode() == CopyAttributes.JobExecutionMode.STORE) {
                String storeJobName = copyAttributesReader.getStoreJobName();
                mJobInfo.setJobName(storeJobName != null ? storeJobName : "[Stored Job]");
            } else {
                mJobInfo.setJobName("[Untitled]");
            }

            mCopyJobData.setDuplex(copyAttributesReader.getScanDuplex());
            mCopyJobData.setScanSize(copyAttributesReader.getScanSize());
            mCopyJobData.setJobExecutionMode(copyAttributesReader.getJobExecutionMode());
        } else {
            mJobInfo.setJobName("");
        }
        mCopyJobData.setJobState(new CopyJobState(CopyJobState.State.ACTIVE));
        mJobInfo.setJobData(mCopyJobData);

        mJobBundle.putInt(Joblet.Keys.KEY_CLIENTS_VERSION, clientVersion);
        mJobBundle.putSerializable(Joblet.Keys.KEY_JOB_TYPE, JobInfo.JobType.COPY);
        mJobBundle.putInt(Joblet.Keys.KEY_SCAN_IMAGE_COUNT, 0);
        mJobBundle.putInt(Joblet.Keys.KEY_DST_TOTAL, 0);
        mJobBundle.putBoolean(JobletService.JOB_EMULATED_TAG, true);
        mJobBundle.putString(JobletService.JOB_SOURCE_TAG, JobSource.SDK.name());
        mJobBundle.putParcelable(ILetObserver.Keys.KEY_JOB_INFO, mJobInfo);

        return mJobBundle;
    }

    private Bundle createExtraJobBundle(int clientVersion, String rid, String packageName) {
        Bundle mExtraBundle = new Bundle();
        mExtraBundle.putString(Joblet.Keys.KEY_RID, rid);
        mExtraBundle.putSerializable(Joblet.Keys.KEY_JOB_TYPE, JobInfo.JobType.COPY);
        mExtraBundle.putInt(Joblet.Keys.KEY_CLIENTS_VERSION, clientVersion);
        mExtraBundle.putString(Joblet.Keys.KEY_CLIENT_PACKAGE, packageName);

        return mExtraBundle;
    }

    private boolean isSupported(ContentResolver contentResolver) {
        if (testMode) {
            return true;
        }
        final Bundle returnBundle = contentResolver.call(Copylet.CONTENT_OXP_URI, Copylet.Method.IS_SUPPORTED, null,
                null);
        return returnBundle != null && (Result.parse(returnBundle, new Result()).getCode() == Result.RESULT_OK)
                && (returnBundle.containsKey(Copylet.IS_SUPPORTED_EXTRA)
                        && returnBundle.getBoolean(Copylet.IS_SUPPORTED_EXTRA));
    }

    private BaseJobIntentServiceState processCopyJobIntent(BaseJobIntentServiceStateMachine stateMachine,
            Bundle extraParams) throws Exception {
        CopyToRequestIntent.IntentParams reqParams = CopyToRequestIntent.getIntentParams(extraParams);
        if (reqParams == null || (reqParams.getCopyAttributes() == null)) {
            SLog.e(TAG, "processCopyJobIntent : COPY - CopyAttributes not found");
            return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, "CopyAttributes not found");
        }
        SLog.d(TAG, "processCopyJobIntent : Copy Attributes: " + reqParams.getCopyAttributes());

        CopyAttributesReader copyAttributesReader = new CopyAttributesReader(reqParams.getCopyAttributes());
        int clientVersion = reqParams.getApiLevel() != null ? reqParams.getApiLevel()
                : copyAttributesReader.getVersion();

        // Common setup for both NORMAL and STORE create paths
        stateMachine.setJobBundle(
                createCopyJobBundle(clientVersion, copyAttributesReader));
        stateMachine.setExtraJobBundle(
                createExtraJobBundle(clientVersion, reqParams.getReqId(), reqParams.getPackageName()));
        stateMachine.setJobAttributesReader(copyAttributesReader);

        // STORE mode: create a ScanJob with JobStorage destination instead of a
        // standard CopyJob
        if (copyAttributesReader.getJobExecutionMode() == CopyAttributes.JobExecutionMode.STORE) {
            return processStoredCopyJobIntent(stateMachine, reqParams, copyAttributesReader);
        }

        return createCopyJob(stateMachine, reqParams.getPackageName());
    }

    private BaseJobIntentServiceState processStoredCopyJobIntent(
            BaseJobIntentServiceStateMachine stateMachine,
            CopyToRequestIntent.IntentParams reqParams,
            CopyAttributesReader copyAttributesReader) {
        String packageName = reqParams.getPackageName();
        String rid = reqParams.getReqId();
        SLog.d(TAG, "processStoredCopyJobIntent : START, packageName=" + packageName + ", rid=" + rid);

        // Job bundles and reader already set by processCopyJobIntent

        // Create ScanJob with JobStorage destination
        IDeviceScanJobService scanJobService = new StandardDeviceScanJobService();
        SLog.d(TAG, "processStoredCopyJobIntent : Creating ScanJob with JobStorage destination");
        String scanJobId = StoredCopyJobAdapter.createScanJobForStorage(
                scanJobService, copyJobService, packageName, copyAttributesReader);

        if (scanJobId == null || scanJobId.isEmpty()) {
            SLog.d(TAG, "processStoredCopyJobIntent : Failed to create scan job for storage");
            return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR,
                    "Failed to create scan job for storage");
        }

        // Save CopyAttributes to preferences keyed by scanJobId (Parcel-based)
        StoredCopyJobPreferenceStorage.saveCopyAttributes(stateMachine.getContext(), scanJobId,
                reqParams.getCopyAttributes());
        SLog.d(TAG, "processStoredCopyJobIntent : CopyAttributes saved to PreferenceStorage, key=" + scanJobId);

        SLog.d(TAG, "processStoredCopyJobIntent : ScanJob ID = " + scanJobId);
        stateMachine.setJobId(scanJobId);
        return new MonitoringStoredCopyJobState(scanJobId);
    }

    private BaseJobIntentServiceState processReleaseJobIntent(BaseJobIntentServiceStateMachine stateMachine,
            Bundle extraParams) throws Exception {
        // 1. Extract request params
        ReleaseRequestIntent.IntentParams reqParams = ReleaseRequestIntent.getIntentParams(extraParams);
        if (reqParams == null || reqParams.getStoredJobAttributes() == null) {
            SLog.e(TAG, "processReleaseJobIntent : RELEASE - StoredJobAttributes not found");
            return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM,
                    "StoredJobAttributes not found");
        }
        SLog.d(TAG, "processReleaseJobIntent : Store Attributes: " + reqParams.getStoredJobAttributes());

        StoredJobAttributes storedJobAttributes = reqParams.getStoredJobAttributes();
        String storedJobId = storedJobAttributes.getStoredJobId();
        if (storedJobId == null || storedJobId.isEmpty()) {
            SLog.e(TAG, "processReleaseJobIntent : storedJobId is null or empty");
            return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM,
                    "Stored job ID is null or empty");
        }
        int clientVersion = reqParams.getApiLevel() != null ? reqParams.getApiLevel()
                : storedJobAttributes.getVersion();
        String packageName = reqParams.getPackageName();
        SLog.i(TAG, "processReleaseJobIntent : storedJobId=" + storedJobId + ", packageName=" + packageName
                + ", clientVersion=" + clientVersion + ", copies=" + storedJobAttributes.getCopies());

        // 2. Retrieve saved CopyAttributes from PreferenceStorage
        CopyAttributes savedCopyAttributes = StoredCopyJobPreferenceStorage.getCopyAttributes(
                stateMachine.getContext(), storedJobId);
        SLog.d(TAG,
                "processReleaseJobIntent : savedCopyAttributes=" + (savedCopyAttributes != null ? "found" : "null"));

        // 3. Create job bundles for release
        CopyAttributesReader releaseReader = savedCopyAttributes != null
                ? new CopyAttributesReader(savedCopyAttributes)
                : null;
        Bundle releaseJobBundle = createCopyJobBundle(clientVersion, releaseReader);
        // Release is a print operation, not a store. Override STORE mode from saved attributes.
        JobInfo jobInfo = releaseJobBundle.getParcelable(ILetObserver.Keys.KEY_JOB_INFO);
        if (jobInfo != null) {
            jobInfo.setJobName(releaseReader != null
                    ? releaseReader.getStoreJobName() : "[Stored Job]");
            if (jobInfo.getJobData() instanceof CopyJobData) {
                ((CopyJobData) jobInfo.getJobData()).setJobExecutionMode(null);
            }
        }
        stateMachine.setJobBundle(releaseJobBundle);
        stateMachine.setExtraJobBundle(createExtraJobBundle(clientVersion, reqParams.getReqId(), packageName));

        // 4. Release stored job via adapter (E2 type conversion handled inside adapter)
        String copyJobId = CopyJobAdapter.releaseStoredJob(
                copyJobService, packageName, storedJobId, savedCopyAttributes, storedJobAttributes);
        SLog.i(TAG, "processReleaseJobIntent : CopyJob ID = " + copyJobId);
        stateMachine.setJobId(copyJobId);

        // 5. Store delete-on-release info for CopyJobCompletedState to handle after the copy job finishes
        storeDeleteOnReleaseInfo(stateMachine, savedCopyAttributes, storedJobAttributes,
                packageName, storedJobId);

        // 6. Monitor the released copy job (E2 CopyNotification)
        return new MonitoringCopyJobState(copyJobId);
    }

    private void storeDeleteOnReleaseInfo(BaseJobIntentServiceStateMachine stateMachine,
            CopyAttributes savedCopyAttributes,
            StoredJobAttributes storedJobAttributes,
            String packageName, String storedJobId) {
        if (savedCopyAttributes == null) {
            return;
        }
        CopyAttributesReader reader = new CopyAttributesReader(savedCopyAttributes);
        CopyAttributes.RetentionMode retentionMode = reader.getStoredJobRetentionModeOnRelease();
        if (retentionMode != CopyAttributes.RetentionMode.DELETE) {
            return;
        }
        SLog.i(TAG, "storeDeleteOnReleaseInfo : RetentionModeOnRelease=DELETE, storing info for storedJobId="
                + storedJobId);
        Bundle extraBundle = stateMachine.getExtraJobBundle();
        if (extraBundle == null) {
            extraBundle = new Bundle();
            stateMachine.setExtraJobBundle(extraBundle);
        }
        extraBundle.putBoolean(CopyJobCompletedState.KEY_DELETE_ON_RELEASE, true);
        extraBundle.putString(CopyJobCompletedState.KEY_DELETE_STORED_JOB_ID, storedJobId);
        extraBundle.putString(CopyJobCompletedState.KEY_DELETE_PACKAGE_NAME, packageName);

        // Store password for delete request if the stored job was created with a password
        JobCredentialsAttributes credentials = storedJobAttributes.getJobCredentialsAttributes();
        if (credentials != null && credentials.getStoreJobPasswordType() != null) {
            String password = credentials.getStoreJobPassword();
            extraBundle.putString(CopyJobCompletedState.KEY_DELETE_PASSWORD, password != null ? password : "");
            extraBundle.putSerializable(CopyJobCompletedState.KEY_DELETE_PASSWORD_TYPE,
                    credentials.getStoreJobPasswordType());
        }
    }

    private boolean setTargetPackage(BaseJobIntentServiceStateMachine stateMachine, Bundle extraParams) {
        if (extraParams == null) {
            SLog.e(TAG, "setTargetPackage : Extra params not found from the request intent");
            return false;
        }

        BaseCopyRequestIntent.IntentParams reqParams = BaseCopyRequestIntent.getIntentParams(extraParams);
        if (reqParams == null) {
            SLog.e(TAG, "setTargetPackage : Can't retrieve IntentParams from the request intent");
            return false;
        }

        if (reqParams.getReqId() == null || reqParams.getReqId().isEmpty()) {
            SLog.e(TAG, "setTargetPackage : RID is null or empty");
            return false;
        }

        if (reqParams.getPackageName() == null || reqParams.getPackageName().isEmpty()) {
            SLog.e(TAG, "setTargetPackage : Target PackageName is null or empty");
            return false;
        }

        stateMachine.setJobRid(reqParams.getReqId());
        stateMachine.setTargetPackageName(reqParams.getPackageName());
        if (stateMachine.getReporterToApp() != null) {
            stateMachine.getReporterToApp().setTargetPackage(reqParams.getPackageName());
        }
        SLog.d(TAG, "setTargetPackage : RID : " + reqParams.getReqId() + ", TARGET : " + reqParams.getPackageName());
        return true;
    }

}
