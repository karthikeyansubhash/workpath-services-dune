// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.jabberwocky.chat;

import android.os.Build;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.X509TrustManager;

/**
 * First-Use Pinning Trust Manager
 */
public class PinningTrustManager implements X509TrustManager {
    /** Server public key hash */
    private byte[] mPin = new byte[0];
    /** Keystore to store values in */
    private KeyStore mKS = null;
    /** Allow Keystore update */
    private boolean mAllowCertificateUpdate = false;
    /** Trust the first cert if unknown */
    private boolean mTrustFirstCert;

    /**
     * Pinning exception thrown for untrusted or mismatched certificates
     */
    @SuppressWarnings("WeakerAccess")
    public static abstract class PinningCertificateException extends CertificateException {

        /** Server hostname */
        public final String mHost;
        /** Server certificate */
        public final Certificate mCertificate;

        /**
         * Constructor
         * @param hostName Server host
         * @param certificate Server certificate
         * @param message Exception message
         */
        protected PinningCertificateException(String hostName, Certificate certificate, String message) {
            super(message);
            mCertificate = certificate;
            mHost = hostName;
        }
    }

    /**
     * Untrusted certificate
     */
    public static class UntrustedCertificateException extends PinningCertificateException {
        /** {@inheritDoc} */
        UntrustedCertificateException(String hostName, Certificate certificate, String message)  {
            super(hostName, certificate, message);
        }
    }

    /**
     * Certificate mismatch
     */
    public static class CertificateMismatchException extends PinningCertificateException {
        /** {@inheritDoc} */
        CertificateMismatchException(String hostName, Certificate certificate, String message) {
            super(hostName, certificate, message);
        }
    }

    /**
     * Constructor
     * @param ks Keystore to use
     */
    @SuppressWarnings("WeakerAccess")
    public PinningTrustManager(KeyStore ks) {
        this(ks, (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2));
    }

    /**
     * Constructor
     * @param ks Keystore to use
     * @param trustFirstCert Trust the first certificate we receive
     */
    @SuppressWarnings("WeakerAccess")
    public PinningTrustManager(KeyStore ks, boolean trustFirstCert) {
        mKS = ks;
        mTrustFirstCert = trustFirstCert;
    }

    /**
     * Allow new certificate to be accepted
     */
    public void acceptNewCertificate() {
        mAllowCertificateUpdate = true;
    }

    /** {@inheritDoc} */
    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        throw new CertificateException("Client certificates not supported!");
    }

    /** {@inheritDoc} */
    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        try {
            // TODO temp until further investigation
            acceptNewCertificate();
            //

            if ((x509Certificates == null) || (x509Certificates.length == 0))
                throw new CertificateException("no certificates");
            X509Certificate cert = x509Certificates[0];
            String hostName = cert.getSubjectX500Principal().getName();

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                try {
                    MessageDigest digest = MessageDigest.getInstance("SHA1");
                    if (mAllowCertificateUpdate || mTrustFirstCert) {
                        mPin = digest.digest(cert.getPublicKey().getEncoded());
                    } else {
                        if (Arrays.equals(mPin, digest.digest(cert.getPublicKey().getEncoded()))) {
                            return;
                        }
                        throw new CertificateMismatchException(hostName, cert, "Host certificate does not match previous value");
                    }
                    throw new UntrustedCertificateException(hostName, cert, "untrusted certificate");
                } catch (NoSuchAlgorithmException e) {
                    throw new CertificateException("could not compute digest");
                }
            } else {
                try {
                    boolean updateKS = false;
                    try {
                        if (mKS == null) {
                            mKS = KeyStore.getInstance("AndroidKeyStore");
                            mKS.load(null);
                        }
                    } catch (Exception ignored) {
                        throw new CertificateException("cannot load ks");
                    }
                    if (mKS.containsAlias(hostName)) {
                        if (mKS.isCertificateEntry(hostName)) {
                            Certificate oldCert = mKS.getCertificate(hostName);
                            if (cert.equals(oldCert)) return;
                            if (mAllowCertificateUpdate) {
                                updateKS = true;
                            } else if (oldCert instanceof X509Certificate) {
                                // check if old certificate expired already
                                X509Certificate x509Certificate = (X509Certificate) oldCert;
                                Date currentDate = Calendar.getInstance().getTime();
                                if (currentDate.after(x509Certificate.getNotAfter())) {
                                    updateKS = true;
                                }
                            }
                            // update keystore?
                            if (!updateKS) {
                                throw new CertificateMismatchException(hostName, cert, "Host certificate does not match previous value");
                            }
                        } else {
                            throw new CertificateException("keystore entry not a certificate");
                        }
                    } else if (mTrustFirstCert || mAllowCertificateUpdate) {
                        updateKS = true;
                    }
                    if (updateKS) {
                        // update entry
                        mKS.setCertificateEntry(hostName, cert);
                        return;
                    }
                    throw new UntrustedCertificateException(hostName, cert, "untrusted certificate");
                } catch (KeyStoreException e) {
                    throw new CertificateException(e.getMessage());
                }
            }
        } finally {
            mAllowCertificateUpdate = false;
            mTrustFirstCert = false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
