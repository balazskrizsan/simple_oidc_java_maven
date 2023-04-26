package com.kbalazsworks.simple_oidc.services;

import com.kbalazsworks.simple_oidc.entities.grant_type.IGrantType;
import lombok.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GrantStoreService implements IGrantStoreService
{
    private Map<String, IGrantType> clientCredentialsState = new HashMap<>();

    @Override public void addGrant(@NonNull String key, @NonNull IGrantType grant)
    {
        clientCredentialsState.put(key, grant);
    }

    @Override public IGrantType getGrant(@NonNull String key)
    {
        return Objects.requireNonNull(clientCredentialsState.get(key));
    }

    @Override public void protectStore()
    {
        clientCredentialsState = Collections.unmodifiableMap(clientCredentialsState);
    }
}
