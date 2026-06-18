// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.statistics.jobinfo.httpinfo;

import com.hp.workpath.api.statistics.jobinfo.StatisticsAttributes;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Provides Http information
 *
 * @since API 5
 */
@DeviceApi
public class HttpInfo {
    private StatisticsAttributes.DigitalSendInfo digitalSendInfo;
    private String uri;
    private String userName;

    /**
     * Returns digitalSendInfo for HTTP information
     *
     * @return digitalSendInfo
     * <p>
     * <ul>
     * <li>Return can be null if the digitalSendInfo is null</li>
     * <li>Return can be null if the digitalSendInfo is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public StatisticsAttributes.DigitalSendInfo getDigitalSendInfo() {
        return digitalSendInfo;
    }

    /**
     * Returns uri for HTTP information
     *
     * @return uri
     * <p>
     * <ul>
     * <li>Return can be null if the uri is null</li>
     * <li>Return can be null if the uri is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getUri() {
        return uri;
    }

    /**
     * Returns userName for HTTP information
     *
     * @return userName
     * <p>
     * <ul>
     * <li>Return can be null if the userName is null</li>
     * <li>Return can be null if the userName is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set the digital send info
     * @param digitalSendInfo the digitalSendInfo to set
     * @since API 5
     */
    public void setDigitalSendInfo(StatisticsAttributes.DigitalSendInfo digitalSendInfo) {
        this.digitalSendInfo = digitalSendInfo;
    }

    /**
     * Set the URI
     * @param uri the uri to set
     * @since API 5
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * Set the user name
     * @param userName the userName to set
     * @since API 5
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
