// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.joblet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.api.job.intent.JobProgressRequestIntent;
import com.hp.jetadvantage.link.api.job.intent.JobProgressRequestIntent.Params;
import com.hp.jetadvantage.link.common.constants.CommonConstants;
import com.hp.jetadvantage.link.services.common.exception.SdkUnauthorizedException;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.joblet.service.JobletService;

public class JobletReceiver extends BroadcastReceiver {
    private static final String TAG = "Joblet";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        SLog.d(TAG, "Received broadcast");

        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case JobProgressRequestIntent.ACTION: {
                    // NOTE: JOBPROGRESS must always be handled regardless of SYSTEM_SVC_FLAG.
                    // On DUNE, all services run in the same process. Other services (e.g.
                    // StatisticsNotificationObserverService) set SYSTEM_SVC_FLAG in that shared
                    // process, which would incorrectly block JobletReceiver from processing this
                    // broadcast. The flag check is intentionally skipped for this action.
                    final Params params = JobProgressRequestIntent.getIntentParams(intent);

                    if (params.mPackageName != null) {
                        try {
                            SpsPermissionHelper.ensurePermission(context, params.mPackageName);
                        }catch (SdkUnauthorizedException sue) {
                            SLog.e(TAG, sue.getMessage());
                            throw new SecurityException("SDK permissions are not granted to empty packages");
                        }
                    }

                    // TODO This JobProgressActivity was designed for old API 1 but now disabled
                    // Check if UI is to be shown
                    //if (params.mTaskAttributes != null && params.mTaskAttributes.getShowUi()) {
                    //    SLog.d(TAG, "show UI is true, Launching JobProgressActivity");
                    //    JobProgressActivity.showJobProgressActivity(context.getApplicationContext(), params.mJobId);
                    //}

                    // In all cases start monitoring the Job
                    SLog.d(TAG, "Starting Job Monitoring for jobId " + params.mJobId);
                    JobletService.startMonitoring(context.getApplicationContext(),
                            params.mJobId,
                            params.mPendingIntent,
                            params.mTaskAttributes);
                }
                break;
            }
        } else {
            SLog.w(TAG, "Received unknown broadcast action " + (intent != null ? intent.getAction() : ""));
        }
    }
}
