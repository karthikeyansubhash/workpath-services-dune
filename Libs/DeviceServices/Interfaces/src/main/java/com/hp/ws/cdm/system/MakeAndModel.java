
package com.hp.ws.cdm.system;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * resource to retrieve the device's model information
 * 
 */
public class MakeAndModel {

    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("family")
    @Expose
    private String family;
    @SerializedName("name")
    @Expose
    private String name;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
