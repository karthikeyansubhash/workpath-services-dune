
package com.hp.ws.cdm.security;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Information to authenticate the request.
 * 
 */
public class Authentication {

    /**
     * Indicates to use basic authentication.
     * 
     */
    @SerializedName("basic")
    @Expose
    private Basic basic;

    /**
     * Indicates to use basic authentication.
     * 
     */
    public Basic getBasic() {
        return basic;
    }

    /**
     * Indicates to use basic authentication.
     * 
     */
    public void setBasic(Basic basic) {
        this.basic = basic;
    }

}
