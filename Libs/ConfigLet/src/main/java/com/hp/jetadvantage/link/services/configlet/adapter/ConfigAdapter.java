/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.configlet.adapter;

import static com.hp.jetadvantage.link.common.constants.LogConstants.TAG_CONFIG_SERVICE;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.hp.ext.service.solutionManager.Configuration;
import com.hp.ext.service.solutionManager.Configuration_Modify;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.common.utils.StringUtility;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceSolutionManager;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkInvalidParamException;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ConfigAdapter {
    private static final String TAG = TAG_CONFIG_SERVICE + "/Adapter";
    private static final String CONFIG_MIME_TYPE = "application/json";
    private static final String CONFIG_DEFAULT_DESCRIPTION = "Workpath App Default Configuration";

    public static Bundle getConfigData(final IDeviceSolutionManager solutionManager, final Bundle bundle,
                                       String packageName) throws SdkException {
        validateSolutionManager(solutionManager);

        initializeConfigurationMetaData(solutionManager, packageName);
        String configJsonData = solutionManager.getConfigurationData(packageName);
        if (StringUtility.isEmpty(configJsonData)) {
            configJsonData = "{}";
        }

        Result.pack(bundle, Result.RESULT_OK);
        bundle.putString(Result.KEY_RESULT, configJsonData);
        return bundle;
    }

    public static Bundle setConfigData(final IDeviceSolutionManager solutionManager, final Bundle bundle,
                                       final String packageName, final String configData) throws SdkException {
        validateSolutionManager(solutionManager);

        if (StringUtility.isEmpty(configData)) {
            Log.e(TAG, "setConfigData: configData is null");
            throw new SdkInvalidParamException("The input configuration data is null.");
        }
        if (!isValidJson(configData)) {
            Log.e(TAG, "setConfigData: configData is not valid JSON");
            throw new SdkInvalidParamException("The input configuration data is not valid JSON.");
        }
        initializeConfigurationMetaData(solutionManager, packageName);

        InputStream configDataStream = new ByteArrayInputStream(configData.getBytes(StandardCharsets.UTF_8));
        solutionManager.replaceConfigurationData(packageName, configDataStream);
        Result.pack(bundle, Result.RESULT_OK);
        return bundle;
    }

    private static void initializeConfigurationMetaData(final IDeviceSolutionManager solutionManager,
                                                        final String packageName) throws SdkException {
        validateSolutionManager(solutionManager);

        try {
            Configuration config = solutionManager.getConfiguration(packageName);
            if (config == null) {
                Log.e(TAG, "initializeConfigurationMetaData: getConfiguration returns null");
                throw new SdkServiceErrorException("Internal Service Error: Configuration is null");
            }
            if (!CONFIG_MIME_TYPE.equals(config.getMimeType())) {
                Configuration_Modify configModify = new Configuration_Modify();
                configModify.setMimeType(CONFIG_MIME_TYPE);
                configModify.setDescription(CONFIG_DEFAULT_DESCRIPTION);
                Configuration modified = solutionManager.modifyConfiguration(packageName, configModify);
                if (modified == null || !CONFIG_MIME_TYPE.equals(modified.getMimeType())) {
                    Log.e(TAG, "initializeConfigurationMetaData: failed to modify configuration : " +
                            (modified == null ? "modifyConfiguration null" : modified.getMimeType()));
                    throw new SdkServiceErrorException(
                            "Internal Service Error: Failed to initialize configuration meta data");
                }
                Log.i(TAG, "initializeConfigurationMetaData: configuration meta data initialized");
            } else {
                Log.d(TAG, "initializeConfigurationMetaData: configuration meta data already initialized");
            }
        } catch (Exception e) {
            Log.e(TAG, "initializeConfigurationMetaData: Failed to initialize configuration meta data", e);
            throw e;
        }
    }

    private static void validateSolutionManager(final IDeviceSolutionManager solutionManager) throws SdkServiceErrorException {
        if (solutionManager == null) {
            Log.e(TAG, "validateSolutionManager: solutionManager is null");
            throw new SdkServiceErrorException("Internal Service Error: SolutionManager is null");
        }
    }

    public static boolean isValidJson(String json) {
        try {
            JsonElement element = JsonParser.parseString(json);
            return element.isJsonObject() || element.isJsonArray();
        } catch (JsonSyntaxException e) {
            return false;
        } catch (Exception e) {
            Log.e(TAG, "isValidJson: Exception occurred while parsing JSON", e);
            return false;
        }
    }
}
