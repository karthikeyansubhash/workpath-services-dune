
package com.hp.ws.cdm.controlpanel;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Folder {

    /**
     * unique identifier of the folder, this will be GUID
     * 
     */
    @SerializedName("id")
    @Expose
    private String id;
    /**
     * The localized title of the folder
     * 
     */
    @SerializedName("title")
    @Expose
    private String title;
    /**
     * The localized description of the folder
     * 
     */
    @SerializedName("description")
    @Expose
    private String description;
    /**
     * array of children. Due to performance reason FW will only accept 1 depth of children
     * 
     */
    @SerializedName("children")
    @Expose
    private List<Application> children = new ArrayList<Application>();

    /**
     * unique identifier of the folder, this will be GUID
     * 
     */
    public String getId() {
        return id;
    }

    /**
     * unique identifier of the folder, this will be GUID
     * 
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * The localized title of the folder
     * 
     */
    public String getTitle() {
        return title;
    }

    /**
     * The localized title of the folder
     * 
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * The localized description of the folder
     * 
     */
    public String getDescription() {
        return description;
    }

    /**
     * The localized description of the folder
     * 
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * array of children. Due to performance reason FW will only accept 1 depth of children
     * 
     */
    public List<Application> getChildren() {
        return children;
    }

    /**
     * array of children. Due to performance reason FW will only accept 1 depth of children
     * 
     */
    public void setChildren(List<Application> children) {
        this.children = children;
    }

}
