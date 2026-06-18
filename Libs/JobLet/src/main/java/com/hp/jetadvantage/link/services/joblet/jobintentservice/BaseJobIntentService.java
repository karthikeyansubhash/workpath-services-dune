/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.Nullable;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.common.notification.ServiceNotification;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;

/**
 * BaseJobIntentService is an abstract class that extends the IntentService class.
 * This class serves as a base for all job intent services in the job service modules.
 * It provides common functionality such as starting the service, handling intents, and managing a handler thread.
 * Specific job intent services should extend this class and implement the abstract methods.
 */
public abstract class BaseJobIntentService extends IntentService {
    public static final String EXTRA_PARAMS = "params";
    protected String TAG = "JobIS";

    protected int maxStateMachinePoolSize = 10;
    protected StateMachinePool mStateMachinePool;
    private volatile int mLastStartId;

    public BaseJobIntentService(String name) {
        super(name);
    }

    public static void start(final Context context, final Bundle bundle,
                             Class<? extends BaseJobIntentService> serviceClass) {
        try {
            SpsPermissionHelper.ensurePermission(context);

            final Intent intent = new Intent(context, serviceClass);
            intent.putExtra(EXTRA_PARAMS, bundle);

            context.startForegroundService(intent);

        } catch (Throwable e) {
            SLog.d(serviceClass.getSimpleName(), "BaseJobIntentService : Not created job intent service because of " +
                    "permission(start)." + e.getMessage());
        }
    }

    public static void start(final Context context, final Intent intent,
                             Class<? extends BaseJobIntentService> serviceClass) {
        try {
            SpsPermissionHelper.ensurePermission(context);
            context.startForegroundService(intent);
        } catch (Throwable e) {
            SLog.d(serviceClass.getSimpleName(), "BaseJobIntentService : Not created job intent service because of " +
                    "permission(start)." + e.getMessage());
        }
    }

    @Override
    public void onCreate() {
        SLog.i(TAG, "JobIntentService " + this.toString() + " created in process " + Thread.currentThread().getName());
        super.onCreate();

        mStateMachinePool = new StateMachinePool(this, maxStateMachinePoolSize);
        ServiceNotification.showNotification(this);
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        SLog.i(TAG, "JobIntentService " + this.toString() + "started in process " +
                Thread.currentThread().getName() + ", StartId=" + startId);

        mLastStartId = startId;

        if (intent == null) {
            SLog.e(TAG, "onStartCommand: intent is null, ignoring");
            return START_NOT_STICKY;
        }

        if (getJobCancelAction().equals(intent.getAction())) {
            cancelJob(intent);
            return START_NOT_STICKY;
        }

        if (mStateMachinePool == null) {
            SLog.e(TAG, "mStateMachinePool is null (Unexpected)");
            return START_NOT_STICKY;
        }

        BaseJobIntentServiceStateMachine sm = mStateMachinePool.acquire(isConcurrentJobsAllowed(intent));
        if (sm == null) {
            SLog.e(TAG, "Pool is full, rejecting job.");
            reportBusyToApp(intent);
            return START_NOT_STICKY;
        }
        if (intent != null && intent.hasExtra(EXTRA_PARAMS) && intent.getBundleExtra(EXTRA_PARAMS) != null) {
            Message msg = sm.obtainMessage();
            msg.what = BaseJobIntentServiceStateMachine.MSG_START;
            msg.obj = intent;
            sm.sendMessage(msg);
        } else {
            SLog.e(TAG, "Expected parameters not found. ignore");
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        try {
            mStateMachinePool.shutdown();
            mStateMachinePool = null;
        } catch (Exception ignored) {

        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    /**
     * Called when a StateMachine completes its job.
     * Uses atomic onStateMachineCompleted to prevent duplicate stopSelf() calls
     * when multiple StateMachines complete simultaneously.
     *
     * @param sm StateMachine that completed
     */
    public void onStateMachineCompleted(BaseJobIntentServiceStateMachine sm) {
        if (mStateMachinePool != null) {
            boolean noActiveJobs = mStateMachinePool.onStateMachineCompleted(sm);

            if (noActiveJobs) {
                SLog.i(TAG, "No active jobs remaining, stopping service (startId=" + mLastStartId + ")");
                stopSelf(mLastStartId);
            } else {
                SLog.d(TAG, "Other jobs still running (" +
                        mStateMachinePool.getStats().activeCount + "), service continues");
            }
        }
    }

    public StateMachinePool getStateMachinePool() {
        return mStateMachinePool;
    }

    /**
     * inject state machine for testing purpose
     *
     * @param handler state machine handler
     */
    public void setHandler(BaseJobIntentServiceStateMachine handler) {
        // For testing - not used in simple pool design
        SLog.w(TAG, "setHandler() is deprecated for simple pool design");
    }
    // ================================= abstract methods =================================

    /**
     * Creates a new JobIntentServiceStateMachine object for the job intent service.
     *
     * @param service the intent service
     * @param looper  the looper
     * @return a new JobIntentServiceStateMachine object, which extends the BaseJobIntentServiceStateMachine class
     */
    protected abstract BaseJobIntentServiceStateMachine createStateMachine(IntentService service, Looper looper);

    /**
     * Cancels a job with the specified intent
     *
     * @param intent the intent
     * @return true if the job cancel is requested successfully; otherwise, false
     */
    protected abstract boolean cancelJob(Intent intent);

    /**
     * Gets the job cancel action string
     *
     * @return the job cancel action string
     */
    protected abstract String getJobCancelAction();

    protected abstract void reportBusyToApp(Intent intent);

    /**
     * Checks whether concurrent jobs are allowed for the given intent.
     * By default, this method returns false, indicating that concurrent jobs are not allowed.
     * Subclasses can override this method to allow concurrent jobs if needed depending on the request intent.
     *
     * @param intent job intent to check for concurrent job allowance
     * @return true if concurrent jobs are allowed
     */
    protected boolean isConcurrentJobsAllowed(Intent intent) {
        return false;
    }

    protected void reportBusyToApp(String rid, String targetPackage) {
        if (rid == null || rid.isEmpty() || targetPackage == null || targetPackage.isEmpty()) {
            SLog.e(TAG, "Report BUSY: Param is empty");
            return;
        }

        SLog.i(TAG, "Report BUSY for RID:" + rid);

        // make local reporter
        BaseJobIntentServiceStateMachine.Reporter reporter = new BaseJobIntentServiceStateMachine.Reporter();
        reporter.setTargetPackage(targetPackage);

        reporter.fail(getApplicationContext(), rid, Result.RESULT_FAIL, Result.ErrorCode.JOB_FAILURE, "Device is busy. Maximum concurrent jobs reached.");
    }
}
