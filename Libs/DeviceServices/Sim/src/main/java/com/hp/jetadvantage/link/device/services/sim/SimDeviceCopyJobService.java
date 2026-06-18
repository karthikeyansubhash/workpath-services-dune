package com.hp.jetadvantage.link.device.services.sim;

import com.hp.ext.service.copy.CopyJob;
import com.hp.ext.service.copy.CopyJob_Cancel;
import com.hp.ext.service.copy.CopyJob_Create;
import com.hp.ext.service.copy.CopyNotification;
import com.hp.ext.service.copy.DefaultOptions;
import com.hp.ext.service.copy.Profile;
import com.hp.ext.service.copy.ReleaseStoredJobRequest;
import com.hp.ext.service.copy.RemoveStoredJobRequest;
import com.hp.ext.service.copy.StoredJob;
import com.hp.ext.service.copy.StoredJobs;
import com.hp.ext.service.copy.StoredJob_Release;
import com.hp.ext.service.copy.StoredJob_Remove;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceCopyJobService;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;

public class SimDeviceCopyJobService implements IDeviceCopyJobService {

    @Override
    public boolean isSupported() {
        return false;
    }

    @Override
    public DefaultOptions getDefaultOptions(String packageName) {
        return null;
    }

    @Override
    public Profile getProfile(String packageName) {
        return null;
    }

    @Override
    public CopyJob createCopyJob(String packageName, CopyJob_Create copyJobCreate) {
        return null;
    }

    @Override
    public CopyJob getCopyJob(String packageName, String copyJobId) {
        return null;
    }

    @Override
    public CopyJob_Cancel cancelCopyJob(String packageName, String copyJobId) {
        return null;
    }

    @Override
    public void registerNotificationCallback(IE2PayloadCallback<CopyNotification> callback) {

    }

    @Override
    public void unRegisterNotificationCallback() {

    }

    @Override
    public StoredJobs enumerateStoredJobs(String packageName) {
        return null;
    }

    @Override
    public StoredJob getStoredJob(String packageName, String storedJobId) {
        return null;
    }

    @Override
    public StoredJob_Release releaseStoredJob(String packageName, String storedJobId, ReleaseStoredJobRequest request) {
        return null;
    }

    @Override
    public StoredJob_Remove deleteStoredJob(String packageName, String storedJobId, RemoveStoredJobRequest request) {
        return null;
    }
}
