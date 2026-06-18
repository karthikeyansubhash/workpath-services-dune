/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.application;

import com.hp.ext.clients.DeletableResource;
import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.application.Message;
import com.hp.ext.types.base.DeleteContent;

public interface MessageResource extends ReadableResource<Message>, DeletableResource<DeleteContent> {

}
