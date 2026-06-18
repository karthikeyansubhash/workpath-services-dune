
package com.hp.ws.cdm.email;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;

public class AllowedEmailDomains {

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
     * The list of email domains
     * 
     */
    @SerializedName("emailDomains")
    @Expose
    private List<String> emailDomains = new ArrayList<String>();
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
     * The list of email domains
     * 
     */
    public List<String> getEmailDomains() {
        return emailDomains;
    }

    /**
     * The list of email domains
     * 
     */
    public void setEmailDomains(List<String> emailDomains) {
        this.emailDomains = emailDomains;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}
