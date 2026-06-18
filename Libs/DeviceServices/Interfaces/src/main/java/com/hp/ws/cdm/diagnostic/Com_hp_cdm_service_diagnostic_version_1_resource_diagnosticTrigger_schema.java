
package com.hp.ws.cdm.diagnostic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Schema for Dune IDiagnostic interaction
 * 
 */
public class Com_hp_cdm_service_diagnostic_version_1_resource_diagnosticTrigger_schema {

    /**
     * Contains the list of tests
     * 
     */
    @SerializedName("testsList")
    @Expose
    private TestsList testsList;
    /**
     * Contains the list of subsystems
     * 
     */
    @SerializedName("subsystemsList")
    @Expose
    private SubsystemsList subsystemsList;
    /**
     * Contains the list of categories
     * 
     */
    @SerializedName("categoriesList")
    @Expose
    private CategoriesList categoriesList;

    /**
     * Contains the list of tests
     * 
     */
    public TestsList getTestsList() {
        return testsList;
    }

    /**
     * Contains the list of tests
     * 
     */
    public void setTestsList(TestsList testsList) {
        this.testsList = testsList;
    }

    /**
     * Contains the list of subsystems
     * 
     */
    public SubsystemsList getSubsystemsList() {
        return subsystemsList;
    }

    /**
     * Contains the list of subsystems
     * 
     */
    public void setSubsystemsList(SubsystemsList subsystemsList) {
        this.subsystemsList = subsystemsList;
    }

    /**
     * Contains the list of categories
     * 
     */
    public CategoriesList getCategoriesList() {
        return categoriesList;
    }

    /**
     * Contains the list of categories
     * 
     */
    public void setCategoriesList(CategoriesList categoriesList) {
        this.categoriesList = categoriesList;
    }

}
