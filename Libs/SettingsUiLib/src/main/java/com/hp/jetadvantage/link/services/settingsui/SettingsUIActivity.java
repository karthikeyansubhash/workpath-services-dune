// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.settingsui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.copier.Copylet;
import com.hp.jetadvantage.link.api.copier.CopyletAttributes;
import com.hp.jetadvantage.link.api.copier.intent.CopyToRequestIntent;
import com.hp.jetadvantage.link.api.printer.Printlet;
import com.hp.jetadvantage.link.api.printer.PrintletAttributes;
import com.hp.jetadvantage.link.api.printer.intent.PrintRequestIntent;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.Scanlet;
import com.hp.jetadvantage.link.api.scanner.ScanletAttributes;
import com.hp.jetadvantage.link.api.scanner.intent.ScanToRequestIntent;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.common.ssp.AbstractReporter;
import com.hp.jetadvantage.link.services.settingsui.fragments.CopyConfigurationFragment;
import com.hp.jetadvantage.link.services.settingsui.fragments.PrintConfigurationFragment;
import com.hp.jetadvantage.link.services.settingsui.fragments.ScanConfigurationFragment;
import com.hp.jetadvantage.link.services.settingsui.interfaces.ConfigurationCallback;
import com.hp.jetadvantage.link.services.settingsui.interfaces.ConfigurationFragment;

/**
 * Main activity to host all Settings fragments
 */
public final class SettingsUIActivity extends AppCompatActivity implements ConfigurationCallback {

    public static final String TAG = "SettingsUi";

    private static final String SETTINGS_FRAGMENT = "SettingsFragment";
    private static final String KEY_RID = "Rid";

    private Operations mOperation = Operations.SCAN_TO_ME;
    /**
     * Submitted job Rid
     */
    private String mRid = null;
    private String mClientPackage = null;
    private Integer mClientApiLevel = null;
    private ConfigurationFragment mFragment;
    /**
     * Start [operation] button
     */
    private FloatingActionButton mStartButton;
    /**
     * Configured intent from corresponding configure fragment
     */
    private Intent mConfiguredIntent = null;
    private String mApplicationId = null;

    public enum Operations {
        SCAN_TO_ME(R.string.scan_options, R.string.start_scan),
        PRINT(R.string.print_options, R.string.start_print),
        FAX(R.string.fax_options, R.string.start_fax),
        COPY(R.string.copy_options, R.string.start_copy);

        /**
         * Res Id for title to be displayed for this operation
         */
        public int titleResId = 0;
        /**
         * Res Id for start button for this operation
         */
        public int btnResId = 0;

        Operations(final int titleResId, final int btnResId) {
            this.titleResId = titleResId;
            this.btnResId = btnResId;
        }
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        // Check called interface
        final Intent intent = getIntent();

        if (intent == null || !intent.hasExtra("reqIDExtra")) {
            SLog.e(TAG, "Invalid input parameters, finish");
            finish();
            return;
        }

        final String action = intent.getAction();

        if (ScanToRequestIntent.ACTION.equals(action)) {
            mOperation = Operations.SCAN_TO_ME;
        } else if (PrintRequestIntent.ACTION.equals(action)) {
            mOperation = Operations.PRINT;
        } else if (CopyToRequestIntent.ACTION.equals(action)) {
            mOperation = Operations.COPY;
        } else {
            SLog.e(TAG, "Invalid input intent, finish");
            finish();
            return;
        }

        setContentView(R.layout.activity_settings_ui);

        ImageButton ivBack = findViewById(R.id.iv_back);
        TextView tvTitle = findViewById(R.id.tv_title);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText(mOperation.titleResId);

        mStartButton = findViewById(R.id.start_operation_button);
        setFloatingButtonBackground(mStartButton);
        if (mOperation.equals(Operations.PRINT)) {
            mStartButton.setImageResource(R.drawable.ic_print);
        } else {
            mStartButton.setImageResource(R.drawable.ic_scan);
        }

