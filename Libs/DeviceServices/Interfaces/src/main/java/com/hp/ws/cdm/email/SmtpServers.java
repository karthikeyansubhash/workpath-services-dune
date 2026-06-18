
package com.hp.ws.cdm.email;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;

public class SmtpServers {

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
     * Array of smtp server settings
     * 
     */
    @SerializedName("servers")
    @Expose
    private List<SmtpServer> servers = new ArrayList<SmtpServer>();
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();

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
     * Array of smtp server settings
     * 
     */
    public List<SmtpServer> getServers() {
        return servers;
    }

    /**
     * Array of smtp server settings
     * 
     */
    public void setServers(List<SmtpServer> servers) {
        this.servers = servers;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}
