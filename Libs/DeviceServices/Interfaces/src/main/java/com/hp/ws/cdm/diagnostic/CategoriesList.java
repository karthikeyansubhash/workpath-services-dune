
package com.hp.ws.cdm.diagnostic;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Contains the list of categories
 * 
 */
public class CategoriesList {

    @SerializedName("item")
    @Expose
    private List<CategoryInformation> item = new ArrayList<CategoryInformation>();

    public List<CategoryInformation> getItem() {
        return item;
    }

    public void setItem(List<CategoryInformation> item) {
        this.item = item;
    }

}
