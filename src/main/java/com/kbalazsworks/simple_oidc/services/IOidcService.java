package com.kbalazsworks.simple_oidc.services;

import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.entities.BasicAuth;
import com.kbalazsworks.simple_oidc.entities.IntrospectRawResponse;

public interface IOidcService
{
    AccessTokenRawResponse callTokenEndpoint(String clientId, String clientSecret, String scope, String grantType);
    IntrospectRawResponse callIntrospectEndpoint(String accessToken, BasicAuth basicAuth);
}
