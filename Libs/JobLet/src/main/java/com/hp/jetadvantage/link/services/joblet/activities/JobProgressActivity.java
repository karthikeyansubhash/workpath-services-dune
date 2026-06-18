// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.joblet.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.Joblet;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.joblet.R;
import com.hp.jetadvantage.link.services.joblet.model.JobState;
import com.hp.jetadvantage.link.services.joblet.service.JobletService;


public class JobProgressActivity extends Activity {
    private static final String TAG = "Joblet";
    public static final String ACTION_JOB_PROGRESS_UI = "com.hp.jetadvantage.link.intent.action.JOBPROGRESSUI";

    private LinearLayout mCloseLayout;
    private Button mCancelButton;
    private TextView mProgressText;
    private TextView mJobNumberText;
    private TextView mJobNumberLabel;
    private TextView mJobNameText;
    private TextView mJobNameLabel;

    private boolean mIsJobCancellingFlag = false;
    private static String mJobId;

    private BroadcastReceiver mJobStateReceiver = new BroadcastReceiver() {
        @SuppressLint("StringFormatMatches")
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent == null || intent.getAction() == null || !(intent.hasExtra(Joblet.Keys.KEY_JOBID))) {
                SLog.w(TAG, "Invalid Params received in JobState BroadcastReceiver. Returning...");
                return;
            }
            // If this Activity is already in the foreground, and a broadcast is receiver, this Activity
            // updates itself to show progress of the received jobId.  Its possible that the Activity was
            // started with some jobId and the broadcast received while being in foreground was a different jobId.
            mJobId = intent.getStringExtra(Joblet.Keys.KEY_JOBID);
            if (mJobId == null) {
                SLog.w(TAG, "Invalid JobId received in JobState BroadcastReceiver. Returning...");
                finishJob(getString(R.string.failed));
                return;
            }
            // If a non-RESULT_OK is received, then finish this activity
            if (intent.hasExtra(Result.KEY_CODE) && (intent.getIntExtra(Result.KEY_CODE, Result.RESULT_FAIL) != Result.RESULT_OK)) {
                SLog.w(TAG, "Failure result code received in JobState BroadcastReceiver. Finishing...");
                finishJob(getString(R.string.failed));// Not displaying any cause of the failure
                return;
            }
            SLog.d(TAG, "Received jobId as " + mJobId);
            // Updating view for valid jobId
            if (mJobId != null) {
                mJobNumberLabel.setVisibility(View.VISIBLE);
                mJobNumberText.setVisibility(View.VISIBLE);
                mJobNumberText.setText(String.valueOf(mJobId));
            } else {
                // Else, resetting the visibility to prevent showing old data
                mJobNumberLabel.setVisibility(View.INVISIBLE);
                mJobNumberText.setVisibility(View.INVISIBLE);
            }
            // Updating view if jobName is received
            if (intent.hasExtra(JobletService.JOB_NAME_TAG) && (intent.getStringExtra(JobletService.JOB_NAME_TAG) != null)) {
                mJobNameLabel.setVisibility(View.VISIBLE);
                mJobNameText.setVisibility(View.VISIBLE);
                mJobNameText.setText(intent.getStringExtra(JobletService.JOB_NAME_TAG));
            } else {
                // Else, resetting the visibility to prevent showing old data
                mJobNameLabel.setVisibility(View.INVISIBLE);
                mJobNameText.setVisibility(View.INVISIBLE);
            }
            SLog.d(TAG, "Received the job State as " + JobState.valueOf(intent.getAction()));
            switch (JobState.valueOf(intent.getAction())) {
                case TL_ST_COMPLETE:
                    SLog.d(TAG, "Job Complete of jobId " + mJobId);
                    finishJob(R.string.completed);
                    break;
                case TL_ST_CANCELED:
                    SLog.d(TAG, "Job Canceled of jobId " + mJobId);
                    finishJob(R.string.job_canceled);
                    break;
                case TL_ST_JOB_PENDING:
                    SLog.d(TAG, "Job Pending of jobId " + mJobId);
                    mProgressText.setText(R.string.job_progressing);
                    break;
                case TL_ST_PROGRESSING:
                    SLog.d(TAG, "Job Progressing of jobId " + mJobId);
                    if (mIsJobCancellingFlag) {
                        SLog.d(TAG, "Job is being cancelled.. so event ignored !!!");
                        return;
                    }
                    // If jobType is not received, we simply tell that the Job is progressing as in the state received
                    mProgressText.setText(R.string.job_progressing);
                    break;
                case TL_ST_WAITING:
                    SLog.d(TAG, "Job Waiting of jobId " + mJobId);
                    break;
                case TL_ST_FAILED:
                    SLog.d(TAG, "Job Failed of jobId " + mJobId);
                    finishJob(R.string.job_failed);
                    break;
                default:
                    SLog.d(TAG, "Unknown state notification");
                    break;
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(android.R.id.content).invalidate();
                }
            });
        }
    };

    private String getPrintProgressDisplayString(final Bundle bundle) {
        String displayStr = "";
        if (bundle.containsKey(Joblet.Keys.KEY_SET_COUNT)) {
            int setCount = bundle.getInt(Joblet.Keys.KEY_SET_COUNT);
            String numCopiesStr = null;
            if (bundle.containsKey(JobletService.KEY_EMULATED_NUM_COPIES_TAG)) {
                final int numCopies = bundle.getInt(JobletService.KEY_EMULATED_NUM_COPIES_TAG);
                numCopiesStr = (numCopies > 0) ? (("/" + numCopies) + " " + getString(R.string.copies)) : "";
            }
            displayStr = setCount + numCopiesStr;
        }
        return displayStr;
    }

    // TODO: This method must be removed and the getPrintProgressDisplayString must be used for Print job types too
    // DEPENDS: Resolution of DEFCT00916068.  As mentioned in the defect, the number of copies and other data is not
    // received for Print job by XUP.  As per spec this data must be provided.  Until its provided, Joblet doesn't
    // know the number of copies.  Hence displaying the page number.
    private String getPageNumDisplayString(final Bundle bundle) {
        String pageNumStr = "";
        if (bundle.containsKey(Joblet.Keys.KEY_PRINT_IMAGE_COUNT)) {
            final int pageNumber = bundle.getInt(Joblet.Keys.KEY_PRINT_IMAGE_COUNT);

            pageNumStr = (pageNumber > 0) ? " " + getString(R.string.job_image_number, pageNumber) : getString(R.string.ellipces);
        }
        return pageNumStr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        mIsJobCancellingFlag = false;
        setContentView(R.layout.job_progress);
        getViews();

        final Intent intent = getIntent();
        mJobId = intent.getStringExtra(Joblet.Keys.KEY_JOBID);
        SLog.d(TAG, "Received jobId as " + mJobId);
        if (mJobId == null) {
            // First line of validation.  If this Activity is started without a proper JobID, then this Activity is finished
            SLog.w(TAG, "Invalid Job ID " + mJobId);
            finish();
        }
        setViewListeners();

        final PrinterInfo printer = SelectedPrinterHelper.get(getContentResolver());

        if (!PrinterInfo.isEmpty(printer)) {
            mCancelButton.setVisibility(View.INVISIBLE);
            findViewById(R.id.cancelText).setVisibility(View.INVISIBLE);
        }

        // Register receivers
        final IntentFilter filter = new IntentFilter(JobState.TL_ST_COMPLETE.name());

        filter.addAction(JobState.TL_ST_CANCELED.name());
        filter.addAction(JobState.TL_ST_FAILED.name());
        filter.addAction(JobState.TL_ST_JOB_PENDING.name());
        filter.addAction(JobState.TL_ST_PROGRESSING.name());
        filter.addAction(JobState.TL_ST_WAITING.name());
        // Failure cases could be received in UNKNOWN_STATE, hence registering for this state too
        filter.addAction(JobState.TL_ST_IDLE.name());

        LocalBroadcastManager.getInstance(this).registerReceiver(mJobStateReceiver, filter);
    }

    private void getViews() {
        mCloseLayout = (LinearLayout) findViewById(R.id.closeLayout);
        mCancelButton = (Button) findViewById(R.id.cancelJob);
        mProgressText = (TextView) findViewById(R.id.progress_text);
        mJobNumberText = (TextView) findViewById(R.id.job_number);
        mJobNumberLabel = (TextView) findViewById(R.id.job_number_label);
        mJobNameText = (TextView) findViewById(R.id.job_name);
        mJobNameLabel = (TextView) findViewById(R.id.job_name_label);
    }

    private void setViewListeners() {
        mCloseLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SLog.d(TAG, "Close clicked.  Finishing the Activity");
                finish();
            }
        });

        mCancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SLog.d(TAG, "Cancel button clicked.  Canceling the Job");
                mCancelButton.setEnabled(false);

                final Bundle paramsBundle = new Bundle();
                paramsBundle.putString(Joblet.Params.JOB_ID_TAG, mJobId);

                final Bundle bundle = getApplicationContext().getContentResolver().call(Joblet.CONTENT_URI, Joblet.Method.CANCEL_JOB,
                        null, paramsBundle);
                if (null == bundle) {
                    SLog.e(TAG, "Job cancel failed.. no response");
                    mCancelButton.setEnabled(true);
                    mIsJobCancellingFlag = false;
                } else {
                    int responseCode = bundle.getInt(Result.KEY_CODE);
                    SLog.i(TAG, "Response code: " + responseCode);
                    if (responseCode == Result.RESULT_OK) {
                        mProgressText.setText(getString(R.string.job_cancelling));
                        SLog.i(TAG, "Job with id: " + mJobId + " cancelling request registered sucessfully");
                        mIsJobCancellingFlag = true;
                    } else {
                        mIsJobCancellingFlag = false;
                        mCancelButton.setEnabled(true);
                        SLog.e(TAG, "Job cancel failed with cause: " + bundle.getString(Result.KEY_CAUSE));
                    }
                }
            }
        });
    }

    private void finishJob(final int message) {
        SLog.d(TAG, "Finish Job: " + message);
        mProgressText.setText(message);
        finish();
    }

    private void finishJob(final CharSequence message) {
        SLog.d(TAG, "Finish Job: " + message);
        mProgressText.setText(message);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mJobStateReceiver);
    }

    public static void showJobProgressActivity(final Context context, final String jobId) {
        SLog.d(TAG, "showJobProgress");
        final Intent intent = new Intent(ACTION_JOB_PROGRESS_UI);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(Joblet.Keys.KEY_JOBID, jobId);
        context.startActivity(intent);
    }
}
