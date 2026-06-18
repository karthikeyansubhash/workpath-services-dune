package com.hp.ext.clients.solutionmanager;

import static com.hp.ext.clients.ResourceFacadeHelper.readBytes;

import com.hp.ext.clients.BaseResourceFacade;
import com.hp.ext.clients.CustomObjectMapper;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.solutionManager.CertificateAuthoritiesImportRequest;
import com.hp.ext.service.solutionManager.CertificateAuthorities_Import;
import com.hp.net.http.HttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ImportCertificateAuthoritiesOperationResourceFacade extends BaseResourceFacade
        implements ImportCertificateAuthoritiesOperationResource {

    public static String name = "import";

    public ImportCertificateAuthoritiesOperationResourceFacade(HttpClient httpClient, URI serviceUri, String path) {
        super(httpClient, serviceUri, path);
    }

    @Override
    public CompletableFuture<CertificateAuthorities_Import> executeAsync(String accessToken, CertificateAuthoritiesImportRequest importRequest, InputStream certificate, String certificateFileName,
                                                                        String queryParams) throws URISyntaxException, IOException {
        URI uri = createRequestUri(getServiceUri(), getPath(), queryParams);
        CustomObjectMapper<CertificateAuthoritiesImportRequest> mapper = new CustomObjectMapper(CertificateAuthoritiesImportRequest.class);

        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.MIXED);

        requestBodyBuilder.addFormDataPart("content", "content.json",
            RequestBody.create(
                mapper.writeValueAsString(importRequest),
                MediaType.parse("application/json")));

        requestBodyBuilder.addFormDataPart("certificate", "certificate.pem",
            RequestBody.create(readBytes(certificate), MediaType.parse("application/x-pem-file")));

        return ResourceFacadeHelper.executeResourceOperationAsync(getHttpClient(), uri, accessToken, requestBodyBuilder.build(), CertificateAuthorities_Import.class);
    }

    public CompletableFuture<CertificateAuthorities_Import> executeAsync(String accessToken, CertificateAuthoritiesImportRequest importRequest, InputStream certificate, String certificateFileName) throws URISyntaxException, IOException{
          return executeAsync(accessToken, importRequest, certificate, certificateFileName,null);
    }

}

