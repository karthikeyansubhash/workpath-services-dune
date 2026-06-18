// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.discovery;

/**
 * Enumeration of Error Name.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum ErrorName {

    /** An error occurred when attempting the CORS/Ajax request. */
    AjaxError,
    /** A specific resource that can't be found. */
    NotFound,
    /** The request contained invalid input. */
    ServerError,
    /** Unknown error. */
    Unknown
}
