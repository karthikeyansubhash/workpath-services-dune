// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.statistics.jobinfo;

import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Provides file information
 *
 * @since API 5
 */
@DeviceApi
public class FileInfo {
    private int dataSize;
    private String fileName;

    /**
     * Returns dataSize
     *
     * @return dataSize
     * <p>
     * <ul>
     * <li>Ensures that the return numeric value is non-negative.</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public int getDataSize() {
        return dataSize;
    }

    /**
     * Returns file name
     *
     * @return file name
     * <p>
     * <ul>
     * <li>Return can be null if the fileName is null</li>
     * <li>Return can be null if the fileName is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Set the data size
     * @param dataSize the dataSize to set
     * @since API 5
     */
    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    /**
     * Set the file name
     * @param fileName the fileName to set
     * @since API 5
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
