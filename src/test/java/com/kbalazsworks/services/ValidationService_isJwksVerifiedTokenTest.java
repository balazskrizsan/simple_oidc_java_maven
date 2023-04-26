package com.kbalazsworks.services;

import com.google.inject.Inject;
import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.services.IValidationService;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidationService_isJwksVerifiedTokenTest extends AbstractTest
{
    @Inject
    IValidationService validationService;

    @Test
    @SneakyThrows
    public void freshNewToken_doesNothing()
    {
        // Arrange
        AccessTokenRawResponse testedToken = requestJwtAccessTokenFromIds();

        boolean expectedResponse = true;

        // Act
        boolean actual = validationService.isJwksVerifiedToken(testedToken.getAccessToken());

        // Assert
        assertThat(actual).isEqualTo(expectedResponse);
    }
}
