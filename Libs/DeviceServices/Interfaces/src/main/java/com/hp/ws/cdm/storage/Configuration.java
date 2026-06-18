
package com.hp.ws.cdm.storage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Configuration {

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
     * The type of Secure Erase methods
     * 
     */
    @SerializedName("fileEraseMode")
    @Expose
    private com.hp.ws.cdm.storage.SecureErase.SecureEraseMethod fileEraseMode;

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
     * The type of Secure Erase methods
     * 
     */
    public com.hp.ws.cdm.storage.SecureErase.SecureEraseMethod getFileEraseMode() {
        return fileEraseMode;
    }

    /**
     * The type of Secure Erase methods
     * 
     */
    public void setFileEraseMode(com.hp.ws.cdm.storage.SecureErase.SecureEraseMethod fileEraseMode) {
        this.fileEraseMode = fileEraseMode;
    }

}
