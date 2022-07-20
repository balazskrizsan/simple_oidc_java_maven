package com.kbalazsworks.simple_oidc.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BasicAuth
{
    private final String userName;
    private final String password;
}
