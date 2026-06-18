// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.os.Parcel;
import android.os.Parcelable;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;

/**
 * Validity status of current ScanTicket
 *  <p>A scan ticket validation structure indicates whether the supplied scan ticket specifies
 *  valid ticket properties for this device, and if not valid, an indication of which specific
 *  ticket properties are invalid and why.</p>
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ScanTicketValidation implements Parcelable {

    private static final String XML_TAG__SCAN__IS_SCAN_TICKET_VALID = "isScanTicketValid";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_BACKGROUND_CLEANUP_VALIDITY = "basicOptions_backgroundCleanup_validity";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_BLANK_IMAGE_REMOVAL_MODE_VALIDITY = "basicOptions_blankImageRemovalMode_validity";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_COLOR_DROPOUT_MODE_VALIDITY = "basicOptions_colorDropoutMode_validity";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_COLOR_MODE_VALIDITY = "basicOptions_colorMode_validity";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_CONTRAST_ADJUSTMENT_VALIDITY = "basicOptions_contrastAdjustment_validity ";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_CROP_MODE_VALIDITY = "basicOptions_cropMode_validity";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_CUSTOM_LENGTH_VALIDITY = "basicOptions_customLength_validity";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_CUSTOM_WIDTH_VALIDITY = "basicOptions_customWidth_validity";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_DARKNESS_ADJUSTMENT_VALIDITY = "basicOptions_darknessAdjustment_validity";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_DUPLEX_FORMAT_VALIDITY = "basicOptions_duplexFormat_validity";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_FILE_TYPE_VALIDITY = "basicOptions_fileType_validity";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_JOB_ASSEMBLY_MODE_VALIDITY = "basicOptions_jobAssemblyMode_validity";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_MEDIA_ORIENTATION_VALIDITY = "basicOptions_mediaOrientation_validity";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_MEDIA_SIZE_VALIDITY = "basicOptions_mediaSize_validity";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_MEDIA_SOURCE_VALIDITY = "basicOptions_mediaSource_validity";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_MEDIA_WEIGHT_ADJUSTMENT_VALIDITY = "basicOptions_mediaWeightAdjustment_validity";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_MISFEED_DETECTION_MODE_VALIDITY = "basicOptions_misfeedDetectionMode_validity";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_OUTPUT_QUALITY_VALIDITY = "basicOptions_outputQuality_validity";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_PLEX_MODE_VALIDITY = "basicOptions_plexMode_validity";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_PREVIEW_MODE_VALIDITY = "basicOptions_previewMode_validity";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_PROGRESS_DIALOG_MODE_VALIDITY = "basicOptions_progressDialogMode_validity";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_RESOLUTION_VALIDITY = "basicOptions_resolution_validity";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_SHARPNESS_ADJUSTMENT_VALIDITY = "basicOptions_sharpnessAdjustment_validity";
    private static final String XML_TAG__SCAN__BASIC_OPTIONS_TEXT_PHOTO_OPTIMIZATION_VALIDITY = "basicOptions_textPhotoOptimization_validity";
    private static final String XML_TAG__SCAN__CALLBACK_BINDING_VALIDITY = "callback_binding_validity";
    private static final String XML_TAG__SCAN__CALLBACK_CONNECTION_TIMEOUT_VALIDITY = "callback_connectionTimeout_validity";
    private static final String XML_TAG__SCAN__CALLBACK_NETWORK_CREDENTIALS_DOMAIN_VALIDITY = "callback_networkCredentials_domain_validity";
    private static final String XML_TAG__SCAN__CALLBACK_NETWORK_CREDENTIALS_PASSWORD_VALIDITY = "callback_networkCredentials_password_validity";
    private static final String XML_TAG__SCAN__CALLBACK_NETWORK_CREDENTIALS_USER_NAME_VALIDITY = "callback_networkCredentials_userName_validity";
    private static final String XML_TAG__SCAN__CALLBACK_RESPONSE_TIMEOUT_VALIDITY = "callback_responseTimeout_validity";
    private static final String XML_TAG__SCAN__CALLBACK_URI_VALIDITY = "callback_uri_validity";
    private static final String XML_TAG__SCAN__DESTINATION_BINDING_VALIDITY = "destination_binding_validity";
    private static final String XML_TAG__SCAN__DESTINATION_CONNECTION_TIMEOUT_VALIDITY = "destination_connectionTimeout_validity";
    private static final String XML_TAG__SCAN__DESTINATION_MAX_CONSECUTIVE_RETRIES_VALIDITY = "destination_maxConsecutiveRetries_validity";
    private static final String XML_TAG__SCAN__DESTINATION_NETWORK_CREDENTIALS_DOMAIN_VALIDITY = "destination_networkCredentials_domain_validity";
    private static final String XML_TAG__SCAN__DESTINATION_NETWORK_CREDENTIALS_PASSWORD_VALIDITY = "destination_networkCredentials_password_validity";
    private static final String XML_TAG__SCAN__DESTINATION_NETWORK_CREDENTIALS_USER_NAME_VALIDITY = "destination_networkCredentials_userName_validity";
    private static final String XML_TAG__SCAN__DESTINATION_RESPONSE_TIMEOUT_VALIDITY = "destination_responseTimeout_validity";
    private static final String XML_TAG__SCAN__DESTINATION_RETRY_INTERVAL_VALIDITY = "destination_retryInterval_validity";
    private static final String XML_TAG__SCAN__DESTINATION_URI_VALIDITY = "destination_uri_validity";
    private static final String XML_TAG__SCAN__FILE_OPTIONS_OCR_LANGUAGE_VALIDITY = "fileOptions_ocrLanguage_validity";
    private static final String XML_TAG__SCAN__FILE_OPTIONS_PDF_COMPRESSION_MODE_VALIDITY = "fileOptions_pdfCompressionMode_validity";
    private static final String XML_TAG__SCAN__FILE_OPTIONS_PDF_ENCRYPTION_PASSWORD_VALIDITY = "fileOptions_pdfEncryptionPassword_validity";
    private static final String XML_TAG__SCAN__FILE_OPTIONS_TIFF_COMPRESSION_MODE_VALIDITY = "fileOptions_tiffCompressionMode_validity";
    private static final String XML_TAG__SCAN__FILE_OPTIONS_XPS_COMPRESSION_MODE_VALIDITY = "fileOptions_xpsCompressionMode_validity";
    private static final String XML_TAG__SCAN__METADATA_FILE_LIST_DELIMITER_VALIDITY = "metadata_fileListDelimiter_validity";
    private static final String XML_TAG__SCAN__METADATA_FILE_NAME_BASE_VALIDITY = "metadata_fileNameBase_validity";
    private static final String XML_TAG__SCAN__METADATA_FILE_NAME_EXTENSION_VALIDITY = "metadata_fileNameExtension_validity";
    private static final String XML_TAG__SCAN__METADATA_FORMAT_STRING_VALIDITY = "metadata_formatString_validity";
    private static final String XML_TAG__SCAN__SCAN_FILE_NAME_BASE_VALIDITY = "scanFileNameBase_validity";
    private static final String XML_TAG__SCAN__TRANSMISSION_MODE_VALIDITY = "transmissionMode_validity";
    
    /**
     * Indicates whether the entire scan ticket is valid.
     */
    public final boolean isScanTicketValid;
    /**
     * Validation code for {@see BasicOptions#backgroundCleanup|BasicOptions.backgroundCleanup}.
     */
    public final ValidationCode basicOptions_backgroundCleanup_validity;
    /**
     * Validation code for {@see BasicOptions#blankImageRemovalMode|BasicOptions.blankImageRemovalMode}.
     */
    public final ValidationCode basicOptions_blankImageRemovalMode_validity;
    /**
     * Validation code for {@see BasicOptions#colorDropoutMode|BasicOptions.colorDropoutMode}.
     */
    public final ValidationCode basicOptions_colorDropoutMode_validity;
    /**
     * Validation code for {@see BasicOptions#colorMode|BasicOptions.colorMode}.
     */
    public final ValidationCode basicOptions_colorMode_validity;
    /**
     * Validation code for {@see BasicOptions#contrastAdjustment|BasicOptions.contrastAdjustment}.
     */
    public final ValidationCode basicOptions_contrastAdjustment_validity ;
    /**
     * Validation code for {@see BasicOptions#cropMode|BasicOptions.cropMode}.
     */
    public final ValidationCode basicOptions_cropMode_validity;
    /**
     * Validation code for {@see BasicOptions#customLength|BasicOptions.customLength}.
     */
    public final ValidationCode basicOptions_customLength_validity;
    /**
     * Validation code for {@see BasicOptions#customWidth|BasicOptions.customWidth}.
     */
    public final ValidationCode basicOptions_customWidth_validity;
    /**
     * Validation code for {@see BasicOptions#darknessAdjustment|BasicOptions.darknessAdjustment}.
     */
    public final ValidationCode basicOptions_darknessAdjustment_validity;
    /**
     * Validation code for {@see BasicOptions#duplexFormat|BasicOptions.duplexFormat}.
     */
    public final ValidationCode basicOptions_duplexFormat_validity;
    /**
     * Validation code for {@see BasicOptions#fileType|BasicOptions.fileType}.
     */
    public final ValidationCode basicOptions_fileType_validity;
    /**
     * Validation code for {@see BasicOptions#jobAssemblyMode|BasicOptions.jobAssemblyMode}.
     */
    public final ValidationCode basicOptions_jobAssemblyMode_validity;
    /**
     * Validation code for {@see BasicOptions#mediaOrientation|BasicOptions.mediaOrientation}.
     */
    public final ValidationCode basicOptions_mediaOrientation_validity;
    /**
     * Validation code for {@see BasicOptions#mediaSize|BasicOptions.mediaSize}.
     */
    public final ValidationCode basicOptions_mediaSize_validity;
    /**
     * Validation code for {@see BasicOptions#mediaSource|BasicOptions.mediaSource}.
     */
    public final ValidationCode basicOptions_mediaSource_validity;
    /**
     * Validation code for {@see BasicOptions#mediaWeightAdjustment|BasicOptions.mediaWeightAdjustment}.
     */
    public final ValidationCode basicOptions_mediaWeightAdjustment_validity;
    /**
     * Validation code for {@see BasicOptions#misfeedDetectionMode|BasicOptions.misfeedDetectionMode}.
     */
    public final ValidationCode basicOptions_misfeedDetectionMode_validity;
    /**
     * Validation code for {@see BasicOptions#outputQuality|BasicOptions.outputQuality}.
     */
    public final ValidationCode basicOptions_outputQuality_validity;
    /**
     * Validation code for {@see BasicOptions#plexMode|BasicOptions.plexMode}.
     */
    public final ValidationCode basicOptions_plexMode_validity;
    /**
     * Validation code for {@see BasicOptions#previewMode|BasicOptions.previewMode}.
     */
    public final ValidationCode basicOptions_previewMode_validity;
    /**
     * Validation code for {@see BasicOptions#progressDialogMode|BasicOptions.progressDialogMode}.
     */
    public final ValidationCode basicOptions_progressDialogMode_validity;
    /**
     * Validation code for {@see BasicOptions#resolution|BasicOptions.resolution}.
     */
    public final ValidationCode basicOptions_resolution_validity;
    /**
     * Validation code for {@see BasicOptions#sharpnessAdjustment|BasicOptions.sharpnessAdjustment}.
     */
    public final ValidationCode basicOptions_sharpnessAdjustment_validity;
    /**
     * Validation code for {@see BasicOptions#textPhotoOptimization|BasicOptions.textPhotoOptimization}.
     */
    public final ValidationCode basicOptions_textPhotoOptimization_validity;
    /**
     * Validation code for callback bindings (callbacks are not supported by this SDK).
     */
    public final ValidationCode callback_binding_validity;
    /**
     * Validation code for callback connectionTimeout (callbacks are not supported by this SDK).
     */
    public final ValidationCode callback_connectionTimeout_validity;
    /**
     * Validation code for callback domain (callbacks are not supported by this SDK).
     */
    public final ValidationCode callback_networkCredentials_domain_validity;
    /**
     * Validation code for callback password (callbacks are not supported by this SDK).
     */
    public final ValidationCode callback_networkCredentials_password_validity;
    /**
     * Validation code for callback userName (callbacks are not supported by this SDK).
     */
    public final ValidationCode callback_networkCredentials_userName_validity;
    /**
     * Validation code for callback responseTimeout (callbacks are not supported by this SDK).
     */
    public final ValidationCode callback_responseTimeout_validity;
    /**
     * Validation code for callback uri (callbacks are not supported by this SDK).
     */
    public final ValidationCode callback_uri_validity;
    /**
     * This value is hardcoded by the SDK and should never be invalid.
     */
    public final ValidationCode destination_binding_validity;
    /**
     * Validation code for {@see Destination#connectionTimeout|Destination.connectionTimeout}.
     */
    public final ValidationCode destination_connectionTimeout_validity;
    /**
     * Validation code for {@see Destination#maxConsecutiveRetries|Destination.maxConsecutiveRetries}.
     */
    public final ValidationCode destination_maxConsecutiveRetries_validity;
    /**
     * Validation code for {@see Destination#domain|Destination.domain}.
     */
    public final ValidationCode destination_networkCredentials_domain_validity;
    /**
     * Validation code for {@see Destination#password|Destination.password}.
     */
    public final ValidationCode destination_networkCredentials_password_validity;
    /**
     * Validation code for {@see Destination#userName|Destination.userName}.
     */
    public final ValidationCode destination_networkCredentials_userName_validity;
    /**
     * Validation code for {@see Destination#responseTimeout|Destination.responseTimeout}.
     */
    public final ValidationCode destination_responseTimeout_validity;
    /**
     * Validation code for {@see Destination#retryInterval|Destination.retryInterval}.
     */
    public final ValidationCode destination_retryInterval_validity;
    /**
     * Validation code for {@see Destination#uri|Destination.uri}.
     */
    public final ValidationCode destination_uri_validity;
    /**
     * Validation code for {@see FileOptions#ocrLanguage|FileOptions.ocrLanguage}.
     */
    public final ValidationCode fileOptions_ocrLanguage_validity;
    /**
     * Validation code for {@see FileOptions#pdfCompressionMode|FileOptions.pdfCompressionMode}.
     */
    public final ValidationCode fileOptions_pdfCompressionMode_validity;
    /**
     * Validation code for {@see FileOptions#pdfEncryptionPassword|FileOptions.pdfEncryptionPassword}.
     */
    public final ValidationCode fileOptions_pdfEncryptionPassword_validity;
    /**
     * Validation code for {@see FileOptions#tiffCompressionMode|FileOptions.tiffCompressionMode}.
     */
    public final ValidationCode fileOptions_tiffCompressionMode_validity;
    /**
     * Validation code for {@see FileOptions#xpsCompressionMode|FileOptions.xpsCompressionMode}.
     */
    public final ValidationCode fileOptions_xpsCompressionMode_validity;
    /**
     * Validation code for {@see Metadata#fileListDelimiter|Metadata.fileListDelimiter}.
     */
    public final ValidationCode metadata_fileListDelimiter_validity;
    /**
     * Validation code for {@see Metadata#fileNameBase|Metadata.fileNameBase}.
     */
    public final ValidationCode metadata_fileNameBase_validity;
    /**
     * Validation code for {@see Metadata#fileNameExtension|Metadata.fileNameExtension}.
     */
    public final ValidationCode metadata_fileNameExtension_validity;
    /**
     * Validation code for {@see Metadata#formatString|Metadata.formatString}.
     */
    public final ValidationCode metadata_formatString_validity;
    /**
     * Validation code for {@see ScanTicket#scanFileNameBase|ScanTicket.scanFileNameBase}.
     */
    public final ValidationCode scanFileNameBase_validity;
    /**
     * Validation code for {@see ScanTicket#transmissionMode|ScanTicket.transmissionMode}.
     */
    public final ValidationCode transmissionMode_validity;


    /**
     * Constructor used by the library to construct ScanTicketValidation objects from the device's ScanTicketValidation.
     * @param tagHandler XML handler to extract data from
     * @throws Error If an error occurs.
     */
    private ScanTicketValidation(RestXMLTagHandler tagHandler) throws Error
    {
        OXPdScan.faultExceptionCheck(tagHandler);

        this.isScanTicketValid = (Boolean)(tagHandler.getTagData(XML_TAG__SCAN__IS_SCAN_TICKET_VALID, Boolean.FALSE));
        this.basicOptions_backgroundCleanup_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_BACKGROUND_CLEANUP_VALIDITY, null);
        this.basicOptions_blankImageRemovalMode_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_BLANK_IMAGE_REMOVAL_MODE_VALIDITY, null);
        this.basicOptions_colorDropoutMode_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_COLOR_DROPOUT_MODE_VALIDITY, null);
        this.basicOptions_colorMode_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_COLOR_MODE_VALIDITY, null);
        this.basicOptions_contrastAdjustment_validity  = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_CONTRAST_ADJUSTMENT_VALIDITY, null);
        this.basicOptions_cropMode_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_CROP_MODE_VALIDITY, null);
        this.basicOptions_customLength_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_CUSTOM_LENGTH_VALIDITY, null);
        this.basicOptions_customWidth_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_CUSTOM_WIDTH_VALIDITY, null);
        this.basicOptions_darknessAdjustment_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_DARKNESS_ADJUSTMENT_VALIDITY, null);
        this.basicOptions_duplexFormat_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_DUPLEX_FORMAT_VALIDITY, null);
        this.basicOptions_fileType_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_FILE_TYPE_VALIDITY, null);
        this.basicOptions_jobAssemblyMode_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_JOB_ASSEMBLY_MODE_VALIDITY, null);
        this.basicOptions_mediaOrientation_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_MEDIA_ORIENTATION_VALIDITY, null);
        this.basicOptions_mediaSize_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_MEDIA_SIZE_VALIDITY, null);
        this.basicOptions_mediaSource_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_MEDIA_SOURCE_VALIDITY, null);
        this.basicOptions_mediaWeightAdjustment_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_MEDIA_WEIGHT_ADJUSTMENT_VALIDITY, null);
        this.basicOptions_misfeedDetectionMode_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_MISFEED_DETECTION_MODE_VALIDITY, null);
        this.basicOptions_outputQuality_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_OUTPUT_QUALITY_VALIDITY, null);
        this.basicOptions_plexMode_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_PLEX_MODE_VALIDITY, null);
        this.basicOptions_previewMode_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_PREVIEW_MODE_VALIDITY, null);
        this.basicOptions_progressDialogMode_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_PROGRESS_DIALOG_MODE_VALIDITY, null);
        this.basicOptions_resolution_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_RESOLUTION_VALIDITY, null);
        this.basicOptions_sharpnessAdjustment_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_SHARPNESS_ADJUSTMENT_VALIDITY, null);
        this.basicOptions_textPhotoOptimization_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__BASIC_OPTIONS_TEXT_PHOTO_OPTIMIZATION_VALIDITY, null);
        this.callback_binding_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__CALLBACK_BINDING_VALIDITY, null);
        this.callback_connectionTimeout_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__CALLBACK_CONNECTION_TIMEOUT_VALIDITY, null);
        this.callback_networkCredentials_domain_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__CALLBACK_NETWORK_CREDENTIALS_DOMAIN_VALIDITY, null);
        this.callback_networkCredentials_password_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__CALLBACK_NETWORK_CREDENTIALS_PASSWORD_VALIDITY, null);
        this.callback_networkCredentials_userName_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__CALLBACK_NETWORK_CREDENTIALS_USER_NAME_VALIDITY, null);
        this.callback_responseTimeout_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__CALLBACK_RESPONSE_TIMEOUT_VALIDITY, null);
        this.callback_uri_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__CALLBACK_URI_VALIDITY, null);
        this.destination_binding_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__DESTINATION_BINDING_VALIDITY, null);
        this.destination_connectionTimeout_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__DESTINATION_CONNECTION_TIMEOUT_VALIDITY, null);
        this.destination_maxConsecutiveRetries_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__DESTINATION_MAX_CONSECUTIVE_RETRIES_VALIDITY, null);
        this.destination_networkCredentials_domain_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__DESTINATION_NETWORK_CREDENTIALS_DOMAIN_VALIDITY, null);
        this.destination_networkCredentials_password_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__DESTINATION_NETWORK_CREDENTIALS_PASSWORD_VALIDITY, null);
        this.destination_networkCredentials_userName_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__DESTINATION_NETWORK_CREDENTIALS_USER_NAME_VALIDITY, null);
        this.destination_responseTimeout_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__DESTINATION_RESPONSE_TIMEOUT_VALIDITY, null);
        this.destination_retryInterval_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__DESTINATION_RETRY_INTERVAL_VALIDITY, null);
        this.destination_uri_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__DESTINATION_URI_VALIDITY, null);
        this.fileOptions_ocrLanguage_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__FILE_OPTIONS_OCR_LANGUAGE_VALIDITY, null);
        this.fileOptions_pdfCompressionMode_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__FILE_OPTIONS_PDF_COMPRESSION_MODE_VALIDITY, null);
        this.fileOptions_pdfEncryptionPassword_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__FILE_OPTIONS_PDF_ENCRYPTION_PASSWORD_VALIDITY, null);
        this.fileOptions_tiffCompressionMode_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__FILE_OPTIONS_TIFF_COMPRESSION_MODE_VALIDITY, null);
        this.fileOptions_xpsCompressionMode_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__FILE_OPTIONS_XPS_COMPRESSION_MODE_VALIDITY, null);
        this.metadata_fileListDelimiter_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__METADATA_FILE_LIST_DELIMITER_VALIDITY, null);
        this.metadata_fileNameBase_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__METADATA_FILE_NAME_BASE_VALIDITY, null);
        this.metadata_fileNameExtension_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__METADATA_FILE_NAME_EXTENSION_VALIDITY, null);
        this.metadata_formatString_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__METADATA_FORMAT_STRING_VALIDITY, null);
        this.scanFileNameBase_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__SCAN_FILE_NAME_BASE_VALIDITY, null);
        this.transmissionMode_validity = (ValidationCode)tagHandler.getTagData(XML_TAG__SCAN__TRANSMISSION_MODE_VALIDITY, null);
    }

    /**
     * Builds a ScanTicketValidation instance from the provided HTTP request/response
     * @param device
     *              OXPd device instance
     * @param requestResponse
     *              HTTP request/response pair
     * @param tagHandler
     *              XML tag handler
     * @return
     *              ScanTicketValidation instance
     * @throws Error
     *              When errors are detected
     */
    static ScanTicketValidation parseRequestResult(OXPdDevice device, HttpRequestResponseContainer requestResponse, RestXMLTagHandler tagHandler) throws Error {
        
        RestXMLTagHandler.XMLEndTagHandler validatorCreator = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName, ValidationCode.fromAttributeValue(data));
            }
        };

        RestXMLTagHandler.XMLEndTagHandler booleanCreator = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName, Boolean.valueOf(data));
            }
        };

        tagHandler.setXMLHandler(XML_TAG__SCAN__IS_SCAN_TICKET_VALID, null, booleanCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_BACKGROUND_CLEANUP_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_BLANK_IMAGE_REMOVAL_MODE_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_COLOR_DROPOUT_MODE_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_COLOR_MODE_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_CONTRAST_ADJUSTMENT_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_CROP_MODE_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_CUSTOM_LENGTH_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_CUSTOM_WIDTH_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_DARKNESS_ADJUSTMENT_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_DUPLEX_FORMAT_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_FILE_TYPE_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_JOB_ASSEMBLY_MODE_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_MEDIA_ORIENTATION_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_MEDIA_SIZE_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_MEDIA_SOURCE_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_MEDIA_WEIGHT_ADJUSTMENT_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_MISFEED_DETECTION_MODE_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_OUTPUT_QUALITY_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_PLEX_MODE_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_PREVIEW_MODE_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_PROGRESS_DIALOG_MODE_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_RESOLUTION_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_SHARPNESS_ADJUSTMENT_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__BASIC_OPTIONS_TEXT_PHOTO_OPTIMIZATION_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__CALLBACK_BINDING_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__CALLBACK_CONNECTION_TIMEOUT_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__CALLBACK_NETWORK_CREDENTIALS_DOMAIN_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__CALLBACK_NETWORK_CREDENTIALS_PASSWORD_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__CALLBACK_NETWORK_CREDENTIALS_USER_NAME_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__CALLBACK_RESPONSE_TIMEOUT_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__CALLBACK_URI_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__DESTINATION_BINDING_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__DESTINATION_CONNECTION_TIMEOUT_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__DESTINATION_MAX_CONSECUTIVE_RETRIES_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__DESTINATION_NETWORK_CREDENTIALS_DOMAIN_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__DESTINATION_NETWORK_CREDENTIALS_PASSWORD_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__DESTINATION_NETWORK_CREDENTIALS_USER_NAME_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__DESTINATION_RESPONSE_TIMEOUT_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__DESTINATION_RETRY_INTERVAL_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__DESTINATION_URI_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__FILE_OPTIONS_OCR_LANGUAGE_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__FILE_OPTIONS_PDF_COMPRESSION_MODE_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__FILE_OPTIONS_PDF_ENCRYPTION_PASSWORD_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__FILE_OPTIONS_TIFF_COMPRESSION_MODE_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__FILE_OPTIONS_XPS_COMPRESSION_MODE_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__METADATA_FILE_LIST_DELIMITER_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__METADATA_FILE_NAME_BASE_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__METADATA_FILE_NAME_EXTENSION_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__METADATA_FORMAT_STRING_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__SCAN_FILE_NAME_BASE_VALIDITY, null, validatorCreator);
        tagHandler.setXMLHandler(XML_TAG__SCAN__TRANSMISSION_MODE_VALIDITY, null, validatorCreator);

        device.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);

        return new ScanTicketValidation(tagHandler);
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
        dest.writeInt(this.isScanTicketValid ? 1 : 0);
        dest.writeInt(this.basicOptions_backgroundCleanup_validity.ordinal());
        dest.writeInt(this.basicOptions_blankImageRemovalMode_validity.ordinal());
        dest.writeInt(this.basicOptions_colorDropoutMode_validity.ordinal());
        dest.writeInt(this.basicOptions_colorMode_validity.ordinal());
        dest.writeInt(this.basicOptions_contrastAdjustment_validity .ordinal());
        dest.writeInt(this.basicOptions_cropMode_validity.ordinal());
        dest.writeInt(this.basicOptions_customLength_validity.ordinal());
        dest.writeInt(this.basicOptions_customWidth_validity.ordinal());
        dest.writeInt(this.basicOptions_darknessAdjustment_validity.ordinal());
        dest.writeInt(this.basicOptions_duplexFormat_validity.ordinal());
        dest.writeInt(this.basicOptions_fileType_validity.ordinal());
        dest.writeInt(this.basicOptions_jobAssemblyMode_validity.ordinal());
        dest.writeInt(this.basicOptions_mediaOrientation_validity.ordinal());
        dest.writeInt(this.basicOptions_mediaSize_validity.ordinal());
        dest.writeInt(this.basicOptions_mediaSource_validity.ordinal());
        dest.writeInt(this.basicOptions_mediaWeightAdjustment_validity.ordinal());
        dest.writeInt(this.basicOptions_misfeedDetectionMode_validity.ordinal());
        dest.writeInt(this.basicOptions_outputQuality_validity.ordinal());
        dest.writeInt(this.basicOptions_plexMode_validity.ordinal());
        dest.writeInt(this.basicOptions_previewMode_validity.ordinal());
        dest.writeInt(this.basicOptions_progressDialogMode_validity.ordinal());
        dest.writeInt(this.basicOptions_resolution_validity.ordinal());
        dest.writeInt(this.basicOptions_sharpnessAdjustment_validity.ordinal());
        dest.writeInt(this.basicOptions_textPhotoOptimization_validity.ordinal());
        dest.writeInt(this.callback_binding_validity.ordinal());
        dest.writeInt(this.callback_connectionTimeout_validity.ordinal());
        dest.writeInt(this.callback_networkCredentials_domain_validity.ordinal());
        dest.writeInt(this.callback_networkCredentials_password_validity.ordinal());
        dest.writeInt(this.callback_networkCredentials_userName_validity.ordinal());
        dest.writeInt(this.callback_responseTimeout_validity.ordinal());
        dest.writeInt(this.callback_uri_validity.ordinal());
        dest.writeInt(this.destination_binding_validity.ordinal());
        dest.writeInt(this.destination_connectionTimeout_validity.ordinal());
        dest.writeInt(this.destination_maxConsecutiveRetries_validity.ordinal());
        dest.writeInt(this.destination_networkCredentials_domain_validity.ordinal());
        dest.writeInt(this.destination_networkCredentials_password_validity.ordinal());
        dest.writeInt(this.destination_networkCredentials_userName_validity.ordinal());
        dest.writeInt(this.destination_responseTimeout_validity.ordinal());
        dest.writeInt(this.destination_retryInterval_validity.ordinal());
        dest.writeInt(this.destination_uri_validity.ordinal());
        dest.writeInt(this.fileOptions_ocrLanguage_validity.ordinal());
        dest.writeInt(this.fileOptions_pdfCompressionMode_validity.ordinal());
        dest.writeInt(this.fileOptions_pdfEncryptionPassword_validity.ordinal());
        dest.writeInt(this.fileOptions_tiffCompressionMode_validity.ordinal());
        dest.writeInt(this.fileOptions_xpsCompressionMode_validity.ordinal());
        dest.writeInt(this.metadata_fileListDelimiter_validity.ordinal());
        dest.writeInt(this.metadata_fileNameBase_validity.ordinal());
        dest.writeInt(this.metadata_fileNameExtension_validity.ordinal());
        dest.writeInt(this.metadata_formatString_validity.ordinal());
        dest.writeInt(this.scanFileNameBase_validity.ordinal());
        dest.writeInt(this.transmissionMode_validity.ordinal());
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private ScanTicketValidation(Parcel in) {
        this.isScanTicketValid = in.readInt() > 0;
        this.basicOptions_backgroundCleanup_validity = ValidationCode.values()[in.readInt()];
        this.basicOptions_blankImageRemovalMode_validity = ValidationCode.values()[in.readInt()];
        this.basicOptions_colorDropoutMode_validity = ValidationCode.values()[in.readInt()];
        this.basicOptions_colorMode_validity = ValidationCode.values()[in.readInt()];
        this.basicOptions_contrastAdjustment_validity  = ValidationCode.values()[in.readInt()];
        this.basicOptions_cropMode_validity = ValidationCode.values()[in.readInt()];
        this.basicOptions_customLength_validity = ValidationCode.values()[in.readInt()];
        this.basicOptions_customWidth_validity = ValidationCode.values()[in.readInt()];
        this.basicOptions_darknessAdjustment_validity = ValidationCode.values()[in.readInt()];
        this.basicOptions_duplexFormat_validity = ValidationCode.values()[in.readInt()];
        this.basicOptions_fileType_validity = ValidationCode.values()[in.readInt()];
        this.basicOptions_jobAssemblyMode_validity = ValidationCode.values()[in.readInt()];
        this.basicOptions_mediaOrientation_validity = ValidationCode.values()[in.readInt()];
        this.basicOptions_mediaSize_validity = ValidationCode.values()[in.readInt()];
        this.basicOptions_mediaSource_validity = ValidationCode.values()[in.readInt()];
        this.basicOptions_mediaWeightAdjustment_validity = ValidationCode.values()[in.readInt()];
        this.basicOptions_misfeedDetectionMode_validity = ValidationCode.values()[in.readInt()];
        this.basicOptions_outputQuality_validity = ValidationCode.values()[in.readInt()];
        this.basicOptions_plexMode_validity = ValidationCode.values()[in.readInt()];
        this.basicOptions_previewMode_validity = ValidationCode.values()[in.readInt()];
        this.basicOptions_progressDialogMode_validity = ValidationCode.values()[in.readInt()];
        this.basicOptions_resolution_validity = ValidationCode.values()[in.readInt()];
        this.basicOptions_sharpnessAdjustment_validity = ValidationCode.values()[in.readInt()];
        this.basicOptions_textPhotoOptimization_validity = ValidationCode.values()[in.readInt()];
        this.callback_binding_validity = ValidationCode.values()[in.readInt()];
        this.callback_connectionTimeout_validity = ValidationCode.values()[in.readInt()];
        this.callback_networkCredentials_domain_validity = ValidationCode.values()[in.readInt()];
        this.callback_networkCredentials_password_validity = ValidationCode.values()[in.readInt()];
        this.callback_networkCredentials_userName_validity = ValidationCode.values()[in.readInt()];
        this.callback_responseTimeout_validity = ValidationCode.values()[in.readInt()];
        this.callback_uri_validity = ValidationCode.values()[in.readInt()];
        this.destination_binding_validity = ValidationCode.values()[in.readInt()];
        this.destination_connectionTimeout_validity = ValidationCode.values()[in.readInt()];
        this.destination_maxConsecutiveRetries_validity = ValidationCode.values()[in.readInt()];
        this.destination_networkCredentials_domain_validity = ValidationCode.values()[in.readInt()];
        this.destination_networkCredentials_password_validity = ValidationCode.values()[in.readInt()];
        this.destination_networkCredentials_userName_validity = ValidationCode.values()[in.readInt()];
        this.destination_responseTimeout_validity = ValidationCode.values()[in.readInt()];
        this.destination_retryInterval_validity = ValidationCode.values()[in.readInt()];
        this.destination_uri_validity = ValidationCode.values()[in.readInt()];
        this.fileOptions_ocrLanguage_validity = ValidationCode.values()[in.readInt()];
        this.fileOptions_pdfCompressionMode_validity = ValidationCode.values()[in.readInt()];
        this.fileOptions_pdfEncryptionPassword_validity = ValidationCode.values()[in.readInt()];
        this.fileOptions_tiffCompressionMode_validity = ValidationCode.values()[in.readInt()];
        this.fileOptions_xpsCompressionMode_validity = ValidationCode.values()[in.readInt()];
        this.metadata_fileListDelimiter_validity = ValidationCode.values()[in.readInt()];
        this.metadata_fileNameBase_validity = ValidationCode.values()[in.readInt()];
        this.metadata_fileNameExtension_validity = ValidationCode.values()[in.readInt()];
        this.metadata_formatString_validity = ValidationCode.values()[in.readInt()];
        this.scanFileNameBase_validity = ValidationCode.values()[in.readInt()];
        this.transmissionMode_validity = ValidationCode.values()[in.readInt()];
    }

    /**
     * ScanTicketValidation creator
     */
    public static final Parcelable.Creator<ScanTicketValidation> CREATOR =
            new Parcelable.Creator<ScanTicketValidation>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link ScanTicketValidation#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public ScanTicketValidation createFromParcel(Parcel in) {
                    return new ScanTicketValidation(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public ScanTicketValidation[] newArray(int size) {
                    return new ScanTicketValidation[size];
                }
            };
}
