package com.kbalazsworks.services;

import com.google.inject.Inject;
import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.entities.IntrospectRawResponse;
import com.kbalazsworks.simple_oidc.services.ICommunicationService;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CommunicationService_callIntrospectionEndpointTest extends AbstractTest
{
    @Inject
    ICommunicationService communicationService;

    @Test
    @SneakyThrows
    public void callTokenEndpointWithValidJwtAccessToken_returnsValidJwt()
    {
        // Arrange
        AccessTokenRawResponse testedToken = requestJwtAccessTokenFromIds();

        Boolean      expectedActive    = true;
        String       expectedIss       = "e2e.test";
        Integer      expectedExpiresIn = 3600;
        List<String> expectedAud       = List.of("test_resource_a", "e2e.test/resources");
        String       expectedClientId  = "client1_client_credentials";
        String       expectedScope     = "test_scope test_scope.a";

        // Act
        IntrospectRawResponse actual = communicationService
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
