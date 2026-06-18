package com.hp.ext.clients.solutionmanager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import com.hp.ext.clients.ExecutableResource;
import com.hp.ext.service.solutionManager.CertificateAuthoritiesImportRequest;
import com.hp.ext.service.solutionManager.CertificateAuthorities_Import;

public interface ImportCertificateAuthoritiesOperationResource extends ExecutableResource<CertificateAuthorities_Import> {
    CompletableFuture<CertificateAuthorities_Import> executeAsync(String accessToken, CertificateAuthoritiesImportRequest importRequest, InputStream certificate, String certificateFileName, String queryParams) throws URISyntaxException, IOException;
}
