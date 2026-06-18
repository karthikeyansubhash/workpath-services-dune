
package com.hp.ws.cdm.email;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Com_hp_cdm_service_email_version_1_schema {

    @SerializedName("smtpServers")
    @Expose
    private SmtpServers smtpServers;
    @SerializedName("capabilities")
    @Expose
    private Capabilities capabilities;
    @SerializedName("verifyAccess")
    @Expose
    private VerifyAccess verifyAccess;
    @SerializedName("validatePin")
    @Expose
    private ValidatePin validatePin;
    @SerializedName("allowedEmailDomains")
    @Expose
    private AllowedEmailDomains allowedEmailDomains;

    public SmtpServers getSmtpServers() {
        return smtpServers;
    }

    public void setSmtpServers(SmtpServers smtpServers) {
        this.smtpServers = smtpServers;
    }

    public Capabilities getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    public VerifyAccess getVerifyAccess() {
        return verifyAccess;
    }

    public void setVerifyAccess(VerifyAccess verifyAccess) {
        this.verifyAccess = verifyAccess;
    }

    public ValidatePin getValidatePin() {
        return validatePin;
    }

    public void setValidatePin(ValidatePin validatePin) {
        this.validatePin = validatePin;
    }

    public AllowedEmailDomains getAllowedEmailDomains() {
        return allowedEmailDomains;
    }

    public void setAllowedEmailDomains(AllowedEmailDomains allowedEmailDomains) {
        this.allowedEmailDomains = allowedEmailDomains;
    }

}
