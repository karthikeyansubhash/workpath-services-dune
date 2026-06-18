
package com.hp.ws.cdm.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;
import com.hp.ws.cdm.commonglossary.Property;

public class VerifyAccess {

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
     * Email address for test  
     * 
     */
    @SerializedName("testEmailAddress")
    @Expose
    private String testEmailAddress;
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
     * SMTP operation state
     * 
     */
    @SerializedName("state")
    @Expose
    private State state;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
    @SerializedName("lastResult")
    @Expose
    private Result lastResult;
    /**
     * UTF format date time stamp of the last test completion
     * 
     */
    @SerializedName("resultTimestamp")
    @Expose
    private String resultTimestamp;

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
     * Email address for test  
     * 
     */
    public String getTestEmailAddress() {
        return testEmailAddress;
    }

    /**
     * Email address for test  
     * 
     */
    public void setTestEmailAddress(String testEmailAddress) {
        this.testEmailAddress = testEmailAddress;
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
     * SMTP operation state
     * 
     */
    public State getState() {
        return state;
    }

    /**
     * SMTP operation state
     * 
     */
    public void setState(State state) {
        this.state = state;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public Result getLastResult() {
        return lastResult;
    }

    public void setLastResult(Result lastResult) {
        this.lastResult = lastResult;
    }

    /**
     * UTF format date time stamp of the last test completion
     * 
     */
    public String getResultTimestamp() {
        return resultTimestamp;
    }

    /**
     * UTF format date time stamp of the last test completion
     * 
     */
    public void setResultTimestamp(String resultTimestamp) {
        this.resultTimestamp = resultTimestamp;
    }


    /**
     * SMTP operation state
     * 
     */
    public enum State {

        @SerializedName("processing")
        PROCESSING("processing"),
        @SerializedName("cancelProcessing")
        CANCEL_PROCESSING("cancelProcessing"),
        @SerializedName("idle")
        IDLE("idle");
        private final String value;
        private final static Map<String, State> CONSTANTS = new HashMap<String, State>();

        static {
            for (State c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        State(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static State fromValue(String value) {
            State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
