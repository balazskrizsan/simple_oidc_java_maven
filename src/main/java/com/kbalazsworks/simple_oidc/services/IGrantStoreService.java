package com.kbalazsworks.simple_oidc.services;

import com.kbalazsworks.simple_oidc.entities.grant_type.IGrantType;
import lombok.NonNull;

public interface IGrantStoreService
{
    void addGrant(@NonNull String key, @NonNull IGrantType grant);

    IGrantType getGrant(@NonNull String key);

    void protectStore();
}
