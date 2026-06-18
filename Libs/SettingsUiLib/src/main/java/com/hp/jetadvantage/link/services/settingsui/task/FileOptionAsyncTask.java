// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.settingsui.task;

import android.os.AsyncTask;
import android.preference.ListPreference;

import com.hp.jetadvantage.link.api.JetAdvantageLink;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.SsdkUnsupportedException;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributesCaps;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScannerService;
import com.hp.jetadvantage.link.services.settingsui.SettingsUIActivity;
import com.hp.jetadvantage.link.services.settingsui.fragments.ScanConfigurationFragment;
import com.hp.jetadvantage.link.services.settingsui.interfaces.ConfigurationFragment;

import static com.hp.jetadvantage.link.services.settingsui.fragments.ScanConfigurationFragment.PREF_COLOR_MODE;
import static com.hp.jetadvantage.link.services.settingsui.fragments.ScanConfigurationFragment.PREF_DOC_FORMAT;

/**
 * Async task to load capabilities for ConfigurationFragment
 */
public final class FileOptionAsyncTask extends AsyncTask<Void, Void, FileOptionsAttributesCaps> {
    private static final String TAG = SettingsUIActivity.TAG;

    /** Error Message string to provide to the user */
    private Result result = null;
    private String key = null;

    private ScanConfigurationFragment mFragment = null;
    private ScanAttributes.DocumentFormat docFormat;
    private ScanAttributes.ColorMode colorMode;

    /**
     * General constructor
     *
     * @param fragment {@link ConfigurationFragment} to provide caps to
     */
    public FileOptionAsyncTask(final ScanConfigurationFragment fragment, String key, Result result) {
        this.mFragment = fragment;
        this.key = key;
        this.result = result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ListPreference docPref = (ListPreference) mFragment.findPreference(PREF_DOC_FORMAT);
        String docEntry = docPref.getValue();
        docFormat = docEntry == null ? ScanAttributes.DocumentFormat.DEFAULT : ScanAttributes.DocumentFormat.valueOf(docEntry);

        ListPreference colorPref = (ListPreference) mFragment.findPreference(PREF_COLOR_MODE);
        String colorEntry = colorPref.getValue();
        colorMode = colorEntry == null ? ScanAttributes.ColorMode.DEFAULT : ScanAttributes.ColorMode.valueOf(colorEntry);
    }

    @Override
    protected FileOptionsAttributesCaps doInBackground(final Void... params) {

        try {
            JetAdvantageLink.getInstance().initialize(mFragment.getActivity());
        } catch (final SsdkUnsupportedException e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "SDK is not supported!");
            return null;
        } catch (final SecurityException e) {
            Result.pack(result, Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "Security exception!");
            return null;
        }

        FileOptionsAttributesCaps fileOptionsAttrCaps = ScannerService.getFileOptionsCapabilities(mFragment.getActivity(),
                colorMode, docFormat, result);

        return fileOptionsAttrCaps;
    }

    @Override
    protected void onPostExecute(final FileOptionsAttributesCaps fileOptionsAttrCaps) {
        super.onPostExecute(fileOptionsAttrCaps);

        mFragment.fillFileOptionAttrCaps(fileOptionsAttrCaps, key, result);
    }
}
