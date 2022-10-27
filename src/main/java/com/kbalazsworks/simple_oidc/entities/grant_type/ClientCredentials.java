package com.kbalazsworks.simple_oidc.entities.grant_type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ClientCredentials
{
    private final String       clientId;
    private final String       clientSecret;
    private final List<String> scope;
    private final String       grantType = "client_credentials";

    public String getScopeAsString()
    {
        return String.join(" ", scope);
    }
}
