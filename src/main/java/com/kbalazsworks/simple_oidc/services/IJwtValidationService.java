package com.kbalazsworks.simple_oidc.services;

import com.kbalazsworks.simple_oidc.entities.JwtData;
import com.kbalazsworks.simple_oidc.entities.JwtHeader;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwtParseException;
import com.kbalazsworks.simple_oidc.exceptions.OidcKeyException;

import java.security.PublicKey;

public interface IJwtValidationService
{
    JwtData getJwtData(String token) throws OidcJwtParseException;

    JwtHeader getJwtHeader(String token) throws OidcJwtParseException;

    PublicKey getPublicKey(String modulus, String exponent) throws OidcKeyException;

    byte[] getSignature(String token) throws OidcJwtParseException;

    byte[] getSignedData(String token) throws OidcJwtParseException;
}
