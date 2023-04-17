package com.kbalazsworks.simple_oidc.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GrantTypeEnum
{
    CLIENT_CREDENTIALS("client_credentials"),
    TOKEN_EXCHANGE("token_exchange");

    final private String value;
}
