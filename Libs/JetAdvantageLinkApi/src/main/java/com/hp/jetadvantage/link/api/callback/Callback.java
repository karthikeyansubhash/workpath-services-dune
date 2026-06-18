// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.callback;

import com.hp.jetadvantage.link.common.annotation.MobileApi;

import java.security.Signature;

/**
 * <p>Interface Callback is a generic interface to allow the interaction between application and Link SDK service.</p>
 *
 * @since API 1
 */
@MobileApi
public interface Callback {
    /**
     * <p>A signingCallback interface for processing remote scan using the application private key.</p>
     *
     * @since API 1
     */
    @MobileApi
    interface SigningCallback {
        /**
         * <p>Calls to request signing to the application with provided data and signature.
         * Application must use its private key to update signature before signing.</p>
         *
         * @param signature Signature
         * @param data binary input data for signing
         * @return bye[] Signed data which is capsulized with application signature
         *
         * @since API 1
         */
        byte[] sign(Signature signature, byte[] data);
    }
}
