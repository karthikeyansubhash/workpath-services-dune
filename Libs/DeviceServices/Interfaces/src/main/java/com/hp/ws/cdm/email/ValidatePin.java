
package com.hp.ws.cdm.email;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValidatePin {

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
     * Unique ID for each smtp server settings
     * 
     */
    @SerializedName("smtpServerId")
    @Expose
    private String smtpServerId;
    /**
     * It is 4 to 8 digit string used for accessing the smtpServer settings when custom Profile is selected
     * 
     */
    @SerializedName("pin")
    @Expose
    private String pin;

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

}
