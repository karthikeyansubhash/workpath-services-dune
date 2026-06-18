
package com.hp.ws.cdm.security;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Information about retry behavior.
 * 
 */
public class RetryBehavior {

    /**
     * The connection timeout value in seconds (0-60). If a connection to the server cannot be established within this time, a failure will be declared.
     * (Required)
     * 
     */
    @SerializedName("timeoutSeconds")
    @Expose
    private Integer timeoutSeconds;
    /**
     * The maximum number of retries (1-10) for the solution server.
     * (Required)
     * 
     */
    @SerializedName("maxRetries")
    @Expose
    private Integer maxRetries;

    /**
     * The connection timeout value in seconds (0-60). If a connection to the server cannot be established within this time, a failure will be declared.
     * (Required)
     * 
     */
    public Integer getTimeoutSeconds() {
        return timeoutSeconds;
    }

    /**
     * The connection timeout value in seconds (0-60). If a connection to the server cannot be established within this time, a failure will be declared.
     * (Required)
     * 
     */
    public void setTimeoutSeconds(Integer timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    /**
     * The maximum number of retries (1-10) for the solution server.
     * (Required)
     * 
     */
    public Integer getMaxRetries() {
        return maxRetries;
    }

    /**
     * The maximum number of retries (1-10) for the solution server.
     * (Required)
     * 
     */
    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

}
