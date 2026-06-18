// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.scanner;

import java.util.Collections;
import java.util.List;

import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributes.PdfCompressionMode;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributes.OcrLanguage;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributes.TiffCompressionMode;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributes.XpsCompressionMode;

/**
 * File options which are available from device.
 *
 * @since API 1
 */
public final class FileOptionsAttributesCaps {
    final FileOptionsAttributesCapsCreator mCapsCreator;

    /**
     * Constructor to build FileOptionsAttributesCapsCreator object
     *
     * @param creator object containing the file options capabilities
     * @hide The creator is hidden
     */
    public FileOptionsAttributesCaps(final FileOptionsAttributesCapsCreator creator) {
        mCapsCreator = creator;
    }

    /**
     * Returns supported option of PDF encryption
     *
     * @return true if PDF encryption supported
     * @since API 1
     */
    @SuppressWarnings("unused")
    public boolean isPdfEncryptionPasswordSupported() {
        return mCapsCreator.isPdfEncryptionPasswordSupported;
    }

    /**
     * Gets supported PDF compression modes
     *
     * @return the PDF compression modes supported.
     * @since API 1
     */
    @SuppressWarnings("unused")
    public List<PdfCompressionMode> getPdfCompressionModeList() {
        return Collections.unmodifiableList(mCapsCreator.mPdfCompressionModeList);
    }

    /**
     * Gets supported PDF OCR languages
     *
     * @return the PDF OCR languages supported.
     * @since API 1
     */
    @SuppressWarnings("unused")
    public List<OcrLanguage> getOcrLanguageList() {
        return Collections.unmodifiableList(mCapsCreator.mOcrLanguageList);
    }

    /**
     * Gets supported TIFF compression modes
     *
     * @return the TIFF compression modes supported.
     * @since API 1
     */
    @SuppressWarnings("unused")
    public List<TiffCompressionMode> getTiffCompressionModeList() {
        return Collections.unmodifiableList(mCapsCreator.mTiffCompressionModeList);
    }

    /**
     * Gets supported XPS compression modes
     *
     * @return the XPS compression modes supported.
     * @since API 1
     */
    @SuppressWarnings("unused")
    public List<XpsCompressionMode> getXpsCompressionModeList() {
        return Collections.unmodifiableList(mCapsCreator.mXpsCompressionModeList);
    }
}
