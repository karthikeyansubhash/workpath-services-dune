
package com.hp.ws.cdm.alert;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;
import com.hp.ws.cdm.commonglossary.Supported;


/**
 * option for action
 * 
 */
public class AlertAction {

    /**
     * gun of action resource
     * (Required)
     * 
     */
    @SerializedName("resourceGun")
    @Expose
    private String resourceGun;
    /**
     * path of the alert action resource.
     * 
     */
    @SerializedName("resourcePath")
    @Expose
    private String resourcePath;
    /**
     * JSON path to the property in the resource
     * (Required)
     * 
     */
    @SerializedName("propertyPointer")
    @Expose
    private String propertyPointer;
    /**
     * supported action value and list of requested data
     * (Required)
     * 
     */
    @SerializedName("supported")
    @Expose
    private List<Supported> supported = new ArrayList<Supported>();
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();

    /**
     * gun of action resource
     * (Required)
     * 
     */
    public String getResourceGun() {
        return resourceGun;
    }

    /**
     * gun of action resource
     * (Required)
     * 
     */
    public void setResourceGun(String resourceGun) {
        this.resourceGun = resourceGun;
    }

    /**
     * path of the alert action resource.
     * 
     */
    public String getResourcePath() {
        return resourcePath;
    }

    /**
     * path of the alert action resource.
     * 
     */
    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    /**
     * JSON path to the property in the resource
     * (Required)
     * 
     */
    public String getPropertyPointer() {
        return propertyPointer;
    }

    /**
     * JSON path to the property in the resource
     * (Required)
     * 
     */
    public void setPropertyPointer(String propertyPointer) {
        this.propertyPointer = propertyPointer;
    }

    /**
     * supported action value and list of requested data
     * (Required)
     * 
     */
    public List<Supported> getSupported() {
        return supported;
    }

    /**
     * supported action value and list of requested data
     * (Required)
     * 
     */
    public void setSupported(List<Supported> supported) {
        this.supported = supported;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}
