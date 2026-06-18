// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.settingsui.helper;

import android.content.Context;
import android.content.res.Resources;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;

import com.hp.jetadvantage.link.services.settingsui.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Simple helper to wrap common Configure components utils
 */
public final class PreferenceHelper {

    /**
     * Callback interface for icons holders
     *
     * @param <E> type of the enum used in the list
     */
    public interface IconsHandler<E extends Enum<E>> {
        /**
         * Determines icon res id for provided value
         *
         * @param value to determine icon for
         * @return icon res id or 0
         */
        int getIcon(E value);
    }

    /**
     * Fills {@param listPreference} with provided values, provided strings,
     * and icons.
     *
     * TODO: i18n support should be added
     *
     * @param context        {@link Context} to get strings
     * @param capsList       {@link List} with E capabilities
     * @param listPreference {@link ListPreference} to be filled
     * @param stringPattern  res id for pattern string, if 0, then Camel will be used
     * @param <E>            type of enum to fill preference with
     */
    public static <E extends Enum<E>> void fillValues(final Context context,
                                                      final List<E> capsList,
                                                      final ListPreference listPreference,
                                                      final int stringPattern) {
        final Resources res = context.getResources();
        final ArrayList<CharSequence> entries = new ArrayList<CharSequence>();
        final ArrayList<CharSequence> entryValues = new ArrayList<CharSequence>();
        final String pattern = stringPattern == 0 ? null : res.getString(stringPattern);
        final String packageName = context.getPackageName();
        final String defaultVal = res.getString(R.string.pref_value_default);
        final int size = capsList.size();

        for (int i = 0; i < size; i++) {
            final E value = capsList.get(i);

            if (pattern != null &&
                    !value.name().equalsIgnoreCase(res.getString(R.string.default_value))) {
                final int stringId = res.getIdentifier(
                        String.format(pattern, value.name()), "string", packageName);

                if (stringId > 0) {
                    entries.add(res.getString(stringId));
                } else if (stringPattern == R.string.normal_format) {
                    entries.add(value.name());
                } else {
                    entries.add(capitalizeFirstRemoveUnderscores(value.name()));
                }
            } else {
                entries.add(capitalizeFirstRemoveUnderscores(value.name()));
            }
            entryValues.add(value.name());
        }

        if (listPreference != null) {
            listPreference.setEntries(entries.toArray(new CharSequence[entries.size()]));
            listPreference.setEntryValues(entryValues.toArray(new CharSequence[entryValues.size()]));

            listPreference.setDefaultValue(defaultVal);
            listPreference.setValueIndex(0);
            listPreference.setSummary("%s");
        }
    }

    /**
     * Fills {@param multiSelectListPreference} with provided values, provided strings,
     * and icons.
     *
     * TODO: i18n support should be added
     *
     * @param context                   {@link Context} to get strings
     * @param capsList                  {@link List} with E capabilities
     * @param multiSelectListPreference {@link MultiSelectListPreference} to be filled
     * @param stringPattern             res id for pattern string, if 0, then Camel will be used
     * @param <E>                       type of enum to fill preference with
     */
    public static <E extends Enum<E>> void fillValues(final Context context,
                                                      final List<E> capsList,
                                                      final MultiSelectListPreference multiSelectListPreference,
                                                      final int stringPattern) {
        final Resources res = context.getResources();
        final ArrayList<CharSequence> entries = new ArrayList<CharSequence>();
        final ArrayList<CharSequence> entryValues = new ArrayList<CharSequence>();
        final String pattern = stringPattern == 0 ? null : res.getString(stringPattern);
        final String packageName = context.getPackageName();
        final String defaultVal = res.getString(R.string.pref_value_default);
        final int size = capsList.size();

        for (int i = 0; i < size; i++) {
            final E value = capsList.get(i);

            if (pattern != null &&
                    !value.name().equalsIgnoreCase(res.getString(R.string.default_value))) {
                final int stringId = res.getIdentifier(
                        String.format(pattern, value.name()), "string", packageName);

                if (stringId > 0) {
                    entries.add(res.getString(stringId));
                } else if (stringPattern == R.string.normal_format) {
                    entries.add(value.name());
                } else {
                    entries.add(capitalizeFirstRemoveUnderscores(value.name()));
                }
            } else {
                entries.add(capitalizeFirstRemoveUnderscores(value.name()));
            }
            entryValues.add(value.name());
        }

        multiSelectListPreference.setEntries(entries.toArray(new CharSequence[entries.size()]));
        multiSelectListPreference.setEntryValues(entryValues.toArray(new CharSequence[entryValues.size()]));

        Set<String> sets = new HashSet<>();
        multiSelectListPreference.setDefaultValue(defaultVal);
        sets.add(res.getString(R.string.pref_value_default));
        multiSelectListPreference.setValues(sets);
        multiSelectListPreference.setSummary("%s");
    }

    /**
     * Capitalize first letter and replace '_' symbols by spaces.
     *
     * @param str source
     * @return modified string
     */
    private static CharSequence capitalizeFirstRemoveUnderscores(final String str) {
        return (str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase()).replace('_', ' ');
    }
}
