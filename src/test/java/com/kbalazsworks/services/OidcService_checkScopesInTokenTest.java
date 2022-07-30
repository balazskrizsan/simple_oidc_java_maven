package com.kbalazsworks.services;

import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.exceptions.OidcScopeException;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OidcService_checkScopesInTokenTest extends AbstractTest
{
    @Test
    @SneakyThrows
    public void availableScopeInTokenCheck_doesNothing()
    {
        // Arrange
        AccessTokenRawResponse testedToken = requestTokenFromIds();

        // Act
        getOidcService().checkScopesInToken(testedToken.getAccessToken(), List.of("sj"));

        // Assert
        assertTrue(true);
    }

    @Test
    @SneakyThrows
    public void notAvailableScopeInTokenCheck_doesNothing()
    {
        // Arrange
        AccessTokenRawResponse testedToken = requestTokenFromIds();

        Class<OidcScopeException> exceptedException        = OidcScopeException.class;
        String                    expectedExceptionMessage = "No scope found in token";

        // Act - Assert
        assertThatThrownBy(() -> getOidcService().checkScopesInToken(testedToken.getAccessToken(), List.of("qweasd")))
            .isInstanceOf(exceptedException)
            .hasMessage(expectedExceptionMessage);
    }
}
