// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.statistics.jobinfo.faxinfo;

import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Provides FaxOut information
 *
 * @since API 5
 */
@DeviceApi
public class FaxOutInfo {

    private FaxAttributes.Call[] faxCalls;

    /**
     * Returns faxCalls for FaxOutInfo
     *
     * @return faxCalls
     * <p>
     * <ul>
     * <li>if {@link FaxAttributes.Call} field is not added to Call array list, the list should be empty.</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public FaxAttributes.Call[] getFaxCalls() {
        return faxCalls;
    }

    /**
     * Set the fax calls
     * @param faxCalls the faxCalls to set
     * @since API 5
     */
    public void setFaxCalls(FaxAttributes.Call[] faxCalls) {
        this.faxCalls = faxCalls;
    }
}
