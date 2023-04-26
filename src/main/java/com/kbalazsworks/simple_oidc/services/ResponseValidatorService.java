package com.kbalazsworks.simple_oidc.services;

import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.entities.IntrospectRawResponse;
import com.kbalazsworks.simple_oidc.entities.JwksKeys;
import com.kbalazsworks.simple_oidc.exceptions.OidcApiException;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ResponseValidatorService implements IResponseValidatorService
{
    @Override public AccessTokenRawResponse tokenEndpointValidator(AccessTokenRawResponse response) throws OidcApiException
    {
        if (null == response.getAccessToken()
            || null == response.getExpiresIn()
            || null == response.getTokenType()
            || null == response.getScope())
        {
            log.error("Token endpoint invalid response: {}", response);

            throw new OidcApiException("Token endpoint invalid response");
        }

        return response;
    }

    @Override public IntrospectRawResponse introspectEndpointValidator(IntrospectRawResponse response)
    throws OidcApiException
    {
        if (null == response.getActive())
        {
            log.error("Token endpoint invalid response: {}", response);

            throw new OidcApiException("Token endpoint invalid response");
        }


        return response;
    }

    @Override public JwksKeys jwksEndpointValidator(JwksKeys response) throws OidcApiException
    {
        if (response.getKeys().isEmpty())
        {
            log.error("Token endpoint invalid response: {}", response);

            throw new OidcApiException("Token endpoint invalid response");
        }

        return response;
    }
}
