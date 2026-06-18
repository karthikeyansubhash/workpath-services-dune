// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;

import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Device Basic Options profile
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class BasicOptionsProfile implements Parcelable {
    /** List of BackgroundCleanup values supported by the device for the specified TransmissionMode. */
    public final List<BackgroundCleanup> backgroundCleanups;
    /** List of BlankImageRemovalMode values supported by the device for the specified TransmissionMode. */
    public final List<BlankImageRemovalMode> blankImageRemovalModes;
    /** List of ColorDropoutMode values supported by the device for the specified TransmissionMode. */
    public final List<ColorDropoutMode> colorDropoutModes;
    /** List of ContrastAdjustment values supported by the device for the specified TransmissionMode. */
    public final List<ContrastAdjustment> contrastAdjustments;
    /** List of CropMode values supported by the device for the specified TransmissionMode. */
    public final List<CropMode> cropModes;
    /** Maximum supported length of a Custom media size. */
    public final float customMaxLength;
    /** Maximum supported width of a Custom media size. */
    public final float customMaxWidth;
    /** Minimum supported length of a Custom media size. */
    public final float customMinLength;
    /** Minimum supported width of a Custom media size. */
    public final float customMinWidth;
    /** List of DarknessAdjustment values supported by the device for the specified TransmissionMode. */
    public final List<DarknessAdjustment> darknessAdjustments;
    /** List of DuplexFormat values supported by the device for the specified TransmissionMode. */
    public final List<DuplexFormat> duplexFormats;
    /** List of FileTypesByColorMode supported by the device for the specified TransmissionMode.
     * Known constraints exist between file types and color modes (not all file types support all
     * color modes and vice versa). Apps will use this list to select (or enable) a supported file
     * type for a selected color mode (or vice versa). Apps may construct the superset of all
     * supported file types (independent of color mode) by computing the union of all file types
     * for all color modes. */
    public final List<FileTypesByColorMode> fileTypesByColorModes;
    /** List of JobAssemblyMode values supported by the device for the specified TransmissionMode. */
    public final List<JobAssemblyMode> jobAssemblyModes;
    /** List of MediaOrientation values supported by the device for the specified TransmissionMode. */
    public final List<MediaOrientation> mediaOrientations;
    /** List of MediaSize values supported by the device for the specified TransmissionMode. */
    public final List<MediaSize> mediaSizes;
    /** List of MediaSize values supported by the device for the specified TransmissionMode. */
    public final List<MediaSource> mediaSources;
    /** List of MediaWeightAdjustment values supported by the device for the specified TransmissionMode. */
    public final List<MediaWeightAdjustment> mediaWeightAdjustments;
    /** List of MisfeedDetectionMode values supported by the device for the specified TransmissionMode. */
    public final List<MisfeedDetectionMode> misfeedDetectionModes;
    /** List of OutputQuality values supported by the device for the specified TransmissionMode. */
    public final List<OutputQuality> outputQualities;
    /** List of PlexMode values supported by the device for the specified TransmissionMode. */
    public final List<PlexMode> plexModes;
    /** List of PreviewMode values supported by the device for the specified TransmissionMode. */
    public final List<PreviewMode> previewModes;
    /** List of ProgressDialogMode values supported by the device for the specified TransmissionMode. */
    public final List<ProgressDialogMode> progressDialogModes;
    /** List of Resolution values supported by the device for the specified TransmissionMode. */
    public final List<Resolution> resolutions;
    /** List of SharpnessAdjustment values supported by the device for the specified TransmissionMode. */
    public final List<SharpnessAdjustment> sharpnessAdjustments;
    /** List of TextPhotoOptimization values supported by the device for the specified TransmissionMode. */
    public final List<TextPhotoOptimization> textPhotoOptimizations;

    // option 1.3
    public final List<SplitAttachmentByPage> splitAttachmentByPages;
    public final Integer maxPagesPerAttachmentMin;
    public final Integer maxPagesPerAttachmentMax;
    public final List<EraseMarginUnit> eraseMarginUnits;
    public final Float eraseBackBottomMin;
    public final Float eraseBackBottomMax;
    public final Float eraseBackLeftMin;
    public final Float eraseBackLeftMax;
    public final Float eraseBackRightMin;
    public final Float eraseBackRightMax;
    public final Float eraseBackTopMin;
    public final Float eraseBackTopMax;
    public final Float eraseFrontBottomMin;
    public final Float eraseFrontBottomMax;
    public final Float eraseFrontLeftMin;
    public final Float eraseFrontLeftMax;
    public final Float eraseFrontRightMin;
    public final Float eraseFrontRightMax;
    public final Float eraseFrontTopMin;
    public final Float eraseFrontTopMax;
    public final List<CaptureMode> captureModes;
    public final List<AutomaticToneMode> automaticToneModes;
    public final List<AutomaticStraightenMode> automaticStraightenModes;

    /**
     * Constructor used by the library to construct BasicOptionsProfile objects.
     * @param tagHandler
     *              XML handler to extract data from
     */
    @SuppressWarnings("unchecked")
    private BasicOptionsProfile(RestXMLTagHandler tagHandler) throws Error {

        OXPdScan.faultExceptionCheck(tagHandler);

        this.backgroundCleanups = Collections.unmodifiableList((List<BackgroundCleanup>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__BACKGROUND_CLEANUPS, Collections.EMPTY_LIST));
        this.blankImageRemovalModes = Collections.unmodifiableList((List<BlankImageRemovalMode>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__BLANK_IMAGE_REMOVAL_MODES, Collections.EMPTY_LIST));
        this.colorDropoutModes = Collections.unmodifiableList((List<ColorDropoutMode>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__COLOR_DROPOUT_MODES, Collections.EMPTY_LIST));
        this.contrastAdjustments = Collections.unmodifiableList((List<ContrastAdjustment>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__CONTRAST_ADJUSTMENTS, Collections.EMPTY_LIST));
        this.cropModes = Collections.unmodifiableList((List<CropMode>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__CROP_MODES, Collections.EMPTY_LIST));
        this.darknessAdjustments = Collections.unmodifiableList((List<DarknessAdjustment>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__DARKNESS_ADJUSTMENTS, Collections.EMPTY_LIST));
        this.duplexFormats = Collections.unmodifiableList((List<DuplexFormat>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__DUPLEX_FORMATS, Collections.EMPTY_LIST));
        this.fileTypesByColorModes = Collections.unmodifiableList((List<FileTypesByColorMode>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__FILE_TYPES_BY_COLOR_MODES, Collections.EMPTY_LIST));
        this.jobAssemblyModes = Collections.unmodifiableList((List<JobAssemblyMode>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__JOB_ASSEMBLY_MODES, Collections.EMPTY_LIST));
        this.mediaOrientations = Collections.unmodifiableList((List<MediaOrientation>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_ORIENTATIONS, Collections.EMPTY_LIST));
        this.mediaSizes = Collections.unmodifiableList((List<MediaSize>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_SIZES, Collections.EMPTY_LIST));
        this.mediaSources = Collections.unmodifiableList((List<MediaSource>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_SOURCES, Collections.EMPTY_LIST));
        this.mediaWeightAdjustments = Collections.unmodifiableList((List<MediaWeightAdjustment>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_WEIGHT_ADJUSTMENTS, Collections.EMPTY_LIST));
        this.misfeedDetectionModes = Collections.unmodifiableList((List<MisfeedDetectionMode>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__MISFEED_DETECTION_MODES, Collections.EMPTY_LIST));
        this.outputQualities = Collections.unmodifiableList((List<OutputQuality>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__OUTPUT_QUALITIES, Collections.EMPTY_LIST));
        this.plexModes = Collections.unmodifiableList((List<PlexMode>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__PLEXMODES, Collections.EMPTY_LIST));
        this.previewModes = Collections.unmodifiableList((List<PreviewMode>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__PREVIEW_MODES, Collections.EMPTY_LIST));
        this.progressDialogModes = Collections.unmodifiableList((List<ProgressDialogMode>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__PROGRESS_DIALOG_MODES, Collections.EMPTY_LIST));
        this.resolutions = Collections.unmodifiableList((List<Resolution>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__RESOLUTIONS, Collections.EMPTY_LIST));
        this.sharpnessAdjustments = Collections.unmodifiableList((List<SharpnessAdjustment>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__SHARPNESS_ADJUSTMENTS, Collections.EMPTY_LIST));
        this.textPhotoOptimizations = Collections.unmodifiableList((List<TextPhotoOptimization>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__TEXT_PHOTO_OPTIMIZATIONS, Collections.EMPTY_LIST));

        this.customMinWidth = (Float)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__CUSTOM_MIN_WIDTH, 0.0f);
        this.customMaxWidth = (Float)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__CUSTOM_MAX_WIDTH, 0.0f);
        this.customMinLength = (Float)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__CUSTOM_MIN_LENGTH, 0.0f);
        this.customMaxLength = (Float)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__CUSTOM_MAX_LENGTH, 0.0f);

        this.splitAttachmentByPages = Collections.unmodifiableList((List<SplitAttachmentByPage>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__SPLIT_ATTACHMENT_BY_PAGES, Collections.EMPTY_LIST));
        this.maxPagesPerAttachmentMin = (Integer)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__MAX_PAGES_PER_ATTACHMENT_MIN, 0);
        this.maxPagesPerAttachmentMax = (Integer)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__MAX_PAGES_PER_ATTACHMENT_MAX, 0);
        this.eraseMarginUnits = Collections.unmodifiableList((List<EraseMarginUnit>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_MARGIN_UNITS, Collections.EMPTY_LIST));

        this.eraseBackBottomMin = (Float)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_BOTTOM_MIN, 0.0f);
        this.eraseBackBottomMax = (Float)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_BOTTOM_MAX, 0.0f);
        this.eraseBackLeftMin = (Float)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_LEFT_MIN, 0.0f);
        this.eraseBackLeftMax = (Float)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_LEFT_MAX, 0.0f);
        this.eraseBackRightMin = (Float)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_RIGHT_MIN, 0.0f);
        this.eraseBackRightMax = (Float)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_RIGHT_MAX, 0.0f);
        this.eraseBackTopMin = (Float)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_TOP_MIN, 0.0f);
        this.eraseBackTopMax = (Float)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_TOP_MAX, 0.0f);

        this.eraseFrontBottomMin = (Float)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_BOTTOM_MIN, 0.0f);
        this.eraseFrontBottomMax = (Float)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_BOTTOM_MAX, 0.0f);
        this.eraseFrontLeftMin = (Float)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_LEFT_MIN, 0.0f);
        this.eraseFrontLeftMax = (Float)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_LEFT_MAX, 0.0f);
        this.eraseFrontRightMin = (Float)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_RIGHT_MIN, 0.0f);
        this.eraseFrontRightMax = (Float)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_RIGHT_MAX, 0.0f);
        this.eraseFrontTopMin = (Float)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_TOP_MIN, 0.0f);
        this.eraseFrontTopMax = (Float)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_TOP_MAX, 0.0f);

        this.captureModes = Collections.unmodifiableList((List<CaptureMode>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__CAPTURE_MODES, Collections.EMPTY_LIST));
        this.automaticToneModes = Collections.unmodifiableList((List<AutomaticToneMode>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__AUTOMATIC_TONE_MODES, Collections.EMPTY_LIST));
        this.automaticStraightenModes = Collections.unmodifiableList((List<AutomaticStraightenMode>) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__AUTOMATIC_STRAIGHTEN_MODES, Collections.EMPTY_LIST));
    }

    /**
     * Builds a ScannerStatus instance from the provided HTTP request/response
     * @param device
     *              OXPd device instance
     * @param requestResponse
     *              HTTP request/response pair
     * @param tagHandler
     *              XML tag handler
     * @return
     *              BasicOptionsProfile instance
     * @throws Error
     *              When errors are detected
     */
    @SuppressWarnings("unchecked")
    static BasicOptionsProfile parseRequestResult(OXPdDevice device, HttpRequestResponseContainer requestResponse, RestXMLTagHandler tagHandler) throws Error {

        RestXMLTagHandler.XMLStartTagHandler listCreator = new RestXMLTagHandler.XMLStartTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, Attributes attributes) {
                if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__BACKGROUND_CLEANUPS, localName)) {
                    handler.setTagData(localName, new ArrayList<BackgroundCleanup>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__BACKGROUND_CLEANUPS, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                BackgroundCleanup backgroundCleanup = BackgroundCleanup.fromAttributeValue(data);
                                if (backgroundCleanup != null) {
                                    ((List<BackgroundCleanup>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__BACKGROUND_CLEANUPS)).add(backgroundCleanup);
                                }
                            }
                        }
                    });
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__BLANK_IMAGE_REMOVAL_MODES, localName)) {
                    handler.setTagData(localName, new ArrayList<BlankImageRemovalMode>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__BLANK_IMAGE_REMOVAL_MODES, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                BlankImageRemovalMode blankImageRemovalMode = BlankImageRemovalMode.fromAttributeValue(data);
                                if (blankImageRemovalMode != null) {
                                    ((List<BlankImageRemovalMode>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__BLANK_IMAGE_REMOVAL_MODES)).add(blankImageRemovalMode);
                                }
                            }
                        }
                    });
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__COLOR_DROPOUT_MODES, localName)) {
                    handler.setTagData(localName, new ArrayList<ColorDropoutMode>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__COLOR_DROPOUT_MODES, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                ColorDropoutMode colorDropoutMode = ColorDropoutMode.fromAttributeValue(data);
                                if (colorDropoutMode != null) {
                                    ((List<ColorDropoutMode>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__COLOR_DROPOUT_MODES)).add(colorDropoutMode);
                                }
                            }
                        }
                    });
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__CONTRAST_ADJUSTMENTS, localName)) {
                    handler.setTagData(localName, new ArrayList<ContrastAdjustment>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__CONTRAST_ADJUSTMENTS, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                ContrastAdjustment contrastAdjustment = ContrastAdjustment.fromAttributeValue(data);
                                if (contrastAdjustment != null) {
                                    ((List<ContrastAdjustment>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__CONTRAST_ADJUSTMENTS)).add(contrastAdjustment);
                                }
                            }
                        }
                    });
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__CROP_MODES, localName)) {
                    handler.setTagData(localName, new ArrayList<CropMode>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__CROP_MODES, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                CropMode cropMode = CropMode.fromAttributeValue(data);
                                if (cropMode != null) {
                                    ((List<CropMode>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__CROP_MODES)).add(cropMode);
                                }
                            }
                        }
                    });
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__DARKNESS_ADJUSTMENTS, localName)) {
                    handler.setTagData(localName, new ArrayList<DarknessAdjustment>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__DARKNESS_ADJUSTMENTS, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                DarknessAdjustment darknessAdjustment = DarknessAdjustment.fromAttributeValue(data);
                                if (darknessAdjustment != null) {
                                    ((List<DarknessAdjustment>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__DARKNESS_ADJUSTMENTS)).add(darknessAdjustment);
                                }
                            }
                        }
                    });
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__DUPLEX_FORMATS, localName)) {
                    handler.setTagData(localName, new ArrayList<DuplexFormat>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__DUPLEX_FORMATS, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                DuplexFormat duplexFormat = DuplexFormat.fromAttributeValue(data);
                                if (duplexFormat != null) {
                                    ((List<DuplexFormat>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__DUPLEX_FORMATS)).add(duplexFormat);
                                }
                            }
                        }
                    });
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__JOB_ASSEMBLY_MODES, localName)) {
                    handler.setTagData(localName, new ArrayList<JobAssemblyMode>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__JOB_ASSEMBLY_MODES, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                JobAssemblyMode jobAssemblyMode = JobAssemblyMode.fromAttributeValue(data);
                                if (jobAssemblyMode != null) {
                                    ((List<JobAssemblyMode>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__JOB_ASSEMBLY_MODES)).add(jobAssemblyMode);
                                }
                            }
                        }
                    });
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_ORIENTATIONS, localName)) {
                    handler.setTagData(localName, new ArrayList<MediaOrientation>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_ORIENTATIONS, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                MediaOrientation mediaOrientation = MediaOrientation.fromAttributeValue(data);
                                if (mediaOrientation != null) {
                                    ((List<MediaOrientation>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_ORIENTATIONS)).add(mediaOrientation);
                                }
                            }
                        }
                    });
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_SIZES, localName)) {
                    handler.setTagData(localName, new ArrayList<MediaSize>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_SIZES, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                MediaSize mediaSize = MediaSize.fromAttributeValue(data);
                                if (mediaSize != null) {
                                    ((List<MediaSize>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_SIZES)).add(mediaSize);
                                }
                            }
                        }
                    });
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_SOURCES, localName)) {
                    handler.setTagData(localName, new ArrayList<MediaSource>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_SOURCES, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                MediaSource mediaSource = MediaSource.fromAttributeValue(data);
                                if (mediaSource != null) {
                                    ((List<MediaSource>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_SOURCES)).add(mediaSource);
                                }
                            }
                        }
                    });
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_WEIGHT_ADJUSTMENTS, localName)) {
                    handler.setTagData(localName, new ArrayList<MediaWeightAdjustment>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_WEIGHT_ADJUSTMENTS, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                MediaWeightAdjustment mediaWeightAdjustment = MediaWeightAdjustment.fromAttributeValue(data);
                                if (mediaWeightAdjustment != null) {
                                    ((List<MediaWeightAdjustment>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_WEIGHT_ADJUSTMENTS)).add(mediaWeightAdjustment);
                                }
                            }
                        }
                    });
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__MISFEED_DETECTION_MODES, localName)) {
                    handler.setTagData(localName, new ArrayList<MisfeedDetectionMode>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__MISFEED_DETECTION_MODES, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                MisfeedDetectionMode misfeedDetectionMode = MisfeedDetectionMode.fromAttributeValue(data);
                                if (misfeedDetectionMode != null) {
                                    ((List<MisfeedDetectionMode>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__MISFEED_DETECTION_MODES)).add(misfeedDetectionMode);
                                }
                            }
                        }
                    });
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__OUTPUT_QUALITIES, localName)) {
                    handler.setTagData(localName, new ArrayList<OutputQuality>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__OUTPUT_QUALITIES, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                OutputQuality outputQuality = OutputQuality.fromAttributeValue(data);
                                if (outputQuality != null) {
                                    ((List<OutputQuality>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__OUTPUT_QUALITIES)).add(outputQuality);
                                }
                            }
                        }
                    });
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__PLEXMODES, localName)) {
                    handler.setTagData(localName, new ArrayList<PlexMode>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__PLEXMODES, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                PlexMode plexMode = PlexMode.fromAttributeValue(data);
                                if (plexMode != null) {
                                    ((List<PlexMode>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__PLEXMODES)).add(plexMode);
                                }
                            }
                        }
                    });
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__PREVIEW_MODES, localName)) {
                    handler.setTagData(localName, new ArrayList<PreviewMode>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__PREVIEW_MODES, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                PreviewMode previewMode = PreviewMode.fromAttributeValue(data);
                                if (previewMode != null) {
                                    ((List<PreviewMode>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__PREVIEW_MODES)).add(previewMode);
                                }
                            }
                        }
                    });
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__PROGRESS_DIALOG_MODES, localName)) {
                    handler.setTagData(localName, new ArrayList<ProgressDialogMode>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__PROGRESS_DIALOG_MODES, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                ProgressDialogMode progressDialogMode = ProgressDialogMode.fromAttributeValue(data);
                                if (progressDialogMode != null) {
                                    ((List<ProgressDialogMode>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__PROGRESS_DIALOG_MODES)).add(progressDialogMode);
                                }
                            }
                        }
                    });
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__RESOLUTIONS, localName)) {
                    handler.setTagData(localName, new ArrayList<Resolution>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__RESOLUTIONS, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                Resolution resolution = Resolution.fromAttributeValue(data);
                                if (resolution != null) {
                                    ((List<Resolution>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__RESOLUTIONS)).add(resolution);
                                }
                            }
                        }
                    });
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__SHARPNESS_ADJUSTMENTS, localName)) {
                    handler.setTagData(localName, new ArrayList<SharpnessAdjustment>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__SHARPNESS_ADJUSTMENTS, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                SharpnessAdjustment sharpnessAdjustment = SharpnessAdjustment.fromAttributeValue(data);
                                if (sharpnessAdjustment != null) {
                                    ((List<SharpnessAdjustment>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__SHARPNESS_ADJUSTMENTS)).add(sharpnessAdjustment);
                                }
                            }
                        }
                    });
                }
                else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__TEXT_PHOTO_OPTIMIZATIONS, localName)) {
                    handler.setTagData(localName, new ArrayList<TextPhotoOptimization>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__TEXT_PHOTO_OPTIMIZATIONS, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                TextPhotoOptimization textPhotoOptimization = TextPhotoOptimization.fromAttributeValue(data);
                                if (textPhotoOptimization != null) {
                                    ((List<TextPhotoOptimization>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__TEXT_PHOTO_OPTIMIZATIONS)).add(textPhotoOptimization);
                                }
                            }
                        }
                    });
                }
                // option 1.3
                else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__SPLIT_ATTACHMENT_BY_PAGES, localName)) {
                    handler.setTagData(localName, new ArrayList<SplitAttachmentByPage>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__SPLIT_ATTACHMENT_BY_PAGES, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                SplitAttachmentByPage splitAttachmentByPage = SplitAttachmentByPage.fromAttributeValue(data);
                                if (splitAttachmentByPage != null) {
                                    ((List<SplitAttachmentByPage>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__SPLIT_ATTACHMENT_BY_PAGES)).add(splitAttachmentByPage);
                                }
                            }
                        }
                    });
                }
                else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__ERASE_MARGIN_UNITS, localName)) {
                    handler.setTagData(localName, new ArrayList<EraseMarginUnit>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__ERASE_MARGIN_UNITS, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                EraseMarginUnit eraseMarginUnit = EraseMarginUnit.fromAttributeValue(data);
                                if (eraseMarginUnit != null) {
                                    ((List<EraseMarginUnit>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__ERASE_MARGIN_UNITS)).add(eraseMarginUnit);
                                }
                            }
                        }
                    });
                }
                else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__CAPTURE_MODES, localName)) {
                    handler.setTagData(localName, new ArrayList<CaptureMode>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__CAPTURE_MODES, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                CaptureMode captureMode = CaptureMode.fromAttributeValue(data);
                                if (captureMode != null) {
                                    ((List<CaptureMode>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__CAPTURE_MODES)).add(captureMode);
                                }
                            }
                        }
                    });
                }
                else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__AUTOMATIC_TONE_MODES, localName)) {
                    handler.setTagData(localName, new ArrayList<AutomaticToneMode>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__AUTOMATIC_TONE_MODES, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                AutomaticToneMode automaticToneMode = AutomaticToneMode.fromAttributeValue(data);
                                if (automaticToneMode != null) {
                                    ((List<AutomaticToneMode>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__AUTOMATIC_TONE_MODES)).add(automaticToneMode);
                                }
                            }
                        }
                    });
                }
                else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__AUTOMATIC_STRAIGHTEN_MODES, localName)) {
                    handler.setTagData(localName, new ArrayList<AutomaticStraightenMode>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__AUTOMATIC_STRAIGHTEN_MODES, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                AutomaticStraightenMode automaticStraightenMode = AutomaticStraightenMode.fromAttributeValue(data);
                                if (automaticStraightenMode != null) {
                                    ((List<AutomaticStraightenMode>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__AUTOMATIC_STRAIGHTEN_MODES)).add(automaticStraightenMode);
                                }
                            }
                        }
                    });
                }
                else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__FILE_TYPES_BY_COLOR_MODES, localName)) {
                    handler.setTagData(localName, new ArrayList<FileTypesByColorMode>());
                    handler.setGenericXMLHandler(
                            new RestXMLTagHandler.XMLStartTagHandler() {
                                @Override
                                public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, Attributes attributes) {
                                    if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__FILE_TYPES, localName)) {
                                        handler.setTagData(localName, new ArrayList<FileType>());
                                    }
                                }
                            },
                            new RestXMLTagHandler.XMLEndTagHandler() {
                                @Override
                                public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                                    if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__FILE_TYPES_BY_COLOR_MODES, localName)) {
                                        handler.setGenericXMLHandler(null, null);
                                    } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__FILE_TYPES_BY_COLOR_MODE, localName)){

                                        ColorMode colorMode = (ColorMode)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__COLOR_MODE);
                                        if (colorMode != null) {
                                            FileTypesByColorMode fileTypesByColorMode = new FileTypesByColorMode(
                                                    colorMode,
                                                    (List<FileType>) handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__FILE_TYPES)
                                            );
                                            ((List<FileTypesByColorMode>) handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__FILE_TYPES_BY_COLOR_MODES)).add(fileTypesByColorMode);
                                        }
                                        handler.clearTagData(OXPdScan.Constants.XML_TAG__SCAN__COLOR_MODE);
                                        handler.clearTagData(OXPdScan.Constants.XML_TAG__SCAN__FILE_TYPES);

                                    } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__FILE_TYPE, localName)) {
                                        FileType file = FileType.fromAttributeValue(data);
                                        if (file != null) {
                                            ((List<FileType>) handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__FILE_TYPES)).add(file);
                                        }
                                    } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__COLOR_MODE, localName)) {
                                        handler.setTagData(OXPdScan.Constants.XML_TAG__SCAN__COLOR_MODE, ColorMode.fromAttributeValue(data));
                                    }
                                }
                            });
                }
            }
        };

        RestXMLTagHandler.XMLEndTagHandler dimensionCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                Float value = null;
                try {
                    value = Float.valueOf(data);
                } catch(NumberFormatException ignored) {
                }
                handler.setTagData(localName, value);
            }
        };

        RestXMLTagHandler.XMLEndTagHandler intDimensionCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                Integer value = null;
                try {
                    value = Integer.valueOf(data);
                } catch(NumberFormatException ignored) {
                }
                handler.setTagData(localName, value);
            }
        };

        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__BACKGROUND_CLEANUPS, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__BLANK_IMAGE_REMOVAL_MODES, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__COLOR_DROPOUT_MODES, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__CONTRAST_ADJUSTMENTS, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__CROP_MODES, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__DARKNESS_ADJUSTMENTS, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__DUPLEX_FORMATS, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__FILE_TYPES_BY_COLOR_MODES, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__JOB_ASSEMBLY_MODES, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_ORIENTATIONS, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_SIZES, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_SOURCES, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__MEDIA_WEIGHT_ADJUSTMENTS, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__MISFEED_DETECTION_MODES, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__OUTPUT_QUALITIES, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__PLEXMODES, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__PREVIEW_MODES, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__PROGRESS_DIALOG_MODES, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__RESOLUTIONS, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__SHARPNESS_ADJUSTMENTS, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__TEXT_PHOTO_OPTIMIZATIONS, listCreator, null);
        // version 1.3 options
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__SPLIT_ATTACHMENT_BY_PAGES, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__MAX_PAGES_PER_ATTACHMENT_MIN, null, intDimensionCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__MAX_PAGES_PER_ATTACHMENT_MAX, null, intDimensionCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_MARGIN_UNITS, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_BOTTOM_MIN, null, dimensionCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_BOTTOM_MAX, null, dimensionCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_LEFT_MIN, null, dimensionCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_LEFT_MAX, null, dimensionCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_RIGHT_MIN, null, dimensionCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_RIGHT_MAX, null, dimensionCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_TOP_MIN, null, dimensionCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_BACK_TOP_MAX, null, dimensionCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_BOTTOM_MIN, null, dimensionCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_BOTTOM_MAX, null, dimensionCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_LEFT_MIN, null, dimensionCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_LEFT_MAX, null, dimensionCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_RIGHT_MIN, null, dimensionCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_RIGHT_MAX, null, dimensionCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_TOP_MIN, null, dimensionCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__ERASE_FRONT_TOP_MAX, null, dimensionCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__CAPTURE_MODES, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__AUTOMATIC_TONE_MODES, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__AUTOMATIC_STRAIGHTEN_MODES, listCreator, null);

        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__CUSTOM_MIN_LENGTH, null, dimensionCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__CUSTOM_MAX_LENGTH, null, dimensionCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__CUSTOM_MIN_WIDTH, null, dimensionCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__CUSTOM_MAX_WIDTH, null, dimensionCollector);


        device.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);

        return new BasicOptionsProfile(tagHandler);
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
     * @return
     *              0 for no special objects
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
        dest.writeInt(this.backgroundCleanups.size());
        for(Enum enumEntry : this.backgroundCleanups) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.blankImageRemovalModes.size());
        for(Enum enumEntry : this.blankImageRemovalModes) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.colorDropoutModes.size());
        for(Enum enumEntry : this.colorDropoutModes) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.contrastAdjustments.size());
        for(Enum enumEntry : this.contrastAdjustments) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.cropModes.size());
        for(Enum enumEntry : this.cropModes) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.darknessAdjustments.size());
        for(Enum enumEntry : this.darknessAdjustments) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.duplexFormats.size());
        for(Enum enumEntry : this.duplexFormats) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeTypedList(this.fileTypesByColorModes);
        dest.writeInt(this.jobAssemblyModes.size());
        for(Enum enumEntry : this.jobAssemblyModes) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.mediaOrientations.size());
        for(Enum enumEntry : this.mediaOrientations) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.mediaSizes.size());
        for(Enum enumEntry : this.mediaSizes) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.mediaSources.size());
        for(Enum enumEntry : this.mediaSources) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.mediaWeightAdjustments.size());
        for(Enum enumEntry : this.mediaWeightAdjustments) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.misfeedDetectionModes.size());
        for(Enum enumEntry : this.misfeedDetectionModes) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.outputQualities.size());
        for(Enum enumEntry : this.outputQualities) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.plexModes.size());
        for(Enum enumEntry : this.plexModes) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.previewModes.size());
        for(Enum enumEntry : this.previewModes) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.progressDialogModes.size());
        for(Enum enumEntry : this.progressDialogModes) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.resolutions.size());
        for(Enum enumEntry : this.resolutions) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.sharpnessAdjustments.size());
        for(Enum enumEntry : this.sharpnessAdjustments) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.textPhotoOptimizations.size());
        for(Enum enumEntry : this.textPhotoOptimizations) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeFloat(this.customMinWidth);
        dest.writeFloat(this.customMaxWidth);
        dest.writeFloat(this.customMinLength);
        dest.writeFloat(this.customMaxLength);

        dest.writeInt(this.splitAttachmentByPages.size());
        for(Enum enumEntry : this.splitAttachmentByPages) {
            dest.writeInt(enumEntry.ordinal());
        }

        dest.writeInt(this.maxPagesPerAttachmentMin);
        dest.writeInt(this.maxPagesPerAttachmentMax);

        dest.writeInt(this.eraseMarginUnits.size());
        for(Enum enumEntry : this.eraseMarginUnits) {
            dest.writeInt(enumEntry.ordinal());
        }

        dest.writeFloat(this.eraseBackBottomMin);
        dest.writeFloat(this.eraseBackBottomMax);
        dest.writeFloat(this.eraseBackLeftMin);
        dest.writeFloat(this.eraseBackLeftMax);
        dest.writeFloat(this.eraseBackRightMin);
        dest.writeFloat(this.eraseBackRightMax);
        dest.writeFloat(this.eraseBackTopMin);
        dest.writeFloat(this.eraseBackTopMax);

        dest.writeFloat(this.eraseFrontBottomMin);
        dest.writeFloat(this.eraseFrontBottomMax);
        dest.writeFloat(this.eraseFrontLeftMin);
        dest.writeFloat(this.eraseFrontLeftMax);
        dest.writeFloat(this.eraseFrontRightMin);
        dest.writeFloat(this.eraseFrontRightMax);
        dest.writeFloat(this.eraseFrontTopMin);
        dest.writeFloat(this.eraseFrontTopMax);

        dest.writeInt(this.captureModes.size());
        for(Enum enumEntry : this.captureModes) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.automaticToneModes.size());
        for(Enum enumEntry : this.automaticToneModes) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.automaticStraightenModes.size());
        for(Enum enumEntry : this.automaticStraightenModes) {
            dest.writeInt(enumEntry.ordinal());
        }
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private BasicOptionsProfile(Parcel in) {
        List<BackgroundCleanup> backgroundCleanups = new ArrayList<BackgroundCleanup>();
        for(int length = in.readInt(); length > 0; length--) {
            backgroundCleanups.add(BackgroundCleanup.values()[in.readInt()]);
        }
        this.backgroundCleanups = Collections.unmodifiableList(backgroundCleanups);
        List<BlankImageRemovalMode> blankImageRemovalModes = new ArrayList<BlankImageRemovalMode>();
        for(int length = in.readInt(); length > 0; length--) {
            blankImageRemovalModes.add(BlankImageRemovalMode.values()[in.readInt()]);
        }
        this.blankImageRemovalModes = Collections.unmodifiableList(blankImageRemovalModes);
        List<ColorDropoutMode> colorDropoutModes = new ArrayList<ColorDropoutMode>();
        for(int length = in.readInt(); length > 0; length--) {
            colorDropoutModes.add(ColorDropoutMode.values()[in.readInt()]);
        }
        this.colorDropoutModes = Collections.unmodifiableList(colorDropoutModes);
        List<ContrastAdjustment> contrastAdjustments = new ArrayList<ContrastAdjustment>();
        for(int length = in.readInt(); length > 0; length--) {
            contrastAdjustments.add(ContrastAdjustment.values()[in.readInt()]);
        }
        this.contrastAdjustments = Collections.unmodifiableList(contrastAdjustments);
        List<CropMode> cropModes = new ArrayList<CropMode>();
        for(int length = in.readInt(); length > 0; length--) {
            cropModes.add(CropMode.values()[in.readInt()]);
        }
        this.cropModes = Collections.unmodifiableList(cropModes);
        List<DarknessAdjustment> darknessAdjustments = new ArrayList<DarknessAdjustment>();
        for(int length = in.readInt(); length > 0; length--) {
            darknessAdjustments.add(DarknessAdjustment.values()[in.readInt()]);
        }
        this.darknessAdjustments = Collections.unmodifiableList(darknessAdjustments);
        List<DuplexFormat> duplexFormats = new ArrayList<DuplexFormat>();
        for(int length = in.readInt(); length > 0; length--) {
            duplexFormats.add(DuplexFormat.values()[in.readInt()]);
        }
        this.duplexFormats = Collections.unmodifiableList(duplexFormats);
        this.fileTypesByColorModes = Collections.unmodifiableList(in.createTypedArrayList(FileTypesByColorMode.CREATOR));
        List<JobAssemblyMode> jobAssemblyModes = new ArrayList<JobAssemblyMode>();
        for(int length = in.readInt(); length > 0; length--) {
            jobAssemblyModes.add(JobAssemblyMode.values()[in.readInt()]);
        }
        this.jobAssemblyModes = Collections.unmodifiableList(jobAssemblyModes);
        List<MediaOrientation> mediaOrientations = new ArrayList<MediaOrientation>();
        for(int length = in.readInt(); length > 0; length--) {
            mediaOrientations.add(MediaOrientation.values()[in.readInt()]);
        }
        this.mediaOrientations = Collections.unmodifiableList(mediaOrientations);
        List<MediaSize> mediaSizes = new ArrayList<MediaSize>();
        for(int length = in.readInt(); length > 0; length--) {
            mediaSizes.add(MediaSize.values()[in.readInt()]);
        }
        this.mediaSizes = Collections.unmodifiableList(mediaSizes);
        List<MediaSource> mediaSources = new ArrayList<MediaSource>();
        for(int length = in.readInt(); length > 0; length--) {
            mediaSources.add(MediaSource.values()[in.readInt()]);
        }
        this.mediaSources = Collections.unmodifiableList(mediaSources);
        List<MediaWeightAdjustment> mediaWeightAdjustments = new ArrayList<MediaWeightAdjustment>();
        for(int length = in.readInt(); length > 0; length--) {
            mediaWeightAdjustments.add(MediaWeightAdjustment.values()[in.readInt()]);
        }
        this.mediaWeightAdjustments = Collections.unmodifiableList(mediaWeightAdjustments);
        List<MisfeedDetectionMode> misfeedDetectionModes = new ArrayList<MisfeedDetectionMode>();
        for(int length = in.readInt(); length > 0; length--) {
            misfeedDetectionModes.add(MisfeedDetectionMode.values()[in.readInt()]);
        }
        this.misfeedDetectionModes = Collections.unmodifiableList(misfeedDetectionModes);
        List<OutputQuality> outputQualities = new ArrayList<OutputQuality>();
        for(int length = in.readInt(); length > 0; length--) {
            outputQualities.add(OutputQuality.values()[in.readInt()]);
        }
        this.outputQualities = Collections.unmodifiableList(outputQualities);
        List<PlexMode> plexModes = new ArrayList<PlexMode>();
        for(int length = in.readInt(); length > 0; length--) {
            plexModes.add(PlexMode.values()[in.readInt()]);
        }
        this.plexModes = Collections.unmodifiableList(plexModes);
        List<PreviewMode> previewModes = new ArrayList<PreviewMode>();
        for(int length = in.readInt(); length > 0; length--) {
            previewModes.add(PreviewMode.values()[in.readInt()]);
        }
        this.previewModes = Collections.unmodifiableList(previewModes);
        List<ProgressDialogMode> progressDialogModes = new ArrayList<ProgressDialogMode>();
        for(int length = in.readInt(); length > 0; length--) {
            progressDialogModes.add(ProgressDialogMode.values()[in.readInt()]);
        }
        this.progressDialogModes = Collections.unmodifiableList(progressDialogModes);
        List<Resolution> resolutions = new ArrayList<Resolution>();
        for(int length = in.readInt(); length > 0; length--) {
            resolutions.add(Resolution.values()[in.readInt()]);
        }
        this.resolutions = Collections.unmodifiableList(resolutions);
        List<SharpnessAdjustment> sharpnessAdjustments = new ArrayList<SharpnessAdjustment>();
        for(int length = in.readInt(); length > 0; length--) {
            sharpnessAdjustments.add(SharpnessAdjustment.values()[in.readInt()]);
        }
        this.sharpnessAdjustments = Collections.unmodifiableList(sharpnessAdjustments);
        List<TextPhotoOptimization> textPhotoOptimizations = new ArrayList<TextPhotoOptimization>();
        for(int length = in.readInt(); length > 0; length--) {
            textPhotoOptimizations.add(TextPhotoOptimization.values()[in.readInt()]);
        }
        this.textPhotoOptimizations = Collections.unmodifiableList(textPhotoOptimizations);
        this.customMinWidth = in.readFloat();
        this.customMaxWidth = in.readFloat();
        this.customMinLength = in.readFloat();
        this.customMaxLength = in.readFloat();

        List<SplitAttachmentByPage> splitAttachmentByPages = new ArrayList<SplitAttachmentByPage>();
        for(int length = in.readInt(); length > 0; length--) {
            splitAttachmentByPages.add(SplitAttachmentByPage.values()[in.readInt()]);
        }
        this.splitAttachmentByPages = Collections.unmodifiableList(splitAttachmentByPages);

        this.maxPagesPerAttachmentMin = in.readInt();
        this.maxPagesPerAttachmentMax = in.readInt();

        List<EraseMarginUnit> eraseMarginUnits = new ArrayList<EraseMarginUnit>();
        for(int length = in.readInt(); length > 0; length--) {
            eraseMarginUnits.add(EraseMarginUnit.values()[in.readInt()]);
        }
        this.eraseMarginUnits = Collections.unmodifiableList(eraseMarginUnits);

        this.eraseBackBottomMin = in.readFloat();
        this.eraseBackBottomMax = in.readFloat();
        this.eraseBackLeftMin = in.readFloat();
        this.eraseBackLeftMax = in.readFloat();
        this.eraseBackRightMin = in.readFloat();
        this.eraseBackRightMax = in.readFloat();
        this.eraseBackTopMin = in.readFloat();
        this.eraseBackTopMax = in.readFloat();

        this.eraseFrontBottomMin = in.readFloat();
        this.eraseFrontBottomMax = in.readFloat();
        this.eraseFrontLeftMin = in.readFloat();
        this.eraseFrontLeftMax = in.readFloat();
        this.eraseFrontRightMin = in.readFloat();
        this.eraseFrontRightMax = in.readFloat();
        this.eraseFrontTopMin = in.readFloat();
        this.eraseFrontTopMax = in.readFloat();

        List<CaptureMode> captureModes = new ArrayList<CaptureMode>();
        for(int length = in.readInt(); length > 0; length--) {
            captureModes.add(CaptureMode.values()[in.readInt()]);
        }
        this.captureModes = Collections.unmodifiableList(captureModes);

        List<AutomaticToneMode> automaticToneModes = new ArrayList<AutomaticToneMode>();
        for(int length = in.readInt(); length > 0; length--) {
            automaticToneModes.add(AutomaticToneMode.values()[in.readInt()]);
        }
        this.automaticToneModes = Collections.unmodifiableList(automaticToneModes);

        List<AutomaticStraightenMode> automaticStraightenModes = new ArrayList<AutomaticStraightenMode>();
        for(int length = in.readInt(); length > 0; length--) {
            automaticStraightenModes.add(AutomaticStraightenMode.values()[in.readInt()]);
        }
        this.automaticStraightenModes = Collections.unmodifiableList(automaticStraightenModes);
    }

    /**
     * BasicOptionsProfile creator
     */
    public static final Parcelable.Creator<BasicOptionsProfile> CREATOR =
            new Parcelable.Creator<BasicOptionsProfile>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link BasicOptionsProfile#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public BasicOptionsProfile createFromParcel(Parcel in) {
                    return new BasicOptionsProfile(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public BasicOptionsProfile[] newArray(int size) {
                    return new BasicOptionsProfile[size];
                }
            };
}
