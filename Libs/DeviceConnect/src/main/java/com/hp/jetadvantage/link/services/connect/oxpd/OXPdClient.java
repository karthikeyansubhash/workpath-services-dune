// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.connect.oxpd;

import android.content.Context;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import com.hp.jetadvantage.link.services.connect.OXPdConnect;
import com.hp.oxpdlib.OXPdDevice;
import com.hp.oxpdlib.accessories.OXPdAccessories;
import com.hp.oxpdlib.copy.OXPdCopy;
import com.hp.oxpdlib.deviceinfo.OXPdDeviceInfo;
//import com.hp.oxpdlib.print.OXPdPrint;
import com.hp.oxpdlib.scan.OXPdScan;
import com.hp.oxpdlib.uiconfiguration.OXPdUIConfiguration;

import java.security.Signature;

public class OXPdClient {
    private final static String TAG = "OXPdClient";

    private static final String ADMIN_USER_NAME = "admin";

    private OXPdCopy mCopy;
    private OXPdDevice mDevice;
    private OXPdDeviceInfo mDeviceInfo;
    //private OXPdPrint mPrint;
    private OXPdScan mScan;
    private OXPdUIConfiguration mUIConfiguration;
    private OXPdAccessories mAccessories;

    private com.hp.oxpdlib.copy.Error mCopyError;
    private com.hp.oxpdlib.deviceinfo.Error mDeviceInfoError;
    //private com.hp.oxpdlib.print.Error mPrintError;
    private com.hp.oxpdlib.scan.Error mScanError;
    private com.hp.oxpdlib.uiconfiguration.Error mUIConfigurationError;
    private com.hp.oxpdlib.accessories.Error mAccessoriesError;

    private boolean mSupportsReserveRemoteUIContext;

    private OXPdDevice.RequestCallback mMonitorRequestCallback;
    private OXPdDevice.ExternalSecurityProvider mSecurityProvider;
    private String oxpdApplicationUUID;
    private String packageName;

    private static OXPdClient instance;

    private class OXPdDeviceWrapper extends OXPdDevice {

        private OXPdDeviceWrapper(Context context, String hostName, ExternalSecurityProvider externalSigner,
                ClientInfoProvider clientInfoProvider) throws IllegalArgumentException {
            super(context, hostName, externalSigner, clientInfoProvider);
        }

        @Override
        public void queueRequest(DeviceProcessRequestCallback requestCallback, Object params, int requestID,
                final RequestCallback callback) {
            RequestCallback wrapperCallback = new RequestCallback() {
                @Override
                public void requestResult(OXPdDevice device, Message message) {
                    if (mMonitorRequestCallback != null) {
                        mMonitorRequestCallback.requestResult(device, message);
                    }

                    if (callback != null) {
                        callback.requestResult(device, message);
                    }
                }
            };
            super.queueRequest(requestCallback, params, requestID, wrapperCallback);
        }
    }

    public static OXPdClient getInstance(final Context context, final String hostName) {
        // if previous initialization was failed (OXPd sub-instances are null - try to create again)
        if (instance == null
                || instance.mDeviceInfo == null
                || instance.mUIConfiguration == null
                || (instance.mDevice != null && !hostName.equals(instance.mDevice.getHostName()))) {
            synchronized (OXPdClient.class) {
                // check again
                if (instance == null
                        || instance.mDeviceInfo == null
                        || instance.mUIConfiguration == null
                        || (instance.mDevice != null && !hostName.equals(instance.mDevice.getHostName()))) {
                    instance = new OXPdClient(context.getApplicationContext(), hostName);
                }
            }
        }
        return instance;
    }

    private class ProxySecurityProvider implements OXPdDevice.ExternalSecurityProvider {
        @Override
        public byte[] signData(Signature signature, byte[] bytes) {
            return mSecurityProvider != null ? mSecurityProvider.signData(signature, bytes) : new byte[0];
        }
    }

    private class ProxyClientInfoProvider implements OXPdDevice.ClientInfoProvider {
        ProxyClientInfoProvider() {
        }

        @Override
        public String getPackageName() {
            return packageName;
        }

        @Override
        public String getOXPdID() {
            return oxpdApplicationUUID;
        }
    }

