// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.settingsui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.EditText;

import com.hp.jetadvantage.link.api.CapabilitiesExceededException;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.massstorage.MassStorageInfo;
import com.hp.jetadvantage.link.api.massstorage.MassStorageService;
import com.hp.jetadvantage.link.api.printer.NetworkCredentialsAttributes;
import com.hp.jetadvantage.link.api.printer.PrintAttributes;
import com.hp.jetadvantage.link.api.printer.PrintAttributes.AutoFit;
import com.hp.jetadvantage.link.api.printer.PrintAttributes.ColorMode;
import com.hp.jetadvantage.link.api.printer.PrintAttributes.DocumentFormat;
import com.hp.jetadvantage.link.api.printer.PrintAttributes.Duplex;
import com.hp.jetadvantage.link.api.printer.PrintAttributes.PaperSize;
import com.hp.jetadvantage.link.api.printer.PrintAttributes.PaperSource;
import com.hp.jetadvantage.link.api.printer.PrintAttributes.PaperType;
import com.hp.jetadvantage.link.api.printer.PrintAttributes.Source;
import com.hp.jetadvantage.link.api.printer.PrintAttributes.StapleMode;
import com.hp.jetadvantage.link.api.printer.PrintAttributesCaps;
import com.hp.jetadvantage.link.api.printer.PrintletAttributes;
import com.hp.jetadvantage.link.api.printer.intent.PrintRequestIntent;
import com.hp.jetadvantage.link.common.Platform;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.utils.NetworkUtility;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.settingsui.R;
import com.hp.jetadvantage.link.services.settingsui.SettingsUIActivity;
import com.hp.jetadvantage.link.services.settingsui.helper.PreferenceHelper;
import com.hp.jetadvantage.link.services.settingsui.interfaces.ConfigurationCallback;
import com.hp.jetadvantage.link.services.settingsui.interfaces.ConfigurationFragment;
import com.hp.jetadvantage.link.services.settingsui.preferences.IconsListPreference;
import com.hp.jetadvantage.link.services.settingsui.preferences.NumberPickerPreference;
import com.hp.jetadvantage.link.services.settingsui.task.CapabilitiesAsyncTask;
import com.hp.jetadvantage.link.services.settingsui.utils.FileUtility;
import com.ipaulpro.afilechooser.FileChooserActivity;
import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Simple {@link PreferenceFragment} to set Print Attributes and
 * save into preferences.
 */
public final class PrintConfigurationFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener, ConfigurationFragment<PrintAttributesCaps, PrintAttributes> {
    private static final String TAG = SettingsUIActivity.TAG;

    private static final int PRINT_REQUEST_CODE = 10;
    private static final String CACHE_FILE_NAME = "tmp_print";

    // Preferences keys for PrintAttributes
    public static final String PREF_COPIES = "pref_copies";
    public static final String PREF_COLOR_MODE = "pref_colorMode";
    public static final String PREF_DUPLEX_MODE = "pref_duplexMode";
    public static final String PREF_FILENAME = "pref_filename";
    public static final String PREF_STREAM_FILENAME = "pref_stream_filename";
    public static final String PREF_USB_STORAGE = "pref_usb_storage";
    public static final String PREF_USB_FILENAME = "pref_usb_filename";

    /**
     * Complete filename, because PREF_FILENAME can be just displayable name
     */
    public static final String PREF_FILENAME_COMPLETE = "pref_filenameComplete";
    public static final String PREF_AUTOFIT = "pref_autoFit";
    public static final String PREF_STAPLE_MODE = "pref_stapleMode";
    public static final String PREF_PAPER_SOURCE = "pref_paperSource";
    public static final String PREF_PAPER_SIZE = "pref_paperSize";
    public static final String PREF_PAPER_TYPE = "pref_paperType";
    public static final String PREF_DOCUMENT_FORMAT = "pref_documentFormat";
    public static final String PREF_COLLATE_MODE = "pref_collateMode";

    public static final String PREF_ORIENTATION = "pref_print_orientation";
    public static final String PREF_PRINT_QUALITY = "pref_print_quality";
    public static final String PREF_OUTPUT_BIN = "pref_print_output_bin";
    public static final String PREF_START_PAGE_RANGES = "pref_print_start_pageRanges";
    public static final String PREF_END_PAGE_RANGES = "pref_print_end_pageRanges";
    public static final String PREF_FINISHINGS = "pref_print_finishings";

    public static final String PREF_SOURCE = "pref_source";
    public static final String PREF_URI = "pref_uri";
    public static final String PREF_URI_USERNAME = "pref_uri_username";
    public static final String PREF_URI_PASSWORD = "pref_uri_password";

    public static final String PREF_SOURCE_STORAGE_CATEGORY = "source_storage_category";
    public static final String PREF_SOURCE_HTTP_CATEGORY = "source_http_category";
    public static final String PREF_SOURCE_USB_CATEGORY = "source_usb_category";
    public static final String PREF_SOURCE_STREAM_CATEGORY = "source_stream_category";

    public static final String PREF_BASE_ATTRIBUTES_CATEGORY = "pref_generalCategory";

    private IconsListPreference mDuplexPref;
    private ListPreference mCMPref;
    private ListPreference mAFPref;
    private NumberPickerPreference mCopiesPref;
    private Preference mFilePref;
    private Preference mUsbFilePref;
    private Preference mStreamFilenamePref;
    private ListPreference mSMPref;
    private ListPreference mPaperSrcPref;
    private ListPreference mPaperSzPref;
    private ListPreference mPaperTypePref;
    private ListPreference mDocFmtPref;
    private ListPreference mCollatePref;

    private ListPreference mOrientationPref;
    private ListPreference mPrintQualityPref;
    private ListPreference mOutputBinPref;
    private NumberPickerPreference mStartPageRangesPref;
    private NumberPickerPreference mEndPageRangesPref;
    private MultiSelectListPreference mFinishingsPref;

