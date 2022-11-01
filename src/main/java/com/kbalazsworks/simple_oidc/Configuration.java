package com.kbalazsworks.simple_oidc;

import com.kbalazsworks.simple_oidc.exceptions.OidcApiException;
import com.kbalazsworks.simple_oidc.factories.OidcSystemFactory;
import com.kbalazsworks.simple_oidc.services.GrantStoreService;
import com.kbalazsworks.simple_oidc.services.OidcResponseValidatorService;
import com.kbalazsworks.simple_oidc.services.OidcService;
import com.kbalazsworks.simple_oidc.services.TokenService;
import io.activej.inject.annotation.Provides;
import io.activej.inject.module.AbstractModule;

public class Configuration
{
    private static final String DISCOVERY_ENDPOINT = "/.well-known/openid-configuration";
    private static final String HOST               = "https://localhost:5001";

    public AbstractModule setUpDi()
    {
        return new AbstractModule()
        {
            @Provides
            OidcService oidcService() throws OidcApiException
            {
                return new OidcService(
                    HOST,
                    DISCOVERY_ENDPOINT,
                    new TokenService(),
                    new OidcSystemFactory(),
                    new OidcResponseValidatorService(),
                    new GrantStoreService()
                );
            }
        };
    }
}
