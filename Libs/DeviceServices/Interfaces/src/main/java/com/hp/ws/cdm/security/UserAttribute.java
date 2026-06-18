
package com.hp.ws.cdm.security;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAttribute {

    /**
     * The attribute name.
     * (Required)
     * 
     */
    @SerializedName("attributeName")
    @Expose
    private String attributeName;
    /**
     * The attribute value
     * (Required)
     * 
     */
    @SerializedName("attributeValue")
    @Expose
    private String attributeValue;

    /**
     * The attribute name.
     * (Required)
     * 
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * The attribute name.
     * (Required)
     * 
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     * The attribute value
     * (Required)
     * 
     */
    public String getAttributeValue() {
        return attributeValue;
    }

    /**
     * The attribute value
     * (Required)
     * 
     */
    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

}
