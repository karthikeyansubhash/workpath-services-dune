package com.hp.jetadvantage.link.services.printlet.model;

import com.hp.jetadvantage.link.services.printlet.model.PrintTicket;
import com.hp.oxpdlib.OXPdDevice;

public class PrintJobParams {
    /**
     * {@link PrintTicket} value
     */
    private final PrintTicket mPrintTicket;
    /**
     * Status callback request identifier
     */
    private final int mStatusRequestID;

    /**
     * Constructor
     *
     * @param printTicket     {@link PrintTicket} value
     * @param statusRequestID Status callback request identifier
     */
    public PrintJobParams(PrintTicket printTicket, int statusRequestID) {
        mPrintTicket = printTicket;
        mStatusRequestID = statusRequestID;
    }

    public PrintTicket getPrintTicket() {
        return mPrintTicket;
    }
}
