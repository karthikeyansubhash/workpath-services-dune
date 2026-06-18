// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.settingsui.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.hp.jetadvantage.link.services.settingsui.R;

/**
 * {@link android.preference.ListPreference} to display icons on the right of list items.
 */
public class IconsListPreference extends ListPreference {

    private final Context mContext;
//    private TextView mSummary;
    /** Icons to be displayed along with entries */
    private int[] mIcons;
    /** Checked value */
    private String mCheckedEntry = "";

    /**
     * Holds information about single Icons List item
     */
    private static class IconsItem {
        int iconId;
        String name;
    }

    /**
     * Common Androids view holder
     */
    private static class ViewHolder {
        RadioButton btnRadio;
        ImageView icon;
        TextView text;
        int index;
    }

    /**
     * Special adapter for list with icons
     */
    private class IconsListAdapter extends ArrayAdapter<IconsItem> {

        private final Context mContext;

        /**
         * Default constructor
         *
         * @param context {@link Context}
         * @param objects List of {@link IconsItem} to be displayed
         */
        public IconsListAdapter(final Context context, final List<IconsItem> objects) {
            super(context, R.layout.preference_icon_list, objects);
            mContext = context;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View view = convertView;
            ViewHolder holder = null;
            final IconsItem item = getItem(position);

            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.preference_icon_list, parent, false);
                view.setOnClickListener(mItemsViewClickListener);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            if (holder == null) {
                holder = new ViewHolder();
                holder.text = (TextView) view.findViewById(R.id.text);
                holder.btnRadio = (RadioButton) view.findViewById(R.id.btn_radio);
                holder.icon = (ImageView) view.findViewById(R.id.icon);
                view.setTag(holder);
            }

            holder.text.setText(item.name);
            holder.btnRadio.setChecked(mCheckedEntry.equals(item.name));

            if (item.iconId != 0) {
                holder.icon.setVisibility(View.VISIBLE);
                holder.icon.setImageResource(item.iconId);
            } else {
                holder.icon.setVisibility(View.GONE);
            }
            holder.index = position;

            return view;
        }
    }

    /**
     * Default constructor
     *
     * @param context {@link Context}
     * @param attrs {@link AttributeSet} which contains properties
     */
    public IconsListPreference(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        // Will store result by itself
        setPersistent(false);
        mContext = context;
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

//        mSummary = (TextView) view.findViewById(android.R.id.summary);
//        mSummary.setText(getEntry());
    }

    @Override
    protected void onDialogClosed(final boolean positiveResult) {
        super.onDialogClosed(positiveResult);

//        if (positiveResult) {
//            getEditor().putString(getKey(), getValue()).apply();
//            mSummary.setText(getEntry());
//        }
    }

    private DialogInterface.OnClickListener mItemsClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
            mCheckedEntry = (String) getEntryValues()[which];
            setValue(mCheckedEntry);
            getDialog().dismiss();
        }
    };

    private View.OnClickListener mItemsViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final ViewHolder holder = (ViewHolder) v.getTag();

            mCheckedEntry = (String) getEntryValues()[holder.index];
            setValue(mCheckedEntry);
            getDialog().dismiss();
        }
    };

    @Override
    protected void onPrepareDialogBuilder(final AlertDialog.Builder builder) {
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(null, null);

        final CharSequence[] entries = getEntries();
        final List<IconsItem> icons = new ArrayList<>();

        for (int i = 0; i < entries.length; i++) {
            final IconsItem item = new IconsItem();

            if (mIcons != null && mIcons.length > i) {
                item.iconId = mIcons[i];
            }

            item.name = (String) entries[i];
            icons.add(item);
        }

        mCheckedEntry = (String) getEntry();
        builder.setAdapter(new IconsListAdapter(mContext, icons), mItemsClickListener);
    }

    /**
     * Setter for icon resource ids.
     * The oder should correspond to order of entries.
     *
     * @param iconsIds array of icons ids
     */
    public void setIcons(final int[] iconsIds) {
        mIcons = iconsIds;
    }

    @Override
    public void setValue(final String value) {
        super.setValue(value);

        final SharedPreferences.Editor editor = getEditor();

        editor.putString(getKey(), value);
        editor.apply();
    }

    @Override
    public void setValueIndex(final int index) {
        super.setValueIndex(index);

        if (index >= 0 && index < getEntryValues().length) {
            setValue(String.valueOf(getEntryValues()[index]));
        }
    }
}
