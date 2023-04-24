package com.kbalazsworks.services;

import com.google.inject.Inject;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwtParseException;
import com.kbalazsworks.simple_oidc.services.JwtValidationService;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import okio.ByteString;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JwtValidationService_getSigmedDataTest extends AbstractTest
{
    @Inject
    JwtValidationService jwtValidationService;

    @Test
    @SneakyThrows
    public void validToken_returnsExpectedSigedData()
    {
        // Arrange
        String testedToken           = getValidExpiredToken();
        String expectedSignatureHash = "[hex=29a232cc7d06af87bba8678996ea31208bb34a461e5f78c63c097ea4f07cbab5]";

        // Act
        byte[] actual = jwtValidationService.getSignedData(testedToken);

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
        String                       expectedErrorMessage = "Signed data parse error";

        // Act / Assert
        assertThatThrownBy(() -> jwtValidationService.getSignedData(testedToken))
            .isInstanceOf(expectedException)
            .hasMessage(expectedErrorMessage);
    }
}
