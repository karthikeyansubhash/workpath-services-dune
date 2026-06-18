package com.hp.jetadvantage.link.device.services.interfaces;

/**
 * Interface for device authentication session change callbacks.
 * This interface is used to notify when a user signs in or signs out of a device on the front panel.
 */
public interface IDeviceAuthSessionChangeCallback {
    /**
     * Called when a walk-up user signs in to the device.
     */
    void onSignIn();

    /**
     * Called when a walk-up user signs out of the device.
     */
    void onSignOut();
}
