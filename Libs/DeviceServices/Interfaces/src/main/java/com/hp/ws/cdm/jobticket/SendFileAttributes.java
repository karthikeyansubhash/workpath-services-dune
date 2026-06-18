
package com.hp.ws.cdm.jobticket;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class SendFileAttributes {

    /**
     * This field allow user to select multiple macros (like DATE), which will be expanded as a prefix in the destination filename 
     * 
     */
    @SerializedName("prefix")
    @Expose
    private String prefix;
    /**
     * This field allow user to select multiple macros (like DATE), which will be expanded as a suffix in the destination filename 
     * 
     */
    @SerializedName("suffix")
    @Expose
    private String suffix;
    @SerializedName("fileName")
    @Expose
    private String fileName;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("fileNameIsEditable")
    @Expose
    private Property.FeatureEnabled fileNameIsEditable;
    /**
     * This field is readonly properties, it displays preview of filename based on prefix, suffix, fileName etc.
     * 
     */
    @SerializedName("fileNamePreview")
    @Expose
    private String fileNamePreview;
    @SerializedName("fileType")
    @Expose
    private Usb.FileFormat fileType;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("fileTypeIsEditable")
    @Expose
    private Property.FeatureEnabled fileTypeIsEditable;
    /**
     * For multiple attachment files x represents the attachment count and y is total attachments xy represents <fileName>_x-y, xx_of_yy means <fileName>_xx_of_yy and x_y means <fileName>_x_y
     * 
     */
    @SerializedName("numberingFormat")
    @Expose
    private NumberingFormat numberingFormat;
    @SerializedName("qualityAndFileSize")
    @Expose
    private QualityAndFileSize qualityAndFileSize;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("encryptPDF")
    @Expose
    private Property.FeatureEnabled encryptPDF;
    /**
     * string will be used as password to encrypt pdf
     * 
     */
    @SerializedName("encryptedPDFPassword")
    @Expose
    private String encryptedPDFPassword;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("compressPDF")
    @Expose
    private Property.FeatureEnabled compressPDF;
    @SerializedName("monoTiffCompression")
    @Expose
    private MonoTiffCompression monoTiffCompression;
    @SerializedName("colorGrayScaleTiffCompression")
    @Expose
    private ColorGrayScaleTiffCompression colorGrayScaleTiffCompression;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("multiFileEnabled")
    @Expose
    private Property.FeatureEnabled multiFileEnabled;
    /**
     * maximum images for file attachment. Applicable when multiFileMode is maxImagesPerFile
     * 
     */
    @SerializedName("maxImagesPerFile")
    @Expose
    private Integer maxImagesPerFile;
    @SerializedName("multiFileMode")
    @Expose
    private MultiFileMode multiFileMode;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("singleFileNumberingFormatEnabled")
    @Expose
    private Property.FeatureEnabled singleFileNumberingFormatEnabled;

    /**
     * This field allow user to select multiple macros (like DATE), which will be expanded as a prefix in the destination filename 
     * 
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * This field allow user to select multiple macros (like DATE), which will be expanded as a prefix in the destination filename 
     * 
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * This field allow user to select multiple macros (like DATE), which will be expanded as a suffix in the destination filename 
     * 
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * This field allow user to select multiple macros (like DATE), which will be expanded as a suffix in the destination filename 
     * 
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getFileNameIsEditable() {
        return fileNameIsEditable;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setFileNameIsEditable(Property.FeatureEnabled fileNameIsEditable) {
        this.fileNameIsEditable = fileNameIsEditable;
    }

    /**
     * This field is readonly properties, it displays preview of filename based on prefix, suffix, fileName etc.
     * 
     */
    public String getFileNamePreview() {
        return fileNamePreview;
    }

    /**
     * This field is readonly properties, it displays preview of filename based on prefix, suffix, fileName etc.
     * 
     */
    public void setFileNamePreview(String fileNamePreview) {
        this.fileNamePreview = fileNamePreview;
    }

    public Usb.FileFormat getFileType() {
        return fileType;
    }

    public void setFileType(Usb.FileFormat fileType) {
        this.fileType = fileType;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getFileTypeIsEditable() {
        return fileTypeIsEditable;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setFileTypeIsEditable(Property.FeatureEnabled fileTypeIsEditable) {
        this.fileTypeIsEditable = fileTypeIsEditable;
    }

    /**
     * For multiple attachment files x represents the attachment count and y is total attachments xy represents <fileName>_x-y, xx_of_yy means <fileName>_xx_of_yy and x_y means <fileName>_x_y
     * 
     */
    public NumberingFormat getNumberingFormat() {
        return numberingFormat;
    }

    /**
     * For multiple attachment files x represents the attachment count and y is total attachments xy represents <fileName>_x-y, xx_of_yy means <fileName>_xx_of_yy and x_y means <fileName>_x_y
     * 
     */
    public void setNumberingFormat(NumberingFormat numberingFormat) {
        this.numberingFormat = numberingFormat;
    }

    public QualityAndFileSize getQualityAndFileSize() {
        return qualityAndFileSize;
    }

    public void setQualityAndFileSize(QualityAndFileSize qualityAndFileSize) {
        this.qualityAndFileSize = qualityAndFileSize;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEncryptPDF() {
        return encryptPDF;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEncryptPDF(Property.FeatureEnabled encryptPDF) {
        this.encryptPDF = encryptPDF;
    }

    /**
     * string will be used as password to encrypt pdf
     * 
     */
    public String getEncryptedPDFPassword() {
        return encryptedPDFPassword;
    }

    /**
     * string will be used as password to encrypt pdf
     * 
     */
    public void setEncryptedPDFPassword(String encryptedPDFPassword) {
        this.encryptedPDFPassword = encryptedPDFPassword;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getCompressPDF() {
        return compressPDF;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setCompressPDF(Property.FeatureEnabled compressPDF) {
        this.compressPDF = compressPDF;
    }

    public MonoTiffCompression getMonoTiffCompression() {
        return monoTiffCompression;
    }

    public void setMonoTiffCompression(MonoTiffCompression monoTiffCompression) {
        this.monoTiffCompression = monoTiffCompression;
    }

    public ColorGrayScaleTiffCompression getColorGrayScaleTiffCompression() {
        return colorGrayScaleTiffCompression;
    }

    public void setColorGrayScaleTiffCompression(ColorGrayScaleTiffCompression colorGrayScaleTiffCompression) {
        this.colorGrayScaleTiffCompression = colorGrayScaleTiffCompression;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getMultiFileEnabled() {
        return multiFileEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setMultiFileEnabled(Property.FeatureEnabled multiFileEnabled) {
        this.multiFileEnabled = multiFileEnabled;
    }

    /**
     * maximum images for file attachment. Applicable when multiFileMode is maxImagesPerFile
     * 
     */
    public Integer getMaxImagesPerFile() {
        return maxImagesPerFile;
    }

    /**
     * maximum images for file attachment. Applicable when multiFileMode is maxImagesPerFile
     * 
     */
    public void setMaxImagesPerFile(Integer maxImagesPerFile) {
        this.maxImagesPerFile = maxImagesPerFile;
    }

    public MultiFileMode getMultiFileMode() {
        return multiFileMode;
    }

    public void setMultiFileMode(MultiFileMode multiFileMode) {
        this.multiFileMode = multiFileMode;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getSingleFileNumberingFormatEnabled() {
        return singleFileNumberingFormatEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setSingleFileNumberingFormatEnabled(Property.FeatureEnabled singleFileNumberingFormatEnabled) {
        this.singleFileNumberingFormatEnabled = singleFileNumberingFormatEnabled;
    }

    public enum ColorGrayScaleTiffCompression {

        @SerializedName("lzw")
        LZW("lzw"),
        @SerializedName("tiff6")
        TIFF_6("tiff6"),
        @SerializedName("postTiff6")
        POST_TIFF_6("postTiff6");
        private final String value;
        private final static Map<String, ColorGrayScaleTiffCompression> CONSTANTS = new HashMap<String, ColorGrayScaleTiffCompression>();

        static {
            for (ColorGrayScaleTiffCompression c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ColorGrayScaleTiffCompression(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static ColorGrayScaleTiffCompression fromValue(String value) {
            ColorGrayScaleTiffCompression constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum MonoTiffCompression {

        @SerializedName("automatic")
        AUTOMATIC("automatic"),
        @SerializedName("g3")
        G_3("g3"),
        @SerializedName("g4")
        G_4("g4"),
        @SerializedName("lzw")
        LZW("lzw");
        private final String value;
        private final static Map<String, MonoTiffCompression> CONSTANTS = new HashMap<String, MonoTiffCompression>();

        static {
            for (MonoTiffCompression c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        MonoTiffCompression(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static MonoTiffCompression fromValue(String value) {
            MonoTiffCompression constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum MultiFileMode {

        @SerializedName("maxImagesPerFile")
        MAX_IMAGES_PER_FILE("maxImagesPerFile");
        private final String value;
        private final static Map<String, MultiFileMode> CONSTANTS = new HashMap<String, MultiFileMode>();

        static {
            for (MultiFileMode c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        MultiFileMode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static MultiFileMode fromValue(String value) {
            MultiFileMode constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * For multiple attachment files x represents the attachment count and y is total attachments xy represents <fileName>_x-y, xx_of_yy means <fileName>_xx_of_yy and x_y means <fileName>_x_y
     * 
     */
    public enum NumberingFormat {

        @SerializedName("none")
        NONE("none"),
        @SerializedName("xy")
        XY("xy"),
        @SerializedName("xx_of_yy")
        XX_OF_YY("xx_of_yy"),
        @SerializedName("x_y")
        X_Y("x_y");
        private final String value;
        private final static Map<String, NumberingFormat> CONSTANTS = new HashMap<String, NumberingFormat>();

        static {
            for (NumberingFormat c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        NumberingFormat(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static NumberingFormat fromValue(String value) {
            NumberingFormat constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum QualityAndFileSize {

        @SerializedName("high")
        HIGH("high"),
        @SerializedName("low")
        LOW("low"),
        @SerializedName("medium")
        MEDIUM("medium"),
        @SerializedName("highest")
        HIGHEST("highest"),
        @SerializedName("lowest")
        LOWEST("lowest");
        private final String value;
        private final static Map<String, QualityAndFileSize> CONSTANTS = new HashMap<String, QualityAndFileSize>();

        static {
            for (QualityAndFileSize c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        QualityAndFileSize(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static QualityAndFileSize fromValue(String value) {
            QualityAndFileSize constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
