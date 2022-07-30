package com.kbalazsworks.services;

import com.kbalazsworks.simple_oidc.exceptions.OidcJwtParseException;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import okio.ByteString;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TokenService_getSignatureTest extends AbstractTest
{
    @Test
    @SneakyThrows
    public void validToken_returnsExpectedSignature()
    {
        // Arrange
        String testedToken           = getValidExpiredToken();
        String expectedSignatureHash = "[hex=3e2147b2c6d9c6b7963eaa587b75371820da1519a6839bf086f77a18947d7ab1]";

        // Act
        byte[] actual = getTokenService().getSignature(testedToken);

        // Assert
        assertThat(ByteString.of(actual).sha256().toString()).isEqualTo(expectedSignatureHash);
    }

    @Test
    @SneakyThrows
    public void invalidToken_willThrownException()
    {
        // Arrange
        String                       testedToken          = getInvalidToken();
        Class<OidcJwtParseException> expectedException    = OidcJwtParseException.class;
        String                       expectedErrorMessage = "Signature parse error";

        // Act / Assert
        assertThatThrownBy(() -> getTokenService().getSignature(testedToken))
            .isInstanceOf(expectedException)
            .hasMessage(expectedErrorMessage);
    }
}
