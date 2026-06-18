
package com.hp.ws.cdm.jobmanagement;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * various media properties that describe the media requested vs. used
 * 
 */
public class MediaRequested {

    /**
     * Identifier of the specific media type
     * 
     */
    @SerializedName("mediaId")
    @Expose
    private String mediaId;
    /**
     * Media Name used in this job.
     * 
     */
    @SerializedName("mediaName")
    @Expose
    private String mediaName;
    /**
     * requested media width - units are mm
     * 
     */
    @SerializedName("mediaWidth")
    @Expose
    private Integer mediaWidth;
    /**
     * requested media length - units are mm
     * 
     */
    @SerializedName("mediaLength")
    @Expose
    private Integer mediaLength;
    /**
     * Media profiles are categorized by families. UI groups media profiles by family
     * 
     */
    @SerializedName("mediaFamily")
    @Expose
    private MediaRequested.MediaFamily mediaFamily;
    /**
     * Media profiles are categorized among these classes for PPU (pay per use) kind of solutions
     * 
     */
    @SerializedName("mediaCategoryClass")
    @Expose
    private MediaRequested.MediaCategoryClass mediaCategoryClass;
    @SerializedName("mediaInput")
    @Expose
    private MediaInput mediaInput;
    /**
     * identifier of the predefined media type used to create the media type (for custom media types)
     * 
     */
    @SerializedName("parentMediaId")
    @Expose
    private String parentMediaId;
    /**
     * Parent Media Name used in this job.
     * 
     */
    @SerializedName("parentMediaName")
    @Expose
    private String parentMediaName;
    /**
     * Media profiles are categorized by families. UI groups media profiles by family
     * 
     */
    @SerializedName("parentMediaFamily")
    @Expose
    private MediaRequested.MediaFamily parentMediaFamily;
    /**
     * Media profiles are categorized among these classes for PPU (pay per use) kind of solutions
     * 
     */
    @SerializedName("parentMediaCategoryClass")
    @Expose
    private MediaRequested.MediaCategoryClass parentMediaCategoryClass;

    /**
     * Identifier of the specific media type
     * 
     */
    public String getMediaId() {
        return mediaId;
    }

    /**
     * Identifier of the specific media type
     * 
     */
    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    /**
     * Media Name used in this job.
     * 
     */
    public String getMediaName() {
        return mediaName;
    }

    /**
     * Media Name used in this job.
     * 
     */
    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    /**
     * requested media width - units are mm
     * 
     */
    public Integer getMediaWidth() {
        return mediaWidth;
    }

    /**
     * requested media width - units are mm
     * 
     */
    public void setMediaWidth(Integer mediaWidth) {
        this.mediaWidth = mediaWidth;
    }

    /**
     * requested media length - units are mm
     * 
     */
    public Integer getMediaLength() {
        return mediaLength;
    }

    /**
     * requested media length - units are mm
     * 
     */
    public void setMediaLength(Integer mediaLength) {
        this.mediaLength = mediaLength;
    }

    /**
     * Media profiles are categorized by families. UI groups media profiles by family
     * 
     */
    public MediaRequested.MediaFamily getMediaFamily() {
        return mediaFamily;
    }

    /**
     * Media profiles are categorized by families. UI groups media profiles by family
     * 
     */
    public void setMediaFamily(MediaRequested.MediaFamily mediaFamily) {
        this.mediaFamily = mediaFamily;
    }

    /**
     * Media profiles are categorized among these classes for PPU (pay per use) kind of solutions
     * 
     */
    public MediaRequested.MediaCategoryClass getMediaCategoryClass() {
        return mediaCategoryClass;
    }

    /**
     * Media profiles are categorized among these classes for PPU (pay per use) kind of solutions
     * 
     */
    public void setMediaCategoryClass(MediaRequested.MediaCategoryClass mediaCategoryClass) {
        this.mediaCategoryClass = mediaCategoryClass;
    }

    public MediaInput getMediaInput() {
        return mediaInput;
    }

    public void setMediaInput(MediaInput mediaInput) {
        this.mediaInput = mediaInput;
    }

