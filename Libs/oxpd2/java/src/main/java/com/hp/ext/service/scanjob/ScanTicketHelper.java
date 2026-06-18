package com.hp.ext.service.scanJob;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hp.ext.types.optionProfile.OptionDefinition;
import com.hp.ext.types.optionProfile.OptionProfile;
import com.hp.ext.types.optionProfile.optionProfileHelper.OptionProfileHelper;
import com.hp.ext.types.optionProfile.optionProfileHelper.OptionProfileHelperException;
import com.hp.ext.types.security.UserCredential;
import com.hp.ext.types.security.UserCredential_Password_Binding;
import com.hp.ext.types.security.UserCredential_UserName_Binding;
import com.hp.ext.types.target.BasicAuthorization_Password_Binding;
import com.hp.ext.types.target.BasicAuthorization_Username_Binding;
import com.hp.ext.types.target.BasicServerRetryBehavior_MaxRetries_Binding;
import com.hp.ext.types.target.BasicServerRetryBehavior_TimeoutSeconds_Binding;
import com.hp.ext.types.target.BearerAuthorization_Token_Binding;
import com.hp.ext.types.target.Header;
import com.hp.ext.types.target.Headers;
import com.hp.ext.types.target.HttpAuthorization;
import com.hp.ext.types.target.HttpStyleClientCommon_Path_Binding;
import com.hp.ext.types.target.HttpStyleHostCommon_Host_Binding;
import com.hp.ext.types.target.RetryBehavior;

public class ScanTicketHelper {

    private OptionProfileHelper<ScanOptions> httpScanOptionsProfileHelper;

    public OptionProfileHelper<ScanOptions> getHttpScanOptionsProfileHelper() {
        return httpScanOptionsProfileHelper;
    }

    private ScanOptions httpScanOptions = new ScanOptions();

    public ScanOptions getHttpScanOptions() {
        return httpScanOptions;
    }

    public void setHttpScanOptions(ScanOptions scanOptions) throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, OptionProfileHelperException {
        this.httpScanOptions = scanOptions;
        getHttpScanOptionsProfileHelper().setOptionsInstance(scanOptions);
    }

    private OptionProfileHelper<ScanOptions> networkFolderScanOptionsProfileHelper;

    public OptionProfileHelper<ScanOptions> getNetworkFolderScanOptionsProfileHelper() {
        return networkFolderScanOptionsProfileHelper;
    }

    private ScanOptions networkFolderScanOptions = new ScanOptions();

    public ScanOptions getNetworkFolderScanOptions() {
        return networkFolderScanOptions;
    }

    public void setNetworkFolderScanOptions(ScanOptions scanOptions) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, OptionProfileHelperException {
        this.networkFolderScanOptions = scanOptions;
        getNetworkFolderScanOptionsProfileHelper().setOptionsInstance(scanOptions);
    }

    private OptionProfileHelper<ScanOptions> jobStorageScanOptionsProfileHelper;

    public OptionProfileHelper<ScanOptions> getJobStorageScanOptionsProfileHelper() {
        return jobStorageScanOptionsProfileHelper;
    }

    private ScanOptions jobStorageScanOptions = new ScanOptions();

    public ScanOptions getJobStorageScanOptions() {
        return jobStorageScanOptions;
    }

    public void setJobStorageScanOptions(ScanOptions scanOptions) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, OptionProfileHelperException {
        this.jobStorageScanOptions = scanOptions;
        getJobStorageScanOptionsProfileHelper().setOptionsInstance(scanOptions);
    }

