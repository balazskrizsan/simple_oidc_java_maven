package com.kbalazsworks.simple_oidc.services;

import com.kbalazsworks.simple_oidc.common.services.SystemFactory;
import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.entities.BasicAuth;
import com.kbalazsworks.simple_oidc.entities.IntrospectRawResponse;
import com.kbalazsworks.simple_oidc.entities.JwksKeyItem;
import com.kbalazsworks.simple_oidc.entities.JwksKeys;
import com.kbalazsworks.simple_oidc.entities.OidcConfig;
import com.kbalazsworks.simple_oidc.exceptions.OidcException;
import com.kbalazsworks.simple_oidc.exceptions.OidcExpiredTokenException;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwksVerificationException;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwtParseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.util.LinkedMultiValueMap;

import java.security.PublicKey;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
public class OidcService implements IOidcService
{
    private final OidcConfig            oidcConfig;
    private final TokenService          tokenService;
    private final OidcHttpClientService oidcHttpClientService;
    private final SystemFactory         systemFactory;

    public AccessTokenRawResponse callTokenEndpoint(
        String clientId,
        String clientSecret,
        String scope,
        String grantType
    )
    {
        return oidcHttpClientService.postWithMap(
            oidcConfig.getTokenEndpoint(),
            new LinkedMultiValueMap<>()
            {{
                addAll("client_id", List.of(clientId));
                addAll("client_secret", List.of(clientSecret));
                addAll("scope", List.of(scope));
                addAll("grant_type", List.of(grantType));
            }},
            AccessTokenRawResponse.class
        );
    }

    public IntrospectRawResponse callIntrospectEndpoint(String accessToken, BasicAuth basicAuth)
    {
        return oidcHttpClientService.postWithMap(
            oidcConfig.getIntrospectionEndpoint(),
            new LinkedMultiValueMap<>()
            {{
                addAll("token", List.of(accessToken));
            }},
            IntrospectRawResponse.class,
            basicAuth
        );
    }

    public JwksKeys callJwksEndpoint()
    {
        return oidcHttpClientService.getWithMap(oidcConfig.getJwksUri(), JwksKeys.class);
    }

    public void checkScopesInToken(String token, List<String> scopes) throws OidcException
    {
        checkValidated(token);

        List<String> matchedScopes = tokenService
            .getJwtData(token)
            .getScope()
            .stream()
            .filter(scopes::contains)
            .collect(Collectors.toList());

        if (matchedScopes.isEmpty())
        {
            throw new OidcException("Scope missing from token");
        }
    }

    public void checkValidated(String token)
    throws OidcExpiredTokenException, OidcJwksVerificationException, OidcJwtParseException
    {
        checkExpiredToken(token);
        checkJwksVerifiedToken(token);
    }

    public Boolean isExpiredToken(String token) throws OidcJwtParseException
    {
        Integer expiration = tokenService.getJwtData(token).getExp();
        long    now        = systemFactory.getCurrentTimeMillis() / 1000;

        return expiration < now;
    }

    public void checkExpiredToken(String token) throws OidcExpiredTokenException, OidcJwtParseException
    {
        if (isExpiredToken(token))
        {
            throw new OidcExpiredTokenException();
        }
    }

    public Boolean isJwksVerifiedToken(String token)
    {
        try
        {
            return checkJwksVerifiedTokenLogic(token);
        }
        catch (Exception e)
        {
            log.error("JWKS verification failed: {}", e.getMessage());

            return false;
        }
    }

    public void checkJwksVerifiedToken(String token) throws OidcJwksVerificationException
    {
        Boolean isVerified;
        try
        {
            isVerified = checkJwksVerifiedTokenLogic(token);
        }
        catch (Exception e)
        {
            throw new OidcJwksVerificationException(e.getMessage());
        }

        if (!isVerified)
        {
            throw new OidcJwksVerificationException();
        }
    }

    private Boolean checkJwksVerifiedTokenLogic(String token) throws OidcException
    {
        String alg = tokenService.getJwtHeader(token).alg;

        final JwksKeyItem key = callJwksEndpoint()
            .getKeys()
            .stream()
            .filter(k -> k.getAlg().equals(alg))
            .findFirst()
            .get();

        PublicKey publicKey  = tokenService.getPublicKey(key.getN(), key.getE());
        byte[]    signature  = tokenService.getSignature(token);
        val       signedData = tokenService.getSignedData(token);

        return tokenService.isVerified(publicKey, signedData, signature);
    }
}
