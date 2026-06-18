// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.job;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.copier.CopierService;
import com.hp.jetadvantage.link.api.copier.CopyAttributes;
import com.hp.jetadvantage.link.api.copier.CopyletAttributes;
import com.hp.jetadvantage.link.api.job.intent.JobProgressRequestIntent;
import com.hp.jetadvantage.link.api.job.ui.CredentialsDialog;
import com.hp.jetadvantage.link.api.job.ui.JobInteractionDialog;
import com.hp.jetadvantage.link.api.printer.PrintAttributes;
import com.hp.jetadvantage.link.api.printer.PrinterService;
import com.hp.jetadvantage.link.api.printer.PrintletAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.Scanlet;
import com.hp.jetadvantage.link.api.scanner.ScanletAttributes;
import com.hp.jetadvantage.link.api.scanner.ScannerService;
import com.hp.jetadvantage.link.api.scanner.intent.ScanToRequestIntent;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.utils.SLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * JobService provides interfaces to monitor submitted job.
 * </p>
 */
public class JobService {
    private static final String TAG = Joblet.TAG;
    private static final String JID_FRAGMENT_TAG = "JID";
    private static final String UCD_FRAGMENT_TAG = "UCD";

    /**
     * @hide only for internal usage
     */
    public static JobInteractionDialog sJobInteractionDialog;

    private JobService() {
    }

