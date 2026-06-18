/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.interfaces;

/**
 * Interface to be used for receiving PayloadTarget event notification from E2 service
 *
 */
public interface IE2ChannelSetupCallback {
    /**
     * callback function to receive E2 app channel setup event notification
     *
     * @param appPackageName : target application package name (ex: com.hp.workpath.sample.accessorysample)
     */
    void onSetup(String appPackageName);
}
