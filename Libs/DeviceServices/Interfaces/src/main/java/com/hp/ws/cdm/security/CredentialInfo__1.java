
package com.hp.ws.cdm.security;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * This describes what credentials are required
 * 
 */
public class CredentialInfo__1 {

    /**
     * Identifies the type of user input through a single defined enumeration value. Future revisions of this type may add new values.
     * 
     */
    @SerializedName("credentialType")
    @Expose
    private com.hp.ws.cdm.security.CredentialInfo.CredentialType credentialType;
    /**
     * Present if the credentials can be acquired by processing metadata describing the required information. The format of this metadata is not currently described, but can be added by further design activities, as needed.
     * 
     */
    @SerializedName("metadata")
    @Expose
    private List<CredentialMetadataItem> metadata = new ArrayList<CredentialMetadataItem>();

    /**
     * Identifies the type of user input through a single defined enumeration value. Future revisions of this type may add new values.
     * 
     */
    public com.hp.ws.cdm.security.CredentialInfo.CredentialType getCredentialType() {
        return credentialType;
    }

    /**
     * Identifies the type of user input through a single defined enumeration value. Future revisions of this type may add new values.
     * 
     */
    public void setCredentialType(com.hp.ws.cdm.security.CredentialInfo.CredentialType credentialType) {
        this.credentialType = credentialType;
    }

    /**
     * Present if the credentials can be acquired by processing metadata describing the required information. The format of this metadata is not currently described, but can be added by further design activities, as needed.
     * 
     */
    public List<CredentialMetadataItem> getMetadata() {
        return metadata;
    }

    /**
     * Present if the credentials can be acquired by processing metadata describing the required information. The format of this metadata is not currently described, but can be added by further design activities, as needed.
     * 
     */
    public void setMetadata(List<CredentialMetadataItem> metadata) {
        this.metadata = metadata;
    }

}
