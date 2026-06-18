// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.printer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.JetAdvantageLink;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.JobService;
import com.hp.jetadvantage.link.api.printer.intent.PrintRequestIntent;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.common.utils.NetworkUtility;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.common.utils.SecurityUtility;

import java.io.File;
import java.util.UUID;

/**
 * <p>
 * PrinterService provides an easy to use interface to initiate printing related
 * built-in tasks. In its simplest form, the printing task can be initiated by
 * simply calling {@link #submit submit(Context, PrintAttributes,
 * PrintletAttributes)}. For more control of attributes or to present its own UI,
 * the {@link PrintletAttributes
 * PrintletAttributes} must be built prior to calling {@link #submit
 * submit(Context, PrintAttributes, PrintletAttributes)}.
 * </p>
 * <p>
 * <p>
 * A call to {@link #submit submit(Context, PrintAttributes,
 * PrintletAttributes)} starts the printing task. The printing process is long
 * running and asynchronous by nature, and details of its progress are
 * communicated to the calling entity via a
 * {@link JobService.AbstractJobletObserver
 * AbstractJobletObserver}. The application must create an object of
 * {@link JobService.AbstractJobletObserver
 * AbstractJobletObserver} and register/unregister it based on the component's
 * life cycle, i.e. Activity or Service.
 * </p>
 * <p>
 * <p>
 * Depending upon the task (see <a href="#SupportedTasks">SupportedTask</a>)
 * being submitted, its completion is communicated via a call to
 * {@link ILetObserver#onComplete(String, JobInfo)
 * ILetObserver.onComplete(String, JobInfo)}
 * </p>
 * <p>
 * <h3>Selecting Attributes</h3>
 * <p>
 * For finer control of the attributes passed to {@link #submit
 * submit(Context, PrintAttributes, PrintletAttributes)}, note that
 * the application must determine the existing capability of the
 * device. These capabilities differ per devices.
 * A call to {@link #getCapabilities getCapabilities(Context, Result)}
 * returns these capabilities to the caller.
 * </p>
 *
 * <h3>Submitting a Job</h3>
 * <p>
 * The following is an excerpt showing how to select attributes and submit a
 * print job:
 * </p>
 *
 * <script src=
 * "https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"
 * ></script>
 *
 * <pre class="prettyprint" style="word-wrap: break-word; white-space: pre-wrap; white-space:-moz-pre-wrap; white-space:-pre-wrap; white-space:-o-pre-wrap; word-break:break-all;">
 * private static final class PrintAsyncTask extends AsyncTask&lt;Void, Void, Void&gt; {
 *
 *  protected Void doInBackground(final Void... params) {
 *      Result result = new Result();
 *      final PrintAttributesCaps caps = PrinterService.getCapabilities(
 *              mContext, result);
 *
 *      if (null == caps) {
 *          // Unable to retrieve capabilities. Need to look at the result to
 *          // see what happened.
 *          return null;
 *      }
 *
 *      try {
 *          final Duplex duplex =
 *              Duplex.valueOf(mPrefs.getString(PrintConfigureFragment.PREF_DUPLEX_MODE, Duplex.DEFAULT.name()));
 *
 *          .....
 *
 *          // Create the print attributes
 *          final PrintAttributes attributes =
 *              new PrintAttributes.PrintFromStorageBuilder(Uri.fromFile(new File(filePath)))
 *                  .setColorMode(cm)
 *                  .setDuplex(duplex)
 *                  .setAutoFit(af)
 *                  .setStapleMode(sm)
 *                  .setPaperSource(psrc)
 *                  .setPaperSize(psz)
 *                  .setDocumentFormat(dfmt)
 *                  .setCopies(copies)
 *                  .build(caps);
 *
 *          // Create the printlet attributes
 *          final PrintletAttributes taskAttribs = new PrintletAttributes.Builder().build();
 *
 *          // Submit the job
 *          PrinterService.submit(mContext, attributes, taskAttribs);
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
 * <li><a href="#PFS">Print from local Storage</a></li>
 * <li><a href="#PFH">Print from HTTP</a></li>
 * <li><a href="#PFM">Print from Stream</a></li>
 * <li><a href="#PFU">Print from Usb</a></li>
 * </ul>
 * <p>
 * <a name="PFS"></a> <h3>Print from local Storage</h3>
 * <p>
 * When executed on the device, this task prints a local
 * file using the given attributes. A local file is one which resides on the
 * local file system of the printer.
 * </p>
 * <p>
 * <a name="PFH"></a> <h3>Print from HTTP</h3>
 * <p>
 * When executed on the device, this task prints a remote
 * file using the given http url.
 * </p>
 * <p>
 * <a name="PFM"></a> <h3>Print from Stream</h3>
 * <p>
 * When executed on the device, this task prints from the given streams.
 * </p>
 * <p>
 * <a name="PFU"></a> <h3>Print from Usb</h3>
 * <p>
 * When executed on the device, this task prints a file from the given Usb url.
 * </p>
 * <p>
 * <h3 name="JobMonitoring">Monitoring the Job</h3> After submitting the job, it
 * can be monitored by using the
 * {@link JobService#monitorJobInForeground
 * monitorJobInForeground(Activity, int, JobletAttributes, Intent)} API and providing
 * the job Id.
 * <p>
 * <h3 name="JobCancelation">Canceling the Job</h3> To cancel a submitted job,
 * you can use
 * {@link JobService#cancelJob
 * cancelJob(Context, int)}
 *
 * @since API 1
 */
