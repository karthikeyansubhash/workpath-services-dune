
package com.hp.ws.cdm.jobmanagement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

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
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("pauseQueueSupported")
    @Expose
    private Property.FeatureEnabled pauseQueueSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("pauseJobSupported")
    @Expose
    private Property.FeatureEnabled pauseJobSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("reprintJobSupported")
    @Expose
    private Property.FeatureEnabled reprintJobSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("resendJobSupported")
    @Expose
    private Property.FeatureEnabled resendJobSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("previewThumbnailSupported")
    @Expose
    private Property.FeatureEnabled previewThumbnailSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("exportJobHistorySupported")
    @Expose
    private Property.FeatureEnabled exportJobHistorySupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("promoteJobSupported")
    @Expose
    private Property.FeatureEnabled promoteJobSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("jobOnHoldSupported")
    @Expose
    private Property.FeatureEnabled jobOnHoldSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("jobOnHoldManualReleaseSupported")
    @Expose
    private Property.FeatureEnabled jobOnHoldManualReleaseSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("jobConcurrencySupported")
    @Expose
    private Property.FeatureEnabled jobConcurrencySupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("priorityModeSupported")
    @Expose
    private Property.FeatureEnabled priorityModeSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("jobOnHoldAccountIdSupported")
    @Expose
    private Property.FeatureEnabled jobOnHoldAccountIdSupported;

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
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getPauseQueueSupported() {
        return pauseQueueSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPauseQueueSupported(Property.FeatureEnabled pauseQueueSupported) {
        this.pauseQueueSupported = pauseQueueSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getPauseJobSupported() {
        return pauseJobSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPauseJobSupported(Property.FeatureEnabled pauseJobSupported) {
        this.pauseJobSupported = pauseJobSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getReprintJobSupported() {
        return reprintJobSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setReprintJobSupported(Property.FeatureEnabled reprintJobSupported) {
        this.reprintJobSupported = reprintJobSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getResendJobSupported() {
        return resendJobSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setResendJobSupported(Property.FeatureEnabled resendJobSupported) {
        this.resendJobSupported = resendJobSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getPreviewThumbnailSupported() {
        return previewThumbnailSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPreviewThumbnailSupported(Property.FeatureEnabled previewThumbnailSupported) {
        this.previewThumbnailSupported = previewThumbnailSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getExportJobHistorySupported() {
        return exportJobHistorySupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setExportJobHistorySupported(Property.FeatureEnabled exportJobHistorySupported) {
        this.exportJobHistorySupported = exportJobHistorySupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getPromoteJobSupported() {
        return promoteJobSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPromoteJobSupported(Property.FeatureEnabled promoteJobSupported) {
        this.promoteJobSupported = promoteJobSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getJobOnHoldSupported() {
        return jobOnHoldSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setJobOnHoldSupported(Property.FeatureEnabled jobOnHoldSupported) {
        this.jobOnHoldSupported = jobOnHoldSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getJobOnHoldManualReleaseSupported() {
        return jobOnHoldManualReleaseSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setJobOnHoldManualReleaseSupported(Property.FeatureEnabled jobOnHoldManualReleaseSupported) {
        this.jobOnHoldManualReleaseSupported = jobOnHoldManualReleaseSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getJobConcurrencySupported() {
        return jobConcurrencySupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setJobConcurrencySupported(Property.FeatureEnabled jobConcurrencySupported) {
        this.jobConcurrencySupported = jobConcurrencySupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getPriorityModeSupported() {
        return priorityModeSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPriorityModeSupported(Property.FeatureEnabled priorityModeSupported) {
        this.priorityModeSupported = priorityModeSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getJobOnHoldAccountIdSupported() {
        return jobOnHoldAccountIdSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setJobOnHoldAccountIdSupported(Property.FeatureEnabled jobOnHoldAccountIdSupported) {
        this.jobOnHoldAccountIdSupported = jobOnHoldAccountIdSupported;
    }

}
