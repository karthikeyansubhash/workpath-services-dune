
package com.hp.ws.cdm.jobmanagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;
import com.hp.ws.cdm.commonglossary.Property;

public class Job {

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
    @SerializedName("isPrivateJob")
    @Expose
    private Property.FeatureEnabled isPrivateJob;
    @SerializedName("state")
    @Expose
    private Job.State state;
    @SerializedName("processingSubState")
    @Expose
    private Job.ProcessingSubState processingSubState;
    /**
     * completion state of the job
     * 
     */
    @SerializedName("completionState")
    @Expose
    private Job.JobCompletionState completionState;
    /**
     * The completion code or cancel/failure reason of the job. For Example, memory out, job corruption etc.
     * 
     */
    @SerializedName("completionCode")
    @Expose
    private String completionCode;
    /**
     * Brief description of the cancel/failure reason of the job. This property will be server-side localized to the language specified in the Accept-Language header of the request.
     * 
     */
    @SerializedName("localizedCompletionText")
    @Expose
    private String localizedCompletionText;
    /**
     * unique identifier
     * 
     */
    @SerializedName("jobId")
    @Expose
    private String jobId;
    @SerializedName("jobName")
    @Expose
    private String jobName;
    @SerializedName("jobType")
    @Expose
    private Job.JobType jobType;
    /**
     * The username of the person who owns this job.
     * 
     */
    @SerializedName("userName")
    @Expose
    private String userName;
    /**
     * The reason why a job was paused
     * 
     */
    @SerializedName("pauseReason")
    @Expose
    private Job.PauseReason pauseReason;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("jobDelayedDuringExecution")
    @Expose
    private Property.FeatureEnabled jobDelayedDuringExecution;
    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    @SerializedName("startTime")
    @Expose
    private Date startTime;
    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    @SerializedName("completionTime")
    @Expose
    private Date completionTime;
    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    @SerializedName("scheduledTime")
    @Expose
    private Date scheduledTime;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("autoStart")
    @Expose
    private Property.FeatureEnabled autoStart;
    /**
     * This is ticketId to use when creating a job; ticket located at /cdm/jobTicket/v1/tickets/<ticketId>
     * 
     */
    @SerializedName("ticketId")
    @Expose
    private String ticketId;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("reprintJobAvailable")
    @Expose
    private Property.FeatureEnabled reprintJobAvailable;
    /**
     * Clients can use this property to sort the jobs in the /queue.  Higher priority jobs will have a smaller sortOrder value.
     * 
     */
    @SerializedName("sortOrder")
    @Expose
    private Integer sortOrder;
    @SerializedName("cancelMode")
    @Expose
    private Job.CancelMode cancelMode;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("jobInFinalStage")
    @Expose
    private Property.FeatureEnabled jobInFinalStage;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("isJobPromotable")
    @Expose
    private Property.FeatureEnabled isJobPromotable;
    /**
     * The sessionId for the priorityMode session. If not empty then it means that job has been started by with client holding priority mode session.
     * 
     */
    @SerializedName("priorityModeSessionId")
    @Expose
    private String priorityModeSessionId;
    /**
     * normal: not stored job, store: original stored job, retrieve: a retrieved stored job
     * 
     */
    @SerializedName("executionMode")
    @Expose
    private Job.ExecutionMode executionMode;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
    /**
     * Unique jobId from the perspective of the client sending the job.
     * 
     */
    @SerializedName("clientJobId")
    @Expose
    private String clientJobId;
    /**
     * Brief description of the active job state. This property will be server-side localized to the language specified in the Accept-Language header of the request.
     * 
     */
    @SerializedName("localizedActiveJobText")
    @Expose
    private String localizedActiveJobText;

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
    public Property.FeatureEnabled getIsPrivateJob() {
        return isPrivateJob;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIsPrivateJob(Property.FeatureEnabled isPrivateJob) {
        this.isPrivateJob = isPrivateJob;
    }

    public Job.State getState() {
        return state;
    }

    public void setState(Job.State state) {
        this.state = state;
    }

    public Job.ProcessingSubState getProcessingSubState() {
        return processingSubState;
    }

    public void setProcessingSubState(Job.ProcessingSubState processingSubState) {
        this.processingSubState = processingSubState;
    }

    /**
     * completion state of the job
     * 
     */
    public Job.JobCompletionState getCompletionState() {
        return completionState;
    }

    /**
     * completion state of the job
     * 
     */
    public void setCompletionState(Job.JobCompletionState completionState) {
        this.completionState = completionState;
    }

    /**
     * The completion code or cancel/failure reason of the job. For Example, memory out, job corruption etc.
     * 
     */
    public String getCompletionCode() {
        return completionCode;
    }

    /**
     * The completion code or cancel/failure reason of the job. For Example, memory out, job corruption etc.
     * 
     */
    public void setCompletionCode(String completionCode) {
        this.completionCode = completionCode;
    }

    /**
     * Brief description of the cancel/failure reason of the job. This property will be server-side localized to the language specified in the Accept-Language header of the request.
     * 
     */
    public String getLocalizedCompletionText() {
        return localizedCompletionText;
    }

    /**
     * Brief description of the cancel/failure reason of the job. This property will be server-side localized to the language specified in the Accept-Language header of the request.
     * 
     */
    public void setLocalizedCompletionText(String localizedCompletionText) {
        this.localizedCompletionText = localizedCompletionText;
    }

    /**
     * unique identifier
     * 
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * unique identifier
     * 
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Job.JobType getJobType() {
        return jobType;
    }

    public void setJobType(Job.JobType jobType) {
        this.jobType = jobType;
    }

    /**
     * The username of the person who owns this job.
     * 
     */
    public String getUserName() {
        return userName;
    }

    /**
     * The username of the person who owns this job.
     * 
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * The reason why a job was paused
     * 
     */
    public Job.PauseReason getPauseReason() {
        return pauseReason;
    }

    /**
     * The reason why a job was paused
     * 
     */
    public void setPauseReason(Job.PauseReason pauseReason) {
        this.pauseReason = pauseReason;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getJobDelayedDuringExecution() {
        return jobDelayedDuringExecution;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setJobDelayedDuringExecution(Property.FeatureEnabled jobDelayedDuringExecution) {
        this.jobDelayedDuringExecution = jobDelayedDuringExecution;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public Date getCompletionTime() {
        return completionTime;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public void setCompletionTime(Date completionTime) {
        this.completionTime = completionTime;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public Date getScheduledTime() {
        return scheduledTime;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public void setScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getAutoStart() {
        return autoStart;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setAutoStart(Property.FeatureEnabled autoStart) {
        this.autoStart = autoStart;
    }

    /**
     * This is ticketId to use when creating a job; ticket located at /cdm/jobTicket/v1/tickets/<ticketId>
     * 
     */
    public String getTicketId() {
        return ticketId;
    }

    /**
     * This is ticketId to use when creating a job; ticket located at /cdm/jobTicket/v1/tickets/<ticketId>
     * 
     */
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getReprintJobAvailable() {
        return reprintJobAvailable;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setReprintJobAvailable(Property.FeatureEnabled reprintJobAvailable) {
        this.reprintJobAvailable = reprintJobAvailable;
    }

    /**
     * Clients can use this property to sort the jobs in the /queue.  Higher priority jobs will have a smaller sortOrder value.
     * 
     */
    public Integer getSortOrder() {
        return sortOrder;
    }

    /**
     * Clients can use this property to sort the jobs in the /queue.  Higher priority jobs will have a smaller sortOrder value.
     * 
     */
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Job.CancelMode getCancelMode() {
        return cancelMode;
    }

    public void setCancelMode(Job.CancelMode cancelMode) {
        this.cancelMode = cancelMode;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getJobInFinalStage() {
        return jobInFinalStage;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setJobInFinalStage(Property.FeatureEnabled jobInFinalStage) {
        this.jobInFinalStage = jobInFinalStage;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getIsJobPromotable() {
        return isJobPromotable;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIsJobPromotable(Property.FeatureEnabled isJobPromotable) {
        this.isJobPromotable = isJobPromotable;
    }

    /**
     * The sessionId for the priorityMode session. If not empty then it means that job has been started by with client holding priority mode session.
     * 
     */
    public String getPriorityModeSessionId() {
        return priorityModeSessionId;
    }

    /**
     * The sessionId for the priorityMode session. If not empty then it means that job has been started by with client holding priority mode session.
     * 
     */
    public void setPriorityModeSessionId(String priorityModeSessionId) {
        this.priorityModeSessionId = priorityModeSessionId;
    }

    /**
     * normal: not stored job, store: original stored job, retrieve: a retrieved stored job
     * 
     */
    public Job.ExecutionMode getExecutionMode() {
        return executionMode;
    }

    /**
     * normal: not stored job, store: original stored job, retrieve: a retrieved stored job
     * 
     */
    public void setExecutionMode(Job.ExecutionMode executionMode) {
        this.executionMode = executionMode;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * Unique jobId from the perspective of the client sending the job.
     * 
     */
    public String getClientJobId() {
        return clientJobId;
    }

    /**
     * Unique jobId from the perspective of the client sending the job.
     * 
     */
    public void setClientJobId(String clientJobId) {
        this.clientJobId = clientJobId;
    }

    /**
     * Brief description of the active job state. This property will be server-side localized to the language specified in the Accept-Language header of the request.
     * 
     */
    public String getLocalizedActiveJobText() {
        return localizedActiveJobText;
    }

    /**
     * Brief description of the active job state. This property will be server-side localized to the language specified in the Accept-Language header of the request.
     * 
     */
    public void setLocalizedActiveJobText(String localizedActiveJobText) {
        this.localizedActiveJobText = localizedActiveJobText;
    }

    public enum CancelMode {

        @SerializedName("normal")
        NORMAL("normal"),
        @SerializedName("cancelPrintingNotCuring")
        CANCEL_PRINTING_NOT_CURING("cancelPrintingNotCuring");
        private final String value;
        private final static Map<String, Job.CancelMode> CONSTANTS = new HashMap<String, Job.CancelMode>();

        static {
            for (Job.CancelMode c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        CancelMode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Job.CancelMode fromValue(String value) {
            Job.CancelMode constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * normal: not stored job, store: original stored job, retrieve: a retrieved stored job
     * 
     */
    public enum ExecutionMode {

        @SerializedName("normal")
        NORMAL("normal"),
        @SerializedName("store")
        STORE("store"),
        @SerializedName("retrieve")
        RETRIEVE("retrieve");
        private final String value;
        private final static Map<String, Job.ExecutionMode> CONSTANTS = new HashMap<String, Job.ExecutionMode>();

        static {
            for (Job.ExecutionMode c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ExecutionMode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Job.ExecutionMode fromValue(String value) {
            Job.ExecutionMode constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * completion state of the job
     * 
     */
    public enum JobCompletionState {

        @SerializedName("cancelled")
        CANCELLED("cancelled"),
        @SerializedName("success")
        SUCCESS("success"),
        @SerializedName("failed")
        FAILED("failed"),
        @SerializedName("partialSuccess")
        PARTIAL_SUCCESS("partialSuccess");
        private final String value;
        private final static Map<String, Job.JobCompletionState> CONSTANTS = new HashMap<String, Job.JobCompletionState>();

        static {
            for (Job.JobCompletionState c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        JobCompletionState(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Job.JobCompletionState fromValue(String value) {
            Job.JobCompletionState constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum JobType {

        @SerializedName("copy")
        COPY("copy"),
        @SerializedName("scanEmail")
        SCAN_EMAIL("scanEmail"),
        @SerializedName("scanNetworkFolder")
        SCAN_NETWORK_FOLDER("scanNetworkFolder"),
        @SerializedName("print")
        PRINT("print"),
        @SerializedName("usbPrint")
        USB_PRINT("usbPrint"),
        @SerializedName("receiveFax")
        RECEIVE_FAX("receiveFax"),
        @SerializedName("scanFax")
        SCAN_FAX("scanFax"),
        @SerializedName("scanSharePoint")
        SCAN_SHARE_POINT("scanSharePoint"),
        @SerializedName("scanUsb")
        SCAN_USB("scanUsb"),
        @SerializedName("scanHttp")
        SCAN_HTTP("scanHttp"),
        @SerializedName("faxArchive")
        FAX_ARCHIVE("faxArchive"),
        @SerializedName("scanInternalStorage")
        SCAN_INTERNAL_STORAGE("scanInternalStorage"),
        @SerializedName("folderPrint")
        FOLDER_PRINT("folderPrint"),
        @SerializedName("ldapPrint")
        LDAP_PRINT("ldapPrint");
        private final String value;
        private final static Map<String, Job.JobType> CONSTANTS = new HashMap<String, Job.JobType>();

        static {
            for (Job.JobType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        JobType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Job.JobType fromValue(String value) {
            Job.JobType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * The reason why a job was paused
     * 
     */
    public enum PauseReason {

        @SerializedName("none")
        NONE("none"),
        @SerializedName("scheduled")
        SCHEDULED("scheduled"),
        @SerializedName("devicePaused")
        DEVICE_PAUSED("devicePaused"),
        @SerializedName("paperMismatch")
        PAPER_MISMATCH("paperMismatch"),
        @SerializedName("suppliesMismatch")
        SUPPLIES_MISMATCH("suppliesMismatch"),
        @SerializedName("holdForRecovery")
        HOLD_FOR_RECOVERY("holdForRecovery"),
        @SerializedName("holdForManualRelease")
        HOLD_FOR_MANUAL_RELEASE("holdForManualRelease"),
        @SerializedName("holdForAttendedMode")
        HOLD_FOR_ATTENDED_MODE("holdForAttendedMode"),
        @SerializedName("holdForAccountId")
        HOLD_FOR_ACCOUNT_ID("holdForAccountId");
        private final String value;
        private final static Map<String, Job.PauseReason> CONSTANTS = new HashMap<String, Job.PauseReason>();

        static {
            for (Job.PauseReason c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        PauseReason(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Job.PauseReason fromValue(String value) {
            Job.PauseReason constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum ProcessingSubState {

        @SerializedName("none")
        NONE("none"),
        @SerializedName("waiting")
        WAITING("waiting"),
        @SerializedName("preparingToPrint")
        PREPARING_TO_PRINT("preparingToPrint"),
        @SerializedName("printing")
        PRINTING("printing"),
        @SerializedName("drying")
        DRYING("drying"),
        @SerializedName("printerMaintenance")
        PRINTER_MAINTENANCE("printerMaintenance"),
        @SerializedName("scanning")
        SCANNING("scanning"),
        @SerializedName("dialing")
        DIALING("dialing"),
        @SerializedName("incomingCall")
        INCOMING_CALL("incomingCall"),
        @SerializedName("connecting")
        CONNECTING("connecting"),
        @SerializedName("connected")
        CONNECTED("connected"),
        @SerializedName("sending")
        SENDING("sending"),
        @SerializedName("receiving")
        RECEIVING("receiving"),
        @SerializedName("faxing")
        FAXING("faxing"),
        @SerializedName("received")
        RECEIVED("received"),
        @SerializedName("communicationProblem")
        COMMUNICATION_PROBLEM("communicationProblem"),
        @SerializedName("noAnswer")
        NO_ANSWER("noAnswer"),
        @SerializedName("recipientBusy")
        RECIPIENT_BUSY("recipientBusy");
        private final String value;
        private final static Map<String, Job.ProcessingSubState> CONSTANTS = new HashMap<String, Job.ProcessingSubState>();

        static {
            for (Job.ProcessingSubState c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ProcessingSubState(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Job.ProcessingSubState fromValue(String value) {
            Job.ProcessingSubState constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum State {

        @SerializedName("created")
        CREATED("created"),
        @SerializedName("ready")
        READY("ready"),
        @SerializedName("processing")
        PROCESSING("processing"),
        @SerializedName("prepareProcessing")
        PREPARE_PROCESSING("prepareProcessing"),
        @SerializedName("startProcessing")
        START_PROCESSING("startProcessing"),
        @SerializedName("initializeProcessing")
        INITIALIZE_PROCESSING("initializeProcessing"),
        @SerializedName("cancelProcessing")
        CANCEL_PROCESSING("cancelProcessing"),
        @SerializedName("resumeProcessing")
        RESUME_PROCESSING("resumeProcessing"),
        @SerializedName("pauseProcessing")
        PAUSE_PROCESSING("pauseProcessing"),
        @SerializedName("paused")
        PAUSED("paused"),
        @SerializedName("completed")
        COMPLETED("completed");
        private final String value;
        private final static Map<String, Job.State> CONSTANTS = new HashMap<String, Job.State>();

        static {
            for (Job.State c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        State(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Job.State fromValue(String value) {
            Job.State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
