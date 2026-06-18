
package com.hp.ws.cdm.commonglossary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Constraints definitions - moving to beta for ratification
 * 
 */
public class Com_hp_cdm_service_validator_version_1_schema {

    @SerializedName("constraints")
    @Expose
    private Constraints constraints;

    public Constraints getConstraints() {
        return constraints;
    }

    public void setConstraints(Constraints constraints) {
        this.constraints = constraints;
    }

}
