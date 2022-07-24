package com.kbalazsworks.simple_oidc.services;

import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.entities.BasicAuth;
import com.kbalazsworks.simple_oidc.entities.IntrospectRawResponse;
import com.kbalazsworks.simple_oidc.exceptions.OidcApiException;

public interface IOidcService
{
    AccessTokenRawResponse callTokenEndpoint(String clientId, String clientSecret, String scope, String grantType)
    throws OidcApiException;

    IntrospectRawResponse callIntrospectEndpoint(String accessToken, BasicAuth basicAuth) throws OidcApiException;
}
