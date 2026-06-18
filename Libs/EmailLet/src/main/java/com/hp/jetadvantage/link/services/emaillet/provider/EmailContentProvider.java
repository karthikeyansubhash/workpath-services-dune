// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.emaillet.provider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.helper.email.EmailAttributes;
import com.hp.jetadvantage.link.api.helper.email.Emaillet;
import com.hp.jetadvantage.link.api.helper.email.ProxyAttributes;
import com.hp.jetadvantage.link.api.helper.email.SmtpAttributes;
import com.hp.jetadvantage.link.common.Platform;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.common.exception.SdkConnectionErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkInvalidParamException;
import com.hp.jetadvantage.link.services.common.exception.SdkNotSupportedException;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.emaillet.util.EmailLetUtils;

import com.hp.jetadvantage.link.device.services.standard.StandardDeviceEmailService;
import com.hp.jetadvantage.link.device.services.types.EmailSettingsData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.UUID;

public class EmailContentProvider extends ContentProvider {
    private static final String TAG = Emaillet.TAG;

    private static final UriMatcher S_URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.sec.Emaillet";

    private static final int EMAIL_LET_CODE = 1;

    static {
        S_URI_MATCHER.addURI(Emaillet.AUTHORITY, Emaillet.DIR_PATH_SEGMENT, EMAIL_LET_CODE);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        final Bundle bundle = new Bundle();
        Result.pack(bundle, Result.RESULT_OK);

        StrictMode.ThreadPolicy originalPolicy = StrictMode.getThreadPolicy();
        try {
            if (getContext() == null) {
                throw new SdkInvalidParamException("Context is null");
            }

            if (extras == null) {
                throw new SdkInvalidParamException("No parameters provided");
            }

            SLog.v(TAG, "call " + method);
            SpsPermissionHelper.ensurePermission(getContext());

            final PrinterInfo pi = SelectedPrinterHelper.get(getContext().getContentResolver());

            if (PrinterInfo.isEmpty(pi)) {
                SLog.e(TAG, "Device is not connected");
                throw new SdkConnectionErrorException("Device is not connected");
            }

            final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
            StrictMode.setThreadPolicy(policy);

            boolean serviceSupported = isSupported(pi);

            if (Emaillet.Method.IS_SUPPORTED.equals(method)) {
                bundle.putBoolean(Emaillet.IS_SUPPORTED_EXTRA, serviceSupported);
            } else {
                if (!serviceSupported) {
                    throw new SdkNotSupportedException("Email Helper is not supported");
                }

                extras.setClassLoader(EmailAttributes.class.getClassLoader());

                String pkgName = extras.getString(Emaillet.Param.PACKAGE_NAME);
                if (TextUtils.isEmpty(pkgName)) {
                    throw new SdkInvalidParamException("Package name is empty");
                }
                SLog.v(TAG, "method: " + method + " pkgName: " + pkgName);

                switch (method) {
                    case Emaillet.Method.SEND:
                        EmailAttributes emailAttributes = extras.getParcelable(Emaillet.Param.EMAIL_ATTRIBUTES);
                        SmtpAttributes smtpAttributes = extras.getParcelable(Emaillet.Param.SMTP_ATTRIBUTES);
                        ProxyAttributes proxyAttributes = extras.getParcelable(Emaillet.Param.PROXY_ATTRIBUTES);

                        String EMAIL_FILENAMES = "emailFileNames";
                        if(extras.containsKey(EMAIL_FILENAMES)) {
                            return send(bundle, emailAttributes, smtpAttributes, proxyAttributes, extras.getStringArrayList(EMAIL_FILENAMES));
                        } else {
                            return send(bundle, emailAttributes, smtpAttributes, proxyAttributes);
                        }
                    case Emaillet.Method.GET_DEFAULTS:
                        return getDefaults(bundle);

                    default:
                        throw new SdkInvalidParamException("Method " + method + " is not supported");
                }
            }
        } catch (SdkException e) {
            Result.pack(bundle, e.getResult());
        } catch (Exception e) {
            SLog.e(TAG, "Failed to send email", e);
            Result.pack(bundle, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        } finally {
            if (originalPolicy != null) {
                StrictMode.setThreadPolicy(originalPolicy);
            }
        }

        return bundle;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (S_URI_MATCHER.match(uri)) {
            case EMAIL_LET_CODE:
                SLog.d(TAG, " in EMAIL_LET_CODE ");
                return CONTENT_TYPE;

            default:
                throw new IllegalArgumentException("Unknown or Invalid URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private boolean isSupported(final PrinterInfo pi) {
        return Platform.isPanel();
    }

    private Bundle send(Bundle bundle, EmailAttributes emailAttributes, SmtpAttributes smtpAttributes, ProxyAttributes proxyAttributes, List<String> attachedFileNames) throws Exception {
        StandardDeviceEmailService deviceEmailService = new StandardDeviceEmailService();
        EmailSettingsData emailSettingsData = deviceEmailService.getEmailSettings();

        FileInputStream is = null;
        String path = null;
        if(attachedFileNames != null && attachedFileNames.size() > 0 && attachedFileNames.size() == emailAttributes.getAttachments().size()) {
            final String TEMP_FOLDER = ".tmp";
            File ffParent = new File(getContext().getFilesDir().getAbsolutePath() + "/" + TEMP_FOLDER, UUID.randomUUID().toString());
            if (!ffParent.exists()) ffParent.mkdirs();

            for (int inx = 0; inx < attachedFileNames.size(); inx++) {
                try {
                    String extraUri = attachedFileNames.get(inx);
                    Uri uri = Uri.parse(extraUri);
                    SLog.d(TAG, "FileUri is " + uri.toString()); //TODO
                    ParcelFileDescriptor fd = getContext().getContentResolver().openFileDescriptor(uri, "rw");
                    is = new FileInputStream(fd.getFileDescriptor());

                    String subFile = extraUri.substring(extraUri.lastIndexOf("/"));
                    File ff = new File(ffParent, subFile);
                    writeStreamToFile(is, ff);
                    if (ff.exists()) {
                        Log.d(TAG, "Success to move file " + ff.getName());
                        path = ff.getAbsolutePath();
                        attachedFileNames.set(inx, path);
                    }
                } catch (Exception e) {
                    Log.i(TAG, "Failed to move file: " + e.getMessage());
                    throw e;
                } finally {
                    if (is != null) try {
                        is.close();
                    } catch (Exception e) {
                    }
                    path = null;
                }
            }
            EmailLetUtils.send(emailSettingsData, smtpAttributes, emailAttributes, proxyAttributes, attachedFileNames);
        } else {
            EmailLetUtils.send(emailSettingsData, smtpAttributes, emailAttributes, proxyAttributes);
        }

        return bundle;
    }

    private Bundle send(Bundle bundle, EmailAttributes emailAttributes, SmtpAttributes smtpAttributes, ProxyAttributes proxyAttributes) throws Exception {
        StandardDeviceEmailService deviceEmailService = new StandardDeviceEmailService();
        EmailSettingsData emailSettingsData = deviceEmailService.getEmailSettings();
        EmailLetUtils.send(emailSettingsData, smtpAttributes, emailAttributes, proxyAttributes);

        return bundle;
    }

    private Bundle getDefaults(final Bundle bundle) throws Exception {
        StandardDeviceEmailService deviceEmailService = new StandardDeviceEmailService();
        EmailSettingsData emailSettingsData = deviceEmailService.getEmailSettings();
        if (emailSettingsData != null) {
            EmailAttributes.Builder builder = new EmailAttributes.Builder();
            if (emailSettingsData.getDefaultFrom() != null) {
                if (emailSettingsData.getDefaultFrom().getEmailAddress() != null) {
                    builder.setFrom(emailSettingsData.getDefaultFrom().getEmailAddress(),
                            TextUtils.isEmpty(emailSettingsData.getDefaultFrom().getDisplayName())?"":emailSettingsData.getDefaultFrom().getDisplayName());
                } else if (emailSettingsData.getDefaultFrom().getDefaultEmailAddress() != null) { //For compatibility
                    builder.setFrom(emailSettingsData.getDefaultFrom().getDefaultEmailAddress(),
                            TextUtils.isEmpty(emailSettingsData.getDefaultFrom().getDisplayName())?"":emailSettingsData.getDefaultFrom().getDisplayName());
                }
            }

            builder.setMessage(emailSettingsData.getDefaultMessage());

            bundle.putParcelable(Emaillet.Param.EMAIL_ATTRIBUTES, builder.build());

            return bundle;
        }

        throw new SdkServiceErrorException("Failed to get default email settings from device");
    }

    private void writeStreamToFile(FileInputStream input, File file) throws Exception {
        FileChannel src = null;
        FileChannel dst = null;
        try {
            src = input.getChannel();
            dst = new FileOutputStream(file).getChannel();
            dst.transferFrom(src, 0, src.size());
        } catch (FileNotFoundException fne) {
            SLog.i(TAG, "[FILE]Not found: " + fne.getMessage());
            throw fne;
        } catch (IOException ie) {
            SLog.i(TAG, "[FILE]IO Error: " + ie.getMessage());
            throw ie;
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

}
