package com.hp.jetadvantage.link.services.settingsui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
import android.widget.EditText;

import com.hp.jetadvantage.link.api.CapabilitiesExceededException;
import com.hp.jetadvantage.link.api.copier.CopyAttributes;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.BackgroundCleanup;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.CollateMode;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.ColorMode;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.ContrastAdjustment;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.CopyPreview;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.DarknessAdjustment;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.Duplex;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.JobAssemblyMode;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.JobExecutionMode;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.NumberUpDirection;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.NumberUpMode;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.Orientation;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.PaperSize;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.PaperSource;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.PaperType;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.ScaleMode;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.ScanSize;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.ScanSource;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.SharpnessAdjustment;
import com.hp.jetadvantage.link.api.copier.CopyAttributes.TextGraphicsOptimization;
import com.hp.jetadvantage.link.api.copier.CopyAttributesCaps;
import com.hp.jetadvantage.link.api.copier.CopyletAttributes;
import com.hp.jetadvantage.link.api.copier.JobCredentialsAttributes;
import com.hp.jetadvantage.link.api.copier.Range;
import com.hp.jetadvantage.link.api.copier.intent.CopyToRequestIntent;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.settingsui.R;
import com.hp.jetadvantage.link.services.settingsui.SettingsUIActivity;
import com.hp.jetadvantage.link.services.settingsui.helper.PreferenceHelper;
import com.hp.jetadvantage.link.services.settingsui.interfaces.ConfigurationCallback;
import com.hp.jetadvantage.link.services.settingsui.interfaces.ConfigurationFragment;
import com.hp.jetadvantage.link.services.settingsui.preferences.FloatNumberPickerPreference;
import com.hp.jetadvantage.link.services.settingsui.preferences.IconsListPreference;
import com.hp.jetadvantage.link.services.settingsui.preferences.NumberPickerPreference;
import com.hp.jetadvantage.link.services.settingsui.task.CapabilitiesAsyncTask;

import java.util.Arrays;

/**
 * Simple {@link PreferenceFragment} to set Copy Attributes and save into preferences.
 */
public final class CopyConfigurationFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener, ConfigurationFragment<CopyAttributesCaps, CopyAttributes> {
    private static final String TAG = SettingsUIActivity.TAG;

    // Preferences keys for CopyAttributes
    public static final String PREF_COPIES = "pref_copy_copies";
    public static final String PREF_COLOR_MODE = "pref_copy_colorMode";
    public static final String PREF_ORIENTATION = "pref_copy_orientation";
    public static final String PREF_SCAN_DUPLEX_MODE = "pref_copy_scanDuplexMode";
    public static final String PREF_PRINT_DUPLEX_MODE = "pref_copy_printDuplexMode";
    public static final String PREF_SCAN_SIZE = "pref_copy_scanSize";
    public static final String PREF_SCAN_CUSTOM_LENGTH = "pref_copy_scan_customLength";
    public static final String PREF_SCAN_CUSTOM_WIDTH = "pref_copy_scan_customWidth";
    public static final String PREF_PRINT_SIZE = "pref_copy_printSize";
    public static final String PREF_PRINT_CUSTOM_LENGTH = "pref_copy_print_customLength";
    public static final String PREF_PRINT_CUSTOM_WIDTH = "pref_copy_print_customWidth";
    public static final String PREF_COPY_PREVIEW = "pref_copy_preview";
    public static final String PREF_SCAN_SOURCE = "pref_copy_scanSource";
    public static final String PREF_BACKGROUND_CLEANUP = "pref_copy_backgroundCleanup";
    public static final String PREF_CONTRAST_ADJUSTMENT = "pref_copy_contrastAdjustment";
    public static final String PREF_DARKNESS_ADJUSTMENT = "pref_copy_darknessAdjustment";
    public static final String PREF_SHARPNESS_ADJUSTMENT = "pref_copy_sharpnessAdjustment";
    public static final String PREF_COLLATE_MODE = "pref_copy_collateMode";
    public static final String PREF_PAPER_SOURCE = "pref_copy_paperSource";
    public static final String PREF_PAPER_TYPE = "pref_paperType";
    public static final String PREF_SCALE_MODE = "pref_copy_scaleMode";
    public static final String PREF_SCALE_PERCENT = "pref_copy_scalePercent";
    public static final String PREF_TEXT_GRAPHICS_OPTIMIZATION = "pref_textGraphicsOptimization";
    public static final String PREF_NUMBER_UP_MODE = "pref_copy_numberUpMode";
    public static final String PREF_NUMBER_UP_DIRECTION = "pref_copy_numberUpDirection";
    public static final String PREF_JOB_ASSEMBLY_MODE = "pref_copy_jobAssemblyMode";
    public static final String PREF_JOB_EXECUTION_MODE = "pref_copy_jobExecutionMode";
    public static final String PREF_STORE_JOB_NAME = "pref_copy_storedJobName";
    public static final String PREF_STORE_JOB_FOLDER_NAME = "pref_copy_storedJobFolderName";
    public static final String PREF_STORE_DELETE_ON_POWER = "pref_copy_storedDeleteOnPower";
    public static final String PREF_STORE_DELETE_ON_RELEASE = "pref_copy_storedDeleteOnRelease";
    public static final String PREF_STORE_JOB_PASSWORD_TYPE = "pref_copy_storedJobPasswordType";
    public static final String PREF_STORE_JOB_PASSWORD = "pref_copy_storedJobPassword";
    public static final String PREF_OUTPUT_BIN = "pref_copy_outputBin";
    public static final String PREF_PROGRESS_DIALOG_MODE = "pref_copy_progressDialogMode";

    public static final String PREF_BASE_ATTRIBUTES_CATEGORY = "base_attributes_category";
    public static final String PREF_DESTINATION_STORE_CATEGORY = "destination_copy_store_category";

    private static final String ARG_CLIENT_API_LEVEL = "client_api_level";
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
    private JobExecutionMode mJobExecutionMode = JobExecutionMode.NORMAL;
    /**
     * Cached caps
     */
    private CopyAttributesCaps mCaps = null;
    /**
     * Cached default caps
     */
    private CopyAttributes mDefaultCaps = null;
    /**
     * Callback interface for errors handling on parents side
     */
    private ConfigurationCallback mCallback;
    /**
     * Saved fragment state with current settings
     */
    private Bundle mSavedState;

