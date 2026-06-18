
package com.hp.ws.cdm.security;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * The credentials newly acquired. The field of this object depends on the type of agent and/or credentialInfo.
 * 
 */
public class Credentials {

    /**
     * The domain for the credentials to authenticate against
     * 
     */
    @SerializedName("domain")
    @Expose
    private String domain;
    /**
     * The username of the credentials to authenticate with
     * 
     */
    @SerializedName("username")
    @Expose
    private String username;
    /**
     * The password for the username to authenticate with
     * 
     */
    @SerializedName("password")
    @Expose
    private String password;
    /**
     * The pin to authenticate with
     * 
     */
    @SerializedName("pin")
    @Expose
    private String pin;

    /**
     * The domain for the credentials to authenticate against
     * 
     */
    public String getDomain() {
        return domain;
    }

    /**
     * The domain for the credentials to authenticate against
     * 
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * The username of the credentials to authenticate with
     * 
     */
    public String getUsername() {
        return username;
    }

    /**
     * The username of the credentials to authenticate with
     * 
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * The password for the username to authenticate with
     * 
     */
    public String getPassword() {
        return password;
    }

    /**
     * The password for the username to authenticate with
     * 
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * The pin to authenticate with
     * 
     */
    public String getPin() {
        return pin;
    }

    /**
     * The pin to authenticate with
     * 
     */
    public void setPin(String pin) {
        this.pin = pin;
    }

}
