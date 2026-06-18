/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.Joblet;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.common.ssp.AbstractReporter;
import com.hp.jetadvantage.link.services.common.util.WakeLock;
import com.hp.jetadvantage.link.services.joblet.service.JobletService;

/**
 * This abstract class represents a state machine for handling job intents.
 * It extends the Handler class and overrides its handleMessage method to handle different types of messages.
 * Each job service needs to extend this class and implement the abstract methods and overload handleMessage method to define the behavior of the state machine.
 */
public abstract class BaseJobIntentServiceStateMachine extends Handler {

    /**
     * Definitions for static final variables used in the JobIntentServiceStateMachine class.
     * These variables represent different message types that can be handled by the state machine.
     * External threads can send a message to trigger the state transition
     */
    public static final int MSG_START = 0;
    public static final int MSG_REPORT_FAIL = 5;
    public static final int MSG_REPORT_CANCELLED = 6;
    public static final int MSG_REPORT_COMPLETED = 7;

    // The following message types are deprecated, should not be used for state transition.
    // Instead of using these message types, the state machine handler thread directly defines the next state for the transition.
    // These constants are kept here for historical reference only.
    // public static final int MSG_END = 1;
    // public static final int MSG_REPORT_BUSY = 3;
    // public static final int MSG_REPORT_ERROR = 4;

    public static final String EXTRA_PARAMS = "params";
    public String TAG = "JobISM";

    /**
     * Final variables used in the JobIntentServiceStateMachine class
     */
    protected final String JOB_TYPE_NAME;
    private final Object mTransitionLock = new Object();

    /**
     * Private variables used in the JobIntentServiceStateMachine class
     */
    private ContentResolver mContentResolver;
    private Context mContext;
    private IntentService mIntentService;
    private Looper mLooper;
    private AbstractReporter mReporterToApp;
    private boolean mDeviceDisconnectedOrJobFail;
    private boolean mIsRunning = false;
    private BaseJobIntentServiceState mState;

    /**
     * Private variables for job information
     */
    private Bundle mExtraJobBundle;
    private Object mJobAttributesReader;
    private Bundle mJobBundle;
    private String mJobId;
    private String mRid;
    private String mTargetPackageName;

    /**
     * Private variables only for testing purpose
     */
    private boolean mStopFollowingStateTransition = false;

    public BaseJobIntentServiceStateMachine(final IntentService service, final Looper looper, final String jobTypeName) {
        this(service, looper, jobTypeName, new Reporter(), new InitState());
    }

    public BaseJobIntentServiceStateMachine(final IntentService service, final Looper looper, final String jobTypeName, final AbstractReporter reporter, final BaseJobIntentServiceState initState) {
        super(looper);

        this.mLooper = looper;
        this.mIntentService = service;
        this.mContext = service.getApplicationContext();
        this.JOB_TYPE_NAME = jobTypeName;
        this.mContentResolver = service.getContentResolver();
        this.mReporterToApp = reporter;
        mState = initState;
        TAG = TAG + "/" + JOB_TYPE_NAME;
    }

    /**
     * Constructor for testing purpose to control state transition to only one step at a time
     */
    public BaseJobIntentServiceStateMachine(final IntentService service, final Looper looper, final String jobTypeName, final boolean stopFollowingStateTransition) {
        this(service, looper, jobTypeName);
        this.mStopFollowingStateTransition = stopFollowingStateTransition;
    }