    private PreferenceCategory mBaseAttributesCategory;
    private PreferenceCategory mStoreCategory;

    private NumberPickerPreference mCopiesPref;
    private NumberPickerPreference mScalePercentPref;

    private EditTextPreference mJobNamePref;
    private EditTextPreference mJobFolderNamePref;
    private EditTextPreference mJobPasswordPref;
    private FloatNumberPickerPreference mScanCustomLengthPref;
    private FloatNumberPickerPreference mScanCustomWidthPref;
    private FloatNumberPickerPreference mPrintCustomLengthPref;
    private FloatNumberPickerPreference mPrintCustomWidthPref;

    private IconsListPreference mOutputBinPref;
    private IconsListPreference mProgressDialogModePref;

    private ListPreference mNumberUpDirectionPref;

    private int mClientApiLevel;
    /**
     * Default factory
     *
     * @param attrs copy attributes
     * @return new instance
     */
    public static CopyConfigurationFragment newInstance(final CopyAttributes attrs, int clientApiLevel) {
        final Bundle args = new Bundle();
        final CopyConfigurationFragment frag = new CopyConfigurationFragment();

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
            mClientApiLevel = args.getInt(ARG_CLIENT_API_LEVEL, Sdk.VERSION.LEVEL);
        }

        addPreferencesFromResource(R.xml.copy_preferences);

        mBaseAttributesCategory = (PreferenceCategory) findPreference(PREF_BASE_ATTRIBUTES_CATEGORY);
        mStoreCategory = (PreferenceCategory) findPreference(PREF_DESTINATION_STORE_CATEGORY);

        mCopiesPref = ((NumberPickerPreference) findPreference(PREF_COPIES));
        mCopiesPref.setLimits(1, 1);
        mCopiesPref.getEditor().putInt(PREF_COPIES, 1).apply();

        mScalePercentPref = ((NumberPickerPreference) findPreference(PREF_SCALE_PERCENT));
        mScalePercentPref.setLimits(100, 100);
        mScalePercentPref.getEditor().putInt(PREF_SCALE_PERCENT, 100).apply();

        mScanCustomLengthPref = (FloatNumberPickerPreference) findPreference(PREF_SCAN_CUSTOM_LENGTH);
        mScanCustomLengthPref.setLimits(0, 0);
        mScanCustomLengthPref.getEditor().putFloat(PREF_SCAN_CUSTOM_LENGTH, 0).apply();
        mScanCustomWidthPref = (FloatNumberPickerPreference) findPreference(PREF_SCAN_CUSTOM_WIDTH);
        mScanCustomWidthPref.setLimits(0, 0);
        mScanCustomWidthPref.getEditor().putFloat(PREF_SCAN_CUSTOM_WIDTH, 0).apply();

        mPrintCustomLengthPref = (FloatNumberPickerPreference) findPreference(PREF_PRINT_CUSTOM_LENGTH);
        mPrintCustomLengthPref.setLimits(0, 0);
        mPrintCustomLengthPref.getEditor().putFloat(PREF_PRINT_CUSTOM_LENGTH, 0).apply();
        mPrintCustomWidthPref = (FloatNumberPickerPreference) findPreference(PREF_PRINT_CUSTOM_WIDTH);
        mPrintCustomWidthPref.setLimits(0, 0);
        mPrintCustomWidthPref.getEditor().putFloat(PREF_PRINT_CUSTOM_WIDTH, 0).apply();

        mNumberUpDirectionPref = (ListPreference) findPreference(PREF_NUMBER_UP_DIRECTION);

        mOutputBinPref = (IconsListPreference) findPreference(PREF_OUTPUT_BIN);
        mProgressDialogModePref = (IconsListPreference) findPreference(PREF_PROGRESS_DIALOG_MODE);

