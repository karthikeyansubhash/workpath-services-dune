
package com.hp.ws.cdm.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;

public class AuthenticationAgent {

    /**
     * unique identifier for the agent
     * (Required)
     * 
     */
    @SerializedName("agentId")
    @Expose
    private String agentId;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("agentName")
    @Expose
    private String agentName;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("agentStatus")
    @Expose
    private AuthenticationAgent.AgentStatus agentStatus;
    /**
     * The type of the agent. Future revisions of this type may add new values.
     * (Required)
     * 
     */
    @SerializedName("agentType")
    @Expose
    private AuthenticationAgent.AgentType agentType;
    /**
     * Array of the clients supported by the authentication agent
     * (Required)
     * 
     */
    @SerializedName("agentVisibility")
    @Expose
    private List<SupportedClient> agentVisibility = new ArrayList<SupportedClient>();
    /**
     * This describes what credentials are required
     * (Required)
     * 
     */
    @SerializedName("credentialInfo")
    @Expose
    private CredentialInfo__1 credentialInfo;
    /**
     * This is the localized name, based on the Accept-Language header in the request.
     * 
     */
    @SerializedName("localizedName")
    @Expose
    private String localizedName;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();

    /**
     * unique identifier for the agent
     * (Required)
     * 
     */
    public String getAgentId() {
        return agentId;
    }

    /**
     * unique identifier for the agent
     * (Required)
     * 
     */
    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getAgentName() {
        return agentName;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    /**
     * 
     * (Required)
     * 
     */
    public AuthenticationAgent.AgentStatus getAgentStatus() {
        return agentStatus;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setAgentStatus(AuthenticationAgent.AgentStatus agentStatus) {
        this.agentStatus = agentStatus;
    }

    /**
     * The type of the agent. Future revisions of this type may add new values.
     * (Required)
     * 
     */
    public AuthenticationAgent.AgentType getAgentType() {
        return agentType;
    }

    /**
     * The type of the agent. Future revisions of this type may add new values.
     * (Required)
     * 
     */
    public void setAgentType(AuthenticationAgent.AgentType agentType) {
        this.agentType = agentType;
    }

    /**
     * Array of the clients supported by the authentication agent
     * (Required)
     * 
     */
    public List<SupportedClient> getAgentVisibility() {
        return agentVisibility;
    }

    /**
     * Array of the clients supported by the authentication agent
     * (Required)
     * 
     */
    public void setAgentVisibility(List<SupportedClient> agentVisibility) {
        this.agentVisibility = agentVisibility;
    }

    /**
     * This describes what credentials are required
     * (Required)
     * 
     */
    public CredentialInfo__1 getCredentialInfo() {
        return credentialInfo;
    }

    /**
     * This describes what credentials are required
     * (Required)
     * 
     */
    public void setCredentialInfo(CredentialInfo__1 credentialInfo) {
        this.credentialInfo = credentialInfo;
    }

    /**
     * This is the localized name, based on the Accept-Language header in the request.
     * 
     */
    public String getLocalizedName() {
        return localizedName;
    }

    /**
     * This is the localized name, based on the Accept-Language header in the request.
     * 
     */
    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }

    /**
     * 
     * (Required)
     * 
     */
    public List<Link> getLinks() {
        return links;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public enum AgentStatus {

        @SerializedName("available")
        AVAILABLE("available"),
        @SerializedName("unavailable")
        UNAVAILABLE("unavailable");
        private final String value;
        private final static Map<String, AuthenticationAgent.AgentStatus> CONSTANTS = new HashMap<String, AuthenticationAgent.AgentStatus>();

        static {
            for (AuthenticationAgent.AgentStatus c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        AgentStatus(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static AuthenticationAgent.AgentStatus fromValue(String value) {
            AuthenticationAgent.AgentStatus constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * The type of the agent. Future revisions of this type may add new values.
     * 
     */
    public enum AgentType {

        @SerializedName("devicePin")
        DEVICE_PIN("devicePin"),
        @SerializedName("deviceUser")
        DEVICE_USER("deviceUser"),
        @SerializedName("idCode")
        ID_CODE("idCode"),
        @SerializedName("deviceService")
        DEVICE_SERVICE("deviceService"),
        @SerializedName("pushButton")
        PUSH_BUTTON("pushButton"),
        @SerializedName("ldap")
        LDAP("ldap"),
        @SerializedName("windows")
        WINDOWS("windows"),
        @SerializedName("smartCard")
        SMART_CARD("smartCard"),
        @SerializedName("deviceAdmin")
        DEVICE_ADMIN("deviceAdmin"),
        @SerializedName("abacus")
        ABACUS("abacus"),
        @SerializedName("custom")
        CUSTOM("custom"),
        @SerializedName("guest")
        GUEST("guest"),
        @SerializedName("argos")
        ARGOS("argos"),
        @SerializedName("other")
        OTHER("other");
        private final String value;
        private final static Map<String, AuthenticationAgent.AgentType> CONSTANTS = new HashMap<String, AuthenticationAgent.AgentType>();

        static {
            for (AuthenticationAgent.AgentType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        AgentType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static AuthenticationAgent.AgentType fromValue(String value) {
            AuthenticationAgent.AgentType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
