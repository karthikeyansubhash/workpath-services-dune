// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
/**
 * PackageManager Contract
 *
 * @author Link SDK Team (link.sdk@hp.com)
 * @version 1.2.0, 20181227
 *          1.1.0, 20181210
 */
package com.hp.jetadvantage.link.common.constants;

import android.net.Uri;
import android.provider.BaseColumns;

@SuppressWarnings({"unused", "WeakerAccess"})
public interface PackageContract {
    String CONTENT_SCHEME = "content";
    String PACKAGES_AUTHORITY = "packages";
    String PROVIDERS_AUTHORITY = "providers";
    String PACKAGES_ATTESTATION_AUTHORITY = "packages-attestation";
    String PACKAGES_SOLUTION_PATH_SEGMENT = "solution";
    String INSTALLERS_AUTHORITY = "packages-installers";
    Uri PACKAGES_CONTENT_URI = new Uri.Builder().scheme(CONTENT_SCHEME)
            .authority(PACKAGES_AUTHORITY).build();
    Uri INSTALLERS_CONTENT_URI = new Uri.Builder().scheme(CONTENT_SCHEME)
            .authority(INSTALLERS_AUTHORITY).build();
    Uri PROVIDERS_CONTENT_URI = new Uri.Builder().scheme(CONTENT_SCHEME)
            .authority(PROVIDERS_AUTHORITY).build();
    Uri PACKAGES_ATTESTATION_CONTENT_URI = new Uri.Builder().scheme(CONTENT_SCHEME)
            .authority(PACKAGES_ATTESTATION_AUTHORITY).build();

    // Install/Uninstall activities return code
    interface Error {
        String EC_NO_ERROR = "ec_no_error";
        String EC_INVALID_PACKAGE = "ec_invalid_package";
        String EC_INVALID_CERT = "ec_invalid_cert";
        String EC_EXPIRED_CERT = "ec_expired_cert";
        String EC_NOT_ENOUGH_SPACE = "ec_not_enough_space";
        String EC_FILE_IO = "ec_file_io";
        String EC_INTERNAL_ERR = "ec_internal_err";
        String EC_INVALID_REQUEST = "ec_invalid_request";
        String EC_AUTHORIZATION_ERROR = "ec_authorization_error";
    }

    interface Intent {
        String INSTALL_PACKAGE = "com.hp.packagemanager.intent.action.INSTALL";
        String UNINSTALL_PACKAGE = "com.hp.packagemanager.intent.action.UNINSTALL";

        String ACTION_PACKAGE_INSTALLED = "com.hp.packagemanager.intent.action.PACKAGE_INSTALLED";
        String ACTION_PACKAGE_UPDATED = "com.hp.packagemanager.intent.action.PACKAGE_UPDATED";
        String ACTION_PACKAGE_UNINSTALLED = "com.hp.packagemanager.intent.action.PACKAGE_UNINSTALLED";

        //1.2
        String EXTRA_APPLICATION_AGENT_ID = "EXTRA_APPLICATION_ID";
        String EXTRA_SOLUTION_ID = "EXTRA_SOLUTION_ID";
        String EXTRA_CLIENT_ID = "EXTRA_CLIENT_ID";
        String EXTRA_INSTALL_SOURCE = "EXTRA_INSTALL_SOURCE";

        // returned package object
        String EXTRA_PACKAGE = "EXTRA_PACKAGE";
    }

    interface Permission {
        String PACKAGE_LIFECYCLE_EVENTS = "com.hp.packagemanager.permission.PACKAGE_LIFECYCLE_EVENTS";
        // android permission
        String GRANT_RUNTIME_PERMISSIONS = "android.permission.GRANT_RUNTIME_PERMISSIONS";
    }

    /* Inner class that defines Package table contents */
    interface PackageEntry extends BaseColumns {
        String APPLICATION_AGENT_ID = "applicationAgentId";
        String SOLUTION_ID = "solutionId";
        String SOLUTION_NAME = "solutionName";
        String HPK2_VERSION = "hpk2Version";
        String APK_VERSION = "apkVersion";
        String PACKAGE_NAME = "packageName";
        String INSTALL_DATE = "installDate";
        String METADATA = "metaData";
        String VENDOR_NAME = "vendorName";
        String LAUNCH_INTENT = "launchIntent";
        String IS_MAIN_ACTIVITY = "isMainActivity";
        String IS_HOME_SCREEN_APP = "isHomeScreenApp";
        String ALLOWLIST_TYPE = "allowlistType";
    }

