/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients;

import java.net.URI;
import com.hp.net.http.HttpClient;

public abstract class BaseCollectionResourceFacade<TCollected> extends BaseResourceFacade
        implements CollectionResourceFacade<TCollected> {

    public abstract TCollected getMember(String id);

    public BaseCollectionResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

}
