// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.settingsui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.hp.jetadvantage.link.api.SsdkUnsupportedException;
import com.hp.jetadvantage.link.common.Platform;
import com.hp.jetadvantage.link.services.settingsui.R;

/**
 * Fragment to show error related to incorrect JetAdvantage Link SDK initialization,
 * which can be caused by missed
 */
public final class InitializationErrorDialogFragment extends DialogFragment {
    private static final String TAG = "InitializationError";
    private static final String SDK_UNSUPPORTED_EXCEPTION_TYPE_ARG = "SsdkUnsupportedException";
    private static final String SDK_SECURITY_EXCEPTION = "SecurityException";

    /**
     * Create a new instance of InitializationErrorDialogFragment.
     *
     * @param e {@link Exception} to use as argument
     *
     * @return created DialogFragment
     */
    public static InitializationErrorDialogFragment newInstance(final Exception e) {
        InitializationErrorDialogFragment f = new InitializationErrorDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();

        if (e instanceof SsdkUnsupportedException) {
            args.putInt(SDK_UNSUPPORTED_EXCEPTION_TYPE_ARG, ((SsdkUnsupportedException) e).getType());
        } else if (e instanceof SecurityException) {
            args.putString(SDK_SECURITY_EXCEPTION, e.getMessage());
        }
        f.setArguments(args);

        f.setCancelable(false);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle args = getArguments();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (args.containsKey(SDK_UNSUPPORTED_EXCEPTION_TYPE_ARG)) {
            final int type = args.getInt(SDK_UNSUPPORTED_EXCEPTION_TYPE_ARG);

            switch (type) {
                case SsdkUnsupportedException.LIBRARY_NOT_INSTALLED:
                case SsdkUnsupportedException.LIBRARY_UPDATE_IS_REQUIRED:
                    // Query whether the user needs to install / update SDK
                    if (Platform.isPanel()) {
                        builder.setMessage(R.string.sdk_support_missing);
                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int which) {
                                getActivity().finish();
                            }
                        });
                    }
                    break;

                default:
                    Log.e(TAG, "Received invalid exception type");
                    builder.setMessage(getString(R.string.unknown_error) + type);
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            getActivity().finish();
                        }
                    });
                    break;
            }
        } else if (args.containsKey(SDK_SECURITY_EXCEPTION)) {
            final String errorText = args.getString(SDK_SECURITY_EXCEPTION, "Security Error");

            builder.setMessage(errorText);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    getActivity().finish();
                }
            });
        }

        return builder.create();
    }
}
