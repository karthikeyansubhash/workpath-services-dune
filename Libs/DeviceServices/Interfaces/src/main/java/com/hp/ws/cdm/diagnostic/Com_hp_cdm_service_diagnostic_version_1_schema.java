
package com.hp.ws.cdm.diagnostic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * system event log resource schema definitions - Promoted from 1.1.0 to refer shared type 1.2.0-beta.1
 * 
 */
public class Com_hp_cdm_service_diagnostic_version_1_schema {

    /**
     * paginated system events
     * 
     */
    @SerializedName("systemEvents")
    @Expose
    private PaginatedEvents systemEvents;
    /**
     * paginated system events
     * 
     */
    @SerializedName("errorSystemEvents")
    @Expose
    private PaginatedEvents errorSystemEvents;
    /**
     * paginated system events
     * 
     */
    @SerializedName("warningSystemEvents")
    @Expose
    private PaginatedEvents warningSystemEvents;
    /**
     * paginated system events
     * 
     */
    @SerializedName("infoSystemEvents")
    @Expose
    private PaginatedEvents infoSystemEvents;

    /**
     * paginated system events
     * 
     */
    public PaginatedEvents getSystemEvents() {
        return systemEvents;
    }

    /**
     * paginated system events
     * 
     */
    public void setSystemEvents(PaginatedEvents systemEvents) {
        this.systemEvents = systemEvents;
    }

    /**
     * paginated system events
     * 
     */
    public PaginatedEvents getErrorSystemEvents() {
        return errorSystemEvents;
    }

    /**
     * paginated system events
     * 
     */
    public void setErrorSystemEvents(PaginatedEvents errorSystemEvents) {
        this.errorSystemEvents = errorSystemEvents;
    }

    /**
     * paginated system events
     * 
     */
    public PaginatedEvents getWarningSystemEvents() {
        return warningSystemEvents;
    }

    /**
     * paginated system events
     * 
     */
    public void setWarningSystemEvents(PaginatedEvents warningSystemEvents) {
        this.warningSystemEvents = warningSystemEvents;
    }

    /**
     * paginated system events
     * 
     */
    public PaginatedEvents getInfoSystemEvents() {
        return infoSystemEvents;
    }

    /**
     * paginated system events
     * 
     */
    public void setInfoSystemEvents(PaginatedEvents infoSystemEvents) {
        this.infoSystemEvents = infoSystemEvents;
    }

}