    private OXPdClient(final Context context, final String hostName) {
        mDevice = new OXPdDeviceWrapper(context,
                hostName,
                new ProxySecurityProvider(),
                new ProxyClientInfoProvider());

        mDevice.updateUserCredentials(ADMIN_USER_NAME, null);

        // check if Remote Reserve UI supported
        try {
            // disable legacy mode
            PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).edit()
                    .putBoolean(context.getPackageName() + "#oxpd_legacy_mode", false).apply();

            OXPdAsyncCallFuture callFuture = new OXPdAsyncCallFuture();
            mDevice.getOXPdUIConfigurationInstance(0, callFuture);
            Message message = callFuture.get();
            if (message.arg1 == OXPdDevice.REQUEST_RETURN_CODE__OK) {
                // device supports Remote Reserve UI
                mSupportsReserveRemoteUIContext = true;
                Log.i(TAG, "Device supports Remote Reserve UI Context");
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to check UIConfiguration Remote Reserve UI Context support", e);
        } finally {
            // enable back legacy mode (CURRENTLY ALWAYS DISABLED TO RESTRICT DEVICES WITH REMOTE UI SUPPORT)
            PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).edit()
                    .putBoolean(context.getPackageName() + "#oxpd_legacy_mode", true).apply();
        }

        try {
            OXPdAsyncCallFuture callFuture = new OXPdAsyncCallFuture();
            mDevice.getOXPdDeviceInfoInstance(0, callFuture);
            Message message = callFuture.get();
            if (message.arg1 == OXPdDevice.REQUEST_RETURN_CODE__OK) {
                mDeviceInfo = (OXPdDeviceInfo) message.obj;
                Log.i(TAG, "OXPd DeviceInfo instance successfully retrieved");
            } else if (message.obj instanceof Exception) {
                throw (Exception) message.obj;
            }
        } catch (com.hp.oxpdlib.deviceinfo.Error error) {
            Log.e(TAG, "Failed to obtain OXPd DeviceInfo instance", error);
            mDeviceInfoError = error;
        } catch (Exception e) {
            Log.e(TAG, "Failed to obtain OXPd DeviceInfo instance", e);
        }

//        try {
//            OXPdAsyncCallFuture callFuture = new OXPdAsyncCallFuture();
//            mDevice.getOXPdPrintInstance(0, callFuture);
//            Message message = callFuture.get();
//            if (message.arg1 == OXPdDevice.REQUEST_RETURN_CODE__OK) {
//                mPrint = (OXPdPrint) message.obj;
//                if (OXPdDevice.isEmulator())
//                    mPrint.setIppUrlSim(OXPdConnect.getInstance().getDuneSimulatorIP());
//                Log.i(TAG, "OXPd Print instance successfully retrieved");
//            } else if (message.obj instanceof Exception) {
//                throw (Exception) message.obj;
//            }
//        } catch (com.hp.oxpdlib.print.Error error) {
//            Log.e(TAG, "Failed to obtain OXPd Print instance", error);
//            mPrintError = error;
//        } catch (Exception e) {
//            Log.e(TAG, "Failed to obtain OXPd Print instance", e);
//        }

        try {
            OXPdAsyncCallFuture callFuture = new OXPdAsyncCallFuture();
            mDevice.getOXPdScanInstance(0, callFuture);
            Message message = callFuture.get();
            if (message.arg1 == OXPdDevice.REQUEST_RETURN_CODE__OK) {
                mScan = (OXPdScan) message.obj;
                Log.i(TAG, "OXPd Scan instance successfully retrieved");
            } else if (message.obj instanceof Exception) {
                throw (Exception) message.obj;
            }
        } catch (com.hp.oxpdlib.scan.Error error) {
            Log.e(TAG, "Failed to obtain OXPd Scan instance", error);
            mScanError = error;
        } catch (Exception e) {
            Log.e(TAG, "Failed to obtain OXPd Scan instance", e);
        }

