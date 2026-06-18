// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.discovery;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This library is used to retrieve and parse a target device's OXPd Discovery Tree.
 * The OXPd service libraries provided in this SDK ({@link com.hp.oxpdlib.deviceinfo.OXPdDeviceInfo},
 * {@link com.hp.oxpdlib.scan.OXPdScan}, {@link com.hp.oxpdlib.print.OXPdPrint},
 * {@link com.hp.oxpdlib.uiconfiguration.OXPdUIConfiguration})
 * make use of a device feature called the 'OXPd Discovery Tree' to determine whether the target
 * device supports the required OXPd service and version, as well as the URL path to the required
 * service.
 * Those service libraries must be initialized with the OXPd Discovery Tree before they can be used.
 * @hide
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class OXPdDiscovery {

    // discovery tree path
    private static final String discoveryTreeResource = "/DevMgmt/DiscoveryTree.xml";

    // device instance
    private final OXPdDevice mDevice;
    // discovery tree url
    private final URL mDiscoveryTreeUrl;

    /**
     * Create an OXPdDiscovery instance
     * @param device
     *              OXPd device instance
     * @hide
     */
    public OXPdDiscovery(OXPdDevice device) {
        mDevice = device;
        URL url;
        try {
            url = mDevice.getDeviceUrl(discoveryTreeResource);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            url = null;
        }
        mDiscoveryTreeUrl = url;
    }

    /**
     * Retrieve and parse the discovery tree
     * @return
     *              Parsed discovery tree
     * @throws Error
     *              When an error occurs
     * @hide
     */
    @SuppressWarnings("DuplicateThrows")
    public DiscoveryTree GetOXPdDiscoveryTree() throws Error, Exception {
        HttpRequestResponseContainer requestResponse = mDevice.doHttpGet(mDiscoveryTreeUrl,
                30000, 30000, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
        if (requestResponse.exception != null) {
            throw requestResponse.exception;
        }
        return DiscoveryTree.parseDiscoveryTree(mDevice, requestResponse);
    }
}
