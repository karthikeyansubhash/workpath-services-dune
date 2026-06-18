// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.accessory.hid;

import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Provides the accessory report types.
 *
 * @since API 3
 */
@DeviceApi
public enum HIDReportType {
    /**
     * A feature report.
     *
     * @since API 3
     */
    FEATURE,

    /**
     * An input report.
     *
     * @since API 3
     */
    INPUT,

    /**
     * An output report.
     *
     * @since API 3
     */
    OUTPUT
}
