
package com.hp.ws.cdm.certificate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Com_hp_cdm_service_certificate_version_1_schema {

    @SerializedName("capabilities")
    @Expose
    private Capabilities capabilities;
    @SerializedName("certificates")
    @Expose
    private Certificates certificates;
    @SerializedName("certificateDetails")
    @Expose
    private CertificateDetails certificateDetails;
    @SerializedName("certificateSigningRequest")
    @Expose
    private SelfSignedCertificate certificateSigningRequest;
    @SerializedName("selfSignedCertificate")
    @Expose
    private CertificateOperation selfSignedCertificate;

    public Capabilities getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    public Certificates getCertificates() {
        return certificates;
    }

    public void setCertificates(Certificates certificates) {
        this.certificates = certificates;
    }

    public CertificateDetails getCertificateDetails() {
        return certificateDetails;
    }

    public void setCertificateDetails(CertificateDetails certificateDetails) {
        this.certificateDetails = certificateDetails;
    }

    public SelfSignedCertificate getCertificateSigningRequest() {
        return certificateSigningRequest;
    }

    public void setCertificateSigningRequest(SelfSignedCertificate certificateSigningRequest) {
        this.certificateSigningRequest = certificateSigningRequest;
    }

    public CertificateOperation getSelfSignedCertificate() {
        return selfSignedCertificate;
    }

    public void setSelfSignedCertificate(CertificateOperation selfSignedCertificate) {
        this.selfSignedCertificate = selfSignedCertificate;
    }

}
