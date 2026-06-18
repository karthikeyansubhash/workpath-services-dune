
package com.hp.ws.cdm.jobticket;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Usb {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("path")
    @Expose
    private String path;
    /**
     * Not used for scan to Usb and print from Usb case
     * 
     */
    @SerializedName("fileName")
    @Expose
    private String fileName;
    @SerializedName("fileFormat")
    @Expose
    private FileFormat fileFormat;

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public void setVersion(String version) {
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Not used for scan to Usb and print from Usb case
     * 
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Not used for scan to Usb and print from Usb case
     * 
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileFormat getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(FileFormat fileFormat) {
        this.fileFormat = fileFormat;
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
        private final static Map<String, FileFormat> CONSTANTS = new HashMap<String, FileFormat>();

        static {
            for (FileFormat c: values()) {
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

        public static FileFormat fromValue(String value) {
            FileFormat constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
