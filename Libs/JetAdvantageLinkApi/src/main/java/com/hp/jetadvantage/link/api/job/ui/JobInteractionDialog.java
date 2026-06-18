// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.job.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.annotation.CommonApi;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.JobService;
import com.hp.jetadvantage.link.api.job.Joblet;

import java.lang.ref.WeakReference;

/**
 * @hide The client need not know the internals of Job Dialogs handling on client side
 */
@CommonApi
public class JobInteractionDialog extends Fragment {
    @CommonApi
    private enum BindState {
        DISCONNECTED, BINDING, CONNECTED
    }

    private static final String TAG = "Joblet";
    private static WeakReference<Activity> sParentActivity;
    private String mJobId = "";
    private BindState mBound = BindState.DISCONNECTED;
    private Messenger mFromServer;
    private Messenger mToServer;
    private final ServerMsgHandler mServerHandler = new ServerMsgHandler();

    private static final String FG_TRACKER_SVC_COMP_NAME = "com.hp.jetadvantage.link.services.joblet.service.ForegroundActivityTracker";

    /**
     * Extra from the service for progress text
     *
     * @hide internal use
     */
    public static final String PROGRESS_TEXT_EXTRA = "progressTextExtra";

    private static final String STRING_TYPE_RES = "string";

    /**
     * @hide internal usage
     */
    public Messenger getFromServer() {
        return mFromServer;
    }

    /**
     * @hide internal usage
     */
    public Messenger getToServer() {
        return mToServer;
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            mToServer = new Messenger(service);
            mBound = BindState.CONNECTED;

            Bundle jobIdBundle = new Bundle();
            jobIdBundle.putString(Joblet.Keys.KEY_JOBID, mJobId);
            final Message message = Message.obtain(null, Joblet.Message.MSG_IN_FG, 0, 0, jobIdBundle);
            message.replyTo = mFromServer;

            try {
                mToServer.send(message);
            } catch (final RemoteException re) {
                SLog.e(TAG, "Failed to send message to FG Tracking service: " , re);
            }

            SLog.d(TAG, "Service Connected");
        }

