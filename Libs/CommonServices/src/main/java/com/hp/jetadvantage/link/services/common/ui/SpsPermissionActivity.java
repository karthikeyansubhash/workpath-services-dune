// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.common.R;

/**
 * Permission confirmation activity
 */
public class SpsPermissionActivity extends AppCompatActivity implements SpsSelfPermissionDialogFragment.SelfPermissionDialogListener {

    private static final String TAG = "SpsPermissionActivity";

    private static final String DIALOG = "dialog";
    public static final int REQUEST_STORAGE_PERMISSIONS = 1;

    /** Extra to hold callers package, might be missed of equals to self package */
    public static String EXTRA_CALLER_PACKAGE = "com.hp.jetadvantage.link.services.common.ui.CALLER_PACKAGE";
    public static String EXTRA_CALLER_ENV = "env";

    private AppCompatDialogFragment mDialog = null;
    private String mCallerPackage = null;
    private String mCallerEnv = "FALSE";
    private boolean mShowGrantDialogOnResume = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        final Intent intent = getIntent();

        if (intent == null) {
            SLog.e(TAG, "Invalid activity parameters were provided");
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        mCallerPackage = intent.getStringExtra(EXTRA_CALLER_PACKAGE);

        if (TextUtils.isEmpty(mCallerPackage)) {
            SLog.e(TAG, "Invalid activity parameters were provided");
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        if (intent.hasExtra(EXTRA_CALLER_ENV)) {
            mCallerEnv = intent.getStringExtra(EXTRA_CALLER_ENV);
        } else {
            mCallerEnv = "FALSE";
        }

        mDialog = (AppCompatDialogFragment) getSupportFragmentManager().findFragmentByTag(DIALOG);

        if (mDialog == null) {
            final boolean hasWritePermission = (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            final boolean hasReadPermission = (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

            if (!hasWritePermission || !hasReadPermission) {
                String[] permissions = null;

                if (!hasWritePermission && !hasReadPermission) {
                    permissions = new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.SYSTEM_ALERT_WINDOW,
                            Manifest.permission.FOREGROUND_SERVICE
                    };
                } else {
                    if (!hasWritePermission) {
                        permissions = new String[] {
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        };
                    } else if (!hasReadPermission) {
                        permissions = new String[] {
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        };
                    }
                }

                if(!TextUtils.isEmpty(mCallerEnv) && "TRUE".equalsIgnoreCase(mCallerEnv)) { //panel >= oreo
                    if (permissions != null) {
                        try {
                            ActivityCompat.requestPermissions(this,
                                    permissions,
                                    SpsPermissionActivity.REQUEST_STORAGE_PERMISSIONS);
                        } catch (Exception e) {
                            SLog.e(TAG, "Permission request is failed with " + e.getMessage());
                        }
                    } else {
                    }
                } else { //emulator or mobile
                    mDialog = SpsSelfPermissionDialogFragment.newInstance();
                    mDialog.show(getSupportFragmentManager(), DIALOG);
                }
            }
        }
    }

    @Override
    public void onSelfPermissionFinish() {
        // Proceed with next dialog
        if (mDialog != null) {
            mDialog.dismiss();
        }

        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mShowGrantDialogOnResume) {
            //reload my activity with permission granted or use the features what required the permission
            onSelfPermissionFinish();
            mShowGrantDialogOnResume = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSIONS: {
                this.finish();

                boolean granted = false;

                if (grantResults.length > 0) {
                    granted = true;

                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            granted = false;
                        }
                    }
                }

                if (granted) {
                    mShowGrantDialogOnResume = true;
                } else {
                    Toast.makeText(this, R.string.permission_not_granted, Toast.LENGTH_LONG).show();
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }

            default:
                break;
        }
    }
}
