package com.hp.jetadvantage.link.services.accesslet.adapter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PrincipalContextDataUnitTest {

    @Test
    public void givenNullExpression_whenFromResolvedExpression_thenAllFieldsAreNull() {
        PrincipalContextData contextData = PrincipalContextData.fromResolvedExpression(null);

        assertNull(contextData.getAuthAgentId());
        assertNull(contextData.getAuthType());
        assertNull(contextData.getEmailAddress());
        assertNull(contextData.getFullyQualifiedUserName());
        assertNull(contextData.getUserDomain());
        assertNull(contextData.getUserName());
    }

    @Test
    public void givenBlankExpression_whenFromResolvedExpression_thenAllFieldsAreNull() {
        PrincipalContextData contextData = PrincipalContextData.fromResolvedExpression("   ");

        assertNull(contextData.getAuthAgentId());
        assertNull(contextData.getAuthType());
        assertNull(contextData.getEmailAddress());
        assertNull(contextData.getFullyQualifiedUserName());
        assertNull(contextData.getUserDomain());
        assertNull(contextData.getUserName());
    }

    @Test
    public void givenSupportedBase64Fields_whenFromResolvedExpression_thenAllSupportedFieldsAreDecodedAndMapped() {
        String expression = "AUTH_AGENT_ID=YWdlbnQtaWQ=\n"
                + "AUTH_TYPE=YmVhcmVy\n"
                + "EMAIL_ADDRESS=dXNlckBleGFtcGxlLmNvbQ==\n"
                + "FQ_USER_NAME=ZG9tYWluXHVzZXI=\n"
                + "USER_DOMAIN=ZG9tYWlu\n"
                + "USER_NAME=dXNlcg==\n";

        PrincipalContextData contextData = PrincipalContextData.fromResolvedExpression(expression);

        assertEquals("agent-id", contextData.getAuthAgentId());
        assertEquals("bearer", contextData.getAuthType());
        assertEquals("user@example.com", contextData.getEmailAddress());
        assertEquals("domain\\user", contextData.getFullyQualifiedUserName());
        assertEquals("domain", contextData.getUserDomain());
        assertEquals("user", contextData.getUserName());
    }

    @Test
    public void givenUnsupportedAndMalformedLines_whenFromResolvedExpression_thenTheyAreIgnored() {
        String expression = "UNKNOWN_KEY=YWJj\n"
                + "DISPLAY_NAME=Sm9obiBEb2U=\n"
                + "MALFORMED_LINE\n"
                + "USER_NAME=dXNlcg==\n";

        PrincipalContextData contextData = PrincipalContextData.fromResolvedExpression(expression);

        assertNull(contextData.getAuthAgentId());
        assertNull(contextData.getAuthType());
        assertNull(contextData.getEmailAddress());
        assertNull(contextData.getFullyQualifiedUserName());
        assertNull(contextData.getUserDomain());
        assertEquals("user", contextData.getUserName());
    }

    @Test
    public void givenInvalidBase64ForSupportedField_whenFromResolvedExpression_thenRawValueIsKept() {
        String expression = "USER_NAME=not-valid-base64@@@\n";

        PrincipalContextData contextData = PrincipalContextData.fromResolvedExpression(expression);

        assertEquals("not-valid-base64@@@", contextData.getUserName());
    }

    @Test
    public void givenDuplicateSupportedKey_whenFromResolvedExpression_thenLastValueWins() {
        String expression = "USER_NAME=Zmlyc3Q=\n"
                + "USER_NAME=c2Vjb25k\n";

        PrincipalContextData contextData = PrincipalContextData.fromResolvedExpression(expression);

        assertEquals("second", contextData.getUserName());
    }
}
