package com.hp.jetadvantage.link.services.accesslet.adapter;

import android.util.Log;

import java.util.Base64;

public class PrincipalContextData {
    private final String authAgentId;
    private final String authType;
    private final String emailAddress;
    private final String fullyQualifiedUserName;
    private final String userDomain;
    private final String userName;

    public String getAuthAgentId() {
        return authAgentId;
    }

    public String getAuthType() {
        return authType;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getFullyQualifiedUserName() {
        return fullyQualifiedUserName;
    }

    public String getUserDomain() {
        return userDomain;
    }

    public String getUserName() {
        return userName;
    }

    private PrincipalContextData(
            String authAgentId,
            String authType,
            String emailAddress,
            String fullyQualifiedUserName,
            String userDomain,
            String userName
    ) {
        this.authAgentId = authAgentId;
        this.authType = authType;
        this.emailAddress = emailAddress;
        this.fullyQualifiedUserName = fullyQualifiedUserName;
        this.userDomain = userDomain;
        this.userName = userName;
    }

    public static PrincipalContextData fromResolvedExpression(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return new PrincipalContextData(null, null, null, null, null, null);
        }

        String authAgentId = null;
        String authType = null;
        String emailAddress = null;
        String fullyQualifiedUserName = null;
        String userDomain = null;
        String userName = null;

        String[] lines = expression.split("\n");
        for (String line : lines) {
            if (line.contains("=")) {
                String[] parts = line.split("=", 2);
                String key = parts[0].trim();
                String value = parts[1].trim();

                if (!value.isEmpty()) {
                    try {
                        value = new String(Base64.getDecoder().decode(value));
                    } catch (IllegalArgumentException e) {
                        //Decoding fails
                    }
                }

                ContextKey contextKey = ContextKey.fromValue(key);
                if (contextKey == null) {
                    continue; // Ignore unsupported keys
                }
                switch (contextKey) {
                    case AUTH_AGENT_ID:
                        authAgentId = value;
                        break;
                    case AUTH_TYPE:
                        authType = value;
                        break;
                    case EMAIL_ADDRESS:
                        emailAddress = value;
                        break;
                    case FQ_USER_NAME:
                        fullyQualifiedUserName = value;
                        break;
                    case USER_DOMAIN:
                        userDomain = value;
                        break;
                    case USER_NAME:
                        userName = value;
                        break;
                    default:
                        // Ignore other keys
                        break;
                }
            }
        }
        return new PrincipalContextData(authAgentId, authType, emailAddress, fullyQualifiedUserName, userDomain, userName);
    }
}