    /**
     * identifier of the predefined media type used to create the media type (for custom media types)
     * 
     */
    public String getParentMediaId() {
        return parentMediaId;
    }

    /**
     * identifier of the predefined media type used to create the media type (for custom media types)
     * 
     */
    public void setParentMediaId(String parentMediaId) {
        this.parentMediaId = parentMediaId;
    }

    /**
     * Parent Media Name used in this job.
     * 
     */
    public String getParentMediaName() {
        return parentMediaName;
    }

    /**
     * Parent Media Name used in this job.
     * 
     */
    public void setParentMediaName(String parentMediaName) {
        this.parentMediaName = parentMediaName;
    }

    /**
     * Media profiles are categorized by families. UI groups media profiles by family
     * 
     */
    public MediaRequested.MediaFamily getParentMediaFamily() {
        return parentMediaFamily;
    }

    /**
     * Media profiles are categorized by families. UI groups media profiles by family
     * 
     */
    public void setParentMediaFamily(MediaRequested.MediaFamily parentMediaFamily) {
        this.parentMediaFamily = parentMediaFamily;
    }

    /**
     * Media profiles are categorized among these classes for PPU (pay per use) kind of solutions
     * 
     */
    public MediaRequested.MediaCategoryClass getParentMediaCategoryClass() {
        return parentMediaCategoryClass;
    }

    /**
     * Media profiles are categorized among these classes for PPU (pay per use) kind of solutions
     * 
     */
    public void setParentMediaCategoryClass(MediaRequested.MediaCategoryClass parentMediaCategoryClass) {
        this.parentMediaCategoryClass = parentMediaCategoryClass;
    }


    /**
     * Media profiles are categorized among these classes for PPU (pay per use) kind of solutions
     * 
     */
    public enum MediaCategoryClass {

        @SerializedName("general")
        GENERAL("general"),
        @SerializedName("special")
        SPECIAL("special"),
        @SerializedName("blueprint")
        BLUEPRINT("blueprint"),
        @SerializedName("rigid")
        RIGID("rigid"),
        @SerializedName("unknown")
        UNKNOWN("unknown");
        private final String value;
        private final static Map<String, MediaRequested.MediaCategoryClass> CONSTANTS = new HashMap<String, MediaRequested.MediaCategoryClass>();

        static {
            for (MediaRequested.MediaCategoryClass c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        MediaCategoryClass(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static MediaRequested.MediaCategoryClass fromValue(String value) {
            MediaRequested.MediaCategoryClass constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Media profiles are categorized by families. UI groups media profiles by family
     * 
     */
    public enum MediaFamily {

        @SerializedName("adhesive")
        ADHESIVE("adhesive"),
        @SerializedName("backlit")
        BACKLIT("backlit"),
        @SerializedName("bannerAndSign")
        BANNER_AND_SIGN("bannerAndSign"),
        @SerializedName("bondAndCoated")
        BOND_AND_COATED("bondAndCoated"),
        @SerializedName("blueprint")
        BLUEPRINT("blueprint"),
        @SerializedName("canvas")
        CANVAS("canvas"),
        @SerializedName("custom")
        CUSTOM("custom"),
        @SerializedName("film")
        FILM("film"),
        @SerializedName("heatTransfer")
        HEAT_TRANSFER("heatTransfer"),
        @SerializedName("plain")
        PLAIN("plain"),
        @SerializedName("photo")
        PHOTO("photo"),
        @SerializedName("technical")
        TECHNICAL("technical"),
        @SerializedName("textile")
        TEXTILE("textile"),
        @SerializedName("wallcovering")
        WALLCOVERING("wallcovering"),
        @SerializedName("unknown")
        UNKNOWN("unknown"),
        @SerializedName("poster")
        POSTER("poster"),
        @SerializedName("advancedFoldingPaper")
        ADVANCED_FOLDING_PAPER("advancedFoldingPaper");
        private final String value;
        private final static Map<String, MediaRequested.MediaFamily> CONSTANTS = new HashMap<String, MediaRequested.MediaFamily>();

        static {
            for (MediaRequested.MediaFamily c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        MediaFamily(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static MediaRequested.MediaFamily fromValue(String value) {
            MediaRequested.MediaFamily constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
