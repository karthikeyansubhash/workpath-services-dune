
package com.hp.ws.cdm.jobmanagement;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;

public class Stats {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    /**
     * unique job identifier
     * (Required)
     * 
     */
    @SerializedName("jobUuid")
    @Expose
    private String jobUuid;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
    /**
     * generic job details
     * (Required)
     * 
     */
    @SerializedName("jobInfo")
    @Expose
    private JobInfo jobInfo;
    /**
     * provides submitting client information
     * 
     */
    @SerializedName("clientInfo")
    @Expose
    private ClientInfo clientInfo;
    /**
     * provides the print job specific details
     * 
     */
    @SerializedName("printInfo")
    @Expose
    private PrintInfo printInfo;
    /**
     * provides the scan job specific details
     * 
     */
    @SerializedName("scanInfo")
    @Expose
    private ScanInfo scanInfo;
    /**
     * provides the send job specific details
     * 
     */
    @SerializedName("sendInfo")
    @Expose
    private SendInfo sendInfo;
    /**
     * provides the fax job specific details
     * 
     */
    @SerializedName("faxInfo")
    @Expose
    private FaxInfo faxInfo;
    /**
     * provides the finishingInfo job specific details
     * 
     */
    @SerializedName("finishingInfo")
    @Expose
    private FinishingInfo finishingInfo;

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * unique job identifier
     * (Required)
     * 
     */
    public String getJobUuid() {
        return jobUuid;
    }

    /**
     * unique job identifier
     * (Required)
     * 
     */
    public void setJobUuid(String jobUuid) {
        this.jobUuid = jobUuid;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * generic job details
     * (Required)
     * 
     */
    public JobInfo getJobInfo() {
        return jobInfo;
    }

    /**
     * generic job details
     * (Required)
     * 
     */
    public void setJobInfo(JobInfo jobInfo) {
        this.jobInfo = jobInfo;
    }

    /**
     * provides submitting client information
     * 
     */
    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    /**
     * provides submitting client information
     * 
     */
    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    /**
     * provides the print job specific details
     * 
     */
    public PrintInfo getPrintInfo() {
        return printInfo;
    }

    /**
     * provides the print job specific details
     * 
     */
    public void setPrintInfo(PrintInfo printInfo) {
        this.printInfo = printInfo;
    }

    /**
     * provides the scan job specific details
     * 
     */
    public ScanInfo getScanInfo() {
        return scanInfo;
    }

    /**
     * provides the scan job specific details
     * 
     */
    public void setScanInfo(ScanInfo scanInfo) {
        this.scanInfo = scanInfo;
    }

    /**
     * provides the send job specific details
     * 
     */
    public SendInfo getSendInfo() {
        return sendInfo;
    }

    /**
     * provides the send job specific details
     * 
     */
    public void setSendInfo(SendInfo sendInfo) {
        this.sendInfo = sendInfo;
    }

    /**
     * provides the fax job specific details
     * 
     */
    public FaxInfo getFaxInfo() {
        return faxInfo;
    }

    /**
     * provides the fax job specific details
     * 
     */
    public void setFaxInfo(FaxInfo faxInfo) {
        this.faxInfo = faxInfo;
    }

    /**
     * provides the finishingInfo job specific details
     * 
     */
    public FinishingInfo getFinishingInfo() {
        return finishingInfo;
    }

    /**
     * provides the finishingInfo job specific details
     * 
     */
    public void setFinishingInfo(FinishingInfo finishingInfo) {
        this.finishingInfo = finishingInfo;
    }

}
