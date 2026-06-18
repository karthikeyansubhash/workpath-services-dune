// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.scanner;

import androidx.annotation.NonNull;
import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.CapabilitiesExceededException;

/**
 * The file options that have a dependency on file type.
 *
 * @since API 1
 */
public final class FileOptionsAttributes implements Parcelable {
    /**
     * Enumeration for optimizing the language of the OCR engine
     *
     * @since API 1
     */
    public enum OcrLanguage {
        /**
         * Optimize the OCR engine with device default
         * @since API 1
         */
        DEFAULT,

        /**
         * Optimize the OCR engine for Arabic
         * @since API 1
         */
        ARABIC,

        /**
         * Optimize the OCR engine for Catalan
         * @since API 1
         */
        CATALAN,

        /**
         * Optimize the OCR engine for Simplified Chinese
         * @since API 1
         */
        CHINESE_SIMPLIFIED,

        /**
         * Optimize the OCR engine for Traditional Chinese
         * @since API 1
         */
        CHINESE_TRADITIONAL,

        /**
         * Optimize the OCR engine for Croatian
         * @since API 1
         */
        CROATIAN,

        /**
         * Optimize the OCR engine for Czech
         * @since API 1
         */
        CZECH,

        /**
         * Optimize the OCR engine for Danish
         * @since API 1
         */
        DANISH,

        /**
         * Optimize the OCR engine for Dutch
         * @since API 1
         */
        DUTCH,

        /**
         * Optimize the OCR engine for English
         * @since API 1
         */
        ENGLISH,

        /**
         * Optimize the OCR engine for Finish
         * @since API 1
         */
        FINNISH,

        /**
         * Optimize the OCR engine for French
         * @since API 1
         */
        FRENCH,

        /**
         * Optimize the OCR engine for German
         * @since API 1
         */
        GERMAN,

        /**
         * Optimize the OCR engine for Greek
         * @since API 1
         */
        GREEK,

        /**
         * Optimize the OCR engine for Hebrew
         * @since API 1
         */
        HEBREW,

        /**
         * Optimize the OCR engine for Hungarian
         * @since API 1
         */
        HUNGARIAN,

        /**
         * Optimize the OCR engine for Indonesian
         * @since API 1
         */
        INDONESIAN,

        /**
         * Optimize the OCR engine for Italian
         * @since API 1
         */
        ITALIAN,

        /**
         * Optimize the OCR engine for Japanese
         * @since API 1
         */
        JAPANESE,

        /**
         * Optimize the OCR engine for Korean
         * @since API 1
         */
        KOREAN,

        /**
         * Optimize the OCR engine for Norwegian
         * @since API 1
         */
        NORWEGIAN,

        /**
         * Optimize the OCR engine for Polish
         * @since API 1
         */
        POLISH,

        /**
         * Optimize the OCR engine for Portuguese
         * @since API 1
         */
        PORTUGUESE,

        /**
         * Optimize the OCR engine for Romanian
         * @since API 1
         */
        ROMANIAN,

        /**
         * Optimize the OCR engine for Russian
         * @since API 1
         */
        RUSSIAN,

        /**
         * Optimize the OCR engine for Slovak
         * @since API 1
         */
        SLOVAK,

        /**
         * Optimize the OCR engine for Slovenian
         * @since API 1
         */
        SLOVENIAN,

        /**
         * Optimize the OCR engine for Spanish
         * @since API 1
         */
        SPANISH,

        /**
         * Optimize the OCR engine for Swedish
         * @since API 1
         */
        SWEDISH,

        /**
         * Optimize the OCR engine for Turkish
         * @since API 1
         */
        TURKISH
    }

    /**
     * Enumeration of compression mode
     *
     * @since API 1
     */
    public enum PdfCompressionMode {
        /**
         * Default compression
         * @since API 1
         */
        DEFAULT,

        /**
         * Normal compression
         * @since API 1
         */
        NORMAL,

        /**
         * The device will attempt to use adaptive/selective methods to produce the smallest possible output.
         * @since API 1
         */
        HIGH
    }

