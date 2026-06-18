/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.jetadvantage.link.device.services.standard;

import android.util.Log;

import com.hp.ext.clients.device.DeviceServiceClient;
import com.hp.ext.clients.device.DeviceServiceClientImpl;
import com.hp.ext.service.device.DeploymentInformation;
import com.hp.ext.service.device.Identity;
import com.hp.ext.service.device.PrintEngine;
import com.hp.ext.service.device.Scanner;
import com.hp.ext.service.device.Status;
import com.hp.jetadvantage.link.common.utils.StringUtility;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceInfoService;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.DeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;
import com.hp.ws.cdm.commonglossary.Constraints;
import com.hp.ws.cdm.commonglossary.Property;
import com.hp.ws.cdm.commonglossary.Validator;
import com.hp.ws.cdm.controlpanel.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class StandardDeviceInfoService extends StandardDeviceService implements IDeviceInfoService {
    private static final String TAG = Constants.TAG + "/DevInfo";
    private static final com.hp.ws.cdm.controlpanel.Configuration.Language DEFAULT_LANGUAGE =
            com.hp.ws.cdm.controlpanel.Configuration.Language.EN;
    private static final String RESOURCE_GUN_SYSTEM_CONFIG =
            "com.hp.cdm.service.system.version.1.resource.configuration";
    private static final String PROPERTY_POINTER_DEVICE_LANGUAGE = "/deviceLanguage";

    public StandardDeviceInfoService() {
        super();
    }

    public StandardDeviceInfoService(DeviceManagementService deviceManagementService) {
        super(deviceManagementService);
    }

    @Override
    public DeploymentInformation getDeploymentInformation() {
        E2call<DeploymentInformation> call = () -> client().deploymentInformation().getAsync().get();
        try {
            return perform(call);
        } catch (Exception e) {
            Log.e(TAG, "getDeploymentInformation : Exception=" + e.getMessage());
            return null;
        }
    }

    @Override
    public Identity getIdentity() {
        E2call<Identity> call = () -> client().identity().getAsync().get();
        try {
            return perform(call);
        } catch (Exception e) {
            Log.i(TAG, "getIdentity : Exception=" + e.getMessage());
            return null;
        }
    }

    @Override
    public PrintEngine getPrintEngine() {
        return null;
    }

    @Override
    public Scanner getScanner() {
        E2call<Scanner> call = () -> client().scanner().getAsync().get();
        try {
            return perform(call);
        } catch (Exception e) {
            Log.i(TAG, "getScanner : Exception=" + e.getMessage());
            return null;
        }
    }

    @Override
    public Status getStatus() {
        E2call<Status> call = () -> client().status().getAsync().get();
        return perform(call);
    }

    @Override
    public com.hp.ws.cdm.controlpanel.Configuration.Language getDeviceLanguage() {
        com.hp.ws.cdm.controlpanel.Configuration.Language language = DEFAULT_LANGUAGE;
        try {
            CdmCall call = () -> getCDMClient().sendGetRequest(CDMUrl.SYSTEM_CONFIGURATION);
            com.hp.ws.cdm.system.Configuration configuration = perform(call, com.hp.ws.cdm.system.Configuration.class);
            if (configuration != null && configuration.getDeviceLanguage() != null) {
                language = configuration.getDeviceLanguage();
            }
        } catch (Exception e) {
            Log.e(TAG, "getDeviceLanguage : Exception=" + e.getMessage());
            // keep default language on other errors
        }
        return language;
    }

    @Override
    public List<Configuration.Language> getAvailableDeviceLanguages() {
        final List<Configuration.Language> languages = new ArrayList<>();
        try {
            CdmCall call = () -> getCDMClient().sendGetRequest(CDMUrl.SYSTEM_CONFIGURATION_CONSTRAINTS);
            Constraints constraints = perform(call, Constraints.class);
            if (constraints != null && constraints.getValidators() != null) {
                Set<Configuration.Language> unique = new LinkedHashSet<>();
                for (Validator validator : constraints.getValidators()) {
                    if (isLanguageValidator(validator)) {
                        addLanguagesFromOptions(validator.getOptions(), unique);
                    }
                }
                languages.addAll(unique);
            }
        } catch (Exception e) {
            Log.e(TAG, "getAvailableDeviceLanguages: Exception=" + e.getMessage(), e);
        }
        if (languages.isEmpty()) {
            languages.add(DEFAULT_LANGUAGE);
            Log.i(TAG, "getAvailableDeviceLanguages: falling back to default=" + DEFAULT_LANGUAGE);
        }
        return languages;
    }

    private boolean isLanguageValidator(Validator validator) {
        return validator != null
                && RESOURCE_GUN_SYSTEM_CONFIG.equalsIgnoreCase(validator.getResourceGun())
                && PROPERTY_POINTER_DEVICE_LANGUAGE.equalsIgnoreCase(validator.getPropertyPointer())
                && validator.getOptions() != null;
    }

    private void addLanguagesFromOptions(List<Property> options, Set<Configuration.Language> languages) {
        for (Property option : options) {
            if (option != null && option.getSeValue() != null) {
                String raw = option.getSeValue().trim();
                if (StringUtility.isEmpty(raw)) {
                    continue;
                }
                try {
                    Configuration.Language lang = Configuration.Language.fromValue(raw);
                    if (lang != null) {
                        languages.add(lang);
                    }
                } catch (IllegalArgumentException ex) {
                    Log.e(TAG, "getAvailableDeviceLanguages: Unknown language option=" + raw);
                }
            }
        }
    }

    private DeviceServiceClient client() throws java.net.URISyntaxException {
        return new DeviceServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
    }

    public static final class CDMUrl {
        public static final String SYSTEM_CONFIGURATION = "/cdm/system/v1/configuration";
        public static final String SYSTEM_CONFIGURATION_CONSTRAINTS = "/cdm/system/v1/configuration/constraints";
    }
}
