// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.statistics.jobinfo.emailinfo;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;
import com.hp.workpath.api.statistics.jobinfo.StatisticsAttributes;
import com.hp.workpath.api.statistics.jobinfo.userinfo.ExtendedUserInfo;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Provides Email information
 *
 * @since API 5
 */
@DeviceApi
public class EmailInfo {

    private String[] bccAddresses;
    private String[] ccAddresses;
    private DigitalFaxCall[] digitalFaxCalls;
    private StatisticsAttributes.DigitalSendInfo digitalSendInfo;
    private String emailSubject;
    private String[] failedRecipientsList;
    private String fromAddress;
    private String hostName;
    private String ipAddress;
    private int port;
    private String[] toAddresses;

    /**
     * Returns DigitalFaxCall
     *
     * @since API 5
     */
    public class DigitalFaxCall {
        private String billingCode;
        private String faxNumber;

        /**
         * Returns billingCode for DigitalFaxCall
         *
         * @return billingCode
         * <p>
         * <ul>
         * <li>Return can be null if the billingCode is null</li>
         * <li>Return can be null if the billingCode is empty</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public String getBillingCode() {
            return billingCode;
        }

        /**
         * Set the billing code
         * @param billingCode the billingCode to set
         * @since API 5
         */
        public void setBillingCode(String billingCode) {
            this.billingCode = billingCode;
        }

        /**
         * Returns faxNumber for DigitalFaxCall
         *
         * @return faxNumber
         * <p>
         * <ul>
         * <li>Return can be null if the faxNumber is null</li>
         * <li>Return can be null if the faxNumber is empty</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public String getFaxNumber() {
            return faxNumber;
        }

        /**
         * Set the fax number
         * @param faxNumber the faxNumber to set
         * @since API 5
         */
        public void setFaxNumber(String faxNumber) {
            this.faxNumber = faxNumber;
        }
    }

    /**
     * Enums of Result
     *
     * @since API 5
     */
    @Keep
    public enum Result {
        /**
         * EmailInfo was completed successfully.
         *
         * @since API 5
         */
        @SerializedName("Successful")
        SUCCESSFUL,

        /**
         * EmailInfo was partially completed.
         *
         * @since API 5
         */
        @SerializedName("Partial")
        PARTIAL,

        /**
         * EmailInfo failed prior to completion.
         *
         * @since API 5
         */
        @SerializedName("Failed")
        FAILED,

        /**
         * EmailInfo was canceled.
         *
         * @since API 5
         */
        @SerializedName("Canceled")
        CANCELED
    }

    /**
     * Returns bccAddresses for Email
     *
     * @return bccAddresses
     * <p>
     * <ul>
     * <li>if bcc address field is not added to String array list, the list should be empty.</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String[] getBccAddresses() {
        return bccAddresses;
    }

    /**
     * Returns ccAddresses for Email
     *
     * @return ccAddresses
     * <p>
     * <ul>
     * <li>if cc address field is not added to String array list, the list should be empty.</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String[] getCcAddresses() {
        return ccAddresses;
    }

    /**
     * Returns digitalFaxCalls for Email
     *
     * @return digitalFaxCalls
     * <p>
     * <ul>
     * <li>if DigitalFaxCall field is not added to DigitalFaxCall array list, the list should be empty.</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public DigitalFaxCall[] getDigitalFaxCalls() {
        return digitalFaxCalls;
    }

    /**
     * Returns digitalSendInfo for Email
     *
     * @return digitalSendInfo
     *
     * @since API 5
     */
    public StatisticsAttributes.DigitalSendInfo getDigitalSendInfo() {
        return digitalSendInfo;
    }

    /**
     * Returns emailSubject for Email
     *
     * @return The email subject
     * <p>
     * <ul>
     * <li>Return can be null if the emailSubject is null</li>
     * <li>Return can be null if the emailSubject is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getEmailSubject() {
        return emailSubject;
    }

    /**
     * The list of email addresses that the smtp server failed to deliver to. This list will exists only if digitalSendInfo.result is 'Partial'
     *
     * @return failedRecipientsList
     * <p>
     * <ul>
     * <li>if failedRecipient field is not added to String array list, the list should be empty.</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String[] getFailedRecipientsList() {
        return failedRecipientsList;
    }

    /**
     * Returns fromAddress for Email
     *
     * @return fromAddress
     * <p>
     * <ul>
     * <li>Return can be null if the fromAddress is null</li>
     * <li>Return can be null if the fromAddress is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getFromAddress() {
        return fromAddress;
    }

    /**
     * The email server's hostname for Email
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
     * An IP address in the form of a string. May contain an IPv4 or an IPv6 address, but is never a host name.
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
     * Returns email server port for Email
     *
     * @return email server port
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
     * Returns The list of recipient (To) addresses for Email
     *
     * @return The list of recipient (To) addresses
     * <p>
     * <ul>
     * <li>if toAddress field is not added to String array list, the list should be empty.</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String[] getToAddresses() {
        return toAddresses;
    }

    /**
     * Set the BCC addresses
     * @param bccAddresses the bccAddresses to set
     * @since API 5
     */
    public void setBccAddresses(String[] bccAddresses) {
        this.bccAddresses = bccAddresses;
    }

    /**
     * Set the CC addresses
     * @param ccAddresses the ccAddresses to set
     * @since API 5
     */
    public void setCcAddresses(String[] ccAddresses) {
        this.ccAddresses = ccAddresses;
    }

    /**
     * Set the digital fax calls
     * @param digitalFaxCalls the digitalFaxCalls to set
     * @since API 5
     */
    public void setDigitalFaxCalls(DigitalFaxCall[] digitalFaxCalls) {
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
     * Set the email subject
     * @param emailSubject the emailSubject to set
     * @since API 5
     */
    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    /**
     * Set the failed recipients list
     * @param failedRecipientsList the failedRecipientsList to set
     * @since API 5
     */
    public void setFailedRecipientsList(String[] failedRecipientsList) {
        this.failedRecipientsList = failedRecipientsList;
    }

    /**
     * Set the from address
     * @param fromAddress the fromAddress to set
     * @since API 5
     */
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
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
     * Set the to addresses
     * @param toAddresses the toAddresses to set
     * @since API 5
     */
    public void setToAddresses(String[] toAddresses) {
        this.toAddresses = toAddresses;
    }
}
