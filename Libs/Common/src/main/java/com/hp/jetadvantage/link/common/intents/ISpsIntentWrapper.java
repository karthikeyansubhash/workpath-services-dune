// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.common.intents;

import android.content.Intent;

/**
 * Base class for wrapping intents which are shared across applications
 *
 * @param <DataType> A class representing the parameters for the intent
 */
@SuppressWarnings({"unused"})
public interface ISpsIntentWrapper<DataType> {
    /**
     * Adds the parameters provided to the intent
     *
     * @param params The parameters class
     * @return The intent with the added DataType parameters
     */
    Intent putIntentParams(DataType params);

    /**
     * Extract the parameters from the Intent
     *
     * @return Datatype parameters
     */
    DataType getIntentParams();


}
