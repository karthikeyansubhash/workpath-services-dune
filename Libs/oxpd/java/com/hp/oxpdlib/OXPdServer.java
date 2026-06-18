// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.util.SparseArray;

import com.hp.sdd.jabberwocky.chat.HttpRequest;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorCompletionService;

import fi.iki.elonen.NanoHTTPD;

/**
 * HTTP server capable of receiving an OXPd push scan locally
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class OXPdServer extends NanoHTTPD {
    private static final String TAG = "OXPdServer";

    /** incoming SOAP request body */
    private static final String POST_DATA = "postData";

    /** Synchronization lock */
    private static final Object sLock = new Object();
    /** Bit set to assign array slots */
    private static final BitSet sBitSet = new BitSet();
    /** Sparse array to hold OXPd device instances */
    private static final SparseArray<ServerRequestHandler> sDevices = new SparseArray<ServerRequestHandler>();
    /** Application Context used to obtain assets */
    private final Context mContext;
    /** Port to start server listening on */
    private int mPort;
    /** Server instance */
    @SuppressLint("StaticFieldLeak")
    private static OXPdServer sServerInstance = null;
    /** Panel port */
    private static final int DEFAULT_PANEL_PORT = 3000;

    /**
     * Server request handler
     */
    public interface ServerRequestHandler {
        /**
         * Get the application context
         * @return The application context
         */
        Context getContext();

        /**
         * Handle an HTTP request
         * @param session HTTP request ession
         * @return HTTP response
         */
        NanoHTTPD.Response handleServerRequest(NanoHTTPD.IHTTPSession session);
    }

    /**
     * Temporary file manager that stores files in application directory
     */
    static class OXPdServerTempFileManager implements TempFileManager {

        /** Temporary directory */
        private final File tmpdir;

        /** List of temp files */
        private final List<TempFile> tempFiles = new ArrayList<TempFile>();

        /**
         * Constructor
         * @param tempDir Temporary file directory
         */
        OXPdServerTempFileManager(File tempDir) {
            if (!tempDir.exists() && !tempDir.mkdirs()) {
                // if failed to create specified directory - use standard tmp folder
                tempDir = new File(System.getProperty("java.io.tmpdir"));
            }
            this.tmpdir = tempDir;
            if (!tmpdir.exists()) {
                //noinspection ResultOfMethodCallIgnored
                tempDir.mkdirs();
            }
        }

        /**
         * Delete temp files
         */
        @Override
        public void clear() {
            for (TempFile file : this.tempFiles) {
                try {
                    file.delete();
                } catch (Exception ignored) {
                }
            }
            this.tempFiles.clear();
        }

        /**
         * Create a temporary file
         * @param filename_hint Filename hint
         * @return New temp file
         * @throws Exception if error occurs
         */
        @Override
        public TempFile createTempFile(String filename_hint) throws Exception {
            DefaultTempFile tempFile = new DefaultTempFile(this.tmpdir);
            this.tempFiles.add(tempFile);
            return tempFile;
        }
    }

    /**
     * Add an OXPd device instance and return the OXPdServer instance
     * @param device OXPd device instance to add
     * @return OXPd Server instance
     */
    public static OXPdServer addServerClient(final ServerRequestHandler device) {
        synchronized (sLock) {
            if (sDevices.indexOfValue(device) == -1) {
                int index = 0;//sBitSet.nextClearBit(0);
                Log.d(TAG, "Adding new server at " + index);
                sBitSet.set(index);
                sDevices.put(index, device);
                if (sServerInstance == null) {
                    String hostname = getLocalIpAddress(device.getContext(), null);
                    sServerInstance = new OXPdServer(hostname, device.getContext());
                    sServerInstance.setTempFileManagerFactory(new TempFileManagerFactory() {
                        @Override
                        public TempFileManager create() {
                            return new OXPdServerTempFileManager(device.getContext().getFilesDir());
                        }
                    });
                }
            }
            return sServerInstance;
        }
    }

    /**
     * Start the server
     */
    public void startServer() {
        if (sServerInstance != null) {
            // start server only if required
            if (!sServerInstance.isAlive()) {
                try {
                    // For SDK Simulator
                    //sServerInstance.start(OXPdDevice.Constants.SOCKET_TIMEOUT);
                    sServerInstance.start(OXPdDevice.Constants.SOCKET_TIMEOUT_120);
                    Log.d(TAG, "Server started at port " + sServerInstance.getListeningPort());
                } catch (BindException e) {
                    Log.w(TAG, "Port is in use. Probably Server already started in another application");
                } catch (IOException e) {
                    Log.e(TAG, "Failed to start the server", e);
                }
            } else {
                Log.i(TAG, "Server is alive, skipping starting server");
            }
        } else {
            Log.w(TAG, "Server instance is null, addOXPdDevice must be called before");
        }
    }

    /**
     * Return the local IP address
     * @return IP address
     */
    static String getLocalIpAddress(Context context, String networkInterface) {
        Log.d(TAG, "Getting address...");
        if (networkInterface == null) {
            WifiManager wifiMgr = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if ((wifiMgr != null) && wifiMgr.isWifiEnabled()) {
                WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
                int ip = wifiInfo.getIpAddress();
                String formettedIP = String.format(Locale.US, "%d.%d.%d.%d",
                        (ip & 0xff),
                        (ip >> 8 & 0xff),
                        (ip >> 16 & 0xff),
                        (ip >> 24 & 0xff));
                return formettedIP;
            }
        }

        Enumeration<NetworkInterface> en;
        try {
            en = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            return null;
        }

        while (en.hasMoreElements()) {
            NetworkInterface netIfc = en.nextElement();
            if (networkInterface == null || networkInterface.equals(netIfc.getName())) {
                for (Enumeration<InetAddress> enumIpAddr = netIfc.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    //the condition after && is missing in your snippet, checking instance of inetAddress
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        }

        return null;
    }

    /**
     * Return the local IP address
     * @return Local ip address
     */
    public String getLocalIpAddress(String networkInterface) {
        return getLocalIpAddress(mContext, networkInterface);
    }

    /**
     * Return the local port
     * @return local port
     */
    public int getLocalPort() {
        return mPort;
    }

    /**
     * Remove an OXPd device. Stops the OPXd server when no devices are attached.
     * @param device OXPd device instance to remove
     */
    public static void removeServerClient(ServerRequestHandler device) {
        synchronized (sLock) {
            int index = sDevices.indexOfValue(device);
            if (index >= 0) {
                Log.d(TAG, "Removing server at " + index);
                sDevices.remove(index);
                sBitSet.clear(index);
                if (sBitSet.isEmpty()) {
                    sServerInstance.stop();
                    sServerInstance = null;
                    Log.d(TAG, "Server stopped");
                }
            }
        }
    }

    /**
     * Constructor
     * @param context Application context
     */
    private OXPdServer(String hostname, Context context) {
        super(OXPdDevice.isPanel() && !OXPdDevice.isEmulator() ?
                        hostname : null,
                OXPdDevice.isPanel() ? DEFAULT_PANEL_PORT  : context.getResources().getInteger(R.integer.default_local_receiver_port));
        mContext = context.getApplicationContext();
        mPort = (OXPdDevice.isPanel() ? DEFAULT_PANEL_PORT  : context.getResources().getInteger(R.integer.default_local_receiver_port));
    }

    /**
     * Process the HTTP session request
     * @param session HTTP session
     * @return HTTP response
     */
    @Override
    public Response serve(IHTTPSession session) {
        Log.d(TAG, "request is served");

        try {
            if(session != null) {
                Log.d(TAG, "session is not null");
            } else {
                Log.d(TAG, "session is null");
            }

            ContentType contentType = new ContentType(session.getHeaders().get(HttpRequest.HEADER__CONTENT_TYPE.toLowerCase())).tryUTF8();
            session.getHeaders().put(HttpRequest.HEADER__CONTENT_TYPE.toLowerCase(), contentType.getContentTypeHeader());
            Log.d(TAG, "contentType is updated.");
        } catch (Exception e) {
            Log.e(TAG, "contextType is not updated. " + e.getMessage());
        }

        String uri = session.getUri();
        Log.d(TAG, "Serving " + session.getMethod() + " " + uri);
        String[] parts = uri.split("/");

        if (parts.length <= 1) {
            // bad request
            Log.w(TAG, "Uri path is not recognized: " + uri);
            return newFixedLengthResponse(Response.Status.BAD_REQUEST, NanoHTTPD.MIME_PLAINTEXT, null, 0);
        } else if (parts.length == 2) {
            // assume get on base path
            if (session.getMethod() == Method.GET){
                String name = parts[parts.length - 1];
                try {
                    AssetFileDescriptor fd = mContext.getAssets().openFd(name);
                    return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.getMimeTypeForFile(name), fd.createInputStream(), fd.getLength());
                } catch(IOException e) {
                    Log.e(TAG, "Failed to open file " + name +", reason: " + e.getMessage());
                    return newFixedLengthResponse(Response.Status.BAD_REQUEST, NanoHTTPD.MIME_PLAINTEXT, e.getMessage());
                }
            }
            Log.w(TAG, "Method " + session.getMethod() + " is not supported for uri " + uri);
            return newFixedLengthResponse(Response.Status.BAD_REQUEST, NanoHTTPD.MIME_PLAINTEXT, null, 0);
        } else {
            // link back to server request handler
            int index;
            try {
                index = Integer.valueOf(parts[1]);

                ServerRequestHandler device;
                synchronized (sLock) {
                    if(sDevices != null) Log.d(TAG, "devices:" + sDevices.size());
                    else Log.d(TAG, "devices is null");

                    device = sDevices.get(index);
                }
                if (device == null) {
                    Log.w(TAG, "Device with index " + index + " not found, skip processing");
                    return newFixedLengthResponse(Response.Status.GONE, NanoHTTPD.MIME_PLAINTEXT, null, 0);
                } else {
                    Log.i(TAG, "continue to process the request: " + uri);
                }

                return device.handleServerRequest(session);
            } catch (NumberFormatException ignored) {
                Log.e(TAG, "Failed to parse device identifier: " + parts[1]);
                return newFixedLengthResponse(Response.Status.BAD_REQUEST, NanoHTTPD.MIME_PLAINTEXT,
                        "Failed to parse device identifier: " + parts[1]);
            }
        }
    }

    /**
     * Get the URL receiving path for the specified device
     * @param device OXPd device instance
     * @return String URL
     */
    public String getDevicePathIdentifier(ServerRequestHandler device) {
        synchronized (sLock) {
            return String.valueOf(sDevices.indexOfValue(device));
        }
    }
}
