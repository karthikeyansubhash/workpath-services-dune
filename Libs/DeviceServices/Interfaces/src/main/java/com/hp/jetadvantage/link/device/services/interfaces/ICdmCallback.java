/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.interfaces;

import com.hp.ws.cdm.pubsub.Message;

import java.util.List;

public interface ICdmCallback {
    void onChangeEvent(List<Message> reports);
}
