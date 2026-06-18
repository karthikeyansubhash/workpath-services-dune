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
 * File options profile
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class FileOptionsProfile implements Parcelable {

    /**
     * Indicates whether the device is capable of encrypting the requested file type and color mode combination with a supplied encryption password. If the device's profile indicates that it is incapable of encrypting a file type and color mode combination, but a non-null encryption password is supplied with that file type and color mode in a scan ticket, the scan ticket is invalid.
     */
    public final boolean isPdfEncryptionPasswordSupported;
    /**
     * List of ocrLanguage values supported by the device for the specified file type and color mode.
     */
    public final List<OcrLanguage> ocrLanguages;
    /**
     * List of pdfCompressionMode values supported by the device for the specified file type and color mode.
     */
    public final List<PdfCompressionMode> pdfCompressionModes;
    /**
     * List of tiffCompressionMode values supported by the device for the specified file type and color mode.
     */
    public final List<TiffCompressionMode> tiffCompressionModes;
    /**
     * List of xpsCompressionMode values supported by the device for the specified file type and color mode.
     */
    public final List<XpsCompressionMode> xpsCompressionModes;

    /**
     * Constructor used by the library to construct FileOptionsProfile objects.
     * @param tagHandler
     *              XML handler to extract data from
     * @throws Error
     *              If an error occurs.
     */
    @SuppressWarnings("unchecked")
    private FileOptionsProfile(RestXMLTagHandler tagHandler) throws Error
    {
        List<OcrLanguage> ocrLanguageList = (List<OcrLanguage>)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__OCR_LANGUAGES);
        if (ocrLanguageList == null) {
            ocrLanguageList = Collections.emptyList();
        }
        this.ocrLanguages = Collections.unmodifiableList(ocrLanguageList);

        List<PdfCompressionMode> pdfCompressionModeList = (List<PdfCompressionMode>)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__PDF_COMPRESSION_MODES);
        if (pdfCompressionModeList == null) {
            pdfCompressionModeList = Collections.emptyList();
        }
        this.pdfCompressionModes = Collections.unmodifiableList(pdfCompressionModeList);

        List<TiffCompressionMode> tiffCompressionModeList = (List<TiffCompressionMode>)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__TIFF_COMPRESSION_MODES);
        if (tiffCompressionModeList == null) {
            tiffCompressionModeList = Collections.emptyList();
        }
        this.tiffCompressionModes = Collections.unmodifiableList(tiffCompressionModeList);

        List<XpsCompressionMode> xpsCompressionModeList = (List<XpsCompressionMode>)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__XPS_COMPRESSION_MODES);
        if (xpsCompressionModeList == null) {
            xpsCompressionModeList = Collections.emptyList();
        }
        this.xpsCompressionModes = Collections.unmodifiableList(xpsCompressionModeList);

        this.isPdfEncryptionPasswordSupported = Boolean.valueOf((String)tagHandler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__IS_PDF_ENCRYPTION_PASSWORD_SUPPORTED));
    }

    //For SDK Simulator
    private FileOptionsProfile(List<OcrLanguage> ocrLanguageList,
                               List<PdfCompressionMode> pdfCompressionModeList,
                               List<TiffCompressionMode> tiffCompressionModeList,
                               List<XpsCompressionMode> xpsCompressionModeList,
                               String isPdfEncryptionPasswordSupported) throws Error
    {
        if (ocrLanguageList == null) {
            ocrLanguageList = Collections.emptyList();
        }
        this.ocrLanguages = Collections.unmodifiableList(ocrLanguageList);
        if (pdfCompressionModeList == null) {
            pdfCompressionModeList = Collections.emptyList();
        }
        this.pdfCompressionModes = Collections.unmodifiableList(pdfCompressionModeList);
        if (tiffCompressionModeList == null) {
            tiffCompressionModeList = Collections.emptyList();
        }
        this.tiffCompressionModes = Collections.unmodifiableList(tiffCompressionModeList);
        if (xpsCompressionModeList == null) {
            xpsCompressionModeList = Collections.emptyList();
        }
        this.xpsCompressionModes = Collections.unmodifiableList(xpsCompressionModeList);
        this.isPdfEncryptionPasswordSupported = Boolean.valueOf((String)isPdfEncryptionPasswordSupported);
    }

    /**
     * Builds a FileOptionsProfile instance from the provided HTTP request/response
     * @param device
     *              OXPd device instance
     * @param requestResponse
     *              HTTP request/response pair
     * @param tagHandler
     *              XML tag handler
     * @return
     *              FileOptionsProfile instance
     * @throws Error
     *              When errors are detected
     */
    @SuppressWarnings({"unchecked","ConstantConditions"})
    static FileOptionsProfile parseRequestResult(OXPdDevice device, HttpRequestResponseContainer requestResponse, RestXMLTagHandler tagHandler) throws Error {

        RestXMLTagHandler.XMLStartTagHandler listCreator = new RestXMLTagHandler.XMLStartTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, Attributes attributes) {
                if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__OCR_LANGUAGES, localName)) {
                    handler.setTagData(localName, new ArrayList<OcrLanguage>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__OCR_LANGUAGES, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                OcrLanguage ocrLanguage = OcrLanguage.fromAttributeValue(data);
                                if (ocrLanguage != null) {
                                    ((List<OcrLanguage>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__OCR_LANGUAGES)).add(ocrLanguage);
                                }
                            }
                        }
                    });
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__PDF_COMPRESSION_MODES, localName)) {
                    handler.setTagData(localName, new ArrayList<PdfCompressionMode>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__OCR_LANGUAGES, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                PdfCompressionMode pdfCompressionMode = PdfCompressionMode.fromAttributeValue(data);
                                if (pdfCompressionMode != null)  {
                                    ((List<PdfCompressionMode>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__PDF_COMPRESSION_MODES)).add(pdfCompressionMode);
                                }
                            }
                        }
                    });
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__TIFF_COMPRESSION_MODES, localName)) {
                    handler.setTagData(localName, new ArrayList<TiffCompressionMode>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__OCR_LANGUAGES, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                TiffCompressionMode tiffCompressionMode = TiffCompressionMode.fromAttributeValue(data);
                                if (tiffCompressionMode != null) {
                                    ((List<TiffCompressionMode>) handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__TIFF_COMPRESSION_MODES)).add(tiffCompressionMode);
                                }
                            }
                        }
                    });
                } else if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__XPS_COMPRESSION_MODES, localName)) {
                    handler.setTagData(localName, new ArrayList<XpsCompressionMode>());
                    handler.setGenericXMLHandler(null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__OCR_LANGUAGES, localName)) {
                                handler.setGenericXMLHandler(null, null);
                            } else {
                                XpsCompressionMode xpsCompressionMode = XpsCompressionMode.fromAttributeValue(data);
                                if (xpsCompressionMode != null) {
                                    ((List<XpsCompressionMode>)handler.getTagData(OXPdScan.Constants.XML_TAG__SCAN__XPS_COMPRESSION_MODES)).add(xpsCompressionMode);
                                }
                            }
                        }
                    });
                }
            }
        };


        RestXMLTagHandler.XMLEndTagHandler infoCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                if (TextUtils.equals(OXPdScan.Constants.XML_TAG__SCAN__IS_PDF_ENCRYPTION_PASSWORD_SUPPORTED, localName)) {
                    handler.setTagData(localName, data);
                }
            }
        };

        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__OCR_LANGUAGES, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__PDF_COMPRESSION_MODES, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__TIFF_COMPRESSION_MODES, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__XPS_COMPRESSION_MODES, listCreator, null);
        tagHandler.setXMLHandler(OXPdScan.Constants.XML_TAG__SCAN__IS_PDF_ENCRYPTION_PASSWORD_SUPPORTED, null, infoCollector);

        device.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);

        return new FileOptionsProfile(tagHandler);
    }


    //For SDK Simulator
    static FileOptionsProfile parseRequestResult(List<OcrLanguage> ocrLanguageList,
                                                 List<PdfCompressionMode> pdfCompressionModeList,
                                                 List<TiffCompressionMode> tiffCompressionModeList,
                                                 List<XpsCompressionMode> xpsCompressionModeList,
                                                 String isPdfEncryptionPasswordSupported)  throws Error {
        return new FileOptionsProfile(ocrLanguageList,
                pdfCompressionModeList,
                tiffCompressionModeList,
                xpsCompressionModeList,
                isPdfEncryptionPasswordSupported);
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
        dest.writeInt(this.isPdfEncryptionPasswordSupported ? 1 : 0);
        dest.writeInt(this.ocrLanguages.size());
        for(Enum enumEntry : this.ocrLanguages) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.pdfCompressionModes.size());
        for(Enum enumEntry : this.pdfCompressionModes) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.tiffCompressionModes.size());
        for(Enum enumEntry : this.tiffCompressionModes) {
            dest.writeInt(enumEntry.ordinal());
        }
        dest.writeInt(this.xpsCompressionModes.size());
        for(Enum enumEntry : this.xpsCompressionModes) {
            dest.writeInt(enumEntry.ordinal());
        }
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private FileOptionsProfile(Parcel in) {
        this.isPdfEncryptionPasswordSupported = in.readInt() > 0;
        List<OcrLanguage> ocrLanguages = new ArrayList<OcrLanguage>();
        for(int length = in.readInt();length > 0; length--) {
            ocrLanguages.add(OcrLanguage.values()[in.readInt()]);
        }
        this.ocrLanguages = Collections.unmodifiableList(ocrLanguages);
        List<PdfCompressionMode> pdfCompressionModes = new ArrayList<PdfCompressionMode>();
        for(int length = in.readInt();length > 0; length--) {
            pdfCompressionModes.add(PdfCompressionMode.values()[in.readInt()]);
        }
        this.pdfCompressionModes = Collections.unmodifiableList(pdfCompressionModes);
        List<TiffCompressionMode> tiffCompressionModes = new ArrayList<TiffCompressionMode>();
        for(int length = in.readInt();length > 0; length--) {
            tiffCompressionModes.add(TiffCompressionMode.values()[in.readInt()]);
        }
        this.tiffCompressionModes = Collections.unmodifiableList(tiffCompressionModes);
        List<XpsCompressionMode> xpsCompressionModes = new ArrayList<XpsCompressionMode>();
        for(int length = in.readInt();length > 0; length--) {
            xpsCompressionModes.add(XpsCompressionMode.values()[in.readInt()]);
        }
        this.xpsCompressionModes = Collections.unmodifiableList(xpsCompressionModes);
    }

    /**
     * FileOptionsProfile creator
     */
    public static final Parcelable.Creator<FileOptionsProfile> CREATOR =
            new Parcelable.Creator<FileOptionsProfile>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link FileOptionsProfile#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public FileOptionsProfile createFromParcel(Parcel in) {
                    return new FileOptionsProfile(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public FileOptionsProfile[] newArray(int size) {
                    return new FileOptionsProfile[size];
                }
            };
}
