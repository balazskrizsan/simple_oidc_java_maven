package com.kbalazsworks.simple_oidc.services;

import com.google.inject.Inject;
import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.entities.BasicAuth;
import com.kbalazsworks.simple_oidc.entities.IntrospectRawResponse;
import com.kbalazsworks.simple_oidc.entities.JwksKeys;
import com.kbalazsworks.simple_oidc.entities.grant_type.IGrantType;
import com.kbalazsworks.simple_oidc.exceptions.OidcApiException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class CommunicationService implements ICommunicationService
{
    private final ResponseValidatorService responseValidatorService;
    private final GrantStoreService        grantStoreService;
    private final HttpClientService        httpClientService;

    public @NonNull AccessTokenRawResponse callTokenEndpoint(@NonNull String key) throws OidcApiException
    {
        log.info("Call token endpoint with key: {}", key);
        IGrantType clientCredential = grantStoreService.getGrant(key);

        return callTokenEndpoint(
            clientCredential.getClientId(),
            clientCredential.getClientSecret(),
            clientCredential.getScopeAsString(),
            clientCredential.getGrantType(),
            new HashMap<>()
        );
    }

    // @todo: test
    @Override public @NonNull AccessTokenRawResponse callTokenEndpoint(
        @NonNull String key, @NonNull Map<String, String> extraParams
    ) throws OidcApiException
    {
        log.info("Call token endpoint with key: {}", key);
        IGrantType clientCredential = grantStoreService.getGrant(key);

        return callTokenEndpoint(
            clientCredential.getClientId(),
            clientCredential.getClientSecret(),
            clientCredential.getScopeAsString(),
            clientCredential.getGrantType(),
            new HashMap<>()
        );
    }

    public @NonNull AccessTokenRawResponse callTokenEndpoint(
        @NonNull String clientId, @NonNull String clientSecret, @NonNull String scope, @NonNull String grantType
    ) throws OidcApiException
    {
        return callTokenEndpoint(clientId, clientSecret, scope, grantType, new HashMap<>());
    }

    public @NonNull AccessTokenRawResponse callTokenEndpoint(
        @NonNull String clientId,
        @NonNull String clientSecret,
        @NonNull String scope,
        @NonNull String grantType,
        @NonNull Map<String, String> extraParams
    ) throws OidcApiException
    {
        log.info("Call token endpoint with ClientId: {}", clientId);

        Map<String, String> params = new HashMap<>()
        {{
            put("client_id", clientId);
            put("client_secret", clientSecret);
            put("scope", scope);
            put("grant_type", grantType);
        }};
        params.putAll(extraParams);

        return responseValidatorService.tokenEndpointValidator(httpClientService.getOidcHttpClientService().post(
            httpClientService.getOidcConfig().getTokenEndpoint(),
            params,
            AccessTokenRawResponse.class
        ));
    }

    public @NonNull IntrospectRawResponse callIntrospectEndpoint(
        @NonNull String accessToken, @NonNull BasicAuth basicAuth
    ) throws OidcApiException
    {
        log.info("Call introspection endpoint");

        return responseValidatorService.introspectEndpointValidator(httpClientService
            .getOidcHttpClientService()
            .post(
                httpClientService.getOidcConfig().getIntrospectionEndpoint(),
                Map.of("token", accessToken),
                IntrospectRawResponse.class,
                basicAuth
            ));
    }

    public @NonNull JwksKeys callJwksEndpoint() throws OidcApiException
    {
        log.info("Call JWKS endpoint");

        return responseValidatorService.jwksEndpointValidator(httpClientService.getOidcHttpClientService().get(
            httpClientService.getOidcConfig().getJwksUri(),
            JwksKeys.class
        ));
    }

    // @todo: test
    public <T> @NonNull T callUserInfoEndpoint(String idToken, @NonNull Class<T> mapperClass) throws OidcApiException
    {
        log.info("Call User Info endpoint");

        return httpClientService.getOidcHttpClientService().get(
            httpClientService.getOidcConfig().getUserinfoEndpoint(),
            new HashMap<>(),
            Map.of("Authorization", "Bearer " + idToken),
            mapperClass
        );
    }
}
