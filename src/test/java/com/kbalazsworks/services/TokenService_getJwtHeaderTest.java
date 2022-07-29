package com.kbalazsworks.services;

import com.kbalazsworks.simple_oidc.entities.JwtHeader;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwtParseException;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TokenService_getJwtHeaderTest extends AbstractTest
{
    @Test
    @SneakyThrows
    public void validToken_willReturnHeaderObject()
    {
        // Arrange
        String testedToken = getValidExpiredToken();
        JwtHeader expectedData = new JwtHeader(
            "RS256",
            "4B9CC612EBE61D72BE1B2CEE8DDAE495",
            "at+jwt"
        );

        // Act
        JwtHeader actual = getTokenService().getJwtHeader(testedToken);

        // Assert
        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedData);
    }

    @Test
    @SneakyThrows
    public void invalidToken_willThrownException()
    {
        // Arrange
        String                       testedToken          = getInvalidToken();
        Class<OidcJwtParseException> expectedException    = OidcJwtParseException.class;
        String                       expectedErrorMessage = "JWT Header parse error";

        // Act / Assert
        assertThatThrownBy(() -> getTokenService().getJwtHeader(testedToken))
            .isInstanceOf(expectedException)
            .hasMessage(expectedErrorMessage);
    }
}
