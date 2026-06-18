// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.scanner;

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
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.JobService;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.ColorMode;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.DocumentFormat;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.TransmissionMode;
import com.hp.jetadvantage.link.api.scanner.intent.ScanToRequestIntent;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.utils.CommonUtility;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.common.utils.SecurityUtility;

import java.util.UUID;

/**
 * <p>
 * ScannerService provides an easy to use interface to initiate built-in tasks
 * related to the scanner. In its simplest form, a scanning task can be
 * initiated by simply calling {@link #submit submit(Context, ScanAttributes,
 * ScanletAttributes)}. For more control of attributes or to present its own UI,
 * {@link ScanletAttributes
 * ScanletAttributes} has to be built prior to calling {@link #submit
 * submit(Context, ScanAttributes, ScanletAttributes)}.
 * </p>
 *
 * <p>
 * A call to {@link #submit submit(Context, ScanAttributes, ScanletAttributes)}
 * starts the scanning task. The scanning process is long
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
 * submit(Context, ScanAttributes, ScanletAttributes)}, note that
 * the application must determine the existing capability of the
 * device. These capabilities differ per devices.
 * A call to {@link #getCapabilities getCapabilities(Context, Result)}
 * returns these capabilities to the caller.
 * </p>
 * 
 * <h3>Submitting a Job</h3>
 * <p>
 * The following is to show how to select attributes and create a
 * scan job:
 * </p>
 * 
 * <script src=
 * "https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"
 * ></script>
 * 
 * <pre class="prettyprint" style="word-wrap: break-word; white-space: pre-wrap; white-space:-moz-pre-wrap; white-space:-pre-wrap; white-space:-o-pre-wrap; word-break:break-all;">
 * private static final class ScanAsyncTask extends AsyncTask&lt;Void, Void, Void&gt; {
 * 
 *  protected Void doInBackground(final Void... params) {
 *      Result result = new Result();
 *      final ScanAttributesCaps caps = ScannerService.getCapabilities(
 *              mContext, result);
 * 
 *      if (null == caps) {
 *          // Unable to retrieve capabilities. Need to look at the result to
 *          // see what happened.
 *          return null;
 *      }
 * 
 *      try {
 *          final ScanAttributes.MeBuilder meBuilder = new ScanAttributes.MeBuilder();
 * 
 *          // Scan in color, if available. Otherwise, use the default.
 *          for (final ColorMode cm : caps.getColorModeList()) {
 *              if (ColorMode.COLOR.equals(cm)) {
 *                  meBuilder.setColorMode(cm);
 *              }
 *          }
 * 
 *          // Create the scan attributes
 *          final ScanAttributes scanAttributes = meBuilder.build(caps);
 * 
 *          // Create the scanlet attributes
 *          final ScanletAttributes scanletAttributes = new ScanletAttributes.Builder()
 *                  .build();
 * 
 *          ScannerService.submit(mContext, scanAttributes, scanletAttributes);
 * 
 *      } catch (final CapabilitiesExceededException cee) {
 *          // Either an attribute was outside of the capabilities or the
 *          // capabilities were empty.
 *      }
 *   }
 * }
 * </pre>
 *
 * <a name="SupportedTasks"></a> <h3 name="SupportedTasks">Supported Tasks</h3>
 * <ul>
 * <li><a href="#S2M">Scan To Me </a></li>
 * <li><a href="#S2H">Scan To HTTP </a></li>
 * <li><a href="#S2N">Scan To NetworkFolder </a></li>
 * <li><a href="#S2F">Scan To FTP </a></li>
 * <li><a href="#S2E">Scan To Email </a></li>
 * <li><a href="#S2M">Scan To Usb </a></li>
 * </ul>
 * 
 * <a name="S2M"></a> <h3>Scan To Me</h3>
 * <p>
 * When this task is deployed, the documents are scanned and saved in the local storage on the
 * device, where other apps can access them.
 * </p>
 *
 * <a name="S2H"></a> <h3>Scan To HTTP</h3>
 * <p>
 * When this task is deployed, the documents are scanned and sent to the HTTP server which configured by an application.
 * </p>
 *
 * <a name="S2N"></a> <h3>Scan To NetworkFolder</h3>
 * <p>
 * When this task is deployed, the documents are scanned and saved in the shared folder on network.
 * </p>
 *
 * <a name="S2M"></a> <h3>Scan To FTP</h3>
 * <p>
 * When this task is deployed, the documents are scanned and sent to the FTP server which configured by an application.
 * </p>
 *
 * <a name="S2E"></a> <h3>Scan To Email</h3>
 * <p>
 * When this task is deployed, the documents are scanned and sent to email as attachments based on SMTP configuration.
 * </p>
 *
 * <a name="S2M"></a> <h3>Scan To Usb</h3>
 * <p>
 * When this task is deployed, the documents are scanned and sent to Usb which selected by a user.
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
 * 
 * @since API 1
 */
