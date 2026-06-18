
package com.hp.ws.cdm.jobmanagement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * jobManagement schema definition
 * 
 */
public class Com_hp_cdm_service_jobManagement_version_1_schema {

    @SerializedName("jobs")
    @Expose
    private Jobs jobs;
    @SerializedName("job")
    @Expose
    private Job job;
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("capabilities")
    @Expose
    private Capabilities capabilities;
    @SerializedName("configuration")
    @Expose
    private Configuration configuration;
    @SerializedName("historyStats")
    @Expose
    private HistoryStats historyStats;
    @SerializedName("queueStats")
    @Expose
    private QueueStats queueStats;
    @SerializedName("stats")
    @Expose
    private Stats stats;
    @SerializedName("jobOperation")
    @Expose
    private JobOperation jobOperation;
    @SerializedName("moveOperation")
    @Expose
    private MoveOperation moveOperation;
    @SerializedName("pages")
    @Expose
    private Pages pages;
    @SerializedName("page")
    @Expose
    private Page page;
    @SerializedName("priorityModeSessions")
    @Expose
    private PriorityModeSessions priorityModeSessions;
    @SerializedName("priorityModeSession")
    @Expose
    private PriorityModeSession priorityModeSession;

    public Jobs getJobs() {
        return jobs;
    }

    public void setJobs(Jobs jobs) {
        this.jobs = jobs;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Capabilities getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public HistoryStats getHistoryStats() {
        return historyStats;
    }

    public void setHistoryStats(HistoryStats historyStats) {
        this.historyStats = historyStats;
    }

    public QueueStats getQueueStats() {
        return queueStats;
    }

    public void setQueueStats(QueueStats queueStats) {
        this.queueStats = queueStats;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public JobOperation getJobOperation() {
        return jobOperation;
    }

    public void setJobOperation(JobOperation jobOperation) {
        this.jobOperation = jobOperation;
    }

    public MoveOperation getMoveOperation() {
        return moveOperation;
    }

    public void setMoveOperation(MoveOperation moveOperation) {
        this.moveOperation = moveOperation;
    }

    public Pages getPages() {
        return pages;
    }

    public void setPages(Pages pages) {
        this.pages = pages;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public PriorityModeSessions getPriorityModeSessions() {
        return priorityModeSessions;
    }

    public void setPriorityModeSessions(PriorityModeSessions priorityModeSessions) {
        this.priorityModeSessions = priorityModeSessions;
    }

    public PriorityModeSession getPriorityModeSession() {
        return priorityModeSession;
    }

    public void setPriorityModeSession(PriorityModeSession priorityModeSession) {
        this.priorityModeSession = priorityModeSession;
    }

}
