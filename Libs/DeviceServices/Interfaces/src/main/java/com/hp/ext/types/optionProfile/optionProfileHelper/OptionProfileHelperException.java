package com.hp.ext.types.optionProfile.optionProfileHelper;

public class OptionProfileHelperException extends Exception {

    /**
     * Base OptionProfileHelperException constructor
     */
    public OptionProfileHelperException() {
        super();
    }

    /**
     * OptionProfileHelperException constructor.
     * 
     * @param message Message to show in the exception
     */
    public OptionProfileHelperException(String message) {
        super(message);
    }

    /**
     * Check to see if the provided optionsInstance is without conflicts
     * 
     * @param message Message to show in the exception
     * @param inner   Inner exception to add to the exception
     */
    public OptionProfileHelperException(String message, Exception inner) {
        super(message, inner);
    }
}
