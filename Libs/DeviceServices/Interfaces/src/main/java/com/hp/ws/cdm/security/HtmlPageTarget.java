
package com.hp.ws.cdm.security;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Present if the user input is to be acquired by displaying an acquired HTML page. The target describes how to fetch the page.
 * 
 */
public class HtmlPageTarget {

    /**
     * The URL to use to fetch the HTML page. This may include query parameters
     * 
     */
    @SerializedName("url")
    @Expose
    private String url;
    /**
     * An array of headers to send with the request.
     * 
     */
    @SerializedName("headers")
    @Expose
    private List<Header> headers = new ArrayList<Header>();
    /**
     * Information to authenticate the request.
     * 
     */
    @SerializedName("authentication")
    @Expose
    private Authentication authentication;
    /**
     * Information about retry behavior.
     * 
     */
    @SerializedName("retryBehavior")
    @Expose
    private RetryBehavior retryBehavior;
    /**
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * 
     */
    @SerializedName("solutionId")
    @Expose
    private String solutionId;
    /**
     * The list of external network addresses that are to be trusted
     * 
     */
    @SerializedName("trustedSites")
    @Expose
    private String trustedSites;

    /**
     * The URL to use to fetch the HTML page. This may include query parameters
     * 
     */
    public String getUrl() {
        return url;
    }

    /**
     * The URL to use to fetch the HTML page. This may include query parameters
     * 
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * An array of headers to send with the request.
     * 
     */
    public List<Header> getHeaders() {
        return headers;
    }

    /**
     * An array of headers to send with the request.
     * 
     */
    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    /**
     * Information to authenticate the request.
     * 
     */
    public Authentication getAuthentication() {
        return authentication;
    }

    /**
     * Information to authenticate the request.
     * 
     */
    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    /**
     * Information about retry behavior.
     * 
     */
    public RetryBehavior getRetryBehavior() {
        return retryBehavior;
    }

    /**
     * Information about retry behavior.
     * 
     */
    public void setRetryBehavior(RetryBehavior retryBehavior) {
        this.retryBehavior = retryBehavior;
    }

    /**
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * 
     */
    public String getSolutionId() {
        return solutionId;
    }

    /**
     * standard uuid format - according to RFC 4122. In its canonical textual representation, the 16 octets of a UUID are represented as 32 hexadecimal (base-16) digits, displayed in five groups separated by hyphens, in the form 8-4-4-4-12 for a total of 36 characters (32 hexadecimal characters and 4 hyphens).   For example: 123e4567-e89b-12d3-a456-426614174000
     * 
     */
    public void setSolutionId(String solutionId) {
        this.solutionId = solutionId;
    }

    /**
     * The list of external network addresses that are to be trusted
     * 
     */
    public String getTrustedSites() {
        return trustedSites;
    }

    /**
     * The list of external network addresses that are to be trusted
     * 
     */
    public void setTrustedSites(String trustedSites) {
        this.trustedSites = trustedSites;
    }

}
