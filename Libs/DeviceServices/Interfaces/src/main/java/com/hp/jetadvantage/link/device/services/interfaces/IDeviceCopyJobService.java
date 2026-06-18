/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.jetadvantage.link.device.services.interfaces;

import com.hp.ext.service.copy.CopyNotification;
import com.hp.ext.service.copy.DefaultOptions;
import com.hp.ext.service.copy.Profile;
import com.hp.ext.service.copy.CopyJob;
import com.hp.ext.service.copy.CopyJob_Cancel;
import com.hp.ext.service.copy.CopyJob_Create;
import com.hp.ext.service.copy.ReleaseStoredJobRequest;
import com.hp.ext.service.copy.StoredJob;
import com.hp.ext.service.copy.StoredJobs;
import com.hp.ext.service.copy.StoredJob_Release;
import com.hp.ext.service.copy.StoredJob_Remove;

/**
 * Device Service Interface to execute a scan job service on a device
 */
public interface IDeviceCopyJobService {

    boolean isSupported();

    DefaultOptions getDefaultOptions(String packageName);

    Profile getProfile(String packageName);

    CopyJob createCopyJob(String packageName, CopyJob_Create copyJobCreate);

    CopyJob getCopyJob(String packageName, String copyJobId);

    CopyJob_Cancel cancelCopyJob(String packageName, String copyJobId);

    /**
     * Register callback to receive scan notifications for all scan events indicating a change from E2 service
     * Only one callback can be registered on a instance of IDeviceScanJobService.
     * If multiple callbacks are registered, the last callback will be available.
     * Cautions : The registered callback should be unregistered by calling unRegisterNotificationCallback()
     *            when the IDeviceScanJobService object is not used anymore. Otherwise, it will make memory leakage.
     *
     * @param callback callback object to receive scan notification by overriding onReceiveNotification method
     */
    void registerNotificationCallback(IE2PayloadCallback<CopyNotification> callback);

    /**
     * Unregister callback to clean up
     */
    void unRegisterNotificationCallback();

    /**
     * Enumerate stored copy jobs on the device.
     * E2 endpoint: GET /ext/copy/v1/copyAgents/{agentId}/storedJobs
     *
     * @param packageName The package name of the calling application.
     * @return StoredJobs object containing the list of stored jobs, or null if not supported.
     */
    StoredJobs enumerateStoredJobs(String packageName);

    /**
     * Get an individual stored copy job resource with full details.
     * E2 endpoint: GET /ext/copy/v1/copyAgents/{agentId}/storedJobs/{storedJobId}
     *
     * @param packageName  The package name of the calling application.
     * @param storedJobId  The ID of the stored job to retrieve.
     * @return StoredJob with full field details, or null on failure.
     */
    StoredJob getStoredJob(String packageName, String storedJobId);

    /**
     * Release a stored copy job for printing with copy options.
     * E2 endpoint: POST /ext/copy/v1/copyAgents/{agentId}/storedJobs/{storedJobId}/release
     *
     * @param packageName The package name of the calling application.
     * @param storedJobId The ID of the stored job to release.
     * @param request     The release request containing copyOptions and optional jobPassword.
     * @return StoredJob_Release containing the created CopyJob, or null on failure.
     */
    StoredJob_Release releaseStoredJob(String packageName, String storedJobId, ReleaseStoredJobRequest request);

    /**
     * Delete a stored copy job from the device with request parameters like password.
     * E2 endpoint: POST /ext/copy/v1/copyAgents/{agentId}/storedJobs/{storedJobId}/remove
     *
     * @param packageName The package name of the calling application.
     * @param storedJobId The ID of the stored job to delete.
     * @param request     The remove request containing jobPassword.
     * @return StoredJob_Remove result, or null on failure.
     */
    StoredJob_Remove deleteStoredJob(String packageName, String storedJobId, com.hp.ext.service.copy.RemoveStoredJobRequest request);

}
