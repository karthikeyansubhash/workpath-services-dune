// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.statistics.jobinfo.folderinfo;

import com.hp.workpath.api.statistics.jobinfo.StatisticsAttributes;
import com.hp.workpath.api.statistics.jobinfo.faxinfo.FaxAttributes;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Provides Folder information
 *
 * @since API 5
 */
@DeviceApi
public class FolderInfo {
    private FaxAttributes.DigitalFaxCall[] digitalFaxCalls;
    private StatisticsAttributes.DigitalSendInfo digitalSendInfo;
    private String uncPath;
    private String userName;

    /**
     * Returns digitalFaxCalls for Folder information
     *
     * @return digitalFaxCalls
     * <p>
     * <ul>
     * <li>if {@link FaxAttributes.DigitalFaxCall} field is not added to DigitalFaxCall array list, the list should be empty.</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public FaxAttributes.DigitalFaxCall[] getDigitalFaxCalls() {
        return digitalFaxCalls;
    }

    /**
     * Returns digitalSendInfo for Folder information
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
     * Returns uncPath for Folder information
     *
     * @return uncPath
     * <p>
     * <ul>
     * <li>Return can be null if the uncPath is null</li>
     * <li>Return can be null if the uncPath is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getUncPath() {
        return uncPath;
    }

    /**
     * Returns userName for Folder information
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
     * Set the digital fax calls
     * @param digitalFaxCalls the digitalFaxCalls to set
     * @since API 5
     */
    public void setDigitalFaxCalls(FaxAttributes.DigitalFaxCall[] digitalFaxCalls) {
        this.digitalFaxCalls = digitalFaxCalls;
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
     * Set the UNC path
     * @param uncPath the uncPath to set
     * @since API 5
     */
    public void setUncPath(String uncPath) {
        this.uncPath = uncPath;
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
