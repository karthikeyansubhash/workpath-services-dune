// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import com.hp.sdd.servicediscovery.DuplicateAddressArbitrator;
import com.hp.sdd.servicediscovery.DuplicateAddressResolution;
import com.hp.sdd.servicediscovery.IDiscoveryListener;
import com.hp.sdd.servicediscovery.NetworkDevice;
import com.hp.sdd.servicediscovery.NetworkDiscovery;
import com.hp.sdd.servicediscovery.mdns.MDnsDiscovery;
import com.hp.sdd.servicediscovery.mdns.MDnsUtils;
import com.hp.sdd.servicediscovery.snmp.SnmpDiscovery;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Helper class to find printers on the network & identify those supporting OXPd
 */
@SuppressWarnings("unused")
public class OXPdDeviceDiscovery implements IDiscoveryListener {

    /**
     * Synchronization lock
     */
    private final Object mLock = new Object();
    /**
     * Context using discovery
     */
    private final Context mContext;
    /**
     * Network discovery instance
     */
    private final NetworkDiscovery mDiscovery;
    /**
     * OXPd requested features required by the client
     */
    private final int mRequiredFeatureSet;
    /**
     * Callback to invoke when an OXPd device is found
     */
    private OXPdDeviceFoundCallback mCallback;
    /**
     * Current Network device being examined
     */
    private NetworkDevice mCurrent = null;
    /**
     * List of NetworkDevices that need to be examined
     */
    private LinkedList<NetworkDevice> mDevicesToCheck = new LinkedList<NetworkDevice>();

    /**
     * IP to OXPd device mapping
     */
    private HashMap<String, OXPdDevice> mDeviceMap = new HashMap<String,OXPdDevice>();

    /**
     * Discovery failed callback
     */
    @Override
    public void onDiscoveryFailed() {
    }

    /**
     * Network device remove callback
     * @param networkDevice
     *              Network device no longer on the network
     */
    @Override
    public void onDeviceRemoved(NetworkDevice networkDevice) {
    }

    /**
     * Network device found callback
     * @param networkDevice
     *              Network device that was discovered
     */
    @Override
    public void onDeviceFound(final NetworkDevice networkDevice) {
        String ipAddress = networkDevice.getHostAddress();
        synchronized (mLock) {
            if (!mDeviceMap.containsKey(ipAddress)) {
                mDevicesToCheck.add(networkDevice);
                mDeviceMap.put(ipAddress, null);
                checkNextDevice();
            }
        }
    }

    /**
     * If idle, start the next device check
     */
    private void checkNextDevice() {
        synchronized (mLock) {
            if ((mCurrent == null) && !mDevicesToCheck.isEmpty()) {
                final NetworkDevice networkDevice = mDevicesToCheck.removeFirst();
                mCurrent = networkDevice;

                final OXPdDevice testDev = new OXPdDevice(mContext, networkDevice.getHostAddress());
                testDev.getOXPdUIConfigurationInstance(0, new OXPdDevice.RequestCallback() {
                    @Override
                    public void requestResult(OXPdDevice device, Message message) {
                        if (message.arg1 == OXPdDevice.REQUEST_RETURN_CODE__OK) {
                            testDev.isOXPdSupported(0, mRequiredFeatureSet, new OXPdDevice.RequestCallback() {
                                @Override
                                public void requestResult(OXPdDevice device, Message message) {
                                    device.closeDevice();
                                    boolean isSupported = false;
                                    if (message.obj instanceof Boolean) {
                                        isSupported = (Boolean) message.obj;
                                    }

                                    synchronized (mLock) {
                                        if (isSupported && (mCallback != null)) {
                                            mCallback.oxpdDeviceFound(networkDevice);
                                        }
                                        mCurrent = null;
                                        checkNextDevice();
                                    }
                                }
                            });
                        } else {
                            mCurrent = null;
                            checkNextDevice();
                        }
                    }
                });
            }
        }
    }

    /**
     * Activity discovery finished callback
     */
    @Override
    public void onActiveDiscoveryFinished() {
    }

    /**
     * Discovery finished callback
     */
    @Override
    public void onDiscoveryFinished() {

    }

    /**
     * Interface clients must implement to be informed of an OXPd device available on the network
     */
    @SuppressWarnings("WeakerAccess")
    public interface OXPdDeviceFoundCallback {
        void oxpdDeviceFound(NetworkDevice networkDevice);
    }

    /**
     * Constructor
     * @param context
     *              Context initiating discovery
     */
    public OXPdDeviceDiscovery(Context context) {
        this(context, OXPdDevice.OXPD_FEATURE_ANY, null);
    }

