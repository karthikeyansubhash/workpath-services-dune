/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients;

public interface CollectionResourceFacade<TCollected> {
    TCollected getMember(String id);
}
