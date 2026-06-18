// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.text.TextUtils;

/** Enumeration of scan file types */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum FileType {
    /** Each image (side of a sheet) is provided as a separate JPEG file (Content-Type: image/jpeg). */
    Jpeg("Jpeg"),
    /** Multiple TIFF images in a single TIFF file (Content-Type: image/tiff). */
    Mtiff("Mtiff"),
    /** Comma separated value (CSV) file resulting from OCR (Content-Type: text/csv). Values for graphics-related options may be ignored for text only file types but must always be set to supported values. */
    OcrCsv("OcrCsv"),
    /** Hypertext markup language (HTML) file resulting from OCR (web.htm), with accompanying JPEG images (images\img*.jpg), bundled into a zip file (Content-Type: text/html). */
    OcrHtml("OcrHtml"),
    /** Searchable PDF with text plane under the image plane (Content-Type: application/pdf). */
    OcrPdfTextUnderImage("OcrPdfTextUnderImage"),
    /** Searchable PDF/A with text plane under the image plane (Content-Type: application/pdf). */
    OcrPdfATextUnderImage("OcrPdfATextUnderImage"),
    /** Rich Text Format (RTF) file resulting from OCR with text and images (Content-Type: text/richtext). */
    OcrRtf("OcrRtf"),
    /** Plain text file resulting from OCR (Content-Type: text/plain). Values for graphics-related options may be ignored for text only file types, but must always be set to supported values. */
    OcrText("OcrText"),
    /** Unicode plain text file (UTF-16 encoded, with a Byte Order Mark) resulting from OCR (Content-Type: text/plain) Values for graphics-related options may be ignored for text only file types, but must always be set to supported values. */
    OcrUnicodeText("OcrUnicodeText"),
    /** WordML (Word 2003 XML) file resulting from OCR with text and images (Content-Type: text/xml). */
    OcrXml("OcrXml"),
    /** XML Paper Specification (XPS) file resulting from OCR with text plane under the image plane (Content-Type: application/vnd.ms-xpsdocument). */
    OcrXpsTextUnderImage("OcrXpsTextUnderImage"),
    /** PDF with image plane only (Content-Type: application/pdf). */
    Pdf("Pdf"),
    /** PDF/A with image plane only (Content-Type: application/pdf). */
    PdfA("PdfA"),
    /** Each image (side of a sheet) is provided as a separate TIFF file (Content-Type: image/tiff). */
    Tiff("Tiff"),
    /** XML Paper Specification (XPS) file with image plane only (Content-Type: application/vnd.ms-xpsdocument). */
    Xps("Xps");

    /**
     * SOAP value associated with enum
     */
    final String mValue;

    /**
     * FileType constructor
     * @param value
     *              SOAP value associated with enum
     */
    FileType(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to FileType
     * @param value
     *              SOAP value string
     * @return
     *              Matching FileType enum or null if no match is found
     */
    static FileType fromAttributeValue(String value) {
        for(FileType enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
