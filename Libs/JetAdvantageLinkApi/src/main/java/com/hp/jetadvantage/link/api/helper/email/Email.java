// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.helper.email;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.JetAdvantageLink;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;
import com.hp.jetadvantage.link.common.utils.SLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Email provides interfaces for sending email using smtp server
 * which is configured on a device or provided by a client.</p>
 *
 * @since API 1
 */
@DeviceApi
public class Email {
    private static final String TAG = Emaillet.TAG;
    private Email() {}

    /**
     * <p>Request to send an email specified by {@link EmailAttributes} based on device's SMTP settings.
     * Device's SMTP settings is configured through EWS.</p>
     *
     * @param context The Context in which the application is running. If it's null, sending will be failed.
     * @param emailAttributes Attributes for sending email such as the content and recipients
     * @param result  (optional) Indicates any errors which occurred while sending an email.
     * @throws NullPointerException if context or emailAttributes are null.
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public static void send(@NonNull final Context context,
                            @NonNull final EmailAttributes emailAttributes, @Nullable Result result) {
        send(context, emailAttributes, null, null, result);
    }

    /**
     * <p>Sends an email specified by {@link EmailAttributes} using device's SMTP settings or client's SMTP information.</p>
     *
     * @param context The Context in which the application is running. If it's null, sending will be failed.
     * @param emailAttributes attributes defining the content and recipients of the email
     * @param smtpAttributes (optional) settings custom SMTP settings,
     *                       if null the device settings will be used
     * @param result  (optional) Indicates any errors which occurred while sending an email.
     * @throws NullPointerException if context or emailAttributes are null.
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public static void send(@NonNull final Context context,
                            @NonNull final EmailAttributes emailAttributes,
                            @Nullable final SmtpAttributes smtpAttributes,
                            @Nullable Result result) {
        send(context, emailAttributes, smtpAttributes, null, result);
    }

    /**
     * <p>Sends an email specified by {@link EmailAttributes} using SMTP settings defined by
     * {@link SmtpAttributes}</p>
     *
     * @param context The Context in which the application is running. If it's null, sending will be failed.
     * @param emailAttributes attributes defining the content and recipients of the email
     * @param smtpAttributes (optional) settings custom SMTP setting,
     *                       if null the device settings will be used
     * @param proxyAttributes (optional) Proxy information for setting custom proxy,
     *                       if null the no proxy will be used
     * @param result  (optional) Indicates any errors which occurred while sending an email.
     * @throws NullPointerException if context or emailAttributes is null.
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static void send(@NonNull final Context context,
                            @NonNull final EmailAttributes emailAttributes,
                            @Nullable final SmtpAttributes smtpAttributes,
                            @Nullable final ProxyAttributes proxyAttributes,
                            @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);
        Preconditions.checkNotNull(emailAttributes, "EmailAttributes must be provided");

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Emaillet.Param.PACKAGE_NAME, packageName);
        extras.putInt(Emaillet.Param.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);
        extras.putParcelable(Emaillet.Param.EMAIL_ATTRIBUTES, emailAttributes);
        extras.putParcelable(Emaillet.Param.SMTP_ATTRIBUTES, smtpAttributes);
        extras.putParcelable(Emaillet.Param.PROXY_ATTRIBUTES, proxyAttributes);

        if (result == null) {
            result = new Result();
        }


        List<String> fileNames = new ArrayList<String>();
        try {
            List<File> attachments = emailAttributes.getAttachments();
            final File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            for (int inx = 0; attachments != null && inx < attachments.size(); inx++) {
                if (attachments.get(inx).getAbsolutePath().startsWith(downloadFolder.getAbsolutePath())) { //For download directory
                } else {
                    File newFile = new File(attachments.get(inx).getAbsolutePath());
                    Uri fileUri = com.hp.jetadvantage.link.api.FileProvider.getUriForFile(context.getApplicationContext(), context.getApplicationContext().getPackageName() + ".provider", newFile);

                    context.getApplicationContext().grantUriPermission(Sdk.SERVICES_PACKAGE, fileUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    fileNames.add(fileUri.toString());
                    SLog.d(TAG, "Permission is granted to " + fileUri + ", " + fileNames.size());
                }
            }
            if (emailAttributes.getAttachments() != null && fileNames.size() > 0 && fileNames.size() == emailAttributes.getAttachments().size()) {
                extras.putStringArrayList(Emaillet.Param.EMAIL_FILENAMES, (ArrayList<String>)fileNames);
            }

            final Bundle bundle =
                    context.getContentResolver().call(Emaillet.CONTENT_URI, Emaillet.Method.SEND, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Return empty");
            } else {
                Result.parse(bundle, result);
            }
        } catch (SecurityException se){
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, se.getMessage());
            throw se;
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.UNKNOWN, e.getMessage());
        } finally {
            if(fileNames != null && fileNames.size() > 0) {
                for(String fileName: fileNames) {
                    try {
                        context.getApplicationContext().revokeUriPermission(Uri.parse(fileName), Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } catch(Exception e) {}
                }
            }
        }
    }

    /**
     * <p>Returns email parameters configured on device as default through EWS.</p>
     *
     * @param context The Context in which the application is running.
     * @param result  (optional) Indicates any errors which occurred while sending an email.
     * @return EmailAttributes Returned Email settings which is configured on a device.
     * @throws NullPointerException if context is null.
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    public static EmailAttributes getDefaults(@NonNull final Context context, @Nullable Result result) {
        JetAdvantageLink.getInstance().checkPreconditions(context);

        Bundle extras = new Bundle();
        String packageName = context.getPackageName();
        extras.putString(Emaillet.Param.PACKAGE_NAME, packageName);
        extras.putInt(Emaillet.Param.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        if (result == null) {
            result = new Result();
        }

        try {
            final Bundle bundle =
                    context.getContentResolver().call(Emaillet.CONTENT_URI, Emaillet.Method.GET_DEFAULTS, null, extras);

            if (null == bundle) {
                Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "Response is empty");
            } else {
                bundle.setClassLoader(EmailAttributes.class.getClassLoader());

                Result.parse(bundle, result);

                return bundle.getParcelable(Emaillet.Param.EMAIL_ATTRIBUTES);
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
     * If it's not supported, Email operation will be failed.</p>
     *
     * @param context The Context in which the application is running.
     * @return boolean Returns result of supported. If service is supported, returns "TRUE".
     * @throws NullPointerException Returns error if context is null.
     *
     * @since API 1
     */
    @SuppressWarnings({"unused"})
    @SuppressLint("RestrictedApi")
    public static boolean isSupported(@NonNull final Context context) {
        Preconditions.checkNotNull(context, "Context must be provided");

        Bundle extras = new Bundle();
        extras.putInt(Emaillet.Param.KEY_CLIENT_VERSION, Sdk.VERSION.LEVEL);

        final Bundle returnBundle = context.getContentResolver()
                .call(Emaillet.CONTENT_URI,
                        Emaillet.Method.IS_SUPPORTED,
                        null,
                        extras);
        return returnBundle != null
                && Result.parse(returnBundle, new Result()).getCode() == Result.RESULT_OK
                && returnBundle.containsKey(Emaillet.IS_SUPPORTED_EXTRA) && returnBundle.getBoolean(Emaillet.IS_SUPPORTED_EXTRA);
    }
}