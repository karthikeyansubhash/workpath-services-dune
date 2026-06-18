// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.copy;

/** Enumeration of Error Name. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum ErrorName {
    /** The library has not yet been set up. */
    NotSetup,
    /** The version of the OXPd:Copy service required by this library is not available on this device. */
    ServiceNotFound,
    /** An error occurred when attempting the CORS/Ajax request. */
    AjaxError,
    /** The requested resource was not found. */
    NotFound,
    /** An unexpected condition prevented the device from fulfilling the request. */
    ServerError,
    /** The attempted operation is not allowed. This is usually caused by attempting a sequence of related operations out of order. */
    IllegalOperation,
    /** The attempted operation could not be performed because a required resource was temporarily unavailable. The same operation is expected to succeed if requested again after waiting for the resource to become free. */
    Concurrency,
    /** A required parameter or a required element of a supplied parameter was missing from the request. */
    RequiredElementMissing,
    /** One (or more) of the supplied parameters was invalid for the requested operation. */
    InvalidParameter,
    /** The supplied uiContextId parameter was invalid (an important special case of InvalidParameter). */
    InvalidUIContextId,
    /** An unsupported feature was attempted. */
    UnsupportedFeature,
    /** Unknown error. */
    Unknown
}