    /**
     * Enumeration of compression mode
     *
     * @since API 1
     */
    public enum TiffCompressionMode {
        /**
         * Default compression mode
         * @since API 1
         */
        DEFAULT,

        /**
         * CCITT Group 3 (may only be used with Black color mode).
         * @since API 1
         */
        G_3,

        /**
         * CCITT Group 3 (may only be used with Black color mode).
         * @since API 1
         */
        G_4,

        /**
         * JPEG TIFF Version 6.0 (may not be used with Black color mode).
         * @since API 1
         */
        JPEG_TIFF_6,

        /**
         * JPEG TIFF Technical Note 2 (may not be used with Black color mode).
         * @since API 1
         */
        JPEG_TTN_2,

        /**
         * Lempel Ziv Welch
         * @since API 1
         */
        LZW,

        /**
         * The device will use adaptive methods to produce the smallest possible output (may only be used with Black color mode).
         * @since API 1
         */
        HIGH
    }

    /**
     * Enumeration of compression mode
     *
     * @since API 1
     */
    public enum XpsCompressionMode {
        /**
         * Default compression mode
         * @since API 1
         */
        DEFAULT,

        /**
         * Normal compression
         * @since API 1
         */
        NORMAL,

        /**
         * The device will attempt to use adaptive/selective methods to produce the smallest possible output.
         * @since API 1
         */
        HIGH
    }

    final String mPdfEncryptionPassword;
    final OcrLanguage mOcrLanguage;
    final PdfCompressionMode mPdfCompressionMode;
    final TiffCompressionMode mTiffCompressionMode;
    final XpsCompressionMode mXpsCompressionMode;

    private FileOptionsAttributes(Builder builder) {
        mPdfEncryptionPassword = builder.mPdfEncryptionPassword;
        mOcrLanguage = builder.mOcrLanguage;
        mPdfCompressionMode = builder.mPdfCompressionMode;
        mTiffCompressionMode = builder.mTiffCompressionMode;
        mXpsCompressionMode = builder.mXpsCompressionMode;
    }

