
package com.hp.ws.cdm.pubsub;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * pubsub capabilities
 * <p>
 * List of resources which support eventing
 * 
 */
public class Capabilities {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    /**
     * A list of events in order of sequence number starting at sequenceNumber with a maximum length of limit.
     * 
     */
    @SerializedName("supportedGuns")
    @Expose
    private List<String> supportedGuns = new ArrayList<String>();
    /**
     * supported maximum message size in kilo bytes
     * 
     */
    @SerializedName("supportedMaxMessageSizeKilobytes")
    @Expose
    private Integer supportedMaxMessageSizeKilobytes;
    /**
     * supported maximum limit for collection pagination
     * 
     */
    @SerializedName("supportedMaxPaginationLimit")
    @Expose
    private Integer supportedMaxPaginationLimit;

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * A list of events in order of sequence number starting at sequenceNumber with a maximum length of limit.
     * 
     */
    public List<String> getSupportedGuns() {
        return supportedGuns;
    }

    /**
     * A list of events in order of sequence number starting at sequenceNumber with a maximum length of limit.
     * 
     */
    public void setSupportedGuns(List<String> supportedGuns) {
        this.supportedGuns = supportedGuns;
    }

    /**
     * supported maximum message size in kilo bytes
     * 
     */
    public Integer getSupportedMaxMessageSizeKilobytes() {
        return supportedMaxMessageSizeKilobytes;
    }

    /**
     * supported maximum message size in kilo bytes
     * 
     */
    public void setSupportedMaxMessageSizeKilobytes(Integer supportedMaxMessageSizeKilobytes) {
        this.supportedMaxMessageSizeKilobytes = supportedMaxMessageSizeKilobytes;
    }

    /**
     * supported maximum limit for collection pagination
     * 
     */
    public Integer getSupportedMaxPaginationLimit() {
        return supportedMaxPaginationLimit;
    }

    /**
     * supported maximum limit for collection pagination
     * 
     */
    public void setSupportedMaxPaginationLimit(Integer supportedMaxPaginationLimit) {
        this.supportedMaxPaginationLimit = supportedMaxPaginationLimit;
    }

}
