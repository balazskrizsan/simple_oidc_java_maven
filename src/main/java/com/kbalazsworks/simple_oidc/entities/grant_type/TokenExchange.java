package com.kbalazsworks.simple_oidc.entities.grant_type;

import com.kbalazsworks.simple_oidc.enums.GrantTypeEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class TokenExchange implements IGrantType
{
    private final String              clientId;
    private final String              clientSecret;
    private final List<String>        scope;
    private final String              grantType        = GrantTypeEnum.TOKEN_EXCHANGE.getValue();
    private final Map<String, String> customParameters = new HashMap<>();

    public String getScopeAsString()
    {
        return String.join(" ", scope);
    }
}
