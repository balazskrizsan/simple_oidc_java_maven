package com.kbalazsworks.services;

import com.google.inject.Inject;
import com.kbalazsworks.simple_oidc.entities.grant_type.ClientCredentials;
import com.kbalazsworks.simple_oidc.services.GrantStoreService;
import com.kbalazsworks.simple_oidc.services.SmartTokenStoreService;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.kbalazsworks.test_helpers.asserts.JwtAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


public class SmartKeyStoreServiceTest_AddTest_GetTest extends AbstractTest
{
    @Inject
    private SmartTokenStoreService smartTokenStoreService;
    @Inject
    private GrantStoreService      grantStoreService;

    @Test
    @SneakyThrows
    public void addAngGetValidToken()
    {
        // Arrange
        grantStoreService.addGrant("client3_client_credentials_1s", new ClientCredentials(
            "client3_client_credentials_1s",
            "client3_client_credentials_1s",
            new ArrayList<>()
            {{
                add("test_scope");
                add("test_scope.a");
            }}
        ));
        grantStoreService.protectStore();

        String testedKey = "key1";
        String testedJwtToken = requestJwtAccessTokenFromIdsWithGrantStoreKey("client3_client_credentials_1s")
            .getAccessToken();
        String expectedToken = String.valueOf(testedJwtToken);

        // Act
        smartTokenStoreService.AddKey(testedKey, testedJwtToken);
        String actual = smartTokenStoreService.GetKey(testedKey);

        // Assert
        assertAll(
            () -> assertThat(actual).isEqualTo(expectedToken),
            () -> assertThat(actual).isJwt()
        );
    }

    @Test
    @SneakyThrows
    public void addAngGetValidTokenWait1sToTimeoutAndRefreshAutomatically()
    {
        // Arrange
        grantStoreService.addGrant("client3_client_credentials_1s", new ClientCredentials(
            "client3_client_credentials_1s",
            "client3_client_credentials_1s",
            new ArrayList<>()
            {{
                add("test_scope");
                add("test_scope.a");
            }}
        ));
        grantStoreService.protectStore();

        String testedKey = "client3_client_credentials_1s";
        String testedJwtToken = requestJwtAccessTokenFromIdsWithGrantStoreKey("client3_client_credentials_1s")
            .getAccessToken();

        // Act
        smartTokenStoreService.AddKey(testedKey, testedJwtToken);
        TimeUnit.SECONDS.sleep(5);
        String actual = smartTokenStoreService.GetKey(testedKey);

        // Assert
        assertAll(
            () -> assertThat(actual).isNotEqualTo(testedJwtToken),
            () -> assertThat(actual).isJwt()
        );
    }
}
