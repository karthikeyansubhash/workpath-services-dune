
package com.hp.ws.cdm.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LpdStringValue {

    @SerializedName("stringName")
    @Expose
    private String stringName;
    @SerializedName("stringValue")
    @Expose
    private String stringValue;

    public String getStringName() {
        return stringName;
    }

    public void setStringName(String stringName) {
        this.stringName = stringName;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

}
