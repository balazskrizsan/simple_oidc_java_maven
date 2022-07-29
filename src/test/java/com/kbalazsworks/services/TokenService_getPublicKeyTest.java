package com.kbalazsworks.services;

import com.kbalazsworks.simple_oidc.exceptions.OidcKeyException;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import okio.ByteString;
import org.junit.Test;

import java.security.PublicKey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TokenService_getPublicKeyTest extends AbstractTest
{
    @Test
    @SneakyThrows
    public void validKeyData_generatesPublicKey()
    {
        // Arrange
        String testedModulus
            = "1CtIveaHoNVBda1TVSPWCE8owTaQ1-qSxkkLDqKsuIC_p0ND9RgWlU0fArIa4yGzVNWlHL30jdVUEPVbBltZWjjQe_EYeUN-PB8UivhjdtKC_A6hgB3PjfYnUgpg32hzlpCR2hw8sWZ_VNbsOGrSym8hJ7IgugNcFJ0wKG4KjxhxYWxhNyzwO6bMoF2rxyOouFZ0opeSfzSrZdAxOuuOgmbT1X7hl_k1xvsRRDJHrfu_YV10nMQksoPIXWLvy_XyLz3IuhnWi5mrK20UM3vTWRt6gUcRkTQuEzrPBPkKv5UW4R7aOWjzSbSJ0pqdF7J0aRlMpHXuioUhPZAmI8zZ5Q";
        String testedExponent = "AQAB";

        String expectedAlgorithm = "RSA";
        String expectedFormat    = "X.509";
        String expectedToString = "Sun RSA public key, 2048 bits\n" +
            "  params: null\n" +
            "  modulus: 26783864861521073443576794371307653805125482200676057182736355414917798379976885131959420676469889990994214843288903819243756136522709902752731390033385824750164962220547671486137688497654424221783336119631761033615596866293957758502550283207859774245775783128491827072806518065996728796190399331513993964290817921900659626887397624764519873415063410615248840930079003167176875457933430961122573531780448503760788227152482718949267394795445119815903805891051692863774464761165271032933001119409028516061477716038498637599532207203283867735180137244335581706786522362817391654084376110728106618992374030942594501761509\n" +
            "  public exponent: 65537";
        String expectedEncodedHash = "[hex=bc20b5a69af5a1682e3974b272491c058a58848107eab579376ecac8bc4d5c35]";

        // Act
        PublicKey actual = getTokenService().getPublicKey(testedModulus, testedExponent);

        // Assert
        assertAll(
            () -> assertThat(actual.getAlgorithm()).isEqualTo(expectedAlgorithm),
            () -> assertThat(actual.getFormat()).isEqualTo(expectedFormat),
            () -> assertThat(ByteString.of(actual.getEncoded()).sha256().toString()).isEqualTo(expectedEncodedHash),
            () -> assertThat(actual.toString()).isEqualTo(expectedToString)
        );
    }

    @Test
    @SneakyThrows
    public void invalidKeyData_throwsException()
    {
        // Arrange
        String testedModulus  = "short-modulus";
        String testedExponent = "AQAB";

        Class<OidcKeyException> expectedException    = OidcKeyException.class;
        String                  expectedErrorMessage = "Public key generate error";

        // Act - Assert
        assertThatThrownBy(() -> getTokenService().getPublicKey(testedModulus, testedExponent))
            .isInstanceOf(expectedException)
            .hasMessage(expectedErrorMessage);
    }
}
