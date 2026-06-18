// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.settingsui.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.hp.jetadvantage.link.services.settingsui.R;
import com.hp.jetadvantage.link.services.settingsui.SettingsUIActivity;

public class MarginsPreference extends DialogPreference {

    EditText mLeftMarginEditText;
    EditText mTopMarginEditText;
    EditText mRightMarginEditText;
    EditText mBottomMarginEditText;

    float mLeft = 0.0f;
    float mTop = 0.0f;
    float mRight = 0.0f;
    float mBottom = 0.0f;

    float mLeftMin = 0.0f;
    float mTopMin = 0.0f;
    float mRightMin = 0.0f;
    float mBottomMin = 0.0f;

    float mLeftMax = 0.0f;
    float mTopMax = 0.0f;
    float mRightMax = 0.0f;
    float mBottomMax = 0.0f;

    public MarginsPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Will store values itself
        setPersistent(false);
        setDialogLayoutResource(R.layout.layout_margins);
    }

    @Override
    protected void onBindDialogView(final View view) {
        super.onBindDialogView(view);
        mLeftMarginEditText = view.findViewById(R.id.leftEditText);
        mTopMarginEditText = view.findViewById(R.id.topEditText);
        mRightMarginEditText = view.findViewById(R.id.rightEditText);
        mBottomMarginEditText = view.findViewById(R.id.bottomEditText);
        loadMargins();
    }

    @Override
    protected void onDialogClosed(final boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            try {
                setMargins(getFloatValue(mLeftMarginEditText),
                        getFloatValue(mTopMarginEditText),
                        getFloatValue(mRightMarginEditText),
                        getFloatValue(mBottomMarginEditText));
                applyMargins();
            } catch (Exception e) {
                if (e.getMessage() != null)
                    Log.e(SettingsUIActivity.TAG, e.getMessage());
            }
        }
    }

    public void applyMargins() throws Exception {
        if (!isInRange(mLeftMin, mLeftMax, mLeft)) {
            throw new Exception(getContext().getString(R.string.range_margin, mLeftMin, mLeftMax));
        }
        if (!isInRange(mTopMin, mTopMax, mTop)) {
            throw new Exception(getContext().getString(R.string.range_margin, mTopMin, mTopMax));
        }
        if (!isInRange(mRightMin, mRightMax, mRight)) {
            throw new Exception(getContext().getString(R.string.range_margin, mRightMin, mRightMax));
        }
        if (!isInRange(mBottomMin, mBottomMax, mBottom)) {
            throw new Exception(getContext().getString(R.string.range_margin, mBottomMin, mBottomMax));
        }
        saveMargins();
    }

    private void loadMargins() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        mLeft = sharedPref.getFloat(getKey() + "Left", 0.0f);
        mTop = sharedPref.getFloat(getKey() + "Top", 0.0f);
        mRight = sharedPref.getFloat(getKey() + "Right", 0.0f);
        mBottom = sharedPref.getFloat(getKey() + "Bottom", 0.0f);

        mLeftMarginEditText.setText(String.valueOf(getLeft()));
        mTopMarginEditText.setText(String.valueOf(getTop()));
        mRightMarginEditText.setText(String.valueOf(getRight()));
        mBottomMarginEditText.setText(String.valueOf(getBottom()));
        setMarginSummary();
    }

    public void clean() {
        mLeft = mTop = mRight = mBottom = 0;
        saveMargins();
    }

    public void setMarginSummary() {
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                setSummary(getContext().getString(R.string.summary_margin, mLeft, mTop, mRight, mBottom));
            }
        };
        handler.post(r);
    }

    private void saveMargins() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(getKey() + "Left", mLeft);
        editor.putFloat(getKey() + "Top", mTop);
        editor.putFloat(getKey() + "Right", mRight);
        editor.putFloat(getKey() + "Bottom", mBottom);
        editor.apply();
        setMarginSummary();
    }

    public void setLeftLimits(final float min, final float max) {
        mLeftMin = min;
        mLeftMax = max;
    }

    public void setTopLimits(final float min, final float max) {
        mTopMin = min;
        mTopMax = max;
    }

    public void setRightLimits(final float min, final float max) {
        mRightMin = min;
        mRightMax = max;
    }

    public void setBottomLimits(final float min, final float max) {
        mBottomMin = min;
        mBottomMax = max;
    }

    public void setMargins(float left, float top, float right, float bottom) {
        this.mLeft = left;
        this.mTop = top;
        this.mRight = right;
        this.mBottom = bottom;
    }

    public float getLeft() {
        return mLeft;
    }

    public float getTop() {
        return mTop;
    }

    public float getRight() {
        return mRight;
    }

    public float getBottom() {
        return mBottom;
    }

    private boolean isInRange(final float min, final float max, final float value) {
        return max > min ? value >= min && value <= max : value >= max && value <= min;
    }

    private float getFloatValue(EditText editText) {
        float value = 0.0f;
        try {
            if (editText != null && !TextUtils.isEmpty(editText.getText().toString())) {
                value = Float.parseFloat(editText.getText().toString());
            }
        } catch (Exception e) {
        }
        return value;
    }
}