
package com.hp.ws.cdm.pubsub;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * on demand information
 * <p>
 * This schema represents the pubsub on demand desire information
 * 
 */
public class OnDemandInfo {

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
     * Client specific instance identifier
     * 
     */
    @SerializedName("clientInstanceId")
    @Expose
    private String clientInstanceId;
    @SerializedName("filter")
    @Expose
    private FilterInfo filter;

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
     * Client specific instance identifier
     * 
     */
    public String getClientInstanceId() {
        return clientInstanceId;
    }

    /**
     * Client specific instance identifier
     * 
     */
    public void setClientInstanceId(String clientInstanceId) {
        this.clientInstanceId = clientInstanceId;
    }

    public FilterInfo getFilter() {
        return filter;
    }

    public void setFilter(FilterInfo filter) {
        this.filter = filter;
    }

}
