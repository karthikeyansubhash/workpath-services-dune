
package com.hp.ws.cdm.jobmanagement;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * provides the scan job specific details
 * 
 */
public class ScanInfo {

    @SerializedName("colorMode")
    @Expose
    private com.hp.ws.cdm.jobmanagement.PrintSettings.ColorModes colorMode;
    @SerializedName("duplexFormat")
    @Expose
    private ScanInfo.DuplexBinding duplexFormat;
    @SerializedName("fileType")
    @Expose
    private ScanInfo.FileFormat fileType;
    /**
     * related to the physical orientation of the media on the tray
     * 
     */
    @SerializedName("mediaOrientation")
    @Expose
    private ScanInfo.MediaOrientation mediaOrientation;
    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    @SerializedName("mediaSize")
    @Expose
    private com.hp.ws.cdm.jobmanagement.MediaInput.MediaSize mediaSize;
    @SerializedName("plexMode")
    @Expose
    private com.hp.ws.cdm.jobmanagement.PrintSettings.PlexMode plexMode;
    /**
     * Scan media source values. This values will be use in the jobTicket scan definition
     * 
     */
    @SerializedName("scanMediaSourceId")
    @Expose
    private ScanInfo.ScanMediaSourceId scanMediaSourceId;
    /**
     * length in inches for the scanned media
     * 
     */
    @SerializedName("scannedLength")
    @Expose
    private Integer scannedLength;
    /**
     * total number of pages scanned in the job, where-in page is one side of a sheet. ex: if 2 sides of a sheet are scanned, page count is 2
     * 
     */
    @SerializedName("scannedPageCount")
    @Expose
    private Integer scannedPageCount;
    /**
     * width in inches for the scanned media
     * 
     */
    @SerializedName("scannedWidth")
    @Expose
    private Integer scannedWidth;
    @SerializedName("xResolution")
    @Expose
    private com.hp.ws.cdm.jobmanagement.PrintSettings.Resolutions xResolution;
    @SerializedName("yResolution")
    @Expose
    private com.hp.ws.cdm.jobmanagement.PrintSettings.Resolutions yResolution;
    @SerializedName("scanAreaUsage")
    @Expose
    private ScanAreaUsage scanAreaUsage;

    public com.hp.ws.cdm.jobmanagement.PrintSettings.ColorModes getColorMode() {
        return colorMode;
    }

    public void setColorMode(com.hp.ws.cdm.jobmanagement.PrintSettings.ColorModes colorMode) {
        this.colorMode = colorMode;
    }

    public ScanInfo.DuplexBinding getDuplexFormat() {
        return duplexFormat;
    }

    public void setDuplexFormat(ScanInfo.DuplexBinding duplexFormat) {
        this.duplexFormat = duplexFormat;
    }

    public ScanInfo.FileFormat getFileType() {
        return fileType;
    }

    public void setFileType(ScanInfo.FileFormat fileType) {
        this.fileType = fileType;
    }

    /**
     * related to the physical orientation of the media on the tray
     * 
     */
    public ScanInfo.MediaOrientation getMediaOrientation() {
        return mediaOrientation;
    }

    /**
     * related to the physical orientation of the media on the tray
     * 
     */
    public void setMediaOrientation(ScanInfo.MediaOrientation mediaOrientation) {
        this.mediaOrientation = mediaOrientation;
    }

    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    public com.hp.ws.cdm.jobmanagement.MediaInput.MediaSize getMediaSize() {
        return mediaSize;
    }

    /**
     * Media size, values according to the standard PWG5101.1 or HP extension pattern for exceptions
     * 
     */
    public void setMediaSize(com.hp.ws.cdm.jobmanagement.MediaInput.MediaSize mediaSize) {
        this.mediaSize = mediaSize;
    }

    public com.hp.ws.cdm.jobmanagement.PrintSettings.PlexMode getPlexMode() {
        return plexMode;
    }

    public void setPlexMode(com.hp.ws.cdm.jobmanagement.PrintSettings.PlexMode plexMode) {
        this.plexMode = plexMode;
    }

    /**
     * Scan media source values. This values will be use in the jobTicket scan definition
     * 
     */
    public ScanInfo.ScanMediaSourceId getScanMediaSourceId() {
        return scanMediaSourceId;
    }

    /**
     * Scan media source values. This values will be use in the jobTicket scan definition
     * 
     */
    public void setScanMediaSourceId(ScanInfo.ScanMediaSourceId scanMediaSourceId) {
        this.scanMediaSourceId = scanMediaSourceId;
    }

    /**
     * length in inches for the scanned media
     * 
     */
    public Integer getScannedLength() {
        return scannedLength;
    }

    /**
     * length in inches for the scanned media
     * 
     */
    public void setScannedLength(Integer scannedLength) {
        this.scannedLength = scannedLength;
    }

    /**
     * total number of pages scanned in the job, where-in page is one side of a sheet. ex: if 2 sides of a sheet are scanned, page count is 2
     * 
     */
    public Integer getScannedPageCount() {
        return scannedPageCount;
    }

    /**
     * total number of pages scanned in the job, where-in page is one side of a sheet. ex: if 2 sides of a sheet are scanned, page count is 2
     * 
     */
    public void setScannedPageCount(Integer scannedPageCount) {
        this.scannedPageCount = scannedPageCount;
    }

