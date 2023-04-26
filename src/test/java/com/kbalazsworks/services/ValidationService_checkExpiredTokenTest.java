package com.kbalazsworks.services;

import com.google.inject.Inject;
import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.exceptions.OidcExpiredTokenException;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwtParseException;
import com.kbalazsworks.simple_oidc.services.IValidationService;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationService_checkExpiredTokenTest extends AbstractTest
{
    @Inject
    private IValidationService validationService;

    @Test
    @SneakyThrows
    public void invalidStringAsToken_throwsException()
    {
        // Arrange
        String testedToken = "qweasd";

        Class<OidcJwtParseException> expectedException = OidcJwtParseException.class;
        String                       expectedMessage   = "JWT Data parse error";

        // Act - Assert
        assertThatThrownBy(() -> validationService.checkExpiredToken(testedToken))
            .isInstanceOf(expectedException)
            .hasMessage(expectedMessage);
    }

    @Test
    @SneakyThrows
    public void expiredToken_throwsException()
    {
        // Arrange
        String testedToken = getValidExpiredToken();

        Class<OidcExpiredTokenException> expectedException = OidcExpiredTokenException.class;
        String                           expectedMessage   = "Expired token";

        // Act - Assert
        assertThatThrownBy(() -> validationService.checkExpiredToken(testedToken))
            .isInstanceOf(expectedException)
            .hasMessage(expectedMessage);
    }

    @Test
    @SneakyThrows
    public void freshNewToken_doesNothing()
    {
        // Arrange
        AccessTokenRawResponse testedToken = requestJwtAccessTokenFromIds();

        // Act
        validationService.checkExpiredToken(testedToken.getAccessToken());

        // Assert
        assertTrue(true);
    }
}
