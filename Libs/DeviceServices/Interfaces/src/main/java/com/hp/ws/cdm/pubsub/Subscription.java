
package com.hp.ws.cdm.pubsub;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;


/**
 * subscription
 * <p>
 * This schema represents the pubsub subscription
 * 
 */
public class Subscription {

    /**
     * The callback uri that asynchronous events will be sent to as they occur.
     * 
     */
    @SerializedName("callbackUri")
    @Expose
    private String callbackUri;
    /**
     * Client identifier
     * 
     */
    @SerializedName("clientId")
    @Expose
    private String clientId;
    /**
     * Client specific subscription instance identifier
     * 
     */
    @SerializedName("clientInstanceId")
    @Expose
    private String clientInstanceId;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
    /**
     * maximum message size in kilo bytes for this subscription
     * 
     */
    @SerializedName("maxMessageSizeKilobytes")
    @Expose
    private Integer maxMessageSizeKilobytes;
    /**
     * maximum limit for collection pagination for this subscription
     * 
     */
    @SerializedName("maxPaginationLimit")
    @Expose
    private Integer maxPaginationLimit;
    @SerializedName("resources")
    @Expose
    private List<Resource> resources = new ArrayList<Resource>();
    @SerializedName("subscriptionId")
    @Expose
    private String subscriptionId;
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
     * The callback uri that asynchronous events will be sent to as they occur.
     * 
     */
    public String getCallbackUri() {
        return callbackUri;
    }

    /**
     * The callback uri that asynchronous events will be sent to as they occur.
     * 
     */
    public void setCallbackUri(String callbackUri) {
        this.callbackUri = callbackUri;
    }

    /**
     * Client identifier
     * 
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Client identifier
     * 
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * Client specific subscription instance identifier
     * 
     */
    public String getClientInstanceId() {
        return clientInstanceId;
    }

    /**
     * Client specific subscription instance identifier
     * 
     */
    public void setClientInstanceId(String clientInstanceId) {
        this.clientInstanceId = clientInstanceId;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * maximum message size in kilo bytes for this subscription
     * 
     */
    public Integer getMaxMessageSizeKilobytes() {
        return maxMessageSizeKilobytes;
    }

    /**
     * maximum message size in kilo bytes for this subscription
     * 
     */
    public void setMaxMessageSizeKilobytes(Integer maxMessageSizeKilobytes) {
        this.maxMessageSizeKilobytes = maxMessageSizeKilobytes;
    }

    /**
     * maximum limit for collection pagination for this subscription
     * 
     */
    public Integer getMaxPaginationLimit() {
        return maxPaginationLimit;
    }

    /**
     * maximum limit for collection pagination for this subscription
     * 
     */
    public void setMaxPaginationLimit(Integer maxPaginationLimit) {
        this.maxPaginationLimit = maxPaginationLimit;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

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

}
