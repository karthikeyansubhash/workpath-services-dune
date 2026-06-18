// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.printer;

import android.net.Uri;

import androidx.annotation.Keep;

import java.util.List;

/**
 * Reads attributes which are requesting of a print from the device
 *
 * @since API 1
 */
public class PrintAttributesReader {
    @Keep
    private final PrintAttributes mAttrs;

    /**
     * Constructor
     *
     * @param attrs The print attributes used to construct this.
     * @since API 1
     */
    @Keep
    public PrintAttributesReader(final PrintAttributes attrs) {
        mAttrs = attrs;
    }

    /**
     * Gets the version of the created attributes.
     *
     * @return The version of the created attributes.
     * @hide for internal use
     * @since API 1
     */
    public int getVersion() {
        return mAttrs.mVersion;
    }

    /**
     * Gets the color mode for the print.
     *
     * @return color mode.
     * @since API 1
     */
    @Keep
    public PrintAttributes.ColorMode getColorMode() {
        return mAttrs.mColorMode;
    }

    /**
     * Gets the duplex mode for the print.
     *
     * @return duplex mode
     * @since API 1
     */
    @Keep
    public PrintAttributes.Duplex getPlex() {
        return mAttrs.mPlex;
    }

    /**
     * Gets the auto fit mode (enabled or disabled) for the print.
     *
     * @return auto fit mode
     * @since API 1
     */
    @Keep
    public PrintAttributes.AutoFit getAutoFit() {
        return mAttrs.mAutoFit;
    }

    /**
     * Gets the staple mode for the print.
     *
     * @return staple mode
     * @since API 1
     */
    @Keep
    public PrintAttributes.StapleMode getStapleMode() { return mAttrs.mStapleMode; }

    /**
     * Gets the number of copies to print
     *
     * @return number of copies
     * @since API 1
     */
    @Keep
    public int getCopies() {
        return mAttrs.mCopies;
    }

    /**
     * Gets Media tray to be used for print
     *
     * @return paper source
     * @since API 1
     */
    @Keep
    public PrintAttributes.PaperSource getPaperSource() {
        return mAttrs.mPaperSource;
    }

    /**
     * Gets Media size to be used for print
     *
     * @return paper size
     * @since API 1
     */
    @Keep
    public PrintAttributes.PaperSize getPaperSize() {
        return mAttrs.mPaperSize;
    }

    /**
     * Gets Media type to be used for print
     *
     * @return paper type
     * @since API 1
     */
    @Keep
    public PrintAttributes.PaperType getPaperType() {
        return mAttrs.mPaperType;
    }

    /**
     * Gets Document format of the file to print
     *
     * @return file format
     * @since API 1
     */
    @Keep
    public PrintAttributes.DocumentFormat getDocumentFormat() {
        return mAttrs.mDocumentFormat;
    }

    /**
     * Gets the collate mode to be used for print
     *
     * @return collate mode
     * @since API 1
     */
    @Keep
    public PrintAttributes.CollateMode getCollateMode() {
        return mAttrs.mCollateMode;
    }

    /**
     * Gets the job name to be used for print
     *
     * @return String jobname
     * @since API 3
     */
    @Keep
    public String getJobName() {
        return mAttrs.mJobName;
    }

    /**
     * Gets the orientation to be used for print
     *
     * @return orientation
     * @since API 5
     */
    @Keep
    public PrintAttributes.Orientation getOrientation() {
        return mAttrs.mOrientation;
    }

    /**
     * Gets the print quality to be used for print
     *
     * @return printQuality
     * @since API 5
     */
    @Keep
    public PrintAttributes.PrintQuality getPrintQuality() {
        return mAttrs.mPrintQuality;
    }

    /**
     * Gets the Outputbin to be used for print
     *
     * @return outputBin
     * @since API 5
     */
    @Keep
    public PrintAttributes.OutputBin getOutputBin() {
        return mAttrs.mOutputBin;
    }
    /**
     * Gets the start page of page ranges to print
     *
     * @return start page of page ranges
     * @since API 5
     */
    @Keep
    public int getStartPageRanges() {
        return mAttrs.mStartPageRanges;
    }
    /**
     * Gets the end page of page ranges to print
     *
     * @return end page of page ranges
     * @since API 5
     */
    @Keep
    public int getEndPageRanges() {
        return mAttrs.mEndPageRanges;
    }
    /**
     * Gets the finishings for the print.
     *
     * @return finishings
     * @since API 5
     */
    @Keep
    public List<PrintAttributes.Finishings> getFinishingsList() { return mAttrs.mFinishings; }
    /**
     * Gets Location of the file to print
     *
     * @return uri of file
     * @hide for internal use
     * @since API 1
     */
    public Uri getUri() {
        return mAttrs.mFileUri;
    }

    /**
     * Gets source of file to print
     *
     * @return paper source
     * @hide for internal use
     * @since API 1
     */
    public PrintAttributes.Source getSource() {
        return mAttrs.mSource;
    }

    /**
     * Gets Network credentials for authentication when accessing Uri
     *
     * @return uri authentication credentials
     * @hide for internal use
     * @since API 1
     */
    public NetworkCredentialsAttributes getNetworkCredentials() {
        return mAttrs.mNetworkCredentialsAttributes;
    }
}
