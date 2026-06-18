// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.settingsui.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.Toast;

import com.hp.jetadvantage.link.api.CapabilitiesExceededException;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.massstorage.MassStorageInfo;
import com.hp.jetadvantage.link.api.massstorage.MassStorageService;
import com.hp.jetadvantage.link.api.scanner.EmailAttributes;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributes;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributesCaps;
import com.hp.jetadvantage.link.api.scanner.Margins;
import com.hp.jetadvantage.link.api.scanner.NetworkCredentialsAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.ColorMode;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.Destination;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.DocumentFormat;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.Duplex;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.Orientation;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.Resolution;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.ScanPreview;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes.ScanSize;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesCaps;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesReader;
import com.hp.jetadvantage.link.api.scanner.ScanletAttributes;
import com.hp.jetadvantage.link.api.scanner.ScannerService;
import com.hp.jetadvantage.link.api.scanner.SmtpAttributes;
import com.hp.jetadvantage.link.api.scanner.intent.ScanToRequestIntent;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.settingsui.R;
import com.hp.jetadvantage.link.services.settingsui.SettingsUIActivity;
import com.hp.jetadvantage.link.services.settingsui.helper.PreferenceHelper;
import com.hp.jetadvantage.link.services.settingsui.interfaces.ConfigurationCallback;
import com.hp.jetadvantage.link.services.settingsui.interfaces.ConfigurationFragment;
import com.hp.jetadvantage.link.services.settingsui.preferences.CustomSizePreference;
import com.hp.jetadvantage.link.services.settingsui.preferences.IconsListPreference;
import com.hp.jetadvantage.link.services.settingsui.preferences.LongSummaryPreference;
import com.hp.jetadvantage.link.services.settingsui.preferences.MarginsPreference;
import com.hp.jetadvantage.link.services.settingsui.preferences.NumberPickerPreference;
import com.hp.jetadvantage.link.services.settingsui.task.CapabilitiesAsyncTask;
import com.hp.jetadvantage.link.services.settingsui.task.DefaultCapabilitiesAsyncTask;
import com.hp.jetadvantage.link.services.settingsui.task.FileOptionAsyncTask;
import com.ipaulpro.afilechooser.FileChooserActivity;
import com.ipaulpro.afilechooser.utils.FileUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
 * Simple {@link PreferenceFragment} to set Scan Attributes and save into preferences.
 */
public final class ScanConfigurationFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener, ConfigurationFragment<ScanAttributesCaps, ScanAttributes> {
    private static final String TAG = SettingsUIActivity.TAG;
    private static final int SCAN_REQUEST_CODE = 11;

    public static final String PREF_DESTINATION = "pref_destination";
    public static final String PREF_FILENAME = "pref_filename";
    public static final String PREF_FOLDER_NAME = "pref_folder_name";

    // Preferences keys for ScanAttributes
    public static final String PREF_COLOR_MODE = "pref_colorMode";
    public static final String PREF_DUPLEX_MODE = "pref_duplexMode";
    public static final String PREF_RESOLUTION_TYPE = "pref_resolutionType";
    public static final String PREF_DOC_FORMAT = "pref_docFormat";
    public static final String PREF_ORIGINAL_SIZE = "pref_originalSize";
    public static final String PREF_CUSTOM_LENGTH = "pref_customLength";
    public static final String PREF_CUSTOM_WIDTH = "pref_customWidth";
    public static final String PREF_ORIGINAL_ORIENTATION = "pref_originalOrientation";
    public static final String PREF_SCAN_PREVIEW = "pref_scanPreview";
    public static final String PREF_BACKGROUND_CLEANUP = "pref_backgroundCleanup";
    public static final String PREF_CONTRAST_ADJUSTMENT = "pref_contrastAdjustment";
    public static final String PREF_DARKNESS_ADJUSTMENT = "pref_darknessAdjustment";
    public static final String PREF_BLANK_IMAGE_REMOVAL_MODE = "pref_blankImageRemovalMode";
    public static final String PREF_COLOR_DROPOUT_MODE = "pref_colorDropoutMode";
    public static final String PREF_CROP_MODE = "pref_cropMode";
    public static final String PREF_PROGRESS_DIALOG_MODE = "pref_progressDialogMode";
    public static final String PREF_OUTPUT_QUALITY = "pref_outputQuality";
    public static final String PREF_TRANSMISSION_MODE = "pref_transmissionMode";
    public static final String PREF_JOB_ASSEMBLY_MODE = "pref_jobAssemblyMode";
    public static final String PREF_SHARPNESS_ADJUSTMENT = "pref_sharpnessAdjustment";
    public static final String PREF_MEDIA_WEIGHT_ADJUSTMENT = "pref_mediaWeightAdjustment";
    public static final String PREF_TEXT_PHOTO_OPTIMIZATION = "pref_textPhotoOptimization";

    public static final String PREF_SPLIT_ATTACHMENT_BY_PAGE = "pref_scan_splitAttachmentByPage";
    public static final String PREF_MAX_PAGES_PER_ATTACHMENT = "pref_scan_maxPagesPerAttachment";
    public static final String PREF_ERASE_MARGIN_UNIT = "pref_scan_eraseMarginUnit";
    public static final String PREF_ERASE_BACK_MARGIN = "pref_scan_eraseBackMargin";
    public static final String PREF_ERASE_BACK_LEFT_MARGIN = "pref_scan_eraseBackMarginLeft";
    public static final String PREF_ERASE_BACK_TOP_MARGIN = "pref_scan_eraseBackMarginTop";
    public static final String PREF_ERASE_BACK_RIGHT_MARGIN = "pref_scan_eraseBackMarginRight";
    public static final String PREF_ERASE_BACK_BOTTOM_MARGIN = "pref_scan_eraseBackMarginBottom";
    public static final String PREF_ERASE_FRONT_MARGIN = "pref_scan_eraseFrontMargin";
    public static final String PREF_ERASE_FRONT_LEFT_MARGIN = "pref_scan_eraseFrontMarginLeft";
    public static final String PREF_ERASE_FRONT_TOP_MARGIN = "pref_scan_eraseFrontMarginTop";
    public static final String PREF_ERASE_FRONT_RIGHT_MARGIN = "pref_scan_eraseFrontMarginRight";
    public static final String PREF_ERASE_FRONT_BOTTOM_MARGIN = "pref_scan_eraseFrontMarginBottom";
    public static final String PREF_CAPTURE_MODE = "pref_scan_captureMode";
    public static final String PREF_AUTOMATIC_TONE_MODE = "pref_scan_automaticToneMode";
    public static final String PREF_AUTOMATIC_STRAIGHTEN_MODE = "pref_scan_automaticStraightenMode";

    public static final String PREF_MEDIA_SOURCE = "pref_mediaSource";
    public static final String PREF_MISFEED_DETECTION_MODE = "pref_misfeedDetectionMode";
    public static final String PREF_PDF_COMPRESSION = "pref_pdf_compression";
    public static final String PREF_OCR_LANGUAGE = "pref_ocr_language";
    public static final String PREF_PDF_PASSWORD = "pref_pdf_password";
    public static final String PREF_TIFF_COMPRESSION = "pref_tiff_compression";
    public static final String PREF_XPS_COMPRESSION = "pref_xps_compression";

    public static final String PREF_URI = "pref_uri";
    public static final String PREF_URI_USERNAME = "pref_uri_username";
    public static final String PREF_URI_PASSWORD = "pref_uri_password";
    public static final String PREF_URI_DOMAIN = "pref_uri_domain";

    public static final String PREF_EMAIL_TO = "pref_email_to";
    public static final String PREF_EMAIL_CC = "pref_email_cc";
    public static final String PREF_EMAIL_BCC = "pref_email_bcc";
    public static final String PREF_EMAIL_FROM = "pref_email_from";
    public static final String PREF_EMAIL_SUBJECT = "pref_email_subject";
    public static final String PREF_EMAIL_MESSAGE = "pref_email_message";
    public static final String PREF_EMAIL_SMTP = "pref_email_smtp";

    public static final String PREF_USB_STORAGE = "pref_usb_storage";

    public static final String PREF_DESTINATION_HTTP_CATEGORY = "destination_http_category";
    public static final String PREF_DESTINATION_FTP_CATEGORY = "destination_ftp_category";
    public static final String PREF_DESTINATION_NETWORK_FOLDER_CATEGORY = "destination_network_folder_category";
    public static final String PREF_DESTINATION_EMAIL_CATEGORY = "destination_email_category";
    public static final String PREF_DESTINATION_USB_CATEGORY = "destination_usb_category";
    public static final String PREF_BASE_ATTRIBUTES_CATEGORY = "pref_generalCategory";

    private static final String ARG_DESTINATION = "destination";

    /**
     * Task for loading caps, keep reference to be able to cancel
     */
    private AsyncTask<Void, Void, Void> mCapsLoadingTask;
    /**
     * Task for loading default caps, keep reference to be able to cancel
     */
    private AsyncTask<Void, Void, Void> mDefaultCapsLoadingTask;
    /**
     * Destination the fragment launched for
     */
    private ScanAttributes.Destination mDestination = ScanAttributes.Destination.ME;
    /**
     * Cached caps
     */
    private ScanAttributesCaps mCaps = null;
    /**
     * Cached default caps
     */
    private ScanAttributes mDefaultCaps = null;
    /**
     * Callback interface for errors handling on parents side
     */
    private ConfigurationCallback mCallback;
    /**
     * Saved fragment state with current settings
     */
    private Bundle mSavedState;

    private EditTextPreference mFileUriPref;
    private EditTextPreference mFileUriUsernamePref;
    private EditTextPreference mFileUriPasswordPref;
    private EditTextPreference mFileUriDomainPref;
    private EditTextPreference mEmailToPref;
    private EditTextPreference mEmailCcPref;
    private EditTextPreference mEmailBccPref;
    private CheckBoxPreference mEmailSmtpPref;

    private IconsListPreference mTransmissionPref;
    private IconsListPreference mPDFCompressionPref;
    private IconsListPreference mOCRLanguagePref;
    private EditTextPreference mPDFPasswordPref;
    private IconsListPreference mTIFFCompressionPref;
    private IconsListPreference mXPSCompressionPref;

    private IconsListPreference mSplitAttachmentByPagePref;
    private NumberPickerPreference mMaxPagesPerAttachmentPref;
    private IconsListPreference mEraseMarginUnit;
    private MarginsPreference mEraseBackMarginPref;
    private MarginsPreference mEraseFrontMarginPref;
    private IconsListPreference mCaptureMode;
    private IconsListPreference mAutomaticToneMode;
    private IconsListPreference mAutomaticStraightenMode;

    private PreferenceCategory mHttpCategory;
    private PreferenceCategory mFtpCategory;
    private PreferenceCategory mNetworkFolderCategory;
    private PreferenceCategory mEmailCategory;
    private PreferenceCategory mUsbCategory;
    private PreferenceCategory mBaseAttributesCategory;

    private CustomSizePreference mCustomLengthPref;
    private CustomSizePreference mCustomWidthPref;

    private LongSummaryPreference mFoldernamePref;

    private int mClientApiLevel;

