// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.copier;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.JetAdvantageLink;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.copier.intent.CopyToRequestIntent;
import com.hp.jetadvantage.link.api.copier.intent.DeleteRequestIntent;
import com.hp.jetadvantage.link.api.copier.intent.ReleaseRequestIntent;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.JobService;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.common.utils.SecurityUtility;

import java.util.List;
import java.util.UUID;

/**
 * <p>
 * CopierService provides an easy to use interface to initiate built-in tasks
 * related to the copier. In its simplest form, a copying task can be
 * initiated by simply calling {@link #submit submit(Context, CopyAttributes,
 * CopyletAttributes)}. For more control of attributes or to present its own UI,
 * {@link CopyletAttributes
 * CopyletAttributes} has to be built prior to calling {@link #submit
 * submit(Context, CopyAttributes, CopyletAttributes)}.
 * </p>
 *
 * <p>
 * A call to {@link #submit submit(Context, CopyAttributes, CopyletAttributes)}
 * starts the copy. The copy is long
 * running and asynchronous by nature, and details of its progress are
 * communicated to the calling entity via a
 * {@link JobService.AbstractJobletObserver
 * AbstractJobletObserver}. The application must create an object of
 * {@link JobService.AbstractJobletObserver
 * AbstractJobletObserver} and register/unregister it based on the component's
 * life cycle, i.e. Activity or Service.
 * </p>
 * <p>
 * Depending upon the task (see <a href="#SupportedTasks">SupportedTask</a>)
 * being submitted, its completion is communicated with a call to
 * {@link ILetObserver#onComplete(String, JobInfo)
 * ILetObserver.onComplete(String, JobInfo)}
 * </p>
 *
 * <h3>Selecting Attributes</h3>
 * <p>
 * For finer control of the attributes passed to {@link #submit
 * submit(Context, CopyAttributes, CopyletAttributes)}, note that
 * the application must determine the existing capability of the
 * device. These capabilities differ per devices.
 * A call to {@link #getCapabilities getCapabilities(Context, Result)}
 * returns these capabilities to the caller.
 * </p>
 *
 * <h3>Submitting a Job</h3>
 * <p>
 * The following is to show how to select attributes and create a
 * copy job:
 * </p>
 *
 * <script src=
 * "https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"
 * ></script>
 *
 * <pre class="prettyprint" style="word-wrap: break-word; white-space: pre-wrap; white-space:-moz-pre-wrap; white-space:-pre-wrap; white-space:-o-pre-wrap; word-break:break-all;">
 * private static final class CopyAsyncTask extends AsyncTask&lt;Void, Void, Void&gt; {
 *
 *  protected Void doInBackground(final Void... params) {
 *      Result result = new Result();
 *      final CopyAttributesCaps caps = CopierService.getCapabilities(context, result);
 *
 *      CopyAttributes attributes = null;
 *      // Obtain Caps to build Copy Attributes
 *      final CopyAttributesCaps caps = activity.getCapabilities();
 *
 *      if (caps == null) {
 *          mErrorMsg = activity.getString(R.string.capabilities_not_loaded);
 *          return null;
 *      }
 *
 *      attributes = buildCopyAttributes(caps);
 *
 *       final CopyletAttributes taskAttribs = new CopyletAttributes.Builder()
 *                                                 .setShowSettingsUi(settingsUi)
 *                                                 .build();
 *
 *      // Submit the job
 *      final String rid = CopierService.submit(activity, attributes, taskAttribs);
 *      Log.i(TAG, "Job submitted with rid = " + rid);
 *   }
 * }
 * </pre>
 *
 * <a name="SupportedTasks"></a> <h3 name="SupportedTasks">Supported Tasks</h3>
 * <ul>
 * <li><a href="#C2N">Normal copy </a></li>
 * <li><a href="#C2S">Copy to store </a></li>
 * </ul>
 *
 * <a name="C2N"></a> <h3>Normal copy</h3>
 * <p>
 * When this task is deployed, the documents are scanned in the local storage and printed from the device.
 * </p>
 *
 * <a name="C2S"></a> <h3>Copy to store</h3>
 * <p>
 * When this task is deployed, the documents are scanned and stored in the internal storage.
 * </p>
 *
 * <h3 name="JobMonitoring">Monitoring the Job</h3> After submitting the job, it
 * can be monitored by using the
 * {@link JobService#monitorJobInForeground
 * monitorJobInForeground(Activity, int, JobletAttributes, Intent)}.
 * <h3 name="JobCancelation">Canceling the Job</h3> To cancel a submitted job,
 * use {@link JobService#cancelJob
 * cancelJob(Context, int)}
 *
 * <p>
 * Copy job supports stored job operation as belows.
 * </p>
 * <ul>
 * <li><a href="#C2R">Release stored copy job </a></li>
 * <li><a href="#C2D">Delete stored copy job </a></li>
 * </ul>
 *
 * <a name="C2R"></a> <h3>Release stored copy job</h3>
 * <p>
 * When this task is deployed, the stored document is printed from the device.
 * </p>
 *
 * <a name="C2D"></a> <h3>Delete stored coyp job</h3>
 * <p>
 * When this task is deployed, the stored document is deleted from the internal storage.
 * </p>
 *
 * @since API 3
 */
