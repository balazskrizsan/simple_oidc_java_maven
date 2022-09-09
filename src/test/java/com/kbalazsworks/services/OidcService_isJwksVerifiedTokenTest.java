package com.kbalazsworks.services;

import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OidcService_isJwksVerifiedTokenTest extends AbstractTest
{
    @Test
    @SneakyThrows
    public void freshNewToken_doesNothing()
    {
        // Arrange
        AccessTokenRawResponse testedToken = requestTokenFromIds();

        boolean expectedReponse = true;

        // Act
        boolean actual = getOidcService().isJwksVerifiedToken(testedToken.getAccessToken());

        // Assert
        assertThat(actual).isEqualTo(expectedReponse);
    }
}
