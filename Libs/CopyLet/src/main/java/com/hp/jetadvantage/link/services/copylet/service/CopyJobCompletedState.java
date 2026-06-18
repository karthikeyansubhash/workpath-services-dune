/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.copylet.service;

import android.os.Bundle;

import com.hp.jetadvantage.link.api.copier.JobCredentialsAttributes;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceCopyJobService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceCopyJobService;
import com.hp.jetadvantage.link.services.copylet.adapter.CopyJobAdapter;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.JobCompletedState;

/**
 * The CopyJobCompletedState class.
 * This state is responsible for processing the completed job.
 */
public class CopyJobCompletedState extends JobCompletedState {

    static final String KEY_DELETE_ON_RELEASE = "deleteOnRelease";
    static final String KEY_DELETE_STORED_JOB_ID = "deleteStoredJobId";
    static final String KEY_DELETE_PACKAGE_NAME = "deletePackageName";
    static final String KEY_DELETE_PASSWORD = "deletePassword";
    static final String KEY_DELETE_PASSWORD_TYPE = "deletePasswordType";

    protected CopyJobCompletedState() {
        super();
        TAG = TAG + "/Copy";
    }

    @Override
    protected void processCompletedJob(BaseJobIntentServiceStateMachine stateMachine) {
        // Note: JobExecutionMode is already set correctly by CreatingCopyJobState:
        //   STORE  → for stored copy jobs (scan to storage)
        //   null   → for release jobs (print from storage)
        //   NORMAL → for normal copy jobs

        // Delete stored job after copy completes (Release only)
        Bundle extraBundle = stateMachine.getExtraJobBundle();
        if (extraBundle != null && extraBundle.getBoolean(KEY_DELETE_ON_RELEASE, false)) {
            deleteStoredJobOnRelease(stateMachine, extraBundle);
        }
    }

    private void deleteStoredJobOnRelease(BaseJobIntentServiceStateMachine stateMachine, Bundle extraBundle) {
        String storedJobId = extraBundle.getString(KEY_DELETE_STORED_JOB_ID);
        String packageName = extraBundle.getString(KEY_DELETE_PACKAGE_NAME);
        String password = extraBundle.getString(KEY_DELETE_PASSWORD);
        JobCredentialsAttributes.PasswordType passwordType =
                (JobCredentialsAttributes.PasswordType) extraBundle.getSerializable(KEY_DELETE_PASSWORD_TYPE);

        if (storedJobId == null || storedJobId.isEmpty()) {
            SLog.e(TAG, "deleteStoredJobOnRelease : storedJobId is null or empty");
            return;
        }

        SLog.i(TAG, "deleteStoredJobOnRelease : Copy completed, deleting storedJobId=" + storedJobId);
        try {
            IDeviceCopyJobService copyJobService = new StandardDeviceCopyJobService();
            JobCredentialsAttributes credentials = buildCredentials(password, passwordType);
            CopyJobAdapter.deleteStoredJob(
                    stateMachine.getContext(), copyJobService, packageName, storedJobId, credentials);
            SLog.d(TAG, "deleteStoredJobOnRelease : success");
        } catch (Exception e) {
            SLog.e(TAG, "deleteStoredJobOnRelease : Failed to delete stored job: " + e.getMessage(), e);
        }
    }

    static JobCredentialsAttributes buildCredentials(String password,
            JobCredentialsAttributes.PasswordType passwordType) {
        if (password == null || password.isEmpty() || passwordType == null) {
            return null;
        }
        try {
            return new JobCredentialsAttributes.Builder()
                    .setPasswordType(passwordType)
                    .setPassword(password)
                    .build();
        } catch (Exception e) {
            SLog.e("CopyJobCompletedState", "buildCredentials : Failed to build credentials", e);
            return null;
        }
    }
}