public class CopierService {
    private static final String TAG = Copylet.TAG;

    private CopierService() {
    }

    /**
     * <p>Submits a copy job asynchronously to the Printer using the provided copy
     * attributes. If
     * {@link JobService.AbstractJobletObserver
     * AbstractJobletObserver} is present, the
     * {@link JobService.AbstractJobletObserver
     * AbstractJobletObserver} will receive call backs indicating job
     * progress, completion, and / or errors.</p>
     * </p>
     *
     * @param context        The Context object for your activity or application.
     * @param copyAttributes
     *            Attributes related to copy.
     * @param letAttributes
     *            Attributes related to how the {@link CopierService} should
     *            behave.
     * @return a unique request ID to track this request
     *
     * @throws NullPointerException     if context or CopyAttributes are null with disabled Show Setting UI
     * @throws IllegalArgumentException
     *             if incompatible attributes are provided
     *             (e.g. {@link CopyAttributes} has Settings UI false and {@link CopyAttributes} are null)
     *
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static String submit(@NonNull final Context context, final CopyAttributes copyAttributes, final CopyletAttributes letAttributes) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        if (letAttributes == null || !letAttributes.mShowSettingsUi) {
            Preconditions.checkNotNull(copyAttributes, "CopyAttributes must be provided");
        }

        final String rid = UUID.randomUUID().toString();

        String appId = SecurityUtility.loadApplicationIdFromAssets(context);

        final CopyToRequestIntent intent = new CopyToRequestIntent();
        final CopyToRequestIntent.IntentParams params =
                new CopyToRequestIntent.IntentParams(copyAttributes, letAttributes, rid, context.getPackageName(), appId,
                        null, null, Sdk.VERSION.LEVEL);
        intent.putIntentParams(params);
        intent.setPackage(Sdk.SERVICES_PACKAGE);

        context.sendOrderedBroadcast(intent, null);
        return rid;
    }

    /**
     * <p>
     * This synchronous operation returns copy capabilities.
     * </p>
     *
     * @param context
     *            The Context object for your activity or application.
     * @param result
     *            (optional) Indicates any errors which occurred while
     *            retrieving capabilities.
     * @return Capabilities found both on the Printer and in the CopierService or null.
     *
     * @throws NullPointerException if context is null.
     *
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    public static synchronized CopyAttributesCaps getCapabilities(@NonNull final Context context,
            @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        if (result == null) {
            result = new Result();
        }

        try {
            final ContentResolver resolver = context.getContentResolver();
            Bundle extras = new Bundle();
            extras.putInt(Copylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
            final Bundle bundle = resolver.call(Copylet.getContentUri(resolver),
                    Copylet.Method.GET_CAPS, null, extras);

            Result.parse(bundle, result);

            if (result.getCode() == Result.RESULT_OK) {
                final String jsonStr = bundle.getString(Result.KEY_RESULT);
                return JsonParser.getInstance().fromJson(jsonStr, CopyAttributesCaps.class);
            }
        } catch (SecurityException se){
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, se.getMessage());
            throw se;
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.UNKNOWN, e.getMessage());
            SLog.e(TAG, "Failed to deserialize", e);
        }

        return null;
    }

    /**
     * Returns the device default values.
     * If attribute value is DEFAULT, It means it is not possible to get default value from the device.
     * If you want to get Attribute from CopyAttributes, please use CopyAttributesReader.
     *
     * @param context The Context object for your activity or application.
     * @param result  (optional) Indicates any errors which occurred while
     *                retrieving defaults.
     * @return CopyAttributes containing default values for submitting a job
     * @throws NullPointerException if context is null.
     *
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    public static synchronized CopyAttributes getDefaults(@NonNull final Context context, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        if (result == null) {
            result = new Result();
        }

        try {
            final ContentResolver resolver = context.getContentResolver();
            Bundle extras = new Bundle();
            extras.putInt(Copylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
            final Bundle bundle = resolver.call(Copylet.getContentUri(resolver),
                    Copylet.Method.GET_DEFAULTS, null, extras);

            Result.parse(bundle, result);

            if (result.getCode() == Result.RESULT_OK) {
                final String jsonStr = bundle.getString(Result.KEY_RESULT);
                return JsonParser.getInstance().fromJson(jsonStr, CopyAttributes.class);
            }
        } catch (SecurityException se){
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, se.getMessage());
            throw se;
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.UNKNOWN, e.getMessage());
            SLog.e(TAG, "Failed to deserialize", e);
        }
        return null;
    }

    /**
     * <p>
     * Submits a release copy job asynchronously to the Printer using the provided stored job
     * attributes. If {@link JobService.AbstractJobletObserver AbstractJobletObserver} is present,
     * the {@link JobService.AbstractJobletObserver AbstractJobletObserver} will receive
     * call backs indicating job
     * progress, completion, and / or errors.</p>
     * </p>
     *
     * @param context
     *            The Context object for your activity or application.
     * @param storedJobAttributes
     *            Attributes related to stored job.
     * @return Request id which is used to correspond {@link JobService.AbstractJobletObserver
     *         AbstractJobletObserver} callbacks with the original submit.
     *
     * @throws NullPointerException
     *             if context or storedJobAttributes is null
     *
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static String releaseStoredJob(@NonNull final Context context, @NonNull final StoredJobAttributes storedJobAttributes) {
        JetAdvantageLink.getInstance().checkPreconditions(context);
        Preconditions.checkNotNull(storedJobAttributes, "StoredJobAttributes must be provided");

        final String rid = UUID.randomUUID().toString();

        String appId = SecurityUtility.loadApplicationIdFromAssets(context);

        final ReleaseRequestIntent intent = new ReleaseRequestIntent();
        final ReleaseRequestIntent.IntentParams params =
                new ReleaseRequestIntent.IntentParams(storedJobAttributes, rid, context.getPackageName(), appId,
                        null, null, Sdk.VERSION.LEVEL);
        intent.putIntentParams(params);
        intent.setPackage(Sdk.SERVICES_PACKAGE);

        context.sendOrderedBroadcast(intent, null);
        return rid;
    }

    /**
     * <p>
     * Deletes a stored job with the specified job ID.
     * </p>
     *
     * @param context
     *            The Context object for your activity or application.
     * @param jobId
     *            Stored job id to delete
     * @param storedJobCredentials
     *            Password used while to storing job
     * @param result  (optional) Indicates any errors which occurred while
     *                deleting stored job.
     *
     * @throws NullPointerException
     *             if context or jobId is null
     *
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static void deleteStoredJob(@NonNull final Context context, @NonNull final String jobId,
            @Nullable JobCredentialsAttributes storedJobCredentials, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);
        Preconditions.checkNotNull(jobId, "Stored job ID must be provided");

        if (result == null) {
            result = new Result();
        }

        try {
            final String rid = UUID.randomUUID().toString();

            String appId = SecurityUtility.loadApplicationIdFromAssets(context);

            final DeleteRequestIntent intent = new DeleteRequestIntent();
            final DeleteRequestIntent.IntentParams params =
                    new DeleteRequestIntent.IntentParams(storedJobCredentials, rid, context.getPackageName(), appId,
                            null, null, Sdk.VERSION.LEVEL);
            intent.putIntentParams(params);
            intent.setPackage(Sdk.SERVICES_PACKAGE);

            Bundle extras = new Bundle();
            extras.putInt(Copylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
            extras.putString(Copylet.Keys.KEY_STORED_JOB_ID, jobId);
            extras.putParcelable(Copylet.Keys.KEY_DELETE_REQ, intent);

            final ContentResolver resolver = context.getContentResolver();
            final Bundle bundle = resolver
                    .call(Copylet.getContentUri(resolver),
                            Copylet.Method.DELETE_JOB,
                            null,
                            extras);

            Result.parse(bundle, result);
        } catch (SecurityException se){
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, se.getMessage());
            throw se;
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.UNKNOWN, e.getMessage());
            SLog.e(TAG, "Failed to deserialize", e);
        }
    }

    /**
     * <p>
     * Returns list of stored jobs to release or delete it
     * </p>
     *
     * @param context
     *            The Context object for your activity or application.
     * @param result  (optional) Indicates any errors which occurred while
     *                enumerating stored job.
     * @return List of storedJobAttributes
     *
     * @throws NullPointerException
     *             if context or storedJobAttributes is null
     *
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    public static List<StoredJobInfo> enumerateStoredJob(@NonNull final Context context, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        if (result == null) {
            result = new Result();
        }

        try {
            final ContentResolver resolver = context.getContentResolver();
            Bundle extras = new Bundle();
            extras.putInt(Copylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
            final Bundle bundle = resolver.call(Copylet.getContentUri(resolver),
                    Copylet.Method.ENUMERATE_JOBS, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                bundle.setClassLoader(StoredJobInfo.class.getClassLoader());
                Result.parse(bundle, result);

                if (result.getCode() == Result.RESULT_OK && bundle.containsKey(Result.KEY_RESULT)) {
                    return bundle.getParcelableArrayList(Result.KEY_RESULT);
                }
            }
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }

        return null;
    }

    /**
     * <p>This method is needed to determine if the service supported or not.
     * If it's not supported, CopierService operation will be failed.</p>
     *
     * @param context The Context in which the application is running.
     * @return boolean Returns result of supported. If service is supported, method returns "TRUE".
     * @throws NullPointerException Returns error if context is null.
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    public static boolean isSupported(final Context context) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        Bundle extras = new Bundle();
        extras.putInt(Copylet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        try {
            final ContentResolver resolver = context.getContentResolver();
            final Bundle returnBundle = resolver
                    .call(Copylet.getContentUri(resolver),
                            Copylet.Method.IS_SUPPORTED,
                            null,
                            extras);

            return returnBundle != null
                    && Result.parse(returnBundle, new Result()).getCode() == Result.RESULT_OK
                    && returnBundle.containsKey(Copylet.IS_SUPPORTED_EXTRA) && returnBundle.getBoolean(Copylet.IS_SUPPORTED_EXTRA);
        } catch (SecurityException se){
            throw se;
        } catch (Exception e) {
            SLog.e(TAG, "Failed to get supported status", e);
        }

        return false;
    }
}