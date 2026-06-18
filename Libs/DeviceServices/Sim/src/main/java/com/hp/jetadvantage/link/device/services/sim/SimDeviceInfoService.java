package com.hp.jetadvantage.link.device.services.sim;

import com.hp.ext.service.device.DeploymentInformation;
import com.hp.ext.service.device.Identity;
import com.hp.ext.service.device.PrintEngine;
import com.hp.ext.service.device.Scanner;
import com.hp.ext.service.device.Status;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceInfoService;
import com.hp.ws.cdm.controlpanel.Configuration;

import java.util.Collections;
import java.util.List;

public class SimDeviceInfoService implements IDeviceInfoService {
    @Override
    public DeploymentInformation getDeploymentInformation() {
        return null;
    }

    @Override
    public Identity getIdentity() {
        return null;
    }

    @Override
    public PrintEngine getPrintEngine() {
        return null;
    }

    @Override
    public Scanner getScanner() {
        return null;
    }

    @Override
    public Status getStatus() {
        return null;
    }

    @Override
    public com.hp.ws.cdm.controlpanel.Configuration.Language getDeviceLanguage() {
        return com.hp.ws.cdm.controlpanel.Configuration.Language.EN;
    }

    @Override
    public List<Configuration.Language> getAvailableDeviceLanguages() {
        return Collections.emptyList();
    }
}
