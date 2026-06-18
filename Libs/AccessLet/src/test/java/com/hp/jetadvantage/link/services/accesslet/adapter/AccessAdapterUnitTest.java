package com.hp.jetadvantage.link.services.accesslet.adapter;

import com.hp.ext.service.security.ResolveSecurityExpressionRequest;
import com.hp.ext.service.security.SecurityAgent_ResolveSecurityExpression;
import com.hp.jetadvantage.link.api.access.Principal;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAccessService;
import com.hp.ws.cdm.security.AuthenticationAgent;
import com.hp.ws.cdm.security.AuthenticationAgents;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccessAdapterUnitTest {

    @Mock
    private IDeviceAccessService accessService;

    @Mock
    private AuthenticationAgents authenticationAgents;

    @Mock
    private AuthenticationAgent authenticationAgent;

    @Mock
    private SecurityAgent_ResolveSecurityExpression resolvedPrincipal;

    @Captor
    private ArgumentCaptor<ResolveSecurityExpressionRequest> requestCaptor;

    @Test
    public void givenSupportedService_whenIsSupported_thenDelegatesToDeviceAccessService() {
        when(accessService.isSupported()).thenReturn(true);

        assertTrue(AccessAdapter.isSupported(accessService));
    }

    @Test
    public void givenAuthenticationAgents_whenGetAuthenticationAgent_thenReturnsConfiguredAgents() {
        when(accessService.getAuthenticationAgents()).thenReturn(authenticationAgents);

        assertSame(authenticationAgents, AccessAdapter.getAuthenticationAgent(accessService));
    }

    @Test
    public void givenFullyQualifiedNames_whenGetPrincipalId_thenExtractsTheLastPathSegment() {
        assertNull(AccessAdapter.getPrincipalId(null));
        assertNull(AccessAdapter.getPrincipalId(""));
        assertEquals("user", AccessAdapter.getPrincipalId("domain\\user"));
        assertEquals("user", AccessAdapter.getPrincipalId("user"));
        assertEquals("domain\\", AccessAdapter.getPrincipalId("domain\\"));
    }

    @Test
    public void givenGuestAndMatchingAgents_whenGetProvider_thenReturnsExpectedProvider() {
        when(authenticationAgents.getAuthenticationAgents()).thenReturn(
                Collections.singletonList(authenticationAgent("agent-1", "Provider One"))
        );

        assertEquals("LOCAL", AccessAdapter.getProvider(authenticationAgents, "agent-1", true));
        assertEquals("Provider One", AccessAdapter.getProvider(authenticationAgents, "agent-1", false));
        assertNull(AccessAdapter.getProvider(authenticationAgents, "missing", false));
        assertNull(AccessAdapter.getProvider(authenticationAgents, null, false));
    }

    @Test
    public void givenResolvedAdminPrincipal_whenGetPrincipalProperties_thenMapsPrincipalAndBuildsExpression() {
        when(accessService.isSupported()).thenReturn(true);
        when(accessService.getAuthenticationAgents()).thenReturn(authenticationAgents);
        when(authenticationAgents.getAuthenticationAgents()).thenReturn(
                Collections.singletonList(authenticationAgent("agent-123", "Provider One"))
        );
        when(accessService.getCurrentPrincipal(eq("com.example.app"), any(ResolveSecurityExpressionRequest.class)))
                .thenReturn(resolvedPrincipal);
        when(resolvedPrincipal.getResult()).thenReturn(resolvedExpression(
                "agent-123",
                "AUTH_TYPE_DEVICEADMIN",
                "Alice Admin",
                "alice@example.com",
                "corp\\alice",
                "corp",
                "alice"
        ));

        Principal principal = AccessAdapter.getPrincipalProperties(accessService, "com.example.app");

        verify(accessService).getCurrentPrincipal(eq("com.example.app"), requestCaptor.capture());
        assertTrue(requestCaptor.getValue().getExpression().contains("AUTH_AGENT_ID=$ENCODE(BASE64, $SECURITY_CONTEXT(AUTH_AGENT_ID)$)$"));
        assertTrue(requestCaptor.getValue().getExpression().contains("_CANARY=OK\""));

        assertTrue(principal.isAuthenticated());
        assertTrue(principal.isAdmin());
        assertFalse(principal.isGuestUser());
        assertFalse(principal.isDeviceUser());
        assertEquals("alice", principal.getUsername());
        assertEquals("alice@example.com", principal.getUserEmail());
        assertEquals("corp", principal.getDomain());
        assertEquals("agent-123", principal.getProviderUUID());
        assertEquals("Provider One", principal.getProvider());
        assertEquals("corp\\alice", principal.getFullyQualifiedName());
        assertEquals("alice", principal.getPrincipalId());
        assertEquals("Provider One", principal.getUserProperty(UserProperty.AUTH_AGENT_NAME.getValue()));
        assertEquals("agent-123", principal.getUserProperty(UserProperty.AUTH_AGENT_UUID.getValue()));
        assertEquals("alice", principal.getUserProperty(UserProperty.DISPLAY_NAME.getValue()));
        assertEquals("alice@example.com", principal.getUserProperty(UserProperty.EMAIL_ADDRESS.getValue()));
        assertEquals("alice", principal.getUserProperty(UserProperty.USER_NAME.getValue()));
        assertEquals("alice", principal.getUserProperty(UserProperty.USER_NAME_LOWER.getValue()));
        assertEquals("corp", principal.getUserProperty(UserProperty.DOMAIN.getValue()));
        assertTrue(principal.getSimpleAuthorities().containsAll(Arrays.asList(
                Principal.SimpleAuthority.ADMIN,
                Principal.SimpleAuthority.PRINT,
                Principal.SimpleAuthority.SCAN,
                Principal.SimpleAuthority.COPY,
                Principal.SimpleAuthority.JOB_CONTROL
        )));
    }

    @Test
    public void givenGuestPrincipal_whenGetPrincipalProperties_thenUsesGuestDefaults() {
        when(accessService.isSupported()).thenReturn(false);
        when(accessService.getAuthenticationAgents()).thenReturn(authenticationAgents);
        when(accessService.getCurrentPrincipal(eq("com.example.app"), any(ResolveSecurityExpressionRequest.class)))
                .thenReturn(resolvedPrincipal);
        when(resolvedPrincipal.getResult()).thenReturn(resolvedExpression(
                null,
                "AUTH_TYPE_GUEST",
                "Guest User",
                "guest@example.com",
                null,
                null,
                "guest"
        ));

        Principal principal = AccessAdapter.getPrincipalProperties(accessService, "com.example.app");

        assertFalse(principal.isAuthenticated());
        assertTrue(principal.isGuestUser());
        assertEquals("LOCAL", principal.getProvider());
        assertEquals("LOCAL\\\\guest", principal.getFullyQualifiedName());
        assertEquals("guest", principal.getPrincipalId());
        assertEquals("", principal.getProviderUUID());
        assertEquals("LOCAL", principal.getUserProperty(UserProperty.AUTH_AGENT_NAME.getValue()));
        assertEquals("", principal.getUserProperty(UserProperty.AUTH_AGENT_UUID.getValue()));
        assertEquals("guest", principal.getUserProperty(UserProperty.DISPLAY_NAME.getValue()));
        assertEquals("guest@example.com", principal.getUserProperty(UserProperty.EMAIL_ADDRESS.getValue()));
        assertEquals("guest", principal.getUserProperty(UserProperty.USER_NAME.getValue()));
        assertEquals("guest", principal.getUserProperty(UserProperty.USER_NAME_LOWER.getValue()));
        assertEquals("", principal.getUserProperty(UserProperty.DOMAIN.getValue()));
    }

    private static AuthenticationAgent authenticationAgent(String agentId, String agentName) {
        AuthenticationAgent authenticationAgent = new AuthenticationAgent();
        authenticationAgent.setAgentId(agentId);
        authenticationAgent.setAgentName(agentName);
        return authenticationAgent;
    }

    private static String resolvedExpression(
            String authAgentId,
            String authType,
            String displayName,
            String emailAddress,
            String fullyQualifiedUserName,
            String userDomain,
            String userName
    ) {
        StringBuilder builder = new StringBuilder();
        builder.append("AUTH_AGENT_ID=").append(encode(authAgentId)).append('\n');
        builder.append("AUTH_TYPE=").append(encode(authType)).append('\n');
        builder.append("DISPLAY_NAME=").append(encode(displayName)).append('\n');
        builder.append("EMAIL_ADDRESS=").append(encode(emailAddress)).append('\n');
        builder.append("FQ_USER_NAME=").append(encode(fullyQualifiedUserName)).append('\n');
        builder.append("USER_DOMAIN=").append(encode(userDomain)).append('\n');
        builder.append("USER_NAME=").append(encode(userName)).append('\n');
        return builder.toString();
    }

    private static String encode(String value) {
        if (value == null) {
            return "";
        }
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }
}
