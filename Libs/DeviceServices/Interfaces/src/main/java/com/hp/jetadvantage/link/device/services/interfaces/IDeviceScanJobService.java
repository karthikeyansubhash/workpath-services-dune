/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.jetadvantage.link.device.services.interfaces;

import com.hp.ext.service.device.Scanner;
import com.hp.ext.service.scanJob.DefaultOptions;
import com.hp.ext.service.scanJob.Profile;
import com.hp.ext.service.scanJob.ScanJob;
import com.hp.ext.service.scanJob.ScanJob_Cancel;
import com.hp.ext.service.scanJob.ScanJob_Create;
import com.hp.ext.service.scanJob.ScanNotification;
import com.hp.ext.types.base.DeleteContent;

/**
 * Device Service Interface to execute a scan job service on a device
 */
public interface IDeviceScanJobService {

    boolean isSupported();

    Scanner getScannerStatus();

    DefaultOptions getDefaultOptions(String packageName);

    Profile getProfile(String packageName);

    ScanJob createScanJob(String packageName, ScanJob_Create scanJob_Create);

    ScanJob getScanJob(String packageName, String scanJobId);

    /**
     * Deletes a local scan job.
     * Delete the original scan file and the folder corresponding to the given job ID located in
     * {@code com.hp.jetadvantage.link.services.scanlet.ScanConstants.EXTENSIBILITY_WORKPATH_SOLUTIONS_DIRECTORY}.
     *
     * @param packageName The package name of the calling application.
     * @param scanJobId The ID of the scan job to delete.
     * @return DeleteContent if the job was successfully deleted, false otherwise.
     */
    DeleteContent deleteLocalScanJob(String packageName, String scanJobId);

    ScanJob_Cancel cancelScanJob(String packageName, String scanJobId);

    /**
     * Register callback to receive scan notifications for all scan events indicating a change from E2 service
     * Only one callback can be registered on a instance of IDeviceScanJobService.
     * If multiple callbacks are registered, the last callback will be available.
     * Cautions : The registered callback should be unregistered by calling unRegisterNotificationCallback()
     *            when the IDeviceScanJobService object is not used anymore. Otherwise, it will make memory leakage.
     *
     * @param callback callback object to receive scan notification by overriding onReceiveNotification method
     */
    void registerNotificationCallback(IE2PayloadCallback<ScanNotification> callback);

    /**
     * Unregister callback to clean up
     */
    void unRegisterNotificationCallback();

}
