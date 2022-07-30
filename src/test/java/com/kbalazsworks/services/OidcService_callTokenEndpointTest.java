package com.kbalazsworks.services;

import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.entities.JwtData;
import com.kbalazsworks.simple_oidc.entities.JwtHeader;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OidcService_callTokenEndpointTest extends AbstractTest
{
    @Test
    @SneakyThrows
    public void callTokenEndpointWithValidParameters_returnsValidJwt()
    {
        // Arrange
        String       expectedAlg         = "RS256";
        String       expectedTyp         = "at+jwt";
        String       expectedClientId    = "sj.aws";
        List<String> expectedScopeAsList = List.of("sj", "sj.aws.ec2.upload_company_logo", "sj.aws.ses.send_mail");
        String       expectedIss         = "https://localhost:5001";
        List<String> expectedAud         = List.of("sj_aws", "https://localhost:5001/resources");
        Integer      expectedExpiresIn   = 3600;
        String       expectedTokenType   = "Bearer";
        String       expectedScope       = "sj sj.aws.ec2.upload_company_logo sj.aws.ses.send_mail";

        // Act
        AccessTokenRawResponse actual = requestTokenFromIds();

        // Assert
        JwtData   jwtData   = getTokenService().getJwtData(actual.getAccessToken());
        JwtHeader jwtHeader = getTokenService().getJwtHeader(actual.getAccessToken());

        assertAll(
            () -> assertThat(jwtHeader.getAlg()).isEqualTo(expectedAlg),
            () -> assertThat(jwtHeader.getTyp()).isEqualTo(expectedTyp),
            () -> assertThat(jwtData.getClientId()).isEqualTo(expectedClientId),
            () -> assertThat(jwtData.getScope()).isEqualTo(expectedScopeAsList),
            () -> assertThat(jwtData.getIss()).isEqualTo(expectedIss),
            () -> assertThat(jwtData.getAud()).isEqualTo(expectedAud),
            () -> assertThat(jwtData.getExp() - jwtData.getIat()).isEqualTo(expectedExpiresIn),
            () -> assertThat(actual.getExpiresIn()).isEqualTo(expectedExpiresIn),
            () -> assertThat(actual.getTokenType()).isEqualTo(expectedTokenType),
            () -> assertThat(actual.getScope()).isEqualTo(expectedScope)
        );
    }
}
