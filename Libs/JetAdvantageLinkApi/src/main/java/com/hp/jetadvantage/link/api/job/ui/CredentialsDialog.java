// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.job.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.hp.jetadvantage.link.common.annotation.MobileApi;

/**
 * @hide The client need not know the internals of Credentials Dialogs handling on client side
 */
@MobileApi
public class CredentialsDialog extends DialogFragment {
    @MobileApi
    public interface CredentialsDialogCallback {
        void onOkClick(String userName, String password);
        void ooCancelClick();
    }

    private CredentialsDialogCallback mDialogCallback;

    public static CredentialsDialog newInstance(CredentialsDialogCallback dialogCallback) {
        CredentialsDialog frag = new CredentialsDialog();
        frag.mDialogCallback = dialogCallback;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context context = getActivity();

        View prompt = LayoutInflater.from(context).inflate(getResources()
                .getIdentifier("fragment_credentials", "layout", context.getPackageName()), null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(prompt);
        final EditText userNameText = prompt.findViewById(
                getResources().getIdentifier("username", "id", context.getPackageName()));

        final EditText passwordText = prompt.findViewById(
                getResources().getIdentifier("password", "id", context.getPackageName()));

        alertDialogBuilder
                .setTitle(getString(getResources().getIdentifier("credentials_dialog_title", "string", context.getPackageName())))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (TextUtils.isEmpty(userNameText.getText())) {
                            userNameText.setError(getString(getResources().getIdentifier("credentials_dialog_required", "string", context.getPackageName())));
                        } else {
                            userNameText.setError(null);

                            if (mDialogCallback != null) {
                                mDialogCallback.onOkClick(userNameText.getText().toString(), passwordText.getText().toString());
                            }
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (mDialogCallback != null) {
                            mDialogCallback.ooCancelClick();
                        }
                    }
                });

        return alertDialogBuilder.create();
    }
}