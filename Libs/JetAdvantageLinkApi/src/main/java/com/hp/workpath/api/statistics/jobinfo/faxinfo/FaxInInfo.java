// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.statistics.jobinfo.faxinfo;

import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Provides FaxIn information
 *
 * @since API 5
 */
@DeviceApi
public class FaxInInfo {

    private Id callerId;
    private String stationId;
    private int totalImagesReceived;

    /**
     * Returns Id
     *
     * @since API 5
     */
    public class Id {
        private String name;
        private String number;

        /**
         * Returns name of FaxInInfo
         *
         * @return name
         * <p>
         * <ul>
         * <li>Return can be null if the name is null</li>
         * <li>Return can be null if the name is empty</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Returns number of FaxInInfo
         *
         * @return number
         * <p>
         * <ul>
         * <li>Return can be null if the number is null</li>
         * <li>Return can be null if the number is empty</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }

    /**
     * Returns callerId for FaxInInfo
     *
     * @return callerId
     * <p>
     * <ul>
     * <li>Return can be null if the callerId is null</li>
     * <li>Return can be null if the callerId is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public Id getCallerId() {
        return callerId;
    }

    /**
     * Returns stationId for FaxInInfo
     *
     * @return stationId
     * <p>
     * <ul>
     * <li>Return can be null if the stationId is null</li>
     * <li>Return can be null if the stationId is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getStationId() {
        return stationId;
    }

    /**
     * Returns totalImagesReceived for FaxInInfo
     *
     * @return totalImagesReceived
     * <p>
     * <ul>
     * <li>Ensures that the return numeric value is non-negative.</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public int getTotalImagesReceived() {
        return totalImagesReceived;
    }

    /**
     * Set the caller ID
     * @param callerId the callerId to set
     * @since API 5
     */
    public void setCallerId(Id callerId) {
        this.callerId = callerId;
    }

    /**
     * Set the station ID
     * @param stationId the stationId to set
     * @since API 5
     */
    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    /**
     * Set the total images received
     * @param totalImagesReceived the totalImagesReceived to set
     * @since API 5
     */
    public void setTotalImagesReceived(int totalImagesReceived) {
        this.totalImagesReceived = totalImagesReceived;
    }
}
