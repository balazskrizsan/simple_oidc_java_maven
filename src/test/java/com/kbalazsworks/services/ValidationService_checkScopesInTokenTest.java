package com.kbalazsworks.services;

import com.google.inject.Inject;
import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.exceptions.OidcScopeException;
import com.kbalazsworks.simple_oidc.services.IValidationService;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationService_checkScopesInTokenTest extends AbstractTest
{
    @Inject
    IValidationService validationService;

    @Test
    @SneakyThrows
    public void availableScopeInTokenCheck_doesNothing()
    {
        // Arrange
        AccessTokenRawResponse testedToken = requestJwtAccessTokenFromIds();

        // Act
        validationService.checkScopesInToken(testedToken.getAccessToken(), List.of("test_scope.a"));

        // Assert
        assertTrue(true);
    }

    @Test
    @SneakyThrows
    public void notAvailableScopeInTokenCheck_doesNothing()
    {
        // Arrange
        AccessTokenRawResponse testedToken = requestJwtAccessTokenFromIds();

        Class<OidcScopeException> exceptedException        = OidcScopeException.class;
        String                    expectedExceptionMessage = "No scope found in token";

        // Act - Assert
        assertThatThrownBy(() -> validationService.checkScopesInToken(testedToken.getAccessToken(), List.of("qweasd")))
            .isInstanceOf(exceptedException)
            .hasMessage(expectedExceptionMessage);
    }
}