    /**
     * <p>Monitors a job asynchronously for the specified jobId.
     * It enables providing an Activity for showing job progress dialog.<br></p>
     *
     * @param activity      The activity hosting job interaction dialogs.
     * @param jobId         The jobId of the job to monitor.
     * @param letAttributes Attributes related to how the {@link JobService} should
     *                      behave.
     * @param pendingIntent (Optional) Intent that will be sent as broadcast upon job
     *                      completion. The extras passed as part of the intent as
     *                      specified in the above table provide details about the job after its
     *                      completion. It should be exported and security should be ensured by the client
     *                      (e.g. provide explicit component in the intent, check jobId using {@link Joblet.Keys#KEY_JOBID}).
     * @return String requested id which is used to correspond
     * @throws NullPointerException     if activity or jobId is null
     * @throws IllegalArgumentException if activity is destroyed or is finishing
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static String monitorJobInForeground(@NonNull final Activity activity,
                                                @NonNull final String jobId,
                                                @Nullable final JobletAttributes letAttributes,
                                                @Nullable final Intent pendingIntent) {
        Preconditions.checkNotNull(activity, "Activity must be provided");
        Preconditions.checkNotNull(jobId, "Job Id must be provided");
        Preconditions.checkArgument(!activity.isFinishing());
        Preconditions.checkArgument(!activity.isDestroyed());

        final FragmentManager fm = activity.getFragmentManager();

        if (sJobInteractionDialog == null) {
            SLog.d(TAG, "sInteractionDialog is null. Creating it");
            sJobInteractionDialog = new JobInteractionDialog();

            fm.beginTransaction().add(sJobInteractionDialog, JID_FRAGMENT_TAG).commit();
        }

        sJobInteractionDialog.setJobId(jobId);
        sJobInteractionDialog.setShowUi(letAttributes != null && letAttributes.mShowUi);
        sJobInteractionDialog.setParentActivity(activity);

        Bundle jobIdBundle = new Bundle();
        jobIdBundle.putString(Joblet.Keys.KEY_JOBID, jobId);
        Message message = Message.obtain(null, Joblet.Message.MSG_IN_FG, 0, 0, jobIdBundle);
        message.replyTo = sJobInteractionDialog.getFromServer();

        try {
            Messenger toServer = sJobInteractionDialog.getToServer();

            if (toServer != null) {
                toServer.send(message);
                SLog.d(TAG, "Sent MSG_IN_FG message to server");
            } else {
                SLog.w(TAG, "Did not send MSG_IN_FG message to server as toServer is null");
                sJobInteractionDialog.bindService();
            }
        } catch (final RemoteException re) {
            SLog.e(TAG, "Failed to send MSG_IN_FG: ", re);
        }

        final JobProgressRequestIntent intent = new JobProgressRequestIntent();
        final JobProgressRequestIntent.Params params =
                new JobProgressRequestIntent.Params(jobId, letAttributes,
                        pendingIntent, activity.getPackageName());

        intent.putIntentParams(params);
        intent.setPackage(Sdk.SERVICES_PACKAGE);

        activity.sendOrderedBroadcast(intent, null);

        final Bundle paramsBundle = new Bundle();
        paramsBundle.putString(Joblet.Params.JOB_ID_TAG, jobId);
        final Bundle bundle =
                activity.getContentResolver().call(Joblet.CONTENT_URI, Joblet.Method.GET_JOB_RID, null, paramsBundle);

        String rid;
        if (bundle != null && bundle.getInt(Result.KEY_CODE) == Result.RESULT_OK) {
            rid = bundle.getString(Joblet.Keys.KEY_RID);
        } else {
            rid = UUID.randomUUID().toString();
        }

        SLog.d(TAG, "Returning rid " + rid);
        return rid;
    }

    /**
     * <p>Currently only for internal use</p>
     *
     * <p>Monitors a job asynchronously for the specified jobId. This can be
     * optionally used to monitor a job submitted through other services. This API is used
     * for background monitoring of the job.
     *
     * If a {@link JobService.AbstractJobletObserver
     * AbstractJobletObserver} is present, the
     * {@link JobService.AbstractJobletObserver
     * AbstractJobletObserver} will receive callbacks indicating job progress,
     * completion, and / or errors. In addition, if the Intent is specified,
     * {@link JobService} it will be sent as broadcast when the job is completed (either
     * successfully or unsuccessfully) as specified in the bundle carrying the
     * key/values, listed below:</p>
     * <p/>
     * <p/>
     *
     *              <style>
     *                  #border {
     *                      border: 1px solid black;
     *                      border-collapse: collapse;
     *                  }
     *                  #border_text {
     *                      border: 1px solid black;
     *                      border-collapse: collapse;
     *                      padding: 5px;
     *                      text-align: left;
     *                  }
     *              </style>
     *
     *              <table id="border">
     *                  <tr id="border">
     *                      <th id="border_text">Job Type</th>
     *                      <th colspan="3" id="border_text">Bundle Details</th>
     *                  </tr>
     *                  <tr id="border">
     *                      <td rowspan="3" id="border_text">All</td>
     *                      <td id="border_text">{@link Joblet.Keys#KEY_JOBID Joblet.Keys.KEY_JOBID}</td>
     *                      <td id="border_text">int</td>
     *                      <td id="border_text">Job Id</td>
     *                  </tr>
     *                  <tr id="border">
     *                      <td id="border_text">{@link Result#KEY_CODE Result.KEY_CODE}</td>
     *                      <td id="border_text">int</td>
     *                      <td id="border_text">Result Code specifying success or failure of the job monitoring.
     *                      This covers any internal failure related to job monitoring and does not
     *                      indicate the state of the job itself.</td>
     *                  </tr>
     *                  <tr id="border">
     *                      <td rowspan="3" id="border_text">Scan</td>
     *                      <td id="border_text">{@link Joblet.Keys#KEY_SCAN_IMAGE_COUNT Joblet.Keys.KEY_SCAN_IMAGE_COUNT}</td>
     *                      <td id="border_text">int</td>
     *                      <td id="border_text">Number of images scanned</td>
     *                  </tr>
     *                  <tr id="border">
     *                      <td id="border_text">{@link Joblet.Keys#KEY_DST_COUNT Joblet.Keys.KEY_DST_COUNT}</td>
     *                      <td id="border_text">int</td>
     *                      <td id="border_text">Number of destinations to which the job has been delivered.</td>
     *                  </tr>
     *                  <tr id="border">
     *                      <td id="border_text">{@link Joblet.Keys#KEY_DST_TOTAL Joblet.Keys.KEY_DST_TOTAL}</td>
     *                      <td id="border_text">int</td>
     *                      <td id="border_text">Total number of destinations.</td>
     *                  </tr>
     *                  <tr id="border">
     *                      <td rowspan="3" id="border_text">Print</td>
     *                      <td id="border_text">{@link Joblet.Keys#KEY_PRINT_IMAGE_COUNT Joblet.Keys.KEY_PRINT_IMAGE_COUNT}</td>
     *                      <td id="border_text">int</td>
     *                      <td id="border_text">Number of images printed.</td>
     *                  </tr>
     *                  <tr id="border">
     *                      <td id="border_text">{@link Joblet.Keys#KEY_SHEET_COUNT Joblet.Keys.KEY_SHEET_COUNT}</td>
     *                      <td id="border_text">int</td>
     *                      <td id="border_text">Number of sheets printed.</td>
     *                  </tr>
     *                  <tr id="border">
     *                      <td id="border_text">{@link Joblet.Keys#KEY_SET_COUNT Joblet.Keys.KEY_SET_COUNT}</td>
     *                      <td id="border_text">int</td>
     *                      <td id="border_text">Number of sets printed.</td>
     *                  </tr>
     *                  <tr id="border">
     *                      <td rowspan="4" id="border_text">Copy</td>
     *                      <td id="border_text">{@link Joblet.Keys#KEY_SCAN_IMAGE_COUNT Joblet.Keys.KEY_SCAN_IMAGE_COUNT}</td>
     *                      <td id="border_text">int</td>
     *                      <td id="border_text">Number of images scanned</td>
     *                  </tr>
     *                  <tr id="border">
     *                      <td id="border_text">{@link Joblet.Keys#KEY_PRINT_IMAGE_COUNT Joblet.Keys.KEY_PRINT_IMAGE_COUNT}</td>
     *                      <td id="border_text">int</td>
     *                      <td id="border_text">Number of images printed.</td>
     *                  </tr>
     *                  <tr id="border">
     *                      <td id="border_text">{@link Joblet.Keys#KEY_SHEET_COUNT Joblet.Keys.KEY_SHEET_COUNT}</td>
     *                      <td id="border_text">int</td>
     *                      <td id="border_text">Number of sheets printed.</td>
     *                  </tr>
     *                  <tr id="border">
     *                      <td id="border_text">{@link Joblet.Keys#KEY_SET_COUNT Joblet.Keys.KEY_SET_COUNT}</td>
     *                      <td id="border_text">int</td>
     *                      <td id="border_text">Number of sets printed.</td>
     *                  </tr>
     *              </table>
     *
     * @param context       The Context in which the application is running.
     * @param jobId         The jobId of the job that you want to monitor.
     * @param letAttributes Attributes related to how the {@link JobService} should
     *                      behave.
     * @param pendingIntent (Optional) Intent that will be sent as broadcast upon job
     *                      completion. The extras passed as part of the intent as
     *                      specified in the above table provide details about the job after its
     *                      completion. It should be exported and security should be ensured by the client
     *                      (e.g. provide explicit component in the intent, check jobId using {@link Joblet.Keys#KEY_JOBID}).
     * @return Request id is returned which is used to correspond
     * {@link JobService.AbstractJobletObserver
     * AbstractJobletObserver} callbacks
     * @throws NullPointerException     if context, jobId, or letAttributes is null
     * @throws IllegalArgumentException if jobId is invalid
     *
     * @hide only for internal use by SDKs services
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static String monitorJobInBackground(@NonNull final Context context, @NonNull final String jobId,
                                                @NonNull final JobletAttributes letAttributes,
                                                @Nullable final Intent pendingIntent) {
        Preconditions.checkNotNull(context, "Context must be provided");
        Preconditions.checkNotNull(jobId, "Job Id must be provided");
        Preconditions.checkNotNull(letAttributes, "Let attributes must be provided");
        Preconditions.checkArgument(!letAttributes.mShowUi, "Invalid attribute for background monitoring");
        Preconditions.checkNotNull(letAttributes.mExtras, "Missing extras for background monitoring");

        String rid;
        if (letAttributes.mExtras.containsKey(Joblet.Keys.KEY_RID)) {
            rid = letAttributes.mExtras.getString(Joblet.Keys.KEY_RID);
        } else {
            // generating new rid
            rid = UUID.randomUUID().toString();
            letAttributes.mExtras.putString(Joblet.Keys.KEY_RID, rid);
        }

        SLog.d(TAG, "monitorJob called");
        final JobProgressRequestIntent intent = new JobProgressRequestIntent();
        final JobProgressRequestIntent.Params params =
                new JobProgressRequestIntent.Params(jobId, letAttributes, pendingIntent, context.getPackageName());
        intent.putIntentParams(params);
        intent.setPackage(Sdk.SERVICES_PACKAGE);

        context.sendOrderedBroadcast(intent, null);

        SLog.d(TAG, "Returning " + rid);
        return rid;
    }

    /**
     * <p>Request to cancel a job with jobId.</p>
     *
     * @param context The Context in which the application is running.
     * @param jobId   Job ID for requesting cancel.
     * @return {@link Result Result} Indicates the result whether job cancellation is succeeded or failed.
     * @throws NullPointerException If context or jobId is null.
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static synchronized Result cancelJob(@NonNull final Context context, @NonNull final String jobId) {
        Preconditions.checkNotNull(context, "Context must be provided");
        Preconditions.checkNotNull(jobId, "Job ID must be provided");

        final Bundle paramsBundle = new Bundle();
        paramsBundle.putString(Joblet.Params.JOB_ID_TAG, jobId);

        final Bundle bundle =
                context.getContentResolver().call(Joblet.CONTENT_URI, Joblet.Method.CANCEL_JOB, null, paramsBundle);
        if (null == bundle) {
            return new Result(Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "Return empty");
        } else {
            return new Result(bundle.getInt(Result.KEY_CODE),
                    (Result.ErrorCode) bundle.get(Result.KEY_ERROR_CODE), bundle.getString(Result.KEY_CAUSE));
        }
    }

    /**
     * <p>Returns the {@link JobInfo} for
     * the specified job ID synchronously.</p>
     *
     * @param context The Context in which the application is running.
     * @param jobId   Job ID to get the details.
     * @param result   (optional) Indicates any errors which occurred while
     *                retrieving the job info.
     * @return {@link JobInfo JobInfo} of the specified job.
     * @throws NullPointerException if context or jobId is null
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static synchronized JobInfo getJobInfo(@NonNull final Context context,
                                                  @NonNull final String jobId,
                                                  @Nullable final Result result) {
        Preconditions.checkNotNull(context, "Context must be provided");
        Preconditions.checkNotNull(jobId, "Job ID must be provided");

        final Bundle paramsBundle = new Bundle();
        paramsBundle.putString(Joblet.Params.JOB_ID_TAG, jobId);
        paramsBundle.putInt(Joblet.Params.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        final Bundle bundle =
                context.getContentResolver().call(Joblet.CONTENT_URI, Joblet.Method.GET_JOB_INFO, null, paramsBundle);
        if (null == bundle) {
            if (null != result) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "bundle is empty");
            }
        } else {
            bundle.setClassLoader(JobInfo.class.getClassLoader());

            // Fill in any errors
            if (null != result) {
                int code = Result.RESULT_OK;
                Result.ErrorCode errorCode = null;
                String cause = null;
                // Error
                if (bundle.containsKey(Result.KEY_CODE)) {
                    code = bundle.getInt(Result.KEY_CODE);
                }

                // ErrorCode
                if (bundle.containsKey(Result.KEY_ERROR_CODE)) {
                    errorCode = (Result.ErrorCode) bundle.get(Result.KEY_ERROR_CODE);
                }

                // optional
                if (bundle.containsKey(Result.KEY_CAUSE)) {
                    cause = bundle.getString(Result.KEY_CAUSE);
                }
                Result.pack(result, code, errorCode, cause);
            }

            if (bundle.containsKey(Joblet.Keys.KEY_JOB_INFO)) {
                SLog.d(TAG, "Take parcelable job info");
                bundle.setClassLoader(JobInfo.class.getClassLoader());
                return bundle.getParcelable(Joblet.Keys.KEY_JOB_INFO);
            }
        }

        return null;
    }

    /**
     * <p>This method is needed to determine if the service supported or not.
     * If it's not supported, JobService operation will be failed.</p>
     *
     * @param context The Context in which the application is running.
     * @return boolean Returns result of supported. If service is supported, method returns "TRUE".
     * @throws NullPointerException Returns error if context is null.
     *
     * @since API 1
     */
    @SuppressWarnings({"unused", "SameReturnValue"})
    @SuppressLint("RestrictedApi")
    public static boolean isSupported(@NonNull final Context context) {
        Preconditions.checkNotNull(context, "Context must be provided");
        // Currently this service is supported at all devices, at least partially
        return true;
    }