        try {
            OXPdAsyncCallFuture callFuture = new OXPdAsyncCallFuture();
            mDevice.getOXPdUIConfigurationInstance(0, callFuture);
            Message message = callFuture.get();
            if (message.arg1 == OXPdDevice.REQUEST_RETURN_CODE__OK) {
                mUIConfiguration = (OXPdUIConfiguration) message.obj;
                Log.i(TAG, "OXPd UIConfiguration instance successfully retrieved");
            } else if (message.obj instanceof Exception) {
                throw (Exception) message.obj;
            }
        } catch (com.hp.oxpdlib.uiconfiguration.Error error) {
            Log.e(TAG, "Failed to obtain OXPd UIConfiguration instance", error);
            mUIConfigurationError = error;
        } catch (Exception e) {
            Log.e(TAG, "Failed to obtain OXPd UIConfiguration instance", e);
        }

//        try {
//            OXPdAsyncCallFuture callFuture = new OXPdAsyncCallFuture();
//            mDevice.getOXPdCopyInstance(0, callFuture);
//            Message message = callFuture.get();
//            if (message.arg1 == OXPdDevice.REQUEST_RETURN_CODE__OK) {
//                mCopy = (OXPdCopy) message.obj;
//                Log.i(TAG, "OXPd Copy instance successfully retrieved");
//            } else if (message.obj instanceof Exception) {
//                throw (Exception) message.obj;
//            }
//        } catch (com.hp.oxpdlib.copy.Error error) {
//            Log.e(TAG, "Failed to obtain OXPd Copy instance", error);
//            mCopyError = error;
//        } catch (Exception e) {
//            Log.e(TAG, "Failed to obtain OXPd Copy instance", e);
//        }
/*
        try {
            OXPdAsyncCallFuture callFuture = new OXPdAsyncCallFuture();
            mDevice.getOXPdAccessoriesInstance(0, callFuture);
            Message message = callFuture.get();
            if (message.arg1 == OXPdDevice.REQUEST_RETURN_CODE__OK) {
                mAccessories = (OXPdAccessories) message.obj;
                Log.i(TAG, "OXPd Accessories instance successfully retrieved");
            } else if (message.obj instanceof Exception) {
                throw (Exception) message.obj;
            }
        } catch (com.hp.oxpdlib.accessories.Error error) {
            Log.e(TAG, "Failed to obtain OXPd Accessories instance", error);
            mAccessoriesError = error;
        } catch (Exception e) {
            Log.e(TAG, "Failed to obtain OXPd Accessories instance", e);
        }

 */
    }

    public OXPdDevice getDevice() {
        return mDevice;
    }

    public OXPdDeviceInfo getDeviceInfo() throws com.hp.oxpdlib.deviceinfo.Error {
        if (mDeviceInfo != null) {
            return mDeviceInfo;
        }

        // if it's connection error - report it to connection state monitor
        if (mMonitorRequestCallback != null && mDeviceInfoError != null) {
            if (mDeviceInfoError.name == com.hp.oxpdlib.deviceinfo.ErrorName.AjaxError) {
                mMonitorRequestCallback.requestResult(mDevice,
                    Message.obtain(null, 0, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0, mDeviceInfoError));
            }
        }

        throw (mDeviceInfoError != null) ? mDeviceInfoError :
            new com.hp.oxpdlib.deviceinfo.Error(com.hp.oxpdlib.deviceinfo.ErrorName.ServiceNotFound,
                    "OXPd:DeviceInfo is not supported on the target device");
    }

