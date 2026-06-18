// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.settingsui.preferences;

import android.content.Context;
import android.preference.Preference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LongSummaryPreference extends Preference{

    public LongSummaryPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LongSummaryPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LongSummaryPreference(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        TextView summary = view.findViewById(android.R.id.summary);
        if (summary != null) {
            summary.setSingleLine();
            summary.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        }
    }
}
