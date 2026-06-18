
package com.hp.ws.cdm.commonglossary;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Supported {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("value")
    @Expose
    private Property value;
    @SerializedName("data")
    @Expose
    private List<Validator> data = new ArrayList<Validator>();

    /**
     * 
     * (Required)
     * 
     */
    public Property getValue() {
        return value;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setValue(Property value) {
        this.value = value;
    }

    public List<Validator> getData() {
        return data;
    }

    public void setData(List<Validator> data) {
        this.data = data;
    }

}
