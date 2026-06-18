package com.hp.jetadvantage.link.services.printlet.service;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.hp.ext.types.job.JobDoneStatus;
import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.Joblet;
import com.hp.jetadvantage.link.api.job.JobletAttributes;
import com.hp.jetadvantage.link.api.job.PrintJobData;
import com.hp.jetadvantage.link.api.job.PrintJobState;
import com.hp.jetadvantage.link.api.printer.intent.PrintRequestIntent;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.MonitoringJobState;
import com.hp.jetadvantage.link.services.joblet.service.JobletService;
import com.hp.jetadvantage.link.services.printlet.ipp.IppClient;
import com.hp.jetadvantage.link.services.printlet.ipp.IppJobMonitor;
import com.hp.jetadvantage.link.services.printlet.model.Error;
import com.hp.jetadvantage.link.services.printlet.model.JobAttributes;
import com.hp.jetadvantage.link.services.printlet.model.JobState;

public class MonitoringPrintJobState  extends MonitoringJobState {

    private IppJobMonitor mJobMonitor;

    protected MonitoringPrintJobState(String jobId) {
        super(jobId);
        TAG = TAG + "/Print";
    }

    @Override
    protected void registerNotificationCallback(BaseJobIntentServiceStateMachine stateMachine) {
        Log.e(TAG, "registerNotificationCallback: MonitoringPrintJobState");
        PrintJobIntentServiceStateMachine sm = (PrintJobIntentServiceStateMachine) stateMachine;
        mJobMonitor = new IppJobMonitor(sm, new IppJobMonitor.IppJobMonitorCallback() {
            @Override
            public void onJobStatusChanged(int jobId, JobAttributes jobAttributes) {
                updateJobStatus(sm, jobAttributes);
                //sm.sendLocalPrintResult(OXPCreatePrintSpoolerIntentService.ACTION_PRINT_FINISH, false);
            }

            @Override
            public boolean isJobFinished(JobAttributes jobAttributes) {
                switch (jobAttributes.jobState) {
                    case Aborted:
                    case Canceled:
                    case Completed:
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    protected void unregisterNotificationCallback() {
        Log.e(TAG, "unregisterNotificationCallback: MonitoringPrintJobState");
        mJobMonitor.stop();
    }

    /**
     * Update job status.
     * This method is from the mPrintJobAttributesCallback in OXPCreatePrintJobIntentService.
     * @param stateMachine
     * @param jobAttributes
     */
    private void updateJobStatus(PrintJobIntentServiceStateMachine stateMachine, JobAttributes jobAttributes) {
        Bundle jobBundle = stateMachine.getJobBundle();
        if (jobBundle == null || jobAttributes == null) {
            SLog.e(TAG, "jobBundle or jobAttributes is null");
            return;
        }
        SLog.i(TAG, "Received print job state: " + jobAttributes.jobState
                + " (" + jobAttributes.jobImpressionsCompleted + ", " + jobAttributes.jobMediaSheetsCompleted
                + ")");

        JobInfo jobInfo = stateMachine.getJobBundle().getParcelable(ILetObserver.Keys.KEY_JOB_INFO);

        if (jobAttributes.jobName != null && jobAttributes.jobName.startsWith("ipp://")) {
            Uri uri = Uri.parse(jobAttributes.jobName);
            String name = uri.getLastPathSegment();
            if (name.startsWith("port") && name.indexOf("-") > 0) {
                String index = name.substring(name.indexOf("-") + 1);
                jobInfo.setJobName("IPP-JOB-" + index);
            }
        } else {
            jobInfo.setJobName(jobAttributes.jobName);
        }

        String jobUser = jobAttributes.jobUser;
        if ("anonymous".equals(jobUser)) jobUser = "anonymous"; //1.3
        jobInfo.setOwner(jobUser);

        if (jobAttributes.jobCreatedTime > 0) {
            jobInfo.setStartTime(jobAttributes.jobCreatedTime);
        }

        if (jobAttributes.jobCompletedTime > 0) {
            jobInfo.setCompleteTime(jobAttributes.jobCompletedTime);
        }
        PrintJobData printJobData = jobInfo.getJobData();
        printJobData.setSheetsPrinted(jobAttributes.jobMediaSheetsCompleted);
        printJobData.setImpressionsPrinted(jobAttributes.jobImpressionsCompleted);

        jobBundle.putInt(Joblet.Keys.KEY_PRINT_IMAGE_COUNT, jobAttributes.jobImpressionsCompleted);
        jobBundle.putInt(Joblet.Keys.KEY_SHEET_COUNT, jobAttributes.jobMediaSheetsCompleted);

        if (jobAttributes.jobState == JobState.Completed
                || jobAttributes.jobState == JobState.Aborted
                || jobAttributes.jobState == JobState.Canceled) {
            stateMachine.awaitDefaultPrintAttributesReceived();
        }

        if (jobAttributes.jobState != JobState.Processing) {
            String uiContext = stateMachine.getPrintJobService().getUiContextToken(PrintRequestIntent.getIntentParams(stateMachine.getExtraParams()).getPackageName());
            if (uiContext != null) {
                SLog.d(TAG, "Job status for requesting releaseUIContext: " + jobAttributes.jobState);
                try {
                    IppClient.getInstance().exitPriorityPrint(stateMachine.getContext(), uiContext, 0, stateMachine.isBackgroundJob(), stateMachine.isLastJob());
                } catch (Error e) {
                    SLog.e(TAG, "something wrong happened while calling exitPriorityPrint: " + e.getMessage());
                }
            } else {
                SLog.e(TAG, "Job status for requesting releaseUIContext (no uiContext)");
            }
        }

        switch (jobAttributes.jobState) {
            case Processing:
                printJobData.setJobState(new PrintJobState(PrintJobState.State.PROCESSING));
                processJobDoneStatus(stateMachine, JobDoneStatus.JdsActive);
                break;
            case Aborted:
                printJobData.setJobState(new PrintJobState(PrintJobState.State.ABORTED));
                processJobDoneStatus(stateMachine, JobDoneStatus.JdsFailed);
                break;
            case Completed:
                printJobData.setJobState(new PrintJobState(PrintJobState.State.COMPLETED));
                if (jobInfo.getCompleteTime() == 0) {
                    jobInfo.setCompleteTime(System.currentTimeMillis());
                }

                if (jobInfo.getJobId() != null) {
                    stateMachine.sendLocalJobDataToJobLet(JobletService.Event.TL_EV_JOB_PROGRESS);

                    processJobDoneStatus(stateMachine, JobDoneStatus.JdsSucceeded);
                } else {
                    printJobData.setJobState(new PrintJobState(PrintJobState.State.ABORTED));

                    // IPP returns job completed status but actually it was not created, so mark job as failed
                    processJobDoneStatus(stateMachine, JobDoneStatus.JdsFailed);
                }
                break;
            case Canceled:
                //report(OXPCreatePrintJobIntentService.OXPPrintHandler.MSG_REPORT_INTERNAL_CANCELLED, Result.RESULT_FAIL, null, null);
                processJobDoneStatus(stateMachine, JobDoneStatus.JdsCanceled);
                // FIXED JALPINF-2149
                // For entering to PriorityPrintExit
                stateMachine.setIsFirstJob(false);
                stateMachine.setIsLastJob(true);
                break;
        }
    }
}
