package com.hp.jetadvantage.link.services.printlet.service;

import android.util.Log;

import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.MonitoringJobState;
import com.hp.jetadvantage.link.services.printlet.model.PrintJobID;

import java.util.UUID;

public class StartPrintState extends BaseJobIntentServiceState {
    protected StartPrintState() {
        super(StartPrintState.class.getSimpleName());
        TAG = TAG + "/StartPrint";
        this.possibleNextStates.add(MonitoringJobState.class.getSimpleName());
    }

    @Override
    protected void onProcess(BaseJobIntentServiceStateMachine stateMachine) {
        PrintJobIntentServiceStateMachine sm = (PrintJobIntentServiceStateMachine) stateMachine;
        JobInfo jobInfo = sm.getJobBundle().getParcelable(ILetObserver.Keys.KEY_JOB_INFO);
        jobInfo.setStartTime(System.currentTimeMillis());
        //sm.setJobId(sm.getPrintJobID().getJobUUID());
        String jobId = null;
        PrintJobID printJobID = sm.getPrintJobID();
        if (printJobID != null) {
            if (printJobID.getJobUUID() != null) { // chck job-uuid first
                UUID jobUuid = UUID.fromString(printJobID.getJobUUID());
                if (jobUuid.getMostSignificantBits() != 0 && jobUuid.getLeastSignificantBits() != 0) {
                    jobId = printJobID.getJobUUID();
                }
            } else { // otherwise take integer job-id
                jobId = Integer.toString(printJobID.getJobID());
            }
        }
        sm.setJobId(jobId);
        setNextState(new MonitoringPrintJobState(jobId));
        sm.countDownDefaultPrintAttributesReceived();
    }
}
