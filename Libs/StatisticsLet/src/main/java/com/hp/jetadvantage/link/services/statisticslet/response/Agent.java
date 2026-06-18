// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.statisticslet.response;

public class Agent {
    private String agentId;
    private NotificationTarget notificationTarget;
    private long lastSequenceNumberProcessed;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public NotificationTarget getNotificationTarget() {
        return notificationTarget;
    }

    public void setNotificationTarget(NotificationTarget notificationTarget) {
        this.notificationTarget = notificationTarget;
    }

    public long getLastSequenceNumberProcessed() {
        return lastSequenceNumberProcessed;
    }

    public void setLastSequenceNumberProcessed(long lastSequenceNumberProcessed) {
        this.lastSequenceNumberProcessed = lastSequenceNumberProcessed;
    }
}
