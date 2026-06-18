// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.statistics.jobinfo.ftpinfo;

import com.hp.workpath.api.statistics.jobinfo.StatisticsAttributes;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Provides Ftp information
 *
 * @since API 5
 */
@DeviceApi
public class FtpInfo {
    private StatisticsAttributes.DigitalSendInfo digitalSendInfo;
    private String directoryPath;
    private String hostName;
    private String ipAddress;
    private int port;
    private String userName;

    /**
     * Returns digitalSendInfo for FTP infomation
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
     * Returns directoryPath for FTP infomation
     *
     * @return directoryPath
     * <p>
     * <ul>
     * <li>Return can be null if the directoryPath is null</li>
     * <li>Return can be null if the directoryPath is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getDirectoryPath() {
        return directoryPath;
    }

    /**
     * Returns hostName for FTP infomation
     *
     * @return hostName
     * <p>
     * <ul>
     * <li>Return can be null if the hostName is null</li>
     * <li>Return can be null if the hostName is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Returns ipAddress for FTP infomation
     *
     * @return ipAddress
     * <p>
     * <ul>
     * <li>Return can be null if the ipAddress is null</li>
     * <li>Return can be null if the ipAddress is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Returns port for FTP infomation
     *
     * @return port
     * <p>
     * <ul>
     * <li>Ensures that the return numeric value is non-negative.</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public int getPort() {
        return port;
    }

    /**
     * Returns userName for FTP infomation
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
     * Set the directory path
     * @param directoryPath the directoryPath to set
     * @since API 5
     */
    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    /**
     * Set the host name
     * @param hostName the hostName to set
     * @since API 5
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * Set the IP address
     * @param ipAddress the ipAddress to set
     * @since API 5
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Set the port
     * @param port the port to set
     * @since API 5
     */
    public void setPort(int port) {
        this.port = port;
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
