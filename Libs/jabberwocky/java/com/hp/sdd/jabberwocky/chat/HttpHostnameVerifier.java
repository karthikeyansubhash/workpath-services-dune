// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.jabberwocky.chat;

import android.text.TextUtils;
import android.util.Patterns;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * HTTP Host name verifier
 */
public class HttpHostnameVerifier implements HostnameVerifier {

    /** Skip verification */
    private boolean mSkipVerification;

    /**
     * Constructor
     * @param host Hostname expected to be verified
     */
    public HttpHostnameVerifier(String host) {
        mSkipVerification = (TextUtils.isEmpty(host) || Patterns.IP_ADDRESS.matcher(host).matches());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verify(String hostname, SSLSession sslSession) {
        return mSkipVerification || HttpsURLConnection.getDefaultHostnameVerifier().verify(hostname, sslSession);
    }
}
