/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutionmanager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.ExecutableResource;
import com.hp.ext.service.solutionManager.CertificateAuthorities_Export;
import com.hp.ext.service.solutionManager.CertificateAuthority_Export;

public interface ExportCertificateAuthorityOperationResource
extends ExecutableResource<SimpleEntry<CertificateAuthorities_Export, byte[]>> {
    CompletableFuture<SimpleEntry<CertificateAuthority_Export, byte[]>> executeAsync(String accessToken, String queryParams) throws  URISyntaxException, IOException;

}
