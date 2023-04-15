package com.kbalazsworks.services;

import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.entities.IntrospectRawResponse;
import com.kbalazsworks.simple_oidc.entities.JwtData;
import com.kbalazsworks.simple_oidc.entities.JwtHeader;
import com.kbalazsworks.simple_oidc.entities.grant_type.ClientCredentials;
import com.kbalazsworks.simple_oidc.services.GrantStoreService;
import com.kbalazsworks.simple_oidc.services.OidcService;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OidcService_callTokenEndpointTest extends AbstractTest
{
    @Test
    @SneakyThrows
    public void callTokenEndpointWithValidParameters_returnsValidJwt()
    {
        // Arrange
        String       expectedAlg         = "RS256";
        String       expectedTyp         = "at+jwt";
        String       expectedClientId    = "client1_client_credentials";
        List<String> expectedScopeAsList = List.of("test_scope", "test_scope.a");
        String       expectedIss         = HOST;
        List<String> expectedAud         = List.of("test_resource_a", HOST + "/resources");
        Integer      expectedExpiresIn   = 3600;
        String       expectedTokenType   = "Bearer";
        String       expectedScope       = "test_scope test_scope.a";

        // Act
        AccessTokenRawResponse actual = requestJwtAccessTokenFromIds();

        // Assert
        JwtData   jwtData   = getTokenService().getJwtData(actual.getAccessToken());
        JwtHeader jwtHeader = getTokenService().getJwtHeader(actual.getAccessToken());

        assertAll(
            () -> assertThat(jwtHeader.getAlg()).isEqualTo(expectedAlg),
            () -> assertThat(jwtHeader.getTyp()).isEqualTo(expectedTyp),
            () -> assertThat(jwtData.getClientId()).isEqualTo(expectedClientId),
            () -> assertThat(jwtData.getScope()).isEqualTo(expectedScopeAsList),
            () -> assertThat(jwtData.getIss()).isEqualTo(expectedIss),
            () -> assertThat(jwtData.getAud()).isEqualTo(expectedAud),
            () -> assertThat(jwtData.getExp() - jwtData.getIat()).isEqualTo(expectedExpiresIn),
            () -> assertThat(actual.getExpiresIn()).isEqualTo(expectedExpiresIn),
            () -> assertThat(actual.getTokenType()).isEqualTo(expectedTokenType),
            () -> assertThat(actual.getScope()).isEqualTo(expectedScope)
        );
    }

    @Test
    @SneakyThrows
    public void callTokenEndpointWithCustomTokenExchangeGrant_returnsValidJwt()
    {
        // Arrange
        String firstTestedClientId     = "client.scope_for_token_exchange";
        String firstTestedClientSecret = "client.scope_for_token_exchange";
        String firstTestedScope        = "xc/test_token_exchange";
        String firstTestedGrantType    = "client_credentials";

        String secondTestedClientId     = "client.token_exchange";
        String secondTestedClientSecret = "client.token_exchange";
        String secondTestedScope        = "test_token_exchange";
        String secondTestedGrantType    = "token_exchange";

        String expectedScope = "test_token_exchange";
        String expectedSub = "xc/test_token_exchange";
        String expectedClient = "client.token_exchange";
        String expectedIss = HOST;
        String expectedIdp = "idp";
        boolean expectedActive = true;
        String exchangeFromActive = "xc/test_token_exchange";

        // Act
        @NonNull AccessTokenRawResponse firstActual = getOidcService().callTokenEndpoint(
            firstTestedClientId,
            firstTestedClientSecret,
            firstTestedScope,
            firstTestedGrantType
        );
        @NonNull AccessTokenRawResponse secondActual = getOidcService().callTokenEndpoint(
            secondTestedClientId,
            secondTestedClientSecret,
            secondTestedScope,
            secondTestedGrantType,
            new HashMap<>() {{
                put("token", firstActual.getAccessToken());
                put("exchange_from", exchangeFromActive);
            }}
        );
        IntrospectRawResponse introspectActual = getOidcService()
            .callIntrospectEndpoint(secondActual.getAccessToken(), INTROSPECT_BASIC_AUTH);

        // Assert
        assertAll(
            () -> assertThat(introspectActual.getScope()).isEqualTo(expectedScope),
            () -> assertThat(introspectActual.getSub()).isEqualTo(expectedSub),
            () -> assertThat(introspectActual.getClientId()).isEqualTo(expectedClient),
            () -> assertThat(introspectActual.getIss()).isEqualTo(expectedIss),
            () -> assertThat(introspectActual.getIdp()).isEqualTo(expectedIdp),
            () -> assertThat(introspectActual.getActive()).isEqualTo(expectedActive)
        );
    }

    @Test
    @SneakyThrows
    public void storeAndRequestTokenWithGrantStore_returnsValidJwt()
    {
        // Arrange
        String       expectedAlg         = "RS256";
        String       expectedTyp         = "at+jwt";
        String       expectedClientId    = "client1_client_credentials";
        List<String> expectedScopeAsList = List.of("test_scope", "test_scope.a");
        String       expectedIss         = HOST;
        List<String> expectedAud         = List.of("test_resource_a", HOST + "/resources");
        Integer      expectedExpiresIn   = 3600;
        String       expectedTokenType   = "Bearer";
        String       expectedScope       = "test_scope test_scope.a";

        OidcService oidcService = getOidcService();

        GrantStoreService grantStoreService = oidcService.getGrantStoreService();
        grantStoreService.addGrant("test1", new ClientCredentials(
            "client1_client_credentials",
            "client1_client_credentials_secret",
            List.of("test_scope", "test_scope.a")
        ));
        grantStoreService.protectStore();

        // Act
        AccessTokenRawResponse actual = oidcService.callTokenEndpoint("test1");

        // Assert
        JwtData   jwtData   = getTokenService().getJwtData(actual.getAccessToken());
        JwtHeader jwtHeader = getTokenService().getJwtHeader(actual.getAccessToken());

        assertAll(
            () -> assertThat(jwtHeader.getAlg()).isEqualTo(expectedAlg),
            () -> assertThat(jwtHeader.getTyp()).isEqualTo(expectedTyp),
            () -> assertThat(jwtData.getClientId()).isEqualTo(expectedClientId),
            () -> assertThat(jwtData.getScope()).isEqualTo(expectedScopeAsList),
            () -> assertThat(jwtData.getIss()).isEqualTo(expectedIss),
            () -> assertThat(jwtData.getAud()).isEqualTo(expectedAud),
            () -> assertThat(jwtData.getExp() - jwtData.getIat()).isEqualTo(expectedExpiresIn),
            () -> assertThat(actual.getExpiresIn()).isEqualTo(expectedExpiresIn),
            () -> assertThat(actual.getTokenType()).isEqualTo(expectedTokenType),
            () -> assertThat(actual.getScope()).isEqualTo(expectedScope)
        );
    }
}
