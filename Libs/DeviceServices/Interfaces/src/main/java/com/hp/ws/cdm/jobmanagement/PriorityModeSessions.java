
package com.hp.ws.cdm.jobmanagement;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PriorityModeSessions {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("priorityModeSessions")
    @Expose
    private List<PriorityModeSession> priorityModeSessions = new ArrayList<PriorityModeSession>();

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

    public List<PriorityModeSession> getPriorityModeSessions() {
        return priorityModeSessions;
    }

    public void setPriorityModeSessions(List<PriorityModeSession> priorityModeSessions) {
        this.priorityModeSessions = priorityModeSessions;
    }

}
