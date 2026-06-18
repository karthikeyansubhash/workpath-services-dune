
package com.hp.ws.cdm.security;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResourceIdentifiers {

    /**
     * Version of the resource supported by the device
     * 
     */
    @SerializedName("resourceIdentifier")
    @Expose
    private String resourceIdentifier;
    /**
     * Indicates the claim aliases for the CDM resourceIdentifier
     * 
     */
    @SerializedName("claimAliases")
    @Expose
    private List<SupportedClaimAliases> claimAliases = new ArrayList<SupportedClaimAliases>();

    /**
     * Version of the resource supported by the device
     * 
     */
    public String getResourceIdentifier() {
        return resourceIdentifier;
    }

    /**
     * Version of the resource supported by the device
     * 
     */
    public void setResourceIdentifier(String resourceIdentifier) {
        this.resourceIdentifier = resourceIdentifier;
    }

    /**
     * Indicates the claim aliases for the CDM resourceIdentifier
     * 
     */
    public List<SupportedClaimAliases> getClaimAliases() {
        return claimAliases;
    }

    /**
     * Indicates the claim aliases for the CDM resourceIdentifier
     * 
     */
    public void setClaimAliases(List<SupportedClaimAliases> claimAliases) {
        this.claimAliases = claimAliases;
    }

}
