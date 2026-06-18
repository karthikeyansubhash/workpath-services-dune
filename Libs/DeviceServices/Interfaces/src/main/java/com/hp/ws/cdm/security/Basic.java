
package com.hp.ws.cdm.security;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Indicates to use basic authentication.
 * 
 */
public class Basic {

    /**
     * The basic auth user name
     * 
     */
    @SerializedName("username")
    @Expose
    private String username;
    /**
     * The basic auth password
     * 
     */
    @SerializedName("password")
    @Expose
    private String password;

    /**
     * The basic auth user name
     * 
     */
    public String getUsername() {
        return username;
    }

    /**
     * The basic auth user name
     * 
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * The basic auth password
     * 
     */
    public String getPassword() {
        return password;
    }

    /**
     * The basic auth password
     * 
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