    // ================================= Override methods from Handler =================================
    @Override
    public void handleMessage(@NonNull final Message msg) {
        Result.ErrorCode errorCode;
        try {
            switch (msg.what) {
                case MSG_START:
                    if (!isRunning()) {
                        start();
                        transitionTo(createCreatingJobState((Intent) msg.obj));
                    } else {
                        reportBusyToApp((Intent) msg.obj);
                    }
                    break;
                case MSG_REPORT_FAIL:
                    errorCode = msg.arg2 != -1 ? Result.ErrorCode.values()[msg.arg2] : null;
                    String cause = msg.obj instanceof String ? (String) msg.obj : null;
                    transitionTo(new JobFailedState(msg.arg1, errorCode, cause));
                    break;
                case MSG_REPORT_COMPLETED:
                    transitionTo(createJobCompletedState());
                    break;
                case MSG_REPORT_CANCELLED:
                    transitionTo(new JobCanceledState());
                    break;
                default:
                    //throw new IllegalStateException(TAG + ", Unexpected value: " + msg.what);
                    SLog.e(TAG, "Unexpected Msg: " + msg.what);
                    break;
            }
        } catch (Exception e) {
            SLog.e(TAG, "Exception in handleMessage: " + e.getMessage(), e);
            try {
                releaseWakeLock();
                stop();
            } catch (Exception cleanup) {
                SLog.e(TAG, "Cleanup after handleMessage exception also failed", cleanup);
            }
        }
    }

    // ================================= public methods =================================
    public void acquireWakeLock() {
        WakeLock.acquire(this.mContext);
        if (this.mIntentService instanceof BaseJobIntentService) {
            StateMachinePool pool = ((BaseJobIntentService) this.mIntentService).getStateMachinePool();
            if (pool != null) {
                pool.incrementWakeLockCount();
            }
        }
    }

    public void releaseWakeLock() {
        WakeLock.release();
        if (this.mIntentService instanceof BaseJobIntentService) {
            StateMachinePool pool = ((BaseJobIntentService) this.mIntentService).getStateMachinePool();
            if (pool != null) {
                pool.decrementWakeLockCount();
            }
        }
    }

    public ContentResolver getContentResolver() {
        return this.mContentResolver;
    }

    public Context getContext() {
        return this.mContext;
    }

    public BaseJobIntentServiceState getCurrentState() {
        synchronized (mTransitionLock) {
            return this.mState;
        }
    }

    public Bundle getExtraJobBundle() {
        return this.mExtraJobBundle;
    }

    public void setExtraJobBundle(Bundle jobBundle) {
        this.mExtraJobBundle = jobBundle;
    }

    public <T> T getJobAttributesReader(Class<T> type) {
        if (type.isInstance(mJobAttributesReader)) {
            return type.cast(mJobAttributesReader);
        } else {
            return null;
        }
    }

    public <T> void setJobAttributesReader(T jobAttributesReader) {
        this.mJobAttributesReader = jobAttributesReader;
    }

    public Bundle getJobBundle() {
        return this.mJobBundle;
    }

    public void setJobBundle(Bundle jobBundle) {
        this.mJobBundle = jobBundle;
    }

    public String getJobId() {
        return this.mJobId;
    }

    public void setJobId(String jobId) {
        this.mJobId = jobId;
        if (mJobBundle == null) {
            SLog.e(TAG, "setJobId() JobBundle is null");
            return;
        }
        mJobBundle.putString(Joblet.Keys.KEY_JOBID, jobId);
        JobInfo jobInfo = mJobBundle.getParcelable(ILetObserver.Keys.KEY_JOB_INFO);
        if (jobInfo == null || jobInfo.getJobData() == null) {
            SLog.e(TAG, "setJobId() jobInfo or JobData is null");
            return;
        }
        jobInfo.setJobId(jobId);
        jobInfo.setStartTime(System.currentTimeMillis());
        SLog.d(TAG, "setJobId() jobId=" + jobId);
    }

    public JobInfo getJobInfo() {
        if (mJobBundle == null) {
            return null;
        }
        return mJobBundle.getParcelable(ILetObserver.Keys.KEY_JOB_INFO);
    }

    public String getJobRid() {
        return this.mRid;
    }

    public void setJobRid(String rid) {
        this.mRid = rid;
    }

    public AbstractReporter getReporterToApp() {
        return this.mReporterToApp;
    }

    public String getTargetPackageName() {
        return mTargetPackageName;
    }

    public void setTargetPackageName(String packageName) {
        mTargetPackageName = packageName;
    }

    public boolean isDeviceDisconnectedOrJobFail() {
        return this.mDeviceDisconnectedOrJobFail;
    }

