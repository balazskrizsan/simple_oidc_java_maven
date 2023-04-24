package com.kbalazsworks.services;

import com.google.inject.Inject;
import com.kbalazsworks.simple_oidc.entities.grant_type.ClientCredentials;
import com.kbalazsworks.simple_oidc.services.GrantStoreService;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GrantStoreService_protectStoreTest extends AbstractTest
{
    @Inject
    GrantStoreService grantStoreService;

    @Test
    @SneakyThrows
    public void addItemToProtectedStore_throwsException()
    {
        // Arrange
        grantStoreService.protectStore();
        ClientCredentials testedGrant    = new ClientCredentials("a", "b", List.of("c"));
        String            testedGrantKey = "test123";

        // Act / Assert
        assertThatThrownBy(
            () -> grantStoreService.addGrant(testedGrantKey, testedGrant)
        ).isInstanceOf(UnsupportedOperationException.class);
    }
}
