// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.joblet.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.util.Preconditions;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.hp.jetadvantage.link.api.ErrorCode;
import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.CopyJobData;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.JobService;
import com.hp.jetadvantage.link.api.job.Joblet;
import com.hp.jetadvantage.link.api.job.JobletAttributes;
import com.hp.jetadvantage.link.api.job.PrintJobData;
import com.hp.jetadvantage.link.api.job.ScanJobData;
import com.hp.jetadvantage.link.common.Platform;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.dcmgt.datacollectorLib.DataCollectorContracts;
import com.hp.jetadvantage.link.services.common.notification.ServiceNotification;
import com.hp.jetadvantage.link.services.common.ssp.AbstractReporter;
import com.hp.jetadvantage.link.services.joblet.R;
import com.hp.jetadvantage.link.services.joblet.model.JobData;
import com.hp.jetadvantage.link.services.joblet.model.JobDataContentProvider;
import com.hp.jetadvantage.link.services.joblet.model.JobSource;
import com.hp.jetadvantage.link.services.joblet.provider.JobletContentProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Central class for handling job progressing, job events.
 */
public final class JobletService extends Service {
    private static final String TAG = "Joblet" + "/" + JobletService.class.getSimpleName();

    public static final String JOB_TYPE_TAG = "jobTypeTag";
    public static final String JOB_NAME_TAG = "jobNameTag";
    /**
     * Clients which sends this tag emulate UP job execution
     */
    public static final String JOB_EMULATED_TAG = "jobEmulatedTag";
    /**
     * Key for job source passing, {@link JobSource#name()}, added from API 1
     */
    public static final String JOB_SOURCE_TAG = "jobSourceTag";

    private static final String JOB_COMPLETE_EV_TAG = "jobCompleteEvTag";
    /**
     * Key for job attribute change event, to fill job data with more complete data, especially counters, added from API 1
     */
    private static final String JOB_ATTRIBUTE_CHANGE_EV_TAG = "jobAttributeChangedEvTag";
    /**
     * Key for job state information, can be omitted, JobStatus in json form
     */
    private static final String JOB_STATE_TAG = "jobUpStateTag";
    /**
     * Number of expected enulated copies
     */
    public static final String KEY_EMULATED_NUM_COPIES_TAG = "numCopiesTag";
    /**
     * Key for string of json processing causes list
     */
    private static final String KEY_PROCESSING_CAUSES = "jobProcessingCauses";
    /**
     * Extra for jobCompleteEv check service response, added from API 1
     */

    private static final String JOB_STATUS_ACTIVITY_ACTION = "JobStatusForSearchForCurrent";
    private static final String YES_ACTION = "jobletNotificationReceiverYesAction";
    private static final String NO_ACTION = "jobletNotificationReceiverNoAction";

    private static final String KEY_SHOW_UI = "showUi";
    // Holds true if monitoring should be done via activity
    private static final String KEY_EMBEDDED_MONITOR = "embeddedMonitor";

    /**
     * Event for job action emulation from Mobile Context
     */
    public static final String ACTION_BASIC_EVENT = "com.hp.jetadvantage.link.services.joblet.BASIC_EVENT";
    /**
     * Extra to contain {@link Event}
     */
    public static final String EXTRA_BASIC_EVENT = "event";
    /**
     * Variable to hold job completion setting which is currently active.
     * Joblet Service uses it in order to determine behaviour at the end of job,
     * Added from API 1.
     * e.g. which event indicates job end.
     * Note: it's not expected to have intensive multi-thread processing, so don't use Atomic value here
     */
    private JobCompleteType mCompleteSetting = JobCompleteType.UNDEFINED;

    /**
     * Enum to determine how job complete should be captured.
     * This values is stored in shared preferences and should
     * be renewed every time printer connected. Added from API 1
     */
    private enum JobCompleteType {
        /**
         * Undefined, should be determined by UP call
         */
        UNDEFINED,
        /**
         * On current printer JobCompleteEv is supported, so no GET job should be called for it at the end
         */
        COMPLETE_EV,
        /**
         * GET job should be executed at the end of the job (in response to job state changed to final state).
         * This value is also used as placeholder during progress of complete type check.
         */
        GET_JOB_CALL
    }

    private enum JobEventType {
        JET_EV_JOB_PROGRESS,
        JET_EV_NPC,
        JET_EV_TERMINAL
    }

    public enum Event {
        TL_EV_UNKNOWN(100),
        TL_EV_PREINIT(2),
        TL_EV_INIT(5),
        TL_EV_JOB_INITIATED(20),
        TL_EV_JOB_PROGRESS(21),
        TL_EV_JOB_WAITING(22),
        TL_EV_JOB_COMPLETED(23),
        TL_EV_JOB_CANCELED(24),
        TL_EV_GETJOBDATA_FAILED(30),
        TL_EV_JOB_FAILED(31),
        TL_EV_JOB_PROCEED(32),
        TL_EV_CONFIRMATION(33),
        /**
         * Processing job info received, it will be stored and provided in fail()
         */
        TL_EV_JOB_PROCESSING_INFO(34),
        /**
         * Complete vent has been received, but need to refresh job data from job request
         */
        TL_EV_JOB_OBTAIN_COMPLETED(35);

        private int val;

        Event(final int v) {
            val = v;
        }

        int val() {
            return val;
        }

        static public Event map(final int e) {
            for (Event ev : Event.values()) {
                if (ev.val() == e) {
                    return ev;
                }
            }
            return TL_EV_UNKNOWN;
        }
    }

    /**
     * Events received from Basic jobs
     */
    private final BroadcastReceiver mBasicEventsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            // Just translate event to
            final Event event = Event.valueOf(intent.getStringExtra(EXTRA_BASIC_EVENT));

