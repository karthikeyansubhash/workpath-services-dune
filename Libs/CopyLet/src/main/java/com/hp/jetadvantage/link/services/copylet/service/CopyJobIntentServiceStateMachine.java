/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.copylet.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;

import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.copier.intent.BaseCopyRequestIntent;
import com.hp.jetadvantage.link.api.job.CopyJobData;
import com.hp.jetadvantage.link.api.job.CopyJobState;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.common.ssp.AbstractReporter;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.CreatingJobState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.JobCompletedState;

/**
 * The CopyJobIntentServiceStateMachine class is a state machine for the CopyJobIntentService.
 * Reference: See the diagram in 'Libs\JobLet\doc\JobIntentService_StateMachine_diagram.png' for more details.
 */
public class CopyJobIntentServiceStateMachine extends BaseJobIntentServiceStateMachine {
    private int busyReportCounter = 0;

    public CopyJobIntentServiceStateMachine(IntentService service, Looper looper) {
        super(service, looper, "Copy");
    }

    public CopyJobIntentServiceStateMachine(final IntentService service, final Looper looper,
                                            final boolean stopFollowingStateTransition) {
        super(service, looper, "Copy", stopFollowingStateTransition);
    }

    public CopyJobIntentServiceStateMachine(final IntentService service, final Looper looper, String jobTypeName,
                                            final AbstractReporter reporter, final BaseJobIntentServiceState initState) {
        super(service, looper, jobTypeName, reporter, initState);
    }

    // ==================================================================
    //      Override Methods from BaseJobIntentServiceStateMachine
    // ==================================================================

    @Override
    protected CreatingJobState createCreatingJobState(Intent intent) {
        return new CreatingCopyJobState(intent);
    }

    @Override
    protected JobCompletedState createJobCompletedState() {
        return new CopyJobCompletedState();
    }

    @Override
    protected void reportBusyToApp(Intent intent) {
        busyReportCounter++;
        final BaseCopyRequestIntent.IntentParams reqParams =
                BaseCopyRequestIntent.getIntentParams(intent.getBundleExtra(EXTRA_PARAMS));

        if (reqParams == null || reqParams.getPackageName() == null || reqParams.getPackageName().isEmpty()) {
            SLog.e(TAG, "Report BUSY: Param is empty");
            return;
        }
        String rid = reqParams.getReqId();

        reportBusyToApp(rid, reqParams.getPackageName());
    }

    @Override
    protected void updateCanceledJobState() {
        updateFinalJobState(CopyJobState.State.CANCELED);
    }

    @Override
    protected void updateCompletedJobState() {
        updateFinalJobState(CopyJobState.State.COMPLETED);
    }

    @Override
    protected void updateFailedJobState() {
        updateFinalJobState(CopyJobState.State.FAILED);
    }

    // ==================================================================
    //      Instance Methods
    // ==================================================================

    protected void updateFinalJobState(CopyJobState.State finalState) {
        Bundle jobBundle = getJobBundle();
        if (jobBundle == null) {
            return;
        }
        JobInfo jobInfo = jobBundle.getParcelable(ILetObserver.Keys.KEY_JOB_INFO);
        if (jobInfo == null || jobInfo.getJobData() == null) {
            return;
        }
        jobInfo.setCompleteTime(System.currentTimeMillis());

        CopyJobData copyJobData = jobInfo.getJobData();
        if (copyJobData.getJobState() != null) {
            copyJobData.getJobState().setState(finalState);
        } else {
            copyJobData.setJobState(new CopyJobState(finalState));
        }
    }

    protected int getBusyReportCounter() {
        return busyReportCounter;
    }
}
