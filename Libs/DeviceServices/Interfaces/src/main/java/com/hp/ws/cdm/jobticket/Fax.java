
package com.hp.ws.cdm.jobticket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;
import com.hp.ws.cdm.commonglossary.Property;

public class Fax {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("destinationList")
    @Expose
    private List<FaxContact> destinationList = new ArrayList<FaxContact>();
    /**
     * Resolution for send and archive job.
     * 
     */
    @SerializedName("resolution")
    @Expose
    private FaxResolution resolution;
    /**
     * Dialing prefix for send fax job
     * 
     */
    @SerializedName("dialingPrefix")
    @Expose
    private String dialingPrefix;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("faxCover")
    @Expose
    private Property.FeatureEnabled faxCover;
    /**
     * Fax line selection for send and archive job.
     * 
     */
    @SerializedName("line")
    @Expose
    private FaxLine line;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();

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

    public List<FaxContact> getDestinationList() {
        return destinationList;
    }

    public void setDestinationList(List<FaxContact> destinationList) {
        this.destinationList = destinationList;
    }

    /**
     * Resolution for send and archive job.
     * 
     */
    public FaxResolution getResolution() {
        return resolution;
    }

    /**
     * Resolution for send and archive job.
     * 
     */
    public void setResolution(FaxResolution resolution) {
        this.resolution = resolution;
    }

    /**
     * Dialing prefix for send fax job
     * 
     */
    public String getDialingPrefix() {
        return dialingPrefix;
    }

    /**
     * Dialing prefix for send fax job
     * 
     */
    public void setDialingPrefix(String dialingPrefix) {
        this.dialingPrefix = dialingPrefix;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getFaxCover() {
        return faxCover;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setFaxCover(Property.FeatureEnabled faxCover) {
        this.faxCover = faxCover;
    }

    /**
     * Fax line selection for send and archive job.
     * 
     */
    public FaxLine getLine() {
        return line;
    }

    /**
     * Fax line selection for send and archive job.
     * 
     */
    public void setLine(FaxLine line) {
        this.line = line;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }


    /**
     * Fax line selection for send and archive job.
     * 
     */
    public enum FaxLine {

        @SerializedName("auto")
        AUTO("auto"),
        @SerializedName("line1")
        LINE_1("line1"),
        @SerializedName("line2")
        LINE_2("line2");
        private final String value;
        private final static Map<String, FaxLine> CONSTANTS = new HashMap<String, FaxLine>();

        static {
            for (FaxLine c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        FaxLine(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static FaxLine fromValue(String value) {
            FaxLine constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Resolution for send and archive job.
     * 
     */
    public enum FaxResolution {

        @SerializedName("standard")
        STANDARD("standard"),
        @SerializedName("fine")
        FINE("fine"),
        @SerializedName("superfine")
        SUPERFINE("superfine"),
        @SerializedName("ultrafine")
        ULTRAFINE("ultrafine");
        private final String value;
        private final static Map<String, FaxResolution> CONSTANTS = new HashMap<String, FaxResolution>();

        static {
            for (FaxResolution c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        FaxResolution(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static FaxResolution fromValue(String value) {
            FaxResolution constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
