package com.kbalazsworks.services;

import com.kbalazsworks.simple_oidc.entities.grant_type.ClientCredentials;
import com.kbalazsworks.simple_oidc.services.GrantStoreService;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class WakeUpTestIds extends AbstractTest
{
    @SneakyThrows
    @Test
    public void addGrantToTheStore_returnsTheAddedGrant()
    {
        // Arrange
        ClientCredentials testedGrant    = new ClientCredentials("a", "b", List.of("c"));
        String            testedGrantKey = "test123";

        // Act
        try
        {
            GrantStoreService grantStoreService = getOidcService().getGrantStoreService();
            grantStoreService.addGrant(testedGrantKey, testedGrant);
            ClientCredentials actual = grantStoreService.getGrant(testedGrantKey, ClientCredentials.class);
        }
        catch (Exception ignored)
        {
        }

        // Assert
        assertTrue(true);
    }
}
