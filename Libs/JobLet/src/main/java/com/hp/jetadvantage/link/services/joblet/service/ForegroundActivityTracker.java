// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.joblet.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.Joblet;
import com.hp.jetadvantage.link.api.job.ui.JobInteractionDialog;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;

/**
 * Tracks the foreground activity for a jobId
 */

// TODO: Timer to dismiss job confirmation dialog
public final class ForegroundActivityTracker extends Service {
    private static final String TAG = "Joblet";
    private static Messenger sToClient = null;
    private static String sJobId;
    private Messenger mFromClient = null;

    private static final String JOBMGT_JOBS_STATUS_CONTENT_FILTER = "body/(type,stateDetails,status)";

    @Override
    public IBinder onBind(Intent intent) {
        SLog.d(TAG, "onBind");

        return mFromClient.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            SpsPermissionHelper.ensurePermission(getApplicationContext());

            mFromClient = new Messenger(new ClientMsgHandler(getApplicationContext()));
            SLog.d(TAG, "ForegroundActivityTracker service is created");
        } catch (Exception e) {
            SLog.d(TAG, "Not created foregroundActivity because of permission." + e.getMessage());
        }
    }

    /**
     * Setter for job id
     *
     * @param jobId to be stored
     */
    private synchronized static void setJobId(final String jobId) {
        sJobId = jobId;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sToClient = null;
        setJobId(null);
        SLog.d(TAG, "ForegroundActivityTracker service is destroyed");
    }

    public static boolean isAppForJobIdInFG(final String jobId) {
        if (sToClient == null) {
            SLog.d(TAG, "isAppForJobIdInFG: sClient is null.  Returning false.");
            return false;
        } else {
            SLog.d(TAG, "sJobId = " + sJobId + " jobId = " + jobId);
            return (jobId.equals(sJobId));
        }
    }

    public static void setNPCDVisibility(final String jobId, final boolean visible) {
        SLog.d(TAG, "Setting NPCD visibility to " + visible);
        int messageType = (visible) ? Joblet.Message.MSG_SHOW_NPCD : Joblet.Message.MSG_CANCEL_NPCD;

        if (sToClient != null) {

            Bundle jobIdBundle = new Bundle();
            jobIdBundle.putString(Joblet.Keys.KEY_JOBID, jobId);
            Message msg = Message.obtain(null, messageType, 0, 0, jobIdBundle);
            try {
                sToClient.send(msg);
                SLog.d(TAG, "sToClient is not null. Sent NPCD visibility message");
            } catch (final RemoteException re) {
                SLog.e(TAG, "Failed to send message: " + msg.what + ": " + re);
            }
        } else {
            SLog.d(TAG, "sToClient is null");
        }
    }

    public static void showJPD(final String jobId, final String progressText) {
        if (sToClient != null) {
            Bundle jobIdBundle = new Bundle();
            jobIdBundle.putString(Joblet.Keys.KEY_JOBID, jobId);
            Message msg = Message.obtain(null, Joblet.Message.MSG_SHOW_JPD, 0, 0, jobIdBundle);
            Bundle bundle = new Bundle();
            bundle.putString(JobInteractionDialog.PROGRESS_TEXT_EXTRA, progressText);
            msg.setData(bundle);

            try {
                sToClient.send(msg);
                SLog.d(TAG, "sToClient is not null. Sent SHOW_JPD message");
            } catch (final RemoteException re) {
                SLog.e(TAG, "Failed to send message: " + msg.what + ": " + re);
            }
        } else {
            SLog.d(TAG, "sToClient is null");
        }
    }

    public static void cancelJPD(final int jobId) {
        if (sToClient != null) {
            Message msg = Message.obtain(null, Joblet.Message.MSG_CANCEL_JPD, jobId, 0);
            try {
                sToClient.send(msg);
                SLog.d(TAG, "sToClient is not null. Sent CANCEL_JPD message");
            } catch (final RemoteException re) {
                SLog.e(TAG, "Failed to send message: " + msg.what + ": " + re);
            }
        } else {
            SLog.d(TAG, "sToClient is null");
        }
    }

    public static void cancelAll(final String jobId) {
        if (sToClient != null) {
            Bundle jobIdBundle = new Bundle();
            jobIdBundle.putString(Joblet.Keys.KEY_JOBID, jobId);
            Message msg = Message.obtain(null, Joblet.Message.MSG_CANCEL_ALL, 0, 0, jobIdBundle);
            try {
                sToClient.send(msg);
                SLog.d(TAG, "sToClient is not null. Sent CANCEL_ALL message");
            } catch (final RemoteException re) {
                SLog.e(TAG, "Failed to send message: " + msg.what + ": " + re);
            }
        } else {
            SLog.d(TAG, "sToClient is null");
        }
    }

    private static class ClientMsgHandler extends Handler {
        private CheckJobStatusAsyncTask mCheckJobTask = null;

        private Context mContext;

        /**
         * Default constructor
         *
         * @param context {@link Context}
         */
        ClientMsgHandler(final Context context) {
            mContext = context;
        }

        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);

            SLog.d(TAG, "Received: " + msg.what);

            switch (msg.what) {
                case Joblet.Message.MSG_IN_FG:
                    Bundle jobIdBundle = (Bundle) msg.obj;
                    setJobId(jobIdBundle.getString(Joblet.Keys.KEY_JOBID));
                    sToClient = msg.replyTo;

                    if (mCheckJobTask != null) {
                        mCheckJobTask.cancel(true);
                    }

                    mCheckJobTask = new CheckJobStatusAsyncTask(mContext, sJobId);
                    mCheckJobTask.execute();
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    /**
     * {@link AsyncTask} to execute job state checking and show NPCD if job is pending
     */
    private static final class CheckJobStatusAsyncTask extends AsyncTask<Void, Void, Void> {
        private final Context mContext;
        private final String mJobId;

        /**
         * Default constructor
         *
         * @param context to use for check of job status
         * @param jobId   to be checked
         */
        private CheckJobStatusAsyncTask(final Context context, final String jobId) {
            mContext = context;
            mJobId = jobId;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            if (mContext == null) {
                return null;
            }

            final Result result = new Result();
            final Bundle paramsBundle = new Bundle();

            paramsBundle.putString(Joblet.Params.JOB_ID_TAG, mJobId);
            paramsBundle.putString(Joblet.Params.JOB_INFO_FILTER_TAG, JOBMGT_JOBS_STATUS_CONTENT_FILTER);

            final Bundle infoBundle =
                    mContext.getContentResolver().call(Joblet.CONTENT_URI, Joblet.Method.GET_JOB_INFO, null, paramsBundle);

            if (null == infoBundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
                return null;
            }

            // Fill in any errors
            int code = Result.RESULT_OK;
            Result.ErrorCode errorCode = null;
            String cause = null;
            // Error
            if (infoBundle.containsKey(Result.KEY_CODE)) {
                code = infoBundle.getInt(Result.KEY_CODE);
            }

            // ErrorCode
            if (infoBundle.containsKey(Result.KEY_ERROR_CODE)) {
                errorCode = (Result.ErrorCode) infoBundle.get(Result.KEY_ERROR_CODE);
            }

            // optional
            if (infoBundle.containsKey(Result.KEY_CAUSE)) {
                cause = infoBundle.getString(Result.KEY_CAUSE);
            }
            Result.pack(result, code, errorCode, cause);
            return null;
        }
    }
}
