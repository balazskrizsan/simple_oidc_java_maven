package com.kbalazsworks.simple_oidc.entities.grant_type;

public interface IGrantType
{
    String getClientId();
    String getClientSecret();
    String getGrantType();
    String getScopeAsString();
}