    public void setDeviceDisconnectedOrJobFail(boolean deviceDisconnectedOrJobFail) {
        this.mDeviceDisconnectedOrJobFail = deviceDisconnectedOrJobFail;
    }

    public boolean isRunning() {
        return this.mIsRunning;
    }

    public void sendLocalJobDataToJobLet(final JobletService.Event event) {
        final Intent intent = new Intent(JobletService.ACTION_BASIC_EVENT);

        if (mJobBundle != null) {
            mJobBundle.putString(JobletService.EXTRA_BASIC_EVENT, event.name());
            intent.putExtras(mJobBundle);
            SLog.d(TAG, "sendLocalJobDataToJobLet: event=" + event.name());
            LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(intent);
        } else {
            SLog.e(TAG, "sendLocalJobDataToJobLet: mJobBundle is null, broadcast skipped for event=" + event.name());
        }
    }

    // ================================= protected methods =================================

    protected void transitionTo(BaseJobIntentServiceState nextState) {
        synchronized (mTransitionLock) {
            if (this.mState.isValidTransition(nextState)) {
                BaseJobIntentServiceState followingState = performTransition(nextState);
                if (followingState != null && !mStopFollowingStateTransition) {
                    transitionTo(followingState);
                }

            } else {
                SLog.e(TAG, "[" + JOB_TYPE_NAME + "]" + "Invalid state transition from " + this.mState.STATE_NAME + " to " + nextState.STATE_NAME);
            }
        }
    }

    protected BaseJobIntentServiceState performTransition(BaseJobIntentServiceState nextState) {
        SLog.d(TAG, "[" + JOB_TYPE_NAME + "]" + "Transition from " + this.mState.STATE_NAME + " to " + nextState.STATE_NAME);
        this.mState.onExit(this);
        this.mState = nextState;
        this.mState.onEnter(this);
        this.mState.onProcess(this);

        if (mState == null) return null;
        return this.mState.getNextState();
    }

    protected void reportBusyToApp(String rid, String targetPackage) {
        if (rid == null || rid.isEmpty() || targetPackage == null || targetPackage.isEmpty()) {
            SLog.e(TAG, "Report BUSY: Param is empty");
            return;
        }

        SLog.i(TAG, "Report BUSY for RID:" + rid);

        // make local reporter
        Reporter reporter = new Reporter();
        reporter.setTargetPackage(targetPackage);

        reporter.fail(mContext, rid, Result.RESULT_FAIL, Result.ErrorCode.JOB_FAILURE, "Device is busy.");
    }

    protected void start() {
        this.mIsRunning = true;
    }

    /**
     * Stop StateMachine and shutdown thread
     * StateMachine self-destructs after job completion
     */
    protected void stop() {
        // Clean up all resources
        this.mState = null;
        this.mJobAttributesReader = null;
        this.mJobBundle = null;
        this.mExtraJobBundle = null;
        this.mReporterToApp = null;
        this.mContentResolver = null;
        this.mContext = null;
        this.mJobId = null;
        this.mRid = null;
        this.mTargetPackageName = null;
        this.mIsRunning = false;

        // Notify pool - Pool will handle service lifecycle
        if (this.mIntentService instanceof BaseJobIntentService) {
            ((BaseJobIntentService) this.mIntentService).onStateMachineCompleted(this);
        }
        this.mIntentService = null;

        // Shutdown HandlerThread
        this.mLooper.quitSafely();
        this.mLooper = null;
    }

    // ================================= abstract methods =================================
    abstract protected CreatingJobState createCreatingJobState(Intent intent);

    abstract protected JobCompletedState createJobCompletedState();

    abstract protected void updateCanceledJobState();

    abstract protected void updateFailedJobState();

    abstract protected void updateCompletedJobState();

    abstract protected void reportBusyToApp(Intent intent);

    // ================================= Inner classes =================================
    protected static class Reporter extends AbstractReporter {
        /**
         * Default constructor
         */
        Reporter() {
            super(Joblet.ACTION, Joblet.CONTENT_URI, Joblet.TAG);
        }
    }
}
