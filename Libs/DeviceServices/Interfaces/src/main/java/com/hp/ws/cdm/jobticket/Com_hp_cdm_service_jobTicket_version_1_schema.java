
package com.hp.ws.cdm.jobticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * jobTicket definition
 * 
 */
public class Com_hp_cdm_service_jobTicket_version_1_schema {

    @SerializedName("jobTicket")
    @Expose
    private JobTicket jobTicket;
    @SerializedName("capabilities")
    @Expose
    private Capabilities capabilities;

    public JobTicket getJobTicket() {
        return jobTicket;
    }

    public void setJobTicket(JobTicket jobTicket) {
        this.jobTicket = jobTicket;
    }

    public Capabilities getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

}
