package com.kbalazsworks.services;

import com.google.inject.Inject;
import com.kbalazsworks.simple_oidc.entities.JwtData;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwtParseException;
import com.kbalazsworks.simple_oidc.services.IJwtValidationService;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JwtTokenValidationService_getJwtDataTest extends AbstractTest
{
    @Inject
    IJwtValidationService jwtValidationService;

    @Test
    @SneakyThrows
    public void validToken_willReturnTokenObject()
    {
        // Arrange
        String testedToken = getValidExpiredToken();

        JwtData expectedData = new JwtData(
            "https://localhost:5001",
            1659130467,
            1659130467,
            1659134067,
            List.of("sj_aws", "https://localhost:5001/resources"),
            List.of("sj", "sj.aws.ec2.upload_company_logo", "sj.aws.ses.send_mail"),
            "sj.aws",
            "415F884B0FD8FAF27838475D251D8E95"
        );

        // Act
        JwtData actual = jwtValidationService.getJwtData(testedToken);

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
        String                       expectedErrorMessage = "JWT Data parse error";

        // Act / Assert
        assertThatThrownBy(() -> jwtValidationService.getJwtData(testedToken))
            .isInstanceOf(expectedException)
            .hasMessage(expectedErrorMessage);
    }
}