    /**
     * Constructor
     * @param context
     *              Context initiating discovery
     * @param requiredFeatures
     *              OXPd features required by the client
     */
    public OXPdDeviceDiscovery(Context context, int requiredFeatures) {
        this(context, requiredFeatures, null);
    }

    /**
     * Constructor
     * @param context
     *              Context initiating discovery
     * @param networkIFC
     *              Network interface to run discovery on
     */
    @SuppressWarnings("WeakerAccess")
    public OXPdDeviceDiscovery(Context context, String networkIFC) {
        this(context, OXPdDevice.OXPD_FEATURE_ANY, networkIFC);
    }

    /**
     * Constructor
     * @param context
     *              Context initiating discovery
     * @param requiredFeatures
     *              OXPd features required by the client
     * @param networkIFC
     *              Network interface to run discovery on
     */
    @SuppressWarnings("WeakerAccess")
    public OXPdDeviceDiscovery(Context context, int requiredFeatures, String networkIFC) {
        mContext = context;
        mRequiredFeatureSet = OXPdDevice.validatedFeatureSet(requiredFeatures);
        mDiscovery = getPrinterDiscoveryInstance(context, true, networkIFC);
    }

    /**
     * Start discovery
     * @param callback
     *              Device found callback
     */
    public void start(OXPdDeviceFoundCallback callback) {
        synchronized (mLock) {
            mDeviceMap.clear();
            mDevicesToCheck.clear();
            mCurrent = null;
            mCallback = callback;
            mDiscovery.addDiscoveryListener(this);
            mDiscovery.startDiscovery();
        }
    }

    /**
     * Stop discovery
     */
    public void stop() {
        synchronized (mLock) {
            mCallback = null;
            mDiscovery.removeDiscoveryListener(this);
            mDiscovery.stopDiscovery();
            mDevicesToCheck.clear();
            mCurrent = null;
            mDeviceMap.clear();
        }
    }

    /**
     * Create and configure a {@link NetworkDiscovery} instance
     * @param context
     *              Context initiating discovery
     * @param active
     *              Active or passive discovery
     * @param networkIFC
     *              Network interface to run discovery on,
     * @return
     *              A configured {@link NetworkDiscovery} instance
     */
    private static NetworkDiscovery getPrinterDiscoveryInstance(Context context, boolean active, String networkIFC) {
        NetworkDiscovery discovery = new NetworkDiscovery(
                context,
                active,
                networkIFC,
                0,
                new DuplicateAddressArbitrator() {
                    @Override
                    public DuplicateAddressResolution duplicateResolution(List<NetworkDevice> currentDevices, NetworkDevice newDevice) {
                        if (currentDevices.size() == 1) {
                            NetworkDevice oldDevice = currentDevices.get(0);
                            if ((oldDevice.getDiscoveryMethod() == NetworkDevice.DiscoveryMethod.SNMP_DISCOVERY)
                                    && (newDevice.getDiscoveryMethod() == NetworkDevice.DiscoveryMethod.MDNS_DISCOVERY)) {
                                Bundle attrs = new Bundle();
                                attrs.putString(MDnsUtils.ATTRIBUTE__DEV_ID, oldDevice.getTxtAttributes().getString(MDnsUtils.ATTRIBUTE__DEV_ID));
                                newDevice.addAttributes(attrs);
                                return new DuplicateAddressResolution(newDevice, oldDevice);
                            } else if ((oldDevice.getDiscoveryMethod() == NetworkDevice.DiscoveryMethod.MDNS_DISCOVERY)
                                    && (newDevice.getDiscoveryMethod() == NetworkDevice.DiscoveryMethod.SNMP_DISCOVERY)) {
                                Bundle attrs = new Bundle();
                                attrs.putString(MDnsUtils.ATTRIBUTE__DEV_ID, newDevice.getTxtAttributes().getString(MDnsUtils.ATTRIBUTE__DEV_ID));
                                oldDevice.addAttributes(attrs);
                                return new DuplicateAddressResolution(oldDevice, oldDevice);
                            }
                        }

                        return new DuplicateAddressResolution(newDevice, null);
                    }
                }
        );

        final Resources resources = context.getResources();
        if (!TextUtils.isEmpty((networkIFC))) {
            String[] mdnsServices = new String[]{
                    "_pdl-datastream._tcp"
            };

            discovery.addDiscoveryMethod(new MDnsDiscovery(mdnsServices));
        }

        if (TextUtils.isEmpty(networkIFC)) {
            discovery.addDiscoveryMethod(new SnmpDiscovery(context, false));
        }
        return discovery;
    }
}
