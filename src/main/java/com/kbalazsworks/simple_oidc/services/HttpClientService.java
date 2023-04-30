package com.kbalazsworks.simple_oidc.services;

import com.kbalazsworks.simple_oidc.entities.OidcConfig;
import com.kbalazsworks.simple_oidc.exceptions.OidcApiException;
import com.kbalazsworks.simple_oidc.factories.OkHttpFactory;

public class HttpClientService
{
    private OidcConfig            oidcConfig            = null;
    private OidcHttpClientService oidcHttpClientService = null;

    private void init() throws OidcApiException
    {
        if (null == oidcHttpClientService || null == oidcConfig)
        {
            final String host             = "http://localhost:91";
            final String discoverEndpoint = "/.well-known/openid-configuration";

            boolean isHttps = host.contains("https");

            oidcHttpClientService = new OidcHttpClientService(isHttps, new OkHttpFactory());
            oidcConfig            = oidcHttpClientService.get(host + discoverEndpoint, OidcConfig.class);
        }
    }

    public OidcHttpClientService getOidcHttpClientService() throws OidcApiException
    {
        init();

        return oidcHttpClientService;
    }

    public OidcConfig getOidcConfig() throws OidcApiException
    {
        init();

        return oidcConfig;
    }
}
