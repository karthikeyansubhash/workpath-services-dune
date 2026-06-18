// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.accessory;

import android.os.Parcelable;

/**
 * Accessory report event
 *
 * @since API 3
 */
public abstract class ReportEventInfo implements Parcelable {
    /**
     * Returns accessory info details specific to accessory class
     *
     * @since API 3
     */
    public <T extends ReportEventInfo> T getDetails() {
        return (T) this;
    }
}
