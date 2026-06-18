// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.statistics.jobinfo.scan;

import com.hp.workpath.api.statistics.jobinfo.StatisticsAttributes;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Provides Scanned sheet information
 *
 * @since API 5
 */
@DeviceApi
public class ScannedSheetInfo {
    private int otherPrintedSheets;
    private ScannedSheetSets[] scannedSheetSets;

    public class ScannedSheetSets {
        private int count;
        private StatisticsAttributes.PaperSource mediaInputId;
        private StatisticsAttributes.ScanSize mediaSizeId;
        private StatisticsAttributes.Plex plex;

        /**
         * The number of sheets matching this sheet set.
         *
         * @return count
         * <p>
         * <ul>
         * <li>Ensures that the return numeric value is non-negative.</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public int getCount() {
            return count;
        }

        /**
         * Set the count
         * @param count the count to set
         * @since API 5
         */
        public void setCount(int count) {
            this.count = count;
        }


        /**
         * The input location of all sheets in the set.
         *
         * @return mediaInputId
         * <p>
         * <ul>
         * <li>Return can be null if the {@link com.hp.workpath.api.statistics.jobinfo.StatisticsAttributes.PaperSource} is null</li>
         * <li>Return can be null if the {@link com.hp.workpath.api.statistics.jobinfo.StatisticsAttributes.PaperSource} is empty</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public StatisticsAttributes.PaperSource getMediaInputId() {
            return mediaInputId;
        }

        /**
         * Set the media input id
         * @param mediaInputId the mediaInputId to set
         * @since API 5
         */
        public void setMediaInputId(StatisticsAttributes.PaperSource mediaInputId) {
            this.mediaInputId = mediaInputId;
        }

        /**
         * The size of all sheets in the set.
         *
         * @return mediaSizeId
         * <p>
         * <ul>
         * <li>Return can be null if the {@link com.hp.workpath.api.statistics.jobinfo.StatisticsAttributes.ScanSize} is null</li>
         * <li>Return can be null if the {@link com.hp.workpath.api.statistics.jobinfo.StatisticsAttributes.ScanSize} is empty</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public StatisticsAttributes.ScanSize getMediaSizeId() {
            return mediaSizeId;
        }

        /**
         * Set the media size id
         * @param mediaSizeId the mediaSizeId to set
         * @since API 5
         */
        public void setMediaSizeId(StatisticsAttributes.ScanSize mediaSizeId) {
            this.mediaSizeId = mediaSizeId;
        }

        /**
         * Returns plex
         *
         * @return plex
         * <p>
         * <ul>
         * <li>Return can be null if the {@link com.hp.workpath.api.statistics.jobinfo.StatisticsAttributes.Plex} is null</li>
         * <li>Return can be null if the {@link com.hp.workpath.api.statistics.jobinfo.StatisticsAttributes.Plex} is empty</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public StatisticsAttributes.Plex getPlex() {
            return plex;
        }

        /**
         * Set the plex
         * @param plex the plex to set
         * @since API 5
         */
        public void setPlex(StatisticsAttributes.Plex plex) {
            this.plex = plex;
        }
    }

    /**
     * Returns printed sheets for others
     *
     * @return otherPrintedSheets
     * <p>
     * <ul>
     * <li>Ensures that the return numeric value is non-negative.</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public int getOtherPrintedSheets() {
        return otherPrintedSheets;
    }

    /**
     * Returns scanned sheets sets
     *
     * @return scannedSheetSets
     * <p>
     * <ul>
     * <li>if {@link ScannedSheetSets} field is not added to ScannedSheetSets array list, the list should be empty.</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public ScannedSheetSets[] getScannedSheetSets() {
        return scannedSheetSets;
    }

    /**
     * Set the other printed sheets
     * @param otherPrintedSheets the otherPrintedSheets to set
     * @since API 5
     */
    public void setOtherPrintedSheets(int otherPrintedSheets) {
        this.otherPrintedSheets = otherPrintedSheets;
    }

    /**
     * Set the scanned sheet sets
     * @param scannedSheetSets the scannedSheetSets to set
     * @since API 5
     */
    public void setScannedSheetSets(ScannedSheetSets[] scannedSheetSets) {
        this.scannedSheetSets = scannedSheetSets;
    }
}
