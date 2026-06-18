
package com.hp.ws.cdm.security;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;

public class IdCode {

    /**
     * unique identifier for the id code
     * 
     */
    @SerializedName("accountId")
    @Expose
    private String accountId;
    /**
     * codeId is unique PIN to identify a user
     * 
     */
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("emailAddress")
    @Expose
    private String emailAddress;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();

    /**
     * unique identifier for the id code
     * 
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * unique identifier for the id code
     * 
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * codeId is unique PIN to identify a user
     * 
     */
    public String getId() {
        return id;
    }

    /**
     * codeId is unique PIN to identify a user
     * 
     */
    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}