    private static String getRID(final String mfpId, final String jobId) {
        String rid = mfpId.replace("/", "-");
        rid = rid.replaceAll("\\.|:", "");
        return rid + "-" + jobId;
    }

    /**
     * Public method for internal use to compose RID.
     *
     * @param context The Context in which the application is running
     * @param jobId to use
     *
     * @return composed RID
     *
     * @hide only for internal use
     *
     * @since API 1
     */
    public static String getRid(final Context context, final String jobId) {
        final PrinterInfo pi = SelectedPrinterHelper.get(context.getContentResolver());
        String mfpId = Joblet.UNKNOWN_MFP_ID;

        if (PrinterInfo.isEmpty(pi)) {
            SLog.e(TAG, "getRid: PrinterInfo is empty");
        } else {
            mfpId = pi.mUId;
        }

        return getRID(mfpId, jobId);
    }

    /**
     * <p>
     * AbstractJobletObserver provides a mechanism to monitor the state of the
     * submitted job. The AbstractJobletObserver receives callbacks
     * when there have been changes in progress, completion, or failure of the
     * operation.
     * </p>
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public static abstract class AbstractJobletObserver extends BroadcastReceiver implements ILetObserver {
        /** Default priority for this  */
        private static final int DEFAULT_PRIORITY = IntentFilter.SYSTEM_LOW_PRIORITY + 100;
        private final Handler mHandler;

