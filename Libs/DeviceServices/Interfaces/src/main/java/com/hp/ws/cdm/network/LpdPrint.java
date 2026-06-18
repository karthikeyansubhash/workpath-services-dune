
package com.hp.ws.cdm.network;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class LpdPrint {

    @SerializedName("customQueues")
    @Expose
    private List<LpdQueue> customQueues = new ArrayList<LpdQueue>();
    @SerializedName("defaultQueue")
    @Expose
    private String defaultQueue;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("enabled")
    @Expose
    private Property.FeatureEnabled enabled;
    @SerializedName("maxCustomQueues")
    @Expose
    private Integer maxCustomQueues;
    @SerializedName("maxStringValues")
    @Expose
    private Integer maxStringValues;
    @SerializedName("stringValues")
    @Expose
    private List<LpdStringValue> stringValues = new ArrayList<LpdStringValue>();
    @SerializedName("systemQueues")
    @Expose
    private List<LpdQueue> systemQueues = new ArrayList<LpdQueue>();

    public List<LpdQueue> getCustomQueues() {
        return customQueues;
    }

    public void setCustomQueues(List<LpdQueue> customQueues) {
        this.customQueues = customQueues;
    }

    public String getDefaultQueue() {
        return defaultQueue;
    }

    public void setDefaultQueue(String defaultQueue) {
        this.defaultQueue = defaultQueue;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEnabled() {
        return enabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEnabled(Property.FeatureEnabled enabled) {
        this.enabled = enabled;
    }

    public Integer getMaxCustomQueues() {
        return maxCustomQueues;
    }

    public void setMaxCustomQueues(Integer maxCustomQueues) {
        this.maxCustomQueues = maxCustomQueues;
    }

    public Integer getMaxStringValues() {
        return maxStringValues;
    }

    public void setMaxStringValues(Integer maxStringValues) {
        this.maxStringValues = maxStringValues;
    }

    public List<LpdStringValue> getStringValues() {
        return stringValues;
    }

    public void setStringValues(List<LpdStringValue> stringValues) {
        this.stringValues = stringValues;
    }

    public List<LpdQueue> getSystemQueues() {
        return systemQueues;
    }

    public void setSystemQueues(List<LpdQueue> systemQueues) {
        this.systemQueues = systemQueues;
    }

}
