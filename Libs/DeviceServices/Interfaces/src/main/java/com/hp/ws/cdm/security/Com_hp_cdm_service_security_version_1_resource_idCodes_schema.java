
package com.hp.ws.cdm.security;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Com_hp_cdm_service_security_version_1_resource_idCodes_schema {

    @SerializedName("idCodeConfig")
    @Expose
    private IdCodeConfig idCodeConfig;
    @SerializedName("idCodes")
    @Expose
    private IdCodes idCodes;
    @SerializedName("idCode")
    @Expose
    private IdCode idCode;

    public IdCodeConfig getIdCodeConfig() {
        return idCodeConfig;
    }

    public void setIdCodeConfig(IdCodeConfig idCodeConfig) {
        this.idCodeConfig = idCodeConfig;
    }

    public IdCodes getIdCodes() {
        return idCodes;
    }

    public void setIdCodes(IdCodes idCodes) {
        this.idCodes = idCodes;
    }

    public IdCode getIdCode() {
        return idCode;
    }

    public void setIdCode(IdCode idCode) {
        this.idCode = idCode;
    }

}
