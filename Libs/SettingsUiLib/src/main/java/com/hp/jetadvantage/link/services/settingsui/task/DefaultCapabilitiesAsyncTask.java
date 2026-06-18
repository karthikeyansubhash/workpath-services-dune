// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.settingsui.task;

import android.content.Context;
import android.os.AsyncTask;

import com.hp.jetadvantage.link.api.JetAdvantageLink;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.SsdkUnsupportedException;
import com.hp.jetadvantage.link.api.copier.CopierService;
import com.hp.jetadvantage.link.api.copier.CopyAttributesCaps;
import com.hp.jetadvantage.link.api.printer.PrintAttributesCaps;
import com.hp.jetadvantage.link.api.printer.PrinterService;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesCaps;
import com.hp.jetadvantage.link.api.scanner.ScannerService;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.settingsui.SettingsUIActivity;
import com.hp.jetadvantage.link.services.settingsui.interfaces.ConfigurationFragment;

/**
 * Async task to load capabilities for ConfigurationFragment
 */
public final class DefaultCapabilitiesAsyncTask<T, F> extends AsyncTask<Void, Void, Void> {
    private static final String TAG = SettingsUIActivity.TAG + "/D";

    /** Application Context */
    private final Context mContext;

    /** Error Message string to provide to the user */
    private String mErrorMsg = null;

    private F mCaps = null;

    private ConfigurationFragment<T, F> mFragment;
    private SettingsUIActivity.Operations mOperation;

    /**
     * General constructor
     *
     * @param context apps {@link Context}
     * @param fragment {@link ConfigurationFragment} to provide caps to
     * @param operation {@link SettingsUIActivity.Operations}
     */
    public DefaultCapabilitiesAsyncTask(final Context context, final ConfigurationFragment<T, F> fragment,
                                        final SettingsUIActivity.Operations operation, final Class<T> clazz) {
        mContext = context;
        mFragment = fragment;
        mOperation = operation;

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

        mFragment.onDefaultCapabilitiesLoaded(mCaps, mErrorMsg);
    }

    /**
     * Executes request for capabilities from ScannerService.
     *
     * @param context {@link Context}
     *
     * @return {@link ScanAttributesCaps}
     */
    @SuppressWarnings("unchecked") // Because we've already checked that T is from right class
    private F requestCaps(final Context context) {
        final Result result = new Result();
        final F caps;

        switch (mOperation) {
            case SCAN_TO_ME:
                caps = (F) ScannerService.getDefaults(context, result);
                break;

            case PRINT:
                caps = (F) PrinterService.getDefaults(context, result);
                break;

            case COPY:
                caps = (F) CopierService.getDefaults(context, result);
                break;

            default:
                throw new IllegalArgumentException("Invalid operation has been provided.");
        }

        if (caps != null) {
            SLog.d(TAG, "Caps are " + caps);
        }

        return caps;
    }
}
