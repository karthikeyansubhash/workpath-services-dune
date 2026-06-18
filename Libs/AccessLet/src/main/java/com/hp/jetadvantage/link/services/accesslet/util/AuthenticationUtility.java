// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.accesslet.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hp.ext.service.authentication.AuthenticationAgentRegistrationRecord;
import com.hp.ext.types.authentication.AuthenticationCanceled;
import com.hp.ext.types.authentication.AuthenticationContinued;
import com.hp.ext.types.authentication.AuthenticationFailed;
import com.hp.ext.types.authentication.AuthenticationSuccess;
import com.hp.ext.types.common.E2Type;
import com.hp.ext.types.common.EmailAddress;
import com.hp.ext.types.common.FullyQualifiedUserName;
import com.hp.ext.types.localization.LanguageTag;
import com.hp.ext.types.security.KeyValuePairKey;
import com.hp.ext.types.security.KeyValuePairValue;
import com.hp.ext.types.security.WritableAuthenticatedUserDetails;
import com.hp.jetadvantage.link.api.JetAdvantageLink;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.access.Accesslet;
import com.hp.jetadvantage.link.api.access.AuthenticationAttributes;
import com.hp.jetadvantage.link.api.access.EmailAddressInfo;
import com.hp.jetadvantage.link.api.access.Principal;
import com.hp.jetadvantage.link.api.access.SignInAction;
import com.hp.jetadvantage.link.api.access.UserOverridesAttributes;
import com.hp.jetadvantage.link.api.access.UserPreferencesAttributes;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.constants.PackageContract;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.accesslet.PrincipalInternal;
import com.hp.jetadvantage.link.services.accesslet.model.ResultCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AuthenticationUtility {
    private static final String TAG = Accesslet.TAG + ":" + AuthenticationUtility.class.getSimpleName();
    public static final String AUTH_PROVIDER = new AuthenticationAgentRegistrationRecord().getTypeGUN();

    public static WritableAuthenticatedUserDetails generateAuthenticationData(AuthenticationAttributes attributes) {
        WritableAuthenticatedUserDetails details = new WritableAuthenticatedUserDetails();
        String autoLaunchAppAccessPointId = null;

        UserOverridesAttributes userOverridesAttributes = attributes.getUserOverridesAttributes();
        if (userOverridesAttributes != null) {
            details.setEmailToAddresses(getEmailAddressFromUserOverridesEmail(userOverridesAttributes.getTo()));
            details.setEmailCCAddresses(getEmailAddressFromUserOverridesEmail(userOverridesAttributes.getCc()));
            details.setEmailBCCAddresses(getEmailAddressFromUserOverridesEmail(userOverridesAttributes.getBcc()));
            if (userOverridesAttributes.getFrom() != null)
                details.setEmailFromAddress(new EmailAddress(userOverridesAttributes.getFrom().getAddress()));
            details.setEmailMessage(userOverridesAttributes.getMessage());
            details.setEmailSubject(userOverridesAttributes.getSubject());
            details.setFaxBillingCode(userOverridesAttributes.getFaxBillingCode());
            details.setFaxCompanyName(userOverridesAttributes.getFaxCompanyName());
        }
//        details.setPassword(attributes.getPassword()); //DO NOT handling password for security.

        UserPreferencesAttributes userPreferencesAttributes = attributes.getUserPreferencesAttributes();
        if (userPreferencesAttributes != null) {
            details.setPreferredLanguage(getLanguageTagFromLanguageCode(userPreferencesAttributes.getLanguageCode()));
            autoLaunchAppAccessPointId = userPreferencesAttributes.getAutoLaunchAppAccessPointId();
        }

        details.setDisplayName(attributes.getDisplayName());
        details.setEmailAddress(new EmailAddress(attributes.getUserEmail()));
        details.setExchangeMailboxUri(attributes.getExchangeMailboxUri());
        details.setFullyQualifiedUserName(new FullyQualifiedUserName(attributes.getFullyQualifiedName()));
        details.setHomeFolderPath(attributes.getHomeFolderPath());
        details.setLdapBindUser(attributes.getLdapBindUser());
        details.setSAMAccountName(attributes.getSAMAccountName());
        details.setSidString(attributes.getSidString());
        details.setUserDomain(attributes.getUserDomain());
        details.setUserPrincipalName(attributes.getUserPrincipalName());
        details.setUserName(attributes.getUserName());

        List<com.hp.ext.types.security.KeyValuePair> keyValuePairs = null;
        if (attributes.getUserProperties() != null && !attributes.getUserProperties().isEmpty()) {
            keyValuePairs = new ArrayList<>();
            for (Map.Entry<String, String> entry : attributes.getUserProperties().entrySet()) {
                com.hp.ext.types.security.KeyValuePair keyValuePair = new com.hp.ext.types.security.KeyValuePair();
                KeyValuePairKey keyValuePairKey = new KeyValuePairKey(entry.getKey());
                KeyValuePairValue keyValuePairValue = new KeyValuePairValue(entry.getValue());
                keyValuePair.setKey(keyValuePairKey);
                keyValuePair.setValueString(keyValuePairValue);
                keyValuePairs.add(keyValuePair);
            }

            if (autoLaunchAppAccessPointId != null) {
                // Add the auto-launch app access point ID as a key-value pair
                com.hp.ext.types.security.KeyValuePair autoLaunchPair = new com.hp.ext.types.security.KeyValuePair();
                KeyValuePairKey keyValuePairKey = new KeyValuePairKey("autoLaunchAppAccessPointId");
                KeyValuePairValue keyValuePairValue = new KeyValuePairValue(autoLaunchAppAccessPointId);
                autoLaunchPair.setKey(keyValuePairKey);
                autoLaunchPair.setValueString(keyValuePairValue);
                keyValuePairs.add(autoLaunchPair);
            }
        }
        details.setCustomAttributes(keyValuePairs);

        return details;
    }

    public static E2Type getAuthenticationResultFromAuthenticationAttributes(AuthenticationAttributes authenticationAttributes, SignInAction signInAction) {
        if (signInAction == null) {
            throw new IllegalArgumentException("SignInAction is mandatory parameter");
        }
        ResultCode action = getResultCode(signInAction.getAction());
        if (action == ResultCode.SUCCEEDED) {
            if (authenticationAttributes == null) {
                throw new IllegalArgumentException("AuthenticationAttributes is mandatory parameter");
            }
            AuthenticationSuccess success = new AuthenticationSuccess();
            WritableAuthenticatedUserDetails details = generateAuthenticationData(authenticationAttributes);
            success.setDetails(details);
            return success;
        } else if (action == ResultCode.CANCELED) {
            return new AuthenticationCanceled();
        } else if (action == ResultCode.BACK_CANCELED) {
            // TODO: There is no screen move after cancel.
            return new AuthenticationCanceled();
        } else if (action == ResultCode.HOME_CANCELED) {
            // TODO: There is no screen move after cancel.
            return new AuthenticationCanceled();
        } else if (action == ResultCode.FAILED) {
            AuthenticationFailed failed = new AuthenticationFailed();
            failed.setMessage(signInAction.getFailureMessage());
            return failed;
        } else if (action == ResultCode.CONTINUED) {
            return new AuthenticationContinued();
        }
        throw new IllegalStateException("Unhandled action: " + action);
    }

    public static ResultCode getResultCode(SignInAction.Action signInAction) {
        ResultCode resultCode;
        switch (signInAction) {
            case SUCCESS:
                resultCode = ResultCode.SUCCEEDED;
                break;
            case CONTINUE:
                resultCode = ResultCode.CONTINUED;
                break;
            case CANCEL:
                resultCode = ResultCode.CANCELED;
                break;
            case FAIL:
                resultCode = ResultCode.FAILED;
                break;
            case HOME:
                resultCode = ResultCode.HOME_CANCELED;
                break;
            case BACK:
                resultCode = ResultCode.BACK_CANCELED;
                break;
            default:
                throw new IllegalArgumentException("Unsupported action value " + signInAction);
        }
        return resultCode;
    }

    //From Provider Info
    @SuppressLint("Range")
    public static Map<String, String> getAuthAgentPackageNameList(@NonNull final Context context) {
        Cursor providerCursor = null;
        Map<String, String> authagents = new HashMap<String, String>();
        try {
            String selection = PackageContract.PackageProviderEntry.FUNCTION_TYPE + " = ?";
            String[] selectionArgs = new String[]{AUTH_PROVIDER};

            providerCursor = context.getContentResolver().query(
                    PackageContract.PROVIDERS_CONTENT_URI,
                    null, selection, selectionArgs, null, null);

            if (providerCursor != null) {
                while (providerCursor.moveToNext()) {
                    String functionType = providerCursor.getString(
                            providerCursor.getColumnIndex(PackageContract.PackageProviderEntry.FUNCTION_TYPE));
                    if (AUTH_PROVIDER.equalsIgnoreCase(functionType)) {
                        String packageName = providerCursor.getString(
                                providerCursor.getColumnIndex(PackageContract.PackageProviderEntry.PACKAGE_NAME));
                        authagents.put(
                                packageName,
                                providerCursor.getString(providerCursor.getColumnIndex(PackageContract.PackageProviderEntry.AGENT_ID)));
                        SLog.d(TAG, "Found auth " + packageName);
                    }
                }
            }
        } finally {
            if (providerCursor != null) {
                providerCursor.close();
            }
        }
        return authagents;
    }

    @SuppressLint("Range")
    public static UUID getApplicationUuid(@NonNull final Context context, @NonNull final String uuid) {
        if (TextUtils.isEmpty(uuid)) return null;
        Cursor packageCursor = null;
        try {
            String selection = PackageContract.PackageEntry.APPLICATION_AGENT_ID + " = ? OR " + PackageContract.PackageEntry.SOLUTION_ID + " = ?";
            String[] selectionArgs = new String[]{uuid, uuid};

            packageCursor = context.getContentResolver().query(PackageContract.PACKAGES_CONTENT_URI,
                    null, selection, selectionArgs, null, null);

            if (packageCursor != null) {
                if (packageCursor.moveToFirst()) {
                    String appUuid = packageCursor.getString(
                            packageCursor.getColumnIndexOrThrow(PackageContract.PackageEntry.APPLICATION_AGENT_ID));
                    String solutionUuid = packageCursor.getString(
                            packageCursor.getColumnIndexOrThrow(PackageContract.PackageEntry.SOLUTION_ID));

                    if (uuid.equals(appUuid)) {
                        return UUID.fromString(appUuid);
                    } else if (uuid.equals(solutionUuid)) {
                        return UUID.fromString(appUuid);
                    }
                }
            }
        } finally {
            if (packageCursor != null) {
                packageCursor.close();
            }
        }

        return null;
    }

    private static LanguageTag getLanguageTagFromLanguageCode(String languageCode) {
        if (languageCode != null && !languageCode.isEmpty()) {
            return new LanguageTag(languageCode);
        }
        return new LanguageTag("en-US");
    }

    private static List<EmailAddress> getEmailAddressFromUserOverridesEmail(List<EmailAddressInfo> emailList) {
        List<EmailAddress> emailAddresses = new ArrayList<>();
        if (emailList != null && !emailList.isEmpty()) {
            for (EmailAddressInfo info : emailList) {
                EmailAddress emailAddress = new EmailAddress(info.getAddress());
                emailAddresses.add(emailAddress);
            }
        }
        return emailAddresses;
    }
}
