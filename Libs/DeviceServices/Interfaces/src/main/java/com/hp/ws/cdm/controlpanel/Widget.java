
package com.hp.ws.cdm.controlpanel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class Widget {

    /**
     * unique identifier of the widget, this will be GUID
     * 
     */
    @SerializedName("id")
    @Expose
    private String id;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("visible")
    @Expose
    private Property.FeatureEnabled visible;

    /**
     * unique identifier of the widget, this will be GUID
     * 
     */
    public String getId() {
        return id;
    }

    /**
     * unique identifier of the widget, this will be GUID
     * 
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getVisible() {
        return visible;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setVisible(Property.FeatureEnabled visible) {
        this.visible = visible;
    }

}
