// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.mod;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;

import com.hp.jetadvantage.link.api.SsdkUnsupportedException;

/**
 * @hide AMF
 */
public class Mod {
    private final String ERROR_MSG = "Service is not supported.";

    private static Mod instance;

    public static final String DENSITY_LOW = "x0.75";
    public static final String DENSITY_MEDIUM = "x1";
    public static final String DENSITY_HIGH = "x2";
    public static final String DENSITY_XHIGH = "x3";

    private Mod() {
    }

    public static Mod getInstance() {
        if (instance == null) {
            synchronized (Mod.class) {
                if (instance == null) {
                    instance = new Mod();
                }
            }
        }
        return instance;
    }

    public void initialize(Activity activity) throws Exception {
        throw new SsdkUnsupportedException(this.ERROR_MSG, SsdkUnsupportedException.LIBRARY_UPDATE_IS_REQUIRED);
    }

    public void applyViewMod(Activity activity) {
        return;
    }

    public void applyViewMod(View rootView, String canonicalName) {
        return;
    }

    public Frame getFrame(String activityId) { return null; }

    public Uri getAssetUri(String name) { return null; }

    public Drawable getDrawable(String size, String name) { return null; }

}
