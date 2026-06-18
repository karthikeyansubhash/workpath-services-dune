// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.deviceusage.scanner;

import androidx.annotation.Keep;

import com.hp.workpath.api.deviceusage.Plex;
import com.hp.workpath.api.deviceusage.SubUnitInfo;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Provides methods to retrieve scan job information
 *
 * @since API 5
 */
@DeviceApi
public class ScannerInfo {
    private int mVersion;

    /**
     * Job information by category
     *
     * @since API 5
     */
    @DeviceApi
    public class ByJobCategory {
        @Keep
        private String jobCategory;
        @Keep
        private int images;

        /**
         * Retrieves Category string of job
         *
         * @return <p>Job Category
         * <ul>
         * <li>Return value will be the category of the jobs e.g: Copy, Fax, Send etc</li>
         * <li>Return value will not be null</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public String getJobCategory() {
            return jobCategory;
        }

        /**
         * Retrieves Category Type of job
         *
         * @return <p>Job category type
         * <ul>
         * <li>Return value will be the category type of the jobs e.g: COPY, FAX, OTHER etc</li>
         * <li>Return value will be of the type {@link com.hp.workpath.api.deviceusage.SubUnitInfo.JobCategory}</li>
         * </ul>
         * </p>
         *
         * @since API 5
         */
        public SubUnitInfo.JobCategory getJobCategoryType() {
            for (SubUnitInfo.JobCategory enumValue : SubUnitInfo.JobCategory.values()) {
                if (enumValue.getValue().equals(jobCategory)) {
                    return enumValue;
                }
            }
            return SubUnitInfo.JobCategory.OTHER;
        }

        /**
         * A count of images
         *
         * @return <p>Images count
         * <ul>
         *  <li>Return value will be greater than or equal to zero</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public int getImages() {
            return images;
        }
        public void setJobCategory(String jobCategory) {
            this.jobCategory = jobCategory;
        }
        public void setImages(int images) {
            this.images = images;
        }
    }

    /**
     * Retrieves Job information by mediaSize
     *
     * @since API 5
     */
    @DeviceApi
    public class ByMediaSize {
        @Keep
        private String mediaSize;
        @Keep
        private int images;

        /**
         * Retrieves MediaSize string of job
         *
         * @return <p>Mediasize
         * <ul>
         * <li>Returns the Media size string for the printer e.g: ANSI_A_8point5x11in, Legal_8point5x14in, Executive_7point25x10point5in etc.</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public String getMediaSize() {
            return mediaSize;
        }

        /**
         * Retrieves MediaSize Type of job
         *
         * @return <p>media size type of the job
         *  <ul>
         *  <li>Return value will be the media size of SubUnitInfo e.g: LETTER, LETTER_ROTATE, LEDGER etc.</li>
         *  <li>Return value will be of the type {@link com.hp.workpath.api.deviceusage.SubUnitInfo.MediaSize}.</li>
         *  </ul>
         *  </p>
         * @since API 5
         */
        public SubUnitInfo.MediaSize getMediaSizeType() {
            for (SubUnitInfo.MediaSize enumValue : SubUnitInfo.MediaSize.values()) {
                if (enumValue.getValue().equals(mediaSize)) {
                    return enumValue;
                }
            }
            return SubUnitInfo.MediaSize.OTHER;
        }

        /**
         * Retrieves count of images
         *
         * @return <p>Images count
         * <ul>
         *  <li>Return value will be greater than or equal to zero</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public int getImages() {
            return images;
        }
        public void setMediaSize(String mediaSize) {
            this.mediaSize = mediaSize;
        }
        public void setImages(int images) {
            this.images = images;
        }
    }

    private int sheets;
    private int engineCycles;
    private Plex[] byScanPlex;
    private ByJobCategory[] byJobCategory;
    private ByMediaSize[] byMediaSize;

    public ScannerInfo() {
        mVersion = Sdk.VERSION.LEVEL;
    }

    /**
     * Retrieves total number of sheets (one or two sides)
     *
     * @return <p>Number of sheets
     * <ul>
     * <li>Return value will be greater than or equal to zero</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public int getSheets() {
        return sheets;
    }

    /**
     * Retrieves count of Engine Cycle
     *
     * @return <p>engineCycles
     * <ul>
     * <li>Return value will be greater than or equal to zero</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public int getEngineCycles() {
        return engineCycles;
    }

    /**
     * Retrieves the plex mode of the scan job.
     *
     * @return <p>byScanPlex array
     * <ul>
     * <li>Return value will be a array of {@link Plex} which contains information on plex and sheets</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public Plex[] getByScanPlex() {
        return byScanPlex;
    }

    /**
     * Retrieves the information by category.
     *
     * @return <p>byJobCategory array
     * <ul>
     * <li>Return value will be a array of {@link ByJobCategory} which contains information on job category and image</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public ByJobCategory[] getByJobCategory() {
        return byJobCategory;
    }

    /**
     * Retrieves the information by mediaSize.
     *
     * @return <p>byMediaSize array
     * <ul>
     * <li>Return value will be a array of {@link ByMediaSize} which contains information on media size and image</li>
     * <li>Return value can be null if the value for byMediaSize is null</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public ByMediaSize[] getByMediaSize() {
        return byMediaSize;
    }
    public void setSheets(int sheets) {
        this.sheets = sheets;
    }
    public void setEngineCycles(int engineCycles) {
        this.engineCycles = engineCycles;
    }
    public void setByScanPlex(Plex[] byScanPlex) {
        this.byScanPlex = byScanPlex;
    }
    public void setByJobCategory(ByJobCategory[] byJobCategory) {
        this.byJobCategory = byJobCategory;
    }
    public void setByMediaSize(ByMediaSize[] byMediaSize) {
        this.byMediaSize = byMediaSize;
    }
}
