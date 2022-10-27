package com.kbalazsworks.simple_oidc.services;

import com.kbalazsworks.simple_oidc.entities.grant_type.ClientCredentials;
import com.kbalazsworks.simple_oidc.enums.GrantTypesEnum;
import com.kbalazsworks.simple_oidc.exceptions.GrantStoreException;
import lombok.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GrantStoreService
{
    private Map<String, ClientCredentials> clientCredentialsState = new HashMap<>();

    // @todo: test
    public <T> void addGrant(@NonNull GrantTypesEnum grantType, @NonNull String key, @NonNull T grant)
    throws GrantStoreException
    {
        if (grantType.name().equals(GrantTypesEnum.ClientCredentials.name()))
        {
            clientCredentialsState.put(key, (ClientCredentials) grant);

            return;
        }

        throw new GrantStoreException("GrantType not found");
    }

    // @todo: test
    public <T> @NonNull T getGrantTypeEntity(@NonNull GrantTypesEnum grantType, @NonNull String key)
    throws GrantStoreException
    {
        if (grantType.name().equals(GrantTypesEnum.ClientCredentials.name()))
        {
            T grant = (T) clientCredentialsState.get(key);

            if (null == grant)
            {
                throw new GrantStoreException("Grant not found");
            }

            return (T) clientCredentialsState.get(key);
        }

        throw new GrantStoreException("GrantType not found");
    }

    public void setGrantsToImmutable()
    {
        clientCredentialsState = Collections.unmodifiableMap(clientCredentialsState);
    }
}
