
package com.hp.ws.cdm.jobticket;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;


/**
 * This object will have each emailContact details
 * 
 */
public class EmailContact {

    /**
     * Display name of the emailContact
     * 
     */
    @SerializedName("displayName")
    @Expose
    private String displayName;
    /**
     * Email address of the emailContact
     * (Required)
     * 
     */
    @SerializedName("emailAddress")
    @Expose
    private String emailAddress;
    /**
     * This will be the unique ID used to get, delete or modify the emailContact. This will be part of emailContact links
     * 
     */
    @SerializedName("contactId")
    @Expose
    private String contactId;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();

    /**
     * Display name of the emailContact
     * 
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Display name of the emailContact
     * 
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Email address of the emailContact
     * (Required)
     * 
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Email address of the emailContact
     * (Required)
     * 
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * This will be the unique ID used to get, delete or modify the emailContact. This will be part of emailContact links
     * 
     */
    public String getContactId() {
        return contactId;
    }

    /**
     * This will be the unique ID used to get, delete or modify the emailContact. This will be part of emailContact links
     * 
     */
    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}
