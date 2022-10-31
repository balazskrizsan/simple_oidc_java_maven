package com.kbalazsworks.simple_oidc.services;

import com.kbalazsworks.simple_oidc.entities.grant_type.ClientCredentials;
import com.kbalazsworks.simple_oidc.exceptions.GrantStoreException;
import lombok.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GrantStoreService
{
    private Map<String, ClientCredentials> clientCredentialsState = new HashMap<>();

    public <T> void addGrant(@NonNull String key, @NonNull T grant)
    throws GrantStoreException
    {
        if (grant instanceof ClientCredentials)
        {
            clientCredentialsState.put(key, (ClientCredentials) grant);

            return;
        }

        throw new GrantStoreException("GrantType not found");
    }

    public <T> @NonNull T getGrant(@NonNull String key, @NonNull Class<T> returnType)
    throws GrantStoreException
    {
        if (returnType == ClientCredentials.class)
        {
            return Objects.requireNonNull((T) clientCredentialsState.get(key), "Grant not found: " + key);
        }

        throw new GrantStoreException("GrantType not found");
    }

    public void protectStore()
    {
        clientCredentialsState = Collections.unmodifiableMap(clientCredentialsState);
    }
}
