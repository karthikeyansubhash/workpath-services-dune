
package com.hp.ws.cdm.network;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;

public class ProxyConfig {

    /**
     * Proxy Server authentication configuration
     * 
     */
    @SerializedName("authentication")
    @Expose
    private Authentication authentication;
    /**
     * HTTP proxy exception list containing addresses separated by ;
     * 
     */
    @SerializedName("exceptionList")
    @Expose
    private String exceptionList;
    /**
     * Manual HTTP proxy configuration
     * 
     */
    @SerializedName("httpProxy")
    @Expose
    private HttpProxy httpProxy;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
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
     * Web Proxy Auto Detection WPAD configuration
     * 
     */
    @SerializedName("wpad")
    @Expose
    private Wpad wpad;

    /**
     * Proxy Server authentication configuration
     * 
     */
    public Authentication getAuthentication() {
        return authentication;
    }

    /**
     * Proxy Server authentication configuration
     * 
     */
    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    /**
     * HTTP proxy exception list containing addresses separated by ;
     * 
     */
    public String getExceptionList() {
        return exceptionList;
    }

    /**
     * HTTP proxy exception list containing addresses separated by ;
     * 
     */
    public void setExceptionList(String exceptionList) {
        this.exceptionList = exceptionList;
    }

    /**
     * Manual HTTP proxy configuration
     * 
     */
    public HttpProxy getHttpProxy() {
        return httpProxy;
    }

    /**
     * Manual HTTP proxy configuration
     * 
     */
    public void setHttpProxy(HttpProxy httpProxy) {
        this.httpProxy = httpProxy;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
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

    /**
     * Web Proxy Auto Detection WPAD configuration
     * 
     */
    public Wpad getWpad() {
        return wpad;
    }

    /**
     * Web Proxy Auto Detection WPAD configuration
     * 
     */
    public void setWpad(Wpad wpad) {
        this.wpad = wpad;
    }

}
