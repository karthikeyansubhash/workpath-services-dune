
package com.hp.ws.cdm.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * This describes what credentials are required
 * 
 */
public class CredentialInfo {

    /**
     * Identifies the type of user input through a single defined enumeration value. Future revisions of this type may add new values.
     * 
     */
    @SerializedName("credentialType")
    @Expose
    private CredentialInfo.CredentialType credentialType;
    /**
     * Present if the credentials can be acquired by processing metadata describing the required information. The format of this metadata is not currently described, but can be added by further design activities, as needed.
     * 
     */
    @SerializedName("metadata")
    @Expose
    private List<CredentialMetadataItem> metadata = new ArrayList<CredentialMetadataItem>();
    /**
     * Present if the user input is to be acquired by displaying an acquired HTML page. The target describes how to fetch the page.
     * 
     */
    @SerializedName("htmlPageTarget")
    @Expose
    private HtmlPageTarget htmlPageTarget;

    /**
     * Identifies the type of user input through a single defined enumeration value. Future revisions of this type may add new values.
     * 
     */
    public CredentialInfo.CredentialType getCredentialType() {
        return credentialType;
    }

    /**
     * Identifies the type of user input through a single defined enumeration value. Future revisions of this type may add new values.
     * 
     */
    public void setCredentialType(CredentialInfo.CredentialType credentialType) {
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

    /**
     * Present if the user input is to be acquired by displaying an acquired HTML page. The target describes how to fetch the page.
     * 
     */
    public HtmlPageTarget getHtmlPageTarget() {
        return htmlPageTarget;
    }

    /**
     * Present if the user input is to be acquired by displaying an acquired HTML page. The target describes how to fetch the page.
     * 
     */
    public void setHtmlPageTarget(HtmlPageTarget htmlPageTarget) {
        this.htmlPageTarget = htmlPageTarget;
    }


    /**
     * Identifies the type of user input through a single defined enumeration value. Future revisions of this type may add new values.
     * 
     */
    public enum CredentialType {

        @SerializedName("pinCode")
        PIN_CODE("pinCode"),
        @SerializedName("passwordOnly")
        PASSWORD_ONLY("passwordOnly"),
        @SerializedName("nameAndPassword")
        NAME_AND_PASSWORD("nameAndPassword"),
        @SerializedName("namePasswordAndDomain")
        NAME_PASSWORD_AND_DOMAIN("namePasswordAndDomain"),
        @SerializedName("seeMetadata")
        SEE_METADATA("seeMetadata"),
        @SerializedName("dynamic")
        DYNAMIC("dynamic");
        private final String value;
        private final static Map<String, CredentialInfo.CredentialType> CONSTANTS = new HashMap<String, CredentialInfo.CredentialType>();

        static {
            for (CredentialInfo.CredentialType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        CredentialType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static CredentialInfo.CredentialType fromValue(String value) {
            CredentialInfo.CredentialType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
