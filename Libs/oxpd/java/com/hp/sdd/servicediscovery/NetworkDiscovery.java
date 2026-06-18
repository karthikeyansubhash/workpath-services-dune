// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.servicediscovery;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.hp.sdd.servicediscovery.mdns.MDnsResolve;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("unused")
public class NetworkDiscovery {

    private static boolean mIsDebuggable = false;
    public static final int FALLBACK_DELAY = 5000;
    private static final int MAX_ACTIVE_QUERIES = 10;
    private static final int MAX_DELAY_SECONDS = 60;
    private static final String TAG = "NetworkDiscovery";
    private static final int BUFFER_LENGTH = 4 * 1024;

    private final List<IDiscoveryListener> mListeners = new ArrayList<IDiscoveryListener>();
    private final Object mLock = new Object();
    private final LinkedHashMap<String, NetworkDevice> mDiscoveredPrinters = new LinkedHashMap<String, NetworkDevice>();
    private final HashMap<String, List<NetworkDevice>> mDiscoveredPrintersByIP = new HashMap<String, List<NetworkDevice>>();
    private final List<IDiscovery> mDiscoveryMethods = new ArrayList<IDiscovery>();
    private final Context mContext;
    private final String mNetworkIFC;

    private Thread mQueryThread = null;
    private int mQueriesSent = 0;
    private ListenerThread mListenerThread = null;
    private boolean mIsActiveDiscovery = false;
    private final int mFallbackDelay;
    private final DuplicateAddressArbitrator mArbitrator;


    private final List<DiscoveryFilter> mFilters = new ArrayList<DiscoveryFilter>();
    private static final HandlerThread sHandlerThread = new HandlerThread("DiscoveryThread");

    private static class DiscoveryHandler extends Handler {

        private final WeakReference<NetworkDiscovery> mDiscovery;

        public DiscoveryHandler(NetworkDiscovery networkDiscovery, Looper looper) {
            super(looper);
            mDiscovery = new WeakReference<NetworkDiscovery>(networkDiscovery);
        }

        @Override
        public void handleMessage(Message msg) {
            NetworkDiscovery discovery = mDiscovery.get();
            if ((msg == null) || (discovery == null)) return;
            if (msg.obj instanceof DatagramPacket) {
                DatagramPacket packet = (DatagramPacket) msg.obj;
                discovery.processIncomingPacket(packet);
            } else if (msg.obj instanceof ExternalDiscovery) {
                ArrayList<DiscoveryFilter> filters = new ArrayList<DiscoveryFilter>();
                synchronized (discovery.mLock) {
                    filters.addAll(discovery.mFilters);
                }
                ExternalDiscovery extDiscovery = (ExternalDiscovery) msg.obj;
                List<NetworkDevice> devices = extDiscovery.getDevicesFound();
                for (NetworkDevice device : devices) {
                    discovery.processDevice(device, filters);
                }
            }

        }
    }
    private DiscoveryHandler mHandler;

    public NetworkDiscovery(Context context) {
        this(context, true, null, FALLBACK_DELAY);
    }

    public NetworkDiscovery(Context context,
                            boolean activeDiscovery,
                            String networkIFCName,
                            int fallbackDelay) {
        this(context, activeDiscovery, networkIFCName, fallbackDelay, null);
    }

    public NetworkDiscovery(Context context,
                            boolean activeDiscovery,
                            String networkIFCName,
                            int fallbackDelay,
                            DuplicateAddressArbitrator arbitrator) {
        switch(sHandlerThread.getState()) {
            case NEW:
                sHandlerThread.start();
                break;
            default:
                break;
        }
        mContext = context.getApplicationContext();
        mIsActiveDiscovery = activeDiscovery;
        mNetworkIFC = (TextUtils.isEmpty(networkIFCName) ? null : networkIFCName);
        mFallbackDelay = fallbackDelay;
        mArbitrator = arbitrator;
        mHandler = new DiscoveryHandler(this, sHandlerThread.getLooper());
    }

