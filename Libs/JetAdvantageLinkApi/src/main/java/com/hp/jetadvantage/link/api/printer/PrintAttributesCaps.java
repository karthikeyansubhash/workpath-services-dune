// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.printer;

import static com.hp.jetadvantage.link.api.printer.PrintAttributes.*;

import com.hp.jetadvantage.link.api.printer.PrintAttributes.AutoFit;
import com.hp.jetadvantage.link.api.printer.PrintAttributes.ColorMode;
import com.hp.jetadvantage.link.api.printer.PrintAttributes.DocumentFormat;
import com.hp.jetadvantage.link.api.printer.PrintAttributes.Duplex;
import com.hp.jetadvantage.link.api.printer.PrintAttributes.PaperSize;
import com.hp.jetadvantage.link.api.printer.PrintAttributes.PaperSource;
import com.hp.jetadvantage.link.api.printer.PrintAttributes.Source;
import com.hp.jetadvantage.link.api.printer.PrintAttributes.StapleMode;
import com.hp.jetadvantage.link.api.printer.PrintAttributes.Orientation;
import com.hp.jetadvantage.link.api.printer.PrintAttributes.OutputBin;
import com.hp.jetadvantage.link.api.printer.PrintAttributes.PrintQuality;

import java.util.Collections;
import java.util.List;

/**
 * Sets of attributes provided by the device when requesting a print.
 *
 * @since API 1
 */
public class PrintAttributesCaps {
    private final PrintAttributesCapsCreator mCapsCreator;

    /**
     * Constructor to craete object from the PrintAttributesCapsCreator object
     *
     * @param creator object containing the print capabilities
     * @hide The creator is hidden
     * @since API 1
     */
    public PrintAttributesCaps(final PrintAttributesCapsCreator creator) {
        mCapsCreator = creator;
    }

    /**
     * Gets the groups of color sets.
     *
     * @return the color modes supported.
     * @since API 1
     */
    public List<ColorMode> getColorModeList() {
        return Collections.unmodifiableList(mCapsCreator.mColorModeList);
    }

    /**
     * Gets duplex options supported by the device
     *
     * @return List of duplex options.
     * @since API 1
     */
    public List<Duplex> getDuplexList() {
        return Collections.unmodifiableList(mCapsCreator.mPlexList);
    }

    /**
     * Gets max number of copies a user can provide
     *
     * @return maximum number of copies.
     * @since API 1
     */
    public int getMaxCopies() {
        return mCapsCreator.mMaxCopies;
    }

    /**
     * Gets if AutoFit option is supported on the device.
     *
     * @return List of AutoFit options.
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 1
     */
    public List<AutoFit> getAutoFitList() {
        return Collections.unmodifiableList(mCapsCreator.mAutoFitList);
    }

    /**
     * Gets staple options supported on the device.
     *
     * @return List of Staple options.
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 1
     */
    public List<StapleMode> getStapleModeList() {
        return Collections.unmodifiableList(mCapsCreator.mStapleModeList);
    }

    /**
     * Gets paper sources supported on the device.
     *
     * @return List of paper sources.
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 1
     */
    public List<PaperSource> getPaperSourceList() {
        Collections.sort(mCapsCreator.mPaperSourceList);
        mCapsCreator.mPaperSourceList.remove(PaperSource.DEFAULT);
        mCapsCreator.mPaperSourceList.add(0, PaperSource.DEFAULT);

        return Collections.unmodifiableList(mCapsCreator.mPaperSourceList);
    }

    /**
     * Gets paper sizes supported on the device.
     *
     * @return List of paper sizes.
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 1
     */
    public List<PaperSize> getPaperSizeList() {
        Collections.sort(mCapsCreator.mPaperSizeList);
        mCapsCreator.mPaperSizeList.remove(PaperSize.DEFAULT);
        mCapsCreator.mPaperSizeList.add(0, PaperSize.DEFAULT);

        return Collections.unmodifiableList(mCapsCreator.mPaperSizeList);
    }

    /**
     * Gets paper types supported on the device.
     *
     * @return List of paper types.
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 1
     */
    public List<PaperType> getPaperTypeList() {
        return Collections.unmodifiableList(mCapsCreator.mPaperTypeList);
    }

    /**
     * Gets document formats supported on the device.
     *
     * @return List of document formats.
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 1
     */
    public List<DocumentFormat> getDocumentFormatList() {
        return Collections.unmodifiableList(mCapsCreator.mDocumentFormatList);
    }

    /**
     * Gets collate options supported on the device.
     *
     * @return List of Collate options.
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 1
     */
    public List<CollateMode> getCollateModeList() {
        return Collections.unmodifiableList(mCapsCreator.mCollateModeList);
    }

    /**
     * Gets orientation options supported on the device.
     *
     * @return List of Orientation options.
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 5
     */
    public List<Orientation> getOrientationList() {
        return Collections.unmodifiableList(mCapsCreator.mOrientationList);
    }

    /**
     * Gets print-quality options supported on the device.
     *
     * @return List of Print Quality options.
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 5
     */
    public List<PrintQuality> getPrintQualityList() {
        return Collections.unmodifiableList(mCapsCreator.mPrintQualityList);
    }

    /**
     * Gets output-bin options supported on the device.
     *
     * @return List of Output-bin options.
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 5
     */
    public List<OutputBin> getOutputBinList() {
        return Collections.unmodifiableList(mCapsCreator.mOutputBinList);
    }

    /**
     * Gets finishings options supported on the device.
     *
     * @return List of Finishings options.
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 5
     */
    public List<Finishings> getFinishingsList() {
        return Collections.unmodifiableList(mCapsCreator.mFinishingsList);
    }

    /**
     * Gets file sources supported on the device.
     *
     * @return List of supported sources.
     * @remarks An empty list indicates that the option is not supported by the printer.
     * @since API 1
     */
    public List<Source> getSourceList() {
        return Collections.unmodifiableList(mCapsCreator.mSourceList);
    }

    /**
     * @hide trivial
     */
    @Override
    public String toString() {
        return "Print Caps: " +
                ",Plex:" + mCapsCreator.mPlexList +
                ",CM:" + mCapsCreator.mColorModeList +
                ",Copies:" + mCapsCreator.mMaxCopies +
                ",AutoFit:" + mCapsCreator.mAutoFitList +
                ",Staple:" + mCapsCreator.mStapleModeList +
                ",Collate:" + mCapsCreator.mCollateModeList +
                ",PaperSources:" + mCapsCreator.mPaperSourceList +
                ",PaperSizes:" + mCapsCreator.mPaperSizeList +
                ",DocumentFormats:" + mCapsCreator.mDocumentFormatList +
                ",Orientation:" + mCapsCreator.mOrientationList +
                ",PrintQuality:" + mCapsCreator.mPrintQualityList +
                ",OutputBin:" + mCapsCreator.mOutputBinList +
                ",Finishings:" + mCapsCreator.mFinishingsList;
    }
}
