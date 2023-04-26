package com.kbalazsworks.simple_oidc.services;

import com.kbalazsworks.simple_oidc.exceptions.OidcExpiredTokenException;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwksVerificationException;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwtParseException;
import com.kbalazsworks.simple_oidc.exceptions.OidcScopeException;
import lombok.NonNull;

import java.security.PublicKey;
import java.util.List;

public interface IValidationService
{
    void checkScopesInToken(@NonNull String token, @NonNull List<String> scopes)
    throws OidcScopeException, OidcJwtParseException, OidcExpiredTokenException, OidcJwksVerificationException;

    boolean isVerified(PublicKey publicKey, byte[] signedData, byte[] signature);

    boolean isValidated(String token);

    void checkValidated(String token)
    throws OidcExpiredTokenException, OidcJwksVerificationException, OidcJwtParseException;

    void checkExpiredToken(@NonNull String token) throws OidcExpiredTokenException, OidcJwtParseException;

    boolean isExpiredToken(@NonNull String token) throws OidcJwtParseException;

    // @todo: remove; it's in TokenValidationService
    void checkJwksVerifiedToken(@NonNull String token) throws OidcJwksVerificationException;

    boolean isJwksVerifiedToken(@NonNull String token);
}
