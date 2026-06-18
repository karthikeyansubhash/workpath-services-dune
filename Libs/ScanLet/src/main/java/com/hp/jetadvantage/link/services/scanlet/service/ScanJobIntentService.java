/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.scanlet.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;

import com.hp.jetadvantage.link.api.scanner.intent.ScanToRequestIntent;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.intents.ScanJobCancelIntent;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceScanJobService;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentService;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.StateMachinePool;

public class ScanJobIntentService extends BaseJobIntentService {

    IDeviceScanJobService scanJobService = null;

    public ScanJobIntentService() {
        super(ScanJobIntentService.class.getSimpleName());
        TAG = TAG + "/Scan";
    }

    @Override
    protected BaseJobIntentServiceStateMachine createStateMachine(IntentService service, Looper looper) {
        return new ScanJobIntentServiceStateMachine(service, looper);
    }

    @Override
    protected boolean cancelJob(Intent intent) {
        String requestJobId = ScanJobCancelIntent.getJobId(intent);
        if (requestJobId == null) {
            SLog.e(TAG, "cancelJob : Expected parameters not found. ignore");
            return false;
        }

        String packageName = ScanJobCancelIntent.getAppPackageName(intent);
        if (packageName == null) {
            SLog.e(TAG, "cancelJob : Expected app package name not found. ignore");
            return false;
        }

        StateMachinePool pool = getStateMachinePool();
        if (pool != null && pool.findByJobId(requestJobId) == null) {
            SLog.w(TAG, "cancelJob : jobId " + requestJobId + " not found in active pool, forwarding to device anyway");
        }

        return cancelScanJob(packageName, requestJobId);
    }

    @Override
    protected String getJobCancelAction() {
        return ScanJobCancelIntent.ACTION;
    }

    @Override
    protected boolean isConcurrentJobsAllowed(Intent intent) {
        Bundle extraParams = intent.getBundleExtra(EXTRA_PARAMS);
        if (extraParams == null) {
            return false;
        }

        ScanToRequestIntent.IntentParams reqParams = ScanToRequestIntent.getIntentParams(extraParams);
        if (reqParams == null || reqParams.getTaskAttributes() == null) {
            return false;
        }

        Integer apiLevel = reqParams.getApiLevel();
        if (apiLevel == null || apiLevel < Sdk.VERSION_LEVEL.NINE) {
            return false;
        }

        return reqParams.getTaskAttributes().getAllowMultipleScan();
    }

    @Override
    protected void reportBusyToApp(Intent intent) {
        SLog.i(TAG, "reportBusyToApp : Scan");
        final ScanToRequestIntent.IntentParams reqParams = ScanToRequestIntent.getIntentParams(intent.getBundleExtra(EXTRA_PARAMS));

        if (reqParams == null || reqParams.getPackageName() == null || reqParams.getPackageName().isEmpty()) {
            SLog.e(TAG, "Report BUSY: Param is empty");
            return;
        }
        String rid = reqParams.getReqId();
        reportBusyToApp(rid, reqParams.getPackageName());
    }

    protected boolean cancelScanJob(String packageName, String jobId) {
        try {
            if (scanJobService == null) {
                scanJobService = new StandardDeviceScanJobService();
            }
            scanJobService.cancelScanJob(packageName, jobId);
            return true;
        } catch (Exception e) {
            SLog.e(TAG, "cancelScanJob : packageName=" + packageName + ", jobId=" + jobId);
            SLog.e(TAG, "cancelJob : caught exception while cancelling the job. [" + e.getMessage() + "]");
            e.printStackTrace();
            return false;
        }
    }

    protected void setScanJobService(IDeviceScanJobService scanJobService) {
        this.scanJobService = scanJobService;
    }
}
