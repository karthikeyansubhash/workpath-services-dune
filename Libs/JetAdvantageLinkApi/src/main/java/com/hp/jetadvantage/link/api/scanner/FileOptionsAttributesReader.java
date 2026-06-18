// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.scanner;

import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributes.PdfCompressionMode;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributes.OcrLanguage;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributes.TiffCompressionMode;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributes.XpsCompressionMode;

/**
 * Reads attributes from {@link FileOptionsAttributes}
 *
 * @since API 1
 */
@SuppressWarnings({"unused"})
public class FileOptionsAttributesReader {
    private final FileOptionsAttributes mAttrs;

    /**
     * Constructor
     *
     * @param fileOptionsAttrs The attributes of the file options
     * @since API 1
     */
    public FileOptionsAttributesReader(final FileOptionsAttributes fileOptionsAttrs) {
        mAttrs = fileOptionsAttrs;
    }

    /**
     * Gets PDF encryption password
     *
     * @return String password
     * @since API 1
     */
    public String getPdfEncryptionPassword() {
        return mAttrs.mPdfEncryptionPassword;
    }

    /**
     * Gets OCR language
     *
     * @return OcrLanguage language
     * @since API 1
     */
    public OcrLanguage getOcrLanguage() {
        return mAttrs.mOcrLanguage;
    }

    /**
     * Gets PDF compression mode
     *
     * @return PdfCompressionMode mode
     * @since API 1
     */
    public PdfCompressionMode getPdfCompressionMode() {
        return mAttrs.mPdfCompressionMode;
    }

    /**
     * Gets TIFF compression mode
     *
     * @return TiffCompressionMode mode
     * @since API 1
     */
    public TiffCompressionMode getTiffCompressionMode() {
        return mAttrs.mTiffCompressionMode;
    }

    /**
     * Gets XPS compression mode
     *
     * @return XpsCompressionMode mode
     * @since API 1
     */
    public XpsCompressionMode getXpsCompressionMode() {
        return mAttrs.mXpsCompressionMode;
    }
}
