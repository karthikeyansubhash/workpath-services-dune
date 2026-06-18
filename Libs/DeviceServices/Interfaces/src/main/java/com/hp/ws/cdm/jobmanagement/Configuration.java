
package com.hp.ws.cdm.jobmanagement;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

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
     * Policy to apply when a job recover is called.
     * 
     */
    @SerializedName("jobRecoveryPolicy")
    @Expose
    private Configuration.JobRecoveryPolicy jobRecoveryPolicy;
    /**
     * Delay time to cancel a job on hold (in hours).
     * 
     */
    @SerializedName("cancelJobOnHoldDelayInHours")
    @Expose
    private Integer cancelJobOnHoldDelayInHours;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("hideDeletedJobs")
    @Expose
    private Property.FeatureEnabled hideDeletedJobs;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("holdJobsForManualRelease")
    @Expose
    private Property.FeatureEnabled holdJobsForManualRelease;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("reprintResendJobsEnabled")
    @Expose
    private Property.FeatureEnabled reprintResendJobsEnabled;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("holdJobsForAccountId")
    @Expose
    private Property.FeatureEnabled holdJobsForAccountId;

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
     * Policy to apply when a job recover is called.
     * 
     */
    public Configuration.JobRecoveryPolicy getJobRecoveryPolicy() {
        return jobRecoveryPolicy;
    }

    /**
     * Policy to apply when a job recover is called.
     * 
     */
    public void setJobRecoveryPolicy(Configuration.JobRecoveryPolicy jobRecoveryPolicy) {
        this.jobRecoveryPolicy = jobRecoveryPolicy;
    }

    /**
     * Delay time to cancel a job on hold (in hours).
     * 
     */
    public Integer getCancelJobOnHoldDelayInHours() {
        return cancelJobOnHoldDelayInHours;
    }

    /**
     * Delay time to cancel a job on hold (in hours).
     * 
     */
    public void setCancelJobOnHoldDelayInHours(Integer cancelJobOnHoldDelayInHours) {
        this.cancelJobOnHoldDelayInHours = cancelJobOnHoldDelayInHours;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getHideDeletedJobs() {
        return hideDeletedJobs;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setHideDeletedJobs(Property.FeatureEnabled hideDeletedJobs) {
        this.hideDeletedJobs = hideDeletedJobs;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getHoldJobsForManualRelease() {
        return holdJobsForManualRelease;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setHoldJobsForManualRelease(Property.FeatureEnabled holdJobsForManualRelease) {
        this.holdJobsForManualRelease = holdJobsForManualRelease;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getReprintResendJobsEnabled() {
        return reprintResendJobsEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setReprintResendJobsEnabled(Property.FeatureEnabled reprintResendJobsEnabled) {
        this.reprintResendJobsEnabled = reprintResendJobsEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getHoldJobsForAccountId() {
        return holdJobsForAccountId;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setHoldJobsForAccountId(Property.FeatureEnabled holdJobsForAccountId) {
        this.holdJobsForAccountId = holdJobsForAccountId;
    }


    /**
     * Policy to apply when a job recover is called.
     * 
     */
    public enum JobRecoveryPolicy {

        @SerializedName("putOnHold")
        PUT_ON_HOLD("putOnHold"),
        @SerializedName("cancelAndDeleteContents")
        CANCEL_AND_DELETE_CONTENTS("cancelAndDeleteContents"),
        @SerializedName("cancel")
        CANCEL("cancel");
        private final String value;
        private final static Map<String, Configuration.JobRecoveryPolicy> CONSTANTS = new HashMap<String, Configuration.JobRecoveryPolicy>();

        static {
            for (Configuration.JobRecoveryPolicy c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        JobRecoveryPolicy(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Configuration.JobRecoveryPolicy fromValue(String value) {
            Configuration.JobRecoveryPolicy constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
