
package com.hp.ws.cdm.licensing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Comment describing your JSON Schema
 * 
 */
public class Com_hp_cdm_service_licensing_version_1_schema {

    @SerializedName("license")
    @Expose
    private License license;
    @SerializedName("licensePackage")
    @Expose
    private LicensePackage licensePackage;
    @SerializedName("installPackage")
    @Expose
    private InstallPackage installPackage;
    @SerializedName("capabilities")
    @Expose
    private Capabilities capabilities;
    @SerializedName("featureLicenses")
    @Expose
    private FeatureLicenses featureLicenses;
    @SerializedName("solutionLicenses")
    @Expose
    private SolutionLicenses solutionLicenses;
    @SerializedName("factoryLicenses")
    @Expose
    private FactoryLicenses factoryLicenses;

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public LicensePackage getLicensePackage() {
        return licensePackage;
    }

    public void setLicensePackage(LicensePackage licensePackage) {
        this.licensePackage = licensePackage;
    }

    public InstallPackage getInstallPackage() {
        return installPackage;
    }

    public void setInstallPackage(InstallPackage installPackage) {
        this.installPackage = installPackage;
    }

    public Capabilities getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    public FeatureLicenses getFeatureLicenses() {
        return featureLicenses;
    }

    public void setFeatureLicenses(FeatureLicenses featureLicenses) {
        this.featureLicenses = featureLicenses;
    }

    public SolutionLicenses getSolutionLicenses() {
        return solutionLicenses;
    }

    public void setSolutionLicenses(SolutionLicenses solutionLicenses) {
        this.solutionLicenses = solutionLicenses;
    }

    public FactoryLicenses getFactoryLicenses() {
        return factoryLicenses;
    }

    public void setFactoryLicenses(FactoryLicenses factoryLicenses) {
        this.factoryLicenses = factoryLicenses;
    }

}
