package com.kbalazsworks.simple_oidc.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GrantTypesEnum
{
    ClientCredentials("client_credentials");

    final private String value;
}
