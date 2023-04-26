package com.kbalazsworks.services;

import com.google.inject.Inject;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwtParseException;
import com.kbalazsworks.simple_oidc.services.IJwtValidationService;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import okio.ByteString;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JwtValidationService_getSignatureTest extends AbstractTest
{
    @Inject
    IJwtValidationService jwtValidationService;

    @Test
    @SneakyThrows
    public void validToken_returnsExpectedSignature()
    {
        // Arrange
        String testedToken           = getValidExpiredToken();
        String expectedSignatureHash = "[hex=760a7db56407fed85b79fb140b9cc31f565b743c6ee3c902b9827682caced215]";

        // Act
        byte[] actual = jwtValidationService.getSignature(testedToken);

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
        assertThatThrownBy(() -> jwtValidationService.getSignature(testedToken))
            .isInstanceOf(expectedException)
            .hasMessage(expectedErrorMessage);
    }
}
