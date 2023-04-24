package com.kbalazsworks.simple_oidc.exceptions;

import lombok.experimental.StandardException;

@StandardException
public class OidcSmartTokenStoreException extends OidcException
{
    public OidcSmartTokenStoreException(String message)
    {
        super(message);
    }
}