    /**
     * Create a ScanTicketHelper for use with scan jobs
     *
     * @param baseScanOptionProfile The base scan-options OptionProfile
     * @param httpScanOptionProfile The http scan-options OptionProfile
     */
    public ScanTicketHelper(OptionProfile baseScanOptionProfile, OptionProfile httpScanOptionProfile)
            throws ClassNotFoundException, NoSuchFieldException, SecurityException, NoSuchMethodException,
            OptionProfileHelperException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        initialize(baseScanOptionProfile, httpScanOptionProfile, null, null);
    }

    /**
     * Create a ScanTicketHelper for use with Http, NetworkFolder, or JobStorage scan jobs
     *
     * @param baseScanOptionProfile          The base scan-options OptionProfile
     * @param httpScanOptionProfile          The http scan-options OptionProfile
     * @param networkFolderScanOptionProfile The network-folder scan-options
     * @param jobStorageScanOptionProfile    The job-storage scan-options OptionProfile
     *                                       OptionProfile
     */
    public ScanTicketHelper(OptionProfile baseScanOptionProfile, OptionProfile httpScanOptionProfile,
            OptionProfile networkFolderScanOptionProfile, OptionProfile jobStorageScanOptionProfile)
            throws ClassNotFoundException, NoSuchFieldException, SecurityException, NoSuchMethodException,
            OptionProfileHelperException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        initialize(baseScanOptionProfile, httpScanOptionProfile, networkFolderScanOptionProfile, jobStorageScanOptionProfile);
    }

    private void initialize(OptionProfile baseScanOptionProfile, OptionProfile httpScanOptionProfile,
            OptionProfile netFolderScanOptionFolder, OptionProfile jobStorageScanOptionProfile)
            throws ClassNotFoundException, NoSuchFieldException, SecurityException, NoSuchMethodException,
            OptionProfileHelperException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        var baseScanDefinitions = (null == baseScanOptionProfile ? null : baseScanOptionProfile.getDefinitions());
        var httpScanDefinitions = (null == httpScanOptionProfile ? null : httpScanOptionProfile.getDefinitions());
        var netFolderScanDefinitions = (null == netFolderScanOptionFolder ? null
                : netFolderScanOptionFolder.getDefinitions());
        var jobStorageScanDefinitions = (null == jobStorageScanOptionProfile ? null
                : jobStorageScanOptionProfile.getDefinitions());

        var mergedHttpScanDefinitions = MergeOptionDefinitions(baseScanDefinitions, httpScanDefinitions);
        httpScanOptionsProfileHelper = new OptionProfileHelper<ScanOptions>(httpScanOptions,
                mergedHttpScanDefinitions, ScanOptions.class);

        var mergedNetFolderDefinitions = MergeOptionDefinitions(baseScanDefinitions, netFolderScanDefinitions);
        networkFolderScanOptionsProfileHelper = new OptionProfileHelper<ScanOptions>(networkFolderScanOptions,
                mergedNetFolderDefinitions, ScanOptions.class);

        var mergedJobStorageDefinitions = MergeOptionDefinitions(baseScanDefinitions, jobStorageScanDefinitions);
        jobStorageScanOptionsProfileHelper = new OptionProfileHelper<ScanOptions>(jobStorageScanOptions,
                mergedJobStorageDefinitions, ScanOptions.class);
    }

    private Map<String, OptionDefinition> MergeOptionDefinitions(List<OptionDefinition> baseDefinitions,
            List<OptionDefinition> overrideDefinitions) {
        baseDefinitions = (baseDefinitions != null) ? baseDefinitions : new ArrayList<OptionDefinition>();
        overrideDefinitions = (overrideDefinitions != null) ? overrideDefinitions : new ArrayList<OptionDefinition>();

        Map<String, OptionDefinition> mergedMap = baseDefinitions.stream()
                .collect(Collectors.toMap(entry -> entry.getOptionName().toLowerCase(), entry -> entry));

        for (OptionDefinition definition : overrideDefinitions) {
            String normalizedOptionName = definition.getOptionName().toLowerCase();

            if (mergedMap.containsKey(normalizedOptionName)) {
                mergedMap.put(normalizedOptionName, definition);
            } else {
                mergedMap.putIfAbsent(normalizedOptionName, definition);
            }
        }

        return mergedMap;
    }

    private boolean validateHttpAuthorization(HttpAuthorization authorization) {
        if (authorization.isBasic()) {
            if (authorization.getBasic().getUsername() == null || authorization.getBasic().getPassword() == null) {
                return false;
            }

            // Verify Username Bindable Field
            BasicAuthorization_Username_Binding username = authorization.getBasic().getUsername();
            if (username.isExplicit() && username.getExplicit().getExplicitValue() == null) {
                return false;
            }
            if (username.isExplicit() && username.getExplicit().getExplicitValue() != null && username.getExplicit().getExplicitValue().length() >= 128) {
                return false;
            }
            if (username.isExpression() && (username.getExpression().getExpressionPattern() == null || username.getExpression().getExpressionPattern().length() <= 0)) {
                return false;
            }

            // Verify Password Bindable Field
            BasicAuthorization_Password_Binding password = authorization.getBasic().getPassword();
            if (password.isExplicit() && password.getExplicit().getExplicitValue() == null) {
                return false;
            }
            if (password.isExplicit() && password.getExplicit().getExplicitValue() != null && password.getExplicit().getExplicitValue().length() >= 128) {
                return false;
            }
            if (password.isExpression() && (password.getExpression().getExpressionPattern() == null || password.getExpression().getExpressionPattern().length() <= 0)) {
                return false;
            }
        } else if (authorization.isBearer()) {
            if (authorization.getBearer().getToken() == null) {
                return false;
            }

            // Verify Token Bindable Field
            BearerAuthorization_Token_Binding token = authorization.getBearer().getToken();
            if (token.isExplicit() && token.getExplicit().getExplicitValue() == null) {
                return false;
            }
            if (token.isExplicit() && token.getExplicit().getExplicitValue() != null && token.getExplicit().getExplicitValue().length() >= 1024) {
                return false;
            }
            if (token.isExpression() && (token.getExpression().getExpressionPattern() == null || token.getExpression().getExpressionPattern().length() <= 0)) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean validateHttpHeaders(Headers headers) {
        if (headers.getItems().size() > 25) {
            return false;
        }

        for (Header header : headers.getItems()) {
            // Validate Header Name
            if (header.getHeaderName() == null || header.getHeaderName().length() >= 256) {
                return false;
            }

            // Validate Header Bindable Value
            if (header.getHeaderValue() == null) {
                return false;
            }
            if (header.getHeaderValue().isExplicit() && header.getHeaderValue().getExplicit().getExplicitValue() == null) {
                return false;
            }
            if (header.getHeaderValue().isExplicit() && header.getHeaderValue().getExplicit().getExplicitValue() != null && header.getHeaderValue().getExplicit().getExplicitValue().length() >= 256) {
                return false;
            }
            if (header.getHeaderValue().isExpression() && (header.getHeaderValue().getExpression().getExpressionPattern() == null || header.getHeaderValue().getExpression().getExpressionPattern().length() <= 0)) {
                return false;
            }
        }

        return true;
    }

    private boolean validateHttpTargetHost(HttpStyleHostCommon_Host_Binding host) {
        if (host.isExplicit() && host.getExplicit().getExplicitValue() == null) {
            return false;
        }
        if (host.isExpression() && (host.getExpression().getExpressionPattern() == null || host.getExpression().getExpressionPattern().length() <= 0)) {
            return false;
        }
        return true;
    }

    private boolean validateHttpTargetPath(HttpStyleClientCommon_Path_Binding path) {
        if (path.isExplicit() && path.getExplicit().getExplicitValue() == null) {
            return false;
        }
        if (path.isExpression() && (path.getExpression().getExpressionPattern() == null || path.getExpression().getExpressionPattern().length() <= 0)) {
            return false;
        }
        return true;
    }

    private boolean validateHttpRetry(RetryBehavior retry) {
        if (retry.isBasic()) {
            if (retry.getBasic().getMaxRetries() == null || retry.getBasic().getTimeoutSeconds() == null) {
                return false;
            }

            // Verify Max Retries
            BasicServerRetryBehavior_MaxRetries_Binding maxRetries = retry.getBasic().getMaxRetries();
            if (maxRetries.isExplicit() && maxRetries.getExplicit().getExplicitValue() == null) {
                return false;
            }
            if (maxRetries.isExplicit() && maxRetries.getExplicit().getExplicitValue() != null && (maxRetries.getExplicit().getExplicitValue() > 10 || maxRetries.getExplicit().getExplicitValue() < 0)) {
                return false;
            }
            if (maxRetries.isExpression() && (maxRetries.getExpression().getExpressionPattern() == null || maxRetries.getExpression().getExpressionPattern().length() <= 0)) {
                return false;
            }

            // Verify Timeout Seconds
            BasicServerRetryBehavior_TimeoutSeconds_Binding timeoutSeconds = retry.getBasic().getTimeoutSeconds();
            if (timeoutSeconds.isExplicit() && timeoutSeconds.getExplicit().getExplicitValue() == null) {
                return false;
            }
            if (timeoutSeconds.isExplicit() && timeoutSeconds.getExplicit().getExplicitValue() != null && (timeoutSeconds.getExplicit().getExplicitValue() > 60 || timeoutSeconds.getExplicit().getExplicitValue() < 1)) {
                return false;
            }
            if (timeoutSeconds.isExpression() && (timeoutSeconds.getExpression().getExpressionPattern() == null || timeoutSeconds.getExpression().getExpressionPattern().length() <= 0)) {
                return false;
            }
        }
        return true;
    }

    private boolean validateNetworkFolderCredential(UserCredential credential) {
        if (credential.getUserName() == null || credential.getPassword() == null) {
            return false;
        }

        // Verify Username
        UserCredential_UserName_Binding username = credential.getUserName();
        if (username.isExplicit() && username.getExplicit().getExplicitValue() == null) {
            return false;
        }
        if (username.isExplicit() && username.getExplicit().getExplicitValue() != null && (username.getExplicit().getExplicitValue().length() >= 128 || username.getExplicit().getExplicitValue().length() <= 0)) {
            return false;
        }
        if (username.isExpression() && (username.getExpression().getExpressionPattern() == null || username.getExpression().getExpressionPattern().length() <= 0)) {
            return false;
        }

        // Verify Username
        UserCredential_Password_Binding password = credential.getPassword();
        if (password.isExplicit() && password.getExplicit().getExplicitValue() == null) {
            return false;
        }
        if (password.isExplicit() && password.getExplicit().getExplicitValue() != null && (password.getExplicit().getExplicitValue().length() >= 128 || password.getExplicit().getExplicitValue().length() <= 0)) {
            return false;
        }
        if (password.isExpression() && (password.getExpression().getExpressionPattern() == null || password.getExpression().getExpressionPattern().length() <= 0)) {
            return false;
        }

        // Verify Domain If Present
        if (credential.getDomain() != null) {
            if (credential.getDomain().isExplicit() && credential.getDomain().getExplicit().getExplicitValue() == null) {
                return false;
            }
            if (credential.getDomain().isExplicit() && credential.getDomain().getExplicit().getExplicitValue() != null && (credential.getDomain().getExplicit().getExplicitValue().length() >= 256 || credential.getDomain().getExplicit().getExplicitValue().length() <= 0)) {
                return false;
            }
            if (credential.getDomain().isExpression() && (credential.getDomain().getExpression().getExpressionPattern() == null || credential.getDomain().getExpression().getExpressionPattern().length() <= 0)) {
                return false;
            }
        }

        return true;
    }

    private boolean validateJobStorageFolderName(JobStorageOptions_FolderName_Binding folderName) {
        if (folderName.isExplicit() && folderName.getExplicit().getExplicitValue() == null) {
            return false;
        }
        if (folderName.isExplicit() && folderName.getExplicit().getExplicitValue() != null && (folderName.getExplicit().getExplicitValue().length() >= 256 || folderName.getExplicit().getExplicitValue().length() <= 0)) {
            return false;
        }
        if (folderName.isExpression() && (folderName.getExpression().getExpressionPattern() == null || folderName.getExpression().getExpressionPattern().length() <= 0)) {
            return false;
        }
        return true;
    }

    private boolean validateJobStorageJobName(JobStorageOptions_JobName_Binding jobName) {
        if (jobName.isExplicit() && jobName.getExplicit().getExplicitValue() == null) {
            return false;
        }
        if (jobName.isExplicit() && jobName.getExplicit().getExplicitValue() != null && (jobName.getExplicit().getExplicitValue().length() >= 256 || jobName.getExplicit().getExplicitValue().length() <= 0)) {
            return false;
        }
        if (jobName.isExpression() && (jobName.getExpression().getExpressionPattern() == null || jobName.getExpression().getExpressionPattern().length() <= 0)) {
            return false;
        }
        return true;
    }

    private boolean validateJobStorageJobPassword(JobStorageOptions_JobPassword_Binding jobPassword) {
        if (jobPassword.isExplicit() && jobPassword.getExplicit().getExplicitValue() == null) {
            return false;
        }
        if (jobPassword.isExplicit() && jobPassword.getExplicit().getExplicitValue() != null && (jobPassword.getExplicit().getExplicitValue().length() >= 256 || jobPassword.getExplicit().getExplicitValue().length() <= 0)) {
            return false;
        }
        if (jobPassword.isExpression() && (jobPassword.getExpression().getExpressionPattern() == null || jobPassword.getExpression().getExpressionPattern().length() <= 0)) {
            return false;
        }
        return true;
    }

    private boolean validateJobStorageJobPasswordType(JobStorageOptions_JobPasswordType_Binding jobPasswordType) {
        if (jobPasswordType.isExplicit() && jobPasswordType.getExplicit().getExplicitValue() == null) {
            return false;
        }
        if (jobPasswordType.isExpression() && (jobPasswordType.getExpression().getExpressionPattern() == null || jobPasswordType.getExpression().getExpressionPattern().length() <= 0)) {
            return false;
        }
        return true;
    }

    private boolean validateProtocolOptions(ProtocolOptions protocolOptions) {
        // Verify SMB Options If Present
        if (protocolOptions.isSmbOptions()) {
            if (protocolOptions.getSmbOptions().getUncPath() == null) {
                return false;
            }

            // Verify UNC Path
            SmbProtocolOptions_UncPath_Binding uncPath = protocolOptions.getSmbOptions().getUncPath();
            if (uncPath.isExplicit() && uncPath.getExplicit().getExplicitValue() == null) {
                return false;
            }
            if (uncPath.isExplicit() && uncPath.getExplicit().getExplicitValue() != null && (uncPath.getExplicit().getExplicitValue().getValue().length() > 1024 || uncPath.getExplicit().getExplicitValue().getValue().length() <= 0)) {
                return false;
            }
            if (uncPath.isExpression() && (uncPath.getExpression().getExpressionPattern() == null || uncPath.getExpression().getExpressionPattern().length() <= 0)) {
                return false;
            }
        } else if (protocolOptions.isFtpOptions()) { // Verify FTP Options If Present
            if (protocolOptions.getFtpOptions().getPath() == null || protocolOptions.getFtpOptions().getPort() == null || protocolOptions.getFtpOptions().getServer() == null || protocolOptions.getFtpOptions().getTransferMode() == null) {
                return false;
            }

            // Verify Path
            FtpProtocolOptions_Path_Binding path = protocolOptions.getFtpOptions().getPath();
            if (path.isExplicit() && path.getExplicit().getExplicitValue() == null) {
                return false;
            }
            if (path.isExplicit() && path.getExplicit().getExplicitValue() != null && (path.getExplicit().getExplicitValue().getValue().length() > 1024 || path.getExplicit().getExplicitValue().getValue().length() <= 0)) {
                return false;
            }
            if (path.isExpression() && (path.getExpression().getExpressionPattern() == null || path.getExpression().getExpressionPattern().length() <= 0)) {
                return false;
            }

            // Verify Port
            FtpProtocolOptions_Port_Binding port = protocolOptions.getFtpOptions().getPort();
            if (port.isExplicit() && port.getExplicit().getExplicitValue() == null) {
                return false;
            }
            if (port.isExplicit() && port.getExplicit().getExplicitValue() != null && (port.getExplicit().getExplicitValue().getValue() > 65535 || port.getExplicit().getExplicitValue().getValue() < 0)) {
                return false;
            }
            if (port.isExpression() && (port.getExpression().getExpressionPattern() == null || port.getExpression().getExpressionPattern().length() <= 0)) {
                return false;
            }

            // Verify Server
            FtpProtocolOptions_Server_Binding server = protocolOptions.getFtpOptions().getServer();
            if (server.isExplicit() && server.getExplicit().getExplicitValue() == null) {
                return false;
            }
            if (server.isExplicit() && server.getExplicit().getExplicitValue() != null && (server.getExplicit().getExplicitValue().getValue().length() >= 256 || server.getExplicit().getExplicitValue().getValue().length() <= 0)) {
                return false;
            }
            if (server.isExpression() && (server.getExpression().getExpressionPattern() == null || server.getExpression().getExpressionPattern().length() <= 0)) {
                return false;
            }

            // Verify TransferMode
            FtpProtocolOptions_TransferMode_Binding transferMode = protocolOptions.getFtpOptions().getTransferMode();
            if (transferMode.isExplicit() && transferMode.getExplicit().getExplicitValue() == null) {
                return false;
            }
            if (transferMode.isExpression() && (transferMode.getExpression().getExpressionPattern() == null || transferMode.getExpression().getExpressionPattern().length() <= 0)) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean validateMetadataOptions(ScanMetadataOptions metadataOptions) {
        if (metadataOptions.getFileContent() == null || metadataOptions.getFileContentType() == null || metadataOptions.getFileName() == null) {
            return false;
        }

        // Verify File Content
        ScanMetadataOptions_FileContent_Binding fileContent = metadataOptions.getFileContent();
        if (fileContent.isExplicit() && fileContent.getExplicit().getExplicitValue() == null) {
            return false;
        }
        if (fileContent.isExpression() && (fileContent.getExpression().getExpressionPattern() == null || fileContent.getExpression().getExpressionPattern().length() <= 0)) {
            return false;
        }

        // Verify File Content Type
        ScanMetadataOptions_FileContentType_Binding fileContentType = metadataOptions.getFileContentType();
        if (fileContentType.isExplicit() && fileContentType.getExplicit().getExplicitValue() == null) {
            return false;
        }
        if (fileContentType.isExplicit() && fileContentType.getExplicit().getExplicitValue() != null && fileContentType.getExplicit().getExplicitValue().getValue().length() >= 512) {
            return false;
        }
        if (fileContentType.isExpression() && (fileContentType.getExpression().getExpressionPattern() == null || fileContentType.getExpression().getExpressionPattern().length() <= 0)) {
            return false;
        }

        // Verify File Name
        ScanMetadataOptions_FileName_Binding fileName = metadataOptions.getFileName();
        if (fileName.isExplicit() && fileName.getExplicit().getExplicitValue() == null) {
            return false;
        }
        if (fileName.isExplicit() && fileName.getExplicit().getExplicitValue() != null && fileName.getExplicit().getExplicitValue().getValue().length() > 256) {
            return false;
        }
        if (fileName.isExpression() && (fileName.getExpression().getExpressionPattern() == null || fileName.getExpression().getExpressionPattern().length() <= 0)) {
            return false;
        }

        return true;
    }

    private boolean validateRetryOptions(ScanRetryOptions retryOptions) {
        if (retryOptions.getRetryInterval() == null || retryOptions.getMaxRetryAttempts() == null) {
            return false;
        }
        if (retryOptions.getRetryInterval() > 300 || retryOptions.getRetryInterval() <= 0 || retryOptions.getMaxRetryAttempts() > 5 || retryOptions.getMaxRetryAttempts() < 0) {
            return false;
        }

        return true;
    }

    /**
     * Checks if a scan ticket is valid
     *
     * @param ticket The scan ticket to validate
     */
    public Boolean isTicketValid(ScanTicket ticket) throws OptionProfileHelperException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        Boolean result = true;

        if (null == ticket || null == ticket.getDestinationOptions() || null == ticket.getScanOptions()) {
            return false;
        }

        // Validate Metadata Options If Present
        if (null != ticket.getMetadataOptions()) {
            result &= validateMetadataOptions(ticket.getMetadataOptions());
        }

        // Validate Retry Options If Present
        if (null != ticket.getRetryOptions()) {
            result &= validateRetryOptions(ticket.getRetryOptions());
        }

        // Evaluate the options-instance based on destination
        if (ticket.getDestinationOptions().isHttp()) {
            HttpOptions httpOptions = ticket.getDestinationOptions().getHttp();
            if (httpOptions.getDestination() == null || httpOptions.getDestination().getHost() == null || httpOptions.getDestination().getPath() == null || httpOptions.getDestination().getScheme() == null) {
                return false;
            }

            if (httpOptions.getDestination().getAuthorization() != null) {
                result &= validateHttpAuthorization(httpOptions.getDestination().getAuthorization());
            }

            if (httpOptions.getDestination().getHeaders() != null) {
                result &= validateHttpHeaders(httpOptions.getDestination().getHeaders());
            }

            result &= validateHttpTargetHost(httpOptions.getDestination().getHost());
            result &= validateHttpTargetPath(httpOptions.getDestination().getPath());

            if (httpOptions.getDestination().getRetry() != null) {
                result &= validateHttpRetry(httpOptions.getDestination().getRetry());
            }

            result &= httpScanOptionsProfileHelper.isValid(ticket.getScanOptions());
        } else if (ticket.getDestinationOptions().isNetworkFolder()) {
            NetworkFolderOptions networkFolderOptions = ticket.getDestinationOptions().getNetworkFolder();
            if (networkFolderOptions.getCredentialSource() == null || networkFolderOptions.getProtocolOptions() == null) {
                return false;
            }

            if (networkFolderOptions.getCredential() != null) {
                result &= validateNetworkFolderCredential(networkFolderOptions.getCredential());
            }

            result &= validateProtocolOptions(networkFolderOptions.getProtocolOptions());

            result &= networkFolderScanOptionsProfileHelper.isValid(ticket.getScanOptions());
        } else if (ticket.getDestinationOptions().isJobStorage()) {
            JobStorageOptions jobStorageOptions = ticket.getDestinationOptions().getJobStorage();
            if (jobStorageOptions.getJobName() == null || jobStorageOptions.getJobPasswordType() == null) {
                return false;
            }

            if (jobStorageOptions.getFolderName() != null) {
                result &= validateJobStorageFolderName(jobStorageOptions.getFolderName());
            }

            result &= validateJobStorageJobName(jobStorageOptions.getJobName());

            if (jobStorageOptions.getJobPassword() != null) {
                result &= validateJobStorageJobPassword(jobStorageOptions.getJobPassword());
            }

            result &= validateJobStorageJobPasswordType(jobStorageOptions.getJobPasswordType());
            
            result &= jobStorageScanOptionsProfileHelper.isValid(ticket.getScanOptions());
        } else {
            result = false;
        }

        return result;
    }
}
