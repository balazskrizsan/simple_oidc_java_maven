package com.kbalazsworks.simple_oidc.exceptions;

import lombok.experimental.StandardException;

@StandardException
public class OidcApiException extends OidcException
{
    public OidcApiException(String message)
    {
        super(message);
    }
}
