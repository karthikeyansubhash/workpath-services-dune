// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.ssp;

import com.hp.jetadvantage.link.api.ErrorCode;
import com.hp.jetadvantage.link.api.Result;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class to contain main APIs to handle {@link ErrorCode}.
 * This helper is away from {@link ErrorCode} in order to
 * have all utils implementation inside of
 */
public final class SpsCauseHelper {
    /** To be added to PROCESSING kind of items */
    private static final String PROCESSING_POSTFIX = "Pi";
    private static ConcurrentHashMap<String, ErrorCode> sCausesTable = new ConcurrentHashMap<>();

    static {
        for(ErrorCode cause : ErrorCode.values()) {
            sCausesTable.put(composeCauseName(cause), cause);
        }
    }

    private SpsCauseHelper() {
        // nothing to do here
    }

    /**
     * Using special logic creates name for provided ErrorCode.
     * Most of names - class names of XOA Create Job classes and Processing Info classes
     *
     * @param cause to prepare name for
     *
     * @return String composed name
     */
    private static String composeCauseName(final ErrorCode cause) {
        // name of the corresponding class will be composed every time
        final String[] splitted = cause.name().split("_");
        final StringBuilder cb = new StringBuilder();

        for (String part : splitted) {
            if (part.length() > 1) {
                cb.append(part.substring(0, 1).toUpperCase()).append(part.substring(1).toLowerCase());
            } else {
                cb.append(part.toUpperCase());
            }
        }

        if (cause.kind == Result.SpsCauseKind.PROCESSING) {
            cb.append(PROCESSING_POSTFIX);
        }

        return cb.toString();
    }

    /**
     * Prepares ourt string with {@link ErrorCode} items
     *
     * @param causes to be inserted
     *
     * @return result string which contains Json array of causes
     */
    public static String generateJsonWithCauses(final List<ErrorCode> causes) {
        return "Status: " + causes;
    }
}
