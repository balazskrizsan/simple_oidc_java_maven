package com.kbalazsworks.simple_oidc.services;

import com.kbalazsworks.simple_oidc.entities.grant_type.ClientCredentials;
import com.kbalazsworks.simple_oidc.enums.GrantTypesEnum;
import com.kbalazsworks.simple_oidc.exceptions.GrantStoreException;
import lombok.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    public <T> @NonNull T getGrant(@NonNull GrantTypesEnum grantType, @NonNull String key)
    throws GrantStoreException
    {
        if (grantType.name().equals(GrantTypesEnum.ClientCredentials.name()))
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
