// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.common.mfpsversion.helper;

import android.content.ContentResolver;
import android.os.Bundle;

import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.mfpsversion.data.MFPSVersionCP;
import com.hp.jetadvantage.link.common.utils.Preconditions;
import com.hp.jetadvantage.link.common.utils.SLog;

/**
 * Assistance in accessing the version information without having to know about
 * all of the storage internals
 * 
 * @since API 1
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Version {
    private static final String LOG_TAG = Version.class.getSimpleName();

    private Version() {
        // Non-instantiable class (utility / helper)
    }

    /**
     * Retrieve the version of the MFP Services app.
     * Otherwise, null.
     * 
     * @param cr
     *        The Android content resolver
     * @return The version information of the MFP Services app
     * 
     * @since API 1
     */
    public static String get(final ContentResolver cr) {
        SLog.i(LOG_TAG, "Getting version");

        Preconditions.checkNotNull(cr, "Content resolver must be provided to get the mfp services version");

        Bundle bundle = null;

        try {
            bundle = cr.call(MFPSVersionCP.CONTENT_URI, MFPSVersionCP.Method.GET_VERSION, null, null);
        } catch (IllegalArgumentException iae) {
            // Didn't exist, so ignoring
        }

        if (null != bundle) {
            return bundle.getString(MFPSVersionCP.Contract.KEY_VERSION);
        }

        return Sdk.VERSION.NO_VERSION;
    }
}
