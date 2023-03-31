package com.kbalazsworks.services;

import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.entities.BasicAuth;
import com.kbalazsworks.simple_oidc.entities.IntrospectRawResponse;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OidcService_callIntrospectionEndpointTest extends AbstractTest
{
    @Test
    @SneakyThrows
    public void callTokenEndpointWithValidJwtAccessToken_returnsValidJwt()
    {
        // Arrange
        AccessTokenRawResponse testedToken     = requestJwtAccessTokenFromIds();

        Boolean      expectedActive    = true;
        String       expectedIss       = HOST;
        Integer      expectedExpiresIn = 3600;
        List<String> expectedAud       = List.of("test_resource_a", HOST + "/resources");
        String       expectedClientId  = "client1_client_credentials";
        String       expectedScope     = "test_scope test_scope.a";

        // Act
        IntrospectRawResponse actual = getOidcService()
            .callIntrospectEndpoint(testedToken.getAccessToken(), INTROSPECT_BASIC_AUTH);

        // Assert
        assertAll(
            () -> assertThat(actual.getActive()).isEqualTo(expectedActive),
            () -> assertThat(actual.getIss()).isEqualTo(expectedIss),
            () -> assertThat(actual.getExp() - actual.getIat()).isEqualTo(expectedExpiresIn),
            () -> assertThat(actual.getAud()).isEqualTo(expectedAud),
            () -> assertThat(actual.getClientId()).isEqualTo(expectedClientId),
            () -> assertThat(actual.getScope()).isEqualTo(expectedScope)
        );
    }
}
