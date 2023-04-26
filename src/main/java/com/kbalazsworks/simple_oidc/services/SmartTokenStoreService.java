package com.kbalazsworks.simple_oidc.services;

import com.google.inject.Inject;
import com.kbalazsworks.simple_oidc.exceptions.OidcSmartTokenStoreException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Log4j2
public class SmartTokenStoreService implements ISmartTokenStoreService
{
    private final IValidationService    validationService;
    private final ICommunicationService communicationService;

    private Map<String, String> keyMap = new HashMap<>();

    @Override public void AddKey(String key, String token)
    {
        keyMap.put(key, token);
    }

    @Override public String GetKey(String key) throws OidcSmartTokenStoreException
    {
        String token = keyMap.get(key);

        if (isJwt(token))
        {
            if (validationService.isValidated(token))
            {
                return token;
            }

            try
            {
                return communicationService.callTokenEndpoint(key).getAccessToken();
            }
            catch (Exception e)
            {
                log.error("Auto refresh error on key: {}, {}", key, e.getMessage(), e);

                throw new OidcSmartTokenStoreException("Auto refresh error on key");
            }
        }

        return token;
    }

    private boolean isJwt(String token)
    {
        return StringUtils.countMatches(token, ".") == 2;
    }
}