    private FileOptionsAttributes(Parcel in) {
        mPdfEncryptionPassword = in.readString();
        mOcrLanguage = (OcrLanguage) in.readSerializable();
        mPdfCompressionMode = (PdfCompressionMode) in.readSerializable();
        mTiffCompressionMode = (TiffCompressionMode) in.readSerializable();
        mXpsCompressionMode = (XpsCompressionMode) in.readSerializable();
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPdfEncryptionPassword);
        dest.writeSerializable(mOcrLanguage);
        dest.writeSerializable(mPdfCompressionMode);
        dest.writeSerializable(mTiffCompressionMode);
        dest.writeSerializable(mXpsCompressionMode);
    }

    /**
     * @hide parcelable implementation
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @hide parcelable implementation
     */
    public static final Creator<FileOptionsAttributes> CREATOR = new Creator<FileOptionsAttributes>() {
        @Override
        public FileOptionsAttributes createFromParcel(Parcel in) {
            return new FileOptionsAttributes(in);
        }

        @Override
        public FileOptionsAttributes[] newArray(int size) {
            return new FileOptionsAttributes[size];
        }
    };

    /**
     * Builder for creating {@link FileOptionsAttributes} containing file options parameters.
     *
     * @since API 1
     */
    public static class Builder {
        String mPdfEncryptionPassword;
        OcrLanguage mOcrLanguage = OcrLanguage.DEFAULT;
        PdfCompressionMode mPdfCompressionMode = PdfCompressionMode.DEFAULT;
        TiffCompressionMode mTiffCompressionMode = TiffCompressionMode.DEFAULT;
        XpsCompressionMode mXpsCompressionMode = XpsCompressionMode.DEFAULT;

        /**
         * Default constructor to create a new Builder with default attributes:
         * <ul>
         *     <li>PDF encryption password = null</li>
         *     <li>OCR language = DEFAULT</li>
         *     <li>PDF compression mode = DEFAULT</li>
         *     <li>TIFF compression mode = DEFAULT</li>
         *     <li>XPS compression mode = DEFAULT</li>
         * </ul>
         *
         * @since API 1
         */
        public Builder() {
        }

        /**
         * Sets the value of the pdfEncryptionPassword property.
         *
         * @param pdfEncryptionPassword password
         * @return this builder for method chaining
         * @throws NullPointerException if pdfEncryptionPassword is null
         * @since API 1
         */
        @SuppressWarnings("unused")
        @NonNull
        public Builder setPdfEncryptionPassword(String pdfEncryptionPassword) {
            mPdfEncryptionPassword = pdfEncryptionPassword;
            return this;
        }

        /**
         * Sets the value of the ocrLanguage property.
         *
         * @param ocrLanguage language
         * @return this builder for method chaining
         * @throws NullPointerException if ocrLanguage is null
         * @since API 1
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public Builder setOcrLanguage(OcrLanguage ocrLanguage) {
            mOcrLanguage = Preconditions.checkNotNull(ocrLanguage);
            return this;
        }

        /**
         * Sets the value of the pdfCompressionMode property.
         *
         * @param pdfCompressionMode mode
         * @return this builder for method chaining
         * @throws NullPointerException if pdfCompressionMode is null
         * @since API 1
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public Builder setPdfCompressionMode(PdfCompressionMode pdfCompressionMode) {
            mPdfCompressionMode = Preconditions.checkNotNull(pdfCompressionMode);
            return this;
        }

        /**
         * Sets the value of the tiffCompressionMode property.
         *
         * @param tiffCompressionMode mode
         * @return this builder for method chaining
         * @throws NullPointerException if tiffCompressionMode is null
         * @since API 1
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public Builder setTiffCompressionMode(TiffCompressionMode tiffCompressionMode) {
            mTiffCompressionMode = Preconditions.checkNotNull(tiffCompressionMode);
            return this;
        }

        /**
         * Sets the value of the xpsCompressionMode property.
         *
         * @param xpsCompressionMode mode
         * @return this builder for method chaining
         * @throws NullPointerException if xpsCompressionMode is null
         * @since API 1
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public Builder setXpsCompressionMode(XpsCompressionMode xpsCompressionMode) {
            mXpsCompressionMode = Preconditions.checkNotNull(xpsCompressionMode);
            return this;
        }

        /**
         * Combines all of the attributes into a {@link FileOptionsAttributes} object.
         *
         * @return FileOptionsAttributes object containing all of the attributes.
         * @throws CapabilitiesExceededException if caps does not contain the set attribute value.
         * @since API 1
         */
        @SuppressWarnings("unused")
        @SuppressLint("RestrictedApi")
        @NonNull
        public FileOptionsAttributes build(@NonNull final FileOptionsAttributesCaps caps) throws CapabilitiesExceededException {
            if (caps == null) {
                throw new CapabilitiesExceededException("FileOptionsAttributesCapabilities is required");
            }
            Preconditions.checkNotNull(caps.mCapsCreator);

            if (!caps.getPdfCompressionModeList()
                    .contains(mPdfCompressionMode)) {
                throw new CapabilitiesExceededException("Supplied PDF compression mode not supported");
            }

            if (!caps.getOcrLanguageList()
                    .contains(mOcrLanguage)) {
                throw new CapabilitiesExceededException("Supplied OCR language not supported");
            }

            if (!caps.getTiffCompressionModeList()
                    .contains(mTiffCompressionMode)) {
                throw new CapabilitiesExceededException("Supplied TIFF compression mode not supported");
            }

            if (!caps.getXpsCompressionModeList()
                    .contains(mXpsCompressionMode)) {
                throw new CapabilitiesExceededException("Supplied XPS compression mode not supported");
            }

            return new FileOptionsAttributes(this);
        }
    }

    /**
     * @hide This is hidden because it should be understood without documenting in the javadoc
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("OcrLanguage=").append(((mOcrLanguage != null)?mOcrLanguage.name().toString():"null")).append("]").toString();
    }
}
