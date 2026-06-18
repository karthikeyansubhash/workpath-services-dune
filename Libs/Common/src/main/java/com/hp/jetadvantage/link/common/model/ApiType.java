// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.common.model;

/**
 * Possible API types.
 * During discovery the Printer Selector determines these values.
 */

public enum ApiType {
    /** It's basic standard protocols, like ESCL, SFI, SPL, SPS etc. */
    BASIC,
    /** OXPd API is supported */
    OXP
}