        /**
         * @hide internal usage
         */
        @Override
        public void onServiceDisconnected(final ComponentName name) {
            mBound = BindState.DISCONNECTED;
            mToServer = null;
            SLog.d(TAG, "Service Disconnected");
        }
    };

    /**
     * Internal setter for Job Id for JobService
     *
     * @param jobId to be set
     * @hide internal use
     */
    public void setJobId(final String jobId) {
        if (jobId != null && jobId.length() > 0) {
            mJobId = jobId;
        }
    }

    /**
     * @hide internal usage
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLog.d(TAG, "Fragment onCreate");
        setRetainInstance(true);
        mFromServer = new Messenger(mServerHandler);
    }

    /**
     * @hide internal usage
     */
    @Override
    public void onPause() {
        super.onPause();
        SLog.d(TAG, "Fragment onPause");
        if (mBound != BindState.DISCONNECTED) {
            getActivity().unbindService(mConnection);
            mBound = BindState.DISCONNECTED;
            SLog.d(TAG, "Unbound with the service");
        }

        // Cancel the dialog
        Message message = Message.obtain(null, Joblet.Message.MSG_CANCEL_ALL, 0, 0);
        try {
            mFromServer.send(message);
            SLog.d(TAG, "Sent Cancel All message");
        } catch (final RemoteException re) {
            SLog.d(TAG, "RemoteException sending cancel message: " , re);
        }
    }

    /**
     * @hide internal usage
     */
    @Override
    public void onResume() {
        super.onResume();

        SLog.d(TAG, "Fragment onResume");
        bindService();
        SLog.d(TAG, "Bound to Services package and service");
    }

    /**
     * @hide internal usage
     */
    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SLog.d(TAG, "onActivityCreated");
        sParentActivity = new WeakReference<>(getActivity());
    }

    /**
     * @hide internal usage
     */
    @Override
    public void onDetach() {
        super.onDetach();
        SLog.d(TAG, "onDetach");
        JobService.sJobInteractionDialog = null;
        // There's no more attached activity, so cancel dialog which holds activity references
        mServerHandler.cancelAll();
    }

    /**
     * Sets parent activity for this job
     *
     * @param activity to be set as parent
     * @hide internal use
     */
    public void setParentActivity(final Activity activity) {
        sParentActivity = new WeakReference<>(activity);
    }

    /**
     * Determines if progress UI should be displayed
     *
     * @param showUi true if progress Ui should be displayed
     * @hide internal use only
     */
    public void setShowUi(final boolean showUi) {
        mServerHandler.setShowUi(showUi);
    }

    /**
     * Check if connection is alive and re-bind if needed
     */
    public void bindService() {
        if (getActivity() != null && mBound == BindState.DISCONNECTED) {
            final Intent intent = new Intent();
            intent.setComponent(new ComponentName(Sdk.SERVICES_PACKAGE, FG_TRACKER_SVC_COMP_NAME));
            getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            mBound = BindState.BINDING;
        }
    }

    /**
     * @hide internal usage
     */
    @CommonApi
    private static class ServerMsgHandler extends Handler {

        private ProgressDialog mJPD = null;
        private boolean mShowJPD = false;
        private boolean mShowProgressUi = false;
        private boolean mJobCanceling = false;

        // Cache the last SHOW JP params
        private String mJPText = "";
        private String mJobId;
        private String mDefaultProgressText;

        void initJPD() {
            SLog.d(TAG, "initJPD: mJobId = " + mJobId);
            mJPD = new ProgressDialog(sParentActivity.get());
            mJPD.setCancelable(true);
            mJPD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mJPD.setIndeterminate(true);

            final Resources r = sParentActivity.get().getResources();
            final String pkg = sParentActivity.get().getPackageName();
            final int titleTextStrId = r.getIdentifier("progress_dialog_title", STRING_TYPE_RES, pkg);

            mJPD.setTitle(titleTextStrId);

            final int cancelJobStrId = r.getIdentifier("cancel_job", STRING_TYPE_RES, pkg);
            mJPD.setButton(DialogInterface.BUTTON_NEGATIVE, sParentActivity.get().getString(cancelJobStrId), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    SLog.d(TAG, "Cancel Job button clicked.  Canceling the Job");
                    mJobCanceling = true;
                    mJPD.setMessage(sParentActivity.get().getString(cancelJobStrId));

                    SLog.i(TAG, "Canceling job: " + mJobId);
                    final Result result = JobService.cancelJob(sParentActivity.get(), mJobId);

                    if (result.getCode() == Result.RESULT_OK) {
                        SLog.d(TAG, "Successfully canceled the job");
                        final int cancelSuccessId = r.getIdentifier("job_cancel_success", STRING_TYPE_RES, pkg);
                        mJPD.setMessage(sParentActivity.get().getString(cancelSuccessId));
                        mJPD.cancel();
                        mJPD = null;
                    } else {
                        SLog.d(TAG, "Failed to cancel the job: " + result.getCause());
                        final int cancelFailId = r.getIdentifier("job_cancel_failed", STRING_TYPE_RES, pkg);
                        mJPD.setMessage(sParentActivity.get().getString(cancelFailId));
                    }
                }
            });

            mDefaultProgressText = r.getString(r.getIdentifier("sps_default_progress_text",
                    STRING_TYPE_RES, sParentActivity.get().getPackageName()));

            mJPD.setMessage(mJPText);
            mJPD.show();
        }

        /**
         * @hide internal usage
         */
        @Override
        public synchronized void handleMessage(final Message msg) {
            SLog.d(TAG, "Msg: " + msg.what + "\n"
                    + "Msg.arg1: " + msg.arg1 + "\n"
                    + "Msg.arg2: " + msg.arg2 + "\n"
                    + "mShowJPD: " + mShowJPD + "\n"
                    + "mJobCanceling: " + mJobCanceling + "\n"
                    + "mJobId: " + mJobId + "\n"
                    + "mJPD: " + mJPD + "\n");

            switch (msg.what) {
                case Joblet.Message.MSG_RE_SHOW_JPD:
                    // Only show the JPD if NPCD is not showing
                    if (mShowJPD && (!mJobCanceling) && (mJPD == null)) {
                        SLog.d(TAG, "JPD is null and NPCD is not showing.  Calling initJPD to show JPD");
                        initJPD();
                    } else {
                        SLog.d(TAG, "Can't show JPD. mShowJPD: " + mShowJPD + " mJobCanceling: " + mJobCanceling + " mJPD: " + mJPD);
                    }
                    break;

                case Joblet.Message.MSG_SHOW_JPD:
                    // Ignore showing Job Progress if the NPCD is showing. Joblet is anyhow showing the consolidated JobProgress in the notification

                    if (!mShowProgressUi) {
                        SLog.d(TAG, "JPD is not needed");
                        break;
                    }

                    Bundle jobIdBundle = (Bundle) msg.obj;

                    // If this is the first job or if the received jobId is different than the one we have cached, which means its a new job, then set the flag
                    String msgJobId = jobIdBundle.getString(Joblet.Keys.KEY_JOBID);
                    if (mJobId == null || !mJobId.equals(msgJobId)) {
                        SLog.d(TAG, "SHOW_JPD: mJobId " + mJobId + " is null or its not same as in jobBundle " + msgJobId);
                        SLog.d(TAG, "Setting mShowJPD = true");
                        mShowJPD = true;
                        mJobCanceling = false;
                        mJPText = mDefaultProgressText;
                        SLog.d(TAG, "NPCD: Not client handling");
                    }

                    mJobId = msgJobId;
                    SLog.d(TAG, "SHOW_JPD: handleMessage:mJobId = " + mJobId);

                    if (msg.getData().containsKey(PROGRESS_TEXT_EXTRA)) {
                        mJPText = msg.getData().getString(PROGRESS_TEXT_EXTRA);
                    }
                    if (mShowJPD) {
                        if (mJPD == null) {
                            if (!mJobCanceling) {
                                SLog.d(TAG, "SHOW_JPD: JPD is null. Calling initJPD to show JPD.");
                                initJPD();
                            }
                        } else if (!mJobCanceling) {
                            SLog.d(TAG, "SHOW_JPD: JPD is not null. Calling setMessage to show mJPText.");
                            mJPD.setMessage(mJPText);
                            if (!mJPD.isShowing()) {
                                SLog.d(TAG, "SHOW_JPD: JPD show.");
                                mJPD.show();
                            }
                        }
                    } else {
                        SLog.d(TAG, "mShowJPD is " + mShowJPD);
                    }
                    break;

                case Joblet.Message.MSG_CANCEL_JPD:
                    if (mJPD != null) {
                        mJPD.cancel();
                        mJPD = null;
                    }
                    break;

                case Joblet.Message.MSG_CANCEL_ALL:
                    cancelAll();
                    break;

                case Joblet.Message.MSG_CLIENT_HANDLED_CONFIRMATION:
                    cancelAll();
                    SLog.d(TAG, "NPCD: client handling");
                    break;

                default:
                    super.handleMessage(msg);
            }
        }

        /**
         * Cancels all current dialogs
         */
        private void cancelAll() {
            cancelJpd();
        }

        /**
         * Cancels JPD
         */
        private void cancelJpd() {
            if (mJPD != null && mJPD.isShowing()) {
                mJPD.cancel();
            }
            mJPD = null;
        }

        /**
         * Stores setting to show JPD
         *
         * @param showUi true - to show, false - otherwise
         */
        void setShowUi(final boolean showUi) {
            mShowProgressUi = showUi;
        }
    }
}
