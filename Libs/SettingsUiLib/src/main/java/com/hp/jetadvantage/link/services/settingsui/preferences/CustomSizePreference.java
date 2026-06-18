// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.settingsui.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.preference.DialogPreference;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.services.settingsui.R;

import java.math.BigDecimal;

/**
 * Special Dialog Entry which provides two buttons and accepts
 */
public class CustomSizePreference extends DialogPreference {

    private float mValue = 0;

    private float mMaxSize;
    private float mMinSize;

    private EditText mValueText = null;
    private TextView mMinValue = null;
    private TextView mMaxValue = null;
    private SeekBar mValueBar = null;

    private int barRange;

    /**
     * Default constructor
     *
     * @param context {@link Context}
     * @param attrs {@link AttributeSet} which contains properties
     */
    public CustomSizePreference(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        // Will store values itself
        setPersistent(false);
        setDialogLayoutResource(R.layout.preference_custom_size);
    }

    @Override
    protected void onBindDialogView(final View view) {
        super.onBindDialogView(view);

        mMinValue = view.findViewById(R.id.tv_min);
        mMaxValue = view.findViewById(R.id.tv_max);

        if (mMaxValue != null) {
            mMaxValue.setText(String.valueOf(mMaxSize));
        }

        if (mMinValue != null) {
            mMinValue.setText(String.valueOf(mMinSize));
        }

        mValueBar = view.findViewById(R.id.bar_value);
        mValueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    float value = calculateBarValue(progress);
                    if (value < mMinSize) {
                        value = mMinSize;
                    } else if (value > mMaxSize) {
                        value = mMaxSize;
                    }

                    mValueText.setText(String.valueOf(value));
                    mValue = value;

                    Log.e("test","seekbar max: " + barRange + " progress: " + progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mValueText = view.findViewById(R.id.value);
        mValueText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if(!TextUtils.isEmpty(s.toString())) {
                        float value = Float.parseFloat(s.toString());
                        String valueString = String.format("%.2f", value);
                        mValue = Float.parseFloat(valueString);
                    } else {
                        mValue = 0;
                    }
                    int progressValue = calculateProgressValue(mValue);
                    if (mValueBar.getProgress() != progressValue) {
                        mValueBar.setProgress(progressValue);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        if (mValueBar != null) {
            mValueBar.setMax(barRange);
        }

        if (mMinSize >= mMaxSize) {
            mValue = mMinSize;
            mValueText.setText(String.valueOf(mValue));
        } else {
            mValue = getSharedPreferences().getFloat(getKey(), 1);
        }

        validateAndSetValue();
    }

    /**
     * Validates value for limits, sets buttons enable / disable state
     */
    private void validateAndSetValue() {
        if (mValue >= mMaxSize) {
            mValue = mMaxSize;
        }

        if (mValue <= mMinSize) {
            mValue = mMinSize;
        }

        if (mValueText != null) {
            mValueText.setText(String.valueOf(mValue));
        }

        if (mValueBar != null) {
            int progressValue = calculateProgressValue(mValue);
            mValueBar.setProgress(progressValue);
        }
    }

    /**
     * Sets maximum ande minimum possible to enter value
     *
     * @param downLimit bottom / down limit
     * @param upLimit upper limit
     */
    @SuppressLint("RestrictedApi")
    public void setLimits(final float downLimit, final float upLimit) {
        Preconditions.checkArgument(upLimit >= downLimit, "Upper limit shouldn't be lower than down!");

        mMaxSize = upLimit;
        mMinSize = downLimit;

        if (mValue < downLimit) {
            mValue = downLimit;
        } else if (mValue > upLimit) {
            mValue = downLimit;
        }

        if (downLimit >= upLimit) {
            mValue = downLimit;
        }

        BigDecimal max = new BigDecimal(String.valueOf(mMaxSize));
        BigDecimal min = new BigDecimal(String.valueOf(mMinSize));
        BigDecimal hundred = new BigDecimal("100");

        max = max.multiply(hundred);
        min = min.multiply(hundred);

        barRange = max.subtract(min).intValue();
    }

    private float calculateBarValue(int progress){
        BigDecimal pg = new BigDecimal(String.valueOf(progress));
        BigDecimal divide = new BigDecimal("0.01");
        BigDecimal min = new BigDecimal(String.valueOf(mMinSize));

        return pg.multiply(divide).add(min).floatValue();
    }

    private int calculateProgressValue(float value){
        BigDecimal val = new BigDecimal(String.valueOf(value));
        BigDecimal multi = new BigDecimal("100");
        BigDecimal min = new BigDecimal(String.valueOf(mMinSize));

        int progress = val.subtract(min).multiply(multi).intValue();
        if(progress < 0) return 0;
        else if(progress > barRange) return barRange;
        else return progress;
    }

    @Override
    protected void onDialogClosed(final boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            if (mValue > mMaxSize || mValue < mMinSize) {
                Toast.makeText(getContext(),
                        String.format(getContext().getString(R.string.custom_range), mMinSize, mMaxSize),
                        Toast.LENGTH_LONG).show();
            } else {
                setValue(mValue);
            }
        }
    }

    /**
     * @return current preference value
     */
    public float getValue() {
        return mValue;
    }

    /**
     * @param value to be set to preference
     */
    public void setValue(final float value) {
        mValue = value;
        validateAndSetValue();
        getEditor().putFloat(getKey(), mValue).apply();
        setSummary(String.valueOf(mValue));
    }
}
