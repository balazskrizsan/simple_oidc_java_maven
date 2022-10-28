package com.kbalazsworks.simple_oidc.services;

import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.entities.BasicAuth;
import com.kbalazsworks.simple_oidc.entities.IntrospectRawResponse;
import com.kbalazsworks.simple_oidc.entities.JwksKeys;
import com.kbalazsworks.simple_oidc.exceptions.OidcApiException;
import com.kbalazsworks.simple_oidc.exceptions.OidcExpiredTokenException;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwksVerificationException;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwtParseException;
import com.kbalazsworks.simple_oidc.exceptions.OidcScopeException;
import lombok.NonNull;

import java.util.List;

public interface IOidcService
{
    @NonNull AccessTokenRawResponse callTokenEndpoint(
        @NonNull String clientId,
        @NonNull String clientSecret,
        @NonNull String scope,
        @NonNull String grantType
    ) throws OidcApiException;

    GrantStoreService getGrantStoreService();

    @NonNull IntrospectRawResponse callIntrospectEndpoint(
        @NonNull String accessToken,
        @NonNull BasicAuth basicAuth
    ) throws OidcApiException;

    @NonNull JwksKeys callJwksEndpoint() throws OidcApiException;

    void checkScopesInToken(@NonNull String token, @NonNull List<String> scopes)
    throws OidcScopeException, OidcJwtParseException, OidcExpiredTokenException, OidcJwksVerificationException;

    void checkValidated(String token)
    throws OidcExpiredTokenException, OidcJwksVerificationException, OidcJwtParseException;

    void checkExpiredToken(@NonNull String token) throws OidcExpiredTokenException, OidcJwtParseException;

    @NonNull Boolean isExpiredToken(@NonNull String token) throws OidcJwtParseException;

    void checkJwksVerifiedToken(@NonNull String token) throws OidcJwksVerificationException;

    @NonNull Boolean isJwksVerifiedToken(@NonNull String token);

    public <T> @NonNull T callUserInfoEndpoint(String idToken, @NonNull Class<T> mapperClass) throws OidcApiException;
}
