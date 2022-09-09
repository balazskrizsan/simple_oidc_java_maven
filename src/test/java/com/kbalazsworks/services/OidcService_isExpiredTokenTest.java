package com.kbalazsworks.services;

import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.exceptions.OidcExpiredTokenException;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwtParseException;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OidcService_isExpiredTokenTest extends AbstractTest
{
    @Test
    @SneakyThrows
    public void invalidStringAsToken_throwsException()
    {
        // Arrange
        String testedToken = "qweasd";

        Class<OidcJwtParseException> expectedException = OidcJwtParseException.class;
        String                       expectedMessage   = "JWT Data parse error";

        // Act - Assert
        assertThatThrownBy(() -> getOidcService().isExpiredToken(testedToken))
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
        boolean actual = getOidcService().isExpiredToken(testedToken);

        assertThat(actual).isEqualTo(expectedResponse);
    }

    @Test
    @SneakyThrows
    public void freshNewToken_returnsFalse()
    {
        // Arrange
        AccessTokenRawResponse testedToken = requestTokenFromIds();

        boolean expectedResponse = false;

        // Act
        boolean actual = getOidcService().isExpiredToken(testedToken.getAccessToken());

        // Assert
        assertThat(actual).isEqualTo(expectedResponse);
    }
}
