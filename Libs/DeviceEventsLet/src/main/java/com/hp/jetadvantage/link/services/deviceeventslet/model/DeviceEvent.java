package com.hp.jetadvantage.link.services.deviceeventslet.model;

/**
 * Represents a device event with various properties.
 */
public class DeviceEvent {
    private String instanceId;
    private String severity;
    private String category;
    private String stateChangeType;
    private String title;
    private String eventCode;
    private Timestamp timestamp;
    private String[] details;
    private String supportInformationLink;

    private long id;

    /**
     * Returns the instance ID of the event.
     *
     * @return instanceId
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * Sets the instance ID of the event.
     *
     * @param instanceId
     */
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * Returns the severity of the event.
     *
     * @return severity
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * Sets the severity of the event.
     *
     * @param severity
     */
    public void setSeverity(String severity) {
        this.severity = severity;
    }

    /**
     * Returns the category of the event.
     *
     * @return category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of the event.
     *
     * @param category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Returns the state change type of the event.
     *
     * @return stateChangeType
     */
    public String getStateChangeType() {
        return stateChangeType;
    }

    /**
     * Sets the state change type of the event.
     *
     * @param stateChangeType
     */
    public void setStateChangeType(String stateChangeType) {
        this.stateChangeType = stateChangeType;
    }

    /**
     * Returns the title of the event.
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the event.
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the event code of the event.
     *
     * @return eventCode
     */
    public String getEventCode() {
        return eventCode;
    }

    /**
     * Sets the event code of the event.
     *
     * @param eventCode
     */
    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    /**
     * Returns the timestamp of the event.
     *
     * @return timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp of the event.
     *
     * @param timestamp
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Returns the details of the event.
     *
     * @return details
     */
    public String[] getDetails() {
        return details;
    }

    /**
     * Sets the details of the event.
     *
     * @param details
     */
    public void setDetails(String[] details) {
        this.details = details;
    }

    /**
     * Returns the support information link of the event.
     *
     * @return supportInformationLink
     */
    public String getSupportInformationLink() {
        return supportInformationLink;
    }

    /**
     * Sets the support information link of the event.
     *
     * @param supportInformationLink
     */
    public void setSupportInformationLink(String supportInformationLink) {
        this.supportInformationLink = supportInformationLink;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}