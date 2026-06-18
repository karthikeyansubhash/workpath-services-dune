
package com.hp.ws.cdm.jobticket;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Archive {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    /**
     * The fax archive destination can be email, the to email address is defined here. RFC5322 is followed.
     * 
     */
    @SerializedName("toAddress")
    @Expose
    private String toAddress;
    /**
     * The fax archive destination can be email, the from email address is defined here. In case it is not defined the to email address is used. RFC5322 is followed.
     * 
     */
    @SerializedName("fromAddress")
    @Expose
    private String fromAddress;
    /**
     * Resolution for send and archive job.
     * 
     */
    @SerializedName("resolution")
    @Expose
    private Fax.FaxResolution resolution;
    /**
     * FTP or network folder archive destination file name.
     * 
     */
    @SerializedName("archiveFileName")
    @Expose
    private String archiveFileName;
    /**
     * Format of file for fax archibe job can be mtiff, pdf or pdfa.
     * 
     */
    @SerializedName("fileFormat")
    @Expose
    private ArchiveFileFormat fileFormat;
    /**
     * The fax archive destination can be email, network folder or FTP server.
     * 
     */
    @SerializedName("destinationType")
    @Expose
    private FaxArchiveDestination destinationType;
    @SerializedName("folderDestination")
    @Expose
    private FolderDestination folderDestination;

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

    /**
     * The fax archive destination can be email, the to email address is defined here. RFC5322 is followed.
     * 
     */
    public String getToAddress() {
        return toAddress;
    }

    /**
     * The fax archive destination can be email, the to email address is defined here. RFC5322 is followed.
     * 
     */
    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    /**
     * The fax archive destination can be email, the from email address is defined here. In case it is not defined the to email address is used. RFC5322 is followed.
     * 
     */
    public String getFromAddress() {
        return fromAddress;
    }

    /**
     * The fax archive destination can be email, the from email address is defined here. In case it is not defined the to email address is used. RFC5322 is followed.
     * 
     */
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    /**
     * Resolution for send and archive job.
     * 
     */
    public Fax.FaxResolution getResolution() {
        return resolution;
    }

    /**
     * Resolution for send and archive job.
     * 
     */
    public void setResolution(Fax.FaxResolution resolution) {
        this.resolution = resolution;
    }

    /**
     * FTP or network folder archive destination file name.
     * 
     */
    public String getArchiveFileName() {
        return archiveFileName;
    }

    /**
     * FTP or network folder archive destination file name.
     * 
     */
    public void setArchiveFileName(String archiveFileName) {
        this.archiveFileName = archiveFileName;
    }

    /**
     * Format of file for fax archibe job can be mtiff, pdf or pdfa.
     * 
     */
    public ArchiveFileFormat getFileFormat() {
        return fileFormat;
    }

    /**
     * Format of file for fax archibe job can be mtiff, pdf or pdfa.
     * 
     */
    public void setFileFormat(ArchiveFileFormat fileFormat) {
        this.fileFormat = fileFormat;
    }

    /**
     * The fax archive destination can be email, network folder or FTP server.
     * 
     */
    public FaxArchiveDestination getDestinationType() {
        return destinationType;
    }

    /**
     * The fax archive destination can be email, network folder or FTP server.
     * 
     */
    public void setDestinationType(FaxArchiveDestination destinationType) {
        this.destinationType = destinationType;
    }

    public FolderDestination getFolderDestination() {
        return folderDestination;
    }

    public void setFolderDestination(FolderDestination folderDestination) {
        this.folderDestination = folderDestination;
    }


    /**
     * Format of file for fax archibe job can be mtiff, pdf or pdfa.
     * 
     */
    public enum ArchiveFileFormat {

        @SerializedName("mtiff")
        MTIFF("mtiff"),
        @SerializedName("pdf")
        PDF("pdf"),
        @SerializedName("pdfa")
        PDFA("pdfa");
        private final String value;
        private final static Map<String, ArchiveFileFormat> CONSTANTS = new HashMap<String, ArchiveFileFormat>();

        static {
            for (ArchiveFileFormat c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ArchiveFileFormat(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static ArchiveFileFormat fromValue(String value) {
            ArchiveFileFormat constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * The fax archive destination can be email, network folder or FTP server.
     * 
     */
    public enum FaxArchiveDestination {

        @SerializedName("email")
        EMAIL("email"),
        @SerializedName("folder")
        FOLDER("folder"),
        @SerializedName("ftp")
        FTP("ftp");
        private final String value;
        private final static Map<String, FaxArchiveDestination> CONSTANTS = new HashMap<String, FaxArchiveDestination>();

        static {
            for (FaxArchiveDestination c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        FaxArchiveDestination(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static FaxArchiveDestination fromValue(String value) {
            FaxArchiveDestination constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
