/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.jetadvantage.link.device.services.interfaces;

import com.hp.ext.service.jobStatistics.Jobs;
import com.hp.ext.service.jobStatistics.StatisticsCallbackPayload;

public interface IDeviceStatisticsService {
    boolean isSupported();
    Jobs getAllJobsList(String packName, int offset, int limit);
    Jobs getJobsList(String packName);
    Jobs getJobWithLastSequenceNumberProcessed(String packName);
    Jobs commitLastJobSequence(String packName, int lastSequenceNumberProcessed);

    /**
     * Register callback to receive statistics notifications for all job completion events from E2 service.
     * Only one callback can be registered on an instance of IDeviceStatisticsService.
     * If multiple callbacks are registered, the last callback will be available.
     * The registered callback should be unregistered by calling unRegisterNotificationCallback()
     * when the IDeviceStatisticsService object is no longer used. Otherwise, it will cause memory leakage.
     *
     * @param callback callback object to receive statistics notification by overriding onReceiveNotification method
     */
    void registerNotificationCallback(IE2PayloadCallback<StatisticsCallbackPayload> callback);

    /**
     * Unregister callback to clean up
     */
    void unRegisterNotificationCallback();
}
