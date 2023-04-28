package com.kbalazsworks.simple_oidc.services;

import com.kbalazsworks.simple_oidc.entities.OidcConfig;
import com.kbalazsworks.simple_oidc.exceptions.OidcApiException;
import com.kbalazsworks.simple_oidc.factories.OkHttpFactory;

public class HttpClientService
{
    public static String host             = "https://localhost:5001";
    public static String discoverEndpoint = "/.well-known/openid-configuration";

    private OidcConfig            oidcConfig            = null;
    private OidcHttpClientService oidcHttpClientService = null;

    private void init() throws OidcApiException
    {
        if (null == oidcHttpClientService || null == oidcConfig)
        {
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
