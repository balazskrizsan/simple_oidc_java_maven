package com.kbalazsworks.simple_oidc.services;

import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.entities.IntrospectRawResponse;
import com.kbalazsworks.simple_oidc.entities.JwksKeys;
import com.kbalazsworks.simple_oidc.exceptions.OidcApiException;

public interface IResponseValidatorService
{
    AccessTokenRawResponse tokenEndpointValidator(AccessTokenRawResponse response) throws OidcApiException;

    IntrospectRawResponse introspectEndpointValidator(IntrospectRawResponse response)
    throws OidcApiException;

    JwksKeys jwksEndpointValidator(JwksKeys response) throws OidcApiException;
}
