package com.hp.jetadvantage.link.device.services.interfaces;

public interface IE2AsyncIoCallback<T> {
    void onReceiveNotification(String appPackageId, String contextId, T notification);
}
