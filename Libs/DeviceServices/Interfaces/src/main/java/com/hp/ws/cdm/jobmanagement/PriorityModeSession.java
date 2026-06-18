
package com.hp.ws.cdm.jobmanagement;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;

public class PriorityModeSession {

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
     * The sessionId for the priorityMode session. This sessionId will be used when creating jobs to ensure that job runs as part of this priority mode session.
     * 
     */
    @SerializedName("priorityModeSessionId")
    @Expose
    private String priorityModeSessionId;
    /**
     * The application Id of the client requesting to enter a priorityMode session
     * 
     */
    @SerializedName("applicationId")
    @Expose
    private String applicationId;
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
     * The sessionId for the priorityMode session. This sessionId will be used when creating jobs to ensure that job runs as part of this priority mode session.
     * 
     */
    public String getPriorityModeSessionId() {
        return priorityModeSessionId;
    }

    /**
     * The sessionId for the priorityMode session. This sessionId will be used when creating jobs to ensure that job runs as part of this priority mode session.
     * 
     */
    public void setPriorityModeSessionId(String priorityModeSessionId) {
        this.priorityModeSessionId = priorityModeSessionId;
    }

    /**
     * The application Id of the client requesting to enter a priorityMode session
     * 
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * The application Id of the client requesting to enter a priorityMode session
     * 
     */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}