        /**
         * <p>Constructor</p>
         * @param handler The handler to run callbacks on, or null if none.
         * @since API 1
         */
        public AbstractJobletObserver(final Handler handler) {
            super();
            mHandler = handler;
        }

        /**
         * <p>Requests to register the observer to monitor the events of the job status.</p>
         *
         * @param context The Context in which the application is running. If it's null, event will not be triggered.
         * @throws NullPointerException If context is null.
         * @since API 1
         */
        @SuppressLint("RestrictedApi")
        @Override
        public final void register(@NonNull final Context context) {
            Preconditions.checkNotNull(context, "Context must be provided");

            final IntentFilter filter = new IntentFilter();

            filter.addAction(Joblet.ACTION);
            filter.setPriority(DEFAULT_PRIORITY);
            context.registerReceiver(this, filter);
        }

        /**
         * <p>Requests to unregister the observer to stop monitoring and receiving the events of the job.</p>
         *
         * @param context The Context in which the application is running.
         * @throws NullPointerException If context is null.
         * @throws IllegalArgumentException if this receiver hasn't been registered.
         *
         * @since API 1
         */
        @SuppressLint("RestrictedApi")
        @Override
        public final void unregister(@NonNull final Context context) {
            Preconditions.checkNotNull(context, "Context must be provided");
            context.unregisterReceiver(this);

            // Notify JobService about this observer un-registration
            final Intent registerIntent = new Intent(Joblet.ACTION_REGISTER);

            registerIntent.putExtra(Joblet.Keys.KEY_CLIENT_PACKAGE, context.getPackageName());
            registerIntent.setPackage(Sdk.SERVICES_PACKAGE);
            context.sendBroadcast(registerIntent);
        }

