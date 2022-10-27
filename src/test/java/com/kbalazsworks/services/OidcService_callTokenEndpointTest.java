package com.kbalazsworks.services;

import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.entities.JwtData;
import com.kbalazsworks.simple_oidc.entities.JwtHeader;
import com.kbalazsworks.simple_oidc.entities.grant_type.ClientCredentials;
import com.kbalazsworks.simple_oidc.enums.GrantTypesEnum;
import com.kbalazsworks.simple_oidc.services.GrantStoreService;
import com.kbalazsworks.simple_oidc.services.OidcService;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

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
        String       expectedIss         = "https://localhost:5001";
        List<String> expectedAud         = List.of("test_resource_a", "https://localhost:5001/resources");
        Integer      expectedExpiresIn   = 3600;
        String       expectedTokenType   = "Bearer";
        String       expectedScope       = "test_scope test_scope.a";

        // Act
        AccessTokenRawResponse actual = LIVE_TOKEN;

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
    public void storeAndRequestTokenWithGrantStore_returnsValidJwt()
    {
        // Arrange
        String       expectedAlg         = "RS256";
        String       expectedTyp         = "at+jwt";
        String       expectedClientId    = "client1_client_credentials";
        List<String> expectedScopeAsList = List.of("test_scope", "test_scope.a");
        String       expectedIss         = "https://localhost:5001";
        List<String> expectedAud         = List.of("test_resource_a", "https://localhost:5001/resources");
        Integer      expectedExpiresIn   = 3600;
        String       expectedTokenType   = "Bearer";
        String       expectedScope       = "test_scope test_scope.a";

        OidcService oidcService = getOidcService();

        GrantStoreService grantStoreService = oidcService.getGrantStore();
        grantStoreService.addGrant(
            GrantTypesEnum.ClientCredentials,
            "test1",
            new ClientCredentials(
                "client1_client_credentials",
                "client1_client_credentials_secret",
                List.of("test_scope", "test_scope.a")
            )
        );
        grantStoreService.setGrantsToImmutable();

        // Act
        AccessTokenRawResponse actual = oidcService.callTokenEndpoint(GrantTypesEnum.ClientCredentials, "test1");

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
