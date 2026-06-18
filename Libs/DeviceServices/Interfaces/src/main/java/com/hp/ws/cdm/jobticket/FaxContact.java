
package com.hp.ws.cdm.jobticket;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;

public class FaxContact {

    /**
     * Fax identification number for the modem. E.123 is a standards-based recommendation by the International Telecommunications Union sector ITU-T, entitled notation for national and international telephone numbers, e-mail addresses and web addresses.
     * 
     */
    @SerializedName("faxNumber")
    @Expose
    private String faxNumber;
    /**
     * SIP URI for Fax Over IP calls.
     * 
     */
    @SerializedName("faxId")
    @Expose
    private String faxId;
    /**
     * Billing code for send fax job
     * 
     */
    @SerializedName("billingCode")
    @Expose
    private String billingCode;
    /**
     * This will be the unique ID used to get, delete or modify the contact. This will be part of fax contact links
     * 
     */
    @SerializedName("faxContactId")
    @Expose
    private String faxContactId;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();

    /**
     * Fax identification number for the modem. E.123 is a standards-based recommendation by the International Telecommunications Union sector ITU-T, entitled notation for national and international telephone numbers, e-mail addresses and web addresses.
     * 
     */
    public String getFaxNumber() {
        return faxNumber;
    }

    /**
     * Fax identification number for the modem. E.123 is a standards-based recommendation by the International Telecommunications Union sector ITU-T, entitled notation for national and international telephone numbers, e-mail addresses and web addresses.
     * 
     */
    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    /**
     * SIP URI for Fax Over IP calls.
     * 
     */
    public String getFaxId() {
        return faxId;
    }

    /**
     * SIP URI for Fax Over IP calls.
     * 
     */
    public void setFaxId(String faxId) {
        this.faxId = faxId;
    }

    /**
     * Billing code for send fax job
     * 
     */
    public String getBillingCode() {
        return billingCode;
    }

    /**
     * Billing code for send fax job
     * 
     */
    public void setBillingCode(String billingCode) {
        this.billingCode = billingCode;
    }

    /**
     * This will be the unique ID used to get, delete or modify the contact. This will be part of fax contact links
     * 
     */
    public String getFaxContactId() {
        return faxContactId;
    }

    /**
     * This will be the unique ID used to get, delete or modify the contact. This will be part of fax contact links
     * 
     */
    public void setFaxContactId(String faxContactId) {
        this.faxContactId = faxContactId;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}
