package com.hp.jetadvantage.link.device.services.interfaces;

import com.hp.ext.service.security.ResolveSecurityExpressionRequest;
import com.hp.ext.service.security.SecurityAgent_ResolveSecurityExpression;
import com.hp.ws.cdm.security.AuthenticationAgents;

public interface IDeviceAccessService {
    boolean isSupported();

    SecurityAgent_ResolveSecurityExpression getCurrentPrincipal(String packName, ResolveSecurityExpressionRequest request);

    AuthenticationAgents getAuthenticationAgents();
}
