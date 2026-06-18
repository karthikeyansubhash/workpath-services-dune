
package com.hp.ws.cdm.controlpanel;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;

public class Application {

    /**
     * The UUID of the application. This ID needs to be same as registered via shortcuts service
     * 
     */
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();

    /**
     * The UUID of the application. This ID needs to be same as registered via shortcuts service
     * 
     */
    public String getId() {
        return id;
    }

    /**
     * The UUID of the application. This ID needs to be same as registered via shortcuts service
     * 
     */
    public void setId(String id) {
        this.id = id;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}
