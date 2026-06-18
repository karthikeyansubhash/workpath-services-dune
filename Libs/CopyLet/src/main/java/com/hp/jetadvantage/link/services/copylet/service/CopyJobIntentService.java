/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.copylet.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;

import com.hp.jetadvantage.link.api.copier.intent.BaseCopyRequestIntent;
import com.hp.jetadvantage.link.common.intents.CopyJobCancelIntent;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceCopyJobService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceCopyJobService;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentService;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.StateMachinePool;

/**
 * The CopyJobIntentService class is an IntentService that handles copy job requests.
 * When the service is started, it creates a handler thread and create a state machine to handle the requested copy job.
 * This service handles different types of copy job intents such as copy, delete for a stored job, and release for a stored job.
 */
public class CopyJobIntentService extends BaseJobIntentService {
    public static final String EXTRA_RESULT_RECEIVER = "paramsResultReceiver";
    public static final String PARAMS_TYPE = "paramsType";
    public static final String PARAMS_TYPE_COPY = "paramsCopy";
    public static final String PARAMS_TYPE_RELEASE = "paramsRelease";

    IDeviceCopyJobService copyJobService = null;

    public CopyJobIntentService() {
        super(CopyJobIntentService.class.getSimpleName());
        TAG = TAG + "/Copy";
    }

    // ==================================================================
    //       Static Methods
    // ==================================================================

    public static void startCopy(final Context context, final Bundle bundle) {
        final Intent intent = createCopyIntent(context, bundle, PARAMS_TYPE_COPY, null);
        start(context, intent, CopyJobIntentService.class);
        SLog.d(CopyJobIntentService.class.getSimpleName(), "CopyJobIntentService : startCopy");
    }

    public static void startRelease(final Context context, final Bundle bundle) {
        final Intent intent = createCopyIntent(context, bundle, PARAMS_TYPE_RELEASE, null);
        start(context, intent, CopyJobIntentService.class);
        SLog.d(CopyJobIntentService.class.getSimpleName(), "CopyJobIntentService : startRelease");
    }

    public static Intent createCopyIntent(Context context, final Bundle bundle, String paramsType, ResultReceiver resultReceiver) {
        Intent intent = new Intent(context, CopyJobIntentService.class);
        intent.putExtra(PARAMS_TYPE, paramsType);
        intent.putExtra(EXTRA_PARAMS, bundle);
        if (resultReceiver != null) {
            intent.putExtra(EXTRA_RESULT_RECEIVER, resultReceiver);
        }
        return intent;
    }

    // ==================================================================
    //       Override Methods from BaseJobIntentService
    // ==================================================================

    @Override
    protected boolean cancelJob(Intent intent) {
        String requestJobId = CopyJobCancelIntent.getJobId(intent);
        if (requestJobId == null) {
            SLog.e(TAG, "cancelJob : Expected parameters not found. ignore");
            return false;
        }

        String packageName = CopyJobCancelIntent.getAppPackageName(intent);
        if (packageName == null) {
            SLog.e(TAG, "cancelJob : Expected app package name not found. ignore");
            return false;
        }

        StateMachinePool pool = getStateMachinePool();
        if (pool != null && pool.findByJobId(requestJobId) == null) {
            SLog.w(TAG, "cancelJob : jobId " + requestJobId + " not found in active pool, forwarding to device anyway");
        }

        return cancelCopyJob(packageName, requestJobId);
    }

    @Override
    protected BaseJobIntentServiceStateMachine createStateMachine(IntentService service, Looper looper) {
        return new CopyJobIntentServiceStateMachine(service, looper);
    }

    @Override
    protected String getJobCancelAction() {
        return CopyJobCancelIntent.ACTION;
    }

    @Override
    protected void reportBusyToApp(Intent intent) {
        final BaseCopyRequestIntent.IntentParams reqParams =
                BaseCopyRequestIntent.getIntentParams(intent.getBundleExtra(EXTRA_PARAMS));

        if (reqParams == null || reqParams.getPackageName() == null || reqParams.getPackageName().isEmpty()) {
            SLog.e(TAG, "Report BUSY: Param is empty");
            return;
        }
        String rid = reqParams.getReqId();

        reportBusyToApp(rid, reqParams.getPackageName());
    }

    // ==================================================================
    //         Instance Methods
    // ==================================================================

    protected boolean cancelCopyJob(String packageName, String jobId) {
        try {
            if (copyJobService == null) {
                copyJobService = new StandardDeviceCopyJobService();
            }
            copyJobService.cancelCopyJob(packageName, jobId);
            return true;
        } catch (Exception e) {
            SLog.e(TAG, "cancelCopyJob : packageName=" + packageName + ", jobId=" + jobId);
            SLog.e(TAG, "cancelJob : caught exception while cancelling the job. [" + e.getMessage() + "]");
            SLog.e(TAG, Log.getStackTraceString(e));
            return false;
        }
    }

    protected void setScanJobService(IDeviceCopyJobService copyJobService) {
        this.copyJobService = copyJobService;
    }
}
