package com.kbalazsworks.services;

import com.google.inject.Inject;
import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.services.IValidationService;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationService_checkJwksVerifiedTokenTest extends AbstractTest
{
    @Inject
    IValidationService validationService;

    @Test
    @SneakyThrows
    public void freshNewToken_doesNothing()
    {
        // Arrange
        AccessTokenRawResponse testedToken = requestJwtAccessTokenFromIds();

        // Act
        validationService.checkJwksVerifiedToken(testedToken.getAccessToken());

        // Assert
        assertTrue(true);
    }
}
