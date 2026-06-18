/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.scanlet.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;

import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.ScanJobData;
import com.hp.jetadvantage.link.api.job.ScanJobState;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.intent.ScanToRequestIntent;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.common.ssp.AbstractReporter;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.CreatingJobState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.JobCompletedState;

import java.util.EnumSet;

public class ScanJobIntentServiceStateMachine extends BaseJobIntentServiceStateMachine {

    private int busyReportCounter = 0;
    ScanJobIntentServiceStateMachine(final IntentService service, final Looper looper) {
        super(service, looper, "Scan");
    }

    ScanJobIntentServiceStateMachine(final IntentService service, final Looper looper, final boolean stopFollowingStateTransition) {
        super(service, looper, "Scan", stopFollowingStateTransition);
    }

    ScanJobIntentServiceStateMachine(final IntentService service, final Looper looper, String jobTypeName, final AbstractReporter reporter, final BaseJobIntentServiceState initState) {
        super(service, looper, jobTypeName, reporter, initState);
    }

    @Override
    protected CreatingJobState createCreatingJobState(Intent intent) {
        return new CreatingScanJobState(intent);
    }

    @Override
    protected JobCompletedState createJobCompletedState() {
        return new ScanJobCompletedState();
    }

    @Override
    protected void updateCanceledJobState() {
        updateFinalJobState(ScanJobState.State.CANCELED);
    }

    @Override
    protected void updateFailedJobState() {
        updateFinalJobState(ScanJobState.State.FAILED);
    }

    @Override
    protected void updateCompletedJobState() {
        updateFinalJobState(ScanJobState.State.COMPLETED);
    }

    protected void updateFinalJobState(ScanJobState.State finalState) {
        Bundle jobBundle = getJobBundle();
        if (jobBundle == null) {
            return;
        }
        JobInfo jobInfo = jobBundle.getParcelable(ILetObserver.Keys.KEY_JOB_INFO);
        if (jobInfo == null || jobInfo.getJobData() == null) {
            return;
        }
        jobInfo.setCompleteTime(System.currentTimeMillis());

        ScanJobData scanJobData = jobInfo.getJobData();
        if (scanJobData.getJobState() != null) {
            scanJobData.getJobState().setState(finalState);
        } else {
            scanJobData.setJobState(new ScanJobState(finalState));
        }
    }

    @Override
    protected void reportBusyToApp(Intent intent) {
        SLog.i(TAG, "reportBusyToApp : Scan");
        final ScanToRequestIntent.IntentParams reqParams = ScanToRequestIntent.getIntentParams(intent.getBundleExtra(EXTRA_PARAMS));

        busyReportCounter++;
        if (reqParams == null || reqParams.getPackageName() == null || reqParams.getPackageName().isEmpty()) {
            SLog.e(TAG, "Report BUSY: Param is empty");
            return;
        }
        String rid = reqParams.getReqId();

        reportBusyToApp(rid, reqParams.getPackageName());
    }

    public int getBusyReportCounter() {
        return busyReportCounter;
    }
}
