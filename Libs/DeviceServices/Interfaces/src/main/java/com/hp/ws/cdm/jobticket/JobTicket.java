
package com.hp.ws.cdm.jobticket;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;

public class JobTicket {

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
     * The unique id of the jobTicket
     * 
     */
    @SerializedName("ticketId")
    @Expose
    private String ticketId;
    /**
     * Reference to the ticket type
     * 
     */
    @SerializedName("ticketReference")
    @Expose
    private String ticketReference;
    /**
     * The specific account identifier of the job owner.
     * 
     */
    @SerializedName("accountId")
    @Expose
    private String accountId;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
    @SerializedName("src")
    @Expose
    private Src src;
    @SerializedName("pipelineOptions")
    @Expose
    private PipelineOptions pipelineOptions;
    @SerializedName("dest")
    @Expose
    private Dest dest;
    /**
     * Reference to a previously completed job to support reprint and resend.
     * 
     */
    @SerializedName("replayJobReference")
    @Expose
    private String replayJobReference;

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
     * The unique id of the jobTicket
     * 
     */
    public String getTicketId() {
        return ticketId;
    }

    /**
     * The unique id of the jobTicket
     * 
     */
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    /**
     * Reference to the ticket type
     * 
     */
    public String getTicketReference() {
        return ticketReference;
    }

    /**
     * Reference to the ticket type
     * 
     */
    public void setTicketReference(String ticketReference) {
        this.ticketReference = ticketReference;
    }

    /**
     * The specific account identifier of the job owner.
     * 
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * The specific account identifier of the job owner.
     * 
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public Src getSrc() {
        return src;
    }

    public void setSrc(Src src) {
        this.src = src;
    }

    public PipelineOptions getPipelineOptions() {
        return pipelineOptions;
    }

    public void setPipelineOptions(PipelineOptions pipelineOptions) {
        this.pipelineOptions = pipelineOptions;
    }

    public Dest getDest() {
        return dest;
    }

    public void setDest(Dest dest) {
        this.dest = dest;
    }

    /**
     * Reference to a previously completed job to support reprint and resend.
     * 
     */
    public String getReplayJobReference() {
        return replayJobReference;
    }

    /**
     * Reference to a previously completed job to support reprint and resend.
     * 
     */
    public void setReplayJobReference(String replayJobReference) {
        this.replayJobReference = replayJobReference;
    }

}
