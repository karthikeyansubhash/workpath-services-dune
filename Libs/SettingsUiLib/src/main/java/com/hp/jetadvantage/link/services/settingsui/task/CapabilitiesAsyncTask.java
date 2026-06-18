// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.settingsui.task;

import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.hp.jetadvantage.link.api.JetAdvantageLink;
import com.hp.jetadvantage.link.api.copier.CopierService;
import com.hp.jetadvantage.link.api.copier.CopyAttributesCaps;
import com.hp.jetadvantage.link.api.copier.Copylet;
import com.hp.jetadvantage.link.api.printer.Printlet;
import com.hp.jetadvantage.link.api.scanner.Scanlet;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.SsdkUnsupportedException;
import com.hp.jetadvantage.link.api.printer.PrintAttributesCaps;
import com.hp.jetadvantage.link.api.printer.PrinterService;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesCaps;
import com.hp.jetadvantage.link.api.scanner.ScannerService;
import com.hp.jetadvantage.link.services.settingsui.SettingsUIActivity;
import com.hp.jetadvantage.link.services.settingsui.interfaces.ConfigurationFragment;

/**
 * Async task to load capabilities for ConfigurationFragment
 */
public final class CapabilitiesAsyncTask<T, F> extends AsyncTask<Void, Void, Void> {
    private static final String TAG = SettingsUIActivity.TAG;

    /** Application Context */
    private final Context mContext;

    /** Error Message string to provide to the user */
    private String mErrorMsg = null;

    private T mCaps = null;
    private int mClientApiLevel = Sdk.VERSION.LEVEL;

    private ConfigurationFragment<T, F> mFragment;
    private SettingsUIActivity.Operations mOperation;

    /**
     * General constructor
     *
     * @param context apps {@link Context}
     * @param fragment {@link ConfigurationFragment} to provide caps to
     * @param operation {@link SettingsUIActivity.Operations}
     */
    public CapabilitiesAsyncTask(final Context context, final ConfigurationFragment<T, F> fragment,
                                 final SettingsUIActivity.Operations operation, final int clientApiLevel, final Class<T> clazz) {
        mContext = context;
        mFragment = fragment;
        mOperation = operation;
        mClientApiLevel = clientApiLevel;

        switch (mOperation) {
            case SCAN_TO_ME:
                if (!ScanAttributesCaps.class.isAssignableFrom(clazz)) {
                    throw new IllegalArgumentException("Incompatible class and operation have been provided.");
                }
                break;

            case PRINT:
                if (!PrintAttributesCaps.class.isAssignableFrom(clazz)) {
                    throw new IllegalArgumentException("Incompatible class and operation have been provided.");
                }
                break;

            case COPY:
                if (!CopyAttributesCaps.class.isAssignableFrom(clazz)) {
                    throw new IllegalArgumentException("Incompatible class and operation have been provided.");
                }
                break;

            default:
                throw new IllegalArgumentException("Invalid operation has been provided.");
        }
    }

    @Override
    protected Void doInBackground(final Void... params) {

        try {
            JetAdvantageLink.getInstance().initialize(mContext);
        } catch (final SsdkUnsupportedException e) {
            mErrorMsg = "SDK is not supported!";
            return null;
        } catch (final SecurityException e) {
            mErrorMsg = "Security exception!";
            return null;
        }
        mCaps = requestCaps(mContext);

        if (null == mCaps) {
            mErrorMsg = "Not able to obtain printers capabilities";
        }
        return null;
    }

    @Override
    protected void onPostExecute(final Void aVoid) {
        super.onPostExecute(aVoid);

        mFragment.onCapabilitiesLoaded(mCaps, mErrorMsg);
    }

    /**
     * Executes request for capabilities from ScannerService.
     *
     * @param context {@link android.content.Context}
     *
     * @return {@link ScanAttributesCaps}
     */
    @SuppressWarnings("unchecked") // Because we've already checked that T is from right class
    private T requestCaps(final Context context) {
        final Result result = new Result();
        final T caps = getCapabilities(context, result);

        if (caps != null) {
            SLog.d(TAG, "Caps are " + caps);
        }

        return caps;
    }

    private T getCapabilities(Context context, Result result) {
        T caps = null;
        try {
            final ContentResolver resolver = context.getContentResolver();
            Bundle extras = new Bundle();
            extras.putInt(Scanlet.Keys.KEY_CLIENT_VERSION, mClientApiLevel);
            Bundle bundle = null;

            switch (mOperation) {
                case SCAN_TO_ME:
                    bundle = resolver.call(Scanlet.getContentUri(resolver),
                            Scanlet.Method.GET_CAPS, null, extras);
                    Result.parse(bundle, result);
                    if (result.getCode() == Result.RESULT_OK) {
                        final String jsonStr = bundle.getString(Result.KEY_RESULT);
                        caps = (T) JsonParser.getInstance().fromJson(jsonStr, ScanAttributesCaps.class);
                    }
                    break;

                case PRINT:
                    bundle = resolver.call(Printlet.getContentUri(resolver),
                            Printlet.Method.GET_CAPS, null, extras);
                    Result.parse(bundle, result);
                    if (result.getCode() == Result.RESULT_OK) {
                        final String jsonStr = bundle.getString(Result.KEY_RESULT);
                        caps = (T) JsonParser.getInstance().fromJson(jsonStr, PrintAttributesCaps.class);
                    }
                    break;

                case COPY:
                    bundle = resolver.call(Copylet.getContentUri(resolver),
                            Copylet.Method.GET_CAPS, null, extras);
                    Result.parse(bundle, result);
                    if (result.getCode() == Result.RESULT_OK) {
                        final String jsonStr = bundle.getString(Result.KEY_RESULT);
                        caps = (T) JsonParser.getInstance().fromJson(jsonStr, CopyAttributesCaps.class);
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid operation has been provided.");
            }
        } catch (SecurityException se){
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, se.getMessage());
            throw se;
        } catch (Exception e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.UNKNOWN, e.getMessage());
        }

        return caps;
    }
}
