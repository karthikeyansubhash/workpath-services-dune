
package com.hp.ws.cdm.email;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class SmtpServer {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("isDefault")
    @Expose
    private Property.FeatureEnabled isDefault;
    /**
     * display name
     * 
     */
    @SerializedName("name")
    @Expose
    private String name;
    /**
     * It is 4 to 8 digit string used for accessing the smtpServer settings when custom Profile is selected
     * 
     */
    @SerializedName("pin")
    @Expose
    private String pin;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("isPinSet")
    @Expose
    private Property.FeatureEnabled isPinSet;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("includeSenderForEmails")
    @Expose
    private Property.FeatureEnabled includeSenderForEmails;
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
     * Email address for verify access and also used for from if profile exist
     * 
     */
    @SerializedName("emailAddress")
    @Expose
    private String emailAddress;
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
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("validateServerCertificate")
    @Expose
    private Property.FeatureEnabled validateServerCertificate;
    @SerializedName("credential")
    @Expose
    private Credential credential;
    /**
     * Maximum file size allowed by SMTP server.
     * 
     */
    @SerializedName("fileSize")
    @Expose
    private Integer fileSize = -1;
    @SerializedName("serverUsage")
    @Expose
    private ServerUsage serverUsage;

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getIsDefault() {
        return isDefault;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIsDefault(Property.FeatureEnabled isDefault) {
        this.isDefault = isDefault;
    }

    /**
     * display name
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * display name
     * 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * It is 4 to 8 digit string used for accessing the smtpServer settings when custom Profile is selected
     * 
     */
    public String getPin() {
        return pin;
    }

    /**
     * It is 4 to 8 digit string used for accessing the smtpServer settings when custom Profile is selected
     * 
     */
    public void setPin(String pin) {
        this.pin = pin;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getIsPinSet() {
        return isPinSet;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIsPinSet(Property.FeatureEnabled isPinSet) {
        this.isPinSet = isPinSet;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getIncludeSenderForEmails() {
        return includeSenderForEmails;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIncludeSenderForEmails(Property.FeatureEnabled includeSenderForEmails) {
        this.includeSenderForEmails = includeSenderForEmails;
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
     * Email address for verify access and also used for from if profile exist
     * 
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Email address for verify access and also used for from if profile exist
     * 
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getValidateServerCertificate() {
        return validateServerCertificate;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setValidateServerCertificate(Property.FeatureEnabled validateServerCertificate) {
        this.validateServerCertificate = validateServerCertificate;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    /**
     * Maximum file size allowed by SMTP server.
     * 
     */
    public Integer getFileSize() {
        return fileSize;
    }

    /**
     * Maximum file size allowed by SMTP server.
     * 
     */
    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public ServerUsage getServerUsage() {
        return serverUsage;
    }

    public void setServerUsage(ServerUsage serverUsage) {
        this.serverUsage = serverUsage;
    }

}
