package com.kbalazsworks.simple_oidc.services;

import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.entities.BasicAuth;
import com.kbalazsworks.simple_oidc.entities.IntrospectRawResponse;
import com.kbalazsworks.simple_oidc.entities.JwksKeys;
import com.kbalazsworks.simple_oidc.exceptions.OidcException;
import com.kbalazsworks.simple_oidc.exceptions.OidcExpiredTokenException;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwksVerificationException;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwtParseException;

import java.util.List;

public interface IOidcService
{
    AccessTokenRawResponse callTokenEndpoint(String clientId, String clientSecret, String scope, String grantType);

    IntrospectRawResponse callIntrospectEndpoint(String accessToken, BasicAuth basicAuth);

    JwksKeys callJwksEndpoint();

    void checkScopesInToken(String token, List<String> scopes) throws OidcException;

    void checkValidated(String token)
    throws OidcExpiredTokenException, OidcJwksVerificationException, OidcJwtParseException;

    void checkExpiredToken(String token) throws OidcExpiredTokenException, OidcJwtParseException;

    Boolean isJwksVerifiedToken(String token);

    void checkJwksVerifiedToken(String token) throws OidcJwksVerificationException;
}
