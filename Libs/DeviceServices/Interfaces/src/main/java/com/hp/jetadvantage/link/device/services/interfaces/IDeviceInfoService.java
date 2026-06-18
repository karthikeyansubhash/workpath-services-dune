/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.interfaces;

import com.hp.ext.service.device.DeploymentInformation;
import com.hp.ext.service.device.Identity;
import com.hp.ext.service.device.PrintEngine;
import com.hp.ext.service.device.Scanner;
import com.hp.ext.service.device.Status;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.ws.cdm.controlpanel.Configuration;

import java.util.List;

public interface IDeviceInfoService {
    /**
     * Get the current deployment information for the device including network information.
     *
     * @return
     */
    DeploymentInformation getDeploymentInformation();

    /**
     * Get device identity information
     *
     * @return the device identity
     */
    Identity getIdentity();

    /**
     * Get print engine information
     *
     * @return the print engine details
     */
    PrintEngine getPrintEngine();

    /**
     * Get scanner information
     *
     * @return the scanner details
     */
    Scanner getScanner();

    /**
     * Get current device status
     *
     * @return the device status
     */
    Status getStatus();

    /**
     * Get the device configured language
     *
     * @return the language defined in the CDM glossary
     */
    Configuration.Language getDeviceLanguage();

    /**
     * Get the available device languages
     *
     * @return the language list
     */
    List<Configuration.Language> getAvailableDeviceLanguages();
}
