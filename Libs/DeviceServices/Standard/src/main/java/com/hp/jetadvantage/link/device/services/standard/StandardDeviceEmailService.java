/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.standard;

import com.hp.ext.clients.device.DeviceServiceClientImpl;
import com.hp.ext.service.device.Email;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceEmailService;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.DeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;
import com.hp.jetadvantage.link.device.services.types.EmailSettingsData;
import com.hp.ws.e2workpathInterop.SmtpServer;
import com.hp.ws.e2workpathInterop.SmtpServers;
import com.hp.ws.e2workpathInterop.Credential;
import com.hp.ws.cdm.jobticket.JobTicket;


public class StandardDeviceEmailService extends StandardDeviceService implements IDeviceEmailService {
    private static final String TAG = Constants.TAG + "/Email";
    private static final int DEFAULT_SMTP_SERVER_INDEX = 0;

    public StandardDeviceEmailService() {
        super();
    }

    public StandardDeviceEmailService(DeviceManagementService deviceManagementService) {
        super(deviceManagementService);
    }

    @Override
    public boolean isOnline() {
        E2call<Email> call = () -> {
            DeviceServiceClientImpl deviceServiceClientImpl = new DeviceServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return deviceServiceClientImpl.email().getAsync().get();
        };
        Email email = perform(call);

        return email.getIsOnline();
    }

    @Override
    public EmailSettingsData getEmailSettings() {
        CdmCall call = () -> getCDMClient().sendGetRequest(CDMUrl.JOBTICKET_SCAN_EMAIL_DUNE);
        JobTicket jobTicket = perform(call, JobTicket.class);

        call = () -> getCDMClient().sendGetRequest(CDMUrl.NETWORK_CONFIGURATION_INTEROP);
        SmtpServers smtpServers = perform(call, SmtpServers.class);

        EmailSettingsData emailSettingsData = new EmailSettingsData();

        if (jobTicket != null && jobTicket.getDest() != null 
            && jobTicket.getDest().getEmail() != null 
            && jobTicket.getDest().getEmail().getFrom() != null) {
            String defaultDisplayName = jobTicket.getDest().getEmail().getFrom().getDisplayName();
            String defaultEmailAddress = jobTicket.getDest().getEmail().getFrom().getEmailAddress();

            if (defaultDisplayName == null || defaultDisplayName.isEmpty() || defaultEmailAddress == null || defaultEmailAddress.isEmpty()) {
                SLog.d(TAG, "Default from address is not configured");
            }

            emailSettingsData.setDisplayNameForDefaultFrom(defaultDisplayName);
            emailSettingsData.setEmailAddressForDefaultFrom(defaultEmailAddress);
        } else {
            SLog.d(TAG, "JobTicket default from information is not available");
        }
        
        boolean hasDefaultEmailAddress = emailSettingsData.getDefaultFrom().getEmailAddress() != null &&
                !emailSettingsData.getDefaultFrom().getEmailAddress().isEmpty();

        boolean configuredSmtpServer = smtpServers != null && 
                                smtpServers.getServers() != null && 
                                !smtpServers.getServers().isEmpty();

        // Both SMTP server and default email address must be configured to enable email functionality
        emailSettingsData.setSendToEmailEnabled(hasDefaultEmailAddress && configuredSmtpServer);
        SLog.d(TAG, "Scan to Email configuration status - Enabled: " + emailSettingsData.isSendToEmailEnabled() +
                ", Default From: " + hasDefaultEmailAddress +
                ", Default SMTP server : " + configuredSmtpServer);

        if (configuredSmtpServer) {
            SmtpServer defaultSmtpServer = smtpServers.getServers().get(DEFAULT_SMTP_SERVER_INDEX);

            emailSettingsData.setFromDisplayName(defaultSmtpServer.getDisplayName());
            emailSettingsData.setSmtpServerHostName(defaultSmtpServer.getServerAddress());
            emailSettingsData.setSmtpServerPort(defaultSmtpServer.getServerPort());
            emailSettingsData.setUseSSL(Boolean.parseBoolean(defaultSmtpServer.getUseSsl().value()));
            emailSettingsData.setAuthenticationRequired(Boolean.parseBoolean(defaultSmtpServer.getServerRequireAuthentication().value()));

            Credential credential = defaultSmtpServer.getServerCredential();
            if (credential != null) {
                emailSettingsData.setSmtpServerUserName(credential.getUserName());
                emailSettingsData.setSmtpServerPassword(credential.getPassword());
            }
        }

        return emailSettingsData;
    }

    public static final class CDMUrl {
        public static final String JOBTICKET_SCAN_EMAIL_DUNE = "/cdm/jobTicket/v1/configuration/defaults/scanEmail";
        public static final String NETWORK_CONFIGURATION_INTEROP = "/cdm/e2WorkpathInterop/v1/networkConfiguration";
    }
}
