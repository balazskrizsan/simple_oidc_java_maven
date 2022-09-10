package com.kbalazsworks.simple_oidc.exceptions;

import lombok.experimental.StandardException;

@StandardException
public class OidcJwksException extends OidcException
{
    public OidcJwksException()
    {
    }

    public OidcJwksException(String message)
    {
        super(message);
    }
}
