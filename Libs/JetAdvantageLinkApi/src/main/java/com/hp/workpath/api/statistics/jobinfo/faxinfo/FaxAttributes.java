// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.statistics.jobinfo.faxinfo;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Provides FaxAttributes information
 *
 * @since API 5
 */
@DeviceApi
public class FaxAttributes {

    /**
     * Enums of Result
     *
     * @since API 5
     */
    @Keep
    public enum Result {
        /**
         * Fax was completed successfully.
         *
         * @since API 5
         */
        @SerializedName("Successful")
        SUCCESSFUL,

        /**
         * Fax was partially completed.
         *
         * @since API 5
         */
        @SerializedName("Partial")
        PARTIAL,

        /**
         * Fax was Busy.
         *
         * @since API 5
         */
        @SerializedName("Busy")
        BUSY,

        /**
         * Fax failed prior to completion.
         *
         * @since API 5
         */
        @SerializedName("Failed")
        FAILED,

        /**
         * Fax was canceled.
         *
         * @since API 5
         */
        @SerializedName("Canceled")
        CANCELED
    }

    /**
     * Enums of IpTransport
     *
     * @since API 5
     */
    @Keep
    public enum IpTransport {
        /**
         * TCP
         *
         * @since API 5
         */
        @SerializedName("TCP")
        TCP,

        /**
         * UDP
         *
         * @since API 5
         */
        @SerializedName("UDP")
        UDP,

        /**
         * TLS
         *
         * @since API 5
         */
        @SerializedName("TLS")
        TLS
    }

    /**
     * Enums of T38Transport
     *
     * @since API 5
     */
    @Keep
    public enum T38Transport {
        /**
         * t38tTCP
         *
         * @since API 5
         */
        @SerializedName("t38tTCP")
        t38tTCP,

        /**
         * t38tUDP
         *
         * @since API 5
         */
        @SerializedName("t38tUDP ")
        t38tUDP
    }

    /**
     * Returns Call for FaxAttributes
     *
     * @since API 5
     */
    public class Call {
        private String billingCode;
        private int duration;
        private String faxNumber;
        private FaxAttributes.Result faxResult;

        /**
         * Returns billingCode for Call
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
         * Returns billingCode for Call
         *
         * @return duration
         * <p>
         * <ul>
         * <li>Ensures that the return numeric value is non-negative.</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public int getDuration() {
            return duration;
        }

        /**
         * Set the duration
         * @param duration the duration to set
         * @since API 5
         */
        public void setDuration(int duration) {
            this.duration = duration;
        }

        /**
         * Returns faxNumber for Call
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

        /**
         * Returns faxResult for call
         *
         * @return faxResult
         * <p>
         * <ul>
         * <li>Return can be null if the faxResult is null</li>
         * <li>Return can be null if the faxResult is empty</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public FaxAttributes.Result getFaxResult() {
            return faxResult;
        }

        /**
         * Set the fax result
         * @param faxResult the faxResult to set
         * @since API 5
         */
        public void setFaxResult(FaxAttributes.Result faxResult) {
            this.faxResult = faxResult;
        }
    }

    /**
     * Returns DigitalFaxCall for Faxinfo
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
     * Returns FaxConfiguration for Faxinfo
     *
     * @since API 5
     */
    public class FaxConfiguration {
        private IpServer sipServer;
        private IpTransport sipTransport;
        private T38Transport faxNumber;

        /**
         * Returns sipServer for FaxConfiguration
         *
         * @return sipServer
         * <p>
         * <ul>
         * <li>Return can be null if the sipServer is null</li>
         * <li>Return can be null if the sipServer is empty</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public IpServer getSipServer() {
            return sipServer;
        }

        /**
         * Set the SIP server
         * @param sipServer the sipServer to set
         * @since API 5
         */
        public void setSipServer(IpServer sipServer) {
            this.sipServer = sipServer;
        }

        /**
         * Returns sipTransport for FaxConfiguration
         *
         * @return sipTransport
         * <p>
         * <ul>
         * <li>Return can be null if the sipTransport is null</li>
         * <li>Return can be null if the sipTransport is empty</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public IpTransport getSipTransport() {
            return sipTransport;
        }

        /**
         * Set the SIP transport
         * @param sipTransport the sipTransport to set
         * @since API 5
         */
        public void setSipTransport(IpTransport sipTransport) {
            this.sipTransport = sipTransport;
        }

        /**
         * Returns faxNumber for FaxConfiguration
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
        public T38Transport getFaxNumber() {
            return faxNumber;
        }

        /**
         * Set the fax number
         * @param faxNumber the faxNumber to set
         * @since API 5
         */
        public void setFaxNumber(T38Transport faxNumber) {
            this.faxNumber = faxNumber;
        }
    }

    /**
     * Returns IpServer for Faxinfo
     *
     * @since API 5
     */
    public class IpServer {
        private int portNumber;
        private int proxyPortNumber;
        private String proxyServer;
        private String server;

        /**
         * Returns portNumber for IpServer
         *
         * @return portNumber
         * <p>
         * <ul>
         * <li>Ensures that the return numeric value is non-negative.</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public int getPortNumber() {
            return portNumber;
        }

        /**
         * Set the port number
         * @param portNumber the portNumber to set
         * @since API 5
         */
        public void setPortNumber(int portNumber) {
            this.portNumber = portNumber;
        }

        /**
         * Returns proxyPortNumber for IpServer
         * @return proxyPortNumber
         * <p>
         * <ul>
         * <li>Ensures that the return numeric value is non-negative.</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public int getProxyPortNumber() {
            return proxyPortNumber;
        }

        /**
         * Set the proxy port number
         * @param proxyPortNumber the proxyPortNumber to set
         * @since API 5
         */
        public void setProxyPortNumber(int proxyPortNumber) {
            this.proxyPortNumber = proxyPortNumber;
        }

        /**
         * Returns proxyServer for IpServer
         *
         * @return proxyServer
         * <p>
         * <ul>
         * <li>Return can be null if the proxyServer is null</li>
         * <li>Return can be null if the proxyServer is empty</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public String getProxyServer() {
            return proxyServer;
        }

        /**
         * Set the proxy server
         * @param proxyServer the proxyServer to set
         * @since API 5
         */
        public void setProxyServer(String proxyServer) {
            this.proxyServer = proxyServer;
        }

        /**
         * Returns server for IpServer
         *
         * @return server
         * <p>
         * <ul>
         * <li>Return can be null if the server is null</li>
         * <li>Return can be null if the server is empty</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public String getServer() {
            return server;
        }

        /**
         * Set the server
         * @param server the server to set
         * @since API 5
         */
        public void setServer(String server) {
            this.server = server;
        }
    }
}
