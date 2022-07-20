package com.kbalazsworks.simple_oidc.factories;

import com.kbalazsworks.simple_oidc.common.services.SystemFactory;
import com.kbalazsworks.simple_oidc.entities.OidcConfig;
import com.kbalazsworks.simple_oidc.services.OidcHttpClientService;
import com.kbalazsworks.simple_oidc.services.OidcService;
import com.kbalazsworks.simple_oidc.services.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class OidcServiceFactory
{
    private final OidcHttpClientService oidcHttpClientService;
    private final TokenService          tokenService;
    private final SystemFactory         systemFactory;

    private static final String DISCOVERY_ENDPOINT = "/.well-known/openid-configuration";

    public OidcService create(String host)
    {
        OidcConfig oidcConfig = oidcHttpClientService.getWithMap(host + DISCOVERY_ENDPOINT, OidcConfig.class);

        return new OidcService(oidcConfig, tokenService, oidcHttpClientService, systemFactory);
    }
}
