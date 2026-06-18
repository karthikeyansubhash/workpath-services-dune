// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.settingsui.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.preference.DialogPreference;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.services.settingsui.R;

/**
 * Special Dialog Entry which provides two buttons and accepts
 */
public class NumberPickerPreference extends DialogPreference {

    private int mValue = 1;

    private int mUpLimit = 9999;
    private int mDownLimit = 1;

    private ImageButton mPlus = null;
    private ImageButton mMinus = null;
    private EditText mValueText = null;

    /**
     * Default constructor
     *
     * @param context {@link Context}
     * @param attrs   {@link AttributeSet} which contains properties
     */
    public NumberPickerPreference(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        // Will store values itself
        setPersistent(false);
        setDialogLayoutResource(R.layout.preference_number_picker);

        mValue = 1;
    }

    @Override
    protected void onBindDialogView(final View view) {
        super.onBindDialogView(view);

        mPlus = (ImageButton) view.findViewById(R.id.plus);
        mMinus = (ImageButton) view.findViewById(R.id.minus);
        mValueText = (EditText) view.findViewById(R.id.value);
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
                    int value = Integer.parseInt(s.toString());
                    if (value < mDownLimit || value > mUpLimit) {
                        setBeforeValue();
                    } else {
                        mValue = value;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private void setBeforeValue() {
                mValueText.setText(Integer.toString(mValue));
                mValueText.setSelection(Integer.toString(mValue).length());
                Toast.makeText(getContext(),
                        String.format(getContext().getString(R.string.copies_range), mDownLimit, mUpLimit),
                        Toast.LENGTH_LONG).show();
            }
        });

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.length() > 0) {
                    for (int i = start; i < end; i++) {
                        if (!Character.isDigit(source.charAt(i))) {
                            return "";
                        }
                    }
                }
                return null;
            }
        };
        mValueText.setFilters(new InputFilter[]{filter});

        mPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mValue++;
                validateAndSetValue();
                mValueText.setSelection(Integer.toString(mValue).length());
            }
        });

        mMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mValue--;
                validateAndSetValue();
                mValueText.setSelection(Integer.toString(mValue).length());
            }
        });

        if (mDownLimit >= mUpLimit) {
            mMinus.setEnabled(false);
            mPlus.setEnabled(false);
            mValue = mDownLimit;
            mValueText.setText(String.valueOf(mValue));
        } else {
            mValue = getSharedPreferences().getInt(getKey(), 1);
        }

        validateAndSetValue();
    }

    /**
     * Validates value for limits, sets buttons enable / disable state
     */
    private void validateAndSetValue() {
        if (mValue >= mUpLimit) {
            if (mPlus != null) {
                mPlus.setEnabled(false);
            }
            mValue = mUpLimit;
        } else {
            if (mPlus != null) {
                mPlus.setEnabled(true);
            }
        }

        if (mValue <= mDownLimit) {
            if (mMinus != null) {
                mMinus.setEnabled(false);
            }
            mValue = mDownLimit;
        } else {
            if (mMinus != null) {
                mMinus.setEnabled(true);
            }
        }
        if (mValueText != null) {
            mValueText.setText(String.valueOf(mValue));
        }
    }

    /**
     * Sets maximum ande minimum possible to enter value
     *
     * @param downLimit bottom / down limit
     * @param upLimit   upper limit
     */
    @SuppressLint("RestrictedApi")
    public void setLimits(final int downLimit, final int upLimit) {
        Preconditions.checkArgument(upLimit >= downLimit, "Upper limit shouldn't be lower than down!");

        mUpLimit = upLimit;
        mDownLimit = downLimit;

        if (mValue < downLimit) {
            mValue = downLimit;
            if (mMinus != null) {
                mMinus.setEnabled(false);
            }
        } else if (mValue > upLimit) {
            mValue = downLimit;
        }

        if (downLimit >= upLimit) {
            if (mMinus != null) {
                mMinus.setEnabled(false);
            }
            if (mPlus != null) {
                mPlus.setEnabled(false);
            }
            mValue = downLimit;
        }

        if (mValueText != null) {
            mValueText.setText(String.valueOf(mValue));
        }
    }

    @Override
    protected void onDialogClosed(final boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            getEditor().putInt(getKey(), mValue).apply();
            setSummary(String.valueOf(mValue));
        }
    }

    /**
     * @return current preference value
     */
    public Integer getInteger() {
        return mValue;
    }

    /**
     * @param value to be set to preference
     */
    public void setInteger(final Integer value) {
        mValue = value;
        validateAndSetValue();
        getEditor().putInt(getKey(), mValue).apply();
        setSummary(String.valueOf(mValue));
    }
}