    private EditTextPreference mFileUriPref;
    private EditTextPreference mFileUriUsernamePref;
    private EditTextPreference mFileUriPasswordPref;
    private ListPreference mSourcePref;

    private PreferenceCategory mSourceCategory;
    private PreferenceCategory mStorageCategory;
    private PreferenceCategory mHttpCategory;
    private PreferenceCategory mUsbCategory;
    private PreferenceCategory mStreamCategory;

    private PreferenceCategory mBaseAttributesCategory;

    private int mClientApiLevel;
    /**
     * Task for loading caps, keep reference to be able to cancel
     */
    private AsyncTask<Void, Void, Void> mCapsLoadingTask;
    /**
     * Cached caps
     */
    private PrintAttributesCaps mCaps = null;
    /**
     * Parents callback
     */
    private ConfigurationCallback mCallback = null;
    /**
     * To avoid caps loading if resumed from file choose
     */
    private boolean mResumedFromChoose = false;
    /**
     * Saved fragment state with current settings
     */
    private Bundle mSavedState;
    /**
     * Root folder for USB
     */
    private String mUsbRootFolder;

    /**
     * Default factory
     *
     * @param printAttributes clients attributes
     * @return new instance
     */
    public static PrintConfigurationFragment newInstance(final PrintAttributes printAttributes, int clientApiLevel) {
        final Bundle args = new Bundle();
        final PrintConfigurationFragment frag = new PrintConfigurationFragment();

        args.putParcelable(ARG_ATTRS, printAttributes);
        args.putInt(ARG_CLIENT_API_LEVEL, clientApiLevel);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();
        if (args != null) {
            mClientApiLevel = args.getInt(ARG_CLIENT_API_LEVEL, Sdk.VERSION.LEVEL);
        }

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.print_preferences);

