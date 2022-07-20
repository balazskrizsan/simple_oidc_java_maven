package com.kbalazsworks.simple_oidc.exceptions;

import lombok.experimental.StandardException;

@StandardException
public class OidcJwksVerificationException extends OidcException
{
    public OidcJwksVerificationException()
    {
    }

    public OidcJwksVerificationException(String message)
    {
        super(message);
    }
}
