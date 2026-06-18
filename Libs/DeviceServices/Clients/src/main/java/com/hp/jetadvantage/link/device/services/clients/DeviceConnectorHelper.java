/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.clients;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;

public class DeviceConnectorHelper {
    public static synchronized OkHttpClient createUnsafeOkHttpClient(OkHttpClient.Builder builder) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            ConnectionSpec spec;
            spec = new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                    .supportsTlsExtensions(true)
                    .tlsVersions(TlsVersion.TLS_1_3, TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                    .cipherSuites(
                            //CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256,
                            //CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384,
                            //CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, // 20+
                            //CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, //20+
                            //CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256, //20+
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA, // 11+
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA, // 11+
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,   // 11+
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,   // 11+
                            CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA,     // 1+
                            CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA,     // 1+
                            CipherSuite.TLS_DHE_DSS_WITH_AES_128_CBC_SHA,   // 1-22
                            CipherSuite.TLS_DHE_DSS_WITH_AES_256_CBC_SHA,   // 11-22
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,   // 11-23
                            CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA,     // 11-23
                            CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA,       // 9+
                            CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA       // 9+
                    )
                    .build();


            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(DeviceClient.Constant.HTTP_CONNECTION_REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .connectionSpecs(Collections.singletonList(spec))
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .retryOnConnectionFailure(true)
                    .build(); //For SDK Simulator

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized OkHttpClient createOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(DeviceClient.Constant.HTTP_CONNECTION_REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(0, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build();
    }

    public static final String generateHttpsPrefixURL(@NonNull String host, int port) {
        if (port <= 0) return "https://" + host;
        return "https://" + host + ":" + port;
    }

    public static final String generateHttpPrefixURL(@NonNull String host, int port, boolean isTls) {
        if(isTls) {
            if (port <= 0) return "https://" + host;
            return "https://" + host + ":" + port;
        }
        else {
            if (port <= 0) return "http://" + host;
            return "http://" + host + ":" + port;
        }
    }
}
