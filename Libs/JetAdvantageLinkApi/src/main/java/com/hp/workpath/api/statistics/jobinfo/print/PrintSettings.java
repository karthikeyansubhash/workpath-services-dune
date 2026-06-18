// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.statistics.jobinfo.print;

import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Provides Print settings information
 *
 * @since API 5
 */
@DeviceApi
public class PrintSettings {
    private boolean econoMode;

    /**
     * Returns econo mode or not
     *
     * @return econoMode
     *
     * @since API 5
     */
    public boolean isEconoMode() {
        return econoMode;
    }

    public boolean setEconoMode(boolean econoMode) {
        this.econoMode = econoMode;
        return econoMode;
    }
}