public final class PrinterService {
    private static final String TAG = Printlet.TAG;

    private PrinterService() {
    }

    /**
     * <p>Submits a print job asynchronously to the Printer using the provided
     * print attributes. If
     * {@link JobService.AbstractJobletObserver
     * AbstractJobletObserver} is present, the
     * {@link JobService.AbstractJobletObserver
     * AbstractJobletObserver} will receive call backs indicating job
     * progress, completion, and / or errors.</p>
     * <p>
     * <p>This task can accept multiple requests.</p>
     *
     * @param context         The Context object for your activity or application.
     * @param printAttributes
     *            Attributes related to print.
     * @param letAttributes
     *            Attributes related to how the {@link PrinterService} should
     *            behave.
     * @return a unique request ID to track this request
     * @throws NullPointerException     if context or PrintAttributes are null with disabled Show Setting UI
     * @throws IllegalArgumentException
     *             if incompatible attributes are provided
     *             (e.g. {@link PrintAttributes} has Settings UI false and {@link PrintAttributes} are null)
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static String submit(@NonNull final Context context, @Nullable final PrintAttributes printAttributes, @Nullable final PrintletAttributes letAttributes) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        final String rid = UUID.randomUUID().toString();

        //Validation logic when job is requested without settingUI.
        Uri fileUri = null;
        if (letAttributes == null || !letAttributes.mShowSettingsUi) {
            Preconditions.checkNotNull(printAttributes, "PrintAttributes must be provided");

            if (printAttributes.mSource != PrintAttributes.Source.STREAM) {
                Preconditions.checkArgument(printAttributes.mFileUri != null && !Uri.EMPTY.equals(printAttributes.mFileUri),
                        "File Uri should be provided if Settings UI is disabled.");
                Preconditions.checkArgument(printAttributes.mSource == PrintAttributes.Source.HTTP || !"/".equals(printAttributes.mFileUri.getEncodedPath()),
                        "File Uri should be provided if Settings UI is disabled.");
                // Validate provided uri, but don't store received value
                PrintAttributes.validateUri(printAttributes.mSource, printAttributes.mFileUri, printAttributes.mDocumentFormat);

                if(printAttributes.mSource == PrintAttributes.Source.STORAGE) {
                    final File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    if(printAttributes.mFileUri.getPath().startsWith(downloadFolder.getAbsolutePath())) { //For download directory
                    } else {
                        File newFile = new File(printAttributes.mFileUri.getPath());
                        fileUri = com.hp.jetadvantage.link.api.FileProvider.getUriForFile(context.getApplicationContext(), context.getApplicationContext().getPackageName() + ".provider", newFile);

                        context.getApplicationContext().grantUriPermission(Sdk.SERVICES_PACKAGE, fileUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        Log.d(TAG, "Permission is granted to " + fileUri);
                    }
                }
            } else {
                Preconditions.checkNotNull(printAttributes.mPrintInputStream, "Input stream must be provided");

                if (printAttributes.mDocumentFormat == PrintAttributes.DocumentFormat.JPEG) {
                    throw new IllegalArgumentException("DocumentFormat doesn't correspond the file type");
                }

                NetworkUtility.createServerSocket(rid, printAttributes.mPrintInputStream);
            }
        }

        SLog.v(TAG, "submit for " + rid);
        String appId = SecurityUtility.loadApplicationIdFromAssets(context);

        final PrintRequestIntent intent = new PrintRequestIntent();
        PrintRequestIntent.IntentParams params = null;
        if(printAttributes != null && printAttributes.mSource == PrintAttributes.Source.STORAGE && fileUri != null) {
            params = new PrintRequestIntent.IntentParams(
                    printAttributes, letAttributes, rid, context.getPackageName(), null, appId, null, null, Sdk.VERSION.LEVEL, fileUri.toString());
        } else {
            params = new PrintRequestIntent.IntentParams(
                    printAttributes, letAttributes, rid, context.getPackageName(), null, appId, null, null, Sdk.VERSION.LEVEL);
        }
        intent.putIntentParams(params);
        intent.setPackage(Sdk.SERVICES_PACKAGE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        context.sendOrderedBroadcast(intent, null);
        return rid;
    }

    /**
     * <p>This synchronous operation returns the intersection of Printer and
     * PrinterService capabilities. In this example, for the color mode attribute:</p><ul>
     * <li>The color modes supported from the Printer are {mono, auto}</li>
     * <li>The color modes of the {@link PrinterService} are {mono, color}</li><ul>
     * <p>Therefore, the color mode capabilities will include only {mono}.</p>
     * <p/>
     * <i>Note that for some attributes, the intersection of values may result in empty capabilities.</i></p>
     *
     * @param context The Context object for your activity or application.
     * @param result  (optional) Indicates any errors which occurred while
     *                retrieving capabilities.
     * @return Capabilities found both on the Printer and in the PrinterService
     * or null.
     * @throws NullPointerException if context is null
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public static synchronized PrintAttributesCaps getCapabilities(@NonNull final Context context, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        if (result == null) {
            result = new Result();
        }

        try {
            Bundle extras = new Bundle();
            extras.putInt(Printlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

            final ContentResolver resolver = context.getContentResolver();
            final Bundle bundle = resolver.call(Printlet.getContentUri(resolver),
                    Printlet.Method.GET_CAPS, null, extras);

            Result.parse(bundle, result);

            if (result.getCode() == Result.RESULT_OK) {
                final String jsonStr = bundle.getString(Result.KEY_RESULT);
                return JsonParser.getInstance().fromJson(jsonStr, PrintAttributesCaps.class);
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
     * Returns the device default values.
     * If attribute value is DEFAULT, It means it is not possible to get default value from the device.
     * If you want to get Attribute from PrintAttributes, please use PrintAttributesReader.
     *
     * @param context The Context object for your activity or application.
     * @param result  (optional) Indicates any errors which occurred while
     *                retrieving defaults.
     * @return PrinterAttributes containing default values for submitting a job
     * @throws NullPointerException if context is null
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public static synchronized PrintAttributes getDefaults(@NonNull final Context context, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        if (result == null) {
            result = new Result();
        }

        try {
            Bundle extras = new Bundle();
            extras.putInt(Printlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

            final ContentResolver resolver = context.getContentResolver();
            final Bundle bundle = resolver.call(Printlet.getContentUri(resolver),
                    Printlet.Method.GET_DEFAULTS, null, extras);

            Result.parse(bundle, result);

            if (result.getCode() == Result.RESULT_OK) {
                final String jsonStr = bundle.getString(Result.KEY_RESULT);
                return JsonParser.getInstance().fromJson(jsonStr, PrintAttributes.class);
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
     * If it's not supported, PrinterService operation will be failed.</p>
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
        extras.putInt(Printlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        try {
            final ContentResolver resolver = context.getContentResolver();
            final Bundle returnBundle = resolver
                    .call(Printlet.getContentUri(resolver),
                            Printlet.Method.IS_SUPPORTED,
                            null,
                            extras);

            return returnBundle != null
                    && Result.parse(returnBundle, new Result()).getCode() == Result.RESULT_OK
                    && returnBundle.containsKey(Printlet.IS_SUPPORTED_EXTRA) && returnBundle.getBoolean(Printlet.IS_SUPPORTED_EXTRA);
        } catch (SecurityException se){
            throw se;
        } catch (Exception e) {
            SLog.e(TAG, "Failed to get supported status :" + e.getMessage());
        }

        return false;
    }
}
