/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.scanlet.adapter;

import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.hp.ext.service.scanJob.DestinationOptions;
import com.hp.ext.service.scanJob.EmailOptions;
import com.hp.ext.service.scanJob.EmailOptions_Body_Binding;
import com.hp.ext.service.scanJob.EmailOptions_Body_Value;
import com.hp.ext.service.scanJob.EmailOptions_Subject_Binding;
import com.hp.ext.service.scanJob.EmailOptions_Subject_Value;
import com.hp.ext.service.scanJob.FtpProtocolOptions;
import com.hp.ext.service.scanJob.FtpProtocolOptions_Path_Binding;
import com.hp.ext.service.scanJob.FtpProtocolOptions_Path_Value;
import com.hp.ext.service.scanJob.FtpProtocolOptions_Port_Binding;
import com.hp.ext.service.scanJob.FtpProtocolOptions_Port_Value;
import com.hp.ext.service.scanJob.FtpProtocolOptions_Server_Binding;
import com.hp.ext.service.scanJob.FtpProtocolOptions_Server_Value;
import com.hp.ext.service.scanJob.FtpProtocolOptions_TransferMode_Binding;
import com.hp.ext.service.scanJob.FtpProtocolOptions_TransferMode_Value;
import com.hp.ext.service.scanJob.HttpDestination;
import com.hp.ext.service.scanJob.HttpOptions;
import com.hp.ext.service.scanJob.LocalFolderOptions;
import com.hp.ext.service.scanJob.MetadataFileContent;
import com.hp.ext.service.scanJob.NetworkFolderOptions;
import com.hp.ext.service.scanJob.ProtocolOptions;
import com.hp.ext.service.scanJob.ScanJob;
import com.hp.ext.service.scanJob.ScanJob_Create;
import com.hp.ext.service.scanJob.ScanMetadataOptions;
import com.hp.ext.service.scanJob.ScanMetadataOptions_FileContentType_Binding;
import com.hp.ext.service.scanJob.ScanMetadataOptions_FileContentType_Value;
import com.hp.ext.service.scanJob.ScanMetadataOptions_FileContent_Binding;
import com.hp.ext.service.scanJob.ScanMetadataOptions_FileContent_Value;
import com.hp.ext.service.scanJob.ScanMetadataOptions_FileName_Binding;
import com.hp.ext.service.scanJob.ScanMetadataOptions_FileName_Value;
import com.hp.ext.service.scanJob.ScanOptions;
import com.hp.ext.service.scanJob.ScanOptions_FileName_Binding;
import com.hp.ext.service.scanJob.ScanOptions_FileName_Value;
import com.hp.ext.service.scanJob.ScanTicket;
import com.hp.ext.service.scanJob.SmbProtocolOptions;
import com.hp.ext.service.scanJob.SmbProtocolOptions_UncPath_Binding;
import com.hp.ext.service.scanJob.SmbProtocolOptions_UncPath_Value;
import com.hp.ext.service.scanJob.SmtpServerCredential;
import com.hp.ext.service.scanJob.SmtpServerOptions;
import com.hp.ext.service.scanJob.SmtpServerOptions_ServerAuthenticationRequired_Binding;
import com.hp.ext.service.scanJob.SmtpServerOptions_ServerAuthenticationRequired_Value;
import com.hp.ext.service.scanJob.SmtpServerOptions_ServerName_Binding;
import com.hp.ext.service.scanJob.SmtpServerOptions_ServerName_Value;
import com.hp.ext.service.scanJob.SmtpServerOptions_ServerPort_Binding;
import com.hp.ext.service.scanJob.SmtpServerOptions_ServerPort_Value;
import com.hp.ext.service.scanJob.SmtpServerOptions_ServerValidationRequired_Binding;
import com.hp.ext.service.scanJob.SmtpServerOptions_ServerValidationRequired_Value;
import com.hp.ext.service.scanJob.SmtpServerOptions_UseTls_Binding;
import com.hp.ext.service.scanJob.SmtpServerOptions_UseTls_Value;
import com.hp.ext.service.scanJob.TransmissionMode;
import com.hp.ext.types.base.InternetMediaType;
import com.hp.ext.types.common.EmailAddress;
import com.hp.ext.types.common.EmailDestination;
import com.hp.ext.types.common.EmailDestination_Address_Binding;
import com.hp.ext.types.common.EmailDestination_Address_Value;
import com.hp.ext.types.common.EmailDestination_DisplayName_Binding;
import com.hp.ext.types.common.EmailDestination_DisplayName_Value;
import com.hp.ext.types.common.FilePath;
import com.hp.ext.types.imaging.BindingFormat;
import com.hp.ext.types.imaging.PlexMode;
import com.hp.ext.types.network.FtpPath;
import com.hp.ext.types.network.FtpTransferMode;
import com.hp.ext.types.network.IpAddressOrHostName;
import com.hp.ext.types.network.PortNumber;
import com.hp.ext.types.network.UncPath;
import com.hp.ext.types.security.CredentialSourceType;
import com.hp.ext.types.security.UserCredential;
import com.hp.ext.types.security.UserCredential_Domain_Binding;
import com.hp.ext.types.security.UserCredential_Domain_Value;
import com.hp.ext.types.security.UserCredential_Password_Binding;
import com.hp.ext.types.security.UserCredential_Password_Value;
import com.hp.ext.types.security.UserCredential_UserName_Binding;
import com.hp.ext.types.security.UserCredential_UserName_Value;
import com.hp.ext.types.target.BasicAuthorization;
import com.hp.ext.types.target.BasicAuthorization_Password_Binding;
import com.hp.ext.types.target.BasicAuthorization_Password_Value;
import com.hp.ext.types.target.BasicAuthorization_Username_Binding;
import com.hp.ext.types.target.BasicAuthorization_Username_Value;
import com.hp.ext.types.target.BasicServerRetryBehavior;
import com.hp.ext.types.target.BasicServerRetryBehavior_MaxRetries_Binding;
import com.hp.ext.types.target.BasicServerRetryBehavior_MaxRetries_Value;
import com.hp.ext.types.target.BasicServerRetryBehavior_TimeoutSeconds_Binding;
import com.hp.ext.types.target.BasicServerRetryBehavior_TimeoutSeconds_Value;
import com.hp.ext.types.target.HostName;
import com.hp.ext.types.target.HttpAuthorization;
import com.hp.ext.types.target.HttpPath;
import com.hp.ext.types.target.HttpStyleClientCommon_Path_Binding;
import com.hp.ext.types.target.HttpStyleClientCommon_Path_Value;
import com.hp.ext.types.target.HttpStyleHostCommon_Host_Binding;
import com.hp.ext.types.target.HttpStyleHostCommon_Host_Value;
import com.hp.ext.types.target.RetryBehavior;
import com.hp.jetadvantage.link.api.scanner.EmailAddressInfo;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributesReader;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesReader;
import com.hp.jetadvantage.link.api.scanner.Scanlet;
import com.hp.jetadvantage.link.api.scanner.SmtpAttributes;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkInvalidParamException;
import com.hp.jetadvantage.link.services.common.exception.SdkNotSupportedException;
import com.hp.jetadvantage.link.services.scanlet.ScanConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScanTicketAdapter {
    private static final String TAG = Scanlet.TAG + "/TAdap";

    /**
     * get ScanTicket for the connected device
     *
     * @param scanAttributes        Scan Attributes from an app
     * @param defaultScanAttributes Default Scan Attributes
     * @return ScanTicket
     */
    public static ScanTicket getScanTicket(@NonNull ScanAttributesReader scanAttributes, @NonNull ScanAttributesReader defaultScanAttributes) throws SdkException {
        ScanTicket scanTicket = new ScanTicket();
        DestinationOptions destinationOptions = getDestinationOptions(scanAttributes);
        scanTicket.setDestinationOptions(destinationOptions);
        ScanOptions scanOptions = getScanOptions(scanAttributes, defaultScanAttributes);
        if (ScanConstants.LOCAL_FOLDER_DESTINATIONS.contains(scanAttributes.getDestination())) {
            scanTicket.setMetadataOptions(getMetadataOptions());
        }
        scanTicket.setScanOptions(scanOptions);
        return scanTicket;
    }

    /**
     * create a scan job by requesting to the device service
     *
     * @param packageName           request app's package name
     * @param deviceService         device service
     * @param scanAttributes        scan attributes
     * @param defaultScanAttributes default scan attributes
     * @return new scan Job ID, or null if failed
     * @throws SdkException
     */
    public static String createScanJob(@NonNull String packageName, @NonNull IDeviceScanJobService deviceService,
                                       @NonNull ScanAttributesReader scanAttributes, @NonNull ScanAttributesReader defaultScanAttributes) throws SdkException {

        ScanTicket scanTicket = ScanTicketAdapter.getScanTicket(scanAttributes, defaultScanAttributes);

        ScanJob_Create scanJobCreate = new ScanJob_Create();
        scanJobCreate.setScanTicket(scanTicket);
        ScanJob scanJob = deviceService.createScanJob(packageName, scanJobCreate);

        if (scanJob == null || scanJob.getScanJobId() == null) {
            SLog.e(TAG, "createScanJob: Failed to create a scan job. " + (scanJob == null ? "scanJob" : "scanJobId") + " "
                    + "is " + "null!");
            return null;
        }
        return scanJob.getScanJobId().toString();
    }

    /**
     * Retrieves the default scan attributes.
     *
     * @param packageName   the package name of the requesting application
     * @param deviceService the device scan job service interface used to query scan capabilities and attributes
     * @return a {@link ScanAttributesReader} containing the default scan attributes and capabilities,
     * or {@code null} if the attributes could not be retrieved
     */
    public static ScanAttributesReader getDefaultScanAttributes(String packageName, IDeviceScanJobService deviceService,
                                                                ScanAttributes.Destination destination, boolean useDestinationFromScanAttrs,
                                                                int clientVersion) throws SdkException {
        ScanAttributes.Destination defaultsDestination = useDestinationFromScanAttrs ?
                destination : ScanConstants.DEFAULT_SCAN_DESTINATION;

        ScanAttributes defaultScanAttributes = ScanDefaultOptionAdapter.getDefaultScanAttributes(packageName, deviceService, defaultsDestination, clientVersion);
        return new ScanAttributesReader(defaultScanAttributes);
    }

    private static String getDateFileName() {
        final Date time = Calendar.getInstance().getTime();
        final String pattern = "yyyyMMddHHmmssSS";
        final SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
        return sdf.format(time);
    }

    private static DestinationOptions getDestinationOptions(ScanAttributesReader scanAttributes) throws SdkException {
        DestinationOptions destinationOptions = new DestinationOptions();
        Uri uri = null;

        if (!ScanConstants.LOCAL_FOLDER_DESTINATIONS.contains(scanAttributes.getDestination())) {
            uri = scanAttributes.getUri();
            SLog.d(TAG, "getDestinationOptions: uri: " + uri);
        }

        switch (scanAttributes.getDestination()) {
            case ME:
            case USB:
            case EMAIL:
                destinationOptions.setLocalFolder(new LocalFolderOptions());
                break;
            case HTTP:
                handleHttpDestination(scanAttributes, destinationOptions, uri);
                break;
            case FTP:
            case NETWORK_FOLDER:
                handleNetworkOrFtpDestination(scanAttributes, destinationOptions, uri);
                break;
            default:
                throw new SdkInvalidParamException("Invalid ScanAttribute Destination");
        }
        return destinationOptions;
    }

    private static ScanMetadataOptions getMetadataOptions() {
        // Metadata Options
        ScanMetadataOptions metadataOptions = new ScanMetadataOptions();

        ScanMetadataOptions_FileContent_Binding fileContentBinding = new ScanMetadataOptions_FileContent_Binding();
        ScanMetadataOptions_FileContent_Value fileContentValue = new ScanMetadataOptions_FileContent_Value();

        fileContentValue.setExplicitValue(new MetadataFileContent(ScanConstants.CACHED_METADATA_CONTENT));
        fileContentBinding.setExplicit(fileContentValue);
        metadataOptions.setFileContent(fileContentBinding);

        ScanMetadataOptions_FileContentType_Binding fileContentTypeBinding = new ScanMetadataOptions_FileContentType_Binding();
        ScanMetadataOptions_FileContentType_Value fileContentTypeValue = new ScanMetadataOptions_FileContentType_Value();
        fileContentTypeValue.setExplicitValue(new InternetMediaType(ScanConstants.METADATA_MEDIA_TYPE));
        fileContentTypeBinding.setExplicit(fileContentTypeValue);
        metadataOptions.setFileContentType(fileContentTypeBinding);

        ScanMetadataOptions_FileName_Binding fileNameBinding = new ScanMetadataOptions_FileName_Binding();
        ScanMetadataOptions_FileName_Value fileNameValue = new ScanMetadataOptions_FileName_Value();
        fileNameValue.setExplicitValue(new FilePath(ScanConstants.METADATA_FILE_NAME));
        fileNameBinding.setExplicit(fileNameValue);
        metadataOptions.setFileName(fileNameBinding);

        return metadataOptions;
    }

    private static void handleHttpDestination(ScanAttributesReader scanAttributes,
                                              DestinationOptions destinationOptions, Uri uri) throws SdkException {
        if (uri == null) {
            throw new SdkInvalidParamException("getDestinationOptions: HTTP: Uri is null");
        }
        HttpOptions httpOptions = new HttpOptions();
        HttpDestination httpDestination = new HttpDestination();

        if (scanAttributes.getNetworkCredentialsAttributes() != null &&
                !TextUtils.isEmpty(scanAttributes.getNetworkCredentialsAttributes().getUsername())) {
            httpDestination.setAuthorization(createHttpAuthorization(scanAttributes));
        }

        httpDestination.setHost(createHttpHost(uri));
        httpDestination.setPath(createHttpPath(uri));
        httpDestination.setRetry(createRetryBehavior(scanAttributes));
        httpDestination.setScheme(uri.getScheme());

        httpOptions.setDestination(httpDestination);
        destinationOptions.setHttp(httpOptions);
    }

    private static HttpAuthorization createHttpAuthorization(ScanAttributesReader scanAttributes) {
        HttpAuthorization httpAuthorization = new HttpAuthorization();
        BasicAuthorization basicAuthorization = new BasicAuthorization();

        basicAuthorization.setUsername(createBasicAuthUsername(scanAttributes.getNetworkCredentialsAttributes().getUsername()));

        if (!TextUtils.isEmpty(scanAttributes.getNetworkCredentialsAttributes().getPassword())) {
            basicAuthorization.setPassword(createBasicAuthPassword(scanAttributes.getNetworkCredentialsAttributes().getPassword()));
        }

        httpAuthorization.setBasic(basicAuthorization);
        return httpAuthorization;
    }

    private static BasicAuthorization_Username_Binding createBasicAuthUsername(String username) {
        BasicAuthorization_Username_Binding usernameBinding = new BasicAuthorization_Username_Binding();
        BasicAuthorization_Username_Value usernameValue = new BasicAuthorization_Username_Value();
        usernameValue.setExplicitValue(username);
        usernameBinding.setExplicit(usernameValue);
        return usernameBinding;
    }

    private static BasicAuthorization_Password_Binding createBasicAuthPassword(String password) {
        BasicAuthorization_Password_Binding passwordBinding = new BasicAuthorization_Password_Binding();
        BasicAuthorization_Password_Value passwordValue = new BasicAuthorization_Password_Value();
        passwordValue.setExplicitValue(password);
        passwordBinding.setExplicit(passwordValue);
        return passwordBinding;
    }

    private static HttpStyleHostCommon_Host_Binding createHttpHost(Uri uri) {
        HttpStyleHostCommon_Host_Binding hostBinding = new HttpStyleHostCommon_Host_Binding();
        HttpStyleHostCommon_Host_Value hostValue = new HttpStyleHostCommon_Host_Value();
        hostValue.setExplicitValue(new HostName(uri.getHost() + (uri.getPort() < 0 ? "" : ":" + uri.getPort())));
        hostBinding.setExplicit(hostValue);
        return hostBinding;
    }

    private static HttpStyleClientCommon_Path_Binding createHttpPath(Uri uri) {
        HttpStyleClientCommon_Path_Binding pathBinding = new HttpStyleClientCommon_Path_Binding();
        HttpStyleClientCommon_Path_Value pathValue = new HttpStyleClientCommon_Path_Value();
        String path = (uri.getPath() == null || uri.getPath().isEmpty()) ? "/" : uri.getPath();
        pathValue.setExplicitValue(new HttpPath(path));
        pathBinding.setExplicit(pathValue);
        return pathBinding;
    }

    private static RetryBehavior createRetryBehavior(ScanAttributesReader scanAttributes) {
        RetryBehavior retryBehavior = new RetryBehavior();
        BasicServerRetryBehavior retryOptions = new BasicServerRetryBehavior();

        retryOptions.setMaxRetries(createMaxRetries(scanAttributes.getMaxConsecutiveRetries()));
        retryOptions.setTimeoutSeconds(createTimeoutSeconds(scanAttributes.getConnectTimeout()));
        retryBehavior.setBasic(retryOptions);
        return retryBehavior;
    }

    private static BasicServerRetryBehavior_MaxRetries_Binding createMaxRetries(int maxRetries) {
        BasicServerRetryBehavior_MaxRetries_Binding maxRetriesBinding =
                new BasicServerRetryBehavior_MaxRetries_Binding();
        BasicServerRetryBehavior_MaxRetries_Value maxRetriesValue = new BasicServerRetryBehavior_MaxRetries_Value();
        maxRetriesValue.setExplicitValue((byte) Math.max(maxRetries, 0));
        maxRetriesBinding.setExplicit(maxRetriesValue);
        return maxRetriesBinding;
    }

    private static BasicServerRetryBehavior_TimeoutSeconds_Binding createTimeoutSeconds(int connectTimeout) {
        BasicServerRetryBehavior_TimeoutSeconds_Binding timeoutBinding =
                new BasicServerRetryBehavior_TimeoutSeconds_Binding();
        BasicServerRetryBehavior_TimeoutSeconds_Value timeoutValue =
                new BasicServerRetryBehavior_TimeoutSeconds_Value();
        timeoutValue.setExplicitValue((byte) Math.min((connectTimeout > 0 ? connectTimeout : 60), Byte.MAX_VALUE));
        timeoutBinding.setExplicit(timeoutValue);
        return timeoutBinding;
    }

    private static void handleEmailDestination(ScanAttributesReader scanAttributes,
                                               DestinationOptions destinationOptions) throws SdkNotSupportedException {
        EmailOptions emailOptions = new EmailOptions();
        emailOptions.setToList(createEmailDestinationList(scanAttributes.getEmailAttributes().getTo()));
        emailOptions.setCcList(createEmailDestinationList(scanAttributes.getEmailAttributes().getCc()));
        emailOptions.setBccList(createEmailDestinationList(scanAttributes.getEmailAttributes().getBcc()));
        emailOptions.setFrom(createEmailDestination(scanAttributes.getEmailAttributes().getFrom()));
        emailOptions.setSubject(createEmailSubject(scanAttributes.getEmailAttributes().getSubject()));
        emailOptions.setBody(createEmailBody(scanAttributes.getEmailAttributes().getMessage()));
        emailOptions.setServer(createSmtpServerOptions(scanAttributes));

        destinationOptions.setEmail(emailOptions);
    }

    private static List<EmailDestination> createEmailDestinationList(List<EmailAddressInfo> addresses) {
        List<EmailDestination> destinationList = new ArrayList<>();
        for (EmailAddressInfo info : addresses) {
            destinationList.add(createEmailDestination(info));
        }
        return destinationList;
    }

    private static EmailDestination createEmailDestination(EmailAddressInfo info) {
        EmailDestination emailDestination = new EmailDestination();

        EmailDestination_Address_Binding addressBinding = new EmailDestination_Address_Binding();
        EmailDestination_Address_Value addressValue = new EmailDestination_Address_Value();
        addressValue.setExplicitValue(new EmailAddress(info.getAddress()));
        addressBinding.setExplicit(addressValue);
        emailDestination.setAddress(addressBinding);

        EmailDestination_DisplayName_Binding displayNameBinding = new EmailDestination_DisplayName_Binding();
        EmailDestination_DisplayName_Value displayNameValue = new EmailDestination_DisplayName_Value();
        displayNameValue.setExplicitValue(info.getName());
        displayNameBinding.setExplicit(displayNameValue);
        emailDestination.setDisplayName(displayNameBinding);

        return emailDestination;
    }

    private static EmailOptions_Subject_Binding createEmailSubject(String subject) {
        EmailOptions_Subject_Binding subjectBinding = new EmailOptions_Subject_Binding();
        EmailOptions_Subject_Value subjectValue = new EmailOptions_Subject_Value();
        subjectValue.setExplicitValue(subject);
        subjectBinding.setExplicit(subjectValue);
        return subjectBinding;
    }

    private static EmailOptions_Body_Binding createEmailBody(String message) {
        EmailOptions_Body_Binding bodyBinding = new EmailOptions_Body_Binding();
        EmailOptions_Body_Value bodyValue = new EmailOptions_Body_Value();
        if (!TextUtils.isEmpty(message)) {
            bodyValue.setExplicitValue(message);
            bodyBinding.setExplicit(bodyValue);
        }
        return bodyBinding;
    }

    private static SmtpServerOptions createSmtpServerOptions(ScanAttributesReader scanAttributes) throws SdkNotSupportedException {
        SmtpServerOptions smtpServerOptions = new SmtpServerOptions();
        smtpServerOptions.setCredential(createSmtpServerCredential(scanAttributes));
        smtpServerOptions.setCredentialSource(CredentialSourceType.CstProvided);
        smtpServerOptions.setServerAuthenticationRequired(createServerAuthRequired());
        smtpServerOptions.setServerName(createServerName(scanAttributes.getSmtpAttributes().getHost()));
        smtpServerOptions.setServerPort(createServerPort(scanAttributes.getSmtpAttributes().getPort()));
        smtpServerOptions.setServerValidationRequired(createServerValidationRequired());
        smtpServerOptions.setUseTls(createUseTls(scanAttributes.getSmtpAttributes().getTransportMode()));
        return smtpServerOptions;
    }

    private static SmtpServerCredential createSmtpServerCredential(ScanAttributesReader scanAttributes) {
        UserCredential userCredential = new UserCredential();
        SmtpServerCredential smtpCredential = new SmtpServerCredential();

        if (scanAttributes.getSmtpAttributes().getServerCredentials() != null) {
            userCredential.setUserName(createUserNameBinding(scanAttributes.getSmtpAttributes().getServerCredentials().getUsername()));
            userCredential.setPassword(createPasswordBinding(scanAttributes.getSmtpAttributes().getServerCredentials().getPassword()));

            String domain = scanAttributes.getSmtpAttributes().getServerCredentials().getDomain();
            if (domain != null && !domain.isEmpty()) {
                userCredential.setDomain(createDomainBinding(domain));
            }
            smtpCredential.setUser(userCredential);
        }
        return smtpCredential;
    }

    private static UserCredential createNetworkFolderUserCredential(ScanAttributesReader scanAttributes) {
        if (scanAttributes.getNetworkCredentialsAttributes() == null) {
            return null;
        }
        UserCredential userCredential = new UserCredential();
        if (scanAttributes.getNetworkCredentialsAttributes().getUsername() != null) {
            userCredential.setUserName(createUserNameBinding(scanAttributes.getNetworkCredentialsAttributes().getUsername()));
            userCredential.setPassword(createPasswordBinding(scanAttributes.getNetworkCredentialsAttributes().getPassword()));
        }

        String domain = scanAttributes.getNetworkCredentialsAttributes().getDomain();
        if (domain != null && !domain.isEmpty()) {
            userCredential.setDomain(createDomainBinding(domain));
        }
        return userCredential;
    }

    private static UserCredential_UserName_Binding createUserNameBinding(String username) {
        UserCredential_UserName_Binding userNameBinding = new UserCredential_UserName_Binding();
        UserCredential_UserName_Value userNameValue = new UserCredential_UserName_Value();
        userNameValue.setExplicitValue(username);
        userNameBinding.setExplicit(userNameValue);
        return userNameBinding;
    }

    private static UserCredential_Password_Binding createPasswordBinding(String password) {
        UserCredential_Password_Binding passwordBinding = new UserCredential_Password_Binding();
        UserCredential_Password_Value passwordValue = new UserCredential_Password_Value();
        passwordValue.setExplicitValue(password);
        passwordBinding.setExplicit(passwordValue);
        return passwordBinding;
    }

    private static UserCredential_Domain_Binding createDomainBinding(String domain) {
        UserCredential_Domain_Binding domainBinding = new UserCredential_Domain_Binding();
        UserCredential_Domain_Value domainValue = new UserCredential_Domain_Value();
        domainValue.setExplicitValue(domain);
        domainBinding.setExplicit(domainValue);
        return domainBinding;
    }

    private static SmtpServerOptions_ServerAuthenticationRequired_Binding createServerAuthRequired() {
        SmtpServerOptions_ServerAuthenticationRequired_Binding authRequiredBinding =
                new SmtpServerOptions_ServerAuthenticationRequired_Binding();
        SmtpServerOptions_ServerAuthenticationRequired_Value authRequiredValue =
                new SmtpServerOptions_ServerAuthenticationRequired_Value();
        authRequiredValue.setExplicitValue(false);
        authRequiredBinding.setExplicit(authRequiredValue);
        return authRequiredBinding;
    }

    private static SmtpServerOptions_ServerName_Binding createServerName(String host) {
        SmtpServerOptions_ServerName_Binding serverNameBinding = new SmtpServerOptions_ServerName_Binding();
        SmtpServerOptions_ServerName_Value serverNameValue = new SmtpServerOptions_ServerName_Value();
        serverNameValue.setExplicitValue(new IpAddressOrHostName(host));
        serverNameBinding.setExplicit(serverNameValue);
        return serverNameBinding;
    }

    private static SmtpServerOptions_ServerPort_Binding createServerPort(int port) {
        SmtpServerOptions_ServerPort_Binding serverPortBinding = new SmtpServerOptions_ServerPort_Binding();
        SmtpServerOptions_ServerPort_Value serverPortValue = new SmtpServerOptions_ServerPort_Value();
        serverPortValue.setExplicitValue(new PortNumber(port));
        serverPortBinding.setExplicit(serverPortValue);
        return serverPortBinding;
    }

    private static SmtpServerOptions_ServerValidationRequired_Binding createServerValidationRequired() {
        SmtpServerOptions_ServerValidationRequired_Binding validationBinding =
                new SmtpServerOptions_ServerValidationRequired_Binding();
        SmtpServerOptions_ServerValidationRequired_Value validationValue =
                new SmtpServerOptions_ServerValidationRequired_Value();
        validationValue.setExplicitValue(false);
        validationBinding.setExplicit(validationValue);
        return validationBinding;
    }

    private static SmtpServerOptions_UseTls_Binding createUseTls(SmtpAttributes.TransportMode transportMode) throws SdkNotSupportedException {
        SmtpServerOptions_UseTls_Binding useTlsBinding = new SmtpServerOptions_UseTls_Binding();
        SmtpServerOptions_UseTls_Value useTlsValue = new SmtpServerOptions_UseTls_Value();

        if (transportMode != null) {
            switch (transportMode) {
                case SSL_TLS:
                    useTlsValue.setExplicitValue(true);
                    break;
                case START_TLS:
                    throw new SdkNotSupportedException("SMTP: START_TLS is not supported");
                default:
                    useTlsValue.setExplicitValue(false);
            }
        } else {
            useTlsValue.setExplicitValue(false);
        }

        useTlsBinding.setExplicit(useTlsValue);
        return useTlsBinding;
    }

    private static void handleNetworkOrFtpDestination(ScanAttributesReader scanAttributes,
                                                      DestinationOptions destinationOptions, Uri uri) throws SdkException {
        if (uri == null || uri.toString().isEmpty()) {
            throw new SdkInvalidParamException("ScanTicketAdapter : Uri is null for " + scanAttributes.getDestination().name());
        }
        NetworkFolderOptions networkFolderOptions = new NetworkFolderOptions();
        networkFolderOptions.setCredential(createNetworkFolderUserCredential(scanAttributes));
        networkFolderOptions.setCredentialSource(CredentialSourceType.CstProvided);

        if (scanAttributes.getDestination() == ScanAttributes.Destination.NETWORK_FOLDER) {
            //networkFolderOptions.setProtocol(NetworkFolderProtocol.NfpSmb);
            networkFolderOptions.setProtocolOptions(createSmbProtocolOptions(uri));
        } else if (scanAttributes.getDestination() == ScanAttributes.Destination.FTP) {
            //networkFolderOptions.setProtocol(NetworkFolderProtocol.NfpFtp);
            networkFolderOptions.setProtocolOptions(createFtpProtocolOptions(uri));
        } else {
            throw new SdkInvalidParamException("Wrong Destination Type for NetworkFolderOptions: " + scanAttributes.getDestination());
        }

        destinationOptions.setNetworkFolder(networkFolderOptions);
    }

    private static ProtocolOptions createSmbProtocolOptions(Uri uri) {
        ProtocolOptions protocolOptions = new ProtocolOptions();
        SmbProtocolOptions smbOptions = new SmbProtocolOptions();

        SmbProtocolOptions_UncPath_Binding uncPathBinding = new SmbProtocolOptions_UncPath_Binding();
        SmbProtocolOptions_UncPath_Value uncPathValue = new SmbProtocolOptions_UncPath_Value();
        uncPathValue.setExplicitValue(new UncPath(uri.toString()));
        uncPathBinding.setExplicit(uncPathValue);
        smbOptions.setUncPath(uncPathBinding);

        protocolOptions.setSmbOptions(smbOptions);
        return protocolOptions;
    }

    private static ProtocolOptions createFtpProtocolOptions(Uri uri) {
        SLog.d(TAG, "createFtpProtocolOptions: uri:" + uri.toString() + ",  path: " + uri.getPath());
        ProtocolOptions protocolOptions = new ProtocolOptions();
        FtpProtocolOptions ftpOptions = new FtpProtocolOptions();

        FtpProtocolOptions_Path_Binding pathBinding = new FtpProtocolOptions_Path_Binding();
        FtpProtocolOptions_Path_Value pathValue = new FtpProtocolOptions_Path_Value();
        String path = (uri.getPath() == null || uri.getPath().isEmpty()) ? "/" : uri.getPath();
        pathValue.setExplicitValue(new FtpPath(path));
        pathBinding.setExplicit(pathValue);
        ftpOptions.setPath(pathBinding);

        FtpProtocolOptions_Port_Binding portBinding = new FtpProtocolOptions_Port_Binding();
        FtpProtocolOptions_Port_Value portValue = new FtpProtocolOptions_Port_Value();
        portValue.setExplicitValue(new PortNumber(uri.getPort() > 0 ? uri.getPort() : 21));
        portBinding.setExplicit(portValue);
        ftpOptions.setPort(portBinding);

        FtpProtocolOptions_Server_Binding serverBinding = new FtpProtocolOptions_Server_Binding();
        FtpProtocolOptions_Server_Value serverValue = new FtpProtocolOptions_Server_Value();
        serverValue.setExplicitValue(new IpAddressOrHostName(uri.getHost()));
        serverBinding.setExplicit(serverValue);
        ftpOptions.setServer(serverBinding);

        FtpProtocolOptions_TransferMode_Binding transferModeBinding = new FtpProtocolOptions_TransferMode_Binding();
        FtpProtocolOptions_TransferMode_Value transferModeValue = new FtpProtocolOptions_TransferMode_Value();
        transferModeValue.setExplicitValue(FtpTransferMode.FtmPassive);
        transferModeBinding.setExplicit(transferModeValue);
        ftpOptions.setTransferMode(transferModeBinding);

        protocolOptions.setFtpOptions(ftpOptions);
        return protocolOptions;
    }

    private static ScanOptions getScanOptions(ScanAttributesReader scanAttributes,
                                              ScanAttributesReader defaultScanAttributes) {
        ScanOptions scanOptions = new ScanOptions();
        setScanOptionsValue(scanAttributes.getColorMode(), defaultScanAttributes.getColorMode(),
                ScanTypeMapping.colorMode, scanOptions::setColorMode);
        setScanOptionsValue(scanAttributes.getDocumentFormat(), defaultScanAttributes.getDocumentFormat(),
                ScanTypeMapping.outputFileFormat, scanOptions::setOutputFileFormat);

        ScanAttributes.Duplex duplex = scanAttributes.getPlex() != ScanAttributes.Duplex.DEFAULT
                ? scanAttributes.getPlex()
                : defaultScanAttributes.getPlex();

        Pair<PlexMode, BindingFormat> plexMode = ScanTypeMapping.duplex.convertWtoE(duplex);
        if (plexMode != null && plexMode.first != null) {
            scanOptions.setPlexMode(plexMode.first);
            if (plexMode.second != null) {
                scanOptions.setMediaBindingFormat(plexMode.second);
            }
        }

        setScanOptionsValue(scanAttributes.getResolution(), defaultScanAttributes.getResolution(),
                ScanTypeMapping.resolution, scanOptions::setResolution);
        setScanOptionsValue(scanAttributes.getScanSize(), defaultScanAttributes.getScanSize(),
                ScanTypeMapping.mediaSize, scanOptions::setMediaSize);
        setScanOptionsValue(scanAttributes.getOrientation(), defaultScanAttributes.getOrientation(),
                ScanTypeMapping.contentOrientation, scanOptions::setContentOrientation);
        setScanOptionsValue(scanAttributes.getScanPreview(), defaultScanAttributes.getScanPreview(),
                ScanTypeMapping.imagePreviewMode, scanOptions::setImagePreviewMode);
        setScanOptionsValue(scanAttributes.getBackgroundCleanup(), defaultScanAttributes.getBackgroundCleanup(),
                ScanTypeMapping.backgroundCleanup, scanOptions::setBackgroundCleanup);
        setScanOptionsValue(scanAttributes.getContrastAdjustment(), defaultScanAttributes.getContrastAdjustment(),
                ScanTypeMapping.contrast, scanOptions::setContrast);
        setScanOptionsValue(scanAttributes.getDarknessAdjustment(), defaultScanAttributes.getDarknessAdjustment(),
                ScanTypeMapping.exposureLevel, scanOptions::setExposureLevel);
        setScanOptionsValue(scanAttributes.getBlankImageRemovalMode(), defaultScanAttributes.getBlankImageRemovalMode(),
                ScanTypeMapping.blankPageSuppression, scanOptions::setBlankPageSuppression);
        setScanOptionsValue(scanAttributes.getProgressDialogMode(), defaultScanAttributes.getProgressDialogMode(),
                ScanTypeMapping.scanProgressMode, scanOptions::setScanProgressMode);
        setScanOptionsValue(scanAttributes.getOutputQuality(), defaultScanAttributes.getOutputQuality(),
                ScanTypeMapping.outputQualityVsSize, scanOptions::setOutputQualityVsSize);
        setScanOptionsValue(scanAttributes.getCropMode(), defaultScanAttributes.getCropMode(),
                ScanTypeMapping.autoCropMode, scanOptions::setAutoCropMode);

        // Check if Destination requires Job Transmission Mode (e.g., Me, Email, USB, Local Folder)
        if (ScanConstants.LOCAL_FOLDER_DESTINATIONS.contains(scanAttributes.getDestination())) {
            scanOptions.setFileTransmissionMode(TransmissionMode.TmJob);
        } else {
            setScanOptionsValue(scanAttributes.getTransmissionMode(), defaultScanAttributes.getTransmissionMode(),
                    ScanTypeMapping.fileTransmissionMode, scanOptions::setFileTransmissionMode);
        }
        setScanOptionsValue(scanAttributes.getSharpnessAdjustment(), defaultScanAttributes.getSharpnessAdjustment(),
                ScanTypeMapping.sharpness, scanOptions::setSharpness);
        setScanOptionsValue(scanAttributes.getTextPhotoOptimization(), defaultScanAttributes.getTextPhotoOptimization(),
                ScanTypeMapping.contentType, scanOptions::setContentType);
        setScanOptionsValue(scanAttributes.getMediaSource(), defaultScanAttributes.getMediaSource(),
                ScanTypeMapping.mediaSource, scanOptions::setMediaSource);
        setScanOptionsValue(scanAttributes.getMisfeedDetectionMode(), defaultScanAttributes.getMisfeedDetectionMode(),
                ScanTypeMapping.misfeedDetection, scanOptions::setMisfeedDetection);
        setScanOptionsValue(scanAttributes.getCaptureMode(), defaultScanAttributes.getCaptureMode(),
                ScanTypeMapping.scanCaptureMode, scanOptions::setScanCaptureMode);
        setScanOptionsValue(scanAttributes.getAutomaticStraightenMode(),
                defaultScanAttributes.getAutomaticStraightenMode(),
                ScanTypeMapping.autoDeskew, scanOptions::setAutoDeskew);

        if (scanAttributes.getFileOptionsAttributes() != null) {
            FileOptionsAttributesReader fileOptionsAttributes =
                    new FileOptionsAttributesReader(scanAttributes.getFileOptionsAttributes());
            if (fileOptionsAttributes.getPdfEncryptionPassword() != null) {
                scanOptions.setOutputFileEncryption(true);
                scanOptions.setEncryptionPassword(fileOptionsAttributes.getPdfEncryptionPassword());
            }
            setScanOptionsValue(fileOptionsAttributes.getPdfCompressionMode(), null,
                    ScanTypeMapping.outputFileCompression, scanOptions::setOutputFileCompression);
            if (ScanAttributes.ColorMode.MONO.equals(scanAttributes.getColorMode())) {
                setScanOptionsValue(fileOptionsAttributes.getTiffCompressionMode(), null,
                        ScanTypeMapping.monoCompression, scanOptions::setMonoCompression);
            } else {
                setScanOptionsValue(fileOptionsAttributes.getTiffCompressionMode(), null,
                        ScanTypeMapping.colorAndGrayCompression, scanOptions::setColorAndGrayCompression);
            }
        }

        String fileName = (scanAttributes.getFileName() != null && !scanAttributes.getFileName().isEmpty()) ?
                scanAttributes.getFileName() : getDateFileName();
        ScanOptions_FileName_Binding fileNameBinding = new ScanOptions_FileName_Binding();
        ScanOptions_FileName_Value fileNameValue = new ScanOptions_FileName_Value();
        fileNameValue.setExplicitValue(fileName);
        fileNameBinding.setExplicit(fileNameValue);
        scanOptions.setFileName(fileNameBinding);
        return scanOptions;
    }

    /**
     * Convert Workpath type scan attribute to E2 type scan option and set the converted E2 value to the E2
     * ScanOptions object
     * If the scan attribute is set from an app, then it is converted to E2 type and set to the scan option
     * If the scan attribute is not set from an app, then the default scan attribute from E2 is used
     * If the default scan attribute is not set from E2, then the scan option is not set
     *
     * @param scanAttribute        Workpath type scan attribute from an app
     * @param defaultScanAttribute Default scan attribute from E2
     * @param option               scan option mapping
     * @param setFunc              set function in E2 ScanOptions
     * @param <E>                  E2 type
     * @param <W>                  Workpath API type
     */
    private static <E, W> void setScanOptionsValue(W scanAttribute, W defaultScanAttribute, ScanTypeMapping option,
                                                   SetScanOptionValue<E> setFunc) {
        E scanOption = null;
        if (scanAttribute != null && !"DEFAULT".equalsIgnoreCase(scanAttribute.toString())) {
            scanOption = option.convertWtoE(scanAttribute);
        } else if (defaultScanAttribute != null && !"DEFAULT".equalsIgnoreCase(defaultScanAttribute.toString())) {
            scanOption = option.convertWtoE(defaultScanAttribute);
        }
        if (scanOption != null) {
            setFunc.set(scanOption);
        }
    }

    @FunctionalInterface
    private interface SetScanOptionValue<T> {
        void set(T t);
    }
}
