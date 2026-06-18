
package com.hp.ws.cdm.security;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Header {

    /**
     * The header name
     * 
     */
    @SerializedName("name")
    @Expose
    private String name;
    /**
     * The header value
     * 
     */
    @SerializedName("value")
    @Expose
    private String value;

    /**
     * The header name
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * The header name
     * 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The header value
     * 
     */
    public String getValue() {
        return value;
    }

    /**
     * The header value
     * 
     */
    public void setValue(String value) {
        this.value = value;
    }

}