    String[] ALL_PROJECTION_PACKAGE = {
            PackageEntry.APPLICATION_AGENT_ID,
            PackageEntry.SOLUTION_ID,
            PackageEntry.SOLUTION_NAME,
            PackageEntry.HPK2_VERSION,
            PackageEntry.APK_VERSION,
            PackageEntry.PACKAGE_NAME,
            PackageEntry.INSTALL_DATE,
            PackageEntry.METADATA,
            PackageEntry.VENDOR_NAME,
            PackageEntry.LAUNCH_INTENT,
            PackageEntry.IS_MAIN_ACTIVITY,
            PackageEntry.IS_HOME_SCREEN_APP,
            PackageEntry.ALLOWLIST_TYPE
    };

    String[] DEFAULT_PROJECTION_PACKAGE = {
            PackageEntry.APPLICATION_AGENT_ID,
            PackageEntry.SOLUTION_ID,
            PackageEntry.SOLUTION_NAME,
            PackageEntry.HPK2_VERSION,
            PackageEntry.APK_VERSION,
            PackageEntry.PACKAGE_NAME,
            PackageEntry.INSTALL_DATE,
            PackageEntry.METADATA,
            PackageEntry.VENDOR_NAME,
            PackageEntry.LAUNCH_INTENT,
            PackageEntry.IS_MAIN_ACTIVITY,
            PackageEntry.ALLOWLIST_TYPE
    };

    /* Inner class that defines Installer table contents */
    interface PackageInstallerEntry extends BaseColumns {
        String SOLUTION_ID = "solutionId";
        String SOLUTION_NAME = "solutionName";
        String PACKAGE_NAME = "packageName";
        String STATE = "state";
        String ERROR = "error";
        String LAST_UPDATED = "lastUpdated";
    }

    String[] ALL_PROJECTION_INSTALLER = {
            PackageInstallerEntry.SOLUTION_ID,
            PackageInstallerEntry.SOLUTION_NAME,
            PackageInstallerEntry.PACKAGE_NAME,
            PackageInstallerEntry.LAST_UPDATED,
            PackageInstallerEntry.STATE,
            PackageInstallerEntry.ERROR
    };

    /* Inner class that defines providers table contents */
    interface PackageProviderEntry extends BaseColumns {
        String SOLUTION_ID = "solutionId";
        String AGENT_ID = "agentId";
        String PACKAGE_NAME = "packageName";
        String FUNCTION_TYPE = "functionType";
        String AGENT_NAME = "agentName";
        String AGENT_DESCRIPTION = "agentDescription";
        String LAUNCH_INTENT = "launchIntent";
        String METADATA = "metaData";
        String EXT_DATA1 = "extData1";
        String EXT_DATA2 = "extData2";
        String EXT_DATA3 = "extData3";
    }

    String[] ALL_PROJECTION_PROVIDER = {
            PackageProviderEntry.SOLUTION_ID,
            PackageProviderEntry.AGENT_ID,
            PackageProviderEntry.PACKAGE_NAME,
            PackageProviderEntry.FUNCTION_TYPE,
            PackageProviderEntry.AGENT_NAME,
            PackageProviderEntry.AGENT_DESCRIPTION,
            PackageProviderEntry.LAUNCH_INTENT,
            PackageProviderEntry.METADATA,
            PackageProviderEntry.EXT_DATA1,
            PackageProviderEntry.EXT_DATA2,
            PackageProviderEntry.EXT_DATA3
    };

    /* Inner class that defines attestation table contents */
    interface PackageAttestationEntry extends BaseColumns {
        String SOLUTION_ID = "solutionId";
        String AUTH = "auth";
        String USER = "user";
        String KEY = "key";
        String DATA = "data";
        String INSTALL_DATE = "installDate";
        String MODIFY_DATE = "modifyDate";
    }

    String[] ALL_PROJECTION_ATTESTATION = {
            PackageAttestationEntry.SOLUTION_ID,
            PackageAttestationEntry.AUTH,
            PackageAttestationEntry.USER,
            PackageAttestationEntry.KEY,
            PackageAttestationEntry.DATA,
            PackageAttestationEntry.INSTALL_DATE,
            PackageAttestationEntry.MODIFY_DATE
    };
}
