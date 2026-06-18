// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.accessory.hid;

import android.content.Context;

import com.hp.workpath.api.Result;
import com.hp.workpath.api.accessory.AccessoryInfo;

/**
 * Provides the event types for owned accessory.
 *
 * @since API 3
 */
public enum EventCode {
    /**
     * Indicated that the event was sent because the owned accessory was discovered and its context created.
     *
     * @since API 3
     */
    CONTEXT_CREATED,

    /**
     * Indicates that the event was resent due to a call to {@link AccessoryService#resendOwnedAccessoryContext(Context, AccessoryInfo, Result)}.
     *
     * @since API 3
     */
    CONTEXT_RESENT,

    /**
     * Indicates that the event was sent because the "owned" accessory's context was revoked
     * (the device was unplugged from or became unusable by the device).
     *
     * @since API 3
     */
    CONTEXT_REVOKED
}
