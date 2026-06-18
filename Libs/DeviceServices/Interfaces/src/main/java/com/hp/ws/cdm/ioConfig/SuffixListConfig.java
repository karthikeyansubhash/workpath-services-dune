
package com.hp.ws.cdm.ioconfig;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * DNS suffix configuration
 * 
 */
public class SuffixListConfig {

    @SerializedName("suffixes")
    @Expose
    private List<NameConfig> suffixes = new ArrayList<NameConfig>();

    public List<NameConfig> getSuffixes() {
        return suffixes;
    }

    public void setSuffixes(List<NameConfig> suffixes) {
        this.suffixes = suffixes;
    }

}
