package com.hp.jetadvantage.link.services.accesslet.adapter;

import android.util.Log;
import com.hp.ext.service.security.ResolveSecurityExpressionRequest;
import com.hp.ext.service.security.SecurityAgent_ResolveSecurityExpression;
import com.hp.jetadvantage.link.api.access.Accesslet;
import com.hp.jetadvantage.link.api.access.Principal;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAccessService;
import com.hp.jetadvantage.link.services.accesslet.PrincipalInternal;
import com.hp.ws.cdm.security.AuthenticationAgent;
import com.hp.ws.cdm.security.AuthenticationAgents;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AccessAdapter {
    private static final String TAG = Accesslet.TAG + "/AccessAdapter";
    private static final String EXPRESSION = "$ENCODE(BASE64, $SECURITY_CONTEXT(";

    private static final String GUEST_FULLY_QUALIFIED_NAME = "LOCAL\\\\guest";
    private static final String LOCAL = "LOCAL";

    public static boolean isSupported(IDeviceAccessService accessService) {
        return accessService.isSupported();
    }

    private static String getPrincipal(IDeviceAccessService accessService, String packName) {
        ResolveSecurityExpressionRequest request = new ResolveSecurityExpressionRequest();
        String expression = buildExpression();
        request.setExpression(expression);
        SecurityAgent_ResolveSecurityExpression result = accessService.getCurrentPrincipal(packName, request);
        return result.getResult();
    }

    private static String buildExpression() {
        StringBuilder expressionBuilder = new StringBuilder();
        for (ContextKey key : ContextKey.values()) {
            expressionBuilder
                    .append(key.getValue()).append("=")
                    .append(EXPRESSION).append(key.getValue()).append(")$)$")
                    .append("\n");
        }
        expressionBuilder.append("_CANARY=OK\"");
        Log.d(TAG, "Built expression: " + expressionBuilder.toString());
        return expressionBuilder.toString();
    }

    public static AuthenticationAgents getAuthenticationAgent(IDeviceAccessService accessService) {
        return accessService.getAuthenticationAgents();
    }

    public static Principal getPrincipalProperties(IDeviceAccessService accessService, String packName) {
        PrincipalInternal principal = new PrincipalInternal();
        AuthenticationAgents authenticationAgent = AccessAdapter.getAuthenticationAgent(accessService);

        PrincipalContextData principalData = PrincipalContextData.fromResolvedExpression(
                AccessAdapter.getPrincipal(accessService, packName)
        );

        List<String> spsAuths = new ArrayList<>();

        principal.setSupported(AccessAdapter.isSupported(accessService));
        principal.setUsername(principalData.getUserName());
        principal.setUserEmail(principalData.getEmailAddress());
        principal.setPassword(""); //SDK1.3 because of security

        String domain = principalData.getUserDomain();
        principal.setDomain(domain);

        AuthType authType = AuthType.fromValue(principalData.getAuthType());

        switch (authType) {
            case DEVICE_ADMIN:
                spsAuths.add(Principal.SimpleAuthority.ADMIN.name());
                principal.setAdmin(true);
                break;
            case GUEST:
                principal.setGuestUser(true);
                break;
            case DEVICE_USER:
            case DEVICE_PIN:
                principal.setDeviceUser(true);
                break;
            case SMART_CARD:
                principal.setSmartCardUser(true);
                break;
            case DEVICE_SERVICE:
                principal.setServiceUser(true);
                break;
            case UNKNOWN:
                principal.setDeviceUser(true);
                Log.d(TAG, "Unknown auth type: " + principalData.getAuthType());
                break;
            default:
                Log.d(TAG, "Unknown auth type: " + principalData.getAuthType());
                break;
        }

        boolean isGuestUser = principal.isGuestUser();
        principal.setAuthenticated(!isGuestUser);

        String[] authorities = {"PRINT", "SCAN", "COPY", "JOB_CONTROL"};
        spsAuths.addAll(Arrays.asList(authorities));
        principal.setSimpleAuthorities(spsAuths);

        String authAgentId = principalData.getAuthAgentId();
        principal.setProviderUUID(authAgentId);
        String provider = getProvider(authenticationAgent, authAgentId, isGuestUser);
        Log.d(TAG,"Provider: " + provider);
        principal.setProvider(provider);

        String fullyQualifiedName = getFullyQualifiedName(isGuestUser, principalData);
        Log.d(TAG, "Fully Qualified Name: " + fullyQualifiedName);
        principal.setFullyQualifiedName(fullyQualifiedName);

        String principalId = getPrincipalId(fullyQualifiedName);
        Log.d(TAG, "Principal Id: " + principalId);
        principal.setPrincipalId(principalId);

        // https://hp-jira.external.hp.com/browse/DUNE-243822
        HashMap<String, String> userProperties = new HashMap<>();
        userProperties.put(UserProperty.AUTH_AGENT_NAME.getValue(), valueOrEmpty(provider));
        userProperties.put(UserProperty.AUTH_AGENT_UUID.getValue(), valueOrEmpty(authAgentId));
        userProperties.put(UserProperty.DISPLAY_NAME.getValue(), valueOrEmpty(principalData.getUserName()));
        userProperties.put(UserProperty.EMAIL_ADDRESS.getValue(), valueOrEmpty(principalData.getEmailAddress()));
        userProperties.put(UserProperty.USER_NAME.getValue(), valueOrEmpty(principalData.getUserName()));
        userProperties.put(UserProperty.USER_NAME_LOWER.getValue(), valueOrEmpty(principalData.getUserName()));
        userProperties.put(UserProperty.DOMAIN.getValue(), valueOrEmpty(domain));
        principal.setUserProperties(userProperties);
        Log.d(TAG, "User Properties: " + userProperties);

        String principalInString = JsonParser.getInstance().toJson(principal);
        return JsonParser.getInstance().fromJson(principalInString, Principal.class);
    }

    private static String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }

    private static String getFullyQualifiedName(boolean isGuestUser, PrincipalContextData principalData) {
        if (isGuestUser) {
            return GUEST_FULLY_QUALIFIED_NAME;
        }
        return principalData.getFullyQualifiedUserName();
    }

    public static String getPrincipalId(String fullyQualifiedName) {
        if (fullyQualifiedName == null || fullyQualifiedName.isEmpty()) {
            return null;
        } else {
            int lastBackslashIndex = fullyQualifiedName.lastIndexOf('\\');
            if (lastBackslashIndex >= 0 && lastBackslashIndex < fullyQualifiedName.length() - 1) {
                return fullyQualifiedName.substring(lastBackslashIndex + 1);
            } else {
                return fullyQualifiedName;
            }
        }
    }

    public static String getProvider(AuthenticationAgents authenticationAgent, String authAgentId, boolean isGuestUser) {
        if (isGuestUser) return LOCAL;
        if (authAgentId == null || authAgentId.isEmpty()) return  null;
        List<AuthenticationAgent> authenticationAgentsList = authenticationAgent.getAuthenticationAgents();
        for (int i = 0; i < authenticationAgentsList.size(); i++) {
            if (authAgentId.equals(authenticationAgentsList.get(i).getAgentId())) {
                return authenticationAgentsList.get(i).getAgentName();
            }
        }
        return null;
    }
}
