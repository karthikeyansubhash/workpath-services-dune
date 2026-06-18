// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api;

import com.hp.jetadvantage.link.common.annotation.CommonApi;

/**
 * <p>Thrown when an application attempts to build or submit a job with unsupported attributes.</p>
 * These include:
 * <ul>
 *     <li>Throwing error when application sends a job without retrieving compatible attribute</li>
 *     <li>Throwing error when Printer doesn't have capabilities</li>
 * </ul>
 *
 * @since API 1
 */
@CommonApi
public class CapabilitiesExceededException extends Exception {
    private static final long serialVersionUID = 8110758563380362314L;

    /**
     * Constructs a CapabilitiesExceededException with the specified detail message.
     *
     * @param detailMessage the detail message for this exception.
     * @hide The client should not need to construct this exception
     */
    public CapabilitiesExceededException(final String detailMessage) {
        super(detailMessage);
    }

}
