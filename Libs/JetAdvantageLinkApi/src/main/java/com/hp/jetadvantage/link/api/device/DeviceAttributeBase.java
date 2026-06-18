// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.device;

import com.hp.jetadvantage.link.common.annotation.CommonApi;

/**
 * <p>Interfaces of the deviceAttribute type.</p>
 * @since API 1
 */
@SuppressWarnings({"WeakerAccess"})
@CommonApi
public interface DeviceAttributeBase {
    /**
     * Class of returning result
     * @hide
     * @return class
     * @since API 1
     */
    Class getResultClass();

    /**
     * Attribute name
     * @hide
     * @return name
     */
    String name();
}
