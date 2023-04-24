package com.kbalazsworks.test_helpers;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kbalazsworks.simple_oidc.DiConfigModule;
import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.entities.BasicAuth;
import com.kbalazsworks.simple_oidc.entities.JwksKeyItem;
import com.kbalazsworks.simple_oidc.entities.JwksKeys;
import com.kbalazsworks.simple_oidc.services.CommunicationService;
import com.kbalazsworks.simple_oidc.services.JwtValidationService;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;

import java.security.PublicKey;


@Log4j2
abstract public class AbstractTest
{
    protected Injector injector = Guice.createInjector(new DiConfigModule());

    @Before
    public void setup()
    {
        injector.injectMembers(this);
    }

    private static final   String    DISCOVERY_ENDPOINT    = "/.well-known/openid-configuration";
    protected static final String    HOST                  = "http://localhost:91";
    protected static final BasicAuth INTROSPECT_BASIC_AUTH = new BasicAuth("test_resource_a", "test_resource_a_secret");

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
            "206129EFF13845D03992A25B514DF33D",
            "AQAB",
            "txXJXF0geKMNyNkDtBbD8khj4FqKnabgoMVClm-Zd1hgzh5ftyU9DR814N7WztcCSlFe20GrASq5UDh0EfKolVgq7_ZsiABReQuGpNCflkTyUKWCHz0B27OBtwNX5IKpElw4piUwoxuj29i2xlh3NmqsnLnPQFkmXbomySLKxWJO2LRfCBq5-FTF8azCvQOerIhDZBX_9S1GgnqIQCgXnNK6gbg_J8_pmIt-NmtUuFT8y20Qm43AeXVKgAAVqBPdQbYWJ_zBu08DwioQ6IAjVUzfyLNnk25g-2ZY6dd0-RobjKWsNkv2AzbKmQfZ7ugEXPpqNuMyuYfSg_yMGs88nQ",
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
    public AccessTokenRawResponse requestJwtAccessTokenFromIds()
    {
        String testedClientId     = "client1_client_credentials";
        String testedClientSecret = "client1_client_credentials_secret";
        String testedScope        = "test_scope test_scope.a";
        String testedGrantType    = "client_credentials";

        return injector
            .getInstance(CommunicationService.class)
            .callTokenEndpoint(testedClientId, testedClientSecret, testedScope, testedGrantType);
    }

    @SneakyThrows
    public AccessTokenRawResponse requestJwtAccessTokenFromIdsWithGrantStoreKey(String grantStoryKey)
    {
        return injector.getInstance(CommunicationService.class).callTokenEndpoint(grantStoryKey);
    }

    @SneakyThrows
    public JwksKeys requestJwksFromIds()
    {
        return injector.getInstance(CommunicationService.class).callJwksEndpoint();
    }

    public String getInvalidToken()
    {
        return "in-valid-token";
    }

    @SneakyThrows
    public PublicKey getValidPublicKey()
    {
        JwksKeyItem jwksKeyItem = getJwksKeyItem();

        return injector.getInstance(JwtValidationService.class).getPublicKey(jwksKeyItem.getN(), jwksKeyItem.getE());
    }

    @SneakyThrows
    public byte[] getValidSignature()
    {
        return injector.getInstance(JwtValidationService.class).getSignature(requestJwtAccessTokenFromIds().getAccessToken());
    }

    @SneakyThrows
    public byte[] getValidSignedData()
    {
        return injector.getInstance(JwtValidationService.class).getSignedData(requestJwtAccessTokenFromIds().getAccessToken());
    }
}