    /**
     * width in inches for the scanned media
     * 
     */
    public Integer getScannedWidth() {
        return scannedWidth;
    }

    /**
     * width in inches for the scanned media
     * 
     */
    public void setScannedWidth(Integer scannedWidth) {
        this.scannedWidth = scannedWidth;
    }

    public com.hp.ws.cdm.jobmanagement.PrintSettings.Resolutions getxResolution() {
        return xResolution;
    }

    public void setxResolution(com.hp.ws.cdm.jobmanagement.PrintSettings.Resolutions xResolution) {
        this.xResolution = xResolution;
    }

    public com.hp.ws.cdm.jobmanagement.PrintSettings.Resolutions getyResolution() {
        return yResolution;
    }

    public void setyResolution(com.hp.ws.cdm.jobmanagement.PrintSettings.Resolutions yResolution) {
        this.yResolution = yResolution;
    }

    public ScanAreaUsage getScanAreaUsage() {
        return scanAreaUsage;
    }

    public void setScanAreaUsage(ScanAreaUsage scanAreaUsage) {
        this.scanAreaUsage = scanAreaUsage;
    }

    public enum DuplexBinding {

        @SerializedName("oneSided")
        ONE_SIDED("oneSided"),
        @SerializedName("twoSidedShortEdge")
        TWO_SIDED_SHORT_EDGE("twoSidedShortEdge"),
        @SerializedName("twoSidedLongEdge")
        TWO_SIDED_LONG_EDGE("twoSidedLongEdge");
        private final String value;
        private final static Map<String, ScanInfo.DuplexBinding> CONSTANTS = new HashMap<String, ScanInfo.DuplexBinding>();

        static {
            for (ScanInfo.DuplexBinding c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        DuplexBinding(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static ScanInfo.DuplexBinding fromValue(String value) {
            ScanInfo.DuplexBinding constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum FileFormat {

        @SerializedName("pdf")
        PDF("pdf"),
        @SerializedName("tiff")
        TIFF("tiff"),
        @SerializedName("mtiff")
        MTIFF("mtiff"),
        @SerializedName("jpeg")
        JPEG("jpeg"),
        @SerializedName("pdfa")
        PDFA("pdfa"),
        @SerializedName("ppm")
        PPM("ppm"),
        @SerializedName("pgm")
        PGM("pgm"),
        @SerializedName("png")
        PNG("png"),
        @SerializedName("raw")
        RAW("raw"),
        @SerializedName("octetStream")
        OCTET_STREAM("octetStream"),
        @SerializedName("ppt")
        PPT("ppt"),
        @SerializedName("doc")
        DOC("doc"),
        @SerializedName("postScript")
        POST_SCRIPT("postScript"),
        @SerializedName("hpgl2")
        HPGL_2("hpgl2"),
        @SerializedName("pcl3gui")
        PCL_3_GUI("pcl3gui"),
        @SerializedName("bmp")
        BMP("bmp"),
        @SerializedName("nativeOffice")
        NATIVE_OFFICE("nativeOffice"),
        @SerializedName("pcl")
        PCL("pcl"),
        @SerializedName("pclxl")
        PCLXL("pclxl"),
        @SerializedName("pclm")
        PCLM("pclm"),
        @SerializedName("pwgRaster")
        PWG_RASTER("pwgRaster"),
        @SerializedName("rasterStream")
        RASTER_STREAM("rasterStream"),
        @SerializedName("urf")
        URF("urf"),
        @SerializedName("cals")
        CALS("cals"),
        @SerializedName("drp")
        DRP("drp"),
        @SerializedName("auto")
        AUTO("auto"),
        @SerializedName("prn")
        PRN("prn"),
        @SerializedName("txt")
        TXT("txt"),
        @SerializedName("hprip")
        HPRIP("hprip");
        private final String value;
        private final static Map<String, ScanInfo.FileFormat> CONSTANTS = new HashMap<String, ScanInfo.FileFormat>();

        static {
            for (ScanInfo.FileFormat c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        FileFormat(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static ScanInfo.FileFormat fromValue(String value) {
            ScanInfo.FileFormat constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * related to the physical orientation of the media on the tray
     * 
     */
    public enum MediaOrientation {

        @SerializedName("landscape")
        LANDSCAPE("landscape"),
        @SerializedName("portrait")
        PORTRAIT("portrait");
        private final String value;
        private final static Map<String, ScanInfo.MediaOrientation> CONSTANTS = new HashMap<String, ScanInfo.MediaOrientation>();

        static {
            for (ScanInfo.MediaOrientation c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        MediaOrientation(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static ScanInfo.MediaOrientation fromValue(String value) {
            ScanInfo.MediaOrientation constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Scan media source values. This values will be use in the jobTicket scan definition
     * 
     */
    public enum ScanMediaSourceId {

        @SerializedName("adf")
        ADF("adf"),
        @SerializedName("flatbed")
        FLATBED("flatbed"),
        @SerializedName("mdf")
        MDF("mdf");
        private final String value;
        private final static Map<String, ScanInfo.ScanMediaSourceId> CONSTANTS = new HashMap<String, ScanInfo.ScanMediaSourceId>();

        static {
            for (ScanInfo.ScanMediaSourceId c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ScanMediaSourceId(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static ScanInfo.ScanMediaSourceId fromValue(String value) {
            ScanInfo.ScanMediaSourceId constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
