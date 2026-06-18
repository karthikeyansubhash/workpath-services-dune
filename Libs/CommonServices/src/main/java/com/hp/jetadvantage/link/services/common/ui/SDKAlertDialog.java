// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.hp.jetadvantage.link.services.common.R;

public class SDKAlertDialog extends Activity {
    private static final String TAG = "AlertDialog";
    public static final String KEY_ERROR_TITLE = "key_error_title";
    public static final String KEY_ERROR_MSG = "key_error_msg";

    private String mErrorTitle;
    private String mErrorMsg;

    private TextView mErrorTitleTextView;
    private TextView mErrorMsgTextView;
    private Button mOKButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.dialog_error);

        final Intent intent = getIntent();
        mErrorTitle = intent.getStringExtra(KEY_ERROR_TITLE);
        mErrorMsg = intent.getStringExtra(KEY_ERROR_MSG);
        initView();
    }

    private void initView() {
        mErrorTitleTextView = findViewById(R.id.tv_title);
        mErrorTitleTextView.setText(mErrorTitle);
        mErrorMsgTextView = findViewById(R.id.tv_message);
        mErrorMsgTextView.setText(mErrorMsg);
        mOKButton = findViewById(R.id.btn_ok);
        mOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDKAlertDialog.this.finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void showErrorDialog(final Context context, final String title, final String errorMsg) {
        final Intent intent = new Intent(context, SDKAlertDialog.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(KEY_ERROR_TITLE, title);
        intent.putExtra(KEY_ERROR_MSG, errorMsg);
        context.startActivity(intent);
    }
}