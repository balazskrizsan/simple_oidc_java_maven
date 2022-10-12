package com.kbalazsworks.services;

import com.kbalazsworks.simple_oidc.entities.JwksKeyItem;
import com.kbalazsworks.simple_oidc.entities.JwksKeys;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OidcService_callJwksEndpointTest extends AbstractTest
{
    @Test
    @SneakyThrows
    public void availableEndpointCallTest_returnsExpectedJwksObjects()
    {
        // Arrange
        JwksKeyItem jwksKeyItem = getJwksKeyItem();

        // Act
        JwksKeys actual = getOidcService().callJwksEndpoint();

        // Assert
        assertAll(
            () -> assertThat(actual.getKeys().size()).isEqualTo(1),
            () -> assertThat(actual.getKeys().get(0)).usingRecursiveComparison().isEqualTo(jwksKeyItem)
        );
    }
}
