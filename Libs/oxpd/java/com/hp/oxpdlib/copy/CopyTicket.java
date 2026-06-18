// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.copy;

public class CopyTicket {

    public CopyOptions copyOptions;

    /**
     * Default no-arg constructor
     * 
     */
    public CopyTicket() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     * @param copyOptions
     *     the CopyOptions
     */
    public CopyTicket(final CopyOptions copyOptions) {
        this.copyOptions = copyOptions;
    }
}