//    public OXPdPrint getPrint() throws com.hp.oxpdlib.print.Error {
//        if (mPrint != null) {
//            return mPrint;
//        }
//
//        // if it's connection error - report it to connection state monitor
//        if (mMonitorRequestCallback != null && mPrintError != null) {
//            if (mPrintError.name == com.hp.oxpdlib.print.ErrorName.AjaxError) {
//                mMonitorRequestCallback.requestResult(mDevice,
//                    Message.obtain(null, 0, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0, mPrintError));
//            }
//        }
//
//        throw (mPrintError != null) ? mPrintError :
//            new com.hp.oxpdlib.print.Error(com.hp.oxpdlib.print.ErrorName.ServiceNotFound,
//                "OXPd:Print is not supported on the target device");
//    }

    public OXPdScan getScan() throws com.hp.oxpdlib.scan.Error {
        if (mScan != null) {
            return mScan;
        }

        // if it's connection error - report it to connection state monitor
        if (mMonitorRequestCallback != null && mScanError != null) {
            if (mScanError.name == com.hp.oxpdlib.scan.ErrorName.AjaxError) {
                mMonitorRequestCallback.requestResult(mDevice,
                    Message.obtain(null, 0, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0, mScanError));
            }
        }

        throw (mScanError != null) ? mScanError :
            new com.hp.oxpdlib.scan.Error(com.hp.oxpdlib.scan.ErrorName.ServiceNotFound,
                "OXPd:Scan is not supported on the target device");
    }

    public OXPdUIConfiguration getUIConfiguration() throws com.hp.oxpdlib.uiconfiguration.Error {
        if (mUIConfiguration != null) {
            return mUIConfiguration;
        }

        // if it's connection error - report it to connection state monitor
        if (mMonitorRequestCallback != null && mUIConfigurationError != null) {
            if (mUIConfigurationError.name == com.hp.oxpdlib.uiconfiguration.ErrorName.AjaxError) {
                mMonitorRequestCallback.requestResult(mDevice,
                    Message.obtain(null, 0, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0, mUIConfigurationError));
            }
        }

        throw (mUIConfigurationError != null) ? mUIConfigurationError :
            new com.hp.oxpdlib.uiconfiguration.Error(com.hp.oxpdlib.uiconfiguration.ErrorName.ServiceNotFound,
                "OXPd:UIConfiguration is not supported on the target device");
    }

    public OXPdCopy getCopy() throws com.hp.oxpdlib.copy.Error {
        if (mCopy != null) {
            return mCopy;
        }

        // if it's connection error - report it to connection state monitor
        if (mMonitorRequestCallback != null && mCopyError != null) {
            if (mCopyError.name == com.hp.oxpdlib.copy.ErrorName.AjaxError) {
                mMonitorRequestCallback.requestResult(mDevice,
                        Message.obtain(null, 0, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0, mCopyError));
            }
        }

        throw (mCopyError != null) ? mCopyError :
                new com.hp.oxpdlib.copy.Error(com.hp.oxpdlib.copy.ErrorName.ServiceNotFound,
                        "OXPd:Copy is not supported on the target device");
    }

    public OXPdAccessories getAccessories() throws com.hp.oxpdlib.accessories.Error {
        if (mAccessories != null) {
            return mAccessories;
        }

        // if it's connection error - report it to connection state monitor
        if (mMonitorRequestCallback != null && mAccessoriesError != null) {
            if (mAccessoriesError.name == com.hp.oxpdlib.accessories.ErrorName.AjaxError) {
                mMonitorRequestCallback.requestResult(mDevice,
                        Message.obtain(null, 0, OXPdDevice.REQUEST_RETURN_CODE__EXCEPTION, 0, mAccessoriesError));
            }
        }

        throw (mAccessoriesError != null) ? mAccessoriesError :
                new com.hp.oxpdlib.accessories.Error(com.hp.oxpdlib.accessories.ErrorName.ServiceNotFound,
                        "OXPd:Accessories is not supported on the target device");
    }

    public boolean isSupportedReserveRemoteUIContext() {
        return mSupportsReserveRemoteUIContext;
    }

    public void updateMonitorRequestCallback(OXPdDevice.RequestCallback callback) {
        mMonitorRequestCallback = callback;
    }

    public void updateSecurityProvider(OXPdDevice.ExternalSecurityProvider securityProvider) {
        mSecurityProvider = securityProvider;
    }

    public void updateOxpdApplicationUUID(String oxpdApplicationUUID) {
        this.oxpdApplicationUUID = oxpdApplicationUUID;
    }

    public void updatePackageName(String packageName) {
        this.packageName = packageName;
    }

    public void updateCredentials(String userName, String password) {
        if (userName != null) {
            mDevice.updateUserCredentials(userName, password);
        }
    }

    public void updateConnectTimeout(int connectTimeout) {
        mDevice.updateConnectTimeout(connectTimeout * 1000);
    }

    public void updateReadTimeout(int readTimeout) {
        mDevice.updateReadTimeout(readTimeout * 1000);
    }

    public static void clear() {
        if (instance != null) {
            instance.mDevice = null;
            instance.mDeviceInfo = null;
            //instance.mPrint = null;
            instance.mScan = null;
            instance.mUIConfiguration = null;
            instance.mCopy = null;

            instance = null;
        }
    }
}
