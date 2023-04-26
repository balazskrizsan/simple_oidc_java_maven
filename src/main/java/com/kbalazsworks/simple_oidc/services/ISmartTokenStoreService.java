package com.kbalazsworks.simple_oidc.services;

import com.kbalazsworks.simple_oidc.exceptions.OidcSmartTokenStoreException;

public interface ISmartTokenStoreService
{
    void AddKey(String key, String token);

    String GetKey(String key) throws OidcSmartTokenStoreException;
}
