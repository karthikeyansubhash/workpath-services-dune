
package com.hp.ws.cdm.controlpanel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LanguageDefaultKeyboardMapping {

    /**
     * language code as per ISO 639-1. The country extension should only be used when variants or dialects are supported. This list should only have the subset of languages that are actuall supported
     * 
     */
    @SerializedName("language")
    @Expose
    private com.hp.ws.cdm.controlpanel.Configuration.Language language;
    /**
     * The set of possible keyboard layouts
     * 
     */
    @SerializedName("keyboardLayout")
    @Expose
    private com.hp.ws.cdm.controlpanel.Configuration.KeyboardLayout keyboardLayout;

    /**
     * language code as per ISO 639-1. The country extension should only be used when variants or dialects are supported. This list should only have the subset of languages that are actuall supported
     * 
     */
    public com.hp.ws.cdm.controlpanel.Configuration.Language getLanguage() {
        return language;
    }

    /**
     * language code as per ISO 639-1. The country extension should only be used when variants or dialects are supported. This list should only have the subset of languages that are actuall supported
     * 
     */
    public void setLanguage(com.hp.ws.cdm.controlpanel.Configuration.Language language) {
        this.language = language;
    }

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

}
