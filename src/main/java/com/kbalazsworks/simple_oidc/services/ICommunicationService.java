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
import java.util.Map;

public interface ICommunicationService
{
    @NonNull AccessTokenRawResponse callTokenEndpoint(@NonNull String key)
    throws OidcApiException;

    @NonNull AccessTokenRawResponse callTokenEndpoint(@NonNull String key, @NonNull Map<String, String> extraParams)
    throws OidcApiException;

    @NonNull AccessTokenRawResponse callTokenEndpoint(
        @NonNull String clientId,
        @NonNull String clientSecret,
        @NonNull String scope,
        @NonNull String grantType
    ) throws OidcApiException;

    @NonNull AccessTokenRawResponse callTokenEndpoint(
        @NonNull String clientId,
        @NonNull String clientSecret,
        @NonNull String scope,
        @NonNull String grantType,
        @NonNull Map<String, String> extraParams
    ) throws OidcApiException;

    @NonNull IntrospectRawResponse callIntrospectEndpoint(@NonNull String accessToken, @NonNull BasicAuth basicAuth)
    throws OidcApiException;

    @NonNull JwksKeys callJwksEndpoint() throws OidcApiException;

    <T> @NonNull T callUserInfoEndpoint(String idToken, @NonNull Class<T> mapperClass) throws OidcApiException;
}
