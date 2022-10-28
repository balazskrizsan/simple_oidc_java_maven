package com.kbalazsworks.services;

import com.kbalazsworks.simple_oidc.entities.grant_type.ClientCredentials;
import com.kbalazsworks.simple_oidc.enums.GrantTypesEnum;
import com.kbalazsworks.simple_oidc.services.GrantStoreService;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GrantStoreService_addGrant_getGrantTest extends AbstractTest
{
    @SneakyThrows
    @Test
    public void addGrantToTheStore_returnsTheAddedGrant()
    {
        // Arrange
        ClientCredentials testedGrant    = new ClientCredentials("a", "b", List.of("c"));
        String            testedGrantKey = "test123";
        ClientCredentials expectedGrant  = new ClientCredentials("a", "b", List.of("c"));

        // Act
        GrantStoreService grantStoreService = getOidcService().getGrantStoreService();
        grantStoreService.addGrant(GrantTypesEnum.ClientCredentials, testedGrantKey, testedGrant);
        ClientCredentials actual = grantStoreService.getGrant(GrantTypesEnum.ClientCredentials, testedGrantKey);

        // Assert
        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedGrant);
    }
}
