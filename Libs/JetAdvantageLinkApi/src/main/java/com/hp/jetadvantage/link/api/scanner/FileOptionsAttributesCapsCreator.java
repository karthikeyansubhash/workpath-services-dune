// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.scanner;

import java.util.ArrayList;
import java.util.List;

import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributes.PdfCompressionMode;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributes.OcrLanguage;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributes.TiffCompressionMode;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributes.XpsCompressionMode;

/**
 * Holds the set of file options attributes supported by the printer
 *
 * @hide Clients should not be writing capabilities.
 */
public class FileOptionsAttributesCapsCreator {
    boolean isPdfEncryptionPasswordSupported;
    final List<PdfCompressionMode> mPdfCompressionModeList;
    final List<OcrLanguage> mOcrLanguageList;
    final List<TiffCompressionMode> mTiffCompressionModeList;
    final List<XpsCompressionMode> mXpsCompressionModeList;

    private FileOptionsAttributesCapsCreator(final Builder builder) {
        isPdfEncryptionPasswordSupported = builder.isPdfEncryptionPasswordSupported;
        mPdfCompressionModeList = builder.mPdfCompressionModeList;
        mOcrLanguageList = builder.mOcrLanguageList;
        mTiffCompressionModeList = builder.mTiffCompressionModeList;
        mXpsCompressionModeList = builder.mXpsCompressionModeList;
    }

    /**
     * Builder for determining the attributes supported for requesting images to be sent back to the client.
     */
    @SuppressWarnings("unused")
    public static class Builder {
        final List<PdfCompressionMode> mPdfCompressionModeList = new ArrayList<>();
        final List<OcrLanguage> mOcrLanguageList = new ArrayList<>();
        final List<TiffCompressionMode> mTiffCompressionModeList = new ArrayList<>();
        final List<XpsCompressionMode> mXpsCompressionModeList = new ArrayList<>();
        boolean isPdfEncryptionPasswordSupported = false;

        /**
         * Constructs a new builder used for determining the attributes supported for scanned images back to the client.
         */
        @SuppressWarnings("unused")
        public Builder() {
            mPdfCompressionModeList.add(PdfCompressionMode.DEFAULT);
            mOcrLanguageList.add(OcrLanguage.DEFAULT);
            mTiffCompressionModeList.add(TiffCompressionMode.DEFAULT);
            mXpsCompressionModeList.add(XpsCompressionMode.DEFAULT);
        }

        /**
         * Adds a OCR language
         *
         * @param ocrLanguage language
         * @return this builder for method chaining.
         */
        @SuppressWarnings("unused")
        public Builder addOcrLanguage(final OcrLanguage ocrLanguage) {
            mOcrLanguageList.add(ocrLanguage);
            return this;
        }

        /**
         * Adds a PDF compression mode
         *
         * @param pdfCompressionMode mode
         * @return this builder for method chaining.
         */
        @SuppressWarnings("unused")
        public Builder addPdfCompressionMode(final PdfCompressionMode pdfCompressionMode) {
            mPdfCompressionModeList.add(pdfCompressionMode);
            return this;
        }

        /**
         * Adds a TIFF compression mode
         *
         * @param tiffCompressionMode mode
         * @return this builder for method chaining.
         */
        @SuppressWarnings("unused")
        public Builder addTiffCompressionMode(final TiffCompressionMode tiffCompressionMode) {
            mTiffCompressionModeList.add(tiffCompressionMode);
            return this;
        }

        /**
         * Adds a XPS compression mode
         *
         * @param xpsCompressionMode mode
         * @return this builder for method chaining.
         */
        @SuppressWarnings("unused")
        public Builder addXpsCompressionMode(final XpsCompressionMode xpsCompressionMode) {
            mXpsCompressionModeList.add(xpsCompressionMode);
            return this;
        }

        /**
         * Sets whether PDF encryption password supported
         *
         * @param pdfEncryptionPasswordSupported value
         * @return this builder for method chaining.
         */
        @SuppressWarnings("unused")
        public Builder setPdfEncryptionPasswordSupported(boolean pdfEncryptionPasswordSupported) {
            isPdfEncryptionPasswordSupported = pdfEncryptionPasswordSupported;
            return this;
        }

        /**
         * Combine all of the capabilities in this into a FileOptionsAttributesCapsCreator object.
         *
         * @return a FileOptionsAttributesCapsCreator object containing all of the attributes.
         */
        @SuppressWarnings("unused")
        public FileOptionsAttributesCapsCreator build() {
            return new FileOptionsAttributesCapsCreator(this);
        }
    }
}
