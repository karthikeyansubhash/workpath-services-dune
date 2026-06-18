// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hp.jetadvantage.link.services.common.R;

/**
 * Dialog fragment for self permissions request - request androids permissions
 */
public class SpsSelfPermissionDialogFragment extends AppCompatDialogFragment {

    /**
     * Callback to parent activity
     */
    public interface SelfPermissionDialogListener {
        /**
         * Once permission finished
         */
        void onSelfPermissionFinish();
    }

    /**
     * Default factory
     *
     * @return create fragment
     */
    public static SpsSelfPermissionDialogFragment newInstance() {
        final SpsSelfPermissionDialogFragment frag = new SpsSelfPermissionDialogFragment();

        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(false);
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.sps_self_permission)
                .setMessage(R.string.sps_sd_card)
                .setPositiveButton(R.string.grant,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                final boolean hasWritePermission = (ContextCompat.checkSelfPermission(getContext(),
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                                final boolean hasReadPermission = (ContextCompat.checkSelfPermission(getContext(),
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
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

                                if (permissions != null) {
                                    ActivityCompat.requestPermissions(getActivity(),
                                            permissions,
                                            SpsPermissionActivity.REQUEST_STORAGE_PERMISSIONS);
                                } else {
                                    // no dialog is needed, since permissions are fine, just proceed
                                    ((SelfPermissionDialogListener)getActivity()).onSelfPermissionFinish();
                                }
                            }
                        }
                )
                .setNegativeButton(R.string.deny,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((SelfPermissionDialogListener)getActivity()).onSelfPermissionFinish();
                            }
                        }
                )
                .setCancelable(false)
                .create();
    }

    @Override
    public void onCancel(final DialogInterface dialog) {
        super.onCancel(dialog);
        ((SelfPermissionDialogListener)getActivity()).onSelfPermissionFinish();
    }
}
