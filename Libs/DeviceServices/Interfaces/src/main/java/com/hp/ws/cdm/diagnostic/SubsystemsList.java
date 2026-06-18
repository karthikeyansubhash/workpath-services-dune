
package com.hp.ws.cdm.diagnostic;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Contains the list of subsystems
 * 
 */
public class SubsystemsList {

    @SerializedName("item")
    @Expose
    private List<SubsystemInformation> item = new ArrayList<SubsystemInformation>();

    public List<SubsystemInformation> getItem() {
        return item;
    }

    public void setItem(List<SubsystemInformation> item) {
        this.item = item;
    }

}
