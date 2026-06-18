// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.accessory;


import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Provides the accessory registration types.
 *
 * @since API 3
 */
@DeviceApi
public enum RegistrationType {
    /**
     * Exclusive use by a single application.
     *
     * @since API 3
     */
    OWNED,

    /**
     * Allows use by any application.
     *
     * @since API 3
     */
    SHARED
}
