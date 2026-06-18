// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.settingsui.interfaces;

/**
 * Callback definition for {@link ConfigurationFragment}.
 * It should be usually implemented by owner activity.
 */
public interface ConfigurationCallback {
    /**
     * Notifies about configuration fail.
     *
     * @param msg error message
     */
    void onFailed(String msg);

    /**
     * Called then some attributes are missed and job submit is not possible
     *
     * @param msg error message
     */
    void onFailedCreateIntent(String msg);

    /**
     * Called then Configure fragment doesn't allow operaition, for example,
     * then some mandatory data is not input.
     *
     * @param enable true if button should be enabled,
     *               false - otherwise
     */
    void enableActionButton(boolean enable);
}
