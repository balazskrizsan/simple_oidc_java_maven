package com.kbalazsworks.services;

import com.kbalazsworks.simple_oidc.entities.grant_type.ClientCredentials;
import com.kbalazsworks.simple_oidc.entities.grant_type.IGrantType;
import com.kbalazsworks.simple_oidc.entities.grant_type.TokenExchange;
import com.kbalazsworks.simple_oidc.services.GrantStoreService;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class GrantStoreService_addGrant_getGrantTest extends AbstractTest
{
    @SneakyThrows
    @Test
    public void addGrantToTheStore_returnsTheAddedGrant()
    {
        // Arrange
        IGrantType testedGrant1    = new ClientCredentials("a", "b", List.of("c"));
        String     testedGrantKey1 = "test1";
        IGrantType testedGrant2    = new TokenExchange("q", "w", List.of("e"));
        String     testedGrantKey2 = "test2";
        IGrantType expectedGrant1  = new ClientCredentials("a", "b", List.of("c"));
        IGrantType expectedGrant2  = new TokenExchange("q", "w", List.of("e"));

        // Act
        GrantStoreService grantStoreService = getOidcService().getGrantStoreService();
        grantStoreService.addGrant(testedGrantKey1, testedGrant1);
        grantStoreService.addGrant(testedGrantKey2, testedGrant2);
        IGrantType actual1 = grantStoreService.getGrant(testedGrantKey1);
        IGrantType actual2 = grantStoreService.getGrant(testedGrantKey2);

        // Assert
        assertAll(
            () -> assertThat(actual1).usingRecursiveComparison().isEqualTo(expectedGrant1),
            () -> assertThat(actual1.getGrantType()).isEqualTo(expectedGrant1.getGrantType()),
            () -> assertThat(actual1.getScopeAsString()).isEqualTo(expectedGrant1.getScopeAsString()),
            () -> assertThat(actual2).usingRecursiveComparison().isEqualTo(expectedGrant2),
            () -> assertThat(actual2.getGrantType()).isEqualTo(expectedGrant2.getGrantType()),
            () -> assertThat(actual2.getScopeAsString()).isEqualTo(expectedGrant2.getScopeAsString())
        );
    }
}
