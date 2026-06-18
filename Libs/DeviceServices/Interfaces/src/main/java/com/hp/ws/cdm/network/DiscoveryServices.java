
package com.hp.ws.cdm.network;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;

public class DiscoveryServices {

    @SerializedName("bonjour")
    @Expose
    private Bonjour bonjour;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
    @SerializedName("slp")
    @Expose
    private Slp slp;
    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("wsDiscovery")
    @Expose
    private WsDiscovery wsDiscovery;

    public Bonjour getBonjour() {
        return bonjour;
    }

    public void setBonjour(Bonjour bonjour) {
        this.bonjour = bonjour;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public Slp getSlp() {
        return slp;
    }

    public void setSlp(Slp slp) {
        this.slp = slp;
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

    public WsDiscovery getWsDiscovery() {
        return wsDiscovery;
    }

    public void setWsDiscovery(WsDiscovery wsDiscovery) {
        this.wsDiscovery = wsDiscovery;
    }

}
