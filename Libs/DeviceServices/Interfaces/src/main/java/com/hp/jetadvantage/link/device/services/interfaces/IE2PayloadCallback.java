/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.interfaces;

/**
 * Interface to be used for receiving PayloadTarget event notification from E2 service
 *
 * @param <T> : PayloadTarget Type (ex: copyNotification for copy job events)
 */
public interface IE2PayloadCallback<T> {

    /**
     * callback function to receive E2 PayloadTarget<T> event notification
     *
     * @param appPackageId : target application package name (ex: com.hp.workpath.sample.accessorysample)
     * @param notification : event notification
     */
    void onReceiveNotification(String appPackageId, T notification);
}
