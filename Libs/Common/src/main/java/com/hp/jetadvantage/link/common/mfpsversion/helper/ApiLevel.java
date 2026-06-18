// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.common.mfpsversion.helper;

import android.content.ContentResolver;
import android.os.Bundle;

import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.mfpsversion.data.MFPSVersionCP;
import com.hp.jetadvantage.link.common.utils.Preconditions;

/**
 * Assistance in accessing the API level information without having to know about
 * all of the storage internals
 * 
 * @since API 1
 */
@SuppressWarnings({"unused"})
public class ApiLevel {
    private ApiLevel() {
        // Non-instantiable class (utility / helper)
    }

    /**
     * Retrieve the API level of the MFP Services app.
     * Otherwise, null.
     * 
     * @param cr
     *        The Android content resolver
     * @return The API level information of the MFP Services app
     * 
     * @since API 1
     */
    public static int get(final ContentResolver cr) {
        Preconditions.checkNotNull(cr, "Content resolver must be provided to get the mfp services version");

        // Just to ensure that our provider is alive and started to deliver
        cr.getType(MFPSVersionCP.CONTENT_URI);

        // Get SPS version
        final Bundle extra = new Bundle();
        extra.putString(MFPSVersionCP.Contract.KEY_API_NAME_LEVEL, String.valueOf(Sdk.VERSION.VERSION_NAME));
        Bundle bundle = cr.call(MFPSVersionCP.CONTENT_URI, MFPSVersionCP.Method.GET_API_LEVEL, String.valueOf(Sdk.VERSION.LEVEL), extra);

        if (null != bundle) {
            return bundle.getInt(MFPSVersionCP.Contract.KEY_API_LEVEL);
        }

        return Sdk.VERSION_LEVEL.UNDEFINED;
    }

    /**
     * Retrieve the API level of the MFP Services app without checking the HP JetAdvantage Link SDK permission.
     * This method should be used in sdk inside.
     * Otherwise, null.
     *
     * @param cr
     *        The Android content resolver
     * @return The API level information of the MFP Services app
     *
     * @since API 1
     */
    public static int getInternal(final ContentResolver cr) {
        Preconditions.checkNotNull(cr, "Content resolver must be provided to get the mfp services version");

        // Just to ensure that our provider is alive and started to deliver
        cr.getType(MFPSVersionCP.CONTENT_URI);

        // Get SPS version
        Bundle bundle = cr.call(MFPSVersionCP.CONTENT_URI, MFPSVersionCP.Method.GET_API_LEVEL_INTERNAL, String.valueOf(Sdk.VERSION.LEVEL), null);

        if (null != bundle) {
            return bundle.getInt(MFPSVersionCP.Contract.KEY_API_LEVEL);
        }

        return Sdk.VERSION_LEVEL.UNDEFINED;
    }
}