            sendEvent(context, event, intent.getExtras());
        }
    };

    /**
     * Special AbstractJobletReceiver to receive only confirmation callback
     */
    private final BroadcastReceiver mJobletLowPriorityReceiver = new JobService.AbstractJobletObserver(new Handler()) {
        @Override
        public void onComplete(final String rid, final JobInfo jobInfo) {
            // nothing to do
        }

        @Override
        public void onProgress(final String rid, final JobInfo jobInfo) {
            // nothing to do
        }

        @Override
        public void onFail(final String rid, final Result result) {
            // nothing to do
        }

        @Override
        public void onCancel(String rid) {
            // nothing to do
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        handleRestart();

        registerBasicEventsReceiver();
        registerNotificationReceiver();
        registerJobletReceivers();

        ServiceNotification.showNotification(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBasicEventsReceiver);
        unregisterReceiver(mNotificationReceiver);
        unregisterReceiver(mJobletLowPriorityReceiver);
    }

    private void registerNotificationReceiver() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(YES_ACTION);
        filter.addAction(NO_ACTION);
        registerReceiver(mNotificationReceiver, filter);
    }

    private void registerBasicEventsReceiver() {
        final IntentFilter filter = new IntentFilter(ACTION_BASIC_EVENT);

        LocalBroadcastManager.getInstance(this).registerReceiver(mBasicEventsReceiver, filter);
    }

    /**
     * Registers JobletAbstract observer in it's own way
     */
    private void registerJobletReceivers() {
        final IntentFilter filter = new IntentFilter(Joblet.ACTION);

        filter.setPriority(IntentFilter.SYSTEM_LOW_PRIORITY + 10);
        registerReceiver(mJobletLowPriorityReceiver, filter);
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        SLog.d(TAG, "onStartCommand");

        if (null == intent) {
            SLog.w(TAG, "intent is null.  Service is restarted");
            return START_STICKY;
        }

        if (TextUtils.isEmpty(intent.getAction())) {
            SLog.w(TAG, "intent is null. Service is restarted");
            return START_STICKY;
        }

        final Event ev = Event.valueOf(intent.getAction());
        final Bundle bundle = intent.getExtras();

        handleJobEvents(ev, bundle);

        return START_STICKY;
    }

    /**
     * Handles 'job' type events
     *
     * @param ev     {@link Event} to handle, from all
     * @param bundle with the data for event
     */
    private synchronized void handleJobEvents(final Event ev, final Bundle bundle) {
        // True if this is synthetic event generated not from real device, but programmatically
        boolean emulatedEvent = false;
        String jobId = null;

        if (bundle.containsKey(Joblet.Keys.KEY_JOBID)) {
            jobId = bundle.getString(Joblet.Keys.KEY_JOBID);
        }

        final Reporter reporter = new Reporter();

        final PrinterInfo pi = SelectedPrinterHelper.get(this.getContentResolver());
        String mfpId = Joblet.UNKNOWN_MFP_ID;    // Returns the same DEFAULT one as generated in JobService.monitorJob if PI is empty

        if (!PrinterInfo.isEmpty(pi)) {
            mfpId = pi.getMfpId();
        }

        if (jobId == null) {
            jobId = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
            bundle.putString(Joblet.Keys.KEY_JOBID, jobId);
        }
        final String key = getRID(mfpId, jobId);    // rid returned in JobService.monitorJob is the key for this JobletService

        SLog.d(TAG, "Event: " + ev + " for jobid: " + jobId);

        // Get the JobData to process and update the received event
        final JobData jobData = getJobData(key, jobId, bundle);

        if (!TextUtils.isEmpty(jobData.mClientPackage)) {
            SLog.d(TAG, "Set target package " + jobData.mClientPackage);
            reporter.setTargetPackage(jobData.mClientPackage);
        }

        // If PrinterInfo is empty we return now
        if (PrinterInfo.isEmpty(pi)) {
            sendMessage(jobId, jobData, Result.RESULT_FAIL, JobEventType.JET_EV_TERMINAL, false);

            if (JobDataContentProvider.remove(getContentResolver(), key) == 0) {
                stopForeground(true);
            }

            reporter.jobFailed(getApplicationContext(), jobData.mRid, jobData, Result.RESULT_FAIL,
                    Result.ErrorCode.CONNECTION_ERROR, "Device is not connected");
            sendPendingIntent(jobId, jobData, Result.RESULT_FAIL);

            SLog.e(TAG, "Don't handle event, printer is not there, cancel all jobs");
            return;
        } // At this point, Joblet doesn't actually care about the Printer State.  Because, some event was received and
        // JobletService handles it.  But if the Printer was disconnected, then it will not receive any further events.

        if (bundle.containsKey(JOB_EMULATED_TAG)) {
            emulatedEvent = true;
            SLog.d(TAG, "Event is emulater: " + emulatedEvent);
        }

        int resultCode;
        String cause;

        Log.w(TAG, "jobtest Received ev: " + ev + " for jobId: " + jobId + " in state: " + jobData.mState);

        switch (jobData.mState) {
            case TL_ST_IDLE:
                switch (ev) {
                    case TL_EV_JOB_INITIATED:
                        jobData.updateState(com.hp.jetadvantage.link.services.joblet.model.JobState.TL_ST_JOB_PENDING);
                        // Just let the ProgressActivity know of the state
                        sendMessage(jobId, jobData, Result.RESULT_OK, JobEventType.JET_EV_JOB_PROGRESS, false);
                        // Show Notifications for ongoing jobs
                        showJobNotification();
                        SLog.d(TAG, "Job Events Initiated for " + jobId + " send progress ");
                        reporter.jobProgress(getApplicationContext(), jobData.mRid, jobId, jobData);
                        break;
                    case TL_EV_GETJOBDATA_FAILED:
                        SLog.d(TAG, "Get Job Data Failed for " + jobId);

                        jobData.updateState(com.hp.jetadvantage.link.services.joblet.model.JobState.TL_ST_FAILED);    // FAILED state for clients
                        jobData.mIsSuccess = false;

                        // Get Job Data failed. Sending failure
                        sendMessage(jobId, jobData, Result.RESULT_FAIL, JobEventType.JET_EV_TERMINAL, false);   // ProgressActivity doesn't care about cause right now

                        // Failure to get the job data is a terminal condition, so removing jobData for this job.
                        // If Joblet is not tracking any more jobs, then put the service to background
                        if (JobDataContentProvider.remove(getContentResolver(), key) == 0) {
                            stopForeground(true);
                        }


                        reporter.jobFailed(getApplicationContext(), jobData.mRid,
                                jobData,
                                bundle.getInt(Result.KEY_CODE),
                                (Result.ErrorCode) bundle.getSerializable(Result.KEY_ERROR_CODE),
                                bundle.getString(Result.KEY_CAUSE));
                        sendPendingIntent(jobId, jobData, Result.RESULT_FAIL);    // Intent doesn't care about cause

                        break;
                    case TL_EV_JOB_FAILED:
                        resultCode = bundle.containsKey(Result.KEY_CODE) ? bundle.getInt(Result.KEY_CODE) : Result.RESULT_FAIL;
                        Result.ErrorCode errorCode = (Result.ErrorCode) bundle.getSerializable(Result.KEY_ERROR_CODE); // by default is UNKNOWN, if not set in bundle
                        cause = bundle.getString(Result.KEY_CAUSE); // by default is "", if not set in bundle

                        jobData.updateState(com.hp.jetadvantage.link.services.joblet.model.JobState.TL_ST_FAILED);        // FAILED state for clients
                        jobData.mIsSuccess = false;

                        // Local Broadcast uses the same Result class for broadcasting internal success
                        // and failure, hence its OK to pass RESULT_OK for this.
                        sendMessage(jobId, jobData, Result.RESULT_OK, JobEventType.JET_EV_TERMINAL, true);

                        showJobNotification();

                        // JOB FAILED is a terminal condition, so removing the jobData for this job.  And then if we're not tracking any more jobs
                        // let's put this service back into background
                        if (JobDataContentProvider.remove(getContentResolver(), key) == 0) {
                            stopForeground(true);
                        }

                        reporter.jobFailed(getApplicationContext(), jobData.mRid, jobData, resultCode, errorCode, cause);
                        // Local Broadcast uses the same Result class for broadcasting internal success
                        // and failure, hence its OK to pass RESULT_OK for this.
                        sendPendingIntent(jobId, jobData, Result.RESULT_OK);
                        break;
                    case TL_EV_PREINIT:
                        if (emulatedEvent) {
                            // Looks like the job is in some invalid state, report it as failed
                            jobData.updateState(com.hp.jetadvantage.link.services.joblet.model.JobState.TL_ST_FAILED);    // FAILED state for clients
                            jobData.mIsSuccess = false;

                            localBroadcast(jobId, jobData, Result.RESULT_OK, true);    // Local Broadcast uses the same Result class for broadcasting internal success
                            // and failure, hence its OK to pass RESULT_OK for this.
                            showJobNotification();

                            // JOB FAILED is a terminal condition, so removing the jobData for this job.  And then if we're not tracking any more jobs
                            // let's put this service back into background
                            if (JobDataContentProvider.remove(getContentResolver(), key) == 0) {
                                stopForeground(true);
                            }

                            reporter.jobFailed(getApplicationContext(), jobData.mRid, jobData, Result.RESULT_FAIL, Result.ErrorCode.JOB_FAILURE, null);
                            sendPendingIntent(jobId, jobData, Result.RESULT_OK);    // Local Broadcast uses the same Result class for broadcasting internal success
                            // and failure, hence its OK to pass RESULT_OK for this.
                        } else {
                            SLog.w(TAG, "Unhandled ev: " + ev + " in state: " + jobData.mState);
                        }
                        break;
                    case TL_EV_INIT:
                        if (emulatedEvent) {
                            jobData.updateState(com.hp.jetadvantage.link.services.joblet.model.JobState.TL_ST_IDLE);
                        } else {
                            SLog.w(TAG, "Unhandled ev: " + ev + " in state: " + jobData.mState);
                        }
                        break;
                    case TL_EV_JOB_PROGRESS:
                        if (emulatedEvent
                                || jobData.mDeviceType == JobInfo.JobType.PRINT
                                || jobData.mDeviceType == JobInfo.JobType.SCAN
                                || jobData.mDeviceType == JobInfo.JobType.COPY) {
                            jobData.updateState(com.hp.jetadvantage.link.services.joblet.model.JobState.TL_ST_PROGRESSING);
                            localBroadcast(jobId, jobData, Result.RESULT_OK, true);
                            showJobNotification();
                            reporter.jobProgress(getApplicationContext(), jobData.mRid, jobId, jobData);
                        } else {
                            SLog.w(TAG, "Unhandled ev: " + ev + " in state: " + jobData.mState);
                        }
                        break;
                    case TL_EV_JOB_PROCESSING_INFO:
                        // send extra progress if some cause has been added
                        reporter.jobProgress(getApplicationContext(), jobData.mRid, jobId, jobData);
                        break;
                    case TL_EV_JOB_CANCELED:
                        jobData.updateState(com.hp.jetadvantage.link.services.joblet.model.JobState.TL_ST_CANCELED); // CANCELED state for clients
                        jobData.mIsSuccess = false;

                        sendMessage(jobId, jobData, Result.RESULT_OK, JobEventType.JET_EV_TERMINAL, true);
                        showJobNotification();

                        // JOB CANCELED is a terminal condition, so removing the jobData for this job.  And then if we're not tracking any more jobs
                        // let's put this service back into background
                        if (JobDataContentProvider.remove(getContentResolver(), key) == 0) {
                            stopForeground(true);
                        }

                        reporter.cancel(getApplicationContext(), jobData.mRid);
                        sendPendingIntent(jobId, jobData, Result.RESULT_OK);
                        break;
                    case TL_EV_JOB_COMPLETED:
                        // This is abnormal case. Fixed : JALPINF-1078
                        jobData.updateState(com.hp.jetadvantage.link.services.joblet.model.JobState.TL_ST_COMPLETE);    // COMPLETE state for clients
                        jobData.mIsSuccess = true;

                        sendMessage(jobId, jobData, Result.RESULT_OK, JobEventType.JET_EV_TERMINAL, true);
                        showJobNotification();

                        // JOB COMPLETE is a terminal condition, so removing the jobData for this job.  And then if we're not tracking any more jobs
                        // let's put this service back into background
                        if (JobDataContentProvider.remove(getContentResolver(), key) == 0) {
                            stopForeground(true);
                        }

                        reporter.jobComplete(getApplicationContext(), jobData.mRid, jobId, jobData);

                        sendPendingIntent(jobId, jobData, Result.RESULT_OK);
                        Log.d(TAG, "Sent JOB Complete Pending Intent for " + jobId);
                        break;
                    default:
                        SLog.w(TAG, "Unhandled ev: " + ev + " in state: " + jobData.mState);
                        break;
                }
                break;

            case TL_ST_PROGRESSING:
                // We doesn't distinguish between PENDING and PROGRESSING for emulated jobs
                if (!emulatedEvent &&
                        ev != Event.TL_EV_JOB_COMPLETED) {
                    break;
                }
            case TL_ST_JOB_PENDING:
                switch (ev) {
                    case TL_EV_JOB_COMPLETED:
                        jobData.updateState(com.hp.jetadvantage.link.services.joblet.model.JobState.TL_ST_COMPLETE);    // COMPLETE state for clients
                        jobData.mIsSuccess = true;

                        sendMessage(jobId, jobData, Result.RESULT_OK, JobEventType.JET_EV_TERMINAL, true);
                        showJobNotification();

                        // JOB COMPLETE is a terminal condition, so removing the jobData for this job.  And then if we're not tracking any more jobs
                        // let's put this service back into background
                        if (JobDataContentProvider.remove(getContentResolver(), key) == 0) {
                            stopForeground(true);
                        }

                        reporter.jobComplete(getApplicationContext(), jobData.mRid, jobId, jobData);

                        sendPendingIntent(jobId, jobData, Result.RESULT_OK);
                        Log.d(TAG, "Sent JOB Complete Pending Intent for " + jobId);
                        break;
                    case TL_EV_JOB_CANCELED:
                        jobData.updateState(com.hp.jetadvantage.link.services.joblet.model.JobState.TL_ST_CANCELED); // CANCELED state for clients
                        jobData.mIsSuccess = false;

                        sendMessage(jobId, jobData, Result.RESULT_OK, JobEventType.JET_EV_TERMINAL, true);
                        showJobNotification();

                        // JOB CANCELED is a terminal condition, so removing the jobData for this job.  And then if we're not tracking any more jobs
                        // let's put this service back into background
                        if (JobDataContentProvider.remove(getContentResolver(), key) == 0) {
                            stopForeground(true);
                        }

                        reporter.cancel(getApplicationContext(), jobData.mRid);
                        sendPendingIntent(jobId, jobData, Result.RESULT_OK);
                        break;
                    case TL_EV_JOB_FAILED:
                        resultCode = bundle.containsKey(Result.KEY_CODE) ? bundle.getInt(Result.KEY_CODE) : Result.RESULT_FAIL;
                        Result.ErrorCode errorCode = (Result.ErrorCode) bundle.getSerializable(Result.KEY_ERROR_CODE); // by default is UNKNOWN, if not set in bundle
                        cause = bundle.getString(Result.KEY_CAUSE); // by default is "", if not set in bundle

                        jobData.updateState(com.hp.jetadvantage.link.services.joblet.model.JobState.TL_ST_FAILED);        // FAILED state for clients
                        jobData.mIsSuccess = false;

                        // Local Broadcast uses the same Result class for broadcasting internal success
                        // and failure, hence its OK to pass RESULT_OK for this.
                        sendMessage(jobId, jobData, Result.RESULT_OK, JobEventType.JET_EV_TERMINAL, true);

                        showJobNotification();

                        // JOB FAILED is a terminal condition, so removing the jobData for this job.  And then if we're not tracking any more jobs
                        // let's put this service back into background
                        if (JobDataContentProvider.remove(getContentResolver(), key) == 0) {
                            stopForeground(true);
                        }

                        reporter.jobFailed(getApplicationContext(), jobData.mRid, jobData, resultCode, errorCode, cause);
                        // Local Broadcast uses the same Result class for broadcasting internal success
                        // and failure, hence its OK to pass RESULT_OK for this.
                        sendPendingIntent(jobId, jobData, Result.RESULT_OK);
                        break;
                    case TL_EV_JOB_OBTAIN_COMPLETED:
//                        getCompletedJob(getApplicationContext(), jobId, key, jobData);
                        break;
                    case TL_EV_GETJOBDATA_FAILED:
                        SLog.d(TAG, "Get Job Data Failed for " + jobId);
                        // Get Job Data failed. Sending failure
                        sendMessage(jobId, jobData, Result.RESULT_FAIL, JobEventType.JET_EV_TERMINAL, false);   // ProgressActivity doesn't care about cause right now

                        // Failure to get the job data is a terminal condition, so removing jobData for this job.
                        // If Joblet is not tracking any more jobs, then put the service to background
                        if (JobDataContentProvider.remove(getContentResolver(), key) == 0) {
                            stopForeground(true);
                        }

                        reporter.jobFailed(getApplicationContext(), jobData.mRid,
                                jobData,
                                bundle.getInt(Result.KEY_CODE),
                                (Result.ErrorCode) bundle.getSerializable(Result.KEY_ERROR_CODE),
                                bundle.getString(Result.KEY_CAUSE));
                        sendPendingIntent(jobId, jobData, Result.RESULT_FAIL);    // Intent doesn't care about cause
                        break;
                    case TL_EV_JOB_PROGRESS:
                        jobData.updateState(com.hp.jetadvantage.link.services.joblet.model.JobState.TL_ST_PROGRESSING);
                        sendMessage(jobId, jobData, Result.RESULT_OK, JobEventType.JET_EV_JOB_PROGRESS, true);
                        showJobNotification();
                        reporter.jobProgress(getApplicationContext(), jobData.mRid, jobId, jobData);
                        // Set back to pending for the next progress event
                        jobData.updateState(com.hp.jetadvantage.link.services.joblet.model.JobState.TL_ST_JOB_PENDING);    // Set back to pending for the next progress event
                        break;
                    case TL_EV_CONFIRMATION:
                        // Update only JPD text
                        jobData.setConfirmed(true);
                        sendMessage(jobId, jobData, Result.RESULT_OK, JobEventType.JET_EV_JOB_PROGRESS, true);
                        break;
                    case TL_EV_JOB_WAITING:
                        jobData.updateState(com.hp.jetadvantage.link.services.joblet.model.JobState.TL_ST_WAITING);

                        if (jobData.mSource != JobSource.EXTERNAL) {
                            // Send ordered broadcast, in case if there's no users receiver, ours will get broadcast
                            reporter.jobConfirmOrdered(getApplicationContext(), jobData.mRid, jobId, jobData);
                            showJobNotification();
                        }
                        jobData.updateState(com.hp.jetadvantage.link.services.joblet.model.JobState.TL_ST_JOB_PENDING);    // Set back to pending for the next progress event
                        break;
                    case TL_EV_JOB_PROCEED:
                        jobData.updateState(com.hp.jetadvantage.link.services.joblet.model.JobState.TL_ST_WAITING);
                        SLog.d(TAG, "Requested Joblet Handling of waiting job");
                        sendMessage(jobId, jobData, Result.RESULT_OK, JobEventType.JET_EV_NPC, true);
                        jobData.updateState(com.hp.jetadvantage.link.services.joblet.model.JobState.TL_ST_JOB_PENDING);    // Set back to pending for the next progress event
                        break;
                    case TL_EV_JOB_PROCESSING_INFO:
                        // send extra progress if some cause has been added
                        reporter.jobProgress(getApplicationContext(), jobData.mRid, jobId, jobData);
                        break;
                    default:
                        SLog.w(TAG, "Unhandled ev: " + ev + " in state: " + jobData.getState());
                        break;
                }
                break;

            default:
                SLog.w(TAG, "Unhandled ev: " + ev + " in state: " + jobData.getState());
        }
    }

    /**
     * Handles restart of the service
     */
    private void handleRestart() {
        SLog.d(TAG, "handling restart");
        List<Bundle> jobBundles = JobDataContentProvider.getPersistedJobData(getContentResolver());

        if (null == jobBundles) {
            SLog.w(TAG, "jobBundles from persistence is null; perhaps there was nothing to monitor when the service restarted." +
                    " Returning and waiting for a new job to begin monitoring");
            return;
        }
        for (Bundle bundle : jobBundles) {
            JobletService.sendEvent(this, Event.TL_EV_PREINIT, bundle);
        }
    }

    // Returns the JobData in the jobDataMap for the specified key
    // If it doesn't exist, it creates a new one.  The specified key doesn't
    // exist in the map is when startMonitoring is called with a new job
    private JobData getJobData(final String key, final String jobId, final Bundle bundle) {
        JobData jobData;
        if (JobDataContentProvider.contains(getContentResolver(), key)) {
//[security]             SLog.d(TAG, "Model contains key " + key);
            jobData = JobDataContentProvider.get(getContentResolver(), key);
            jobData.addPendingIntent(bundle);    // This is in case a second request to monitor the same jobId was received with a pending intent extra
        } else {
//[security]            SLog.d(TAG, "Model doesn't contain key " + key + ", Creating...");
            jobData = new JobData();

            jobData.addPendingIntent(bundle);
            JobDataContentProvider.put(getContentResolver(), key, jobId, jobData);
        }
        if (bundle != null) {
            if (bundle.containsKey(JobletService.KEY_SHOW_UI)) {
                jobData.mShowUi = bundle.getBoolean(JobletService.KEY_SHOW_UI);
                SLog.d(TAG, "Bundle contains showUi key and got value as  " + bundle.getBoolean(JobletService.KEY_SHOW_UI));
            }
            // Sometimes the app sending monitorJob could be received before the monitorJob from other Tasklets.
            // Hence, its required to check for this regardless of whether JobData gets created due to request from app or from Tasklet
            if (bundle.containsKey(Joblet.Keys.KEY_SINGLESEGMENTSCAN_FLAG)) {
                jobData.mIsSingleSegmentScan = bundle.getBoolean(Joblet.Keys.KEY_SINGLESEGMENTSCAN_FLAG);
                SLog.d(TAG, "Bundle contains single segment key and got value as " + bundle.getBoolean(Joblet.Keys.KEY_SINGLESEGMENTSCAN_FLAG));
            }
            if (bundle.containsKey(Joblet.Keys.KEY_RID)) {
                jobData.mRid = bundle.getString(Joblet.Keys.KEY_RID);
                SLog.d(TAG, "Bundle contains rid key and got value as  " + bundle.getString(Joblet.Keys.KEY_RID));
            }
            if (bundle.containsKey(Joblet.Keys.KEY_JOB_TYPE)) {
                jobData.mDeviceType = (JobInfo.JobType) bundle.getSerializable(Joblet.Keys.KEY_JOB_TYPE);
                SLog.d(TAG, "Bundle contains Job Type key and got value as " + bundle.getSerializable(Joblet.Keys.KEY_JOB_TYPE));
            }
            // Avoid setting of embedded monitor to true once it set to false
            if (bundle.containsKey(KEY_EMBEDDED_MONITOR)) {
                jobData.mEmbeddedMonitor = bundle.getBoolean(KEY_EMBEDDED_MONITOR);
                SLog.d(TAG, "Bundle contains embedded monitor key and got value as " + bundle.getBoolean(KEY_EMBEDDED_MONITOR));
            }
            if (bundle.containsKey(Joblet.Keys.KEY_CLIENT_PACKAGE)) {
                jobData.mClientPackage = bundle.getString(Joblet.Keys.KEY_CLIENT_PACKAGE);
            }
            if (bundle.containsKey(Joblet.Keys.KEY_CLIENTS_VERSION)) {
                jobData.mClientVersion = bundle.getInt(Joblet.Keys.KEY_CLIENTS_VERSION);
                SLog.d(TAG, "Client API Level: " + jobData.mClientVersion);
            }

            if (bundle.containsKey(JOB_NAME_TAG)) {
                jobData.mJobName = bundle.getString(JOB_NAME_TAG);
            }
            if (bundle.containsKey(Joblet.Keys.KEY_SCAN_IMAGE_COUNT)) {
                jobData.mScanImageCount = bundle.getInt(Joblet.Keys.KEY_SCAN_IMAGE_COUNT);
            }
            if (bundle.containsKey(Joblet.Keys.KEY_SCANNINGCOMPLETED_FLAG)) {
                jobData.scanningCompleted = bundle.getBoolean(Joblet.Keys.KEY_SCANNINGCOMPLETED_FLAG);
            }
            if (bundle.containsKey(Joblet.Keys.KEY_DST_COUNT)) {
                jobData.mDestinationCount = bundle.getInt(Joblet.Keys.KEY_DST_COUNT);
            }
            if (bundle.containsKey(Joblet.Keys.KEY_DST_TOTAL)) {
                jobData.mDestinationTotal = bundle.getInt(Joblet.Keys.KEY_DST_TOTAL);
            }
            if (bundle.containsKey(Joblet.Keys.KEY_PRINT_IMAGE_COUNT)) {
                jobData.mPrintImageCount = bundle.getInt(Joblet.Keys.KEY_PRINT_IMAGE_COUNT);
            }
            if (bundle.containsKey(Joblet.Keys.KEY_SET_COUNT)) {
                jobData.mSetCount = bundle.getInt(Joblet.Keys.KEY_SET_COUNT);
            }
            if (bundle.containsKey(Joblet.Keys.KEY_SHEET_COUNT)) {
                jobData.mSheetCount = bundle.getInt(Joblet.Keys.KEY_SHEET_COUNT);
            }
            if (bundle.containsKey(KEY_EMULATED_NUM_COPIES_TAG)) {
                jobData.mNumCopies = bundle.getInt(KEY_EMULATED_NUM_COPIES_TAG);
            }
            if (bundle.containsKey(KEY_PROCESSING_CAUSES)) {
                jobData.setProcessingCauses(bundle.<ErrorCode>getParcelableArrayList(KEY_PROCESSING_CAUSES));
            }
            if (bundle.containsKey(JOB_SOURCE_TAG)) {
                jobData.updateSource(JobSource.valueOf(bundle.getString(JOB_SOURCE_TAG)));
            }
            if (bundle.containsKey(ILetObserver.Keys.KEY_JOB_INFO)) {
                JobInfo jobInfo = bundle.getParcelable(ILetObserver.Keys.KEY_JOB_INFO);
                if (jobInfo != null) {
                    jobData.mStartUTC = jobInfo.getStartTime();
                    jobData.mCompleteUTC = jobInfo.getCompleteTime();
                    jobData.mJobName = jobInfo.getJobName();
                    jobData.mOwner = jobInfo.getOwner();
                    if (jobInfo.getJobType() == JobInfo.JobType.SCAN) {
                        ScanJobData scanJobData = jobInfo.getJobData();
                        jobData.mScanImageCount = scanJobData.getImagesScanned();
                        jobData.mScanImageProcessedCount = scanJobData.getImagesProcessed();
                        jobData.mScanImageTransmittedCount = scanJobData.getImagesTransmitted();
                        jobData.mScanSize = scanJobData.getScanSize();
                        jobData.mScanPlex = scanJobData.getDuplex();
                        jobData.mFileNames = scanJobData.getFileNames();
                        jobData.mDeviceJobState = scanJobData.getJobState();
                        jobData.mScanDestination = scanJobData.getDestination();
                    } else if (jobInfo.getJobType() == JobInfo.JobType.PRINT) {
                        PrintJobData printJobData = jobInfo.getJobData();
                        jobData.mPrintImageCount = printJobData.getImpressionsPrinted();
                        jobData.mSheetCount = printJobData.getSheetsPrinted();
                        jobData.mPrintPlex = printJobData.getDuplex();
                        jobData.mNumCopies = printJobData.getCopies();
                        jobData.mDeviceJobState = printJobData.getJobState();
                        jobData.mPrintSource = printJobData.getSource();
                    } else if (jobInfo.getJobType() == JobInfo.JobType.COPY) {
                        CopyJobData copyJobData = jobInfo.getJobData();
                        jobData.mScanImageCount = copyJobData.getImagesScanned();
                        jobData.mSheetCount = copyJobData.getSheetsPrinted();
                        jobData.mCopySize = copyJobData.getScanSize();
                        jobData.mCopyPlex = copyJobData.getDuplex();
                        jobData.mDeviceJobState = copyJobData.getJobState();
                        jobData.mJobExecutionMode = copyJobData.getJobExecutionMode();
                    }
                }
            }
        } else {
            SLog.d(TAG, "bundle is null");
        }
        return jobData;
    }

    private void localBroadcast(final String jobId, final JobData jobData, final int resultCode, final boolean sendMore) {
        // Locally broadcast to let the ProgressActivity know
        final Intent lbIntent = new Intent(jobData.mState.name());    // When JobProgressActivity uses the local broadcast, it uses the state to display different things.
        // As long as we receive the job events and independent of the job status itself, resultCode will be
        // RESULT_OK.  But in case of internal errors, this result code will reflect it and user can use the
        // result code to either look for rest of the data or not
        final Bundle lbBundle = new Bundle();
        lbBundle.putInt(Result.KEY_CODE, resultCode);
        lbBundle.putString(Joblet.Keys.KEY_JOBID, jobId);
        lbBundle.putParcelable(ILetObserver.Keys.KEY_JOB_INFO, JobData.getJobInfo(jobData));
        if (jobData.mJobName != null) {    // Pass jobName if available
            lbBundle.putString(JOB_NAME_TAG, jobData.mJobName);
        }
        lbBundle.putSerializable(JOB_TYPE_TAG, jobData.mDeviceType);    // For LocalBroadcast jobType is needed as JobProgressActivity will resolve
        // the type and show different things.
        SLog.d(TAG, "Local broadcasting job type as " + jobData.mDeviceType + " for jobId " + jobId);
        if (sendMore && resultCode == Result.RESULT_OK) {
            if (jobData.mDeviceType == JobInfo.JobType.SCAN) {
                lbBundle.putInt(Joblet.Keys.KEY_SCAN_IMAGE_COUNT, jobData.mScanImageCount);
                lbBundle.putBoolean(Joblet.Keys.KEY_SCANNINGCOMPLETED_FLAG, jobData.scanningCompleted);
                lbBundle.putInt(Joblet.Keys.KEY_DST_COUNT, jobData.mDestinationCount);
                lbBundle.putInt(Joblet.Keys.KEY_DST_TOTAL, jobData.mDestinationTotal);
            } else if (jobData.mDeviceType == JobInfo.JobType.COPY) {
                lbBundle.putInt(Joblet.Keys.KEY_SCAN_IMAGE_COUNT, jobData.mScanImageCount);
                lbBundle.putBoolean(Joblet.Keys.KEY_SCANNINGCOMPLETED_FLAG, jobData.scanningCompleted);
                lbBundle.putInt(Joblet.Keys.KEY_DST_COUNT, jobData.mDestinationCount);
                lbBundle.putInt(Joblet.Keys.KEY_DST_TOTAL, jobData.mDestinationTotal);
            } else if (jobData.mDeviceType == JobInfo.JobType.PRINT) {
                lbBundle.putInt(Joblet.Keys.KEY_PRINT_IMAGE_COUNT, jobData.mPrintImageCount);
                lbBundle.putInt(Joblet.Keys.KEY_SET_COUNT, jobData.mSetCount);
                lbBundle.putInt(Joblet.Keys.KEY_SHEET_COUNT, jobData.mSheetCount);
                lbBundle.putInt(KEY_EMULATED_NUM_COPIES_TAG, jobData.mNumCopies);
            }
        }
        lbIntent.putExtras(lbBundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(lbIntent);
    }

    /**
     * @param jobId      of the job to send message about
     * @param jobData    of the job
     * @param resultCode code to report
     * @param jobEvtType event to report
     * @param sendMore   true if more messages will follow;
     */
    private void sendMessage(final String jobId, final JobData jobData, final int resultCode, final JobEventType jobEvtType, final boolean sendMore) {
        if (!jobData.mEmbeddedMonitor) {
            SLog.d(TAG, "Monitored by activity");
            localBroadcast(jobId, jobData, resultCode, sendMore);
            return;
        }

        SLog.d(TAG, "sendMessage called for jobId: " + jobId);
        SLog.d(TAG, "Received jobEvtType as " + jobEvtType.name());
        final String progressText = getProgressText(jobId, jobData);
        SLog.d(TAG, "Job Progress Text: " + progressText);

        switch (jobEvtType) {
            case JET_EV_JOB_PROGRESS:
                if (jobData.mShowUi && ForegroundActivityTracker.isAppForJobIdInFG(jobId)) {
                    ForegroundActivityTracker.showJPD(jobId, progressText);
                    SLog.d(TAG, "Show JPD called for jobId: " + jobId);
                } else {
                    SLog.d(TAG, "App for jobId " + jobId + " is not in foreground or showUi is " + jobData.mShowUi);
                }
                break;
            case JET_EV_NPC:
                if (ForegroundActivityTracker.isAppForJobIdInFG(jobId)) {
                    ForegroundActivityTracker.setNPCDVisibility(jobId, true);
                    SLog.d(TAG, "Show JPD called for jobId: " + jobId);
                } else {
                    SLog.d(TAG, "App for jobId " + jobId + " is not in foreground. Hence showing NPCD in Notification");
                    showNPCDNotification(getApplicationContext(), jobId);
                }
                break;
            case JET_EV_TERMINAL:
                if (ForegroundActivityTracker.isAppForJobIdInFG(jobId)) {
                    ForegroundActivityTracker.cancelAll(jobId);
                    SLog.d(TAG, "Cancel All called for jobId: " + jobId);
                } else {
                    SLog.d(TAG, "App for jobId " + jobId + " is not in foreground.");
                }

                // Check if this job is actually in waiting state
                // TODO: provide support of multiple waiting jobs
                if (jobData.mState == com.hp.jetadvantage.link.services.joblet.model.JobState.TL_ST_WAITING ||
                        jobData.mState == com.hp.jetadvantage.link.services.joblet.model.JobState.TL_ST_CANCELED) {
                    removeNPCDNotification(this);
                }
                break;
            default:
                SLog.d(TAG, "Unknown Job Event Type");
                break;
        }
    }

    private String getProgressText(final String jobId, final JobData jobData) {
        String text = "";
        SLog.d(TAG, "getProgressText for " + jobId);
        jobData.log();
        switch (jobData.mState) {
            case TL_ST_COMPLETE:
                text = getString(R.string.completed);
                break;
            case TL_ST_CANCELED:
                text = getString(R.string.job_canceled);
                break;
            case TL_ST_JOB_PENDING:
                text = getString(R.string.job_progressing);
                break;
            case TL_ST_PROGRESSING:
                if (jobData.mDeviceType == JobInfo.JobType.PRINT) {
                    text = getString(R.string.printing);
                    if (jobData.mPrintImageCount > 0) {
                        //text = text + " " + getPrintProgressDisplayString(jobData);// TODO: Use this line instead of the next line when DEFCT00916068 is fixed
                        text = text + " Page " + jobData.mPrintImageCount;
                    }
                } else if (jobData.mDeviceType == JobInfo.JobType.SCAN) {
                    text = getString(R.string.scanning);
                    if (jobData.scanningCompleted) {
                        text = getString(R.string.saving);
                    } else if (jobData.mScanImageCount > 0) {
                        text = getString(R.string.scanning) + " " + getString(R.string.job_image_number, jobData.mScanImageCount);
                    }
                } else if (jobData.mDeviceType == JobInfo.JobType.COPY) {
                    text = getString(R.string.scanning);
                    if (jobData.scanningCompleted) {
                        text = getString(R.string.printing);
                        if (jobData.mSheetCount > 0) {
                            //text = text + " " + getPrintProgressDisplayString(jobData);// TODO: Use this line instead of the next line when DEFCT00916068 is fixed
                            text = text + " Page " + jobData.mSheetCount;
                        }
                    } else if (jobData.mScanImageCount > 0) {
                        text = getString(R.string.scanning) + " " + getString(R.string.job_image_number, jobData.mScanImageCount);
                    }
                } else {
                    text = getString(R.string.job_progressing);
                }
                break;
            case TL_ST_FAILED:
                text = getString(R.string.failed);
                break;
            default:
                SLog.d(TAG, "Unhandled state");
                break;
        }

        SLog.d(TAG, "progressText for " + jobId + " is " + text);
        return text;
    }

    private void sendPendingIntent(final String jobId, final JobData jobData, final int resultCode) {
        final Bundle extras = new Bundle();

        extras.putString(Joblet.Keys.KEY_JOBID, jobId);
        extras.putInt(Result.KEY_CODE, resultCode);
        extras.putParcelable(ILetObserver.Keys.KEY_JOB_INFO, JobData.getJobInfo(jobData));

        // As long as we receive the job events and independent of the job status itself, resultCode will be RESULT_OK.
        // But in case of internal errors, this result code will reflect it and user can use the
        // result code to either look for rest of the data or not
        // As discussed with AJ, pendingIntent does not need to include a 'cause' if the code is not RESULT_OK because the
        // Tasklets do a getJobInfo and provide that data to the apps
        if (resultCode == Result.RESULT_OK) {
            extras.putBoolean(Joblet.Keys.KEY_IS_SUCCESS, jobData.mIsSuccess);
            if (jobData.mDeviceType == JobInfo.JobType.SCAN) {
                extras.putInt(Joblet.Keys.KEY_SCAN_IMAGE_COUNT, jobData.mScanImageCount);
                extras.putInt(Joblet.Keys.KEY_DST_COUNT, jobData.mDestinationCount);
                extras.putInt(Joblet.Keys.KEY_DST_TOTAL, jobData.mDestinationTotal);
            } else if (jobData.mDeviceType == JobInfo.JobType.COPY) {
                extras.putInt(Joblet.Keys.KEY_SCAN_IMAGE_COUNT, jobData.mScanImageCount);
                extras.putInt(Joblet.Keys.KEY_SHEET_COUNT, jobData.mSheetCount);
            } else if (jobData.mDeviceType == JobInfo.JobType.PRINT) {
                extras.putInt(Joblet.Keys.KEY_PRINT_IMAGE_COUNT, jobData.mPrintImageCount);
                extras.putInt(Joblet.Keys.KEY_SET_COUNT, jobData.mSetCount);
                extras.putInt(Joblet.Keys.KEY_SHEET_COUNT, jobData.mSheetCount);
            }
        }

        // Add extra causes if any
        extras.putString(Result.KEY_CAUSE, jobData.getCause());

        for (final Intent intent : jobData.mPendingIntents) {
            intent.putExtras(extras);

            sendBroadcast(intent);
            SLog.d(TAG, "Sent pending Intent with " + resultCode + " for jobId " + jobId);
        }
    }

    private static final class Reporter extends AbstractReporter {
        /**
         * Default constructor
         */
        Reporter() {
            super(Joblet.ACTION, Joblet.CONTENT_URI, Joblet.TAG);
        }

        void jobProgress(final Context context, final String rid, final String jobId, final JobData jobData) {
            final Bundle bundle = new Bundle();

            String extraSegment = null;

            if (jobData.mDeviceType == JobInfo.JobType.SCAN) {
                SLog.d(TAG, "ScanJobProgress: rid = " + rid + " jobId = " + jobId + " imageCount = " + jobData.mScanImageCount +
                        " dstCount = " + jobData.mDestinationCount + " dstTotal = " + jobData.mDestinationTotal);
                bundle.putString(Joblet.Keys.KEY_JOBID, jobId);
                bundle.putInt(Joblet.Keys.KEY_SCAN_IMAGE_COUNT, jobData.mScanImageCount);
                bundle.putInt(Joblet.Keys.KEY_DST_COUNT, jobData.mDestinationCount);
                bundle.putInt(Joblet.Keys.KEY_DST_TOTAL, jobData.mDestinationTotal);
                bundle.putParcelable(ILetObserver.Keys.KEY_JOB_INFO, JobData.getJobInfo(jobData));
                extraSegment = AbstractReporter.EXTRA_SEGMENT_SCAN;
            } else if (jobData.mDeviceType == JobInfo.JobType.COPY) {
                SLog.d(TAG, "CopyJobProgress: rid = " + rid + " jobId = " + jobId + " imageCount = " + jobData.mScanImageCount +
                        " dstCount = " + jobData.mDestinationCount + " dstTotal = " + jobData.mDestinationTotal);
                bundle.putString(Joblet.Keys.KEY_JOBID, jobId);
                bundle.putInt(Joblet.Keys.KEY_SCAN_IMAGE_COUNT, jobData.mScanImageCount);
                bundle.putInt(Joblet.Keys.KEY_SHEET_COUNT, jobData.mSheetCount);
                bundle.putParcelable(ILetObserver.Keys.KEY_JOB_INFO, JobData.getJobInfo(jobData));
                extraSegment = AbstractReporter.EXTRA_SEGMENT_COPY;
            } else if (jobData.mDeviceType == JobInfo.JobType.PRINT) {
                SLog.d(TAG, "PrintJobProgress: rid = " + rid + " jobId = " + jobId + " imageCount = + " + jobData.mPrintImageCount +
                        " setCount = " + jobData.mSetCount + " sheetCount = " + jobData.mSheetCount);
                bundle.putString(Joblet.Keys.KEY_JOBID, jobId);
                bundle.putInt(Joblet.Keys.KEY_PRINT_IMAGE_COUNT, jobData.mPrintImageCount);
                bundle.putInt(Joblet.Keys.KEY_SET_COUNT, jobData.mSetCount);
                bundle.putInt(Joblet.Keys.KEY_SHEET_COUNT, jobData.mSheetCount);
                bundle.putParcelable(ILetObserver.Keys.KEY_JOB_INFO, JobData.getJobInfo(jobData));
                extraSegment = AbstractReporter.EXTRA_SEGMENT_PRINT;
            }

            setExtraSegment(extraSegment);

            if (jobData.mSource != JobSource.EXTERNAL) {
                progress(context, rid, bundle);
            }
        }

        void jobComplete(final Context context, final String rid, final String jobId, final JobData jobData) {
            final Bundle bundle = new Bundle();
            String extraSegment = null;

            Intent intent = new Intent(DataCollectorContracts.Intent.ACTION_EVENT);

            if (jobData.mDeviceType == JobInfo.JobType.SCAN) {
                SLog.d(TAG, "ScanJobComplete: rid = " + rid + " jobId = " + jobId + " isSuccess = " + jobData.mIsSuccess + " imageCount = + " +
                        jobData.mScanImageCount + " dstCount = " + jobData.mDestinationCount + " dstTotal = " + jobData.mDestinationTotal);
                bundle.putString(Joblet.Keys.KEY_JOBID, jobId);
                bundle.putBoolean(Joblet.Keys.KEY_IS_SUCCESS, jobData.mIsSuccess);
                bundle.putInt(Joblet.Keys.KEY_SCAN_IMAGE_COUNT, jobData.mScanImageCount);
                bundle.putInt(Joblet.Keys.KEY_DST_COUNT, jobData.mDestinationCount);
                bundle.putInt(Joblet.Keys.KEY_DST_TOTAL, jobData.mDestinationTotal);
                bundle.putParcelable(ILetObserver.Keys.KEY_JOB_INFO, JobData.getJobInfo(jobData));
                extraSegment = AbstractReporter.EXTRA_SEGMENT_SCAN;

                intent.putExtra(DataCollectorContracts.EXTRA_EVENT, DataCollectorContracts.LinkSdkCountersCategory.EV_SCANNED);
            } else if (jobData.mDeviceType == JobInfo.JobType.PRINT) {
                SLog.d(TAG, "PrintJobComplete: rid = " + rid + " jobId = " + jobId + " isSuccess = " + jobData.mIsSuccess + " imageCount = + " +
                        jobData.mPrintImageCount + " setCount = " + jobData.mSetCount + " sheetCount = " + jobData.mSheetCount);
                bundle.putString(Joblet.Keys.KEY_JOBID, jobId);
                bundle.putBoolean(Joblet.Keys.KEY_IS_SUCCESS, jobData.mIsSuccess);
                bundle.putInt(Joblet.Keys.KEY_PRINT_IMAGE_COUNT, jobData.mPrintImageCount);
                bundle.putInt(Joblet.Keys.KEY_SET_COUNT, jobData.mSetCount);
                bundle.putInt(Joblet.Keys.KEY_SHEET_COUNT, jobData.mSheetCount);
                bundle.putParcelable(ILetObserver.Keys.KEY_JOB_INFO, JobData.getJobInfo(jobData));
                extraSegment = AbstractReporter.EXTRA_SEGMENT_PRINT;

                intent.putExtra(DataCollectorContracts.EXTRA_EVENT, DataCollectorContracts.LinkSdkCountersCategory.EV_PRINTED);
            } else if (jobData.mDeviceType == JobInfo.JobType.COPY) {
                SLog.d(TAG, "CopyJobComplete: rid = " + rid + " jobId = " + jobId + " isSuccess = " + jobData.mIsSuccess + " imageCount = + " +
                        jobData.mPrintImageCount + " setCount = " + jobData.mSetCount + " sheetCount = " + jobData.mSheetCount);
                bundle.putString(Joblet.Keys.KEY_JOBID, jobId);
                bundle.putBoolean(Joblet.Keys.KEY_IS_SUCCESS, jobData.mIsSuccess);
                bundle.putInt(Joblet.Keys.KEY_PRINT_IMAGE_COUNT, jobData.mPrintImageCount);
                bundle.putInt(Joblet.Keys.KEY_SET_COUNT, jobData.mSetCount);
                bundle.putInt(Joblet.Keys.KEY_SHEET_COUNT, jobData.mSheetCount);
                bundle.putParcelable(ILetObserver.Keys.KEY_JOB_INFO, JobData.getJobInfo(jobData));
                extraSegment = AbstractReporter.EXTRA_SEGMENT_COPY;

                intent.putExtra(DataCollectorContracts.EXTRA_EVENT, DataCollectorContracts.LinkSdkCountersCategory.EV_COPIED);
            }

            setExtraSegment(extraSegment);

            if (jobData.mSource != JobSource.EXTERNAL) {
                Log.d(TAG, "Sending job complete for " + jobId);
                complete(context, rid, bundle);
            }

            JobletContentProvider.removeClient(context.getContentResolver(), null, rid);
        }

        void jobFailed(final Context context, final String rid, final JobData jobData, final int code, final Result.ErrorCode errorCode, final String cause) {
            fail(context, rid, code, errorCode, cause);
        }

        void jobConfirmOrdered(final Context context, final String rid, final String jobId, final JobData jobData) {
            final Bundle bundle = new Bundle();
            bundle.putString(ILetObserver.Keys.KEY_RID, rid);

            SLog.d(TAG, "Sending confirm for job id " + jobId + " of type " + jobData.mDeviceType);

            if (jobData.mDeviceType == JobInfo.JobType.SCAN) {
                SLog.d(TAG, "ScanJobConfirm: rid = " + rid + " jobId = " + jobId + " isSuccess = " + jobData.mIsSuccess + " imageCount = + " +
                        jobData.mScanImageCount + " dstCount = " + jobData.mDestinationCount + " dstTotal = " + jobData.mDestinationTotal);
                bundle.putString(Joblet.Keys.KEY_JOBID, jobId);
                bundle.putInt(Joblet.Keys.KEY_SCAN_IMAGE_COUNT, jobData.mScanImageCount);
                bundle.putInt(Joblet.Keys.KEY_DST_COUNT, jobData.mDestinationCount);
                bundle.putInt(Joblet.Keys.KEY_DST_TOTAL, jobData.mDestinationTotal);
            }

            bundle.putString(ILetObserver.Keys.KEY_STATE, ILetObserver.State.CONFIRMATION);

            // Check if there's JobletAbstractObserver registered from target package, in case if no, we'll send broadcast to ourselves
            if (TextUtils.isEmpty(jobData.mClientPackage)) {
                SLog.d(TAG, "Send ordered without client");
                notifyOrdered(context, bundle, null);
            } else {
                if (JobletContentProvider.isClientRegistered(context.getContentResolver(), jobData.mClientPackage, rid)) {
                    SLog.d(TAG, "Send ordered with client");
                    notifyOrdered(context, bundle, null);
                    JobletContentProvider.removeClient(context.getContentResolver(), null, rid);
                } else {
                    SLog.d(TAG, "Send confirm action");
                }
            }
        }
    }

    private static String getRID(final String mfpId, final String jobId) {
        String rid = mfpId.replace("/", "-");
        rid = rid.replaceAll("\\.|:", "");
        return rid + "-" + jobId;
    }

    /**
     * Internal monitoring start method based on received parameters
     *
     * @param context           {@link Context}
     * @param jobId             to monitor
     * @param pendingIntent     to send to the client in case of job finish
     * @param taskletAttributes to apply for monitoring
     */
    public static void startMonitoring(final Context context,
                                       final String jobId,
                                       final Intent pendingIntent,
                                       final JobletAttributes taskletAttributes) {
        SLog.d(TAG, "Starting job monitoring for " + jobId);
        final Bundle bundle = new Bundle();
        bundle.putString(Joblet.Keys.KEY_JOBID, jobId);

        if (pendingIntent != null) {
            SLog.d(TAG, "Received Intent to be sent upon job completion");
            bundle.putParcelable(Joblet.Keys.KEY_PENDING_INTENT, pendingIntent);
        }

        if (taskletAttributes != null) {
            SLog.d(TAG, "taskletAttributes not empty: " + taskletAttributes.getShowUi() + " extra: " + taskletAttributes.getExtras());
            final Bundle extraBundle = taskletAttributes.getExtras();

            if (extraBundle != null) {
                if (extraBundle.containsKey(Joblet.Keys.KEY_RID)) {
                    bundle.putString(Joblet.Keys.KEY_RID, extraBundle.getString(Joblet.Keys.KEY_RID));
                }

                if (extraBundle.containsKey(Joblet.Keys.KEY_SINGLESEGMENTSCAN_FLAG)) {
                    SLog.d(TAG, "Single segment scan flag found in JobletAttributes");
                    bundle.putBoolean(Joblet.Keys.KEY_SINGLESEGMENTSCAN_FLAG, extraBundle.getBoolean(Joblet.Keys.KEY_SINGLESEGMENTSCAN_FLAG));
                }

                if (extraBundle.containsKey(Joblet.Keys.KEY_JOB_TYPE)) {
                    SLog.d(TAG, "Job Type flag found in JobletAttributes");
                    bundle.putSerializable(Joblet.Keys.KEY_JOB_TYPE, extraBundle.getSerializable(Joblet.Keys.KEY_JOB_TYPE));
                    // Ignore the showUi if received from Tasklet. See below NOTE.
                } else {
                    // NOTE: The showUi received from the App must take precedence over that from the Tasklet.
                    // However, the monitorJob request from Tasklet could come after the same request from App
                    // and override the flag.  Hence used the presence of the KEY_JOB_TYPE to know that this
                    // monitorJob received was from Tasklet.
                    SLog.d(TAG, "Show UI flag found in JobletAttributes: " + taskletAttributes.getShowUi());
                    bundle.putBoolean(KEY_SHOW_UI, taskletAttributes.getShowUi());
                }

                // Check if there's clients version provided in extras and determine type of monitoring
                //if (extraBundle != null && extraBundle.containsKey(Joblet.Keys.KEY_CLIENTS_VERSION)) {
                //    bundle.putBoolean(KEY_EMBEDDED_MONITOR,
                //            extraBundle.getInt(Joblet.Keys.KEY_CLIENTS_VERSION) > Sdk.VERSION_LEVEL.ONE);
                //} else {
                //    // There's no clients version provided by extras, so determine it based on tasklets attrs
                //    bundle.putBoolean(KEY_EMBEDDED_MONITOR, taskletAttributes.getVersion() > Sdk.VERSION_LEVEL.ONE);
                //}

                if (extraBundle.containsKey(Joblet.Keys.KEY_CLIENT_PACKAGE)) {
                    bundle.putString(Joblet.Keys.KEY_CLIENT_PACKAGE, extraBundle.getString(Joblet.Keys.KEY_CLIENT_PACKAGE));

                }
            } else {
                SLog.d(TAG, "Extras is empty, set Show UI flag as: " + taskletAttributes.getShowUi());
                bundle.putBoolean(KEY_SHOW_UI, taskletAttributes.getShowUi());
            }

            bundle.putBoolean(KEY_EMBEDDED_MONITOR, true);
        }

        bundle.putString(JobletService.JOB_SOURCE_TAG, JobSource.SDK.name());

        JobletService.sendEvent(context, Event.TL_EV_INIT, bundle);
    }

    /**
     * Sends event to JobletService
     *
     * @param context apps context
     * @param ev      to be send
     * @param bundle  with data for event
     */
    @SuppressLint("RestrictedApi")
    static void sendEvent(final Context context, final Event ev, final Bundle bundle) {
        Preconditions.checkNotNull(bundle, "bundle cannot be null as we need to send a minimum of jobId");
        final Intent intent = new Intent(ev.name());
        intent.setClass(context, JobletService.class);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    // Notification related constants
    private static final int JOB_PROGRESS_NOTIFICATION_ID = 2005;
    private static final int JOB_CONFIRM_NOTIFICATION_ID = 2015;
    private static boolean SHOW_NOTIFICATION = false; // Notification is not necessary since we usually have 1 job
    private static final int NOTI_REQ_CODE = 110410;

    /**
     * Shows next page confirmation notification
     *
     * @param context {@link Context} to access resources
     * @param jobId   to show notification for
     */
    private static void showNPCDNotification(final Context context, final String jobId) {
        SLog.d(TAG, "showNPCDNotification for jobId: " + jobId);

        final Resources res = context.getResources();
        // Create PendingIntent
        final Intent yesIntent = new Intent(YES_ACTION);
        yesIntent.putExtra(Joblet.Keys.KEY_JOBID, jobId);
        final Intent noIntent = new Intent(NO_ACTION);
        noIntent.putExtra(Joblet.Keys.KEY_JOBID, jobId);

        final PendingIntent yesPI = PendingIntent.getBroadcast(context, NOTI_REQ_CODE, yesIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final PendingIntent noPI = PendingIntent.getBroadcast(context, NOTI_REQ_CODE, noIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        // Build the action buttons
        // This is only for API 20 and above.  Can't use this.
//        Notification.Action yesAction = new Notification.Action.Builder(R.drawable.yes, getString(R.string.yes), yesPI).build();
//        Notification.Action noAction = new Notification.Action.Builder(R.drawable.no, getString(R.string.no), yesPI).build();

        final Notification.BigTextStyle bigStyle = new Notification.BigTextStyle();

        bigStyle.setBigContentTitle(res.getString(R.string.confirmation_dialog_title));
        bigStyle.bigText(res.getString(R.string.confirmation_dialog_message));

        // Building the notification
        final Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(context.getString(R.string.confirmation_dialog_title))
                .setSmallIcon(R.drawable.job_progress_notification)
                .setProgress(100, 0, true)
                .setAutoCancel(false)
                .setPriority(Notification.PRIORITY_MAX)
                .setStyle(bigStyle)
                .setTicker(context.getString(R.string.confirmation_dialog_message));

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Notification.Action actionYes =
                    new Notification.Action.Builder(R.drawable.yes, context.getString(R.string.yes), yesPI).build();
            final Notification.Action actionNo =
                    new Notification.Action.Builder(R.drawable.no, context.getString(R.string.no), noPI).build();

            builder.addAction(actionYes).addAction(actionNo);
        } else {
            builder.addAction(R.drawable.yes, context.getString(R.string.yes), yesPI)
                    .addAction(R.drawable.no, context.getString(R.string.no), noPI);
        }

        final Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;

        final NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(JOB_CONFIRM_NOTIFICATION_ID, notification);
    }

    // Handles the Jobs in JobDataMap and shows appropriate notification
    private void showJobNotification() {
        SLog.d(TAG, "showJobNotification called and the flag is " + SHOW_NOTIFICATION);

        if (SHOW_NOTIFICATION && JobDataContentProvider.size(getContentResolver()) > 0) {
            int progressing = 0;
            int waiting = 0;
            int canceled = 0;

            for (JobData jobData : JobDataContentProvider.getJobs(getContentResolver())) {
                switch (jobData.mState) {
                    case TL_ST_JOB_PENDING:
                    case TL_ST_PROGRESSING:
                        progressing++;
                        break;
                    case TL_ST_WAITING:
                        waiting++;
                        break;
                    case TL_ST_CANCELED:
                        canceled++;
                        break;
                    case TL_ST_COMPLETE:
                    case TL_ST_IDLE:
                    default:
                        // nop
                        break;
                }
            }
            if (progressing > 0 || waiting > 0 || canceled > 0) {    // If there's at least one of them to show, then show notification
                String status = (progressing > 0 ? String.format(getString(R.string.noti_jobs_progressing), Integer.toString(progressing)) : "") +
                        (waiting > 0 ? String.format(getString(R.string.noti_jobs_waiting), Integer.toString(waiting)) : "") +
                        (canceled > 0 ? String.format(getString(R.string.noti_jobs_canceled), Integer.toString(canceled)) : "");

                final Intent intent = new Intent(JOB_STATUS_ACTIVITY_ACTION);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                final Notification.Builder builder = new Notification.Builder(this);
                builder.setContentTitle(status)
                        .setSmallIcon(R.drawable.job_progress_notification)
                        .setProgress(0, 0, true)
                        .setAutoCancel(false)
                        .setContentIntent(pendingIntent);
                final Notification notification = builder.build();
                this.startForeground(JOB_PROGRESS_NOTIFICATION_ID, notification);
                SLog.d(TAG, "Notified job progress. Joblet in Foreground.");
            }
        }
    }

    /**
     * Removes Next Page Confirmation Notification
     *
     * @param context {@link Context}
     */
    static void removeNPCDNotification(final Context context) {
        final NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(JOB_CONFIRM_NOTIFICATION_ID);
    }

    // Notification action receiver
    private final BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            SLog.d(TAG, "Notification Receiver onReceive");
            if (intent == null) {
                SLog.w(TAG, "Intent is null");
                return;
            }
            if (YES_ACTION.equals(intent.getAction())) {
                SLog.d(TAG, "Received Yes Action");
                String jobId = intent.getStringExtra(Joblet.Keys.KEY_JOBID);
                SLog.d(TAG, " for jobId: " + jobId);
//                confirmJob(jobId, JobCondition.JC_WAITING_FOR_NEXT_SEGMENT, ConfirmValues.CV_CONTINUE); // TODO: Should get the correct JobCondition
            } else if (NO_ACTION.equals(intent.getAction())) {
                SLog.d(TAG, "Received No Action");
                String jobId = intent.getStringExtra(Joblet.Keys.KEY_JOBID);
                SLog.d(TAG, " for jobId: " + jobId);
//                confirmJob(jobId, JobCondition.JC_WAITING_FOR_NEXT_SEGMENT, ConfirmValues.CV_STOP); // TODO: Should get the correct JobCondition
            } else {
                SLog.w(TAG, "Didn't receive valid action");
                return;
            }
            // Cancel the notification now that job confirmation is sent
            removeNPCDNotification(context);
        }
    };
}
