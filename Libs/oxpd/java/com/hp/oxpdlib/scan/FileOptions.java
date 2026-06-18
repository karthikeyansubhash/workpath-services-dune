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

/**
 * File options object
 * To obtain a FileOptions object, apps should call
 * {@link OXPdScan#GetDefaultFileOptions}, modify the returned FileOptions if required, and then use that
 * FileOptions object to construct a ScanTicket.</b></b>
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class FileOptions implements Parcelable {

    /**
     * OcrLanguage value.
     */
    public OcrLanguage ocrLanguage;
    /**
     * PdfCompressionMode value.
     */
    public PdfCompressionMode pdfCompressionMode;
    /**
     * Password to use when encrypting an output PDF file (min length=1, max length=32, may be null). A null string will result in no encryption being performed. If the device's file options profile indicates that it is incapable of encrypting a file type and color mode combination, but a non-null encryption password is supplied with that file type and color mode in a scan ticket, the scan ticket is invalid.
     */
    public String pdfEncryptionPassword;
    /**
     * TiffCompressionMode value.
     */
    public TiffCompressionMode tiffCompressionMode;
    /**
     * XpsCompressionMode value.
     */
    public XpsCompressionMode xpsCompressionMode;

    /**
     * Constructor used by the library to construct FileOptions objects from the device's DefaultFileOptions. <b>Apps should never construct a FileOptions object. Instead, they should call GetDefaultFileOptions, modify the returned FileOptions if required, and then use that FileOptions object to construct a ScanTicket.</b></b>
     * @param tagHandler
     *              XML handler to extract data from
     * @throws Error
     *              If an error occurs.
     */
    private FileOptions(RestXMLTagHandler tagHandler) throws Error
    {
        OXPdScan.faultExceptionCheck(tagHandler);
        this.ocrLanguage = OcrLanguage.fromAttributeValue((String) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__OCR_LANGUAGE));
        this.pdfCompressionMode = PdfCompressionMode.fromAttributeValue((String) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__PDF_COMPRESSION_MODE));
        String pdfEncryptionPassword = (String) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__PDF_ENCRYPTION_PASSWORD);
        this.pdfEncryptionPassword = (TextUtils.isEmpty(pdfEncryptionPassword) ? null : pdfEncryptionPassword);
        this.tiffCompressionMode = TiffCompressionMode.fromAttributeValue((String) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__TIFF_COMPRESSION_MODE));
        this.xpsCompressionMode = XpsCompressionMode.fromAttributeValue((String) tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__XPS_COMPRESSION_MODE));
    }

    /**
     * Create a copy of the file options
     * @param defaultFileOptions
     *              File options to copy from
     */
    public FileOptions(FileOptions defaultFileOptions) {
        this.ocrLanguage = defaultFileOptions.ocrLanguage;
        this.pdfCompressionMode = defaultFileOptions.pdfCompressionMode;
        this.pdfEncryptionPassword = defaultFileOptions.pdfEncryptionPassword;
        this.tiffCompressionMode = defaultFileOptions.tiffCompressionMode;
        this.xpsCompressionMode = defaultFileOptions.xpsCompressionMode;
    }

    /**
     * Builds a FileOptions instance from the provided HTTP request/response
     * @param device
     *              OXPd device instance
     * @param requestResponse
     *              HTTP request/response pair
     * @param tagHandler
     *              XML tag handler
     * @return
     *              FileOptions instance
     * @throws Error
     *              When errors are detected
     */
    static FileOptions parseRequestResult(OXPdDevice device, HttpRequestResponseContainer requestResponse, RestXMLTagHandler tagHandler) throws Error {
        RestXMLTagHandler.XMLEndTagHandler infoCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName, TextUtils.isEmpty(data) ? null : data);
            }
        };
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__OCR_LANGUAGE, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__PDF_COMPRESSION_MODE, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__TIFF_COMPRESSION_MODE, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__XPS_COMPRESSION_MODE, null, infoCollector);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__PDF_ENCRYPTION_PASSWORD, null, infoCollector);

        device.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);

        return new FileOptions(tagHandler);
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
        dest.writeInt(this.ocrLanguage.ordinal());
        dest.writeInt(this.pdfCompressionMode.ordinal());
        dest.writeString(this.pdfEncryptionPassword);
        dest.writeInt(this.tiffCompressionMode.ordinal());
        dest.writeInt(this.xpsCompressionMode.ordinal());
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private FileOptions(Parcel in) {
        this.ocrLanguage = OcrLanguage.values()[in.readInt()];
        this.pdfCompressionMode = PdfCompressionMode.values()[in.readInt()];
        this.pdfEncryptionPassword = in.readString();
        this.tiffCompressionMode = TiffCompressionMode.values()[in.readInt()];
        this.xpsCompressionMode = XpsCompressionMode.values()[in.readInt()];
    }

    /**
     * FileOptions creator
     */
    public static final Parcelable.Creator<FileOptions> CREATOR =
            new Parcelable.Creator<FileOptions>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link FileOptions#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public FileOptions createFromParcel(Parcel in) {
                    return new FileOptions(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public FileOptions[] newArray(int size) {
                    return new FileOptions[size];
                }
            };
}