public final class ScannerService {
    private static final String TAG = Scanlet.TAG;

    private ScannerService() {
    }

    /**
     * <p>
     * Creates and requests a scan job asynchronously to the device using the provided scan
     * attributes. If
     * {@link JobService.AbstractJobletObserver
     * AbstractJobletObserver} is present, the
     * {@link JobService.AbstractJobletObserver
     * AbstractJobletObserver} will receive call backs indicating job
     * progress, completion, and / or errors.</p>
     * </p>
     * 
     * @param context
     *            The Context object for your activity or application.
     * @param scanAttributes
     *            Attributes related to scan.
     * @param letAttributes
     *            Attributes related to how the {@link ScannerService} should
     *            behave.
     * @return Request id which is used to correspond
     *         {@link JobService.AbstractJobletObserver
     *         AbstractJobletObserver} callbacks with the original submit.
     * 
     * @throws NullPointerException
     *             if context or ScanAttributes are null with disabled Show Setting UI
     * @throws IllegalArgumentException
     *             if incompatible attributes are provided
     *             (e.g. {@link ScanAttributes} has Settings UI false and {@link ScanAttributes} are null)
     * 
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static String submit(@NonNull final Context context, @Nullable final ScanAttributes scanAttributes,
                                @Nullable final ScanletAttributes letAttributes) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        //Validation logic when job is requested without settingUI.
        if (letAttributes == null || !letAttributes.mShowSettingsUi) {
            Preconditions.checkNotNull(scanAttributes, "ScanAttributes must be provided");

            if (scanAttributes.mDestination != ScanAttributes.Destination.ME
                    && scanAttributes.mDestination != ScanAttributes.Destination.EMAIL
                    && scanAttributes.mDestination != ScanAttributes.Destination.USB) {
                Preconditions.checkNotNull(scanAttributes.mUri, "Destination uri is empty");
                Preconditions.checkArgument(!scanAttributes.mUri.toString().isEmpty(), "Destination uri is empty");

                Preconditions.checkArgument(scanAttributes.mDestination != ScanAttributes.Destination.HTTP
                                || ("http".equals(scanAttributes.mUri.getScheme()) || "https".equals(scanAttributes.mUri.getScheme())),
                        "Destination uri must be HTTP or HTTPS url");

                Preconditions.checkArgument(scanAttributes.mDestination != ScanAttributes.Destination.FTP
                                || "ftp".equals(scanAttributes.mUri.getScheme()),
                        "Destination uri must be FTP url");

                Preconditions.checkArgument(scanAttributes.mDestination != ScanAttributes.Destination.NETWORK_FOLDER
                                || scanAttributes.mUri.toString().startsWith("file:////")
                                || CommonUtility.isValidUNC(scanAttributes.mUri),
                        "Destination uri must be FILE uri or a valid UNC");

                ScanAttributes.validateUri(scanAttributes.mUri);
            }
        }

        final String rid = UUID.randomUUID().toString();

        String appId = SecurityUtility.loadApplicationIdFromAssets(context);

        final ScanToRequestIntent intent = new ScanToRequestIntent();
        final ScanToRequestIntent.IntentParams params =
                new ScanToRequestIntent.IntentParams(scanAttributes, letAttributes, rid, context.getPackageName(), null, appId,
                        scanAttributes != null ? scanAttributes.mCredentialsUsername : null,
                        scanAttributes != null ? scanAttributes.mCredentialsPassword : null,
                        Sdk.VERSION.LEVEL);
        intent.putIntentParams(params);
        intent.setPackage(Sdk.SERVICES_PACKAGE);
        context.sendOrderedBroadcast(intent, null);
        return rid;
    }

    /**
     * <p>
     * This synchronous operation returns the intersection of device and
     * ScannerService capabilities. In this example, for the color mode
     * attribute:
     * </p>
     * <ul>
     * <li>The color modes supported from the device are {mono, auto}</li>
     * <li>The color modes of the {@link ScannerService} are {mono, color}</li>
     * </ul>
     * <p>
     * Therefore, the color mode capabilities will include only {mono}.
     * </p>
     * <p>
     * <i>Note that for some attributes, the intersection of values may result
     * in empty capabilities.</i>
     * </p>
     * 
     * @param context
     *            The Context object for your activity or application.
     * @param result
     *            (optional) Indicates any errors which occurred while
     *            retrieving capabilities.
     * @return Capabilities found both on the Printer and in the ScannerService or null.
     * @throws NullPointerException If context is null.
     * 
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public static synchronized ScanAttributesCaps getCapabilities(@NonNull final Context context, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        if (result == null){
            result = new Result();
        }

        try {
            final ContentResolver resolver = context.getContentResolver();
            Bundle extras = new Bundle();
            extras.putInt(Scanlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
            final Bundle bundle = resolver.call(Scanlet.getContentUri(resolver),
                    Scanlet.Method.GET_CAPS, null, extras);

            Result.parse(bundle, result);

            if (result.getCode() == Result.RESULT_OK) {
                final String jsonStr = bundle.getString(Result.KEY_RESULT);
                return JsonParser.getInstance().fromJson(jsonStr, ScanAttributesCaps.class);
            }
        } catch (SecurityException se){
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, se.getMessage());
            throw se;
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.UNKNOWN, e.getMessage());
        }

        return null;
    }

    /**
     * <p>
     * This synchronous operation returns scan capabilities for particular Transmission Mode.
     * Capabilities can vary for {@link TransmissionMode#IMAGE} and {@link TransmissionMode#JOB}.
     * E.g. for {@link TransmissionMode#IMAGE} only limited {@link DocumentFormat} can be supported.
     * </p>
     *
     * @param context
     *            The Context object for your activity or application.
     * @param transmissionMode
     *            Transmission mode to get capabilities for
     * @param result
     *            (optional) Indicates any errors which occurred while
     *            retrieving capabilities.
     * @return Capabilities found both on the Printer and in the ScannerService or null.
     * @throws NullPointerException if context or transmissionMode is null.
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static synchronized ScanAttributesCaps getCapabilities(@NonNull final Context context,
                                                                  @NonNull final TransmissionMode transmissionMode,
                                                                  @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);
        Preconditions.checkNotNull(transmissionMode, "TransmissionMode parameter must be provided.");

        if (result == null) {
            result = new Result();
        }

        try {
            final ContentResolver resolver = context.getContentResolver();
            Bundle extras = new Bundle();
            extras.putInt(Scanlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
            extras.putSerializable(Scanlet.Keys.KEY_TRANSMISSION_MODE, transmissionMode);
            final Bundle bundle = resolver.call(Scanlet.getContentUri(resolver),
                    Scanlet.Method.GET_CAPS, null, extras);

            Result.parse(bundle, result);

            if (result.getCode() == Result.RESULT_OK) {
                final String jsonStr = bundle.getString(Result.KEY_RESULT);
                return JsonParser.getInstance().fromJson(jsonStr, ScanAttributesCaps.class);
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
     * Return the device default values.
     * If attribute value is DEFAULT, It means it is not possible to get default value from the device.
     * For taking attribute from ScanAttributes, application can read the data through ScanAttributesReader.
     *
     * @param context The Context object for your activity or application.
     * @param result  (optional) Indicates any errors which occurred while
     *                retrieving defaults.
     * @return ScanAttributes containing default values for submitting a job
     * @throws NullPointerException if context is null.
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public static synchronized ScanAttributes getDefaults(@NonNull final Context context, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        if (result == null) {
            result = new Result();
        }

        try {
            final ContentResolver resolver = context.getContentResolver();
            Bundle extras = new Bundle();
            extras.putInt(Scanlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
            final Bundle bundle = resolver.call(Scanlet.getContentUri(resolver),
                    Scanlet.Method.GET_DEFAULTS, null, extras);

            Result.parse(bundle, result);

            if (result.getCode() == Result.RESULT_OK) {
                final String jsonStr = bundle.getString(Result.KEY_RESULT);
                return JsonParser.getInstance().fromJson(jsonStr, ScanAttributes.class);
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
     * This synchronous operation returns file options capabilities for particular pair of Color Mode and Document Format.
     * </p>
     *
     * @param context
     *            The Context object for your activity or application.
     * @param colorMode
     *            ColorMode to get capabilities for
     * @param documentFormat
     *            DocumentFormat to get capabilities for
     * @param result
     *            (optional) Indicates any errors which occurred while
     *            retrieving capabilities.
     * @return FileOptionsAttributesCaps file options capabilities
     * @throws NullPointerException if context, colorMode or documentFormat are null.
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static synchronized FileOptionsAttributesCaps getFileOptionsCapabilities(@NonNull final Context context,
            @NonNull final ColorMode colorMode, @NonNull final DocumentFormat documentFormat, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);
        Preconditions.checkNotNull(colorMode, "ColorMode parameter must be provided.");
        Preconditions.checkNotNull(documentFormat, "DocumentFormat parameter must be provided.");

        if (result == null) {
            result = new Result();
        }

        try {
            final ContentResolver resolver = context.getContentResolver();
            Bundle extras = new Bundle();
            extras.putInt(Scanlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
            extras.putSerializable(Scanlet.Keys.KEY_COLOR_MODE, colorMode);
            extras.putSerializable(Scanlet.Keys.KEY_DOCUMENT_FORMAT, documentFormat);
            final Bundle bundle = resolver.call(Scanlet.getContentUri(resolver),
                Scanlet.Method.GET_FILE_OPTIONS_CAPS, null, extras);

            Result.parse(bundle, result);

            if (result.getCode() == Result.RESULT_OK) {
                final String jsonStr = bundle.getString(Result.KEY_RESULT);
                return JsonParser.getInstance().fromJson(jsonStr, FileOptionsAttributesCaps.class);
            }
        } catch (SecurityException se){
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, se.getMessage());
            throw se;
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.UNKNOWN, e.getMessage());
        }

        return null;
    }

    /**
     * <p>This method is needed to determine if the service supported or not.
     * If it's not supported, ScannerService operation will be failed.</p>
     *
     * @param context The Context in which the application is running.
     * @return boolean Returns result of supported. If service is supported, method returns "TRUE".
     * @throws NullPointerException Returns error if context is null.
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public static boolean isSupported(@NonNull final Context context) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        Bundle extras = new Bundle();
        extras.putInt(Scanlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        try {
            final ContentResolver resolver = context.getContentResolver();
            final Bundle returnBundle = resolver
                    .call(Scanlet.getContentUri(resolver),
                            Scanlet.Method.IS_SUPPORTED,
                            null,
                            extras);

            return returnBundle != null
                    && Result.parse(returnBundle, new Result()).getCode() == Result.RESULT_OK
                    && returnBundle.containsKey(Scanlet.IS_SUPPORTED_EXTRA) && returnBundle.getBoolean(Scanlet.IS_SUPPORTED_EXTRA);
        } catch (SecurityException se){
            throw se;
        } catch (Exception e) {
            SLog.e(TAG, "Failed to get supported status :" + e.getMessage());
        }

        return false;
    }
}
