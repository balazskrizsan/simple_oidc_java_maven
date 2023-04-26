package com.kbalazsworks.simple_oidc.services;

import com.google.inject.Inject;
import com.kbalazsworks.simple_oidc.entities.JwksKeyItem;
import com.kbalazsworks.simple_oidc.exceptions.OidcApiException;
import com.kbalazsworks.simple_oidc.exceptions.OidcExpiredTokenException;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwksException;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwksVerificationException;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwtParseException;
import com.kbalazsworks.simple_oidc.exceptions.OidcKeyException;
import com.kbalazsworks.simple_oidc.exceptions.OidcScopeException;
import com.kbalazsworks.simple_oidc.factories.SystemFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Log4j2
public class ValidationService implements IValidationService
{
    private final IJwtValidationService jwtValidationService;
    private final CommunicationService  communicationService;
    private final SystemFactory         systemFactory;

    /**
     * @param token  Allows JWT token
     * @param scopes If any of the listed scope in the token, it will be void
     * @todo Implement Reference token checker
     */
    public void checkScopesInToken(@NonNull String token, @NonNull List<String> scopes)
    throws OidcScopeException, OidcJwtParseException, OidcExpiredTokenException, OidcJwksVerificationException
    {
        log.info("Check scopes in token: {}", scopes);

        checkValidated(token);

        List<String> matchedScopes = jwtValidationService
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

    public boolean isVerified(PublicKey publicKey, byte[] signedData, byte[] signature)
    {
        try
        {
            return isVerifiedLogic(publicKey, signedData, signature);
        }
        catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e)
        {
            log.error("Public key verification error: {}", e.getMessage());

            return false;
        }
    }

    private boolean isVerifiedLogic(PublicKey publicKey, byte[] signedData, byte[] signature)
    throws NoSuchAlgorithmException, InvalidKeyException, SignatureException
    {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(signedData);

        return sig.verify(signature);
    }

    public boolean isValidated(String token)
    {
        try
        {
            checkValidated(token);
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    public void checkValidated(String token)
    throws OidcExpiredTokenException, OidcJwksVerificationException, OidcJwtParseException
    {
        log.info("Check validated token");

        checkExpiredToken(token);
        checkJwksVerifiedToken(token);
    }

    public void checkExpiredToken(@NonNull String token) throws OidcExpiredTokenException, OidcJwtParseException
    {
        log.info("Check expired token");

        if (isExpiredToken(token))
        {
            log.error("Expired token");

            throw new OidcExpiredTokenException("Expired token");
        }
    }

    public boolean isExpiredToken(@NonNull String token) throws OidcJwtParseException
    {
        Integer expiration = jwtValidationService.getJwtData(token).getExp();
        long    now        = systemFactory.getCurrentTimeMillis() / 1000;

        return expiration < now;
    }

    // @todo: remove; it's in TokenValidationService
    public void checkJwksVerifiedToken(@NonNull String token) throws OidcJwksVerificationException
    {
        boolean isVerified;
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

    public @NonNull boolean isJwksVerifiedToken(@NonNull String token)
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

    private boolean checkJwksVerifiedTokenLogic(@NonNull String token)
    throws OidcApiException, OidcJwtParseException, OidcKeyException, OidcJwksException
    {
        String alg = jwtValidationService.getJwtHeader(token).alg;

        Optional<JwksKeyItem> optionalJwksKeyItem = communicationService
            .callJwksEndpoint()
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

        PublicKey publicKey  = jwtValidationService.getPublicKey(key.getN(), key.getE());
        byte[]    signature  = jwtValidationService.getSignature(token);
        byte[]    signedData = jwtValidationService.getSignedData(token);

        return isVerified(publicKey, signedData, signature);
    }
}
