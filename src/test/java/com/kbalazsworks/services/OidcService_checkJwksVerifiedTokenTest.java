package com.kbalazsworks.services;

import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OidcService_checkJwksVerifiedTokenTest extends AbstractTest
{
    @Test
    @SneakyThrows
    public void freshNewToken_doesNothing()
    {
        // Arrange
        AccessTokenRawResponse testedToken = requestJwtAccessTokenFromIds();

        // Act
        getOidcService().checkJwksVerifiedToken(testedToken.getAccessToken());

        // Assert
        assertTrue(true);
    }
}
