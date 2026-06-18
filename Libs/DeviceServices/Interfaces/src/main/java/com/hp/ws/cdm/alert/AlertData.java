
package com.hp.ws.cdm.alert;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * option for data
 * 
 */
public class AlertData {

    /**
     * GUN of the alert data resource.
     * (Required)
     * 
     */
    @SerializedName("resourceGun")
    @Expose
    private String resourceGun;
    /**
     * path of the alert data resource.
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
     * 
     * (Required)
     * 
     */
    @SerializedName("value")
    @Expose
    private Property value;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();

    /**
     * GUN of the alert data resource.
     * (Required)
     * 
     */
    public String getResourceGun() {
        return resourceGun;
    }

    /**
     * GUN of the alert data resource.
     * (Required)
     * 
     */
    public void setResourceGun(String resourceGun) {
        this.resourceGun = resourceGun;
    }

    /**
     * path of the alert data resource.
     * 
     */
    public String getResourcePath() {
        return resourcePath;
    }

    /**
     * path of the alert data resource.
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
     * 
     * (Required)
     * 
     */
    public Property getValue() {
        return value;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setValue(Property value) {
        this.value = value;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}
