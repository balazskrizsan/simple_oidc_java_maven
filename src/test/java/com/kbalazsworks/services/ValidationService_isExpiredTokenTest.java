package com.kbalazsworks.services;

import com.google.inject.Inject;
import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwtParseException;
import com.kbalazsworks.simple_oidc.services.ValidationService;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ValidationService_isExpiredTokenTest extends AbstractTest
{
    @Inject
    ValidationService validationService;

    @Test
    @SneakyThrows
    public void invalidStringAsToken_throwsException()
    {
        // Arrange
        String testedToken = "qweasd";

        Class<OidcJwtParseException> expectedException = OidcJwtParseException.class;
        String                       expectedMessage   = "JWT Data parse error";

        // Act - Assert
        assertThatThrownBy(() -> validationService.isExpiredToken(testedToken))
            .isInstanceOf(expectedException)
            .hasMessage(expectedMessage);
    }

    @Test
    @SneakyThrows
    public void expiredToken_returnsTrue()
    {
        // Arrange
        String testedToken = getValidExpiredToken();

        boolean expectedResponse = true;

        // Act - Assert
        boolean actual = validationService.isExpiredToken(testedToken);

        assertThat(actual).isEqualTo(expectedResponse);
    }

    @Test
    @SneakyThrows
    public void freshNewToken_returnsFalse()
    {
        // Arrange
        AccessTokenRawResponse testedToken = requestJwtAccessTokenFromIds();

        boolean expectedResponse = false;

        // Act
        boolean actual = validationService.isExpiredToken(testedToken.getAccessToken());

        // Assert
        assertThat(actual).isEqualTo(expectedResponse);
    }
}
