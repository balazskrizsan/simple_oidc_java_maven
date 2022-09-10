package com.kbalazsworks.simple_oidc.services;

import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.entities.BasicAuth;
import com.kbalazsworks.simple_oidc.entities.IntrospectRawResponse;
import com.kbalazsworks.simple_oidc.entities.JwksKeyItem;
import com.kbalazsworks.simple_oidc.entities.JwksKeys;
import com.kbalazsworks.simple_oidc.entities.OidcConfig;
import com.kbalazsworks.simple_oidc.exceptions.OidcApiException;
import com.kbalazsworks.simple_oidc.exceptions.OidcExpiredTokenException;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwksException;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwksVerificationException;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwtParseException;
import com.kbalazsworks.simple_oidc.exceptions.OidcKeyException;
import com.kbalazsworks.simple_oidc.exceptions.OidcScopeException;
import com.kbalazsworks.simple_oidc.factories.OidcSystemFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.security.PublicKey;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
public class OidcService implements IOidcService
{
    private final OidcConfig                   oidcConfig;
    private final TokenService                 tokenService;
    private final OidcHttpClientService        oidcHttpClientService;
    private final OidcSystemFactory            oidcSystemFactory;
    private final OidcResponseValidatorService oidcResponseValidatorService;

    public @NonNull AccessTokenRawResponse callTokenEndpoint(
        @NonNull String clientId,
        @NonNull String clientSecret,
        @NonNull String scope,
        @NonNull String grantType
    )
    throws OidcApiException
    {
        return oidcResponseValidatorService.tokenEndpointValidator(
            oidcHttpClientService.post(
                oidcConfig.getTokenEndpoint(),
                Map.of(
                    "client_id", clientId,
                    "client_secret", clientSecret,
                    "scope", scope,
                    "grant_type", grantType
                ),
                AccessTokenRawResponse.class
            )
        );
    }

    public @NonNull IntrospectRawResponse callIntrospectEndpoint(
        @NonNull String accessToken,
        @NonNull BasicAuth basicAuth
    ) throws OidcApiException
    {
        return oidcResponseValidatorService.introspectEndpointValidator(
            oidcHttpClientService.post(
                oidcConfig.getIntrospectionEndpoint(),
                Map.of("token", accessToken),
                IntrospectRawResponse.class,
                basicAuth
            )
        );
    }

    public @NonNull JwksKeys callJwksEndpoint() throws OidcApiException
    {
        return oidcResponseValidatorService.jwksEndpointValidator(
            oidcHttpClientService.get(oidcConfig.getJwksUri(), JwksKeys.class)
        );
    }

    public void checkScopesInToken(@NonNull String token, @NonNull List<String> scopes)
    throws OidcScopeException, OidcJwtParseException, OidcExpiredTokenException, OidcJwksVerificationException
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
            log.error("No scope found in token: {}", scopes);

            throw new OidcScopeException("No scope found in token");
        }
    }

    public void checkValidated(String token)
    throws OidcExpiredTokenException, OidcJwksVerificationException, OidcJwtParseException
    {
        checkExpiredToken(token);
        checkJwksVerifiedToken(token);
    }

    public void checkExpiredToken(@NonNull String token) throws OidcExpiredTokenException, OidcJwtParseException
    {
        if (isExpiredToken(token))
        {
            log.error("Expired token");

            throw new OidcExpiredTokenException("Expired token");
        }
    }

    public @NonNull Boolean isExpiredToken(@NonNull String token) throws OidcJwtParseException
    {
        Integer expiration = tokenService.getJwtData(token).getExp();
        long    now        = oidcSystemFactory.getCurrentTimeMillis() / 1000;

        return expiration < now;
    }

    public void checkJwksVerifiedToken(@NonNull String token) throws OidcJwksVerificationException
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

    public @NonNull Boolean isJwksVerifiedToken(@NonNull String token)
    {
        try
        {
            return checkJwksVerifiedTokenLogic(token);
        }
        // @todo: test
        catch (Exception e)
        {
            log.error("JWKS verification failed: {}", e.getMessage());

            return false;
        }
    }

    private @NonNull Boolean checkJwksVerifiedTokenLogic(@NonNull String token)
    throws OidcApiException, OidcJwtParseException, OidcKeyException, OidcJwksException
    {
        String alg = tokenService.getJwtHeader(token).alg;

        Optional<JwksKeyItem> optionalJwksKeyItem = callJwksEndpoint()
            .getKeys()
            .stream()
            .filter(k -> k.getAlg().equals(alg))
            .findFirst();

        // @todo: add test
        if (optionalJwksKeyItem.isEmpty())
        {
            log.error("Missing JWKS key for alg: {}", alg);

            throw new OidcJwksException("Missing JWKS key for alg");
        }

        final JwksKeyItem key = optionalJwksKeyItem.get();

        PublicKey publicKey  = tokenService.getPublicKey(key.getN(), key.getE());
        byte[]    signature  = tokenService.getSignature(token);
        byte[]    signedData = tokenService.getSignedData(token);

        return tokenService.isVerified(publicKey, signedData, signature);
    }
}
