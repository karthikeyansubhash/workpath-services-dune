package com.hp.jetadvantage.link.services.printlet.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.PrintJobData;
import com.hp.jetadvantage.link.api.job.PrintJobState;
import com.hp.jetadvantage.link.api.printer.intent.PrintRequestIntent;

import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDevicePrintJobService;
import com.hp.jetadvantage.link.device.services.standard.StandardDevicePrintJobService;
import com.hp.jetadvantage.link.services.common.ssp.AbstractReporter;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.CreatingJobState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.JobCompletedState;
import com.hp.jetadvantage.link.services.printlet.model.PrintJobID;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class PrintJobIntentServiceStateMachine extends BaseJobIntentServiceStateMachine {

    private int busyReportCounter = 0;

    private Bundle mExtraParams;

    private PrintJobID mPrintJobID;

    private IDevicePrintJobService mPrintJobService;

    private static final AtomicBoolean mIsBackgroundJob = new AtomicBoolean(false);

    private static final AtomicBoolean mIsFirstJob = new AtomicBoolean(false);

    private static final AtomicBoolean mIsLastJob = new AtomicBoolean(false);

    private CountDownLatch mDefaultPrintAttributesReceived;

    public PrintJobIntentServiceStateMachine(final IntentService service, final Looper looper) {
        super(service, looper, "Print");
        mPrintJobService = new StandardDevicePrintJobService();
    }

    public PrintJobIntentServiceStateMachine(final IntentService service, final Looper looper, final boolean stopFollowingStateTransition) {
        super(service, looper, "Print", stopFollowingStateTransition);
        mPrintJobService = new StandardDevicePrintJobService();
    }

    public PrintJobIntentServiceStateMachine(final IntentService service, final Looper looper, String jobTypeName, final AbstractReporter reporter, final BaseJobIntentServiceState initState) {
        super(service, looper, jobTypeName, reporter, initState);
        mPrintJobService = new StandardDevicePrintJobService();
    }


    // ======================= Begin: Override methods from BaseJobIntentServiceStateMachine =======================
    @Override
    protected CreatingJobState createCreatingJobState(Intent intent) {
        return new CreatingPrintJobState(intent);
    }

    @Override
    protected JobCompletedState createJobCompletedState() {
        return new PrintJobCompletedState();
    }

    @Override
    protected void updateCanceledJobState() {
        updateFinalJobState(PrintJobState.State.CANCELED);
    }

    @Override
    protected void updateFailedJobState() {
        updateFinalJobState(PrintJobState.State.ABORTED);
    }

    @Override
    protected void updateCompletedJobState() {
        updateFinalJobState(PrintJobState.State.COMPLETED);
    }

    @Override
    protected void reportBusyToApp(Intent intent) {
        final PrintRequestIntent.IntentParams reqParams = PrintRequestIntent.getIntentParams(intent.getBundleExtra(EXTRA_PARAMS));

        busyReportCounter++;
        if (reqParams == null || reqParams.getPackageName() == null || reqParams.getPackageName().isEmpty()) {
            SLog.e(TAG, "Report BUSY: Param is empty");
            return;
        }
        String rid = reqParams.getReqId();

        reportBusyToApp(rid, reqParams.getPackageName());
    }

    @Override
    public void handleMessage(@NonNull final Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case MSG_REPORT_FAIL:
                sendLocalPrintResult(OXPCreatePrintSpoolerIntentService.ACTION_CANCEL_CURRENT_BATCH, true);
                break;
            // We don't need to handle these cases.
            // Here I just want to send a broadcast to cancel the current batch when the job is failed.
//            case MSG_REPORT_COMPLETED:
//                break;
//            case MSG_REPORT_CANCELLED:
//                break;
            default:
                break;
        }
    }

    @Override
    protected void stop() {
        mIsBackgroundJob.set(false);
        mIsFirstJob.set(false);
        mIsLastJob.set(false);
        mPrintJobService = null;
        mPrintJobID = null;
        mDefaultPrintAttributesReceived = null;
        sendLocalPrintResult(OXPCreatePrintSpoolerIntentService.ACTION_PRINT_FINISH, false);
        super.stop();
    }
    // ======================= End: Override methods from BaseJobIntentServiceStateMachine =======================


    protected void updateFinalJobState(PrintJobState.State finalState) {
        Bundle jobBundle = getJobBundle();
        if (jobBundle == null) {
            return;
        }
        JobInfo jobInfo = jobBundle.getParcelable(ILetObserver.Keys.KEY_JOB_INFO);
        if (jobInfo == null || jobInfo.getJobData() == null) {
            return;
        }
        jobInfo.setCompleteTime(System.currentTimeMillis());

        PrintJobData printJobData = jobInfo.getJobData();
        if (printJobData.getJobState() != null) {
            printJobData.getJobState().setState(finalState);
        } else {
            printJobData.setJobState(new PrintJobState(finalState));
        }
    }

    public int getBusyReportCounter() {
        return busyReportCounter;
    }


    // ========== Begin: Getters/Setters for member variables ==========
    protected void setExtraParams(Bundle extraParams) {
        mExtraParams = extraParams;
    }

    protected Bundle getExtraParams() {
        return mExtraParams;
    }

    protected void setIsBackgroundJob(boolean isBackgroundJob) {
        mIsBackgroundJob.set(isBackgroundJob);
    }

    protected boolean isBackgroundJob() {
        return mIsBackgroundJob.get();
    }

    protected void setIsFirstJob(boolean isFirstJob) {
        mIsFirstJob.set(isFirstJob);
    }

    protected boolean isFirstJob() {
        return mIsFirstJob.get();
    }

    protected void setIsLastJob(boolean isLastJob) {
        mIsLastJob.set(isLastJob);
    }

    protected boolean isLastJob() {
        return mIsLastJob.get();
    }

    protected void setPrintJobID(PrintJobID printJobID) {
        mPrintJobID = printJobID;
    }

    public final PrintJobID getPrintJobID() {
        return mPrintJobID;
    }

    protected IDevicePrintJobService getPrintJobService() {
        return mPrintJobService;
    }
    // ========== End : Getters/Setters for member variables ==========


    // ========== Begin : Methods control a CountDownLatch ==========
    protected void initDefaultPrintAttributesReceived() {
        mDefaultPrintAttributesReceived = new CountDownLatch(1);
    }

    protected void countDownDefaultPrintAttributesReceived() {
        if (mDefaultPrintAttributesReceived != null) {
            mDefaultPrintAttributesReceived.countDown();
        } else {
            SLog.e(TAG, "countDownDefaultPrintAttributesReceived: mDefaultPrintAttributesReceived is null");
        }
    }

    protected void awaitDefaultPrintAttributesReceived() {
        try {
            if (mDefaultPrintAttributesReceived != null) {
                mDefaultPrintAttributesReceived.await();
            } else {
                SLog.e(TAG, "awaitDefaultPrintAttributesReceived: mDefaultPrintAttributesReceived is null");
            }
        } catch (InterruptedException e) {
            SLog.e(TAG, "awaitDefaultPrintAttributesReceived: " + e.getMessage());
        }
    }
    // ========== End : Methods control a CountDownLatch ==========


    /**
     * Send a broadcast to notify the print result to the OXPCreatePrintSpoolerIntentService.
     * @param event : an action to be sent
     * @param isError : true for error, false for non-error
     */
    protected void sendLocalPrintResult(final String event, final boolean isError) {
        Intent spoolerNotification = new Intent(event);
        spoolerNotification.putExtra(OXPCreatePrintSpoolerIntentService.EXTRA_IS_ERROR, isError);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(spoolerNotification);
    }
}
