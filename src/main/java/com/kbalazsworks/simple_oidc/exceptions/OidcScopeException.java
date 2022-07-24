package com.kbalazsworks.simple_oidc.exceptions;

import lombok.experimental.StandardException;

@StandardException
public class OidcScopeException extends OidcException
{
    public OidcScopeException(String message)
    {
        super(message);
    }
}
