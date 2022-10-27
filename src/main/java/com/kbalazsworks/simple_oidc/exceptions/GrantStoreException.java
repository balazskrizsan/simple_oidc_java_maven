package com.kbalazsworks.simple_oidc.exceptions;

import lombok.experimental.StandardException;

@StandardException
public class GrantStoreException extends OidcException
{
    public GrantStoreException()
    {
    }

    public GrantStoreException(String message)
    {
        super(message);
    }
}
