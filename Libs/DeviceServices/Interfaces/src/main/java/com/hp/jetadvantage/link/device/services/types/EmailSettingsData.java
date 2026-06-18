package com.hp.jetadvantage.link.device.services.types;

import com.google.gson.annotations.SerializedName;

public class EmailSettingsData {
    @SerializedName("isSmtpConfigured")
    private Boolean mSendToEmailEnabled;

    @SerializedName("hostName")
    private String mSmtpServerHostName;

    @SerializedName("port")
    private Integer mSmtpServerPort;

    @SerializedName("authenticationRequired")
    private Boolean mAuthenticationRequired;

    @SerializedName("userName")
    private String mSmtpServerUserName;

    @SerializedName("password")
    private String mSmtpServerPassword;

    @SerializedName("useSsl")
    private Boolean mUseSSL;

    @SerializedName("defaultFromAddress")
    private EmailContact mDefaultFrom;

    @SerializedName("displayName")
    private String mFromDisplayName;

    @SerializedName("emailSummary")
    private String mDefaultMessage;

    public EmailSettingsData() {
        mDefaultFrom = new EmailContact();
    }

    public void setSendToEmailEnabled(boolean sendToEmailEnabled) {
        this.mSendToEmailEnabled = sendToEmailEnabled;
    }

    public boolean isSendToEmailEnabled() {
        return mSendToEmailEnabled;
    }

    public void setSmtpServerHostName(String smtpServerHostName) {
        this.mSmtpServerHostName = smtpServerHostName;
    }

    public String getSmtpServerHostName() {
        return mSmtpServerHostName;
    }

    public void setSmtpServerPort(Integer smtpServerPort) {
        this.mSmtpServerPort = smtpServerPort;
    }

    public Integer getSmtpServerPort() {
        return mSmtpServerPort;
    }

    public void setAuthenticationRequired(Boolean authenticationRequired) {
        this.mAuthenticationRequired = authenticationRequired;
    }

    public Boolean getAuthenticationRequired() {
        return mAuthenticationRequired;
    }

    public void setSmtpServerUserName(String smtpServerUserName) {
        this.mSmtpServerUserName = smtpServerUserName;
    }

    public String getSmtpServerUserName() {
        return mSmtpServerUserName;
    }

    public void setSmtpServerPassword(String smtpServerPassword) {
        this.mSmtpServerPassword = smtpServerPassword;
    }

    public String getSmtpServerPassword() {
        return mSmtpServerPassword;
    }

    public void setUseSSL(Boolean useSSL) {
        this.mUseSSL = useSSL;
    }

    public Boolean getUseSSL() {
        return mUseSSL;
    }

    public void setEmailAddressForDefaultFrom(String emailAddress) {
            mDefaultFrom.setEmailAddress(emailAddress);
    }

    public void setDisplayNameForDefaultFrom(String displayName) {
        mDefaultFrom.setDisplayName(displayName);
    }

    public EmailContact getDefaultFrom() {
        return mDefaultFrom;
    }

    public void setFromDisplayName(String fromDisplayName) {
        this.mFromDisplayName = fromDisplayName;
    }

    public String getFromDisplayName() {
        return mFromDisplayName;
    }

    public void setDefaultMessage(String defaultMessage) {
        this.mDefaultMessage = defaultMessage;
    }

    public String getDefaultMessage() {
        return mDefaultMessage;
    }
}
