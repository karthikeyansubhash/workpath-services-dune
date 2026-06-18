
package com.hp.ws.cdm.controlpanel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class AlternateKeyboard {

    /**
     * The set of possible keyboard layouts
     * 
     */
    @SerializedName("keyboardLayout")
    @Expose
    private com.hp.ws.cdm.controlpanel.Configuration.KeyboardLayout keyboardLayout;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("enabled")
    @Expose
    private Property.FeatureEnabled enabled;

    /**
     * The set of possible keyboard layouts
     * 
     */
    public com.hp.ws.cdm.controlpanel.Configuration.KeyboardLayout getKeyboardLayout() {
        return keyboardLayout;
    }

    /**
     * The set of possible keyboard layouts
     * 
     */
    public void setKeyboardLayout(com.hp.ws.cdm.controlpanel.Configuration.KeyboardLayout keyboardLayout) {
        this.keyboardLayout = keyboardLayout;
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

}
