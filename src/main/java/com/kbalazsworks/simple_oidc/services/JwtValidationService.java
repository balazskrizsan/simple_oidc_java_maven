package com.kbalazsworks.simple_oidc.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.kbalazsworks.simple_oidc.entities.JwtData;
import com.kbalazsworks.simple_oidc.entities.JwtHeader;
import com.kbalazsworks.simple_oidc.exceptions.OidcJwtParseException;
import com.kbalazsworks.simple_oidc.exceptions.OidcKeyException;
import com.kbalazsworks.simple_oidc.factories.SystemFactory;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;


@Log4j2
public class JwtValidationService
{
    @Inject
    private IValidationService IValidationService;
    @Inject
    private SystemFactory      systemFactory;
    @Inject
    private ICommunicationService communicationService;

    private static final ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public JwtData getJwtData(String token) throws OidcJwtParseException
    {
        try
        {
            return getJwtDataLogic(token);
        }
        catch (Exception e)
        {
            log.error("JWT Data parse error: {}", e.getMessage());

            throw new OidcJwtParseException("JWT Data parse error");
        }
    }

    private JwtData getJwtDataLogic(String token) throws IOException
    {
        String[] tokenParts     = token.split("\\.");
        byte[]   dataPart       = tokenParts[1].getBytes();
        byte[]   decodedJwtData = Base64.getDecoder().decode(dataPart);

        return objectMapper.readValue(decodedJwtData, JwtData.class);
    }

    public JwtHeader getJwtHeader(String token) throws OidcJwtParseException
    {
        try
        {
            return getJwtHeaderLogic(token);
        }
        catch (Exception e)
        {
            log.error("JWT Header parse error: {}", e.getMessage());

            throw new OidcJwtParseException("JWT Header parse error");
        }
    }

    private JwtHeader getJwtHeaderLogic(String token) throws IOException, OidcJwtParseException
    {
        checkValidTokenFormat(token);

        String[] tokenParts       = token.split("\\.");
        byte[]   dataPart         = tokenParts[0].getBytes();
        byte[]   decodedJwtHeader = Base64.getDecoder().decode(dataPart);

        return objectMapper.readValue(decodedJwtHeader, JwtHeader.class);
    }

    public PublicKey getPublicKey(String modulus, String exponent) throws OidcKeyException
    {
        try
        {
            return getPublicKeyLogic(modulus, exponent);
        }
        catch (IllegalArgumentException | InvalidKeySpecException | NoSuchAlgorithmException e)
        {
            log.error("Public key generate error: {}", e.getMessage());

            throw new OidcKeyException("Public key generate error");
        }
    }

    private PublicKey getPublicKeyLogic(String modulus, String exponent)
    throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        Base64.Decoder decoder = Base64.getUrlDecoder();

        byte[]     exponentB   = decoder.decode(exponent);
        byte[]     modulusB    = decoder.decode(modulus);
        BigInteger bigExponent = new BigInteger(1, exponentB);
        BigInteger bigModulus  = new BigInteger(1, modulusB);

        return KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(bigModulus, bigExponent));
    }

    public byte[] getSignature(String token) throws OidcJwtParseException
    {
        try
        {
            return getSignatureLogic(token);
        }
        catch (Exception e)
        {
            log.error("Signature parse error: {}", e.getMessage());

            throw new OidcJwtParseException("Signature parse error");
        }
    }

    private byte[] getSignatureLogic(String token) throws OidcJwtParseException
    {
        checkValidTokenFormat(token);

        String signatureB64u = token.substring(token.lastIndexOf(".") + 1);

        return Base64.getUrlDecoder().decode(signatureB64u);
    }

    public byte[] getSignedData(String token) throws OidcJwtParseException
    {
        try
        {
            checkValidTokenFormat(token);

            return token.substring(0, token.lastIndexOf(".")).getBytes();
        }
        catch (Exception e)
        {
            log.error("Signed data parse error: {}", e.getMessage());

            throw new OidcJwtParseException("Signed data parse error");
        }
    }

    private void checkValidTokenFormat(String token) throws OidcJwtParseException
    {
        int tokenLength = token.replaceAll("[^.]", "").length();
        if (tokenLength != 2)
        {
            throw new OidcJwtParseException("Number of the points in token is: " + tokenLength);
        }
    }
}
