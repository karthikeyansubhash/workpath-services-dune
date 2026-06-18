/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.sdksupport;

import java.net.URI;
import com.hp.net.http.HttpClient;

import com.hp.ext.clients.BaseResourceFacade;

public class FunctionsResourceFacade extends BaseResourceFacade implements FunctionsResource {

    public static final String name = "functions";

    private MacroExpanderResourceFacade macroExpander = null;
    private SolutionTokenResourceFacade solutionToken = null;

    public FunctionsResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
        macroExpander = new MacroExpanderResourceFacade(httpClient, serviceUri,
            path + "/" + MacroExpanderResourceFacade.name);
        solutionToken = new SolutionTokenResourceFacade(httpClient, serviceUri,
            path + "/" + SolutionTokenResourceFacade.name);
    }

    @Override
    public MacroExpanderResourceFacade macroExpander() {
        return macroExpander;
    }

    @Override
    public SolutionTokenResourceFacade solutionToken() {
        return solutionToken;
    }
}
