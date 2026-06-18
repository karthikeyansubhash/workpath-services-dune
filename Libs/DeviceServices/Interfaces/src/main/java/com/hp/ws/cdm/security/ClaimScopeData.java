
package com.hp.ws.cdm.security;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimScopeData {

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
     * Array of resource identifiers available in the claim scopes list
     * 
     */
    @SerializedName("resourceIdentifierList")
    @Expose
    private List<ResourceIdentifiers> resourceIdentifierList = new ArrayList<ResourceIdentifiers>();

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
     * Array of resource identifiers available in the claim scopes list
     * 
     */
    public List<ResourceIdentifiers> getResourceIdentifierList() {
        return resourceIdentifierList;
    }

    /**
     * Array of resource identifiers available in the claim scopes list
     * 
     */
    public void setResourceIdentifierList(List<ResourceIdentifiers> resourceIdentifierList) {
        this.resourceIdentifierList = resourceIdentifierList;
    }

}
