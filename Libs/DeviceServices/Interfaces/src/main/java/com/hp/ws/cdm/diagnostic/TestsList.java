
package com.hp.ws.cdm.diagnostic;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Contains the list of tests
 * 
 */
public class TestsList {

    @SerializedName("item")
    @Expose
    private List<TestInformation> item = new ArrayList<TestInformation>();

    public List<TestInformation> getItem() {
        return item;
    }

    public void setItem(List<TestInformation> item) {
        this.item = item;
    }

}
