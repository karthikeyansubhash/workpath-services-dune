/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.authentication;

import java.net.URI;
import com.hp.net.http.HttpClient;

import com.hp.ext.clients.BaseResourceFacade;

public class SessionResourceFacade extends BaseResourceFacade implements SessionResource {

    public static final String name = "session";

    private ForceLogoutOperationResourceFacade forceLogout = null;

    public SessionResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        forceLogout = new ForceLogoutOperationResourceFacade(httpClient, serviceUri,
                path + "/" + ForceLogoutOperationResourceFacade.name);
    }

    @Override
    public ForceLogoutOperationResourceFacade forceLogout() {
        return this.forceLogout;
    }

}