        getPreferenceScreen().removePreference(mStoreCategory);
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
                    this, SettingsUIActivity.Operations.COPY, mClientApiLevel, CopyAttributesCaps.class).execute();
        }

        if (mDefaultCaps == null) {
            getPreferenceScreen().setEnabled(false);
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
        onSharedPreferenceChanged(prefs, PREF_COLOR_MODE);
        onSharedPreferenceChanged(prefs, PREF_ORIENTATION);
        onSharedPreferenceChanged(prefs, PREF_SCAN_DUPLEX_MODE);
        onSharedPreferenceChanged(prefs, PREF_SCAN_SIZE);
        onSharedPreferenceChanged(prefs, PREF_SCAN_CUSTOM_LENGTH);
        onSharedPreferenceChanged(prefs, PREF_SCAN_CUSTOM_WIDTH);
        onSharedPreferenceChanged(prefs, PREF_SCAN_SOURCE);
        onSharedPreferenceChanged(prefs, PREF_COPY_PREVIEW);
        onSharedPreferenceChanged(prefs, PREF_BACKGROUND_CLEANUP);
        onSharedPreferenceChanged(prefs, PREF_CONTRAST_ADJUSTMENT);
        onSharedPreferenceChanged(prefs, PREF_DARKNESS_ADJUSTMENT);
        onSharedPreferenceChanged(prefs, PREF_SHARPNESS_ADJUSTMENT);
        onSharedPreferenceChanged(prefs, PREF_PRINT_DUPLEX_MODE);
        onSharedPreferenceChanged(prefs, PREF_PRINT_SIZE);
        onSharedPreferenceChanged(prefs, PREF_PRINT_CUSTOM_LENGTH);
        onSharedPreferenceChanged(prefs, PREF_PRINT_CUSTOM_WIDTH);
        onSharedPreferenceChanged(prefs, PREF_COPIES);
        onSharedPreferenceChanged(prefs, PREF_COLLATE_MODE);
        onSharedPreferenceChanged(prefs, PREF_PAPER_SOURCE);
        onSharedPreferenceChanged(prefs, PREF_PAPER_TYPE);
        onSharedPreferenceChanged(prefs, PREF_SCALE_MODE);
        onSharedPreferenceChanged(prefs, PREF_SCALE_PERCENT);
        onSharedPreferenceChanged(prefs, PREF_TEXT_GRAPHICS_OPTIMIZATION);
        onSharedPreferenceChanged(prefs, PREF_JOB_ASSEMBLY_MODE);
        onSharedPreferenceChanged(prefs, PREF_JOB_EXECUTION_MODE);
        onSharedPreferenceChanged(prefs, PREF_NUMBER_UP_MODE);
        onSharedPreferenceChanged(prefs, PREF_NUMBER_UP_DIRECTION);
        onSharedPreferenceChanged(prefs, PREF_STORE_JOB_PASSWORD_TYPE);
        onSharedPreferenceChanged(prefs, PREF_OUTPUT_BIN);
        onSharedPreferenceChanged(prefs, PREF_PROGRESS_DIALOG_MODE);
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

        if (PREF_NUMBER_UP_MODE.equals(key)) {
            fillNumberUpDirectionAttrCaps(sharedPreferences);
        } else if (PREF_SCAN_SOURCE.equals(key)) {
            fillScalePercentRange(sharedPreferences);
        } else if (PREF_SCALE_MODE.equals(key)) {
            showScalePercentPreference(sharedPreferences, key);
            onSharedPreferenceChanged(sharedPreferences, PREF_SCALE_PERCENT);
        } else if (PREF_SCAN_SOURCE.equals(key)) {
            setScalePercentLimitsPerScanSource(sharedPreferences, key);
            onSharedPreferenceChanged(sharedPreferences, PREF_SCALE_PERCENT);
        } else if (PREF_SCAN_SIZE.equals(key)) {
            showOriginalCustomSizePreference(sharedPreferences, key);

            if (mCaps.getScanCustomLengthRange() != null) {
                mScanCustomLengthPref.setLimits(mCaps.getScanCustomLengthRange().getLowerBound(),
                        mCaps.getScanCustomLengthRange().getUpperBound());
                mScanCustomLengthPref.getEditor().putFloat(PREF_SCAN_CUSTOM_LENGTH, mCaps.getScanCustomLengthRange().getLowerBound()).apply();
            }

            if (mCaps.getScanCustomWidthRange() != null) {
                mScanCustomWidthPref.setLimits(mCaps.getScanCustomWidthRange().getLowerBound(),
                        mCaps.getScanCustomWidthRange().getUpperBound());
                mScanCustomWidthPref.getEditor().putFloat(PREF_SCAN_CUSTOM_WIDTH, mCaps.getScanCustomWidthRange().getLowerBound()).apply();
            }
            onSharedPreferenceChanged(sharedPreferences, PREF_SCAN_CUSTOM_LENGTH);
            onSharedPreferenceChanged(sharedPreferences, PREF_SCAN_CUSTOM_WIDTH);
        } else if (PREF_PRINT_SIZE.equals(key)) {
            showOutputCustomSizePreference(sharedPreferences, key);

            if (mCaps.getPrintCustomLengthRange() != null) {
                mPrintCustomLengthPref.setLimits(mCaps.getPrintCustomLengthRange().getLowerBound(),
                        mCaps.getPrintCustomLengthRange().getUpperBound());
                mPrintCustomLengthPref.getEditor().putFloat(PREF_PRINT_CUSTOM_LENGTH, mCaps.getPrintCustomLengthRange().getLowerBound()).apply();
            }

            if (mCaps.getPrintCustomWidthRange() != null) {
                mPrintCustomWidthPref.setLimits(mCaps.getPrintCustomWidthRange().getLowerBound(),
                        mCaps.getPrintCustomWidthRange().getUpperBound());
                mPrintCustomWidthPref.getEditor().putFloat(PREF_PRINT_CUSTOM_WIDTH, mCaps.getPrintCustomWidthRange().getLowerBound()).apply();
            }
            onSharedPreferenceChanged(sharedPreferences, PREF_PRINT_CUSTOM_LENGTH);
            onSharedPreferenceChanged(sharedPreferences, PREF_PRINT_CUSTOM_WIDTH);
        } else if (PREF_JOB_EXECUTION_MODE.equals(key)) {
            final String entryStr = ((ListPreference) preference).getValue();
            final JobExecutionMode entry =
                    entryStr == null ? JobExecutionMode.NORMAL : JobExecutionMode.valueOf(entryStr);

            if (entry == JobExecutionMode.STORE) {
                if (getPreferenceScreen().findPreference(PREF_DESTINATION_STORE_CATEGORY) == null) {
                    getPreferenceScreen().addPreference(mStoreCategory);
                }

                mJobNamePref = (EditTextPreference) findPreference(PREF_STORE_JOB_NAME);
                mJobNamePref.setText(null);
                mJobFolderNamePref = (EditTextPreference) findPreference(PREF_STORE_JOB_FOLDER_NAME);
                mJobFolderNamePref.setText(null);
                mJobPasswordPref = (EditTextPreference) findPreference(PREF_STORE_JOB_PASSWORD);
                mJobPasswordPref.setText(null);

                PreferenceHelper.fillValues(getActivity().getApplicationContext(),
                        Arrays.asList(CopyAttributes.RetentionMode.values()),
                        (ListPreference) findPreference(PREF_STORE_DELETE_ON_POWER),
                        0);

                PreferenceHelper.fillValues(getActivity().getApplicationContext(),
                        Arrays.asList(CopyAttributes.RetentionMode.values()),
                        (ListPreference) findPreference(PREF_STORE_DELETE_ON_RELEASE),
                        0);

                PreferenceHelper.fillValues(getActivity().getApplicationContext(),
                        mCaps.getPasswordTypeList(),
                        (ListPreference) findPreference(PREF_STORE_JOB_PASSWORD_TYPE),
                        0);

                onSharedPreferenceChanged(sharedPreferences, PREF_STORE_JOB_NAME);
                onSharedPreferenceChanged(sharedPreferences, PREF_STORE_JOB_FOLDER_NAME);
                onSharedPreferenceChanged(sharedPreferences, PREF_STORE_DELETE_ON_POWER);
                onSharedPreferenceChanged(sharedPreferences, PREF_STORE_DELETE_ON_RELEASE);
                onSharedPreferenceChanged(sharedPreferences, PREF_STORE_JOB_PASSWORD_TYPE);
                onSharedPreferenceChanged(sharedPreferences, PREF_STORE_JOB_PASSWORD);
            } else {
                getPreferenceScreen().removePreference(mStoreCategory);
            }
        }

        if (mClientApiLevel < Sdk.VERSION_LEVEL.SIX) {
            mBaseAttributesCategory.removePreference(mOutputBinPref);
            mBaseAttributesCategory.removePreference(mProgressDialogModePref);
        }

        setPreferenceValue(key);
    }

    public void setPreferenceValue(String key) {
        final Preference preference = findPreference(key);

        if (preference instanceof ListPreference) {
            final ListPreference listPref = (ListPreference) preference;
            final String entry = (String) listPref.getEntry();

            if (entry == null || entry.length() == 0) {
                listPref.setValueIndex(0);
                preference.setSummary("%s");
            } else {
                preference.setSummary(entry);
            }

            if (PREF_JOB_EXECUTION_MODE.equals(key)) {
                mJobExecutionMode = JobExecutionMode.valueOf(listPref.getValue());
            }
        } else if (preference instanceof EditTextPreference) {
            String text = ((EditTextPreference) preference).getText();
            EditText editText = ((EditTextPreference) preference).getEditText();

            if (TextUtils.isEmpty(text)) {
                preference.setSummary(text);
            } else {
                if (editText.getTransformationMethod() != null) {
                    text = editText.getTransformationMethod().getTransformation(text, editText).toString();
                }
                preference.setSummary(text);
            }
        } else if (preference instanceof NumberPickerPreference) {
            final Integer val = ((NumberPickerPreference) preference).getInteger();

            preference.setSummary(String.valueOf(val));
        } else if (preference instanceof FloatNumberPickerPreference) {
            final Float val = ((FloatNumberPickerPreference) preference).getFloat();

            preference.setSummary(String.valueOf(val));
        }
    }

    @Override
    public Intent getConfiguredIntent(String rid) {
        final SharedPreferences prefs = getPreferenceScreen().getSharedPreferences();
        final CopyToRequestIntent intent = new CopyToRequestIntent();
        CopyAttributes attrs = null;

        // Build attrs and create intent with updated data
        final CopyAttributes.ColorMode colorMode = CopyAttributes.ColorMode.valueOf(
                prefs.getString(PREF_COLOR_MODE,
                        CopyAttributes.ColorMode.DEFAULT.name()));

        final Orientation orientation = Orientation.valueOf(
                prefs.getString(PREF_ORIENTATION,
                        Orientation.DEFAULT.name()));

        final Duplex scanDuplex = Duplex.valueOf(
                prefs.getString(PREF_SCAN_DUPLEX_MODE,
                        Duplex.DEFAULT.name()));

        final ScanSize scanSize = ScanSize.valueOf(
                prefs.getString(PREF_SCAN_SIZE,
                        ScanSize.DEFAULT.name()));

        final float scanCustomLength = prefs.getFloat(PREF_SCAN_CUSTOM_LENGTH, 0);
        final float scanCustomWidth = prefs.getFloat(PREF_SCAN_CUSTOM_WIDTH, 0);

        final ScanSource scanSource = ScanSource.valueOf(
                prefs.getString(PREF_SCAN_SOURCE,
                        ScanSource.DEFAULT.name()));

        final CopyPreview copyPreview = CopyPreview.valueOf(
                prefs.getString(PREF_COPY_PREVIEW,
                        CopyPreview.DEFAULT.name()));

        final BackgroundCleanup backgroundCleanup = BackgroundCleanup.valueOf(
                prefs.getString(PREF_BACKGROUND_CLEANUP,
                        BackgroundCleanup.DEFAULT.name()));

        final ContrastAdjustment contrastAdjustment = ContrastAdjustment.valueOf(
                prefs.getString(PREF_CONTRAST_ADJUSTMENT,
                        ContrastAdjustment.DEFAULT.name()));

        final DarknessAdjustment darknessAdjustment = DarknessAdjustment.valueOf(
                prefs.getString(PREF_DARKNESS_ADJUSTMENT,
                        DarknessAdjustment.DEFAULT.name()));

        final SharpnessAdjustment sharpnessAdjustment = SharpnessAdjustment.valueOf(
                prefs.getString(PREF_SHARPNESS_ADJUSTMENT,
                        SharpnessAdjustment.DEFAULT.name()));

        final Duplex printDuplex = Duplex.valueOf(
                prefs.getString(PREF_PRINT_DUPLEX_MODE,
                        Duplex.DEFAULT.name()));

        final PaperSize printSize = PaperSize.valueOf(
                prefs.getString(PREF_PRINT_SIZE,
                        PaperSize.DEFAULT.name()));

        final float printCustomLength = prefs.getFloat(PREF_PRINT_CUSTOM_LENGTH, 0);
        final float printCustomWidth = prefs.getFloat(PREF_PRINT_CUSTOM_WIDTH, 0);

        final int copies = prefs.getInt(PREF_COPIES, 1);

        final CollateMode collateMode = CollateMode.valueOf(
                prefs.getString(PREF_COLLATE_MODE,
                        CollateMode.DEFAULT.name()));

        final PaperSource paperSource = PaperSource.valueOf(
                prefs.getString(PREF_PAPER_SOURCE,
                        PaperSource.DEFAULT.name()));

        final PaperType paperType = PaperType.valueOf(
                prefs.getString(PREF_PAPER_TYPE,
                        PaperType.DEFAULT.name()));

        final ScaleMode scaleMode = ScaleMode.valueOf(
                prefs.getString(PREF_SCALE_MODE,
                        ScaleMode.DEFAULT.name()));

        final TextGraphicsOptimization textGraphicsOptimization = TextGraphicsOptimization.valueOf(
                prefs.getString(PREF_TEXT_GRAPHICS_OPTIMIZATION,
                        TextGraphicsOptimization.DEFAULT.name()));

        final int scalePercent = prefs.getInt(PREF_SCALE_PERCENT, 100);

        final JobAssemblyMode jobAssemblyMode = JobAssemblyMode.valueOf(
                prefs.getString(PREF_JOB_ASSEMBLY_MODE,
                        JobAssemblyMode.DEFAULT.name()));

        final CopyAttributes.JobExecutionMode jobExecutionMode = CopyAttributes.JobExecutionMode.valueOf(
                prefs.getString(PREF_JOB_EXECUTION_MODE,
                        CopyAttributes.JobExecutionMode.NORMAL.name()));

        final NumberUpMode numberUpMode = NumberUpMode.valueOf(
                prefs.getString(PREF_NUMBER_UP_MODE,
                        NumberUpMode.DEFAULT.name()));

        final NumberUpDirection numberUpDirection = NumberUpDirection.valueOf(
                prefs.getString(PREF_NUMBER_UP_DIRECTION,
                        NumberUpDirection.DEFAULT.name()));

        final CopyAttributes.OutputBin outputBin = CopyAttributes.OutputBin.valueOf(
                prefs.getString(PREF_OUTPUT_BIN,
                        CopyAttributes.OutputBin.DEFAULT.name()));

        final CopyAttributes.ProgressDialogMode progressDialogMode = CopyAttributes.ProgressDialogMode.valueOf(
                prefs.getString(PREF_PROGRESS_DIALOG_MODE,
                        CopyAttributes.ProgressDialogMode.DEFAULT.name()));

        try {
            switch (jobExecutionMode) {
                case NORMAL:
                    attrs = new CopyAttributes.CopyBuilder()
                            .setColorMode(colorMode)
                            .setOrientation(orientation)
                            .setScanDuplex(scanDuplex)
                            .setScanSize(scanSize)
                            .setScanCustomLength(scanCustomLength)
                            .setScanCustomWidth(scanCustomWidth)
                            .setScanSource(scanSource)
                            .setCopyPreview(copyPreview)
                            .setBackgroundCleanup(backgroundCleanup)
                            .setContrastAdjustment(contrastAdjustment)
                            .setDarknessAdjustment(darknessAdjustment)
                            .setSharpnessAdjustment(sharpnessAdjustment)
                            .setPrintDuplex(printDuplex)
                            .setPrintSize(printSize)
                            .setPrintCustomLength(printCustomLength)
                            .setPrintCustomWidth(printCustomWidth)
                            .setCopies(copies)
                            .setCollateMode(collateMode)
                            .setPaperSource(paperSource)
                            .setPaperType(paperType)
                            .setScaleMode(scaleMode)
                            .setScalePercent(scalePercent)
                            .setTextGraphicsOptimization(textGraphicsOptimization)
                            .setJobAssemblyMode(jobAssemblyMode)
                            .setNumberUpMode(numberUpMode)
                            .setNumberUpDirection(numberUpDirection)
                            .setOutputBin(outputBin)
                            .setProgressDialogMode(progressDialogMode)
                            .build(mCaps);
                    break;

                case STORE:
                    final String storeJobName = prefs.getString(PREF_STORE_JOB_NAME, "");
                    final String storeJobFolderName = prefs.getString(PREF_STORE_JOB_FOLDER_NAME, "");
                    final CopyAttributes.RetentionMode deleteOnPower = CopyAttributes.RetentionMode.valueOf(
                            prefs.getString(PREF_STORE_DELETE_ON_POWER,
                                    CopyAttributes.RetentionMode.DEFAULT.name()));

                    final CopyAttributes.RetentionMode deleteOnRelease = CopyAttributes.RetentionMode.valueOf(
                            prefs.getString(PREF_STORE_DELETE_ON_RELEASE,
                                    CopyAttributes.RetentionMode.DEFAULT.name()));

                    final JobCredentialsAttributes.PasswordType storeJobPasswordType = JobCredentialsAttributes.PasswordType.valueOf(
                            prefs.getString(PREF_STORE_JOB_PASSWORD_TYPE,
                                    JobCredentialsAttributes.PasswordType.NONE.name()));

                    final String storeJobPassword = prefs.getString(PREF_STORE_JOB_PASSWORD, "");

                    CopyAttributes.StoreCopyBuilder storeCopyBuilder = new CopyAttributes.StoreCopyBuilder();

                    if (storeJobPasswordType != JobCredentialsAttributes.PasswordType.NONE) {
                        JobCredentialsAttributes jobCredentialsAttributes = new JobCredentialsAttributes.Builder()
                                .setPasswordType(storeJobPasswordType)
                                .setPassword(storeJobPassword)
                                .build();

                        storeCopyBuilder.setStoreJobCredentials(jobCredentialsAttributes);
                    }

                    attrs = storeCopyBuilder
                            .setStoreJobName(storeJobName)
                            .setStoreJobFolderName(storeJobFolderName)
                            .setRetentionModeOnPowerCycle(deleteOnPower)
                            .setRetentionModeOnRelease(deleteOnRelease)
                            .setColorMode(colorMode)
                            .setOrientation(orientation)
                            .setScanDuplex(scanDuplex)
                            .setScanSize(scanSize)
                            .setScanCustomLength(scanCustomLength)
                            .setScanCustomWidth(scanCustomWidth)
                            .setScanSource(scanSource)
                            .setCopyPreview(copyPreview)
                            .setBackgroundCleanup(backgroundCleanup)
                            .setContrastAdjustment(contrastAdjustment)
                            .setDarknessAdjustment(darknessAdjustment)
                            .setSharpnessAdjustment(sharpnessAdjustment)
                            .setPrintDuplex(printDuplex)
                            .setPrintSize(printSize)
                            .setPrintCustomLength(printCustomLength)
                            .setPrintCustomWidth(printCustomWidth)
                            .setCopies(copies)
                            .setCollateMode(collateMode)
                            .setPaperSource(paperSource)
                            .setPaperType(paperType)
                            .setScaleMode(scaleMode)
                            .setScalePercent(scalePercent)
                            .setTextGraphicsOptimization(textGraphicsOptimization)
                            .setJobAssemblyMode(jobAssemblyMode)
                            .setNumberUpMode(numberUpMode)
                            .setNumberUpDirection(numberUpDirection)
                            .setOutputBin(outputBin)
                            .setProgressDialogMode(progressDialogMode)
                            .build(mCaps);
                    break;

                default:
                    throw new IllegalStateException("Unsupported job execution mode");
            }
        } catch (CapabilitiesExceededException e) {
            SLog.e(TAG, "Build failed " + e.getMessage());
            mCallback.onFailedCreateIntent(getString(R.string.copy_creation_failed));
        }

        if (attrs != null) {
            final CopyToRequestIntent.IntentParams params =
                    new CopyToRequestIntent.IntentParams(attrs,
                            new CopyletAttributes.Builder().build(), null, null,
                            null, null, null, null);

            intent.putIntentParams(params);
        }

        return intent;
    }

    @Override
    public void onCapabilitiesLoaded(final CopyAttributesCaps caps, final String errorMsg) {
        if (caps == null || getActivity() == null) {
            mCallback.onFailed(errorMsg);
            return;
        }

        final SharedPreferences prefs = getPreferenceScreen().getSharedPreferences();
        final Context context = getActivity().getApplicationContext();
        ListPreference pref;

        mCaps = caps;

        // Load destinations
        pref = (ListPreference) findPreference(PREF_JOB_EXECUTION_MODE);
        final JobExecutionMode dest = mJobExecutionMode;
        PreferenceHelper.fillValues(context,
                caps.getJobExecutionModeList(),
                pref,
                0);
        pref.setValue(dest.name());
        pref.setSummary("%s");

        // Load color mode
        PreferenceHelper.fillValues(context,
                caps.getColorModeList(),
                (ListPreference) findPreference(PREF_COLOR_MODE),
                0);

        // Load orientation
        PreferenceHelper.fillValues(context,
                caps.getOrientationList(),
                (ListPreference) findPreference(PREF_ORIENTATION),
                0);

        // Load scan duplex mode
        PreferenceHelper.fillValues(context,
                caps.getScanDuplexList(),
                (IconsListPreference) findPreference(PREF_SCAN_DUPLEX_MODE),
                0);

        // Load scan media size
        PreferenceHelper.fillValues(context,
                caps.getScanSizeList(),
                (IconsListPreference) findPreference(PREF_SCAN_SIZE),
                R.string.scan_size_pattern);

        // Load scan source
        PreferenceHelper.fillValues(context,
                caps.getScanSourceList(),
                (ListPreference) findPreference(PREF_SCAN_SOURCE),
                0);

        // Load copy preview
        PreferenceHelper.fillValues(context,
                caps.getCopyPreviewList(),
                (ListPreference) findPreference(PREF_COPY_PREVIEW),
                R.string.scan_preview_pattern);

        // Load background cleanup
        PreferenceHelper.fillValues(context,
                caps.getBackgroundCleanupList(),
                (ListPreference) findPreference(PREF_BACKGROUND_CLEANUP),
                0);

        //
        // Load contrast adjustment
        PreferenceHelper.fillValues(context,
                caps.getContrastAdjustmentList(),
                (ListPreference) findPreference(PREF_CONTRAST_ADJUSTMENT),
                0);

        // Load darkness adjustment
        PreferenceHelper.fillValues(context,
                caps.getDarknessAdjustmentList(),
                (ListPreference) findPreference(PREF_DARKNESS_ADJUSTMENT),
                0);

        // Load sharpness adjustment
        PreferenceHelper.fillValues(context,
                caps.getSharpnessAdjustmentList(),
                (ListPreference) findPreference(PREF_SHARPNESS_ADJUSTMENT),
                0);

        // Load print duplex mode
        PreferenceHelper.fillValues(context,
                caps.getPrintDuplexList(),
                (IconsListPreference) findPreference(PREF_PRINT_DUPLEX_MODE),
                0);

        // Load print size
        PreferenceHelper.fillValues(context,
                caps.getPrintSizeList(),
                (IconsListPreference) findPreference(PREF_PRINT_SIZE),
                R.string.print_size_pattern);

        // Load collate mode
        PreferenceHelper.fillValues(context,
                caps.getCollateModeList(),
                (ListPreference) findPreference(PREF_COLLATE_MODE),
                R.string.pref_collate_mode);

        // Load paper source
        PreferenceHelper.fillValues(context,
                caps.getPaperSourceList(),
                (ListPreference) findPreference(PREF_PAPER_SOURCE),
                R.string.pref_paper_source);

        // Load paper type
        PreferenceHelper.fillValues(context,
                caps.getPaperTypeList(),
                (ListPreference) findPreference(PREF_PAPER_TYPE),
                R.string.print_type_pattern);

        // Load scale mode
        PreferenceHelper.fillValues(context,
                caps.getScaleModeList(),
                (ListPreference) findPreference(PREF_SCALE_MODE),
                R.string.pref_scale_mode);

        // Load scale mode
        PreferenceHelper.fillValues(context,
                caps.getTextGraphicsOptimizationList(),
                (ListPreference) findPreference(PREF_TEXT_GRAPHICS_OPTIMIZATION),
                R.string.pref_textGraphicsOptimization);

        // Load job assembly mode
        PreferenceHelper.fillValues(context,
                caps.getJobAssemblyModeList(),
                (ListPreference) findPreference(PREF_JOB_ASSEMBLY_MODE),
                0);

        // Load number up mode
        PreferenceHelper.fillValues(context,
                caps.getNumberUpModeList(),
                (ListPreference) findPreference(PREF_NUMBER_UP_MODE),
                0);

        // Load output bin
        PreferenceHelper.fillValues(context,
                caps.getOutputBinList(),
                (ListPreference) findPreference(PREF_OUTPUT_BIN),
                0);

        // Load progress dialog mode
        PreferenceHelper.fillValues(context,
                caps.getProgressDialogModeList(),
                (ListPreference) findPreference(PREF_PROGRESS_DIALOG_MODE),
                0);

        // Apply Copies limits
        if (caps.getCopiesRange() != null) {
            mCopiesPref.setLimits(caps.getCopiesRange().getLowerBound(), caps.getCopiesRange().getUpperBound());
        }

        if (caps.getScanCustomLengthRange() != null) {
            mScanCustomLengthPref.setLimits(caps.getScanCustomLengthRange().getLowerBound(),
                    caps.getScanCustomLengthRange().getUpperBound());
        }

        if (caps.getScanCustomWidthRange() != null) {
            mScanCustomWidthPref.setLimits(caps.getScanCustomWidthRange().getLowerBound(),
                    caps.getScanCustomWidthRange().getUpperBound());
        }

        if (caps.getPrintCustomLengthRange() != null) {
            mPrintCustomLengthPref.setLimits(caps.getPrintCustomLengthRange().getLowerBound(),
                    caps.getPrintCustomLengthRange().getUpperBound());
        }

        if (caps.getPrintCustomWidthRange() != null) {
            mPrintCustomWidthPref.setLimits(caps.getPrintCustomWidthRange().getLowerBound(),
                    caps.getPrintCustomWidthRange().getUpperBound());
        }

        if (mSavedState != null) {
            ((ListPreference) findPreference(PREF_COLOR_MODE))
                    .setValue(mSavedState.getString(PREF_COLOR_MODE, ColorMode.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_ORIENTATION))
                    .setValue(mSavedState.getString(PREF_ORIENTATION, Orientation.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_SCAN_DUPLEX_MODE))
                    .setValue(mSavedState.getString(PREF_SCAN_DUPLEX_MODE, Duplex.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_SCAN_SIZE))
                    .setValue(mSavedState.getString(PREF_SCAN_SIZE, ScanSize.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_SCAN_SOURCE))
                    .setValue(mSavedState.getString(PREF_SCAN_SOURCE, ScanSource.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_COPY_PREVIEW))
                    .setValue(mSavedState.getString(PREF_COPY_PREVIEW, CopyPreview.DEFAULT.name()));

            ((ListPreference) findPreference(PREF_BACKGROUND_CLEANUP))
                    .setValue(mSavedState.getString(PREF_BACKGROUND_CLEANUP, BackgroundCleanup.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_CONTRAST_ADJUSTMENT))
                    .setValue(mSavedState.getString(PREF_CONTRAST_ADJUSTMENT, ContrastAdjustment.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_DARKNESS_ADJUSTMENT))
                    .setValue(mSavedState.getString(PREF_DARKNESS_ADJUSTMENT, DarknessAdjustment.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_SHARPNESS_ADJUSTMENT))
                    .setValue(mSavedState.getString(PREF_SHARPNESS_ADJUSTMENT, SharpnessAdjustment.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_PRINT_DUPLEX_MODE))
                    .setValue(mSavedState.getString(PREF_PRINT_DUPLEX_MODE, PaperSize.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_PRINT_SIZE))
                    .setValue(mSavedState.getString(PREF_PRINT_SIZE, PaperSize.DEFAULT.name()));
            ((NumberPickerPreference) findPreference(PREF_COPIES))
                    .setInteger(mSavedState.getInt(PREF_COPIES, 1));
            ((ListPreference) findPreference(PREF_COLLATE_MODE))
                    .setValue(mSavedState.getString(PREF_COLLATE_MODE, CollateMode.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_PAPER_SOURCE))
                    .setValue(mSavedState.getString(PREF_PAPER_SOURCE, PaperSource.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_SCALE_MODE))
                    .setValue(mSavedState.getString(PREF_SCALE_MODE, ScaleMode.DEFAULT.name()));
            ((NumberPickerPreference) findPreference(PREF_SCALE_PERCENT))
                    .setInteger(mSavedState.getInt(PREF_SCALE_PERCENT, 100));
            ((ListPreference) findPreference(PREF_JOB_ASSEMBLY_MODE))
                    .setValue(mSavedState.getString(PREF_JOB_ASSEMBLY_MODE, JobAssemblyMode.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_JOB_EXECUTION_MODE))
                    .setValue(mSavedState.getString(PREF_JOB_EXECUTION_MODE, JobExecutionMode.NORMAL.name()));
            ((ListPreference) findPreference(PREF_NUMBER_UP_MODE))
                    .setValue(mSavedState.getString(PREF_NUMBER_UP_MODE, NumberUpMode.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_NUMBER_UP_DIRECTION))
                    .setValue(mSavedState.getString(PREF_NUMBER_UP_DIRECTION, NumberUpDirection.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_OUTPUT_BIN))
                    .setValue(mSavedState.getString(PREF_OUTPUT_BIN, CopyAttributes.OutputBin.DEFAULT.name()));
            ((ListPreference) findPreference(PREF_PROGRESS_DIALOG_MODE))
                    .setValue(mSavedState.getString(PREF_PROGRESS_DIALOG_MODE, CopyAttributes.ProgressDialogMode.DEFAULT.name()));
        }

        refreshAllPrefs(prefs);

        // Enable preferences for editing
        getPreferenceScreen().setEnabled(true);
    }

    @Override
    public void onDefaultCapabilitiesLoaded(CopyAttributes defaultCaps, String errorMsg) {
        if (defaultCaps == null || getActivity() == null) {
            mCallback.onFailed(errorMsg);
            return;
        }

        // Enable preferences for editing
        getPreferenceScreen().setEnabled(true);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mCaps != null) {
            final SharedPreferences prefs = getPreferenceScreen().getSharedPreferences();

            // Save preference values to restore after caps get again
            outState.putString(PREF_COLOR_MODE, prefs.getString(PREF_COLOR_MODE, ColorMode.DEFAULT.name()));
            outState.putString(PREF_ORIENTATION, prefs.getString(PREF_ORIENTATION, Orientation.DEFAULT.name()));
            outState.putString(PREF_SCAN_DUPLEX_MODE, prefs.getString(PREF_SCAN_DUPLEX_MODE, Duplex.DEFAULT.name()));
            outState.putString(PREF_SCAN_SIZE, prefs.getString(PREF_SCAN_SIZE, ScanSize.DEFAULT.name()));
            outState.putString(PREF_SCAN_SOURCE, prefs.getString(PREF_SCAN_SOURCE, ScanSource.DEFAULT.name()));
            outState.putString(PREF_COPY_PREVIEW, prefs.getString(PREF_COPY_PREVIEW, CopyPreview.DEFAULT.name()));

            outState.putString(PREF_BACKGROUND_CLEANUP, prefs.getString(PREF_BACKGROUND_CLEANUP, BackgroundCleanup.DEFAULT.name()));
            outState.putString(PREF_CONTRAST_ADJUSTMENT, prefs.getString(PREF_CONTRAST_ADJUSTMENT, ContrastAdjustment.DEFAULT.name()));
            outState.putString(PREF_DARKNESS_ADJUSTMENT, prefs.getString(PREF_DARKNESS_ADJUSTMENT, DarknessAdjustment.DEFAULT.name()));
            outState.putString(PREF_SHARPNESS_ADJUSTMENT, prefs.getString(PREF_SHARPNESS_ADJUSTMENT, SharpnessAdjustment.DEFAULT.name()));
            outState.putString(PREF_PRINT_DUPLEX_MODE, prefs.getString(PREF_PRINT_DUPLEX_MODE, Duplex.DEFAULT.name()));
            outState.putString(PREF_PRINT_SIZE, prefs.getString(PREF_PRINT_SIZE, PaperSize.DEFAULT.name()));
            outState.putInt(PREF_COPIES, prefs.getInt(PREF_COPIES, 1));
            outState.putString(PREF_COLLATE_MODE, prefs.getString(PREF_COLLATE_MODE, CollateMode.DEFAULT.name()));
            outState.putString(PREF_PAPER_SOURCE, prefs.getString(PREF_PAPER_SOURCE, PaperSource.DEFAULT.name()));
            outState.putString(PREF_SCALE_MODE, prefs.getString(PREF_SCALE_MODE, ScaleMode.DEFAULT.name()));
            outState.putInt(PREF_SCALE_PERCENT, prefs.getInt(PREF_SCALE_PERCENT, 100));

            outState.putString(PREF_JOB_ASSEMBLY_MODE, prefs.getString(PREF_JOB_ASSEMBLY_MODE, JobAssemblyMode.DEFAULT.name()));
            outState.putString(PREF_JOB_EXECUTION_MODE, prefs.getString(PREF_JOB_EXECUTION_MODE, JobExecutionMode.NORMAL.name()));
            outState.putString(PREF_NUMBER_UP_MODE, prefs.getString(PREF_NUMBER_UP_MODE, NumberUpMode.DEFAULT.name()));
            outState.putString(PREF_NUMBER_UP_DIRECTION, prefs.getString(PREF_NUMBER_UP_DIRECTION, NumberUpDirection.DEFAULT.name()));
            outState.putString(PREF_OUTPUT_BIN, prefs.getString(PREF_OUTPUT_BIN, CopyAttributes.OutputBin.DEFAULT.name()));
            outState.putString(PREF_PROGRESS_DIALOG_MODE, prefs.getString(PREF_PROGRESS_DIALOG_MODE, CopyAttributes.ProgressDialogMode.DEFAULT.name()));
        }
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSavedState = savedInstanceState;
    }

    private void fillNumberUpDirectionAttrCaps(SharedPreferences sharedPreferences) {
        final NumberUpMode numberUpMode = NumberUpMode.valueOf(sharedPreferences.getString(PREF_NUMBER_UP_MODE, NumberUpMode.DEFAULT.name()));

        if (numberUpMode != NumberUpMode.DEFAULT) {
            mBaseAttributesCategory.addPreference(mNumberUpDirectionPref);

            if (mCaps != null) {
                // Load number up direction
                PreferenceHelper.fillValues(getActivity().getApplicationContext(),
                        mCaps.getNumberUpDirectionByNumberUpCount().get(numberUpMode),
                        mNumberUpDirectionPref,
                        0);
            }
        } else {
            mBaseAttributesCategory.removePreference(mNumberUpDirectionPref);
        }
    }

    private void fillScalePercentRange(SharedPreferences sharedPreferences) {
        final ScanSource scanSource = ScanSource.valueOf(sharedPreferences.getString(PREF_SCAN_SOURCE, ScanSource.DEFAULT.name()));

        if (mCaps != null) {
            Range scalePercentRange = mCaps.getScalePercentRangeByScanSource().get(scanSource);
            mScalePercentPref.setLimits(scalePercentRange.getLowerBound(), scalePercentRange.getUpperBound());
        } else {
            mScalePercentPref.setLimits(100, 100);
        }
    }

    private void showOriginalCustomSizePreference(SharedPreferences preferences, String key) {
        ScanSize scanSize = ScanSize.valueOf(preferences.getString(key, ScanSize.DEFAULT.name()));

        if (scanSize == ScanSize.CUSTOM) {
            mBaseAttributesCategory.addPreference(mScanCustomLengthPref);
            mBaseAttributesCategory.addPreference(mScanCustomWidthPref);
        } else {
            mBaseAttributesCategory.removePreference(mScanCustomLengthPref);
            mBaseAttributesCategory.removePreference(mScanCustomWidthPref);
        }
    }

    private void showOutputCustomSizePreference(SharedPreferences preferences, String key) {
        PaperSize printSize = PaperSize.valueOf(preferences.getString(key, PaperSize.DEFAULT.name()));

        if (printSize == PaperSize.CUSTOM) {
            mBaseAttributesCategory.addPreference(mPrintCustomLengthPref);
            mBaseAttributesCategory.addPreference(mPrintCustomWidthPref);
        } else {
            mBaseAttributesCategory.removePreference(mPrintCustomLengthPref);
            mBaseAttributesCategory.removePreference(mPrintCustomWidthPref);
        }
    }

    private void showScalePercentPreference(SharedPreferences preferences, String key) {
        ScaleMode scaleMode = ScaleMode.valueOf(preferences.getString(key, ScaleMode.DEFAULT.name()));

        if (scaleMode == ScaleMode.MANUAL) {
            mBaseAttributesCategory.addPreference(mScalePercentPref);
        } else {
            mBaseAttributesCategory.removePreference(mScalePercentPref);
        }
    }

    private void setScalePercentLimitsPerScanSource(SharedPreferences preferences, String key) {
        ScanSource scanSource = ScanSource.valueOf(preferences.getString(key, ScanSource.DEFAULT.name()));

        if (mCaps != null) {
            Range scalePercentRange = mCaps.getScalePercentRangeByScanSource().get(scanSource);
            mScalePercentPref.setLimits(scalePercentRange.getLowerBound(), scalePercentRange.getUpperBound());
        }
    }
}