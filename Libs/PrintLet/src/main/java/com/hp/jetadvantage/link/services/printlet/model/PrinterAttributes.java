// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.hp.jetadvantage.link.services.printlet.model.ipp.IppAttribute;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppBooleanAttribute;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppCollection;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppCollectionAttribute;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppConstants;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppIntegerAttribute;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppRangeAttribute;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppRawAttribute;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppResponse;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppStringAttribute;
import com.hp.jetadvantage.link.services.printlet.model.ipp.util.IppErrorGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Printer Attributes holder
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class PrinterAttributes implements Parcelable {
    /**
     * Make and Model of the device.
     */
    public final String makeAndModel;
    /**
     * Indicates the current state of the device.
     */
    public final PrinterState printerState;
    /**
     * List of reasons for the current printerState (may be null).
     */
    public final List<String> printerStateReasons;
    /**
     * Indicates whether this device is capable of printing in color.
     */
    public final boolean isColorSupported;
    /**
     * Number of copies supported by this device.
     */
    public final int supportedCopies;
    /**
     * List of printable file types supported by this device.
     */
    public final List<FileType> supportedFileTypes;
    /**
     * List of color modes supported by this device.
     */
    public final List<ColorMode> supportedColorModes;
    /**
     * List of plex modes supported by this device.
     */
    public final List<PlexMode> supportedPlexModes;
    /**
     * List of scaling modes supported by this device.
     */
    public final List<ScalingMode> supportedScalingModes;
    /**
     * List of finishing staple modes supported by this device.
     */
    public final List<StapleMode> supportedStapleModes;
    /**
     * List of media sources supported by this device.
     */
    public final List<MediaSource> supportedMediaSources;
    /**
     * List of media sizes supported by this device.
     */
    public final List<MediaSize> supportedMediaSizes;
    /**
     * List of media types supported by this device.
     */
    public final List<MediaType> supportedMediaTypes;
    /**
     * List of collate supported by this device.
     */
    public final List<CollateMode> supportedCollateModes;
    /**
     * List of media sources supported by this device.
     */
    public final List<String> availableMediaSourcesData;
    /**
     * Media size for media sources supported by this device.
     */
    public final Map<MediaSource, MediaSize> availableMediaSizes;
    /**
     * Media type for media sources supported by this device.
     */
    public final Map<MediaSource, MediaType> availableMediaTypes;
    /**
     * List of orientation supported by this device.
     */
    public final List<Orientation> supportedOrientations;
    /**
     * List of print quality supported by this device.
     */
    public final List<PrintQuality> supportedPrintQualities;
    /**
     * List of output-bin supported by this device.
     */
    public final List<OutputBin> supportedOutputBins;
    /**
     * Indicates whether or not the printer is capable of supporting the printing of page ranges.
     */
    public final boolean isPageRangesSupported;
    /**
     * List of finishings supported by this device.
     */
    public final List<Finishings> supportedFinishingsOptions;
    /**
     * Default copies.
     */
    public final int defaultCopies;

    /**
     * Default color mode.
     */
    public final ColorMode defaultColorMode;
    /**
     * Default file type.
     */
    public final FileType defaultFileType;
    /**
     * Default media size.
     */
    public final MediaSize defaultMediaSize;
    /**
     * Default media type.
     */
    public final MediaType defaultMediaType;
    /**
     * Default media source.
     */
    public final MediaSource defaultMediaSource;
    /**
     * Default plex mode.
     */
    public final PlexMode defaultPlexMode;
    /**
     * Default scaling mode
     */
    public final ScalingMode defaultScalingMode;
    /**
     * Default staple mode
     */
    public final StapleMode defaultStapleMode;
    /**
     * Default collate mode
     */
    public final CollateMode defaultCollateMode;
    /**
     * Default orientation mode
     */
    public final Orientation defaultOrientation;
    /**
     * Default print-quality mode
     */
    public final PrintQuality defaultPrintQuality;
    /**
     * Default output-bin
     */
    public final OutputBin defaultOutputBin;
    /**
     * Default finishings mode
     */
    public final List<Finishings> defaultFinishings;

    /**
     * Constructor used by the library to construct PrinterAttributes objects.
     * @param response
     *              Parsed response received from the device.
     * @throws Error
     *              If an error occurs.
     */
    public PrinterAttributes(IppResponse response) throws Error {

        IppAttribute attribute;

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_TEXT, IppConstants.IPP_ATTRIBUTE_NAME__PRINTER_MAKE_AND_MODEL);
        if (attribute instanceof IppStringAttribute) {
            this.makeAndModel = ((IppStringAttribute) attribute).get(0);
        } else {
            this.makeAndModel = null;
        }

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_ENUM, IppConstants.IPP_ATTRIBUTE_NAME__PRINTER_STATE);
        if (attribute instanceof IppIntegerAttribute) {
            this.printerState = PrinterState.fromAttributeValue(((IppIntegerAttribute)attribute).get(0));
        } else {
            this.printerState = null;
        }

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__PRINTER_STATE_REASONS);
        if (attribute instanceof IppStringAttribute) {
            this.printerStateReasons = ((IppStringAttribute) attribute).getValues();
        } else {
            this.printerStateReasons = null;
        }

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_BOOLEAN, IppConstants.IPP_ATTRIBUTE_NAME__COLOR_SUPPORTED);
        if (!(attribute instanceof IppBooleanAttribute)) IppErrorGenerator.attributeNotFoundError(IppConstants.IPP_ATTRIBUTE_NAME__COLOR_SUPPORTED);
        this.isColorSupported = ((IppBooleanAttribute)attribute).get(0);

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_RANGE, IppConstants.IPP_ATTRIBUTE_NAME__COPIES_SUPPORTED);
        if (!(attribute instanceof IppRangeAttribute)) IppErrorGenerator.attributeNotFoundError(IppConstants.IPP_ATTRIBUTE_NAME__COPIES_SUPPORTED);
        this.supportedCopies = ((IppRangeAttribute)attribute).get(0).mUpperBound;

        // process supported file types
        List<FileType> supportedFileTypes = new ArrayList<FileType>();
        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_MIMETYPE, IppConstants.IPP_ATTRIBUTE_NAME__DOCUMENT_FORMAT_SUPPORTED);
        if (!(attribute instanceof IppStringAttribute)) IppErrorGenerator.attributeNotFoundError(IppConstants.IPP_ATTRIBUTE_NAME__DOCUMENT_FORMAT_SUPPORTED);
        List<String> supportedFileTypesStrings = ((IppStringAttribute)attribute).getValues();
        if (supportedFileTypesStrings == null) supportedFileTypesStrings = Collections.emptyList();
        for(String value : supportedFileTypesStrings) {
            FileType fileType = FileType.fromAttributeValue(value);
            if (fileType != null) supportedFileTypes.add(fileType);
        }
        this.supportedFileTypes = Collections.unmodifiableList(supportedFileTypes);

        // fill in supported color mode list
        List<ColorMode> supportedColorModes = new ArrayList<ColorMode>();
        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__PRINT_COLOR_MODE_SUPPORTED);
        if (!(attribute instanceof IppStringAttribute)) IppErrorGenerator.attributeNotFoundError(IppConstants.IPP_ATTRIBUTE_NAME__PRINT_COLOR_MODE_SUPPORTED);
        List<String> supportedColorModesStrings = ((IppStringAttribute)attribute).getValues();
        if (supportedColorModesStrings == null) supportedColorModesStrings = Collections.emptyList();
        for(String value : supportedColorModesStrings) {
            ColorMode colorMode = ColorMode.fromAttributeValue(value);
            if (colorMode != null) supportedColorModes.add(colorMode);
        }
        this.supportedColorModes = Collections.unmodifiableList(supportedColorModes);

        // process the supported plex modes
        List<PlexMode> supportedPlexModes = new ArrayList<PlexMode>();
        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__SIDES_SUPPORTED);
        if (!(attribute instanceof IppStringAttribute)) IppErrorGenerator.attributeNotFoundError(IppConstants.IPP_ATTRIBUTE_NAME__SIDES_SUPPORTED);
        List<String> supportedPlexModesStrings = ((IppStringAttribute)attribute).getValues();
        if (supportedPlexModesStrings == null) supportedPlexModesStrings = Collections.emptyList();
        for(String value : supportedPlexModesStrings) {
            PlexMode plexMode = PlexMode.fromAttributeValue(value);
            if (plexMode != null) supportedPlexModes.add(plexMode);
        }
        this.supportedPlexModes = Collections.unmodifiableList(supportedPlexModes);

        List<CollateMode> supportedCollateModes = new ArrayList<>();
        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__COLLATE_SUPPORTED);
        if (attribute instanceof IppStringAttribute){
            List<String> supportedCollateModesStrings = ((IppStringAttribute) attribute).getValues();
            if (supportedCollateModesStrings == null)
                supportedCollateModesStrings = Collections.emptyList();
            for (String value : supportedCollateModesStrings) {
                CollateMode collateMode = CollateMode.fromAttributeValue(value);
                if (collateMode != null) supportedCollateModes.add(collateMode);
            }
        }
        this.supportedCollateModes = Collections.unmodifiableList(supportedCollateModes);

        // fill in supported scaling mode list
        List<ScalingMode> supportedScalingModes = new ArrayList<ScalingMode>();
        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__PRINT_SCALING_SUPPORTED);
        if ((attribute instanceof IppStringAttribute)) {
            List<String> supportedScalingModesStrings = ((IppStringAttribute)attribute).getValues();
            if (supportedScalingModesStrings == null) supportedScalingModesStrings = Collections.emptyList();
            for(String value : supportedScalingModesStrings) {
                ScalingMode scalingMode = ScalingMode.fromAttributeValue(value);
                if (scalingMode != null) supportedScalingModes.add(scalingMode);
            }
        }
        this.supportedScalingModes = Collections.unmodifiableList(supportedScalingModes);

        // process the supported staple modes
        List<StapleMode> supportedStapleModes = new ArrayList<StapleMode>();
        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__FINISHING_TEMPLATE_SUPPORTED);
        if ((attribute instanceof IppStringAttribute)) {
            List<String> supportedStapleModesStrings = ((IppStringAttribute)attribute).getValues();
            if (supportedStapleModesStrings == null) supportedStapleModesStrings = Collections.emptyList();
            for (String value : supportedStapleModesStrings) {
                StapleMode stapleMode = StapleMode.fromAttributeValue(value);
                if (stapleMode != null) supportedStapleModes.add(stapleMode);
            }
        }
        this.supportedStapleModes = Collections.unmodifiableList(supportedStapleModes);

        // process supported media sources
        List<MediaSource> supportedMediaSources = new ArrayList<MediaSource>();
        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_SOURCE_SUPPORTED);
        if (attribute instanceof IppStringAttribute) {
            List<String> mediaSourceSupported = ((IppStringAttribute) attribute).getValues();
            if (mediaSourceSupported == null) mediaSourceSupported = Collections.emptyList();
            for (String value : mediaSourceSupported) {
                MediaSource mediaSource = MediaSource.fromAttributeValue(value);
                if (mediaSource == null) {
                    try {
                        mediaSource = MediaSource.valueOf(value);
                    } catch (IllegalArgumentException e) {
                        // ignore
                    }
                }
                if (mediaSource != null) supportedMediaSources.add(mediaSource);
            }
        }
        this.supportedMediaSources = Collections.unmodifiableList(supportedMediaSources);

        // process the supported media sizes
        List<MediaSize> supportedMediaSizes = new ArrayList<MediaSize>();
        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_SUPPORTED);
        if ((attribute instanceof IppStringAttribute)) {
            List<String> supportedMediaStrings = ((IppStringAttribute)attribute).getValues();
            if (supportedMediaStrings == null) supportedMediaStrings = Collections.emptyList();
            for (String value : supportedMediaStrings) {
                MediaSize mediaSize = MediaSize.fromAttributeValue(value);
                if (mediaSize != null) supportedMediaSizes.add(mediaSize);
            }
        }
        this.supportedMediaSizes = Collections.unmodifiableList(supportedMediaSizes);

        // process the supported media types (IPP spec allows keyword or name)
        List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
        int[] mediaTypeTags = { IppConstants.IppTag.IPP_TAG_KEYWORD.getValue(), IppConstants.IppTag.IPP_TAG_NAME.getValue() };
        attribute = response.findAttributeByAnyTag(IppConstants.IppTag.IPP_TAG_PRINTER, mediaTypeTags, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_TYPE_SUPPORTED);
        if ((attribute instanceof IppStringAttribute)) {
            List<String> supportedMediaTypesStrings = ((IppStringAttribute)attribute).getValues();
            if (supportedMediaTypesStrings == null) supportedMediaTypesStrings = Collections.emptyList();
            for (String value : supportedMediaTypesStrings) {
                MediaType mediaType = MediaType.fromAttributeValue(value);
                if (mediaType != null) supportedMediaTypes.add(mediaType);
            }
        }
        this.supportedMediaTypes = Collections.unmodifiableList(supportedMediaTypes);

        // process media sizes for media source
        List<String> availableMediaSourcesData = new ArrayList<String>();
        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_STRING, IppConstants.IPP_ATTRIBUTE_NAME__PRINTER_INPUT_TRAY);
        if (attribute instanceof IppRawAttribute){
            List<byte[]> trayData = ((IppRawAttribute) attribute).getValues();
            if (trayData == null)
                trayData = Collections.emptyList();
            for (byte[] value : trayData) {
                availableMediaSourcesData.add(new String(value));
            }
        }
        this.availableMediaSourcesData = Collections.unmodifiableList(availableMediaSourcesData);

        // process media sizes for media source
        HashMap<MediaSource, MediaSize> availableMediaSizes = new HashMap<MediaSource, MediaSize>();
        HashMap<MediaSource, MediaType> availableMediaTypes = new HashMap<MediaSource, MediaType>();
        IppAttribute mediaReadyAttr = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_READY);
        IppAttribute mediaColReadyAttr = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_BEGIN_COLLECTION, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_COL_READY);
        if ((mediaReadyAttr instanceof IppStringAttribute) && (mediaColReadyAttr instanceof IppCollectionAttribute)) {
            List<String> mediaReady = ((IppStringAttribute)mediaReadyAttr).getValues();
            List<IppCollection> mediaColReady = ((IppCollectionAttribute)mediaColReadyAttr).getValues();
            int mediaReadyIndex, mediaColReadyIndex;
            for(mediaReadyIndex = mediaColReadyIndex = 0; (mediaReadyIndex < mediaReady.size()) && (mediaColReadyIndex < mediaColReady.size()); mediaReadyIndex++) {
                String mediaType = null;
                String mediaTray = null;
                String mediaSize = mediaReady.get(mediaReadyIndex);
                for(; (mediaColReadyIndex < mediaColReady.size()); mediaColReadyIndex++) {

                    IppAttribute mediaTrayAttr = mediaColReady.get(mediaColReadyIndex).findAttribute(IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_SOURCE);
                    if (mediaTrayAttr instanceof IppStringAttribute) {
                        String attrValue = ((IppStringAttribute)mediaTrayAttr).get(0);
                        if (!TextUtils.equals(mediaTray, attrValue)) {
                            // save tray value
                            mediaTray = attrValue;

                            // get media type (IPP spec allows keyword or name)
                            int[] mediaTypeColTags = { IppConstants.IppTag.IPP_TAG_KEYWORD.getValue(), IppConstants.IppTag.IPP_TAG_NAME.getValue() };
                            IppAttribute mediaTypeAttr = mediaColReady.get(mediaColReadyIndex).findAttributeByAnyTag(mediaTypeColTags, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_TYPE);
                            if (mediaTypeAttr instanceof IppStringAttribute) {
                                mediaType = ((IppStringAttribute)mediaTypeAttr).get(0);
                            }

                            // advance for next check
                            mediaColReadyIndex++;
                            break;
                        }
                    }
                }

                if (!TextUtils.isEmpty(mediaSize) && !TextUtils.isEmpty(mediaTray)) {
                    // convert size/tray/type
                    MediaSource mediaSourceKey = MediaSource.fromAttributeValue(mediaTray);
                    if (mediaSourceKey == null) {
                        try {
                            mediaSourceKey = MediaSource.valueOf(mediaTray);
                        } catch (IllegalArgumentException e) {
                            // ignore
                        }
                    }

                    MediaSize mediaSizeValue = MediaSize.fromAttributeValue(mediaSize);
                    if (mediaSourceKey != null && mediaSizeValue != null) {
                        availableMediaSizes.put(mediaSourceKey, mediaSizeValue);
                    }

                    if (!TextUtils.isEmpty(mediaSize)) {
                        MediaType mediaTypeValue = MediaType.fromAttributeValue(mediaType);
                        if (mediaSourceKey != null && mediaTypeValue != null) {
                            availableMediaTypes.put(mediaSourceKey, mediaTypeValue);
                        }
                    }
                }
            }
        }
        this.availableMediaSizes = Collections.unmodifiableMap(availableMediaSizes);
        this.availableMediaTypes = Collections.unmodifiableMap(availableMediaTypes);

        // Supported orientation list
        List<Orientation> supportedOrientations = new ArrayList<Orientation>();
        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_ENUM, IppConstants.IPP_ATTRIBUTE_NAME__ORIENTATION_REQUESTED_SUPPORTED);
        if ((attribute instanceof IppIntegerAttribute)) {
            List<Integer> supportedOrientationsType = ((IppIntegerAttribute)attribute).getValues();
            if(supportedOrientationsType == null) supportedOrientationsType = Collections.emptyList();
            for(int value : supportedOrientationsType) {
                Orientation orientation = Orientation.fromAttributeValue(value);
                if(orientation != null) supportedOrientations.add(orientation);
            }
        }
        this.supportedOrientations = Collections.unmodifiableList(supportedOrientations);

        // Supported print-quality list
        List<PrintQuality> supportedPrintQualities = new ArrayList<PrintQuality>();
        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_ENUM, IppConstants.IPP_ATTRIBUTE_NAME__PRINT_QUALITY_SUPPORTED);
        if ((attribute instanceof IppIntegerAttribute)) {
            List<Integer> supportedPrintQualityType = ((IppIntegerAttribute)attribute).getValues();
            if(supportedPrintQualityType == null) supportedPrintQualityType = Collections.emptyList();
            for(int value : supportedPrintQualityType) {
                PrintQuality printQuality = PrintQuality.fromAttributeValue(value);
                if(printQuality != null) supportedPrintQualities.add(printQuality);
            }
        }
        this.supportedPrintQualities = Collections.unmodifiableList(supportedPrintQualities);

        // process the supported output-bin
        List<OutputBin> supportedOutputBins = new ArrayList<OutputBin>();
        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__OUTPUT_BIN_SUPPORTED);
        if ((attribute instanceof IppStringAttribute)) {
            List<String> supportedOutputBinsStrings = ((IppStringAttribute)attribute).getValues();
            if (supportedOutputBinsStrings == null) supportedOutputBinsStrings = Collections.emptyList();
            for (String value : supportedOutputBinsStrings) {
                OutputBin outputBin = OutputBin.fromAttributeValue(value);
                if (outputBin != null) supportedOutputBins.add(outputBin);
            }
        }
        this.supportedOutputBins = Collections.unmodifiableList(supportedOutputBins);

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_BOOLEAN, IppConstants.IPP_ATTRIBUTE_NAME__PAGE_RANGES_SUPPORTED);
        if (!(attribute instanceof IppBooleanAttribute)) IppErrorGenerator.attributeNotFoundError(IppConstants.IPP_ATTRIBUTE_NAME__PAGE_RANGES_SUPPORTED);
        this.isPageRangesSupported = ((IppBooleanAttribute)attribute).get(0);

        // process the supported finishings
        List<Finishings> supportedFinishingsOptions = new ArrayList<Finishings>();
        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_ENUM, IppConstants.IPP_ATTRIBUTE_NAME__FINISHINGS_SUPPORTED);
        if ((attribute instanceof IppIntegerAttribute)) {
            List<Integer> supportedFinishingsType = ((IppIntegerAttribute)attribute).getValues();
            if (supportedFinishingsType == null) supportedFinishingsType = Collections.emptyList();
            for (int value : supportedFinishingsType) {
                Finishings finishings = Finishings.fromAttributeValue(value);
                if (finishings != null) supportedFinishingsOptions.add(finishings);
            }
        }
        this.supportedFinishingsOptions = Collections.unmodifiableList(supportedFinishingsOptions);

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_INTEGER, IppConstants.IPP_ATTRIBUTE_NAME__COPIES_DEFAULT);
        if (attribute instanceof IppIntegerAttribute) {
            this.defaultCopies = ((IppIntegerAttribute)attribute).get(0);
        } else {
            this.defaultCopies = 1;
        }

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__PRINT_COLOR_MODE_DEFAULT);
        if ((attribute instanceof IppStringAttribute)) {
            String defaultColorMode = ((IppStringAttribute) attribute).get(0);
            this.defaultColorMode = ColorMode.fromAttributeValue(defaultColorMode);
        } else {
            this.defaultColorMode = null;
        }

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_MIMETYPE, IppConstants.IPP_ATTRIBUTE_NAME__DOCUMENT_FORMAT_DEFAULT);
        if ((attribute instanceof IppStringAttribute)) {
            String defaultFile = ((IppStringAttribute) attribute).get(0);
            this.defaultFileType = FileType.fromAttributeValue(defaultFile);
        } else {
            this.defaultFileType = null;
        }

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_DEFAULT);
        if ((attribute instanceof IppStringAttribute)) {
            String defaultMedia = ((IppStringAttribute) attribute).get(0);
            this.defaultMediaSize = MediaSize.fromAttributeValue(defaultMedia);
        } else {
            this.defaultMediaSize = null;
        }

        int[] mediaTypeDefaultTags = { IppConstants.IppTag.IPP_TAG_KEYWORD.getValue(), IppConstants.IppTag.IPP_TAG_NAME.getValue() };
        attribute = response.findAttributeByAnyTag(IppConstants.IppTag.IPP_TAG_PRINTER, mediaTypeDefaultTags, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_TYPE_DEFAULT);
        if ((attribute instanceof IppStringAttribute)) {
            String defaultMediaType = ((IppStringAttribute) attribute).get(0);
            this.defaultMediaType = MediaType.fromAttributeValue(defaultMediaType);
        } else {
            this.defaultMediaType = null;
        }

        // parse default media source from media-col-default collection
        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_BEGIN_COLLECTION, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_COL_DEFAULT);
        if ((attribute instanceof IppCollectionAttribute) && (attribute.getCount() > 0)) {
            IppAttribute mediaSourceAttr = ((IppCollectionAttribute) attribute).get(0).findAttribute(IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_SOURCE);
            if (mediaSourceAttr instanceof IppStringAttribute) {
                String defaultMediaSourceStr = ((IppStringAttribute) mediaSourceAttr).get(0);
                MediaSource parsed = MediaSource.fromAttributeValue(defaultMediaSourceStr);
                if (parsed == null) {
                    try {
                        parsed = MediaSource.valueOf(defaultMediaSourceStr);
                    } catch (IllegalArgumentException e) {
                        // ignore
                    }
                }
                this.defaultMediaSource = parsed;
            } else {
                this.defaultMediaSource = null;
            }
        } else {
            this.defaultMediaSource = null;
        }

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__SIDES_DEFAULT);
        if ((attribute instanceof IppStringAttribute)) {
            String defaultPlexMode = ((IppStringAttribute) attribute).get(0);
            this.defaultPlexMode = PlexMode.fromAttributeValue(defaultPlexMode);
        } else {
            this.defaultPlexMode = null;
        }

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__PRINT_SCALING_MODE_DEFAULT);
        if ((attribute instanceof IppStringAttribute)) {
            String defaultScalingMode = ((IppStringAttribute) attribute).get(0);
            this.defaultScalingMode = ScalingMode.fromAttributeValue(defaultScalingMode);
        } else {
            this.defaultScalingMode = null;
        }

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_BEGIN_COLLECTION, IppConstants.IPP_ATTRIBUTE_NAME__FINISHINGS_COL_DEFAULT);
        if ((attribute instanceof IppCollectionAttribute) && (attribute.getCount() > 0)) {
            IppAttribute memberAttribute = ((IppCollectionAttribute)attribute).get(0).findAttribute(IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__FINISHING_TEMPLATE);
            if ((memberAttribute instanceof IppStringAttribute)) {
                String defaultStapleMode = ((IppStringAttribute) memberAttribute).get(0);
                this.defaultStapleMode = StapleMode.fromAttributeValue(defaultStapleMode);
            } else {
                this.defaultStapleMode = null;
            }
        } else
            this.defaultStapleMode = null;

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__COLLATE_DEFAULT);
        if ((attribute instanceof IppStringAttribute)) {
            String defaultCollateMode = ((IppStringAttribute) attribute).get(0);
            this.defaultCollateMode = CollateMode.fromAttributeValue(defaultCollateMode);
        } else {
            this.defaultCollateMode = null;
        }

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_ENUM, IppConstants.IPP_ATTRIBUTE_NAME__ORIENTATION_REQUESTED_DEFAULT);
        if ((attribute instanceof IppIntegerAttribute)) {
            int defaultOrientation = ((IppIntegerAttribute)attribute).get(0);
            this.defaultOrientation = Orientation.fromAttributeValue(defaultOrientation);
        } else {
            this.defaultOrientation = null;
        }

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_ENUM, IppConstants.IPP_ATTRIBUTE_NAME__PRINT_QUALITY_DEFAULT);
        if ((attribute instanceof IppIntegerAttribute)) {
            int defaultPrintQuality = ((IppIntegerAttribute)attribute).get(0);
            this.defaultPrintQuality = PrintQuality.fromAttributeValue(defaultPrintQuality);
        } else {
            this.defaultPrintQuality = null;
        }

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__OUTPUT_BIN_DEFAULT);
        if ((attribute instanceof IppStringAttribute)) {
            String defaultOutputBin = ((IppStringAttribute) attribute).get(0);
            this.defaultOutputBin = OutputBin.fromAttributeValue(defaultOutputBin);
        } else {
            this.defaultOutputBin = null;
        }

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_PRINTER, IppConstants.IppTag.IPP_TAG_ENUM, IppConstants.IPP_ATTRIBUTE_NAME__FINISHINGS_DEFAULT);
        if ((attribute instanceof IppIntegerAttribute)) {
            Set<Integer> defaultSet = new HashSet<>();
            for(int defaultFinishing : ((IppIntegerAttribute)attribute).getValues()) {
                defaultSet.add(defaultFinishing);
            }
            ArrayList<Integer> defaultFinishingList = new ArrayList<>(defaultSet);
            this.defaultFinishings = Finishings.fromAttributeValue(defaultFinishingList);
        } else {
            this.defaultFinishings = null;
        }
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
     *
     * @return 0 for no special objects
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written. May be 0 or {@link Parcelable#PARCELABLE_WRITE_RETURN_VALUE}
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.makeAndModel);
        dest.writeInt(this.printerState.ordinal());
        dest.writeStringList(this.printerStateReasons);
        dest.writeInt(this.isColorSupported ? 1: 0);
        dest.writeInt(this.supportedCopies);
        dest.writeInt(this.supportedFileTypes.size());
        for(Enum enumValue : this.supportedFileTypes) {
            dest.writeInt(enumValue.ordinal());
        }
        dest.writeInt(this.supportedColorModes.size());
        for(Enum enumValue : this.supportedColorModes) {
            dest.writeInt(enumValue.ordinal());
        }
        dest.writeInt(this.supportedPlexModes.size());
        for(Enum enumValue : this.supportedPlexModes) {
            dest.writeInt(enumValue.ordinal());
        }
        dest.writeInt(this.supportedScalingModes.size());
        for(Enum enumValue : this.supportedScalingModes) {
            dest.writeInt(enumValue.ordinal());
        }
        dest.writeInt(this.supportedStapleModes.size());
        for(Enum enumValue : this.supportedStapleModes) {
            dest.writeInt(enumValue.ordinal());
        }
        dest.writeInt(this.supportedMediaSources.size());
        for(Enum enumValue : this.supportedMediaSources) {
            dest.writeInt(enumValue.ordinal());
        }
        dest.writeInt(this.supportedMediaSizes.size());
        for(Enum enumValue : this.supportedMediaSizes) {
            dest.writeInt(enumValue.ordinal());
        }
        dest.writeInt(this.supportedMediaTypes.size());
        for(Enum enumValue : this.supportedMediaTypes) {
            dest.writeInt(enumValue.ordinal());
        }
        dest.writeInt(this.supportedCollateModes.size());
        for (Enum enumValue : this.supportedCollateModes) {
            dest.writeInt(enumValue.ordinal());
        }

        dest.writeInt(this.availableMediaSourcesData.size());
        for (String stringValue : this.availableMediaSourcesData) {
            dest.writeString(stringValue);
        }

        dest.writeMap(this.availableMediaSizes);
        dest.writeMap(this.availableMediaTypes);

        dest.writeInt(this.supportedOrientations.size());
        for (Enum enumValue : this.supportedOrientations) {
            dest.writeInt(enumValue.ordinal());
        }

        dest.writeInt(this.supportedPrintQualities.size());
        for (Enum enumValue : this.supportedPrintQualities) {
            dest.writeInt(enumValue.ordinal());
        }

        dest.writeInt(this.supportedOutputBins.size());
        for (Enum enumValue : this.supportedOutputBins) {
            dest.writeInt(enumValue.ordinal());
        }

        dest.writeInt(this.isPageRangesSupported ? 1: 0);

        dest.writeInt(this.supportedFinishingsOptions.size());
        for(Enum enumValue : this.supportedFinishingsOptions) {
            dest.writeInt(enumValue.ordinal());
        }

        dest.writeInt(this.defaultCopies);
        writeNullableEnum(dest, this.defaultColorMode);
        writeNullableEnum(dest, this.defaultFileType);
        writeNullableEnum(dest, this.defaultMediaSize);
        writeNullableEnum(dest, this.defaultMediaType);
        writeNullableEnum(dest, this.defaultMediaSource);
        writeNullableEnum(dest, this.defaultPlexMode);
        writeNullableEnum(dest, this.defaultScalingMode);
        writeNullableEnum(dest, this.defaultStapleMode);
        writeNullableEnum(dest, this.defaultCollateMode);
        writeNullableEnum(dest, this.defaultOrientation);
        writeNullableEnum(dest, this.defaultPrintQuality);
        writeNullableEnum(dest, this.defaultOutputBin);
        dest.writeInt(this.defaultFinishings.size());
        for (Enum enumValue : this.defaultFinishings) {
            dest.writeInt(enumValue.ordinal());
        }
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private PrinterAttributes(Parcel in) {
        this.makeAndModel = in.readString();
        this.printerState = PrinterState.values()[in.readInt()];
        this.printerStateReasons = in.createStringArrayList();
        this.isColorSupported = (in.readInt() > 0);
        this.supportedCopies = in.readInt();
        ArrayList<FileType> fileTypes = new ArrayList<FileType>();
        for(int length = in.readInt(); length > 0; length--) {
            fileTypes.add(FileType.values()[in.readInt()]);
        }
        this.supportedFileTypes = Collections.unmodifiableList(fileTypes);
        ArrayList<ColorMode> colorModes = new ArrayList<ColorMode>();
        for(int length = in.readInt(); length > 0; length--) {
            colorModes.add(ColorMode.values()[in.readInt()]);
        }
        this.supportedColorModes = Collections.unmodifiableList(colorModes);
        ArrayList<PlexMode> plexModes = new ArrayList<PlexMode>();
        for(int length = in.readInt(); length > 0; length--) {
            plexModes.add(PlexMode.values()[in.readInt()]);
        }
        this.supportedPlexModes = Collections.unmodifiableList(plexModes);
        ArrayList<ScalingMode> scalingModes = new ArrayList<ScalingMode>();
        for(int length = in.readInt(); length > 0; length--) {
            scalingModes.add(ScalingMode.values()[in.readInt()]);
        }
        this.supportedScalingModes = Collections.unmodifiableList(scalingModes);
        ArrayList<StapleMode> stapleModes = new ArrayList<StapleMode>();
        for(int length = in.readInt(); length > 0; length--) {
            stapleModes.add(StapleMode.values()[in.readInt()]);
        }
        this.supportedStapleModes = Collections.unmodifiableList(stapleModes);
        ArrayList<MediaSource> mediaSources = new ArrayList<MediaSource>();
        for(int length = in.readInt(); length > 0; length--) {
            mediaSources.add(MediaSource.values()[in.readInt()]);
        }
        this.supportedMediaSources = Collections.unmodifiableList(mediaSources);
        ArrayList<MediaSize> mediaSizes = new ArrayList<MediaSize>();
        for(int length = in.readInt(); length > 0; length--) {
            mediaSizes.add(MediaSize.values()[in.readInt()]);
        }
        this.supportedMediaSizes = Collections.unmodifiableList(mediaSizes);
        ArrayList<MediaType> mediaTypes = new ArrayList<MediaType>();
        for(int length = in.readInt(); length > 0; length--) {
            mediaTypes.add(MediaType.values()[in.readInt()]);
        }
        this.supportedMediaTypes = Collections.unmodifiableList(mediaTypes);
        ArrayList<CollateMode> collateModes = new ArrayList<>();
        for (int length = in.readInt(); length > 0; length--) {
            collateModes.add(CollateMode.values()[in.readInt()]);
        }
        this.supportedCollateModes = Collections.unmodifiableList(collateModes);
        ArrayList<String> mediaSourceData = new ArrayList<>();
        for (int length = in.readInt(); length > 0; length--) {
            mediaSourceData.add(in.readString());
        }
        this.availableMediaSourcesData = mediaSourceData;
        this.availableMediaSizes = in.readHashMap(MediaSize.class.getClassLoader());
        this.availableMediaTypes = in.readHashMap(MediaType.class.getClassLoader());

        ArrayList<Orientation> orientations = new ArrayList<Orientation>();
        for(int length = in.readInt(); length > 0; length--) {
            orientations.add(Orientation.values()[in.readInt()]);
        }
        this.supportedOrientations = Collections.unmodifiableList(orientations);
        ArrayList<PrintQuality> printQualities = new ArrayList<PrintQuality>();
        for(int length = in.readInt(); length > 0; length--) {
            printQualities.add(PrintQuality.values()[in.readInt()]);
        }
        this.supportedPrintQualities = Collections.unmodifiableList(printQualities);
        ArrayList<OutputBin> outputBins = new ArrayList<OutputBin>();
        for(int length = in.readInt(); length > 0; length--) {
            outputBins.add(OutputBin.values()[in.readInt()]);
        }
        this.supportedOutputBins = Collections.unmodifiableList(outputBins);

        this.isPageRangesSupported = (in.readInt() > 0);

        ArrayList<Finishings> finishings = new ArrayList<Finishings>();
        for(int length = in.readInt(); length > 0; length--) {
            finishings.add(Finishings.values()[in.readInt()]);
        }
        this.supportedFinishingsOptions = Collections.unmodifiableList(finishings);

        this.defaultCopies = in.readInt();
        this.defaultColorMode = readNullableEnum(in, ColorMode.values());
        this.defaultFileType = readNullableEnum(in, FileType.values());
        this.defaultMediaSize = readNullableEnum(in, MediaSize.values());
        this.defaultMediaType = readNullableEnum(in, MediaType.values());
        this.defaultMediaSource = readNullableEnum(in, MediaSource.values());
        this.defaultPlexMode = readNullableEnum(in, PlexMode.values());
        this.defaultScalingMode = readNullableEnum(in, ScalingMode.values());
        this.defaultStapleMode = readNullableEnum(in, StapleMode.values());
        this.defaultCollateMode = readNullableEnum(in, CollateMode.values());
        this.defaultOrientation = readNullableEnum(in, Orientation.values());
        this.defaultPrintQuality = readNullableEnum(in, PrintQuality.values());
        this.defaultOutputBin = readNullableEnum(in, OutputBin.values());

        ArrayList<Finishings> defaultFinishings = new ArrayList<>();
        for(int length = in.readInt(); length > 0; length--) {
            defaultFinishings.add(Finishings.values()[in.readInt()]);
        }
        this.defaultFinishings = Collections.unmodifiableList(defaultFinishings);
    }

    /**
     * PrinterAttributes creator
     */
    public static final Parcelable.Creator<PrinterAttributes> CREATOR =
            new Parcelable.Creator<PrinterAttributes>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link PrinterAttributes#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public PrinterAttributes createFromParcel(Parcel in) {
                    return new PrinterAttributes(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public PrinterAttributes[] newArray(int size) {
                    return new PrinterAttributes[size];
                }
            };

    private static void writeNullableEnum(Parcel dest, Enum<?> value) {
        dest.writeInt(value != null ? value.ordinal() : -1);
    }

    private static <T extends Enum<T>> T readNullableEnum(Parcel in, T[] values) {
        int ordinal = in.readInt();
        return (ordinal >= 0) ? values[ordinal] : null;
    }
}
