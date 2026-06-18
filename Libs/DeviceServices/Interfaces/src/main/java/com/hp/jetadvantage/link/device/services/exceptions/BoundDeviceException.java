package com.hp.jetadvantage.link.device.services.exceptions;

/**
 * Exception thrown when an operation requiring a bound device is attempted when no device is bound.
 * Any device connection issue will also result in this exception being thrown.
 */
public class BoundDeviceException extends RuntimeException {

    public BoundDeviceException() {

    }

    public BoundDeviceException(String message) {
        super(message);
    }
}
