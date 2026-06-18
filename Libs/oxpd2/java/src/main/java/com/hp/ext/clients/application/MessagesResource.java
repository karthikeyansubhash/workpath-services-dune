/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.application;

import com.hp.ext.clients.CreatableResource;
import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.application.Message;
import com.hp.ext.service.application.Message_Create;
import com.hp.ext.service.application.Messages;

public interface MessagesResource extends ReadableResource<Messages>, CreatableResource<Message, Message_Create> {

}