    /**
     * Default factory
     *
     * @param dest destination to be used in configuration
     * @return new instance
     */
    public static ScanConfigurationFragment newInstance(final ScanAttributes attrs, final ScanAttributes.Destination dest, int clientApiLevel) {
        final Bundle args = new Bundle();
        final ScanConfigurationFragment frag = new ScanConfigurationFragment();

        args.putString(ARG_DESTINATION, dest.name());
        args.putParcelable(ARG_ATTRS, attrs);
        args.putInt(ARG_CLIENT_API_LEVEL, clientApiLevel);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();

        if (args != null) {
            mDestination = ScanAttributes.Destination
                    .valueOf(args.getString(ARG_DESTINATION, ScanAttributes.Destination.ME.name()));
            mClientApiLevel = args.getInt(ARG_CLIENT_API_LEVEL, Sdk.VERSION.LEVEL);
        }

        addPreferencesFromResource(R.xml.scan_preferences);

        clearUriValues();

        mHttpCategory = (PreferenceCategory) findPreference(PREF_DESTINATION_HTTP_CATEGORY);
        mFtpCategory = (PreferenceCategory) findPreference(PREF_DESTINATION_FTP_CATEGORY);
        mNetworkFolderCategory = (PreferenceCategory) findPreference(PREF_DESTINATION_NETWORK_FOLDER_CATEGORY);
        mEmailCategory = (PreferenceCategory) findPreference(PREF_DESTINATION_EMAIL_CATEGORY);
        mUsbCategory = (PreferenceCategory) findPreference(PREF_DESTINATION_USB_CATEGORY);
        mBaseAttributesCategory = (PreferenceCategory) findPreference(PREF_BASE_ATTRIBUTES_CATEGORY);

        mEmailToPref = (EditTextPreference) findPreference(PREF_EMAIL_TO);
        mEmailCcPref = (EditTextPreference) findPreference(PREF_EMAIL_CC);
        mEmailBccPref = (EditTextPreference) findPreference(PREF_EMAIL_BCC);
        mEmailSmtpPref = (CheckBoxPreference) findPreference(PREF_EMAIL_SMTP);
        if (mEmailSmtpPref != null) {
            mEmailSmtpPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if ((boolean) newValue) {
                        FragmentManager fm = getFragmentManager();
                        new EmailSmtpSettingFragment().show(fm, getString(R.string.pref_email_smtp_title));
                    }
                    return true;
                }
            });
        }

        mTransmissionPref = (IconsListPreference) findPreference(PREF_TRANSMISSION_MODE);
        mPDFCompressionPref = (IconsListPreference) findPreference(PREF_PDF_COMPRESSION);
        mOCRLanguagePref = (IconsListPreference) findPreference(PREF_OCR_LANGUAGE);
        mPDFPasswordPref = (EditTextPreference) findPreference(PREF_PDF_PASSWORD);
        mTIFFCompressionPref = (IconsListPreference) findPreference(PREF_TIFF_COMPRESSION);
        mXPSCompressionPref = (IconsListPreference) findPreference(PREF_XPS_COMPRESSION);

        mCustomLengthPref = (CustomSizePreference) findPreference(PREF_CUSTOM_LENGTH);
        mCustomLengthPref.setLimits(0, 0);
        mCustomWidthPref = (CustomSizePreference) findPreference(PREF_CUSTOM_WIDTH);
        mCustomWidthPref.setLimits(0, 0);

        mFoldernamePref = (LongSummaryPreference) findPreference(PREF_FOLDER_NAME);
        mFoldernamePref.setSummary(getString(R.string.pref_folder_name));
        mFoldernamePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                startFileChooser();
                return true;
            }
        });

        mSplitAttachmentByPagePref = (IconsListPreference) findPreference(PREF_SPLIT_ATTACHMENT_BY_PAGE);
        mMaxPagesPerAttachmentPref = (NumberPickerPreference) findPreference(PREF_MAX_PAGES_PER_ATTACHMENT);
        mMaxPagesPerAttachmentPref.setLimits(1, 1);
        mMaxPagesPerAttachmentPref.setInteger(1);
        mEraseMarginUnit = (IconsListPreference) findPreference(PREF_ERASE_MARGIN_UNIT);
        mEraseBackMarginPref = (MarginsPreference) findPreference(PREF_ERASE_BACK_MARGIN);
        mEraseBackMarginPref.clean();
        mEraseFrontMarginPref = (MarginsPreference) findPreference(PREF_ERASE_FRONT_MARGIN);
        mEraseFrontMarginPref.clean();
        mCaptureMode = (IconsListPreference) findPreference(PREF_CAPTURE_MODE);
        mAutomaticToneMode = (IconsListPreference) findPreference(PREF_AUTOMATIC_TONE_MODE);
        mAutomaticStraightenMode = (IconsListPreference) findPreference(PREF_AUTOMATIC_STRAIGHTEN_MODE);

        getPreferenceScreen().removePreference(mHttpCategory);
        getPreferenceScreen().removePreference(mFtpCategory);
        getPreferenceScreen().removePreference(mNetworkFolderCategory);
        getPreferenceScreen().removePreference(mEmailCategory);
        getPreferenceScreen().removePreference(mUsbCategory);

        clearFilename();
        applyDestination();
    }

    private void startFileChooser() {
        Intent intent = new Intent(getActivity(), FileChooserActivity.class);
        intent.putExtra(FileUtils.INTENT_SELECT, FileUtils.FOLDER);

        if (mDestination == Destination.USB) {
            final SharedPreferences prefs = getPreferenceScreen().getSharedPreferences();
            final String usbRootFolder = prefs.getString(ScanConfigurationFragment.PREF_USB_STORAGE, "");

            intent.putExtra(FileUtils.PATH, usbRootFolder);
        }

        startActivityForResult(intent, SCAN_REQUEST_CODE);
    }

    private void clearUriValues() {
        mFileUriPref = (EditTextPreference) findPreference(PREF_URI);
        mFileUriPref.setText("");
        mFileUriUsernamePref = (EditTextPreference) findPreference(PREF_URI_USERNAME);
        mFileUriUsernamePref.setText("");
        mFileUriPasswordPref = (EditTextPreference) findPreference(PREF_URI_PASSWORD);
        mFileUriPasswordPref.setText("");
        mFileUriDomainPref = (EditTextPreference) findPreference(PREF_URI_DOMAIN);
        if (mFileUriDomainPref != null) { // domain is not always displayed
            mFileUriDomainPref.setText("");
        }
    }

    private void clearFilename() {
        EditTextPreference fileNamePref = (EditTextPreference) findPreference(PREF_FILENAME);
        fileNamePref.getEditor().putString(PREF_FILENAME, "").apply();
        fileNamePref.setText(null);
        LongSummaryPreference folderNamePref = (LongSummaryPreference) findPreference(PREF_FOLDER_NAME);
        folderNamePref.getEditor().putString(PREF_FOLDER_NAME, "").apply();
        folderNamePref.setSummary(null);
    }

    /**
     * Applies destination and modifies settings categories accordingly
     */
    private void applyDestination() {
        boolean enableButton;
        switch (mDestination) {
            case ME:
            case USB:
                if (mCallback != null && mCaps != null) {
                    mCallback.enableActionButton(true);
                }
                break;

            case HTTP:
            case FTP:
            case NETWORK_FOLDER:
                enableButton = mCallback != null;
                try {
                    ScanAttributes.validateUri(Uri.parse(mFileUriPref.getText()));
                } catch (Exception e) {
                    enableButton = false;
                }

                int titleColor = Color.BLACK;
                if (!enableButton) {
                    titleColor = Color.RED;
                }
                Spannable title = new SpannableString(mFileUriPref.getTitle().toString());
                title.setSpan(new ForegroundColorSpan(titleColor), 0, title.length(), 0);
                mFileUriPref.setTitle(title);

                if (mCallback != null) {
                    mCallback.enableActionButton(enableButton);
                }

                break;

            case EMAIL:
                if (mEmailToPref != null
                        && mEmailCcPref != null
                        && mEmailBccPref != null) {
                    enableButton = !TextUtils.isEmpty(mEmailToPref.getText())
                            || !TextUtils.isEmpty(mEmailCcPref.getText())
                            || !TextUtils.isEmpty(mEmailBccPref.getText());

                    if (mCallback != null) {
                        mCallback.enableActionButton(enableButton);
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Destination is not presented!");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        final SharedPreferences prefs = getPreferenceScreen().getSharedPreferences();

        // Disable screen until caps are loaded
        prefs.registerOnSharedPreferenceChangeListener(this);

        if (mCaps == null) {
            getPreferenceScreen().setEnabled(false);
            mCapsLoadingTask = new CapabilitiesAsyncTask<>(getActivity().getApplicationContext(),
                    this, SettingsUIActivity.Operations.SCAN_TO_ME, mClientApiLevel, ScanAttributesCaps.class).execute();
        }

        if (mDefaultCaps == null) {
            getPreferenceScreen().setEnabled(false);
            mDefaultCapsLoadingTask = new DefaultCapabilitiesAsyncTask<>(getActivity().getApplicationContext(),
                    this, SettingsUIActivity.Operations.SCAN_TO_ME, ScanAttributesCaps.class).execute();
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

    /**
     * Refreshes al values in the fragment
     *
     * @param prefs {@link SharedPreferences}
     */
    void refreshAllPrefs(final SharedPreferences prefs) {
        onSharedPreferenceChanged(prefs, PREF_DESTINATION);

        onSharedPreferenceChanged(prefs, PREF_FILENAME);
        onSharedPreferenceChanged(prefs, PREF_FOLDER_NAME);
        onSharedPreferenceChanged(prefs, PREF_COLOR_MODE);
        onSharedPreferenceChanged(prefs, PREF_DUPLEX_MODE);
        onSharedPreferenceChanged(prefs, PREF_RESOLUTION_TYPE);
        onSharedPreferenceChanged(prefs, PREF_DOC_FORMAT);
        onSharedPreferenceChanged(prefs, PREF_SCAN_PREVIEW);
        onSharedPreferenceChanged(prefs, PREF_ORIGINAL_SIZE);
        onSharedPreferenceChanged(prefs, PREF_CUSTOM_LENGTH);
        onSharedPreferenceChanged(prefs, PREF_CUSTOM_WIDTH);
        onSharedPreferenceChanged(prefs, PREF_ORIGINAL_ORIENTATION);

        onSharedPreferenceChanged(prefs, PREF_BACKGROUND_CLEANUP);
        onSharedPreferenceChanged(prefs, PREF_CONTRAST_ADJUSTMENT);
        onSharedPreferenceChanged(prefs, PREF_DARKNESS_ADJUSTMENT);
        onSharedPreferenceChanged(prefs, PREF_BLANK_IMAGE_REMOVAL_MODE);
        onSharedPreferenceChanged(prefs, PREF_COLOR_DROPOUT_MODE);
        onSharedPreferenceChanged(prefs, PREF_CROP_MODE);
        onSharedPreferenceChanged(prefs, PREF_PROGRESS_DIALOG_MODE);
        onSharedPreferenceChanged(prefs, PREF_OUTPUT_QUALITY);
        onSharedPreferenceChanged(prefs, PREF_TRANSMISSION_MODE);
        onSharedPreferenceChanged(prefs, PREF_JOB_ASSEMBLY_MODE);
        onSharedPreferenceChanged(prefs, PREF_SHARPNESS_ADJUSTMENT);
        onSharedPreferenceChanged(prefs, PREF_MEDIA_WEIGHT_ADJUSTMENT);
        onSharedPreferenceChanged(prefs, PREF_TEXT_PHOTO_OPTIMIZATION);
        onSharedPreferenceChanged(prefs, PREF_MEDIA_SOURCE);
        onSharedPreferenceChanged(prefs, PREF_MISFEED_DETECTION_MODE);

        onSharedPreferenceChanged(prefs, PREF_SPLIT_ATTACHMENT_BY_PAGE);
        onSharedPreferenceChanged(prefs, PREF_MAX_PAGES_PER_ATTACHMENT);
        onSharedPreferenceChanged(prefs, PREF_ERASE_MARGIN_UNIT);
        onSharedPreferenceChanged(prefs, PREF_CAPTURE_MODE);
        onSharedPreferenceChanged(prefs, PREF_AUTOMATIC_TONE_MODE);
        onSharedPreferenceChanged(prefs, PREF_AUTOMATIC_STRAIGHTEN_MODE);

        onSharedPreferenceChanged(prefs, PREF_URI);
        onSharedPreferenceChanged(prefs, PREF_URI_USERNAME);
        onSharedPreferenceChanged(prefs, PREF_URI_PASSWORD);

        onSharedPreferenceChanged(prefs, PREF_EMAIL_TO);
        onSharedPreferenceChanged(prefs, PREF_EMAIL_CC);
        onSharedPreferenceChanged(prefs, PREF_EMAIL_BCC);
        onSharedPreferenceChanged(prefs, PREF_EMAIL_FROM);
        onSharedPreferenceChanged(prefs, PREF_EMAIL_SUBJECT);
        onSharedPreferenceChanged(prefs, PREF_EMAIL_MESSAGE);
        onSharedPreferenceChanged(prefs, PREF_EMAIL_SMTP);

        onSharedPreferenceChanged(prefs, PREF_PDF_COMPRESSION);
        onSharedPreferenceChanged(prefs, PREF_OCR_LANGUAGE);
        onSharedPreferenceChanged(prefs, PREF_PDF_PASSWORD);
        onSharedPreferenceChanged(prefs, PREF_TIFF_COMPRESSION);
        onSharedPreferenceChanged(prefs, PREF_XPS_COMPRESSION);
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
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        final Preference preference = findPreference(key);

        if (preference == null) {
            return;
        }

        if (PREF_DOC_FORMAT.equals(key)) {
            setColorMode(sharedPreferences, key);
            showOptionalPreference(sharedPreferences, key);
            getFileOptionAttrCaps(key);
            return;
        } else if (PREF_ORIGINAL_SIZE.equals(key)) {
            showCustomSizePreference(sharedPreferences, key);
        } else if (PREF_COLOR_MODE.equals(key)) {
            getFileOptionAttrCaps(key);
            return;
        } else if (PREF_DESTINATION.equals(key)) {
            final String entryStr = ((ListPreference) preference).getValue();
            final Destination entry =
                    entryStr == null ? Destination.ME : Destination.valueOf(entryStr);
            fillDocFormatPreferences(mCaps, PREF_DOC_FORMAT, entry);

            if (entry == Destination.HTTP) {
                if (getPreferenceScreen().findPreference(PREF_DESTINATION_HTTP_CATEGORY) == null) {
                    getPreferenceScreen().addPreference(mHttpCategory);
                }

                getPreferenceScreen().removePreference(mFtpCategory);
                getPreferenceScreen().removePreference(mNetworkFolderCategory);
                getPreferenceScreen().removePreference(mEmailCategory);
                getPreferenceScreen().removePreference(mUsbCategory);

                clearUriValues();

                onSharedPreferenceChanged(sharedPreferences, PREF_URI);
                onSharedPreferenceChanged(sharedPreferences, PREF_URI_USERNAME);
                onSharedPreferenceChanged(sharedPreferences, PREF_URI_PASSWORD);
                supportTransmissionModeCaps(true);
                mBaseAttributesCategory.removePreference(mFoldernamePref);

            } else if (entry == Destination.FTP) {
                if (getPreferenceScreen().findPreference(PREF_DESTINATION_FTP_CATEGORY) == null) {
                    getPreferenceScreen().addPreference(mFtpCategory);
                }

                getPreferenceScreen().removePreference(mHttpCategory);
                getPreferenceScreen().removePreference(mNetworkFolderCategory);
                getPreferenceScreen().removePreference(mEmailCategory);
                getPreferenceScreen().removePreference(mUsbCategory);

                clearUriValues();

                onSharedPreferenceChanged(sharedPreferences, PREF_URI);
                onSharedPreferenceChanged(sharedPreferences, PREF_URI_USERNAME);
                onSharedPreferenceChanged(sharedPreferences, PREF_URI_PASSWORD);
                supportTransmissionModeCaps(false);
                mBaseAttributesCategory.removePreference(mFoldernamePref);

            } else if (entry == Destination.NETWORK_FOLDER) {
                if (getPreferenceScreen().findPreference(PREF_DESTINATION_NETWORK_FOLDER_CATEGORY) == null) {
                    getPreferenceScreen().addPreference(mNetworkFolderCategory);
                }

                getPreferenceScreen().removePreference(mHttpCategory);
                getPreferenceScreen().removePreference(mFtpCategory);
                getPreferenceScreen().removePreference(mEmailCategory);
                getPreferenceScreen().removePreference(mUsbCategory);

                clearUriValues();

                onSharedPreferenceChanged(sharedPreferences, PREF_URI);
                onSharedPreferenceChanged(sharedPreferences, PREF_URI_USERNAME);
                onSharedPreferenceChanged(sharedPreferences, PREF_URI_PASSWORD);
                onSharedPreferenceChanged(sharedPreferences, PREF_URI_DOMAIN);
                supportTransmissionModeCaps(false);
                mBaseAttributesCategory.removePreference(mFoldernamePref);

            } else if (entry == Destination.EMAIL) {
                if (getPreferenceScreen().findPreference(PREF_DESTINATION_EMAIL_CATEGORY) == null) {
                    getPreferenceScreen().addPreference(mEmailCategory);
                }

                getPreferenceScreen().removePreference(mHttpCategory);
                getPreferenceScreen().removePreference(mFtpCategory);
                getPreferenceScreen().removePreference(mNetworkFolderCategory);
                getPreferenceScreen().removePreference(mUsbCategory);

                onSharedPreferenceChanged(sharedPreferences, PREF_EMAIL_TO);
                onSharedPreferenceChanged(sharedPreferences, PREF_EMAIL_CC);
                onSharedPreferenceChanged(sharedPreferences, PREF_EMAIL_BCC);
                onSharedPreferenceChanged(sharedPreferences, PREF_EMAIL_FROM);
                onSharedPreferenceChanged(sharedPreferences, PREF_EMAIL_SUBJECT);
                onSharedPreferenceChanged(sharedPreferences, PREF_EMAIL_MESSAGE);
                onSharedPreferenceChanged(sharedPreferences, PREF_EMAIL_SMTP);
                supportTransmissionModeCaps(false);
                mBaseAttributesCategory.removePreference(mFoldernamePref);

            } else if (entry == Destination.USB) {
                if (getPreferenceScreen().findPreference(PREF_DESTINATION_USB_CATEGORY) == null) {
                    getPreferenceScreen().addPreference(mUsbCategory);
                }

                getPreferenceScreen().removePreference(mHttpCategory);
                getPreferenceScreen().removePreference(mFtpCategory);
                getPreferenceScreen().removePreference(mNetworkFolderCategory);
                getPreferenceScreen().removePreference(mEmailCategory);
                supportTransmissionModeCaps(false);

                if (getPreferenceScreen().findPreference(PREF_FOLDER_NAME) == null) {
                    mBaseAttributesCategory.addPreference(mFoldernamePref);
                }

                fillUSBStorages();

                onSharedPreferenceChanged(sharedPreferences, PREF_USB_STORAGE);
            } else {
                getPreferenceScreen().removePreference(mHttpCategory);
                getPreferenceScreen().removePreference(mFtpCategory);
                getPreferenceScreen().removePreference(mNetworkFolderCategory);
                getPreferenceScreen().removePreference(mEmailCategory);
                getPreferenceScreen().removePreference(mUsbCategory);
                supportTransmissionModeCaps(false);

                if (mClientApiLevel >= Sdk.VERSION_LEVEL.FOUR) {
                    mBaseAttributesCategory.removePreference(mFoldernamePref);
                } else {
                    if (getPreferenceScreen().findPreference(PREF_FOLDER_NAME) == null) {
                        mBaseAttributesCategory.addPreference(mFoldernamePref);
                    }
                }

                mFoldernamePref.getEditor().putString(PREF_FOLDER_NAME, "").apply();

                onSharedPreferenceChanged(sharedPreferences, PREF_FOLDER_NAME);
            }
        } else if (PREF_USB_STORAGE.equals(key)) {
            final String usbName = ((ListPreference) preference).getValue();
            mFoldernamePref.getEditor().putString(PREF_FOLDER_NAME, usbName).apply();

            onSharedPreferenceChanged(sharedPreferences, PREF_FOLDER_NAME);
        }

        if (mClientApiLevel < Sdk.VERSION_LEVEL.SIX) {
            mBaseAttributesCategory.removePreference(mSplitAttachmentByPagePref);
            mBaseAttributesCategory.removePreference(mMaxPagesPerAttachmentPref);
            mBaseAttributesCategory.removePreference(mEraseMarginUnit);
            mBaseAttributesCategory.removePreference(mEraseBackMarginPref);
            mBaseAttributesCategory.removePreference(mEraseFrontMarginPref);
            mBaseAttributesCategory.removePreference(mCaptureMode);
            mBaseAttributesCategory.removePreference(mAutomaticToneMode);
            mBaseAttributesCategory.removePreference(mAutomaticStraightenMode);
        }


        setPreferenceValue(key);
    }

    public void setPreferenceValue(String key) {
        final Preference preference = findPreference(key);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        if (preference instanceof ListPreference) {
            final ListPreference listPref = (ListPreference) preference;
            final String entry = (String) listPref.getEntry();

            if (entry == null || entry.length() == 0) {
                listPref.setValueIndex(0);
                preference.setSummary("%s");
            } else {
                preference.setSummary(entry);
            }

            if (PREF_DESTINATION.equals(key)) {
                mDestination = ScanAttributes.Destination
                        .valueOf(listPref.getValue());
                applyDestination();
            }
        } else if (preference instanceof EditTextPreference) {
            String text = ((EditTextPreference) preference).getText();
            EditText editText = ((EditTextPreference) preference).getEditText();

            if (!PREF_URI_PASSWORD.equals(key) &&
                    !PREF_PDF_PASSWORD.equals(key)) {
                editText.setSingleLine(true);
            }

            if (TextUtils.isEmpty(text)) {
                if (PREF_URI.equals(key)) {
                    preference.setSummary(R.string.hint_uri);
                } else if (PREF_URI_USERNAME.equals(key)) {
                    preference.setSummary(R.string.hint_uri_username);
                } else if (PREF_URI_PASSWORD.equals(key)) {
                    preference.setSummary(R.string.hint_uri_password);
                } else if (PREF_URI_DOMAIN.equals(key)) {
                    preference.setSummary(R.string.hint_uri_domain);
                } else {
                    preference.setSummary(text);
                }
            } else {
                if (editText.getTransformationMethod() != null) {
                    text = editText.getTransformationMethod().getTransformation(text, editText).toString();
                }
                preference.setSummary(text);
            }

            if (PREF_URI.equals(key)) {
                applyDestination();  // revalidate
            } else if (PREF_FILENAME.equals(key)) {
                final String fileName = sharedPreferences.getString(PREF_FILENAME, "");

                if (TextUtils.isEmpty(fileName)) {
                    // Show special summary for file name
                    preference.setSummary(R.string.pref_attachment_name);
                } else {
                    preference.setSummary(fileName);
                }
            } else if (PREF_EMAIL_TO.equals(key) || PREF_EMAIL_CC.equals(key) || PREF_EMAIL_BCC.equals(key)
                    || PREF_EMAIL_FROM.equals(key) || PREF_EMAIL_MESSAGE.equals(key) || PREF_EMAIL_SUBJECT.equals(key)) {
                final String value = sharedPreferences.getString(key, "");
                if (TextUtils.isEmpty(value)) {
                    preference.setSummary(editText.getHint());
                }
                applyDestination();  // revalidate
            }
        } else if (preference instanceof CheckBoxPreference) {
            if (PREF_EMAIL_SMTP.equals(key)) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                boolean value = sharedPref.getBoolean(key, false);
                ((CheckBoxPreference) preference).setChecked(value);
            }
        } else if (preference instanceof LongSummaryPreference) {
            if (PREF_FOLDER_NAME.equals(key)) {
                final String folderName = sharedPreferences.getString(PREF_FOLDER_NAME, "");

                if (TextUtils.isEmpty(folderName)) {
                    // Show special summary for folder name
                    preference.setSummary(R.string.pref_attachment_folder_name);
                } else {
                    preference.setSummary(folderName);
                }
            }
        } else if (preference instanceof NumberPickerPreference) {
            final Integer val = ((NumberPickerPreference) preference).getInteger();
            preference.setSummary(String.valueOf(val));
        }
    }

    /**
     * Fills Doc format preferences values
     *
     * @param caps          {@link ScanAttributesCaps} to take data from
     * @param prefDocFormat String shared preferences key
     * @param dest          {@link ScanAttributes.Destination} to
     *                      get destinations for
     */
    private void fillDocFormatPreferences(ScanAttributesCaps caps, String prefDocFormat, ScanAttributes.Destination dest) {
        if (caps == null) {
            return;
        }

        ListPreference pref = (ListPreference) findPreference(prefDocFormat);

        ArrayList<CharSequence> dfEntries = new ArrayList<>();
        ArrayList<CharSequence> dfEntryValues = new ArrayList<>();

        // Load Doc format
        for (DocumentFormat df : caps.getDocumentFormatList(dest)) {
            // Specific name is presented for DEFAULT only
            if (df == DocumentFormat.DEFAULT) {
                dfEntries.add(getString(R.string.default_value));
            } else if (df == DocumentFormat.OCR_PDF_TEXT_UNDER_IMAGE) {
                dfEntries.add(getString(R.string.searchable_pdf));
            } else if (df == DocumentFormat.OCR_PDF_A_TEXT_UNDER_IMAGE) {
                dfEntries.add(getString(R.string.searchable_pdf_a));
            } else if (df == DocumentFormat.OCR_XPS_TEXT_UNDER_IMAGE) {
                dfEntries.add(getString(R.string.searchable_xps));
            } else if (df == DocumentFormat.OCR_CSV) {
                dfEntries.add(getString(R.string.ocr_csv));
            } else if (df == DocumentFormat.OCR_HTML) {
                dfEntries.add(getString(R.string.ocr_html));
            } else if (df == DocumentFormat.OCR_RTF) {
                dfEntries.add(getString(R.string.ocr_rtf));
            } else if (df == DocumentFormat.OCR_TEXT) {
                dfEntries.add(getString(R.string.ocr_text));
            } else if (df == DocumentFormat.OCR_UNICODE_TEXT) {
                dfEntries.add(getString(R.string.ocr_unicode_text));
            } else if (df == DocumentFormat.OCR_XML) {
                dfEntries.add(getString(R.string.ocr_xml));
            } else if (df == DocumentFormat.PDF_A) {
                dfEntries.add(getString(R.string.pdf_a));
            } else {
                dfEntries.add(df.name());
            }
            dfEntryValues.add(df.name());
        }

        if (pref != null) {
            pref.setEntries(dfEntries.toArray(new CharSequence[dfEntries.size()]));
            pref.setEntryValues(dfEntryValues.toArray(new CharSequence[dfEntryValues.size()]));
            pref.setValueIndex(0);
            pref.setSummary("%s");
        }
    }

    private void setColorMode(SharedPreferences sharedPreferences, final String key) {
        final DocumentFormat docFormat = DocumentFormat.valueOf(sharedPreferences.getString(key, DocumentFormat.DEFAULT.name()));
        final ListPreference colorModeList =
                (ListPreference) findPreference(ScanConfigurationFragment.PREF_COLOR_MODE);
        if (mCaps != null) {
            List<CharSequence> entries = new ArrayList<>();
            List<CharSequence> entriesValues = new ArrayList<>();
            entries.add(getString(R.string.default_value));
            entriesValues.add(ColorMode.DEFAULT.name());

            for (Map.Entry<ColorMode, List<DocumentFormat>> entry : mCaps.getDocumentFormatsByColorMode().entrySet()) {
                if (entry.getValue().contains(docFormat)
                        && entry.getKey() != ColorMode.DEFAULT) {
                    entries.add(entry.getKey().name());
                    entriesValues.add(entry.getKey().name());
                }
            }

            colorModeList.setEntries(entries.toArray(new CharSequence[entries.size()]));
            colorModeList.setEntryValues(entriesValues.toArray(new CharSequence[entriesValues.size()]));
            colorModeList.setValueIndex(0);
        } else {
            colorModeList.setEntries(R.array.pref_default_entries);
            colorModeList.setEntryValues(R.array.pref_default_entries);
        }
    }

    private void showOptionalPreference(SharedPreferences preferences, String key) {
        mBaseAttributesCategory.removePreference(mPDFCompressionPref);
        mBaseAttributesCategory.removePreference(mOCRLanguagePref);
        mBaseAttributesCategory.removePreference(mPDFPasswordPref);
        mBaseAttributesCategory.removePreference(mTIFFCompressionPref);
        mBaseAttributesCategory.removePreference(mXPSCompressionPref);

        DocumentFormat docFormat = DocumentFormat.valueOf(preferences.getString(key, DocumentFormat.DEFAULT.name()));
        switch (docFormat) {
            case PDF:
                mBaseAttributesCategory.addPreference(mPDFCompressionPref);
                mBaseAttributesCategory.addPreference(mPDFPasswordPref);
                break;
            case OCR_PDF_TEXT_UNDER_IMAGE:
                mBaseAttributesCategory.addPreference(mPDFCompressionPref);
                mBaseAttributesCategory.addPreference(mOCRLanguagePref);
                mBaseAttributesCategory.addPreference(mPDFPasswordPref);
                break;
            case MTIFF:
            case TIFF:
                mBaseAttributesCategory.addPreference(mTIFFCompressionPref);
                break;
            case OCR_PDF_A_TEXT_UNDER_IMAGE:
                mBaseAttributesCategory.addPreference(mPDFCompressionPref);
                mBaseAttributesCategory.addPreference(mOCRLanguagePref);
                break;
            case OCR_CSV:
            case OCR_HTML:
            case OCR_RTF:
            case OCR_TEXT:
            case OCR_UNICODE_TEXT:
                mBaseAttributesCategory.addPreference(mOCRLanguagePref);
                break;
            case PDF_A:
                mBaseAttributesCategory.addPreference(mPDFCompressionPref);
                break;
            case XPS:
                mBaseAttributesCategory.addPreference(mXPSCompressionPref);
                break;
        }
    }

    private void supportTransmissionModeCaps(boolean isSupported) {
        if (isSupported) {
            mBaseAttributesCategory.addPreference(mTransmissionPref);
            if (mTransmissionPref != null) {
                PreferenceHelper.fillValues(getActivity(),
                        mCaps.getTransmissionModeList(),
                        (ListPreference) findPreference(PREF_TRANSMISSION_MODE),
                        0);
            }
        } else {
            mBaseAttributesCategory.removePreference(mTransmissionPref);
            SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString(PREF_TRANSMISSION_MODE, null);
            editor.commit();
        }
    }

    private void getFileOptionAttrCaps(String key) {
        Result result = new Result();
        FileOptionAsyncTask fileOptionAsyncTask = new FileOptionAsyncTask(this, key, result);
        fileOptionAsyncTask.execute();
    }

    public void fillFileOptionAttrCaps(FileOptionsAttributesCaps fileOptionsAttrCaps, String key, Result result) {
        try {
            if (result.getCode() == Result.RESULT_OK) {
                SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

                boolean isPdfEncryptionSupport = fileOptionsAttrCaps.isPdfEncryptionPasswordSupported();
                if (mPDFPasswordPref != null && isPdfEncryptionSupport) {
                    String password = mPrefs.getString(PREF_PDF_PASSWORD, null);
                    mPDFPasswordPref.setText(password);
                } else {
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString(PREF_PDF_PASSWORD, null);
                    editor.commit();
                }

                ListPreference pref = (ListPreference) findPreference(PREF_OCR_LANGUAGE);

                if (pref != null) {
                    PreferenceHelper.fillValues(getActivity(),
                            fileOptionsAttrCaps.getOcrLanguageList(),
                            (ListPreference) findPreference(PREF_OCR_LANGUAGE),
                            0);
                } else {
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString(PREF_OCR_LANGUAGE, FileOptionsAttributes.OcrLanguage.DEFAULT.name());
                    editor.commit();
                }

                pref = (ListPreference) findPreference(PREF_PDF_COMPRESSION);

                if (pref != null) {
                    PreferenceHelper.fillValues(getActivity(),
                            fileOptionsAttrCaps.getPdfCompressionModeList(),
                            (ListPreference) findPreference(PREF_PDF_COMPRESSION),
                            0);
                } else {
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString(PREF_PDF_COMPRESSION, FileOptionsAttributes.PdfCompressionMode.DEFAULT.name());
                    editor.commit();
                }

                pref = (ListPreference) findPreference(PREF_TIFF_COMPRESSION);

                if (pref != null) {
                    PreferenceHelper.fillValues(getActivity(),
                            fileOptionsAttrCaps.getTiffCompressionModeList(),
                            (ListPreference) findPreference(PREF_TIFF_COMPRESSION),
                            R.string.normal_format);
                } else {
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString(PREF_TIFF_COMPRESSION, FileOptionsAttributes.TiffCompressionMode.DEFAULT.name());
                    editor.commit();
                }

                pref = (ListPreference) findPreference(PREF_XPS_COMPRESSION);

                if (pref != null) {
                    PreferenceHelper.fillValues(getActivity(),
                            fileOptionsAttrCaps.getXpsCompressionModeList(),
                            (ListPreference) findPreference(PREF_XPS_COMPRESSION),
                            R.string.normal_format);
                } else {
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString(PREF_XPS_COMPRESSION, FileOptionsAttributes.XpsCompressionMode.DEFAULT.name());
                    editor.commit();
                }
                setPreferenceValue(key);
            } else {
                Toast.makeText(getActivity(), result.getCause(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            SLog.e(TAG, e.getMessage());
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

    @Override
    public Intent getConfiguredIntent(String rid) {
        final SharedPreferences prefs = getPreferenceScreen().getSharedPreferences();
        final ScanToRequestIntent intent = new ScanToRequestIntent();
        ScanAttributes attrs = null;

        // Build attrs and create intent with updated data
        final DocumentFormat df = DocumentFormat
                .valueOf(prefs.getString(ScanConfigurationFragment.PREF_DOC_FORMAT,
                        DocumentFormat.DEFAULT.name()));
        final ColorMode cm = ColorMode
                .valueOf(prefs.getString(ScanConfigurationFragment.PREF_COLOR_MODE,
                        ColorMode.DEFAULT.name()));
        final Duplex du = Duplex
                .valueOf(prefs.getString(ScanConfigurationFragment.PREF_DUPLEX_MODE,
                        Duplex.DEFAULT.name()));
        final ScanSize ss = ScanSize
                .valueOf(prefs.getString(ScanConfigurationFragment.PREF_ORIGINAL_SIZE,
                        ScanSize.DEFAULT.name()));

        final float customLength = prefs.getFloat(ScanConfigurationFragment.PREF_CUSTOM_LENGTH, 0.0f);

        final float customWidth = prefs.getFloat(ScanConfigurationFragment.PREF_CUSTOM_WIDTH, 0.0f);

        final Orientation orientation = Orientation
                .valueOf(prefs.getString(ScanConfigurationFragment.PREF_ORIGINAL_ORIENTATION,
                        Orientation.DEFAULT.name()));
        final Resolution resolution = Resolution
                .valueOf(prefs.getString(ScanConfigurationFragment.PREF_RESOLUTION_TYPE,
                        Resolution.DEFAULT.name()));
        final ScanPreview scanPreview = ScanPreview
                .valueOf(prefs.getString(ScanConfigurationFragment.PREF_SCAN_PREVIEW,
                        ScanPreview.DEFAULT.name()));

        final String pdfEncryption = prefs.getString(ScanConfigurationFragment.PREF_PDF_PASSWORD, null);

        final FileOptionsAttributes.OcrLanguage ocrLanguage = FileOptionsAttributes.OcrLanguage.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_OCR_LANGUAGE,
                        FileOptionsAttributes.OcrLanguage.DEFAULT.name()));

        final FileOptionsAttributes.PdfCompressionMode compressionMode = FileOptionsAttributes.PdfCompressionMode.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_PDF_COMPRESSION,
                        FileOptionsAttributes.PdfCompressionMode.DEFAULT.name()));

        final FileOptionsAttributes.TiffCompressionMode tiffCompressionMode = FileOptionsAttributes.TiffCompressionMode.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_TIFF_COMPRESSION,
                        FileOptionsAttributes.TiffCompressionMode.DEFAULT.name()));

        final FileOptionsAttributes.XpsCompressionMode xpsCompressionMode = FileOptionsAttributes.XpsCompressionMode.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_XPS_COMPRESSION,
                        FileOptionsAttributes.XpsCompressionMode.DEFAULT.name()));

        final ScanAttributes.BackgroundCleanup backgroundCleanup = ScanAttributes.BackgroundCleanup.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_BACKGROUND_CLEANUP,
                        ScanAttributes.BackgroundCleanup.DEFAULT.name()));

        final ScanAttributes.ContrastAdjustment contrastAdjustment = ScanAttributes.ContrastAdjustment.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_CONTRAST_ADJUSTMENT,
                        ScanAttributes.ContrastAdjustment.DEFAULT.name()));

        final ScanAttributes.DarknessAdjustment darknessAdjustment = ScanAttributes.DarknessAdjustment.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_DARKNESS_ADJUSTMENT,
                        ScanAttributes.DarknessAdjustment.DEFAULT.name()));

        final ScanAttributes.BlankImageRemovalMode blankImageRemovalMode = ScanAttributes.BlankImageRemovalMode.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_BLANK_IMAGE_REMOVAL_MODE,
                        ScanAttributes.BlankImageRemovalMode.DEFAULT.name()));

        final ScanAttributes.ColorDropoutMode colorDropoutMode = ScanAttributes.ColorDropoutMode.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_COLOR_DROPOUT_MODE,
                        ScanAttributes.ColorDropoutMode.DEFAULT.name()));

        final ScanAttributes.CropMode cropMode = ScanAttributes.CropMode.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_CROP_MODE,
                        ScanAttributes.CropMode.DEFAULT.name()));

        final ScanAttributes.ProgressDialogMode progressDialogMode = ScanAttributes.ProgressDialogMode.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_PROGRESS_DIALOG_MODE,
                        ScanAttributes.ProgressDialogMode.DEFAULT.name()));

        final ScanAttributes.OutputQuality outputQuality = ScanAttributes.OutputQuality.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_OUTPUT_QUALITY,
                        ScanAttributes.OutputQuality.DEFAULT.name()));

        final ScanAttributes.JobAssemblyMode jobAssemblyMode = ScanAttributes.JobAssemblyMode.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_JOB_ASSEMBLY_MODE,
                        ScanAttributes.JobAssemblyMode.DEFAULT.name()));

        final ScanAttributes.SharpnessAdjustment sharpnessAdjustment = ScanAttributes.SharpnessAdjustment.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_SHARPNESS_ADJUSTMENT,
                        ScanAttributes.SharpnessAdjustment.DEFAULT.name()));

        final ScanAttributes.MediaWeightAdjustment mediaWeightAdjustment = ScanAttributes.MediaWeightAdjustment.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_MEDIA_WEIGHT_ADJUSTMENT,
                        ScanAttributes.MediaWeightAdjustment.DEFAULT.name()));

        final ScanAttributes.TextPhotoOptimization textPhotoOptimization = ScanAttributes.TextPhotoOptimization.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_TEXT_PHOTO_OPTIMIZATION,
                        ScanAttributes.TextPhotoOptimization.DEFAULT.name()));

        final ScanAttributes.MediaSource mediaSource = ScanAttributes.MediaSource.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_MEDIA_SOURCE,
                        ScanAttributes.MediaSource.DEFAULT.name()));

        final ScanAttributes.MisfeedDetectionMode misfeedDetectionMode = ScanAttributes.MisfeedDetectionMode.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_MISFEED_DETECTION_MODE,
                        ScanAttributes.MisfeedDetectionMode.DEFAULT.name()));

        final ScanAttributes.SplitAttachmentByPage splitAttachmentByPage = ScanAttributes.SplitAttachmentByPage.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_SPLIT_ATTACHMENT_BY_PAGE,
                        ScanAttributes.SplitAttachmentByPage.DEFAULT.name()));

        final int maxPagesPerAttachment = Integer.valueOf(prefs.getInt(ScanConfigurationFragment.PREF_MAX_PAGES_PER_ATTACHMENT, 0));

        final ScanAttributes.EraseMarginUnit eraseMarginUnit = ScanAttributes.EraseMarginUnit.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_ERASE_MARGIN_UNIT,
                        ScanAttributes.EraseMarginUnit.DEFAULT.name()));

        final Margins backMargin = new Margins(prefs.getFloat(ScanConfigurationFragment.PREF_ERASE_BACK_LEFT_MARGIN, 0.0f),
                prefs.getFloat(ScanConfigurationFragment.PREF_ERASE_BACK_TOP_MARGIN, 0.0f),
                prefs.getFloat(ScanConfigurationFragment.PREF_ERASE_BACK_RIGHT_MARGIN, 0.0f),
                prefs.getFloat(ScanConfigurationFragment.PREF_ERASE_BACK_BOTTOM_MARGIN, 0.0f));

        final Margins frontMargin = new Margins(prefs.getFloat(ScanConfigurationFragment.PREF_ERASE_FRONT_LEFT_MARGIN, 0.0f),
                prefs.getFloat(ScanConfigurationFragment.PREF_ERASE_FRONT_TOP_MARGIN, 0.0f),
                prefs.getFloat(ScanConfigurationFragment.PREF_ERASE_FRONT_RIGHT_MARGIN, 0.0f),
                prefs.getFloat(ScanConfigurationFragment.PREF_ERASE_FRONT_BOTTOM_MARGIN, 0.0f));

        final ScanAttributes.CaptureMode captureMode = ScanAttributes.CaptureMode.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_CAPTURE_MODE,
                        ScanAttributes.CaptureMode.DEFAULT.name()));

        final ScanAttributes.AutomaticToneMode automaticToneMode = ScanAttributes.AutomaticToneMode.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_AUTOMATIC_TONE_MODE,
                        ScanAttributes.AutomaticToneMode.DEFAULT.name()));

        final ScanAttributes.AutomaticStraightenMode automaticStraightenMode = ScanAttributes.AutomaticStraightenMode.valueOf(
                prefs.getString(ScanConfigurationFragment.PREF_AUTOMATIC_STRAIGHTEN_MODE,
                        ScanAttributes.AutomaticStraightenMode.DEFAULT.name()));

        final String filename = prefs.getString(ScanConfigurationFragment.PREF_FILENAME, null);
        final String foldername = prefs.getString(ScanConfigurationFragment.PREF_FOLDER_NAME, null);

        Uri uri = Uri.parse(prefs.getString(ScanConfigurationFragment.PREF_URI, ""));
        String mUriUsername = prefs.getString(ScanConfigurationFragment.PREF_URI_USERNAME, "");
        String mUriPassword = prefs.getString(ScanConfigurationFragment.PREF_URI_PASSWORD, "");
        String mUriDomain = prefs.getString(ScanConfigurationFragment.PREF_URI_DOMAIN, "");

        try {
            NetworkCredentialsAttributes networkCredentialsAttributes = null;
            if (!TextUtils.isEmpty(mUriUsername) && !TextUtils.isEmpty(mUriPassword)) {
                networkCredentialsAttributes = new NetworkCredentialsAttributes.Builder()
                        .setUserName(mUriUsername)
                        .setPassword(mUriPassword)
                        .setDomain(mUriDomain)
                        .build();
            }

            FileOptionsAttributesCaps fileOptionsAttrCaps = ScannerService.getFileOptionsCapabilities(getActivity(),
                    cm, df, null);

            SLog.d(TAG, "FileOptionsAttributes option\n pdfEncryption: " + pdfEncryption + "\n"
                    + "ocrLanguage: " + ocrLanguage.name() + "\n"
                    + "compressionMode: " + compressionMode.name() + "\n"
                    + "tiffCompressionMode: " + tiffCompressionMode.name() + "\n"
                    + "xpsCompressionMode: " + xpsCompressionMode.name() + "\n");


            FileOptionsAttributes fileOptionsAttributes = new FileOptionsAttributes.Builder()
                    .setPdfEncryptionPassword(pdfEncryption)
                    .setOcrLanguage(ocrLanguage)
                    .setPdfCompressionMode(compressionMode)
                    .setTiffCompressionMode(tiffCompressionMode)
                    .setXpsCompressionMode(xpsCompressionMode)
                    .build(fileOptionsAttrCaps);

            switch (mDestination) {
                case ME:
                    attrs = new ScanAttributes.MeBuilder()
                            .setColorMode(cm)
                            .setDuplex(du)
                            .setDocumentFormat(df)
                            .setScanSize(ss)
                            .setCustomLength(customLength)
                            .setCustomWidth(customWidth)
                            .setOrientation(orientation)
                            .setResolution(resolution)
                            .setScanPreview(scanPreview)
                            .setBackgroundCleanup(backgroundCleanup)
                            .setContrastAdjustment(contrastAdjustment)
                            .setDarknessAdjustment(darknessAdjustment)
                            .setBlankImageRemovalMode(blankImageRemovalMode)
                            .setColorDropoutMode(colorDropoutMode)
                            .setCropMode(cropMode)
                            .setProgressDialogMode(progressDialogMode)
                            .setOutputQuality(outputQuality)
                            .setJobAssemblyMode(jobAssemblyMode)
                            .setSharpnessAdjustment(sharpnessAdjustment)
                            .setMediaWeightAdjustment(mediaWeightAdjustment)
                            .setTextPhotoOptimization(textPhotoOptimization)
                            .setMediaSource(mediaSource)
                            .setMisfeedDetectionMode(misfeedDetectionMode)
                            .setFileOptionsAttributes(fileOptionsAttributes)
                            .setSplitAttachmentByPage(splitAttachmentByPage)
                            .setMaxPagesPerAttachment(maxPagesPerAttachment)
                            .setEraseMarginUnit(eraseMarginUnit)
                            .setEraseBackMargin(backMargin)
                            .setEraseFrontMargin(frontMargin)
                            .setCaptureMode(captureMode)
                            .setAutomaticToneMode(automaticToneMode)
                            .setAutomaticStraightenMode(automaticStraightenMode)
                            .setFolderName(foldername)
                            .setFileName(filename)
                            .build(mCaps);
                    break;

                case HTTP:
                    attrs = new ScanAttributes.HttpBuilder(uri)
                            .setColorMode(cm)
                            .setDuplex(du)
                            .setDocumentFormat(df)
                            .setScanSize(ss)
                            .setCustomLength(customLength)
                            .setCustomWidth(customWidth)
                            .setResolution(resolution)
                            .setOrientation(orientation)
                            .setScanPreview(scanPreview)
                            .setBackgroundCleanup(backgroundCleanup)
                            .setContrastAdjustment(contrastAdjustment)
                            .setDarknessAdjustment(darknessAdjustment)
                            .setBlankImageRemovalMode(blankImageRemovalMode)
                            .setColorDropoutMode(colorDropoutMode)
                            .setCropMode(cropMode)
                            .setProgressDialogMode(progressDialogMode)
                            .setOutputQuality(outputQuality)
                            .setJobAssemblyMode(jobAssemblyMode)
                            .setSharpnessAdjustment(sharpnessAdjustment)
                            .setMediaWeightAdjustment(mediaWeightAdjustment)
                            .setTextPhotoOptimization(textPhotoOptimization)
                            .setMediaSource(mediaSource)
                            .setMisfeedDetectionMode(misfeedDetectionMode)
                            .setSplitAttachmentByPage(splitAttachmentByPage)
                            .setMaxPagesPerAttachment(maxPagesPerAttachment)
                            .setEraseMarginUnit(eraseMarginUnit)
                            .setEraseBackMargin(backMargin)
                            .setEraseFrontMargin(frontMargin)
                            .setCaptureMode(captureMode)
                            .setAutomaticToneMode(automaticToneMode)
                            .setAutomaticStraightenMode(automaticStraightenMode)
                            .setFileOptionsAttributes(fileOptionsAttributes)
                            .setFileName(filename)
                            .setNetworkCredentials(networkCredentialsAttributes)
                            .build(mCaps);
                    break;

                case FTP:
                    attrs = new ScanAttributes.FtpBuilder(uri)
                            .setColorMode(cm)
                            .setDuplex(du)
                            .setDocumentFormat(df)
                            .setScanSize(ss)
                            .setCustomLength(customLength)
                            .setCustomWidth(customWidth)
                            .setResolution(resolution)
                            .setOrientation(orientation)
                            .setScanPreview(scanPreview)
                            .setBackgroundCleanup(backgroundCleanup)
                            .setContrastAdjustment(contrastAdjustment)
                            .setDarknessAdjustment(darknessAdjustment)
                            .setBlankImageRemovalMode(blankImageRemovalMode)
                            .setColorDropoutMode(colorDropoutMode)
                            .setCropMode(cropMode)
                            .setProgressDialogMode(progressDialogMode)
                            .setOutputQuality(outputQuality)
                            .setJobAssemblyMode(jobAssemblyMode)
                            .setSharpnessAdjustment(sharpnessAdjustment)
                            .setMediaWeightAdjustment(mediaWeightAdjustment)
                            .setTextPhotoOptimization(textPhotoOptimization)
                            .setMediaSource(mediaSource)
                            .setMisfeedDetectionMode(misfeedDetectionMode)
                            .setSplitAttachmentByPage(splitAttachmentByPage)
                            .setMaxPagesPerAttachment(maxPagesPerAttachment)
                            .setEraseMarginUnit(eraseMarginUnit)
                            .setEraseBackMargin(backMargin)
                            .setEraseFrontMargin(frontMargin)
                            .setCaptureMode(captureMode)
                            .setAutomaticToneMode(automaticToneMode)
                            .setAutomaticStraightenMode(automaticStraightenMode)
                            .setFileOptionsAttributes(fileOptionsAttributes)
                            .setFileName(filename)
                            .setNetworkCredentials(networkCredentialsAttributes)
                            .build(mCaps);
                    break;

                case NETWORK_FOLDER:
                    attrs = new ScanAttributes.NetworkFolderBuilder(uri)
                            .setColorMode(cm)
                            .setDuplex(du)
                            .setDocumentFormat(df)
                            .setScanSize(ss)
                            .setCustomLength(customLength)
                            .setCustomWidth(customWidth)
                            .setResolution(resolution)
                            .setOrientation(orientation)
                            .setScanPreview(scanPreview)
                            .setBackgroundCleanup(backgroundCleanup)
                            .setContrastAdjustment(contrastAdjustment)
                            .setDarknessAdjustment(darknessAdjustment)
                            .setBlankImageRemovalMode(blankImageRemovalMode)
                            .setColorDropoutMode(colorDropoutMode)
                            .setCropMode(cropMode)
                            .setProgressDialogMode(progressDialogMode)
                            .setOutputQuality(outputQuality)
                            .setJobAssemblyMode(jobAssemblyMode)
                            .setSharpnessAdjustment(sharpnessAdjustment)
                            .setMediaWeightAdjustment(mediaWeightAdjustment)
                            .setTextPhotoOptimization(textPhotoOptimization)
                            .setMediaSource(mediaSource)
                            .setMisfeedDetectionMode(misfeedDetectionMode)
                            .setSplitAttachmentByPage(splitAttachmentByPage)
                            .setMaxPagesPerAttachment(maxPagesPerAttachment)
                            .setEraseMarginUnit(eraseMarginUnit)
                            .setEraseBackMargin(backMargin)
                            .setEraseFrontMargin(frontMargin)
                            .setCaptureMode(captureMode)
                            .setAutomaticToneMode(automaticToneMode)
                            .setAutomaticStraightenMode(automaticStraightenMode)
                            .setFileOptionsAttributes(fileOptionsAttributes)
                            .setFileName(filename)
                            .setNetworkCredentials(networkCredentialsAttributes)
                            .build(mCaps);
                    break;

                case EMAIL:
                    String emailTo = prefs.getString(ScanConfigurationFragment.PREF_EMAIL_TO, "");
                    String emailCc = prefs.getString(ScanConfigurationFragment.PREF_EMAIL_CC, "");
                    String emailBcc = prefs.getString(ScanConfigurationFragment.PREF_EMAIL_BCC, "");
                    String emailFrom = prefs.getString(ScanConfigurationFragment.PREF_EMAIL_FROM, "");
                    String emailSubject = prefs.getString(ScanConfigurationFragment.PREF_EMAIL_SUBJECT, "");
                    String emailMessage = prefs.getString(ScanConfigurationFragment.PREF_EMAIL_MESSAGE, "");
                    boolean isEnabledSmtp = prefs.getBoolean(ScanConfigurationFragment.PREF_EMAIL_SMTP, false);

                    EmailAttributes.Builder emailAttributesBuilder = new EmailAttributes.Builder();
                    if (!TextUtils.isEmpty(emailTo)) {
                        emailAttributesBuilder.addToAddresses(emailTo.split(","));
                    }
                    if (!TextUtils.isEmpty(emailCc)) {
                        emailAttributesBuilder.addCcAddresses(emailCc.split(","));
                    }
                    if (!TextUtils.isEmpty(emailBcc)) {
                        emailAttributesBuilder.addBccAddresses(emailBcc.split(","));
                    }

                    if (!TextUtils.isEmpty(emailFrom)) {
                        String value = emailFrom.trim();
                        if (!TextUtils.isEmpty(value)) {
                            emailAttributesBuilder.setFrom(emailFrom, null);
                        }
                    }
                    if (!TextUtils.isEmpty(emailSubject)) {
                        emailAttributesBuilder.setSubject(emailSubject);
                    }
                    if (!TextUtils.isEmpty(emailMessage)) {
                        emailAttributesBuilder.setMessage(emailMessage);
                    }

                    SmtpAttributes smtpAttributes = null;
                    if (isEnabledSmtp) {
                        String hostname = prefs.getString(getString(R.string.pref_email_hostname), null);
                        String port = prefs.getString(getString(R.string.pref_email_port), null);
                        String connectionTimeout = prefs.getString(getString(R.string.pref_email_connection_timeout), null);
                        String readTimeout = prefs.getString(getString(R.string.pref_email_read_timeout), null);
                        String username = prefs.getString(getString(R.string.pref_email_username), null);
                        String password = prefs.getString(getString(R.string.pref_email_password), null);
                        String transportModeStr = prefs.getString(getString(R.string.pref_email_transport_mode), null);

                        NetworkCredentialsAttributes authentication = null;
                        if (!TextUtils.isEmpty(username)) {
                            authentication = new NetworkCredentialsAttributes.Builder()
                                    .setUserName(username)
                                    .setPassword(password)
                                    .build();
                        }

                        SmtpAttributes.TransportMode transportMode = SmtpAttributes.TransportMode.PLAIN;
                        List<SmtpAttributes.TransportMode> transportModeList =
                                new ArrayList<>(EnumSet.allOf(SmtpAttributes.TransportMode.class));
                        for (SmtpAttributes.TransportMode transport : transportModeList) {
                            if (transportMode.name().equals(transportModeStr)) {
                                transportMode = transport;
                                break;
                            }
                        }

                        smtpAttributes = new SmtpAttributes.Builder(hostname)
                                .setPort(Integer.parseInt(port))
                                .setConnectTimeout(Integer.parseInt(connectionTimeout))
                                .setReadTimeout(Integer.parseInt(readTimeout))
                                .setServerCredentials(authentication)
                                .setTransportMode(transportMode)
                                .build();
                    }

                    attrs = new ScanAttributes.EmailBuilder(emailAttributesBuilder.build())
                            .setSmtpAttributes(smtpAttributes)
                            .setColorMode(cm)
                            .setDuplex(du)
                            .setDocumentFormat(df)
                            .setScanSize(ss)
                            .setCustomLength(customLength)
                            .setCustomWidth(customWidth)
                            .setResolution(resolution)
                            .setOrientation(orientation)
                            .setScanPreview(scanPreview)
                            .setBackgroundCleanup(backgroundCleanup)
                            .setContrastAdjustment(contrastAdjustment)
                            .setDarknessAdjustment(darknessAdjustment)
                            .setBlankImageRemovalMode(blankImageRemovalMode)
                            .setColorDropoutMode(colorDropoutMode)
                            .setCropMode(cropMode)
                            .setProgressDialogMode(progressDialogMode)
                            .setOutputQuality(outputQuality)
                            .setJobAssemblyMode(jobAssemblyMode)
                            .setSharpnessAdjustment(sharpnessAdjustment)
                            .setMediaWeightAdjustment(mediaWeightAdjustment)
                            .setTextPhotoOptimization(textPhotoOptimization)
                            .setSplitAttachmentByPage(splitAttachmentByPage)
                            .setMaxPagesPerAttachment(maxPagesPerAttachment)
                            .setEraseMarginUnit(eraseMarginUnit)
                            .setEraseBackMargin(backMargin)
                            .setEraseFrontMargin(frontMargin)
                            .setCaptureMode(captureMode)
                            .setAutomaticToneMode(automaticToneMode)
                            .setAutomaticStraightenMode(automaticStraightenMode)
                            .setMediaSource(mediaSource)
                            .setMisfeedDetectionMode(misfeedDetectionMode)
                            .setFileOptionsAttributes(fileOptionsAttributes)
                            .setFileName(filename)
                            .build(mCaps);
                    break;

                case USB:
                    String usbLocation = foldername; // if folder was chosen from FileChoose then use it

                    if (TextUtils.isEmpty(usbLocation)) {
                        usbLocation = prefs.getString(ScanConfigurationFragment.PREF_USB_STORAGE, "");
                    }

                    attrs = new ScanAttributes.UsbBuilder(usbLocation)
                            .setColorMode(cm)
                            .setDuplex(du)
                            .setDocumentFormat(df)
                            .setScanSize(ss)
                            .setOrientation(orientation)
                            .setResolution(resolution)
                            .setScanPreview(scanPreview)
                            .setBackgroundCleanup(backgroundCleanup)
                            .setContrastAdjustment(contrastAdjustment)
                            .setDarknessAdjustment(darknessAdjustment)
                            .setBlankImageRemovalMode(blankImageRemovalMode)
                            .setColorDropoutMode(colorDropoutMode)
                            .setCropMode(cropMode)
                            .setProgressDialogMode(progressDialogMode)
                            .setOutputQuality(outputQuality)
                            .setJobAssemblyMode(jobAssemblyMode)
                            .setSharpnessAdjustment(sharpnessAdjustment)
                            .setSplitAttachmentByPage(splitAttachmentByPage)
                            .setMaxPagesPerAttachment(maxPagesPerAttachment)
                            .setEraseMarginUnit(eraseMarginUnit)
                            .setEraseBackMargin(backMargin)
                            .setEraseFrontMargin(frontMargin)
                            .setCaptureMode(captureMode)
                            .setAutomaticToneMode(automaticToneMode)
                            .setAutomaticStraightenMode(automaticStraightenMode)
                            .setFileOptionsAttributes(fileOptionsAttributes)
                            .setFileName(filename)
                            .build(mCaps);
                    break;
                default:
            }
        } catch (CapabilitiesExceededException e) {
            SLog.e(TAG, "Build failed " + e.getMessage());
            mCallback.onFailedCreateIntent(getString(R.string.scan_creation_failed));
        }

        if (attrs != null) {
            final ScanToRequestIntent.IntentParams params =
                    new ScanToRequestIntent.IntentParams(attrs,
                            new ScanletAttributes.Builder().build(), null, null,
                            null, null, null, null, null);

            intent.putIntentParams(params);
        }

        return intent;
    }

    @Override
    public void onCapabilitiesLoaded(final ScanAttributesCaps caps, final String errorMsg) {
        if (caps == null || getActivity() == null) {
            mCallback.onFailed(errorMsg);
            return;
        }

        final SharedPreferences prefs = getPreferenceScreen().getSharedPreferences();
        final Context context = getActivity().getApplicationContext();
        ListPreference pref;

        mCaps = caps;

        // Load destinations
        pref = (ListPreference) findPreference(PREF_DESTINATION);
        final ScanAttributes.Destination dest = mDestination;
        PreferenceHelper.fillValues(context,
                caps.getDestinationList(),
                pref,
                0);
        pref.setValue(dest.name());
        pref.setSummary("%s");

        // Load Resolutions
        PreferenceHelper.fillValues(context,
                caps.getResolutionList(),
                (ListPreference) findPreference(PREF_RESOLUTION_TYPE),
                0);

        // Load document format
        fillDocFormatPreferences(caps, PREF_DOC_FORMAT, dest);

        // Load color mode
        PreferenceHelper.fillValues(context,
                caps.getColorModeList(),
                (ListPreference) findPreference(PREF_COLOR_MODE),
                0);

        // Load duplex mode
        PreferenceHelper.fillValues(context,
                caps.getDuplexList(),
                (IconsListPreference) findPreference(PREF_DUPLEX_MODE),
                0);

        // Load Original Size
        PreferenceHelper.fillValues(context,
                caps.getScanSizeList(),
                (IconsListPreference) findPreference(PREF_ORIGINAL_SIZE),
                R.string.scan_size_pattern);

        // Load original orientation
        PreferenceHelper.fillValues(context,
                caps.getOrientationList(),
                (ListPreference) findPreference(PREF_ORIGINAL_ORIENTATION),
                0);

        // Load background cleanup
        PreferenceHelper.fillValues(context,
                caps.getBackgroundCleanupList(),
                (ListPreference) findPreference(PREF_BACKGROUND_CLEANUP),
                0);

        //
        // Load original orientation
        PreferenceHelper.fillValues(context,
                caps.getContrastAdjustmentList(),
                (ListPreference) findPreference(PREF_CONTRAST_ADJUSTMENT),
                0);

        // Load darkness adjustment
        PreferenceHelper.fillValues(context,
                caps.getDarknessAdjustmentList(),
                (ListPreference) findPreference(PREF_DARKNESS_ADJUSTMENT),
                0);

        // Load black image removal mode
        PreferenceHelper.fillValues(context,
                caps.getBlankImageRemovalModeList(),
                (ListPreference) findPreference(PREF_BLANK_IMAGE_REMOVAL_MODE),
                0);

        // Load Color dropout mode
        PreferenceHelper.fillValues(context,
                caps.getColorDropoutModeList(),
                (ListPreference) findPreference(PREF_COLOR_DROPOUT_MODE),
                0);

        // Load crop mode
        PreferenceHelper.fillValues(context,
                caps.getCropModeList(),
                (ListPreference) findPreference(PREF_CROP_MODE),
                0);

        // Load progress dialog mode
        PreferenceHelper.fillValues(context,
                caps.getProgressDialogModeList(),
                (ListPreference) findPreference(PREF_PROGRESS_DIALOG_MODE),
                0);

        // Load output quality
        PreferenceHelper.fillValues(context,
                caps.getOutputQualityList(),
                (ListPreference) findPreference(PREF_OUTPUT_QUALITY),
                0);

        // Load scan preview
        PreferenceHelper.fillValues(context,
                caps.getScanPreviewList(),
                (ListPreference) findPreference(PREF_SCAN_PREVIEW),
                R.string.scan_preview_pattern);

        // Load job assembly mode
        PreferenceHelper.fillValues(context,
                caps.getJobAssemblyModeList(),
                (ListPreference) findPreference(PREF_JOB_ASSEMBLY_MODE),
                0);

        // Load sharpness adjustment
        PreferenceHelper.fillValues(context,
                caps.getSharpnessAdjustmentList(),
                (ListPreference) findPreference(PREF_SHARPNESS_ADJUSTMENT),
                0);

        // Load media weight adjustment
        PreferenceHelper.fillValues(context,
                caps.getMediaWeightAdjustmentList(),
                (ListPreference) findPreference(PREF_MEDIA_WEIGHT_ADJUSTMENT),
                0);

        // Load text photo optimization
        PreferenceHelper.fillValues(context,
                caps.getTextPhotoOptimizationList(),
                (ListPreference) findPreference(PREF_TEXT_PHOTO_OPTIMIZATION),
                0);

        // Load media source
        PreferenceHelper.fillValues(context,
                caps.getMediaSourceList(),
                (ListPreference) findPreference(PREF_MEDIA_SOURCE),
                0);

        // Load misfeed detection mode
        PreferenceHelper.fillValues(context,
                caps.getMisfeedDetectionModeList(),
                (ListPreference) findPreference(PREF_MISFEED_DETECTION_MODE),
                0);

        // Load split attachment by page mode
        PreferenceHelper.fillValues(context,
                caps.getSplitAttachmentByPageList(),
                (ListPreference) findPreference(PREF_SPLIT_ATTACHMENT_BY_PAGE),
                0);

        // Load erase margin unit mode
        PreferenceHelper.fillValues(context,
                caps.getEraseMarginUnitList(),
                (ListPreference) findPreference(PREF_ERASE_MARGIN_UNIT),
                0);

        // Load capture mode
        PreferenceHelper.fillValues(context,
                caps.getCaptureModeList(),
                (ListPreference) findPreference(PREF_CAPTURE_MODE),
                0);

        // Load automatic tone mode
        PreferenceHelper.fillValues(context,
                caps.getAutomaticToneModeList(),
                (ListPreference) findPreference(PREF_AUTOMATIC_TONE_MODE),
                0);

        // Load automatic straighten mode
        PreferenceHelper.fillValues(context,
                caps.getAutomaticStraightenModeList(),
                (ListPreference) findPreference(PREF_AUTOMATIC_STRAIGHTEN_MODE),
                0);

        mMaxPagesPerAttachmentPref.setLimits((int) caps.getMaxPagesPerAttachmentRange().getLowerBound(), (int) caps.getMaxPagesPerAttachmentRange().getUpperBound());

        mEraseBackMarginPref.setBottomLimits(caps.getEraseBackBottomRange().getLowerBound(), caps.getEraseBackBottomRange().getUpperBound());
        mEraseBackMarginPref.setLeftLimits(caps.getEraseBackLeftRange().getLowerBound(), caps.getEraseBackLeftRange().getUpperBound());
        mEraseBackMarginPref.setTopLimits(caps.getEraseBackTopRange().getLowerBound(), caps.getEraseBackTopRange().getUpperBound());
        mEraseBackMarginPref.setRightLimits(caps.getEraseBackRightRange().getLowerBound(), caps.getEraseBackRightRange().getUpperBound());
        mEraseFrontMarginPref.setBottomLimits(caps.getEraseFrontBottomRange().getLowerBound(), caps.getEraseFrontBottomRange().getUpperBound());
        mEraseFrontMarginPref.setLeftLimits(caps.getEraseFrontLeftRange().getLowerBound(), caps.getEraseFrontLeftRange().getUpperBound());
        mEraseFrontMarginPref.setTopLimits(caps.getEraseFrontTopRange().getLowerBound(), caps.getEraseFrontTopRange().getUpperBound());
        mEraseFrontMarginPref.setRightLimits(caps.getEraseFrontRightRange().getLowerBound(), caps.getEraseFrontRightRange().getUpperBound());

        mCustomLengthPref.setLimits(caps.getCustomLengthRange().getLowerBound(), caps.getCustomLengthRange().getUpperBound());
        mCustomWidthPref.setLimits(caps.getCustomWidthRange().getLowerBound(), caps.getCustomWidthRange().getUpperBound());

        if (mSavedState != null) {
            ((ListPreference) findPreference(PREF_DESTINATION))
                    .setValue(mSavedState.getString(PREF_DESTINATION, Destination.ME.name()));
            ((EditTextPreference) findPreference(PREF_FILENAME))
                    .setText(mSavedState.getString(PREF_FILENAME, ""));
            ((LongSummaryPreference) findPreference(PREF_FOLDER_NAME))
                    .setSummary(mSavedState.getString(PREF_FOLDER_NAME, ""));
            ((ListPreference) findPreference(PREF_COLOR_MODE))
                    .setValue(mSavedState.getString(PREF_COLOR_MODE, ColorMode.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_DUPLEX_MODE))
                    .setValue(mSavedState.getString(PREF_DUPLEX_MODE, Duplex.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_RESOLUTION_TYPE))
                    .setValue(mSavedState.getString(PREF_RESOLUTION_TYPE, Resolution.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_DOC_FORMAT))
                    .setValue(mSavedState.getString(PREF_DOC_FORMAT, DocumentFormat.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_ORIGINAL_SIZE))
                    .setValue(mSavedState.getString(PREF_ORIGINAL_SIZE, ScanSize.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_ORIGINAL_ORIENTATION))
                    .setValue(mSavedState.getString(PREF_ORIGINAL_ORIENTATION, Orientation.DEFAULT.name()));

            ((ListPreference) findPreference(PREF_BACKGROUND_CLEANUP))
                    .setValue(mSavedState.getString(PREF_BACKGROUND_CLEANUP, ScanAttributes.BackgroundCleanup.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_CONTRAST_ADJUSTMENT))
                    .setValue(mSavedState.getString(PREF_CONTRAST_ADJUSTMENT, ScanAttributes.ContrastAdjustment.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_DARKNESS_ADJUSTMENT))
                    .setValue(mSavedState.getString(PREF_DARKNESS_ADJUSTMENT, ScanAttributes.DarknessAdjustment.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_BLANK_IMAGE_REMOVAL_MODE))
                    .setValue(mSavedState.getString(PREF_BLANK_IMAGE_REMOVAL_MODE, ScanAttributes.BlankImageRemovalMode.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_COLOR_DROPOUT_MODE))
                    .setValue(mSavedState.getString(PREF_COLOR_DROPOUT_MODE, ScanAttributes.ColorDropoutMode.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_CROP_MODE))
                    .setValue(mSavedState.getString(PREF_CROP_MODE, ColorMode.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_PROGRESS_DIALOG_MODE))
                    .setValue(mSavedState.getString(PREF_PROGRESS_DIALOG_MODE, ScanAttributes.ProgressDialogMode.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_OUTPUT_QUALITY))
                    .setValue(mSavedState.getString(PREF_OUTPUT_QUALITY, ScanAttributes.OutputQuality.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_JOB_ASSEMBLY_MODE))
                    .setValue(mSavedState.getString(PREF_JOB_ASSEMBLY_MODE, ScanAttributes.JobAssemblyMode.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_SHARPNESS_ADJUSTMENT))
                    .setValue(mSavedState.getString(PREF_SHARPNESS_ADJUSTMENT, ScanAttributes.SharpnessAdjustment.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_MEDIA_WEIGHT_ADJUSTMENT))
                    .setValue(mSavedState.getString(PREF_MEDIA_WEIGHT_ADJUSTMENT, ScanAttributes.MediaWeightAdjustment.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_TEXT_PHOTO_OPTIMIZATION))
                    .setValue(mSavedState.getString(PREF_TEXT_PHOTO_OPTIMIZATION, ScanAttributes.TextPhotoOptimization.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_MEDIA_SOURCE))
                    .setValue(mSavedState.getString(PREF_MEDIA_SOURCE, ScanAttributes.MediaSource.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_MISFEED_DETECTION_MODE))
                    .setValue(mSavedState.getString(PREF_MISFEED_DETECTION_MODE, ScanAttributes.MisfeedDetectionMode.DEFAULT.name()));

            ((ListPreference) findPreference(PREF_SPLIT_ATTACHMENT_BY_PAGE))
                    .setValue(mSavedState.getString(PREF_SPLIT_ATTACHMENT_BY_PAGE, ScanAttributes.SplitAttachmentByPage.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_ERASE_MARGIN_UNIT))
                    .setValue(mSavedState.getString(PREF_ERASE_MARGIN_UNIT, ScanAttributes.EraseMarginUnit.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_CAPTURE_MODE))
                    .setValue(mSavedState.getString(PREF_CAPTURE_MODE, ScanAttributes.CaptureMode.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_AUTOMATIC_TONE_MODE))
                    .setValue(mSavedState.getString(PREF_AUTOMATIC_TONE_MODE, ScanAttributes.AutomaticToneMode.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_AUTOMATIC_STRAIGHTEN_MODE))
                    .setValue(mSavedState.getString(PREF_AUTOMATIC_STRAIGHTEN_MODE, ScanAttributes.AutomaticStraightenMode.DEFAULT.name()));

            ((EditTextPreference) findPreference(PREF_PDF_PASSWORD))
                    .setText(mSavedState.getString(PREF_PDF_PASSWORD, ""));
            ((ListPreference) findPreference(PREF_OCR_LANGUAGE))
                    .setValue(mSavedState.getString(PREF_OCR_LANGUAGE, FileOptionsAttributes.OcrLanguage.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_PDF_COMPRESSION))
                    .setValue(mSavedState.getString(PREF_PDF_COMPRESSION, FileOptionsAttributes.PdfCompressionMode.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_TIFF_COMPRESSION))
                    .setValue(mSavedState.getString(PREF_TIFF_COMPRESSION, FileOptionsAttributes.TiffCompressionMode.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_XPS_COMPRESSION))
                    .setValue(mSavedState.getString(PREF_XPS_COMPRESSION, FileOptionsAttributes.XpsCompressionMode.DEFAULT.name()));

            ((ListPreference) findPreference(PREF_SCAN_PREVIEW))
                    .setValue(mSavedState.getString(PREF_SCAN_PREVIEW, ScanPreview.DEFAULT.name()));
        }

        refreshAllPrefs(prefs);

        // Enable preferences for editing
        getPreferenceScreen().setEnabled(true);

        applyDestination();
    }

    @Override
    public void onDefaultCapabilitiesLoaded(ScanAttributes defaultCaps, String errorMsg) {
        if (defaultCaps == null || getActivity() == null) {
            mCallback.onFailed(errorMsg);
            return;
        }

        ScanAttributesReader defaultScanAttr = new ScanAttributesReader(defaultCaps);
        mCustomLengthPref.setValue(defaultScanAttr.getCustomLength());
        mCustomWidthPref.setValue(defaultScanAttr.getCustomWidth());

        // Enable preferences for editing
        getPreferenceScreen().setEnabled(true);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mCaps != null) {
            final SharedPreferences prefs = getPreferenceScreen().getSharedPreferences();

            // Save preference values to restore after caps get again
            outState.putString(PREF_DESTINATION, prefs.getString(PREF_DESTINATION, Destination.ME.name()));
            outState.putString(PREF_FILENAME, prefs.getString(PREF_FILENAME, ""));
            outState.putString(PREF_FOLDER_NAME, prefs.getString(PREF_FOLDER_NAME, ""));
            outState.putString(PREF_COLOR_MODE, prefs.getString(PREF_COLOR_MODE, ColorMode.DEFAULT.name()));
            outState.putString(PREF_DUPLEX_MODE, prefs.getString(PREF_DUPLEX_MODE, Duplex.DEFAULT.name()));
            outState.putString(PREF_RESOLUTION_TYPE, prefs.getString(PREF_RESOLUTION_TYPE, Resolution.DEFAULT.name()));
            outState.putString(PREF_DOC_FORMAT, prefs.getString(PREF_DOC_FORMAT, DocumentFormat.DEFAULT.name()));
            outState.putString(PREF_ORIGINAL_SIZE, prefs.getString(PREF_ORIGINAL_SIZE, ScanSize.DEFAULT.name()));
            outState.putString(PREF_ORIGINAL_ORIENTATION, prefs.getString(PREF_ORIGINAL_ORIENTATION, Orientation.DEFAULT.name()));

            outState.putString(PREF_BACKGROUND_CLEANUP, prefs.getString(PREF_BACKGROUND_CLEANUP, ScanAttributes.BackgroundCleanup.DEFAULT.name()));
            outState.putString(PREF_CONTRAST_ADJUSTMENT, prefs.getString(PREF_CONTRAST_ADJUSTMENT, ScanAttributes.ContrastAdjustment.DEFAULT.name()));
            outState.putString(PREF_DARKNESS_ADJUSTMENT, prefs.getString(PREF_DARKNESS_ADJUSTMENT, ScanAttributes.DarknessAdjustment.DEFAULT.name()));
            outState.putString(PREF_BLANK_IMAGE_REMOVAL_MODE, prefs.getString(PREF_BLANK_IMAGE_REMOVAL_MODE, ScanAttributes.BlankImageRemovalMode.DEFAULT.name()));
            outState.putString(PREF_COLOR_DROPOUT_MODE, prefs.getString(PREF_COLOR_DROPOUT_MODE, ScanAttributes.ColorDropoutMode.DEFAULT.name()));
            outState.putString(PREF_CROP_MODE, prefs.getString(PREF_CROP_MODE, ScanAttributes.CropMode.DEFAULT.name()));
            outState.putString(PREF_PROGRESS_DIALOG_MODE, prefs.getString(PREF_PROGRESS_DIALOG_MODE, ScanAttributes.ProgressDialogMode.DEFAULT.name()));
            outState.putString(PREF_OUTPUT_QUALITY, prefs.getString(PREF_OUTPUT_QUALITY, ScanAttributes.OutputQuality.DEFAULT.name()));
            outState.putString(PREF_JOB_ASSEMBLY_MODE, prefs.getString(PREF_JOB_ASSEMBLY_MODE, ScanAttributes.JobAssemblyMode.DEFAULT.name()));
            outState.putString(PREF_SHARPNESS_ADJUSTMENT, prefs.getString(PREF_SHARPNESS_ADJUSTMENT, ScanAttributes.SharpnessAdjustment.DEFAULT.name()));
            outState.putString(PREF_MEDIA_WEIGHT_ADJUSTMENT, prefs.getString(PREF_MEDIA_WEIGHT_ADJUSTMENT, ScanAttributes.MediaWeightAdjustment.DEFAULT.name()));
            outState.putString(PREF_TEXT_PHOTO_OPTIMIZATION, prefs.getString(PREF_TEXT_PHOTO_OPTIMIZATION, ScanAttributes.TextPhotoOptimization.DEFAULT.name()));
            outState.putString(PREF_MEDIA_SOURCE, prefs.getString(PREF_MEDIA_SOURCE, ScanAttributes.MediaSource.DEFAULT.name()));
            outState.putString(PREF_MISFEED_DETECTION_MODE, prefs.getString(PREF_MISFEED_DETECTION_MODE, ScanAttributes.MisfeedDetectionMode.DEFAULT.name()));

            outState.putString(PREF_PDF_PASSWORD, prefs.getString(PREF_PDF_PASSWORD, ""));
            outState.putString(PREF_OCR_LANGUAGE, prefs.getString(PREF_OCR_LANGUAGE, FileOptionsAttributes.OcrLanguage.DEFAULT.name()));
            outState.putString(PREF_PDF_COMPRESSION, prefs.getString(PREF_PDF_COMPRESSION, FileOptionsAttributes.PdfCompressionMode.DEFAULT.name()));
            outState.putString(PREF_TIFF_COMPRESSION, prefs.getString(PREF_TIFF_COMPRESSION, FileOptionsAttributes.TiffCompressionMode.DEFAULT.name()));
            outState.putString(PREF_XPS_COMPRESSION, prefs.getString(PREF_XPS_COMPRESSION, FileOptionsAttributes.XpsCompressionMode.DEFAULT.name()));

            outState.putString(PREF_SCAN_PREVIEW, prefs.getString(PREF_SCAN_PREVIEW, ScanPreview.DEFAULT.name()));

            outState.putString(PREF_SPLIT_ATTACHMENT_BY_PAGE, prefs.getString(PREF_SPLIT_ATTACHMENT_BY_PAGE, ScanAttributes.SplitAttachmentByPage.DEFAULT.name()));
            outState.putInt(PREF_MAX_PAGES_PER_ATTACHMENT, prefs.getInt(PREF_MAX_PAGES_PER_ATTACHMENT, 0));
            outState.putString(PREF_ERASE_MARGIN_UNIT, prefs.getString(PREF_ERASE_MARGIN_UNIT, ScanAttributes.EraseMarginUnit.DEFAULT.name()));
            outState.putString(PREF_CAPTURE_MODE, prefs.getString(PREF_CAPTURE_MODE, ScanAttributes.CaptureMode.DEFAULT.name()));
            outState.putString(PREF_AUTOMATIC_TONE_MODE, prefs.getString(PREF_AUTOMATIC_TONE_MODE, ScanAttributes.AutomaticToneMode.DEFAULT.name()));
            outState.putString(PREF_AUTOMATIC_STRAIGHTEN_MODE, prefs.getString(PREF_AUTOMATIC_STRAIGHTEN_MODE, ScanAttributes.AutomaticStraightenMode.DEFAULT.name()));
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
        if (requestCode == SCAN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                String absolutePath = data.getExtras().getString(FileUtils.PATH);
                mFoldernamePref.getEditor().putString(PREF_FOLDER_NAME, absolutePath).apply();
                String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                String path = absolutePath.replaceFirst(externalPath, "");
                mFoldernamePref.setSummary(path);
            }
        }
    }

    private void showCustomSizePreference(SharedPreferences preferences, String key) {
        ScanSize scanSize = ScanSize.valueOf(preferences.getString(key, ScanSize.DEFAULT.name()));

        if (scanSize == ScanSize.CUSTOM) {
            mBaseAttributesCategory.addPreference(mCustomLengthPref);
            mBaseAttributesCategory.addPreference(mCustomWidthPref);
        } else {
            mBaseAttributesCategory.removePreference(mCustomLengthPref);
            mBaseAttributesCategory.removePreference(mCustomWidthPref);
        }
    }
}
