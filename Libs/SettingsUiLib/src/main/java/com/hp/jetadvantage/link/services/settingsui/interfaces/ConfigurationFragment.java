// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.settingsui.interfaces;

import android.content.Intent;

/**
 * Interface to keep common behaviour of all configure fragments
 *
 * T - capabilities class
 */
public interface ConfigurationFragment<T, F> {

    /** Arguments key for attributes of configuration */
    String ARG_ATTRS = "attrs";
    String ARG_CLIENT_API_LEVEL = "client_api_level";

    /**
     * @return Intent to launch job filled with configured data
     */
    Intent getConfiguredIntent(String rid);

    /**
     * Callback to notify capabilities were loaded by the owner.
     *
     * @param caps loaded capabilities
     * @param errorMsg if any error happened
     */
    void onCapabilitiesLoaded(T caps, final String errorMsg);

    void onDefaultCapabilitiesLoaded(F defaultCaps, final String errorMsg);
}
