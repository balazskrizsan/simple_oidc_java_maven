package com.kbalazsworks.simple_oidc.services;

import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.entities.IntrospectRawResponse;
import com.kbalazsworks.simple_oidc.entities.JwksKeys;
import com.kbalazsworks.simple_oidc.exceptions.OidcApiException;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class OidcResponseValidatorService
{
    public AccessTokenRawResponse tokenEndpointValidator(AccessTokenRawResponse response) throws OidcApiException
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

    public IntrospectRawResponse introspectEndpointValidator(IntrospectRawResponse response)
    throws OidcApiException
    {
        if (null == response.getActive()
            || null == response.getIss()
            || null == response.getNbf()
            || null == response.getIat()
            || null == response.getExp()
            || response.getAud().isEmpty()
            || null == response.getClientId()
            || null == response.getJti()
            || null == response.getScope()
        )
        {
            log.error("Token endpoint invalid response: {}", response);

            throw new OidcApiException("Token endpoint invalid response");
        }


        return response;
    }

    public JwksKeys jwksEndpointValidator(JwksKeys response) throws OidcApiException
    {
        if (response.getKeys().isEmpty())
        {
            log.error("Token endpoint invalid response: {}", response);

            throw new OidcApiException("Token endpoint invalid response");
        }

        return response;
    }
}
