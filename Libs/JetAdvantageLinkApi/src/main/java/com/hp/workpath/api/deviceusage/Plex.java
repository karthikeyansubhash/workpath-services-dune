// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.deviceusage;

import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Class for Providing methods to get Plex information for a job.
 *
 * @since API 5
 */
@DeviceApi
public class Plex {
    private int mVersion;
    private String plex;
    private int sheets;

    public Plex() {
        mVersion = Sdk.VERSION.LEVEL;
    }

    /**
     * Retrieves the duplex mode of the job.
     *
     * @return <p>plex mode of the job
     * <ul>
     * <li>Return value will be either Simplex or Duplex</li>
     * <li>Return value will not be null</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getPlex() {
        return plex;
    }

    /**
     * Retrieves total number of sheets (one or two sides)
     *
     * @return <p>Number of sheets
     * <ul>
     * <li>Return value will be a positive integer depending on the number of sheets</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public int getSheets() {
        return sheets;
    }

    public void setPlex(String plex) {
        this.plex = plex;
    }

    public void setSheets(int sheets) {
        this.sheets = sheets;
    }
}
