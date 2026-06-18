package com.hp.jetadvantage.link.device.services.sim;

import com.hp.ext.service.device.Scanner;
import com.hp.ext.service.scanJob.DefaultOptions;
import com.hp.ext.service.scanJob.Profile;
import com.hp.ext.service.scanJob.ScanJob;
import com.hp.ext.service.scanJob.ScanJob_Cancel;
import com.hp.ext.service.scanJob.ScanJob_Create;
import com.hp.ext.service.scanJob.ScanNotification;
import com.hp.ext.types.base.DeleteContent;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;

public class SimDeviceScanJobService implements IDeviceScanJobService {

    @Override
    public boolean isSupported() {
        return false;
    }

    @Override
    public Scanner getScannerStatus() {
        return null;
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
    public ScanJob createScanJob(String packageName, ScanJob_Create scanJob_Create) {
        return null;
    }

    @Override
    public ScanJob getScanJob(String packageName, String scanJobId) {
        return null;
    }

    @Override
    public DeleteContent deleteLocalScanJob(String packageName, String scanJobId) {
        return null;
    }

    @Override
    public ScanJob_Cancel cancelScanJob(String packageName, String scanJobId) {
        return null;
    }

    @Override
    public void registerNotificationCallback(IE2PayloadCallback<ScanNotification> callback) {

    }

    @Override
    public void unRegisterNotificationCallback() {

    }
}
