package com.hp.ws.e2workpathInterop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class SmtpServer {

    /**
     * display name
     * 
     */
    @SerializedName("displayName")
    @Expose
    private String displayName;
    /**
     * The SMTP server address
     * 
     */
    @SerializedName("serverAddress")
    @Expose
    private String serverAddress;
    /**
     * Unique ID for each smtp server settings
     * 
     */
    @SerializedName("smtpServerId")
    @Expose
    private String smtpServerId;
    /**
     * The port number for the communication
     * 
     */
    @SerializedName("serverPort")
    @Expose
    private Integer serverPort = -1;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("useSsl")
    @Expose
    private Property.FeatureEnabled useSsl;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("serverRequireAuthentication")
    @Expose
    private Property.FeatureEnabled serverRequireAuthentication;

    /**
     * Credential information for SMTP server authentication
     *
     */
    @SerializedName("serverCredential")
    @Expose
    private Credential serverCredential;

    /**
     * display name
     *
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * display name
     *
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * The SMTP server address
     *
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * The SMTP server address
     *
     */
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * Unique ID for each smtp server settings
     *
     */
    public String getSmtpServerId() {
        return smtpServerId;
    }

    /**
     * Unique ID for each smtp server settings
     *
     */
    public void setSmtpServerId(String smtpServerId) {
        this.smtpServerId = smtpServerId;
    }

    /**
     * The port number for the communication
     *
     */
    public Integer getServerPort() {
        return serverPort;
    }

    /**
     * The port number for the communication
     *
     */
    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     *
     */
    public Property.FeatureEnabled getUseSsl() {
        return useSsl;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     *
     */
    public void setUseSsl(Property.FeatureEnabled useSsl) {
        this.useSsl = useSsl;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     *
     */
    public Property.FeatureEnabled getServerRequireAuthentication() {
        return serverRequireAuthentication;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     *
     */
    public void setServerRequireAuthentication(Property.FeatureEnabled serverRequireAuthentication) {
        this.serverRequireAuthentication = serverRequireAuthentication;
    }

    public Credential getServerCredential() {
        return serverCredential;
    }

    public void setServerCredential(Credential serverCredential) {
        this.serverCredential = serverCredential;
    }

}