        if (mStartButton != null) {
            mStartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    mConfiguredIntent = mFragment.getConfiguredIntent(mRid);
                    finish();
                }
            });
        }

        if (savedInstanceState == null) {
            provideSettingsFragment(intent);
        } else {
            mRid = savedInstanceState.getString(KEY_RID);
            mFragment = (ConfigurationFragment) getFragmentManager().findFragmentByTag(SETTINGS_FRAGMENT);
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_RID, mRid);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mConfiguredIntent != null) {
            startOperation();
        }
    }

    /**
     * Launches execution of the job.
     * It obtains jobs attributes and submit it.
     */
    private void startOperation() {
        Intent resultIntent = null;

        if (mConfiguredIntent == null || mConfiguredIntent.getExtras() == null) {
            SLog.w(TAG, "No intent received from xlet");
            return;
        }

        switch (mOperation) {
            case SCAN_TO_ME: {
                if (!(mConfiguredIntent instanceof ScanToRequestIntent)) {
                    SLog.w(TAG, "Invalid intent received from xlet");
                    return;
                }

                final ScanToRequestIntent letIntent = (ScanToRequestIntent) mConfiguredIntent;
                final ScanToRequestIntent.IntentParams params = letIntent.getIntentParams();

                if (params == null) {
                    SLog.w(TAG, "Invalid intent received from xlet");
                    return;
                }

                final ScanletAttributes letAttrs = new ScanletAttributes.Builder()
                        .setShowSettingsUi(false).build();
                final ScanToRequestIntent.IntentParams outParams =
                        new ScanToRequestIntent.IntentParams(params.getScanAttributes(),
                                letAttrs, mRid, mClientPackage, null, mApplicationId,
                                null, null, mClientApiLevel);

                resultIntent = new ScanToRequestIntent().putIntentParams(outParams);
            }
            break;

            case PRINT: {
                if (!(mConfiguredIntent instanceof PrintRequestIntent)) {
                    SLog.w(TAG, "Invalid intent received from xlet");
                    return;
                }

                final PrintRequestIntent letIntent = (PrintRequestIntent) mConfiguredIntent;
                final PrintRequestIntent.IntentParams params = letIntent.getIntentParams();

                if (params == null) {
                    SLog.w(TAG, "Invalid intent received from xlet");
                    return;
                }

                final PrintletAttributes letAttrs = new PrintletAttributes.Builder()
                        .setShowSettingsUi(false).build();
                final PrintRequestIntent.IntentParams outParams =
                        new PrintRequestIntent.IntentParams(params.getPrintAttributes(),
                                letAttrs, mRid, mClientPackage, null, mApplicationId,
                                null, null, mClientApiLevel);

                resultIntent = new PrintRequestIntent().putIntentParams(outParams);
            }
            break;

            case COPY: {
                if (!(mConfiguredIntent instanceof CopyToRequestIntent)) {
                    SLog.w(TAG, "Invalid intent received from xlet");
                    return;
                }

                final CopyToRequestIntent letIntent = (CopyToRequestIntent) mConfiguredIntent;
                final CopyToRequestIntent.IntentParams params = letIntent.getIntentParams();

                if (params == null) {
                    SLog.w(TAG, "Invalid intent received from xlet");
                    return;
                }

                final CopyletAttributes letAttrs = new CopyletAttributes.Builder()
                        .setShowSettingsUi(false).build();
                final CopyToRequestIntent.IntentParams outParams =
                        new CopyToRequestIntent.IntentParams(params.getCopyAttributes(),
                                letAttrs, mRid, mClientPackage, mApplicationId,
                                null, null, mClientApiLevel);

                resultIntent = new CopyToRequestIntent().putIntentParams(outParams);
            }
            break;

            default:
                SLog.e(TAG, "Invalid operation");
                break;
        }

        finish();

        if (resultIntent != null) {
            resultIntent.setPackage(Sdk.SERVICES_PACKAGE);
            getApplicationContext().sendOrderedBroadcast(resultIntent, null);
        }
    }

    /**
     * Installs current operations settings fragment to be displayed
     */
    private void provideSettingsFragment(final Intent intent) {
        PreferenceFragment fragment = null;

        switch (mOperation) {
            case SCAN_TO_ME: {
                final ScanToRequestIntent.IntentParams params = ScanToRequestIntent.getIntentParams(intent);
                mClientPackage = params.getPackageName();
                mClientApiLevel = params.getApiLevel();
                mRid = params.getReqId();
                mApplicationId = params.getApplicationId();
                fragment = ScanConfigurationFragment.newInstance(params.getScanAttributes(),
                        ScanAttributes.Destination.ME, mClientApiLevel);
            }
            break;

            case PRINT: {
                final PrintRequestIntent.IntentParams params = PrintRequestIntent.getIntentParams(intent);
                mClientPackage = params.getPackageName();
                mClientApiLevel = params.getApiLevel();
                mRid = params.getReqId();
                mApplicationId = params.getApplicationId();
                fragment = PrintConfigurationFragment.newInstance(params.getPrintAttributes(), mClientApiLevel);
            }
            break;

            case COPY: {
                final CopyToRequestIntent.IntentParams params = CopyToRequestIntent.getIntentParams(intent);
                mClientPackage = params.getPackageName();
                mClientApiLevel = params.getApiLevel();
                mRid = params.getReqId();
                mApplicationId = params.getApplicationId();
                fragment = CopyConfigurationFragment.newInstance(params.getCopyAttributes(), mClientApiLevel);
            }
            break;

            default:
                SLog.e(TAG, "Failed to open settings ui");
                break;
        }

        if (fragment != null) {
            mFragment = (ConfigurationFragment) fragment;
        }
        getFragmentManager().beginTransaction().add(R.id.mainFragmentContainer, fragment, SETTINGS_FRAGMENT).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onFailed(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(msg)) {
                    Toast.makeText(SettingsUIActivity.this, msg, Toast.LENGTH_LONG).show();
                }

                finish();

                final AbstractReporter reporter = getNotify();

                if (!TextUtils.isEmpty(mClientPackage) && !Sdk.SERVICES_PACKAGE.equals(mClientPackage)) {
                    reporter.setTargetPackage(mClientPackage);
                }
                reporter.fail(getApplicationContext(), mRid, Result.RESULT_FAIL, msg);
            }
        });
    }

    @Override
    public void onFailedCreateIntent(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(msg)) {
                    Toast.makeText(SettingsUIActivity.this, msg, Toast.LENGTH_LONG).show();
                }
                // Don't finish, just let user correct data
            }
        });
    }

    @Override
    public void enableActionButton(final boolean enable) {
        if (mStartButton != null) {
            mStartButton.setEnabled(enable);
        }
    }

    /**
     * Creates Notify which corresponds to requested job
     *
     * @return {@link com.hp.jetadvantage.link.services.common.ssp.AbstractReporter}
     */
    private AbstractReporter getNotify() {
        String tag = null;
        Uri contentUri = null;
        String action = null;

        switch (mOperation) {
            case SCAN_TO_ME:
                action = Scanlet.ACTION;
                tag = Scanlet.TAG;
                contentUri = Scanlet.CONTENT_URI;
                break;

            case PRINT:
                action = Printlet.ACTION;
                tag = Printlet.TAG;
                contentUri = Printlet.CONTENT_URI;
                break;

            case COPY:
                action = Copylet.ACTION;
                tag = Copylet.TAG;
                contentUri = Copylet.CONTENT_URI;
                break;

            default:
                // Seems to be impossible here
                break;
        }

        return new AbstractReporter(action, contentUri, tag) {
        };
    }

    private void setFloatingButtonBackground(FloatingActionButton button) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_pressed},
                new int[]{-android.R.attr.state_pressed},
                new int[]{android.R.attr.state_focused},
                new int[]{-android.R.attr.state_pressed}
        };

        int[] colors = new int[]{
                ContextCompat.getColor(this, R.color.floating_button_pressed),
                ContextCompat.getColor(this, R.color.floating_button_normal),
                ContextCompat.getColor(this, R.color.floating_button_pressed),
                ContextCompat.getColor(this, R.color.floating_button_normal)
        };
        button.setBackgroundTintList(new ColorStateList(states, colors));
    }
}