    public void addDiscoveryMethod(final IDiscovery discoveryMethod) {
        mDiscoveryMethods.add(discoveryMethod);
        if (discoveryMethod instanceof ExternalDiscovery) {
            final ExternalDiscovery externalDiscovery = (ExternalDiscovery) discoveryMethod;
            externalDiscovery.registerObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    Message msg = mHandler.obtainMessage();
                    msg.obj = externalDiscovery;
                    mHandler.sendMessage(msg);
                }
            });
        }
    }

    public void stopDiscovery() {
        synchronized (mLock) {
            if (mListenerThread != null) {
                mListenerThread.releaseSocket();
                mListenerThread.cancel(true);
                mListenerThread = null;
            }
            stopQueryThread();
        }
    }

    public void rediscover(boolean isActive) {
        synchronized (mLock) {
            stopDiscovery();
            startDiscovery();
        }
    }

    public void startDiscovery() {
        synchronized (mLock) {
            mDiscoveredPrinters.clear();
            mDiscoveredPrintersByIP.clear();
            for (IDiscovery discoverMethod : mDiscoveryMethods) {
                discoverMethod.clear();
            }
            if (mListenerThread == null) {
                mListenerThread = new ListenerThread();
                mListenerThread.start();
            }
        }
    }

    public int getNumberOfDiscoveredPrinters() {
        return mDiscoveredPrinters.size();
    }

    public ArrayList<NetworkDevice> getDiscoveredDevices() {
        return new ArrayList<NetworkDevice>(mDiscoveredPrinters.values());
    }

    public boolean haveDevicesBeenFound() {
        return !mDiscoveredPrinters.isEmpty();
    }

    public void addDiscoveryListener(IDiscoveryListener listener) {
        synchronized (mLock) {
            if (!this.mListeners.contains(listener))
                this.mListeners.add(listener);
            for(NetworkDevice device : mDiscoveredPrinters.values()) {
                listener.onDeviceFound(device);
            }
        }
    }

    public void removeDiscoveryListener(IDiscoveryListener listener) {
        synchronized (mLock) {
            this.mListeners.remove(listener);
        }
    }

    public void addDeviceFilter(DiscoveryFilter filter) {
        synchronized (mLock) {
            mFilters.add(filter);
        }
    }

    public void removeDeviceFilter(DiscoveryFilter filter) {
        synchronized (mLock) {
            mFilters.remove(filter);
        }
    }

    public void clearDeviceFilter() {
        synchronized (mLock) {
            mFilters.clear();
        }
    }

    public void setDeviceFilters(List<DiscoveryFilter> filters) {
        synchronized (mLock) {
            mFilters.clear();
            mFilters.addAll(filters);
        }
    }

    private class ListenerThread extends Thread {

        private DatagramSocket mSocket = null;

        private DatagramSocket createSocket() throws IOException {
            releaseSocket();

            // SNMP requires a broadcast socket, while mDNS requires a multicast socket with a TTL set.
            // Let's create an hybrid one and make everyone happy.
            MulticastSocket socket = NetworkUtils.createMulticastSocket(mContext, mNetworkIFC);
            socket.setBroadcast(true);
            socket.setReuseAddress(true);
            socket.setSoTimeout(0);
            mSocket = socket;
            return socket;
        }

        private void releaseSocket() {
            if ((mSocket != null) && !mSocket.isClosed()) {
                mSocket.close();
            }
            mSocket = null;
        }

        private DatagramSocket getSocket() {
            return mSocket;
        }

        AtomicBoolean mCancelled = new AtomicBoolean(false);
        public void cancel(boolean interrupt) {
            mCancelled.set(true);
            if (interrupt) interrupt();
        }

        public boolean isCancelled() {
            return mCancelled.get();
        }

        private boolean setupSocket() {
            try {
                createSocket();
            } catch (IOException e) {
                releaseSocket();
                e.printStackTrace();
            }
            return (getSocket() != null);
        }

        @Override
        public void run() {
            if (setupSocket()) {
                startQueryThread();
                receiveResponsePackets(getSocket());
                fireDiscoveryFinished();
            } else {
                fireDiscoveryFailed();
            }
        }

        /*
         * The algorithm for receiving the response packets will decrease the
         * timeout according to the search results. Socket timeout starts with a
         * value of 8s. If no printer is found, the socket timeout is decreased by
         * 2s until it reaches 0 and the algorithm stops listening for new
         * responses. So when no printer is found, the sequence of socket timeouts
         * is 8, 6, 4, and 2s, adding up to 20s of wait time. When a printer is
         * found, both timeout and decay are set to 5s, which means that the thread
         * will receive new packets with a timeout of 5s until no packet is
         * received. The first time the receive method reaches the timeout without
         * receiving any packets, the algorithm finishes.
         */
        private void receiveResponsePackets(final DatagramSocket socket) {
            if (socket == null) return;

            while (!Thread.interrupted() && !isCancelled()) {
                try {
                    byte buffer[] = new byte[BUFFER_LENGTH];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    if (!Thread.interrupted()) {
                        Message msg = Message.obtain(mHandler);
                        msg.obj = packet;
                        mHandler.sendMessage(msg);
                    }
                } catch (IOException ioe) {
                    if (socket.isClosed() || !socket.isConnected()) {
                        break;
                    }
                }
            }
        }

    }

    private DatagramSocket getSocket() {
        synchronized (mLock) {
            return mListenerThread != null ? mListenerThread.getSocket() : null;
        }
    }


    boolean mUseFallback = false;
    private void sendQueryPacket() throws IOException {
        final DatagramSocket socket = getSocket();
        if (socket == null) return;

        if (mDiscoveryMethods.isEmpty()) return;

        List<DatagramPacket> datagramList = new ArrayList<DatagramPacket>();
        for (IDiscovery discoverMethod : mDiscoveryMethods) {
            if (!discoverMethod.isFallback() || mUseFallback)
                Collections.addAll(datagramList, discoverMethod.createQueryPackets());
        }

        for(DatagramPacket packet : datagramList) {
            socket.send(packet);
        }

        mQueriesSent++;
    }


    private void sendResolvePacket(MDnsResolve resolve) throws IOException {
        final DatagramSocket socket = getSocket();
        if (socket == null) return;

        List<DatagramPacket> datagramList = new ArrayList<DatagramPacket>();
        for (IDiscovery discoverMethod : mDiscoveryMethods) {
            if (!discoverMethod.isFallback() || mUseFallback)
                Collections.addAll(datagramList, resolve.createQueryPackets());
        }

        for(DatagramPacket packet : datagramList) {
            socket.send(packet);
        }

        mQueriesSent++;
    }

    /*
    * Returns the next wait interval, in milliseconds, using an exponential
    * backoff algorithm.
    */
    private int getQueryDelayInMillis() {
        int delayInSeconds = 1;

        int first = 1;
        int second = 1;
        int index;

        if (mQueriesSent > MAX_ACTIVE_QUERIES) {
            delayInSeconds = MAX_DELAY_SECONDS;
        } else {

            for (index = 1; index < mQueriesSent; index++) {
                if (index <= 1) {
                    delayInSeconds = index;
                } else {
                    delayInSeconds = first + second;
                    first = second;
                    second = delayInSeconds;
                }
            }
        }

        if (delayInSeconds >= MAX_DELAY_SECONDS) {
            delayInSeconds = MAX_DELAY_SECONDS;
            if (mIsActiveDiscovery) {
                mIsActiveDiscovery = false;
                fireActiveDiscoveryFinished();
            }
        }
        // convert seconds to millis
        return delayInSeconds * 1000;
    }

    private void startQueryThread() {
        synchronized (mLock) {
            stopQueryThread();
            final ListenerThread listenerThread = mListenerThread;
            mQueryThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (listenerThread == null) return;
                    mQueriesSent = (mIsActiveDiscovery ? 0 : MAX_ACTIVE_QUERIES);
                    boolean retry = true;
                    final long startTime = System.currentTimeMillis();
                    mUseFallback = false;
                    for (IDiscovery discoverMethod : mDiscoveryMethods) {
                        if (discoverMethod instanceof ExternalDiscovery) {
                            ((ExternalDiscovery)discoverMethod).start();
                        }
                    }
                    while (retry && !Thread.currentThread().isInterrupted() && !listenerThread.isCancelled()) {
                        try {
                            sendQueryPacket();
                            Thread.sleep(getQueryDelayInMillis());
                            if (!mUseFallback && ((System.currentTimeMillis() - startTime) > mFallbackDelay)) {
                                mUseFallback = mDiscoveredPrinters.isEmpty();
                            }
                            // coverity[FB.REC_CATCH_EXCEPTION]
                        } catch (Exception e) {
                            retry = false;
                        }
                    }
                    for (IDiscovery discoverMethod : mDiscoveryMethods) {
                        if (discoverMethod instanceof ExternalDiscovery) {
                            ((ExternalDiscovery)discoverMethod).stop();
                        }
                    }

                }
            });
            mQueryThread.start();
        }
    }
    private void stopQueryThread() {
        if (mQueryThread != null) {
            if (mQueryThread.isAlive()) mQueryThread.interrupt();
            mQueryThread = null;
        }
    }

    private void processDevice(NetworkDevice device, List<DiscoveryFilter> filters) {
        boolean satisfiesFilters = true;
        for(DiscoveryFilter filter : filters) {
            satisfiesFilters = filter.meetsFilterCriteria(device);
            if (!satisfiesFilters) break;
        }
        if (!satisfiesFilters) {
            return;
        }

        NetworkDevice discoveredNetworkDevice = mDiscoveredPrinters.get(device.getDeviceIdentifier());

        if (mIsDebuggable)
            Log.d(TAG, "discoveredNetworkDevice key "  + device.getDeviceIdentifier());
        if ((discoveredNetworkDevice != null) && !device.getInetAddress().equals(discoveredNetworkDevice.getInetAddress())) {
            fireDeviceRemoved(discoveredNetworkDevice);
            discoveredNetworkDevice = null;
            if (mIsDebuggable)
                Log.d(TAG, "discoveredNetworkDevice removed key "  + device.getDeviceIdentifier());
        }

        String ipAddress = device.getHostAddress();
        List<NetworkDevice> ipList = mDiscoveredPrintersByIP.get(ipAddress);
        if (ipList == null) ipList = new ArrayList<NetworkDevice>();

        if (!ipList.isEmpty() && (mArbitrator != null)) {
            DuplicateAddressResolution resolution = mArbitrator.duplicateResolution(ipList, device);

            if (resolution == null) {
                return;
            }

            if (resolution.mDeviceToRemove != null) {
                if (device == resolution.mDeviceToRemove) {
                    device = resolution.mDeviceToAdd;
                } else if (resolution.mDeviceToAdd == resolution.mDeviceToRemove) {
                    //found a new printer with a different discovery protocol snmp or mdns
                    //update the device with the duplicateResultion and fireDeviceFound as a new printer instance
                    //later
                    if (mIsDebuggable)
                        Log.d(TAG, "resolution Device Found mDeviceToAdd");
                    device = resolution.mDeviceToAdd;
                } else {
                    if (mIsDebuggable)
                        Log.d(TAG, "fireDeviceRemoved");
                    device = resolution.mDeviceToAdd;
                    mDiscoveredPrinters.remove(resolution.mDeviceToRemove.getDeviceIdentifier());
                    ipList.remove(resolution.mDeviceToRemove);
                    fireDeviceRemoved(resolution.mDeviceToRemove);

                }
            }
        }

        if (device == null) return;

        // updating the previously stored networkDevice object
        if (discoveredNetworkDevice != null) {
            discoveredNetworkDevice.addDiscoveryInstance(device);
            device = discoveredNetworkDevice;
            if (mIsDebuggable)
                Log.d(TAG, "mDiscoveredPrinters, addDiscoveryInstance");
        }
        else
        {

            //fireDeviceFound to client if we found a new printer instance
            //if the printer has not been discovered before.
            // snmp and mdns/ipp have different discovery identifier
                 mDiscoveredPrinters.put(device.getDeviceIdentifier(), device);
                if (mIsDebuggable)
                    Log.d(TAG, "mDiscoveredPrinters, new printer instance");
                fireDeviceFound(device);
        }

        if (!ipList.contains(device)) {
            //update the client with the first time we saw a printer discovered
            if (mIsDebuggable)
                Log.d(TAG, "mDiscoveredPrintersByIP, new printer instance");
            ipList.add(device);
            mDiscoveredPrintersByIP.put(ipAddress, ipList);
            fireDeviceFound(device);
        }

    }

    private void processIncomingPacket(DatagramPacket packet) {
        List<DiscoveryFilter> filters = new ArrayList<DiscoveryFilter>();
        List<ServiceParser> serviceParsers = new ArrayList<ServiceParser>();
        int port = packet.getPort();
        for (IDiscovery discoveryMethod : mDiscoveryMethods) {
            if (port == discoveryMethod.getPort()) {
                serviceParsers.addAll(discoveryMethod.parseResponse(packet));
                break;
            }
        }

        synchronized (mLock) {
            filters.addAll(mFilters);
        }

        for (ServiceParser parser : serviceParsers) {
            //if port > 0 we asume the service is ready to use
            if (parser.getPort()>0) {
                NetworkDevice device = new NetworkDevice(parser);
                processDevice(device, filters);
            }
            else{
                //we need to resolve the service.
                String[] services = {parser.getServiceName()};
                MDnsResolve resolve = new MDnsResolve(services);

                try {
                    this.sendResolvePacket(resolve);
                }
                catch (Exception e){
                    //TODO retry the call.
                    e.printStackTrace();
                }
            }
        }
    }

    private void fireDeviceRemoved(NetworkDevice networkDevice) {
        synchronized (mLock) {
            for (int i = this.mListeners.size() - 1; i >= 0; i--) {
                this.mListeners.get(i).onDeviceRemoved(networkDevice);
            }
        }
    }

    private void fireDeviceFound(NetworkDevice networkDevice) {
        synchronized (mLock) {
            for (int i = this.mListeners.size() - 1; i >= 0; i--) {
                this.mListeners.get(i).onDeviceFound(networkDevice);
            }
        }
    }

    private void fireDiscoveryFinished() {
        synchronized (mLock) {
            for (int i = this.mListeners.size() - 1; i >= 0; i--) {
                this.mListeners.get(i).onDiscoveryFinished();
            }
        }
    }

    private void fireActiveDiscoveryFinished() {
        synchronized (mLock) {
            for (int i = this.mListeners.size() - 1; i >= 0; i--) {
                this.mListeners.get(i).onActiveDiscoveryFinished();
            }
        }
    }

    private void fireDiscoveryFailed() {
        synchronized (mLock) {
            for (int i = this.mListeners.size() - 1; i >= 0; i--) {
                this.mListeners.get(i).onDiscoveryFailed();
            }
        }
    }
}
