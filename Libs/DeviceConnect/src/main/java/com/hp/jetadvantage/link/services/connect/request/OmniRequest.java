// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.connect.request;

public class OmniRequest {
    public static final String SYSTEM_LANGUAGE_CAPABILITY = "/hp/device/apis/v1/localization/";
    public static final String SYSTEM_STATUS = "/hp/device/apis/v1/status/?expand=href";
    public static final String SYSTEM_ACTIVE_JOBS = "/hp/device/apis/v1/activeJobs";
    public static final String SYSTEM_COMPLETED_JOBS = "/hp/device/apis/v1/completedJobs/";
    public static final String SYSTEM_SECURITY_CONTEXTS = "/hp/device/apis/v1/securityContexts?expand=href";
    public static final String SYSTEM_CURRENT_LANGUAGE = "/hp/device/apis/v1/localizationData/?expand=href";
    public static final String SYSTEM_GATEWAY = "/hp/device/apis/v1/gateway/?expand=href";
    public static final String PACKAGE_MANAGEMENT = "/hp/device/apis/v1/packageManagement";
    public static final String EMAIL_DEFAULTS = "/hp/device/apis/v1/emailWizard/default";
    public static final String ATTESTATION_SERVICE = "/hp/network/security/oauth2/v1/deviceIdentityAuth/serviceId/svc_app_attestation";
    public static final String SYSTEM_CLOUD_AUTHZ = "/hp/device/apis/v1/hpCloudAuthZCode";
    public static final String STORAGE_DEVICES = "/hp/device/apis/v1/storageDevices/?expand=href";
    public static final String DEVICE_STATE = "/hp/device/apis/v1/deviceState/state";

    //StatisticsRequest
    public static final String GET_STATISTICS_JOB_DATA_INFO = "/hp/device/apis/v1/statisticsJobDataProviders";
    public static final String STATISTICS_STATE_INFO = "/hp/device/apis/v1/statisticsStateProviders";
    public static final String STATISTICS_JOB_END_NOTIFICATION = "/hp/device/apis/v1/statisticsJobEndNotificationProviders/instance";

    //DeviceEvents
    public static final String DEVICE_EVENTS_INFO = "/hp/device/apis/v1/deviceEventsProviders";
    public static final String DEVICE_EVENTS = DEVICE_EVENTS_INFO+"/instance";
    public static final String DEVICE_EVENTS_LIST_INFO = "/hp/device/apis/v1/deviceEventsListProviders";

    //SuppliesRequest
    //public static final String GET_SUPPLIES = "/hp/device/apis/v1/suppliesDataProviders";
    public static final String GET_SUPPLIES = "/hp/device/apis/v1/suppliesOnlyDataProviders";
    //DeviceUsageRequest
    public static final String GET_DEVICEUSAGE = "/hp/device/apis/v1/usageDataProviders";

    public static final String AUTH_PROVIDER_PROMPT_RESULT = "/hp/device/apis/v1/authNProviderPrompt/Result";
    public static final String AUTH_PROVIDER_PRE_PROMPT_RESULT = "/hp/device/apis/v1/authNProviderPrePrompt/Result";
    public static final String AUTH_PROVIDER_PROVIDER_CONTEXTS = "/hp/device/apis/v1/authNProviderContext";


    // DUNE
    public static final String STORAGE_DEVICES_DUNE = "/cdm/storageDevices/v1/removableDevices";
    public static final String NETWORK_PRINT_SERVICES_DUNE = "/cdm/network/v1/printServices";
    public static final String SYSTEM_IDENTITY_DUNE = "/cdm/system/v1/identity";
    public static final String COUNTRY_REGION_BY_LANGUAGE_DUNE = "/cdm/localization/v1/countryRegionByLanguage";
    public static final String IOCONFIG_ADAPTER_CONFIG_ETH0_DUNE = "/cdm/ioConfig/v2/adapterConfigs/eth0";
    public static final String JOBTICKET_SCAN_EMAIL_DUNE = "/cdm/jobTicket/v1/configuration/defaults/scanEmail";
    public static final String DEFAULT_SMTP_SERVERS_DUNE = "/cdm/email/v1/defaultSmtpServers";
    public static final String PRIVATE_EMAIL_CONFIG_DUNE = "/cdm/e2WorkpathInterop/v1/emailConfig";
    public static final String UI_CONTEXT_TOKEN_DUNE = "/cdm/e2WorkpathInterop/v1/ext/uiContext";
    public static final String TOKEN_INFO = "/cdm/security/v1/tokenInfo";
    public static final String SIGN_IN = "/ext/authentication/v1/session/authenticationAgents/%s/login";
    public static final String SIGN_OUT = "/ext/authentication/v1/session/forceLogout";
    public static final String AUTH_CAPABILITIES = "/ext/authentication/v1/capabilities";
    // DUNE - Alerts
    public static final String ALERT = "/cdm/alert/v1/alerts";
    public static final String CRITICAL_ALERT = "/cdm/alert/v1/criticalAlerts";
    public static final String ERROR_ALERT = "/cdm/alert/v1/errorAlerts";
    public static final String LONG_POLLING = "/cdm/pubsub/v2/subscriptions";
    public static final String LONG_POLLING_CAPABILITY = "/cdm/pubsub/v2/capabilities";
    public static final String DUNE_LOCALIZATION = "/hp/device/localization/localize.json";

    public static final String GET = "GET";
    public static final String PUT = "PUT";
    public static final String POST = "POST";
    public static final String DELETE = "DELETE";

    private String transactionId;
    private String verb;
    private String uri;
    private String data;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
