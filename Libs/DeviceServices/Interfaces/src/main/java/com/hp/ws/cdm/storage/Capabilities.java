
package com.hp.ws.cdm.storage;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Capabilities {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    /**
     * SecureEraseMethods supported
     * 
     */
    @SerializedName("secureEraseMethodsSupported")
    @Expose
    private List<com.hp.ws.cdm.storage.SecureErase.SecureEraseMethod> secureEraseMethodsSupported = new ArrayList<com.hp.ws.cdm.storage.SecureErase.SecureEraseMethod>();
    /**
     * fileEraseMode Values supported
     * 
     */
    @SerializedName("fileEraseModeValuesSupported")
    @Expose
    private List<com.hp.ws.cdm.storage.SecureErase.SecureEraseMethod> fileEraseModeValuesSupported = new ArrayList<com.hp.ws.cdm.storage.SecureErase.SecureEraseMethod>();

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * SecureEraseMethods supported
     * 
     */
    public List<com.hp.ws.cdm.storage.SecureErase.SecureEraseMethod> getSecureEraseMethodsSupported() {
        return secureEraseMethodsSupported;
    }

    /**
     * SecureEraseMethods supported
     * 
     */
    public void setSecureEraseMethodsSupported(List<com.hp.ws.cdm.storage.SecureErase.SecureEraseMethod> secureEraseMethodsSupported) {
        this.secureEraseMethodsSupported = secureEraseMethodsSupported;
    }

    /**
     * fileEraseMode Values supported
     * 
     */
    public List<com.hp.ws.cdm.storage.SecureErase.SecureEraseMethod> getFileEraseModeValuesSupported() {
        return fileEraseModeValuesSupported;
    }

    /**
     * fileEraseMode Values supported
     * 
     */
    public void setFileEraseModeValuesSupported(List<com.hp.ws.cdm.storage.SecureErase.SecureEraseMethod> fileEraseModeValuesSupported) {
        this.fileEraseModeValuesSupported = fileEraseModeValuesSupported;
    }

}
