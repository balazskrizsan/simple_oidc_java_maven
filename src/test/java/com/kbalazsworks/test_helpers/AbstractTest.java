package com.kbalazsworks.test_helpers;

import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.entities.JwksKeyItem;
import com.kbalazsworks.simple_oidc.entities.OidcConfig;
import com.kbalazsworks.simple_oidc.factories.OidcSystemFactory;
import com.kbalazsworks.simple_oidc.factories.OkHttpFactory;
import com.kbalazsworks.simple_oidc.services.OidcHttpClientService;
import com.kbalazsworks.simple_oidc.services.OidcResponseValidatorService;
import com.kbalazsworks.simple_oidc.services.OidcService;
import com.kbalazsworks.simple_oidc.services.TokenService;
import lombok.SneakyThrows;

import java.security.PublicKey;

abstract public class AbstractTest
{
    private static final String DISCOVERY_ENDPOINT = "/.well-known/openid-configuration";

    @SneakyThrows
    public OidcService getOidcService()
    {
        String                host                  = "https://localhost:5001";
        OidcHttpClientService oidcHttpClientService = new OidcHttpClientService(new OkHttpFactory());

        OidcConfig oidcConfig = oidcHttpClientService.get(host + DISCOVERY_ENDPOINT, OidcConfig.class);

        return new OidcService(
            oidcConfig,
            new TokenService(),
            oidcHttpClientService,
            new OidcSystemFactory(),
            new OidcResponseValidatorService()
        );
    }

    /**
     * {
     * "keys": [
     * {
     * "kty": "RSA",
     * "use": "sig",
     * "kid": "4B9CC612EBE61D72BE1B2CEE8DDAE495",
     * "e": "AQAB",
     * "n": "1CtIveaHoNVBda1TVSPWCE8owTaQ1-qSxkkLDqKsuIC_p0ND9RgWlU0fArIa4yGzVNWlHL30jdVUEPVbBltZWjjQe_EYeUN-PB8UivhjdtKC_A6hgB3PjfYnUgpg32hzlpCR2hw8sWZ_VNbsOGrSym8hJ7IgugNcFJ0wKG4KjxhxYWxhNyzwO6bMoF2rxyOouFZ0opeSfzSrZdAxOuuOgmbT1X7hl_k1xvsRRDJHrfu_YV10nMQksoPIXWLvy_XyLz3IuhnWi5mrK20UM3vTWRt6gUcRkTQuEzrPBPkKv5UW4R7aOWjzSbSJ0pqdF7J0aRlMpHXuioUhPZAmI8zZ5Q",
     * "alg": "RS256"
     * }
     * ]
     * }
     */
    public JwksKeyItem getJwksKeyItem()
    {
        return new JwksKeyItem(
            "RSA",
            "sig",
            "BA4094AD81F3FD0D7CAA4009D192E23D",
            "AQAB",
            "32dTDGG3MLT_piNhmTDP_mwHITqG8B5SL1DRN2UzcQtiFGAstw0uib0CSG4OVFn3q4C2Nxia7EpyfaaVh5QkOW6nBPVPLA0uADWIp4y8gQ3TzVWxuDRc7mCWgDm9LWW67JkLrowcBx89Y84C__MtPPiSbYlFGLwkts-BeJQRfHOJnKj2T9647Vzwakqgy-sTJWZpqeFDWY1q8IW2vhqgssUOJSrWVmHcBN-xIucETo1BMhvIDCQkwK1llcXSb4HPEGyugS6BtFa_w2H1kH-RSiPStnVWd0FwkQpgqBQlCZseIx4X7aIiV6nR5lIj9SpXDG92zO3zL7cc8nLKeRcdxQ",
            "RS256"
        );
    }

    /**
     * {
     * "alg": "RS256",
     * "kid": "4B9CC612EBE61D72BE1B2CEE8DDAE495",
     * "typ": "at+jwt"
     * }
     * .
     * {
     * "iss": "https://localhost:5001",
     * "nbf": 1659100113,
     * "iat": 1659100113,
     * "exp": 1659103713,
     * "aud": [
     * "sj_aws",
     * "https://localhost:5001/resources"
     * ],
     * "scope": [
     * "sj",
     * "sj.aws.ec2.upload_company_logo",
     * "sj.aws.ses.send_mail"
     * ],
     * "client_id": "sj.aws",
     * "jti": "84FC5DF245FCEF80EA7513A2872711D4"
     * }
     */
    public String getValidExpiredToken()
    {
        return "eyJhbGciOiJSUzI1NiIsImtpZCI6IjRCOUNDNjEyRUJFNjFENzJCRTFCMkNFRThEREFFNDk1IiwidHlwIjoiYXQrand0In0.eyJpc3MiOiJodHRwczovL2xvY2FsaG9zdDo1MDAxIiwibmJmIjoxNjU5MTMwNDY3LCJpYXQiOjE2NTkxMzA0NjcsImV4cCI6MTY1OTEzNDA2NywiYXVkIjpbInNqX2F3cyIsImh0dHBzOi8vbG9jYWxob3N0OjUwMDEvcmVzb3VyY2VzIl0sInNjb3BlIjpbInNqIiwic2ouYXdzLmVjMi51cGxvYWRfY29tcGFueV9sb2dvIiwic2ouYXdzLnNlcy5zZW5kX21haWwiXSwiY2xpZW50X2lkIjoic2ouYXdzIiwianRpIjoiNDE1Rjg4NEIwRkQ4RkFGMjc4Mzg0NzVEMjUxRDhFOTUifQ.UlGdTWiLBthB9pEf0SW_Vb5RQgjzTJnkaNUB0hid0jvOj5R4XUfXreX13SNIx1mGYDCkqePNbS-CuCJjDC7B2nra-o7wfyNO6lMPrGSBqgHzvQ3H8_1KPEi0Fmp-ZSxJ5oKZR0KS3URqNOEv0xmNOpQP3u2tUApOR9L8G_50C-sb6o2nW74JfsiQq0-Jm35vBU8VO_UBdEPC8v-WbwbmM6ptb9_Uys7vyeUWBqSrEygu7NkmrPRJoeNNjM1L42PSSX1WQ0rXCOfrpa2GeczJQl2RTfBnqU8jdkHDZq8DrZM5ViHV6d7XzlZKQ0WHTKutK5hbsl_SpGzTX9_tqdJaeg";
    }

    @SneakyThrows
    public AccessTokenRawResponse requestTokenFromIds()
    {
        String testedClientId     = "sj.aws";
        String testedClientSecret = "sj.aws.client.secret";
        String testedScope        = "sj sj.aws.ses.send_mail sj.aws.ec2.upload_company_logo";
        String testedGrantType    = "client_credentials";

        return getOidcService().callTokenEndpoint(testedClientId, testedClientSecret, testedScope, testedGrantType);
    }

    public String getInvalidToken()
    {
        return "in-valid-token";
    }

    public TokenService getTokenService()
    {
        return new TokenService();
    }

    @SneakyThrows
    public PublicKey getValidPrivateKey()
    {
        JwksKeyItem jwksKeyItem = getJwksKeyItem();

        return getTokenService().getPublicKey(jwksKeyItem.getN(), jwksKeyItem.getE());
    }

    @SneakyThrows
    public byte[] getValidSignature()
    {
        return getTokenService().getSignature(getValidExpiredToken());
    }

    @SneakyThrows
    public byte[] getValidSignedData()
    {
        return getTokenService().getSignedData(getValidExpiredToken());
    }
}