        /**
         * @hide final
         */
        @Override
        public final void onReceive(final Context context, final Intent intent) {
            if (isOrderedBroadcast()) {
                abortBroadcast();
            }

            if (mHandler == null) {
                onRecv(context, intent);
            } else {
                mHandler.post(new JobletRunnable(context, intent));
            }
        }

        private void writeStreamToFile(FileInputStream input, File file) {
            FileChannel src = null;
            FileChannel dst = null;
            try {
                src = input.getChannel();
                dst = new FileOutputStream(file).getChannel();
                dst.transferFrom(src, 0, src.size());
            } catch (FileNotFoundException e) {
                SLog.i(TAG, "[FILE]Not found");
            } catch (IOException e) {
                SLog.i(TAG, "[FILE]IO Error");
            } finally {
                try {
                    if (src != null)
                        src.close();
                    if (dst != null)
                        dst.close();
                    if (input != null)
                        input.close();
                } catch (IOException e) {}
            }
        }

        /**
         * Internal intents handler
         *
         * @param context Context for sending intents
         * @param intent Intent with data
         */
        private void onRecv(final Context context, final Intent intent) {
            final Bundle bundle = intent.getExtras();
            final String rid = bundle.getString(ILetObserver.Keys.KEY_RID);
            final String state = bundle.getString(ILetObserver.Keys.KEY_STATE, ILetObserver.State.UNKNOWN);

            JobInfo jobInfo = bundle.getParcelable(ILetObserver.Keys.KEY_JOB_INFO);

            SLog.d(TAG, "JobService state: " + state);
            switch (state) {
                case State.CANCEL:
                    onCancel(rid);
                    break;

                case State.COMPLETE:
                    if(jobInfo.getJobType() == JobInfo.JobType.SCAN) {
                        ScanJobData scanJobData = jobInfo.getJobData();
                        Bundle extras = new Bundle();
                        extras.putInt(Scanlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
                        extras.putString(Scanlet.Param.PACKAGE_NAME,  context.getPackageName());
                        extras.putString(Scanlet.Param.KEY_JOB_ID, jobInfo.getJobId());
                        String path = "";
                        try {
                            final ContentResolver resolver = context.getContentResolver();
                            final Bundle returnBundle = resolver
                                    .call(Scanlet.getContentUri(resolver),
                                            Scanlet.Method.GET_FILE_REQ,
                                            null,
                                            extras);
                            if(returnBundle.containsKey(Scanlet.Param.KEY_FILE_URI)) {
                                path = returnBundle.getString(Scanlet.Param.KEY_FILE_URI);
                            }
                        } catch (Exception e) {
                            Log.i(TAG, "Failed to call cr for getting file path");
                        }

                        if(!TextUtils.isEmpty(path)) {
                            List<String> fileNames = new ArrayList<>();
                            for (String filePath : scanJobData.getFileNames()) {
                                FileInputStream is = null;
                                try {
                                    ParcelFileDescriptor fd = context.getContentResolver().openFileDescriptor(Uri.parse(filePath), "rw");
                                    is = new FileInputStream(fd.getFileDescriptor());

                                    String subFile = filePath.substring(filePath.lastIndexOf("/"));
                                    File ffParent = new File(context.getFilesDir(), jobInfo.getJobId());
                                    if(!ffParent.exists()) ffParent.mkdirs();

                                    File ff = new File(ffParent, subFile);
                                    writeStreamToFile(is, ff);
                                    if(ff.exists()) {
                                        fileNames.add(ff.getAbsolutePath());
                                        Log.d(TAG, "Success to move file:" + ff.getName());
                                    }
                                } catch (Exception e) {
                                    Log.i(TAG, "Failed to move file " + e.getMessage());
                                } finally {
                                    if(is != null) try { is.close(); } catch (Exception e) {}
                                }
                            }
                            if(fileNames.size() > 0) {
                                scanJobData.setFileNames(fileNames);
                                try {
                                    final ContentResolver resolver = context.getContentResolver();
                                    extras.putStringArrayList(Scanlet.Param.KEY_FILE_URIS, (ArrayList<String>)fileNames);
                                    final Bundle returnBundle = resolver
                                            .call(Scanlet.getContentUri(resolver),
                                                    Scanlet.Method.PUT_FILE_REQ,
                                                    null,
                                                    extras);
                                } catch (Exception e) {
                                    Log.i(TAG, "Failed to call cr for updating file path");
                                }
                            }
                        }
                    }
                    onComplete(rid, jobInfo);
                    break;

                case State.PROGRESS:
                    onProgress(rid, jobInfo);
                    // Notify JobService about this observer registration
                    final Intent registerIntent = new Intent(Joblet.ACTION_REGISTER);

                    registerIntent.putExtra(Joblet.Keys.KEY_CLIENT_PACKAGE, context.getPackageName());
                    registerIntent.putExtra(ILetObserver.Keys.KEY_RID, rid);
                    registerIntent.setPackage(Sdk.SERVICES_PACKAGE);
                    context.sendBroadcast(registerIntent);
                    break;

                case State.FAIL:
                    onFail(rid, Result.parse(bundle, new Result()));
                    break;

                case State.CREDENTIALS:
                    // for Authentication Credentials dialog the observer must be registered to Activity context
                    if (context instanceof Activity) {
                        final Intent requestIntent = bundle.getParcelable(ILetObserver.Keys.KEY_REQUEST_INTENT);
                        if (requestIntent != null) {
                            final ScanToRequestIntent.IntentParams params = ScanToRequestIntent.getIntentParams(
                                    requestIntent);

                            if (params != null && (params.getTaskAttributes() == null
                                    || params.getTaskAttributes().getShowCredentialsUI())) {

                                CredentialsDialog credentialsDialog = CredentialsDialog.newInstance(
                                        new CredentialsDialog.CredentialsDialogCallback() {
                                            @Override
                                            public void onOkClick(String userName, String password) {
                                                // re-sending the previous intent again

                                                if (ScanToRequestIntent.ACTION.equals(requestIntent.getAction())) {
                                                    ScanToRequestIntent.IntentParams newParams =
                                                            new ScanToRequestIntent.IntentParams(
                                                                    params.getScanAttributes(), params.getTaskAttributes(),
                                                                    params.getReqId(),
                                                                    params.getPackageName(), params.getForcedApi(),
                                                                    params.getApplicationId(),
                                                                    userName, password,
                                                                    params.getApiLevel());

                                                    ScanToRequestIntent newRequestIntent = new ScanToRequestIntent();
                                                    newRequestIntent.putIntentParams(newParams);
                                                    newRequestIntent.setPackage(Sdk.SERVICES_PACKAGE);

                                                    context.sendOrderedBroadcast(newRequestIntent, null);
                                                }
                                            }

                                            @Override
                                            public void ooCancelClick() {
                                                onFail(rid, Result.parse(bundle, new Result()));
                                            }
                                        });

                                credentialsDialog.show(((Activity) context).getFragmentManager(), UCD_FRAGMENT_TAG);
                                break;
                            }
                        }
                    }

                    onFail(rid, Result.parse(bundle, new Result()));

                    break;
                case State.UNKNOWN:
                default:
                    SLog.w(TAG, "State unknown: " + state);
                    break;

            }
        }

        private final class JobletRunnable implements Runnable {
            private final Intent mIntent;
            private final Context mContext;

            public JobletRunnable(final Context context, final Intent intent) {
                mIntent = intent;
                mContext = context;
            }

            @Override
            public void run() {
                AbstractJobletObserver.this.onRecv(mContext, mIntent);
            }
        }

        /**
         * <p>Called to notify the client when a job is completed successfully for getting details</p>
         * @param rid    Request Id received from {@link PrinterService#submit(Context, PrintAttributes, PrintletAttributes)}
         *               or {@link ScannerService#submit(Context, ScanAttributes, ScanletAttributes)}
         *               or {@link CopierService#submit(Context, CopyAttributes, CopyletAttributes)}
         * @param jobInfo JobInfo carrying of the job details
         * @since API 1
         */
        @Override
        public abstract void onComplete(final String rid, final JobInfo jobInfo);

        /**
         * <p>Called to notify the client when the state of a job in progressing for monitoring the change of job state</p>
        *
         * @param rid    Request Id received from {@link PrinterService#submit(Context, PrintAttributes, PrintletAttributes)}
         *               or {@link ScannerService#submit(Context, ScanAttributes, ScanletAttributes)}
         *               or {@link CopierService#submit(Context, CopyAttributes, CopyletAttributes)}
         * @param jobInfo JobInfo carrying of the job details
         * @since API 1
         */
        @Override
        public abstract void onProgress(final String rid, final JobInfo jobInfo);

        /**
         * <p>Called to notify the client with reason and detail when job is finished with error. </p>
         *
         * @param rid    Request Id received from {@link PrinterService#submit(Context, PrintAttributes, PrintletAttributes)}
         *               or {@link ScannerService#submit(Context, ScanAttributes, ScanletAttributes)}
         *               or {@link CopierService#submit(Context, CopyAttributes, CopyletAttributes)}
         * @param result Returns details of the failure.
         * @see Result
         * @since API 1
         */
        @Override
        public abstract void onFail(final String rid, final Result result);

        /**
         * <p>Called to notify the client when job is canceled completely. </p>
         *
         * @param rid    Request Id received from {@link PrinterService#submit(Context, PrintAttributes, PrintletAttributes)}
         *               or {@link ScannerService#submit(Context, ScanAttributes, ScanletAttributes)}
         *               or {@link CopierService#submit(Context, CopyAttributes, CopyletAttributes)}
         * @since API 1
         */
        @Override
        public abstract void onCancel(final String rid);
    }
}
