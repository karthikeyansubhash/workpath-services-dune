// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.printer;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the set of attributes provided from the printer when requesting a print
 *
 * @hide The creation of capabilities is meant for the printer service to expose a read-only list of capabilities. Clients should not be writing
 * capabilities.
 */
public class PrintAttributesCapsCreator {

    final List<PrintAttributes.ColorMode> mColorModeList;
    final List<PrintAttributes.Duplex> mPlexList;
    final List<PrintAttributes.AutoFit> mAutoFitList;
    final List<PrintAttributes.StapleMode> mStapleModeList;
    final List<PrintAttributes.PaperSource> mPaperSourceList;
    final List<PrintAttributes.PaperSize> mPaperSizeList;
    final List<PrintAttributes.PaperType> mPaperTypeList;
    final List<PrintAttributes.DocumentFormat> mDocumentFormatList;
    final List<PrintAttributes.Source> mSourceList;
    final List<PrintAttributes.CollateMode> mCollateModeList;
    final int mMaxCopies;
    final List<PrintAttributes.Orientation> mOrientationList;
    final List<PrintAttributes.PrintQuality> mPrintQualityList;
    final List<PrintAttributes.OutputBin> mOutputBinList;
    final List<PrintAttributes.Finishings> mFinishingsList;

    private PrintAttributesCapsCreator(final Builder builder) {
        mColorModeList = builder.mColorModeList;
        mPlexList = builder.mPlexList;
        mMaxCopies = builder.mMaxCopies;
        mAutoFitList = builder.mAutoFitList;
        mStapleModeList = builder.mStapleModeList;
        mPaperSourceList = builder.mPaperSourceList;
        mPaperSizeList = builder.mPaperSizeList;
        mPaperTypeList = builder.mPaperTypeList;
        mDocumentFormatList = builder.mDocumentFormatList;
        mSourceList = builder.mSourceList;
        mCollateModeList = builder.mCollateModeList;
        mOrientationList = builder.mOrientationList;
        mPrintQualityList = builder.mPrintQualityList;
        mOutputBinList = builder.mOutputBinList;
        mFinishingsList = builder.mFinishingsList;

    }

    public static class Builder {
        final List<PrintAttributes.ColorMode> mColorModeList = new ArrayList<>();
        final List<PrintAttributes.Duplex> mPlexList = new ArrayList<>();
        final List<PrintAttributes.AutoFit> mAutoFitList = new ArrayList<>();
        final List<PrintAttributes.StapleMode> mStapleModeList = new ArrayList<>();
        final List<PrintAttributes.PaperSource> mPaperSourceList = new ArrayList<>();
        final List<PrintAttributes.PaperSize> mPaperSizeList = new ArrayList<>();
        final List<PrintAttributes.PaperType> mPaperTypeList = new ArrayList<>();
        final List<PrintAttributes.DocumentFormat> mDocumentFormatList = new ArrayList<>();
        final List<PrintAttributes.Source> mSourceList = new ArrayList<>();
        final List<PrintAttributes.CollateMode> mCollateModeList = new ArrayList<>();

        int mMaxCopies = 1;

        final List<PrintAttributes.Orientation> mOrientationList = new ArrayList<>();
        final List<PrintAttributes.PrintQuality> mPrintQualityList = new ArrayList<>();
        final List<PrintAttributes.OutputBin> mOutputBinList = new ArrayList<>();
        final List<PrintAttributes.Finishings> mFinishingsList = new ArrayList<>();

        public Builder() {
            // Initialize set type caps with DEFAULT
            mColorModeList.add(PrintAttributes.ColorMode.DEFAULT);
            mPlexList.add(PrintAttributes.Duplex.DEFAULT);
            mAutoFitList.add(PrintAttributes.AutoFit.DEFAULT);
            mStapleModeList.add(PrintAttributes.StapleMode.DEFAULT);
            mPaperSourceList.add(PrintAttributes.PaperSource.DEFAULT);
            mPaperSizeList.add(PrintAttributes.PaperSize.DEFAULT);
            mPaperTypeList.add(PrintAttributes.PaperType.DEFAULT);
            mDocumentFormatList.add(PrintAttributes.DocumentFormat.AUTO);
            mCollateModeList.add(PrintAttributes.CollateMode.DEFAULT);
            mOrientationList.add(PrintAttributes.Orientation.DEFAULT);
            mPrintQualityList.add(PrintAttributes.PrintQuality.DEFAULT);
            mOutputBinList.add(PrintAttributes.OutputBin.DEFAULT);
            mFinishingsList.add(PrintAttributes.Finishings.DEFAULT);
            // NOTE: Boolean type caps(isSupported) should not be initialized with DEFAULT
            // An empty list indicates the option is not supported
        }

        public Builder addColorMode(final PrintAttributes.ColorMode colorMode) {
            mColorModeList.add(colorMode);
            return this;
        }

        public Builder addPlex(final PrintAttributes.Duplex plex) {
            mPlexList.add(plex);
            return this;
        }

        public Builder setMaxCopies(final int copies) {
            mMaxCopies = copies;
            return this;
        }

        public Builder addAutoFit(final PrintAttributes.AutoFit af) {
            mAutoFitList.add(af);
            return this;
        }

        public Builder addStapleMode(final PrintAttributes.StapleMode sm) {
            mStapleModeList.add(sm);
            return this;
        }

        public Builder addPaperSource(final PrintAttributes.PaperSource ps) {
            mPaperSourceList.add(ps);
            return this;
        }

        public Builder addPaperSize(final PrintAttributes.PaperSize ps) {
            mPaperSizeList.add(ps);
            return this;
        }

        public Builder addPaperType(final PrintAttributes.PaperType paperType) {
            mPaperTypeList.add(paperType);
            return this;
        }

        public Builder addDocumentFormat(final PrintAttributes.DocumentFormat df) {
            mDocumentFormatList.add(df);
            return this;
        }

        public Builder addSource(final PrintAttributes.Source src) {
            mSourceList.add(src);
            return this;
        }

        public Builder addCollateMode(final PrintAttributes.CollateMode src) {
            mCollateModeList.add(src);
            return this;
        }

        public Builder addOrientation(final PrintAttributes.Orientation orientation) {
            mOrientationList.add(orientation);
            return this;
        }

        public Builder addPrintQuality(final PrintAttributes.PrintQuality printQuality) {
            mPrintQualityList.add(printQuality);
            return this;
        }

        public Builder addOutputBin(final PrintAttributes.OutputBin outputBin) {
            mOutputBinList.add(outputBin);
            return this;
        }

        public Builder addFinishings(final PrintAttributes.Finishings fi) {
            mFinishingsList.add(fi);
            return this;
        }
        /**
         * Combine all of the capabilities into a PrintAttributesCapsCreator object.
         *
         * @return a PrintAttributesCapsCreator object containing all of the attributes.
         */
        public PrintAttributesCapsCreator build() {
            return new PrintAttributesCapsCreator(this);
        }
    }
}
