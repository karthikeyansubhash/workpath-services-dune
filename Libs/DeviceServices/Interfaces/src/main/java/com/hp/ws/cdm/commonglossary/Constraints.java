
package com.hp.ws.cdm.commonglossary;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Constraints {

    @SerializedName("version")
    @Expose
    private String version;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("validators")
    @Expose
    private List<Validator> validators = new ArrayList<Validator>();

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * 
     * (Required)
     * 
     */
    public List<Validator> getValidators() {
        return validators;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setValidators(List<Validator> validators) {
        this.validators = validators;
    }

}
