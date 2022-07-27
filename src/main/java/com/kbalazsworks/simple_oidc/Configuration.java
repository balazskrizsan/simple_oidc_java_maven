package com.kbalazsworks.simple_oidc;

import com.kbalazsworks.simple_oidc.entities.OidcConfig;
import com.kbalazsworks.simple_oidc.exceptions.OidcApiException;
import com.kbalazsworks.simple_oidc.factories.OidcSystemFactory;
import com.kbalazsworks.simple_oidc.factories.OkHttpFactory;
import com.kbalazsworks.simple_oidc.services.OidcHttpClientService;
import com.kbalazsworks.simple_oidc.services.OidcResponseValidatorService;
import com.kbalazsworks.simple_oidc.services.OidcService;
import com.kbalazsworks.simple_oidc.services.TokenService;
import io.activej.inject.annotation.Provides;
import io.activej.inject.module.AbstractModule;

public class Configuration
{
    private static final String DISCOVERY_ENDPOINT = "/.well-known/openid-configuration";

    public AbstractModule setUpDi()
    {
        return new AbstractModule()
        {
            @Provides
            OidcService oidcService() throws OidcApiException
            {
                String                host                  = "https://localhost:5001";
                OidcHttpClientService oidcHttpClientService = new OidcHttpClientService(new OkHttpFactory());

                OidcConfig oidcConfig = oidcHttpClientService.get(host + DISCOVERY_ENDPOINT, OidcConfig.class);

                return new OidcService(
                    oidcConfig,
                    new TokenService(),
                    oidcHttpClientService,
                    new OidcSystemFactory(),
                    new OidcResponseValidatorService()
                );
            }
        };
    }
}
