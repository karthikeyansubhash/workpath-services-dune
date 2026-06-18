/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.jetadvantage.link.device.services.standard.common;

import static com.hp.jetadvantage.link.device.services.standard.common.Constants.BOOT_TAG;

import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.VisibleForTesting;

import com.hp.ext.clients.InjectedHttpClient;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.clients.discovery.DiscoveryServiceClient;
import com.hp.ext.clients.discovery.DiscoveryServiceClientImpl;
import com.hp.ext.service.device.Identity;
import com.hp.ext.service.device.Scanner;
import com.hp.ext.types.common.ServicesDiscoveryImpl;
import com.hp.jetadvantage.link.common.constants.PackageContract;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.model.PrinterState;
import com.hp.jetadvantage.link.common.utils.StringUtility;
import com.hp.jetadvantage.link.device.services.clients.CDMClient;
import com.hp.jetadvantage.link.device.services.clients.DeviceConnector;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.interfaces.IAppInstallUninstallCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceInfoService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceInfoService;
import com.hp.jetadvantage.link.device.services.standard.receivers.AppInstallEventReceiver;
import com.hp.net.http.HttpClient;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class StandardDeviceManagementService implements DeviceManagementService {
    private static final String TAG = Constants.TAG + "/DeviceMgmt";
    private static final String DEVICE_IP_KEY = "device_ip";
    private static final String DEVICE_TOKEN_KEY = "device_token";
    private Context context;
    private String deviceIPAddress;
    private String accessToken;
    private DeviceConnector deviceConn;
    private ServicesDiscoveryImpl discoveryTree = null;
    private boolean initialized = false;
    private Identity deviceIdentity;
    private PackageManagerHelper pmHelper;
    private AppTokenManager appTokenMgr;
    private UIContextTokenManager uiContextTokenMgr;

    // App install/uninstall callback map with agentTypeGun as key
    private ConcurrentHashMap<String, IAppInstallUninstallCallback> appInstallUninstallCallBackMap =
            new ConcurrentHashMap<>();

    // App install/uninstall callback list without agent type gun
    private final List<IAppInstallUninstallCallback> appInstallUninstallCallBackList =
            new java.util.concurrent.CopyOnWriteArrayList<>();

    private StandardDeviceManagementService() {
        pmHelper = new PackageManagerHelper();
        uiContextTokenMgr = new UIContextTokenManager();
    }

    public static DeviceManagementService getInstance() {
        return InstanceHolder.instance;
    }

    public synchronized void initialize(String ip, String token) {
        Log.i(TAG, "initialize(" + ip + ") ENTER ");

        deviceIPAddress = ip;
        accessToken = token;
        if (initialized) {
            deviceConn.updateDeviceInfo(deviceIPAddress, accessToken);
        } else {
            deviceConn = new DeviceConnector(deviceIPAddress, accessToken);

            //inject http client to the oxpd2 library
            InjectedHttpClient httpclient = deviceConn.getE2Client();
            ResourceFacadeHelper.setHttpClient(httpclient);
        }
        discoveryTree = callServiceDiscovery();
        appTokenMgr = new AppTokenManager(deviceConn.getCDMClient());

        initialized = true;

        if (discoveryTree != null) {
            Log.i(TAG, "initialize() Get discoveryTree successfully");
            Log.i(BOOT_TAG, "DeviceManagementService -- OK");
        }

        Log.i(TAG, "initialize(" + ip + ") EXIT");
    }

    public synchronized void initialize(Context context, String ip, String token) {

        if (context != null) {
            Log.i(TAG, "initialize() : setSharedPreference ");
            StandardSecureAppStorage.setSharedPreference(context, DEVICE_IP_KEY, ip);
            StandardSecureAppStorage.setSharedPreference(context, DEVICE_TOKEN_KEY, token);
        }

        initialize(ip, token);

        if (context != null && discoveryTree != null) {
            setPrinterInfo(context);
        }

        registerDynamicReceivers(context);
    }

    public synchronized void reInitialize(Context context) {
        Log.i(TAG, "reInitialize() ENTER");
        Log.d(TAG, "reInitialize() context=" + context + ", deviceIPAddress=" + deviceIPAddress);

        //re-initialize from stored SharedPreference when device info(ip) is not configured
        if (context != null && deviceIPAddress == null) {
            String ip = StandardSecureAppStorage.getSharedPreference(context, DEVICE_IP_KEY);
            String token = StandardSecureAppStorage.getSharedPreference(context, DEVICE_TOKEN_KEY);
            Log.i(TAG, "reInitialize() getSharedPreference:" + ip);

            if (!TextUtils.isEmpty(ip) && !TextUtils.isEmpty(token)) {
                initialize(ip, token);
            } else {
                Log.i(TAG, "reInitialize() param is empty");
            }
            setPrinterInfo(context);
            registerDynamicReceivers(context);
        }
        Log.i(TAG, "reInitialize() EXIT");
    }

    public synchronized void updateDeviceInfo(String ip, String token) {
        Log.i(TAG, "updateDeviceInfo() ENTER");
        deviceIPAddress = ip;
        accessToken = token;
        if (initialized) {
            deviceConn.updateDeviceInfo(deviceIPAddress, accessToken);
        }
        Log.i(TAG, "updateDeviceInfo() EXIT");
    }

    public synchronized void updateDeviceInfo(Context context, String ip, String token) {
        if (context != null) {
            StandardSecureAppStorage.setSharedPreference(context, DEVICE_IP_KEY, ip);
            StandardSecureAppStorage.setSharedPreference(context, DEVICE_TOKEN_KEY, token);
        }

        updateDeviceInfo(ip, token);
        Log.i(BOOT_TAG, "DeviceManagementService update -- OK");
    }

    @Override
    public void clearSolutionTokenCache(String solutionId) {
        if (appTokenMgr != null) {
            Log.d(TAG, "clearSolutionTokenCache : " + solutionId);
            appTokenMgr.clearSolutionTokenCache(solutionId);
        }
    }

    /**
     * Get the agent ID of the the app's package from the PACMAN
     *
     * @param packageName application's package name
     * @param agentType   E2 Agent registration Record Type GUN
     * @return agent ID
     */
    @Override
    public String getAgentId(String packageName, String agentType) {
        return pmHelper.getAgentId(context, packageName, agentType);
    }

    @Override
    public List<IAppInstallUninstallCallback> getAppInstallCallbacks(String packageName) {
        List<IAppInstallUninstallCallback> callbacks = new ArrayList<>();
        appInstallUninstallCallBackMap.forEach((key, value) -> {
            String agentId = getAgentId(packageName, key);
            if (agentId != null) {
                callbacks.add(value);
            }
            Log.d(TAG, "getAppInstallUninstallCallbacks: [AppInstall] PackageName:" + packageName +
                    ", AgentId:" + agentId + ", " + "AgentTypeGun:" + key);
        });
        appInstallUninstallCallBackList.forEach((value) -> {
            callbacks.add(value);
        });
        return callbacks;
    }

    @Override
    public List<IAppInstallUninstallCallback> getAppUnInstallCallbacks(String packageName) {
        List<IAppInstallUninstallCallback> callbacks = new ArrayList<>();
        appInstallUninstallCallBackMap.forEach((key, value) -> {
            callbacks.add(value);
        });
        appInstallUninstallCallBackList.forEach((value) -> {
            callbacks.add(value);
        });
        return callbacks;
    }

    public synchronized CDMClient getCDMClient() {
        if (initialized) {
            return deviceConn.getCDMClient();
        }
        throw new BoundDeviceException("Not initialized");
    }

    @Override
    public synchronized String getDeviceIPAddress() {
        return deviceIPAddress;
    }

    @Override
    public synchronized ServicesDiscoveryImpl getDiscoveryTree() {
        if (discoveryTree == null) {
            discoveryTree = callServiceDiscovery();
        }
        return discoveryTree;
    }

    @Override
    public String getSolutionId(String packageName) {
        return pmHelper.getSolutionId(context, packageName);
    }

    @Override
    public String getSolutionToken(String packageName) {
        if (Constants.TEST_PACKAGE_NAME.equalsIgnoreCase(packageName)) {
            return "";
        }

        String solutionId = pmHelper.getSolutionId(context, packageName);
        if(StringUtility.isEmpty(solutionId)) {
            Log.e(TAG, "getSolutionToken : solution uuid is empty for packageName:" + packageName);
        }
        return appTokenMgr.getSolutionToken(solutionId);
    }

    @Override
    public String getUiContextToken(String packageName) {
        String solutionId = pmHelper.getSolutionId(context, packageName);
        return uiContextTokenMgr.getUIContextToken(solutionId);
    }

    @Override
    public UIContextTokenManager getUIContextTokenManager() {
        return uiContextTokenMgr;
    }

    /**
     * For testing purposes only
     */
    @VisibleForTesting
    public void setUIContextTokenManager(UIContextTokenManager mockUiContextTokenMgr) {
        this.uiContextTokenMgr = mockUiContextTokenMgr;
    }

    @Override
    public boolean hasUnauthorizedToken() {
        StandardDeviceToken t = new StandardDeviceToken();
        return t.checkIfUnauthorizedToken();
    }

    @Override
    public synchronized boolean isDeviceConnected() {
        return initialized && (discoveryTree != null);
    }

    @Override
    public void registerAppInstallUninstallReceiver(String agentTypeGun, IAppInstallUninstallCallback callback) {
        if (agentTypeGun != null && callback != null) {
            appInstallUninstallCallBackMap.put(agentTypeGun, callback);
        }
    }

    @Override
    public void unRegisterAppInstallUninstallReceiver(String e2AgentRegistrationRecordTypeGun) {
        if (e2AgentRegistrationRecordTypeGun != null) {
            appInstallUninstallCallBackMap.remove(e2AgentRegistrationRecordTypeGun);
        }
    }

    @Override
    public void registerAppInstallUninstallCallback(IAppInstallUninstallCallback callback) {
        if (callback != null && !appInstallUninstallCallBackList.contains(callback)) {
            appInstallUninstallCallBackList.add(callback);
        }
    }

    @Override
    public void unregisterAppInstallUninstallCallback(IAppInstallUninstallCallback callback) {
        if(callback != null && appInstallUninstallCallBackList.contains(callback)) {
            appInstallUninstallCallBackList.remove(callback);
        }
    }

    public void setApplicationContext(Context context) {
        this.context = context;
    }

    /**
     * For testing purposes only
     */
    @VisibleForTesting
    public void setPackageManagerHelper(PackageManagerHelper mockPmHelper) {
        this.pmHelper = mockPmHelper;
    }

    // ====================== Private Methods Area ======================

    private ServicesDiscoveryImpl callServiceDiscovery() {
        // Get the discovery tree
        DiscoveryServiceClient discoveryClient;
        try {
            discoveryClient = new DiscoveryServiceClientImpl(new HttpClient(), deviceIPAddress);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        ServicesDiscoveryImpl discoveryTree = null;
        try {
            discoveryTree = discoveryClient.servicesDiscovery().getAsync().get();
        } catch (Exception e) {
            Log.i(TAG, "callServiceDiscovery : failed :" + e.getMessage());
        }
        return discoveryTree;
    }

    private void setPrinterInfo(Context context) {
        long capabilities = PrinterInfo.Capability.CAPABILITY_PRINT;

        IDeviceInfoService devInfo = new StandardDeviceInfoService();
        deviceIdentity = devInfo.getIdentity();
        Scanner scanner = devInfo.getScanner();
        if (scanner != null) {
            capabilities |= PrinterInfo.Capability.CAPABILITY_SCAN;
            if ((capabilities & PrinterInfo.Capability.CAPABILITY_PRINT) != 0)
                capabilities |= PrinterInfo.Capability.CAPABILITY_COPY;
        }

        PrinterInfo pi = SelectedPrinterHelper.get(context.getContentResolver());
        PrinterInfo newPrinterInfo = new PrinterInfo.Builder(pi).capabilities(capabilities)
                .ip(Constants.DEVICE_INTERNAL_HOSTNAME)
                .baseUri(Uri.parse("https://" + Constants.DEVICE_INTERNAL_HOSTNAME + "/"))
                .printerState(PrinterState.CONNECTED)
                .build();
        SelectedPrinterHelper.set(context.getContentResolver(), newPrinterInfo);
        Log.i(TAG, "setPrinterInfo : PrinterInfo :" + newPrinterInfo);
    }

    /**
     * Register dynamic broadcast receivers for main process
     * Cautions : if you register a receiver in application's onCreate() method, it registers multiple times for each
     * sub process.
     * So, it is highly recommended to register a receiver in the main process initialization routine only.
     */
    private void registerDynamicReceivers(Context context) {
        Log.i(TAG, "registerReceiver AppInstallEventReceiver");
        if (context != null) {
            IntentFilter filter = new IntentFilter(PackageContract.Intent.ACTION_PACKAGE_UNINSTALLED);
            filter.addAction(PackageContract.Intent.ACTION_PACKAGE_INSTALLED);
            filter.addAction(PackageContract.Intent.ACTION_PACKAGE_UPDATED);
            context.registerReceiver(new AppInstallEventReceiver(), filter,
                    PackageContract.Permission.PACKAGE_LIFECYCLE_EVENTS, null);
        }
    }

    // ====================== Private Class Area ======================
    private static final class InstanceHolder {
        private static final DeviceManagementService instance = new StandardDeviceManagementService();
    }
}