        mDuplexPref = (IconsListPreference) findPreference(PREF_DUPLEX_MODE);
        mCMPref = (ListPreference) findPreference(PREF_COLOR_MODE);
        mAFPref = (ListPreference) findPreference(PREF_AUTOFIT);
        mSMPref = (ListPreference) findPreference(PREF_STAPLE_MODE);
        mPaperSrcPref = (ListPreference) findPreference(PREF_PAPER_SOURCE);
        mPaperSzPref = (ListPreference) findPreference(PREF_PAPER_SIZE);
        mPaperTypePref = (ListPreference) findPreference(PREF_PAPER_TYPE);
        mDocFmtPref = (ListPreference) findPreference(PREF_DOCUMENT_FORMAT);
        mCollatePref = (ListPreference) findPreference(PREF_COLLATE_MODE);
        mFilePref = findPreference(PREF_FILENAME);
        mFilePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                final Intent intent = new Intent(getActivity().getApplicationContext(), FileChooserActivity.class);
                intent.putExtra(FileUtils.INTENT_SELECT, FileUtils.FILE);
                startActivityForResult(intent, PRINT_REQUEST_CODE);
                return true;
            }
        });

        mUsbFilePref = findPreference(PREF_USB_FILENAME);
        mUsbFilePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                final SharedPreferences prefs = getPreferenceScreen().getSharedPreferences();
                mUsbRootFolder = prefs.getString(ScanConfigurationFragment.PREF_USB_STORAGE, "");

                final Intent intent = new Intent(getActivity().getApplicationContext(), FileChooserActivity.class);
                intent.putExtra(FileUtils.PATH, mUsbRootFolder);
                intent.putExtra(FileUtils.INTENT_SELECT, FileUtils.FILE);
                startActivityForResult(intent, PRINT_REQUEST_CODE);
                return true;
            }
        });

        mStreamFilenamePref = findPreference(PREF_STREAM_FILENAME);
        mStreamFilenamePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                final Intent intent = new Intent(getActivity().getApplicationContext(), FileChooserActivity.class);
                intent.putExtra(FileUtils.INTENT_SELECT, FileUtils.FILE);
                startActivityForResult(intent, PRINT_REQUEST_CODE);
                return true;
            }
        });

        // Clear number prefs
        mCopiesPref = ((NumberPickerPreference) findPreference(PREF_COPIES));
        mCopiesPref.setLimits(1, 1);
        mCopiesPref.getEditor().putInt(PREF_COPIES, 1).apply();

        mSourcePref = (ListPreference) findPreference(PREF_SOURCE);
        mFileUriPref = (EditTextPreference) findPreference(PREF_URI);
        mFileUriPref.setText(null);
        mFileUriUsernamePref = (EditTextPreference) findPreference(PREF_URI_USERNAME);
        mFileUriUsernamePref.setText(null);
        mFileUriPasswordPref = (EditTextPreference) findPreference(PREF_URI_PASSWORD);
        mFileUriPasswordPref.setText(null);

        mOrientationPref = (ListPreference) findPreference(PREF_ORIENTATION);
        mPrintQualityPref = (ListPreference) findPreference(PREF_PRINT_QUALITY);
        mOutputBinPref = (ListPreference) findPreference(PREF_OUTPUT_BIN);
        mStartPageRangesPref = (NumberPickerPreference) findPreference(PREF_START_PAGE_RANGES);
        initPageRangePref(mStartPageRangesPref, PREF_START_PAGE_RANGES);
        mEndPageRangesPref = (NumberPickerPreference) findPreference(PREF_END_PAGE_RANGES);
        initPageRangePref(mEndPageRangesPref, PREF_END_PAGE_RANGES);
        mFinishingsPref = (MultiSelectListPreference) findPreference(PREF_FINISHINGS);

        mStorageCategory = (PreferenceCategory) findPreference(PREF_SOURCE_STORAGE_CATEGORY);
        mHttpCategory = (PreferenceCategory) findPreference(PREF_SOURCE_HTTP_CATEGORY);
        mUsbCategory = (PreferenceCategory) findPreference(PREF_SOURCE_USB_CATEGORY);
        mStreamCategory = (PreferenceCategory) findPreference(PREF_SOURCE_STREAM_CATEGORY);
        mBaseAttributesCategory = (PreferenceCategory) findPreference(PREF_BASE_ATTRIBUTES_CATEGORY);

        getPreferenceScreen().removePreference(mHttpCategory);
        getPreferenceScreen().removePreference(mUsbCategory);
        getPreferenceScreen().removePreference(mStreamCategory);

        // Clear text prefs
        if (savedInstanceState == null) {
            mFilePref.getEditor().putString(PREF_FILENAME, "").apply();
            mUsbFilePref.getEditor().putString(PREF_USB_FILENAME, "").apply();
            mFileUriPref.getEditor().putString(PREF_URI, "").apply();
            mFileUriUsernamePref.getEditor().putString(PREF_URI_USERNAME, "").apply();
            mFileUriPasswordPref.getEditor().putString(PREF_URI_PASSWORD, "").apply();
            mStreamFilenamePref.getEditor().putString(PREF_STREAM_FILENAME, "").apply();
        }

        // Clear file name
        onLoaded(null, null);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mResumedFromChoose) {
            if (mCaps == null) {
                getPreferenceScreen().setEnabled(false);

                mCapsLoadingTask = new CapabilitiesAsyncTask<>(getActivity().getApplicationContext(),
                        this, SettingsUIActivity.Operations.PRINT, mClientApiLevel, PrintAttributesCaps.class).execute();
            }
        } else {
            final SharedPreferences prefs = getPreferenceScreen().getSharedPreferences();

            prefs.registerOnSharedPreferenceChangeListener(this);
            onSharedPreferenceChanged(prefs, PREF_FILENAME);
            onSharedPreferenceChanged(prefs, PREF_USB_FILENAME);
            onSharedPreferenceChanged(prefs, PREF_STREAM_FILENAME);
        }

        mResumedFromChoose = false;
    }

    @Override
    public void onPause() {
        super.onPause();

        final SharedPreferences prefs = getPreferenceScreen().getSharedPreferences();

        prefs.unregisterOnSharedPreferenceChangeListener(this);

        if (mCapsLoadingTask != null) {
            mCapsLoadingTask.cancel(true);
            mCapsLoadingTask = null;
        }
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);

        if (activity instanceof ConfigurationCallback) {
            mCallback = (ConfigurationCallback) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        final Preference preference = findPreference(key);
        if (preference == null) {
            // not found - not visible preference
            return;
        }

        if (PREF_SOURCE.equals(key)) {
            final String entryStr = ((ListPreference) preference).getValue();
            final PrintAttributes.Source entry =
                    entryStr == null ? PrintAttributes.Source.STORAGE : PrintAttributes.Source.valueOf(entryStr);

            if (entry == PrintAttributes.Source.STORAGE) {
                mSourceCategory = mStorageCategory;
                if (getPreferenceScreen().findPreference(PREF_SOURCE_STORAGE_CATEGORY) == null) {
                    mSourceCategory.setEnabled(true);
                    getPreferenceScreen().addPreference(mSourceCategory);
                    mSourceCategory.setEnabled(true);
                }
                getPreferenceScreen().removePreference(mHttpCategory);
                getPreferenceScreen().removePreference(mUsbCategory);
                getPreferenceScreen().removePreference(mStreamCategory);

                onSharedPreferenceChanged(sharedPreferences, PREF_FILENAME);
            } else if (entry == Source.HTTP) {
                mSourceCategory = mHttpCategory;
                if (getPreferenceScreen().findPreference(PREF_SOURCE_HTTP_CATEGORY) == null) {
                    mSourceCategory.setEnabled(true);
                    getPreferenceScreen().addPreference(mSourceCategory);
                    mSourceCategory.setEnabled(true);
                }
                getPreferenceScreen().removePreference(mStorageCategory);
                getPreferenceScreen().removePreference(mUsbCategory);
                getPreferenceScreen().removePreference(mStreamCategory);

                onSharedPreferenceChanged(sharedPreferences, PREF_URI);
                onSharedPreferenceChanged(sharedPreferences, PREF_URI_USERNAME);
                onSharedPreferenceChanged(sharedPreferences, PREF_URI_PASSWORD);
            } else if (entry == Source.USB) {
                mSourceCategory = mUsbCategory;

                if (getPreferenceScreen().findPreference(PREF_SOURCE_USB_CATEGORY) == null) {
                    mSourceCategory.setEnabled(true);
                    getPreferenceScreen().addPreference(mSourceCategory);
                    mSourceCategory.setEnabled(true);
                }
                getPreferenceScreen().removePreference(mStorageCategory);
                getPreferenceScreen().removePreference(mHttpCategory);
                getPreferenceScreen().removePreference(mStreamCategory);

                fillUSBStorages();

                onSharedPreferenceChanged(sharedPreferences, PREF_USB_STORAGE);
            } else if (entry == Source.STREAM) {
                mSourceCategory = mStreamCategory;
                if (getPreferenceScreen().findPreference(PREF_SOURCE_STREAM_CATEGORY) == null) {
                    mSourceCategory.setEnabled(true);
                    getPreferenceScreen().addPreference(mSourceCategory);
                    mSourceCategory.setEnabled(true);
                }
                getPreferenceScreen().removePreference(mStorageCategory);
                getPreferenceScreen().removePreference(mHttpCategory);
                getPreferenceScreen().removePreference(mUsbCategory);

                onSharedPreferenceChanged(sharedPreferences, PREF_STREAM_FILENAME);
            }
        } else if (PREF_USB_STORAGE.equals(key)) {
            // USB storage changed - reset file name
            mUsbFilePref.getEditor().putString(PREF_USB_FILENAME, "").apply();

            onSharedPreferenceChanged(sharedPreferences, PREF_USB_FILENAME);
        }

        if (preference instanceof ListPreference) {
            final String entry = (String) ((ListPreference) preference).getEntry();

            if (entry == null || entry.length() == 0) {
                ((ListPreference) preference).setValueIndex(0);
                preference.setSummary("%s");
            } else {
                preference.setSummary(entry);
            }
        } else if (preference instanceof EditTextPreference) {
            String text = ((EditTextPreference) preference).getText();
            if (TextUtils.isEmpty(text)) {
                if (PREF_URI.equals(key)) {
                    preference.setSummary(R.string.hint_uri);
                } else if (PREF_URI_USERNAME.equals(key)) {
                    preference.setSummary(R.string.hint_uri_username);
                } else if (PREF_URI_PASSWORD.equals(key)) {
                    preference.setSummary(R.string.hint_uri_password);
                } else {
                    preference.setSummary(text);
                }
            } else {
                EditText editText = ((EditTextPreference) preference).getEditText();
                if (editText.getTransformationMethod() != null) {
                    text = editText.getTransformationMethod().getTransformation(text, editText).toString();
                }
                preference.setSummary(text);
            }

            if (PREF_URI.equals(key)) {
                if (mCallback != null && mCaps != null) {
                    boolean enableButton = !TextUtils.isEmpty(text);
                    if (enableButton) {
                        try {
                            Source source = Source.valueOf(sharedPreferences.getString(PREF_SOURCE, Source.STORAGE.name()));
                            PrintAttributes.validateUri(source, Uri.parse(text));
                        } catch (Exception e) {
                            enableButton = false;
                        }
                    }

                    mCallback.enableActionButton(enableButton);
                }
            }
        } else if (preference instanceof NumberPickerPreference) {
            final Integer val = ((NumberPickerPreference) preference).getInteger();
            preference.setSummary(String.valueOf(val));
        } else if (preference instanceof MultiSelectListPreference) {
            Set<String> prefSet = ((MultiSelectListPreference) preference).getValues();
            String summary = "";
            for (String value : prefSet) {
                summary += value + " ";
            }
            preference.setSummary(summary);
        }

        if (PREF_FILENAME.equals(key) || PREF_USB_FILENAME.equals(key) || PREF_STREAM_FILENAME.equals(key)) {
            final String fileName = sharedPreferences.getString(key, "");

            if (TextUtils.isEmpty(fileName)) {
                // Show special summary for file name
                preference.setSummary(R.string.select_file);
            } else {
                preference.setSummary(fileName);
            }

            if (mCallback != null && mCaps != null) {
                mCallback.enableActionButton(!TextUtils.isEmpty(fileName));
            }
        }

        if (mClientApiLevel < Sdk.VERSION_LEVEL.SIX) {
            mBaseAttributesCategory.removePreference(mOrientationPref);
            mBaseAttributesCategory.removePreference(mPrintQualityPref);
            mBaseAttributesCategory.removePreference(mOutputBinPref);
            mBaseAttributesCategory.removePreference(mStartPageRangesPref);
            mBaseAttributesCategory.removePreference(mEndPageRangesPref);
            mBaseAttributesCategory.removePreference(mFinishingsPref);
        }
    }

    @Override
    public Intent getConfiguredIntent(String rid) {
        final SharedPreferences prefs = getPreferenceScreen().getSharedPreferences();
        final PrintRequestIntent intent = new PrintRequestIntent();
        PrintAttributes attrs = null;

        final Duplex duplex = Duplex.valueOf(prefs.getString(PrintConfigurationFragment.PREF_DUPLEX_MODE,
                Duplex.DEFAULT.name()));
        final ColorMode cm = ColorMode.valueOf(prefs.getString(PrintConfigurationFragment.PREF_COLOR_MODE,
                ColorMode.DEFAULT.name()));
        final AutoFit af = AutoFit.valueOf(prefs.getString(PrintConfigurationFragment.PREF_AUTOFIT,
                AutoFit.DEFAULT.name()));
        final StapleMode sm = StapleMode.valueOf(prefs.getString(PrintConfigurationFragment.PREF_STAPLE_MODE,
                StapleMode.DEFAULT.name()));
        final PaperSource psrc = PaperSource.valueOf(prefs.getString(PrintConfigurationFragment.PREF_PAPER_SOURCE,
                PaperSource.DEFAULT.name()));
        final PaperSize psz = PaperSize.valueOf(prefs.getString(PrintConfigurationFragment.PREF_PAPER_SIZE,
                PaperSize.DEFAULT.name()));
        final PaperType paperType = PaperType.valueOf(prefs.getString(PrintConfigurationFragment.PREF_PAPER_TYPE,
                PaperType.DEFAULT.name()));
        final DocumentFormat dfmt = DocumentFormat.valueOf(prefs.getString(PrintConfigurationFragment.PREF_DOCUMENT_FORMAT,
                DocumentFormat.AUTO.name()));
        final PrintAttributes.CollateMode collateMode = PrintAttributes.CollateMode.valueOf(prefs.getString(PrintConfigurationFragment.PREF_COLLATE_MODE,
                PrintAttributes.CollateMode.DEFAULT.name()));
        final PrintAttributes.Orientation ot = PrintAttributes.Orientation.valueOf(
                prefs.getString(PrintConfigurationFragment.PREF_ORIENTATION, PrintAttributes.Orientation.DEFAULT.name()));
        final PrintAttributes.PrintQuality pq = PrintAttributes.PrintQuality.valueOf(
                prefs.getString(PrintConfigurationFragment.PREF_PRINT_QUALITY, PrintAttributes.PrintQuality.DEFAULT.name()));
        final PrintAttributes.OutputBin ob = PrintAttributes.OutputBin.valueOf(
                prefs.getString(PrintConfigurationFragment.PREF_OUTPUT_BIN, PrintAttributes.OutputBin.DEFAULT.name()));
        int startPageRanges = prefs.getInt(PrintConfigurationFragment.PREF_START_PAGE_RANGES, 0);
        int endPageRanges = prefs.getInt(PrintConfigurationFragment.PREF_END_PAGE_RANGES, 0);
        Set<String> finishingSet = new HashSet<>();
        finishingSet.add(PrintAttributes.Finishings.DEFAULT.name());
        finishingSet = prefs.getStringSet(PrintConfigurationFragment.PREF_FINISHINGS, finishingSet);
        List<PrintAttributes.Finishings> fo = new ArrayList<>();
        for (String finishing : finishingSet) {
            fo.add(PrintAttributes.Finishings.valueOf(finishing));
        }

        final Source source = Source.valueOf(prefs.getString(PrintConfigurationFragment.PREF_SOURCE,
                Source.STORAGE.name()));

        if (source == Source.STORAGE) {
            String filePath = prefs.getString(PrintConfigurationFragment.PREF_FILENAME_COMPLETE, "");

            if (TextUtils.isEmpty(filePath)) {
                filePath = prefs.getString(PrintConfigurationFragment.PREF_FILENAME, "");
            }

            final Uri fileUri = Uri.fromFile(new File(filePath));
            if (fileUri == null || Uri.EMPTY.equals(fileUri) || "/".equals(fileUri.getEncodedPath())) {
                SLog.e(TAG, "Build failed: filename is not provided ");
                mCallback.onFailedCreateIntent(getString(R.string.print_creation_failed_empty_name));
                return intent;
            }

            try {
                attrs = new PrintAttributes.PrintFromStorageBuilder(fileUri)
                        .setCollateMode(collateMode)
                        .setColorMode(cm)
                        .setDuplex(duplex)
                        .setAutoFit(af)
                        .setStapleMode(sm)
                        .setPaperSource(psrc)
                        .setPaperSize(psz)
                        .setPaperType(paperType)
                        .setDocumentFormat(dfmt)
                        .setCopies(prefs.getInt(PrintConfigurationFragment.PREF_COPIES, 1))
                        .setOrientation(ot)
                        .setPrintQuality(pq)
                        .setOutputBin(ob)
                        .setStartPageRanges(startPageRanges)
                        .setEndPageRanges(endPageRanges)
                        .setFinishingsList(fo)
                        .build(mCaps);
            } catch (CapabilitiesExceededException e) {
                SLog.e(TAG, "Build failed " + e.getMessage());
                mCallback.onFailedCreateIntent(getString(R.string.print_creation_failed));
            } catch (IllegalArgumentException e) {
                SLog.e(TAG, "Build failed " + e.getMessage());
                mCallback.onFailedCreateIntent(getString(R.string.print_creation_failed));
            }
        } else if (source == Source.HTTP) {
            String fileUri = prefs.getString(PrintConfigurationFragment.PREF_URI, "");
            String fileUriUsername = prefs.getString(PrintConfigurationFragment.PREF_URI_USERNAME, "");
            String fileUriPassword = prefs.getString(PrintConfigurationFragment.PREF_URI_PASSWORD, "");

            try {
                NetworkCredentialsAttributes networkCredentialsAttributes = null;
                if (!TextUtils.isEmpty(fileUriUsername) && !TextUtils.isEmpty(fileUriPassword)) {
                    networkCredentialsAttributes = new NetworkCredentialsAttributes.Builder()
                            .setUserName(fileUriUsername)
                            .setPassword(fileUriPassword)
                            .build();
                }

                // building with common print attributes set
                attrs = new PrintAttributes.PrintFromHttpBuilder(Uri.parse(fileUri))
                        .setCollateMode(collateMode)
                        .setColorMode(cm)
                        .setDuplex(duplex)
                        .setAutoFit(af)
                        .setStapleMode(sm)
                        .setPaperSource(psrc)
                        .setPaperSize(psz)
                        .setPaperType(paperType)
                        .setDocumentFormat(dfmt)
                        .setCopies(prefs.getInt(PrintConfigurationFragment.PREF_COPIES, 1))
                        .setOrientation(ot)
                        .setPrintQuality(pq)
                        .setOutputBin(ob)
                        .setStartPageRanges(startPageRanges)
                        .setEndPageRanges(endPageRanges)
                        .setFinishingsList(fo)
                        .setNetworkCredentials(networkCredentialsAttributes)
                        .build(mCaps);
            } catch (CapabilitiesExceededException e) {
                SLog.e(TAG, "Build failed " + e.getMessage());
                mCallback.onFailedCreateIntent(getString(R.string.print_creation_failed));
            } catch (IllegalArgumentException e) {
                SLog.e(TAG, "Build failed " + e.getMessage());
                mCallback.onFailedCreateIntent(getString(R.string.print_creation_failed));
            }
        } else if (source == Source.USB) {
            String filePath = prefs.getString(PrintConfigurationFragment.PREF_FILENAME_COMPLETE, "");

            if (TextUtils.isEmpty(filePath)) {
                filePath = prefs.getString(PrintConfigurationFragment.PREF_USB_FILENAME, "");
            }

            try {
                // building with common print attributes set
                attrs = new PrintAttributes.PrintFromUsbBuilder(filePath)
                        .setCollateMode(collateMode)
                        .setColorMode(cm)
                        .setDuplex(duplex)
                        .setAutoFit(af)
                        .setStapleMode(sm)
                        .setPaperSource(psrc)
                        .setPaperSize(psz)
                        .setDocumentFormat(dfmt)
                        .setCopies(prefs.getInt(PrintConfigurationFragment.PREF_COPIES, 1))
                        .setOrientation(ot)
                        .setPrintQuality(pq)
                        .setOutputBin(ob)
                        .setStartPageRanges(startPageRanges)
                        .setEndPageRanges(endPageRanges)
                        .setFinishingsList(fo)
                        .build(mCaps);
            } catch (CapabilitiesExceededException e) {
                SLog.e(TAG, "Build failed " + e.getMessage());
                mCallback.onFailedCreateIntent(getString(R.string.print_creation_failed));
            } catch (IllegalArgumentException e) {
                SLog.e(TAG, "Build failed " + e.getMessage());
                mCallback.onFailedCreateIntent(getString(R.string.print_creation_failed));
            }
        } else if (source == Source.STREAM) {
            String filePath = prefs.getString(PrintConfigurationFragment.PREF_STREAM_FILENAME, "");
            try {
                // can print from any InputStream, here FileInputStream as example
                InputStream printStream = new FileInputStream(new File(filePath));

                // building with common print attributes set
                attrs = new PrintAttributes.PrintFromStreamBuilder(printStream)
                        .setCollateMode(collateMode)
                        .setColorMode(cm)
                        .setDuplex(duplex)
                        .setAutoFit(af)
                        .setStapleMode(sm)
                        .setPaperSource(psrc)
                        .setPaperSize(psz)
                        .setDocumentFormat(dfmt)
                        .setCopies(prefs.getInt(PrintConfigurationFragment.PREF_COPIES, 1))
                        .setOrientation(ot)
                        .setPrintQuality(pq)
                        .setOutputBin(ob)
                        .setStartPageRanges(startPageRanges)
                        .setEndPageRanges(endPageRanges)
                        .setFinishingsList(fo)
                        .build(mCaps);

                NetworkUtility.createServerSocket(rid, printStream);
            } catch (CapabilitiesExceededException e) {
                SLog.e(TAG, "Build failed " + e.getMessage());
                mCallback.onFailedCreateIntent(getString(R.string.print_creation_failed));
            } catch (FileNotFoundException e) {
                SLog.e(TAG, "Build failed " + e.getMessage());
                mCallback.onFailedCreateIntent(getString(R.string.print_creation_failed_empty_name));
            } catch (IllegalArgumentException e) {
                SLog.e(TAG, "Build failed " + e.getMessage());
                mCallback.onFailedCreateIntent(getString(R.string.print_creation_failed));
            }
        }

        if (attrs != null) {
            final PrintRequestIntent.IntentParams params =
                    new PrintRequestIntent.IntentParams(attrs,
                            new PrintletAttributes.Builder().build(), null, null, null, null);

            intent.putIntentParams(params);
        }

        return intent;
    }

    @Override
    public void onCapabilitiesLoaded(final PrintAttributesCaps caps, final String errorMsg) {
        if (caps == null || getActivity() == null) {
            mCallback.onFailed(errorMsg);
            return;
        }

        final Context context = getActivity().getApplicationContext();

        mCaps = caps;

        List<Source> sourceList = new ArrayList<>(caps.getSourceList());

        PreferenceHelper.fillValues(context,
                sourceList,
                mSourcePref,
                0);

        // Duplex
        PreferenceHelper.fillValues(context,
                caps.getDuplexList(),
                mDuplexPref,
                R.string.double_sided_pattern);

        // Color Mode
        PreferenceHelper.fillValues(context,
                caps.getColorModeList(),
                mCMPref,
                0);

        // AutoFit
        PreferenceHelper.fillValues(context,
                caps.getAutoFitList(),
                mAFPref,
                R.string.auto_fit_pattern);

        // Staple Mode
        PreferenceHelper.fillValues(context,
                caps.getStapleModeList(),
                mSMPref,
                0);

        // Collate Mode
        PreferenceHelper.fillValues(context,
                caps.getCollateModeList(),
                mCollatePref,
                0);

        // Paper Source
        PreferenceHelper.fillValues(context,
                caps.getPaperSourceList(),
                mPaperSrcPref,
                0);

        // Paper Size
        PreferenceHelper.fillValues(context,
                caps.getPaperSizeList(),
                mPaperSzPref,
                0);

        // Paper Size
        PreferenceHelper.fillValues(context,
                caps.getPaperTypeList(),
                mPaperTypePref,
                0);

        // Document format
        PreferenceHelper.fillValues(context,
                caps.getDocumentFormatList(),
                mDocFmtPref,
                0);

        // Orientation format
        PreferenceHelper.fillValues(context,
                caps.getOrientationList(),
                mOrientationPref,
                0);

        // Print Quality format
        PreferenceHelper.fillValues(context,
                caps.getPrintQualityList(),
                mPrintQualityPref,
                0);

        // OutputBin format
        PreferenceHelper.fillValues(context,
                caps.getOutputBinList(),
                mOutputBinPref,
                0);

        // Finishing format
        PreferenceHelper.fillValues(context,
                caps.getFinishingsList(),
                mFinishingsPref,
                0);

        // Apply Copies limits
        if (caps.getMaxCopies() > 0) {
            mCopiesPref.setLimits(1, caps.getMaxCopies());
        }

        getPreferenceScreen().setEnabled(true);

        final SharedPreferences prefs = getPreferenceScreen().getSharedPreferences();

        if (mSavedState != null) {
            mCMPref.setValue(mSavedState.getString(PREF_COLOR_MODE, ColorMode.DEFAULT.name()));
            mDuplexPref.setValue(mSavedState.getString(PREF_DUPLEX_MODE, Duplex.DEFAULT.name()));
            mAFPref.setValue(mSavedState.getString(PREF_AUTOFIT, AutoFit.DEFAULT.name()));
            mSMPref.setValue(mSavedState.getString(PREF_STAPLE_MODE, StapleMode.DEFAULT.name()));
            mPaperSrcPref.setValue(mSavedState.getString(PREF_PAPER_SOURCE, PaperSource.DEFAULT.name()));
            mPaperSzPref.setValue(mSavedState.getString(PREF_PAPER_SIZE, PaperSize.DEFAULT.name()));
            mPaperTypePref.setValue(mSavedState.getString(PREF_PAPER_TYPE, PaperType.DEFAULT.name()));
            mDocFmtPref.setValue(mSavedState.getString(PREF_DOCUMENT_FORMAT, DocumentFormat.AUTO.name()));
            mCopiesPref.setInteger(mSavedState.getInt(PREF_COPIES, 1));
            mCollatePref.setValue(mSavedState.getString(PREF_COLLATE_MODE, PrintAttributes.CollateMode.DEFAULT.name()));

            mOrientationPref.setValue(mSavedState.getString(PREF_ORIENTATION, PrintAttributes.Orientation.DEFAULT.name()));
            mPrintQualityPref.setValue(mSavedState.getString(PREF_PRINT_QUALITY, PrintAttributes.PrintQuality.DEFAULT.name()));
            mOutputBinPref.setValue(mSavedState.getString(PREF_OUTPUT_BIN, PrintAttributes.OutputBin.DEFAULT.name()));
            mStartPageRangesPref.setInteger(mSavedState.getInt(PREF_START_PAGE_RANGES, 0));
            mEndPageRangesPref.setInteger(mSavedState.getInt(PREF_END_PAGE_RANGES, 0));
            mFinishingsPref.setValues(new HashSet<>(mSavedState.getStringArrayList(PREF_FINISHINGS)));

            mFilePref.setSummary(prefs.getString(PREF_FILENAME, ""));
        }

        prefs.registerOnSharedPreferenceChangeListener(this);
        onSharedPreferenceChanged(prefs, PREF_SOURCE);
        onSharedPreferenceChanged(prefs, PREF_COLOR_MODE);
        onSharedPreferenceChanged(prefs, PREF_DUPLEX_MODE);
        onSharedPreferenceChanged(prefs, PREF_COPIES);
        onSharedPreferenceChanged(prefs, PREF_FILENAME);
        onSharedPreferenceChanged(prefs, PREF_AUTOFIT);
        onSharedPreferenceChanged(prefs, PREF_STAPLE_MODE);
        onSharedPreferenceChanged(prefs, PREF_COLLATE_MODE);
        onSharedPreferenceChanged(prefs, PREF_PAPER_SOURCE);
        onSharedPreferenceChanged(prefs, PREF_PAPER_SIZE);
        onSharedPreferenceChanged(prefs, PREF_PAPER_TYPE);
        onSharedPreferenceChanged(prefs, PREF_DOCUMENT_FORMAT);
        onSharedPreferenceChanged(prefs, PREF_URI);
        onSharedPreferenceChanged(prefs, PREF_URI_USERNAME);
        onSharedPreferenceChanged(prefs, PREF_URI_PASSWORD);

        onSharedPreferenceChanged(prefs, PREF_ORIENTATION);
        onSharedPreferenceChanged(prefs, PREF_PRINT_QUALITY);
        onSharedPreferenceChanged(prefs, PREF_OUTPUT_BIN);
        onSharedPreferenceChanged(prefs, PREF_START_PAGE_RANGES);
        onSharedPreferenceChanged(prefs, PREF_END_PAGE_RANGES);
        onSharedPreferenceChanged(prefs, PREF_FINISHINGS);

        if (mCallback != null && mCaps != null) {
            mCallback.enableActionButton(!TextUtils.isEmpty(prefs.getString(PREF_FILENAME, "")));
        }
    }

    @Override
    public void onDefaultCapabilitiesLoaded(PrintAttributes defaultCaps, String errorMsg) {

    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mCaps != null) {
            final SharedPreferences prefs = getPreferenceScreen().getSharedPreferences();

            // Save preference values to restore after caps get again
            outState.putString(PREF_COLOR_MODE, prefs.getString(PREF_COLOR_MODE, ColorMode.DEFAULT.name()));
            outState.putString(PREF_DUPLEX_MODE, prefs.getString(PREF_DUPLEX_MODE, Duplex.DEFAULT.name()));
            outState.putString(PREF_AUTOFIT, prefs.getString(PREF_AUTOFIT, AutoFit.DEFAULT.name()));
            outState.putString(PREF_STAPLE_MODE, prefs.getString(PREF_STAPLE_MODE, StapleMode.DEFAULT.name()));
            outState.putString(PREF_COLLATE_MODE, prefs.getString(PREF_COLLATE_MODE, PrintAttributes.CollateMode.DEFAULT.name()));
            outState.putString(PREF_PAPER_SOURCE, prefs.getString(PREF_PAPER_SOURCE, PaperSource.DEFAULT.name()));
            outState.putString(PREF_PAPER_SIZE, prefs.getString(PREF_PAPER_SIZE, PaperSize.DEFAULT.name()));
            outState.putString(PREF_PAPER_TYPE, prefs.getString(PREF_PAPER_TYPE, PaperType.DEFAULT.name()));
            outState.putString(PREF_DOCUMENT_FORMAT, prefs.getString(PREF_DOCUMENT_FORMAT, DocumentFormat.AUTO.name()));
            outState.putInt(PREF_COPIES, prefs.getInt(PREF_COPIES, 1));

            outState.putInt(PREF_START_PAGE_RANGES, prefs.getInt(PREF_START_PAGE_RANGES, 0));
            outState.putInt(PREF_END_PAGE_RANGES, prefs.getInt(PREF_END_PAGE_RANGES, 0));
        }
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSavedState = savedInstanceState;
    }


    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PRINT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            final Uri fileUri = data.getData();

            new FileLoadingAsyncTask(getActivity().getApplicationContext(), this).execute(fileUri);
        } else {
            SLog.d(TAG, "No file selected");
        }

        mResumedFromChoose = true;
    }

    /**
     * Callback from file loading task
     *
     * @param path to the file on file system
     * @param name of the file to be displayed
     */
    private void onLoaded(final String path, final String name) {
        final SharedPreferences prefs = getPreferenceScreen().getSharedPreferences();
        Source source = Source.valueOf(prefs.getString(PREF_SOURCE, Source.STORAGE.name()));

        if (source == Source.STORAGE) {
            // Populate file path and name
            mFilePref.getEditor().putString(PREF_FILENAME, name).apply();
            mFilePref.getEditor().putString(PREF_FILENAME_COMPLETE, path).apply();
            mFilePref.setSummary(name);
        } else if (source == Source.USB) {
            // Populate file path and name
            mUsbFilePref.getEditor().putString(PREF_USB_FILENAME, path).apply();
            mUsbFilePref.getEditor().putString(PREF_FILENAME_COMPLETE, path).apply();
            mUsbFilePref.setSummary(path);
        } else if (source == Source.STREAM) {
            mStreamFilenamePref.getEditor().putString(PREF_STREAM_FILENAME, path).apply();
            mStreamFilenamePref.getEditor().putString(PREF_FILENAME_COMPLETE, path).apply();
            mStreamFilenamePref.setSummary(name);
        }
    }

    private void fillUSBStorages() {
        ListPreference pref = (ListPreference) findPreference(PREF_USB_STORAGE);

        if (pref != null) {
            Result result = new Result();
            List<MassStorageInfo> storageList = MassStorageService.getStorageList(getActivity(), result);
            if (result.getCode() != Result.RESULT_OK || storageList == null) {
                storageList = Collections.emptyList();
            }

            final List<CharSequence> entries = new ArrayList<>();
            final List<CharSequence> entriesValues = new ArrayList<>();

            for (MassStorageInfo storage : storageList) {
                if (storage.getType() == MassStorageInfo.StorageType.USB && storage.isMounted()) {
                    entries.add(storage.getName());
                    entriesValues.add(storage.getExternalFileDirectory());
                }
            }

            pref.setEntries(entries.toArray(new CharSequence[entries.size()]));
            pref.setEntryValues(entriesValues.toArray(new CharSequence[entriesValues.size()]));

            if (!entries.isEmpty()) {
                String defaultValue = (String) entriesValues.get(0);

                pref.setDefaultValue(defaultValue);
                pref.setValueIndex(0);
                pref.setSummary("%s");
            }
        }
    }

    private void initPageRangePref(NumberPickerPreference pref, String key) {
        pref.setLimits(0, 9999);
        pref.getEditor().putInt(key, 0).apply();
        pref.setInteger(0);
    }

    private static final class FileLoadingAsyncTask extends AsyncTask<Uri, Void, String> {

        /**
         * Apps context
         */
        private final WeakReference<Context> mContextRef;
        /**
         * callback
         */
        private WeakReference<PrintConfigurationFragment> mCallbackRef;
        private String mDisplayName = "";

        /**
         * Default constructor
         *
         * @param context  to access system services
         * @param callback to report result
         */
        FileLoadingAsyncTask(final Context context, final PrintConfigurationFragment callback) {
            mContextRef = new WeakReference<>(context);
            mCallbackRef = new WeakReference<>(callback);
        }

        @Override
        @SuppressLint("Range")
        protected String doInBackground(final Uri... params) {
            final Uri fileUri = params[0];

            if (fileUri == null) {
                return null;
            }

            String path = FileUtility.getPath(mContextRef.get(), fileUri);

            if (TextUtils.isEmpty(path)) {
                // Seems it's not downloads and external storage files, try to create copy in cache
                InputStream contentsStream = null;
                OutputStream output = null;
                ContentResolver contentResolver = mContextRef.get().getContentResolver();
                final String mimeType = contentResolver.getType(fileUri);
                final String ext = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
                final Cursor cursor = contentResolver
                        .query(fileUri, null, null, null, null, null);

                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        // Note it's called "Display Name".  This is
                        // provider-specific, and might not necessarily be the file name.
                        mDisplayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }

                    cursor.close();
                }

                try {
                    final File file = new File(mContextRef.get().getFilesDir(), CACHE_FILE_NAME + "." + ext);
                    byte[] buffer = new byte[Platform.BUFFER_SIZE]; // or other buffer size
                    int read;

                    if (file.exists()) {
                        boolean deletedPreviousFile = file.delete();
                        SLog.d(TAG, "Removed previous file " + deletedPreviousFile);
                    }

                    contentsStream = new BufferedInputStream(contentResolver.openInputStream(fileUri));

                    if (contentsStream == null) {
                        return null;
                    }

                    output = new BufferedOutputStream(new FileOutputStream(file));
                    while ((read = contentsStream.read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                    }
                    output.flush();
                    path = file.getPath();
                } catch (FileNotFoundException e) {
                    SLog.e(TAG, "Not able to open selected file: ", e);
                } catch (IOException e) {
                    SLog.e(TAG, "Failed to get file: ", e);
                } finally {
                    if (output != null) {
                        try {
                            output.close();
                        } catch (IOException e) {
                            SLog.e(TAG, "Failed to close output file after copy: ", e);
                        }
                    }
                    if (contentsStream != null) {
                        try {
                            contentsStream.close();
                        } catch (IOException e) {
                            SLog.e(TAG, "Failed to close input file after copy: ", e);
                        }
                    }
                }
            }

            if (path != null && TextUtils.isEmpty(mDisplayName)) {
                mDisplayName = new File(path).getName();
            }

            return path;
        }

        @Override
        protected void onPostExecute(final String path) {
            super.onPostExecute(path);

            if (mCallbackRef.get() != null) {
                mCallbackRef.get().onLoaded(path, mDisplayName);
            }
        }
    }
}
