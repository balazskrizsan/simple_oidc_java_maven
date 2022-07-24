package com.kbalazsworks.simple_oidc.exceptions;

import lombok.experimental.StandardException;

@StandardException
public class OidcKeyException extends OidcException
{
    public OidcKeyException(String message)
    {
        super(message);
    }
}
