package com.hp.jetadvantage.link.services.statisticslet.response;

public class NotificationTarget<T> {
    private T workpath;

    public T getWorkpath() {
        return workpath;
    }

    public void setWorkpath(T workpath) {
        this.workpath = workpath;
    }
}
