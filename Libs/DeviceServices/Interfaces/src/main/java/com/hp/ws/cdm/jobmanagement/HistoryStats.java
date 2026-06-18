
package com.hp.ws.cdm.jobmanagement;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;

public class HistoryStats {

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
     * Maximum Pagination Limit supported by the server
     * 
     */
    @SerializedName("maxPaginationLimit")
    @Expose
    private Integer maxPaginationLimit;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
    @SerializedName("historyStats")
    @Expose
    private List<Stats> historyStats = new ArrayList<Stats>();

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
     * Maximum Pagination Limit supported by the server
     * 
     */
    public Integer getMaxPaginationLimit() {
        return maxPaginationLimit;
    }

    /**
     * Maximum Pagination Limit supported by the server
     * 
     */
    public void setMaxPaginationLimit(Integer maxPaginationLimit) {
        this.maxPaginationLimit = maxPaginationLimit;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<Stats> getHistoryStats() {
        return historyStats;
    }

    public void setHistoryStats(List<Stats> historyStats) {
        this.historyStats = historyStats;
    }

}
