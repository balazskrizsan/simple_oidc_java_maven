package com.kbalazsworks.services;

import com.kbalazsworks.simple_oidc.entities.AccessTokenRawResponse;
import com.kbalazsworks.simple_oidc.entities.BasicAuth;
import com.kbalazsworks.simple_oidc.entities.IntrospectRawResponse;
import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OidcService_callIntrospectionEndpointTest extends AbstractTest
{
    @Test
    @SneakyThrows
    public void callTokenEndpointWithValidParameters_returnsValidJwt()
    {
        // Arrange
        AccessTokenRawResponse testedToken     = requestTokenFromIds();
        BasicAuth              testedBasicAuth = new BasicAuth("sj_aws", "sj_aws_scopes");

        Boolean      expectedActive    = true;
        String       expectedIss       = "https://localhost:5001";
        Integer      expectedExpiresIn = 3600;
        List<String> expectedAud       = List.of("sj_aws", "https://localhost:5001/resources");
        String       expectedClientId  = "sj.aws";
        String       expectedScope     = "sj sj.aws.ec2.upload_company_logo sj.aws.ses.send_mail";

        // Act
        IntrospectRawResponse actual = getOidcService()
            .callIntrospectEndpoint(testedToken.getAccessToken(), testedBasicAuth);

        // Assert
        assertAll(
            () -> assertThat(actual.getActive()).isEqualTo(expectedActive),
            () -> assertThat(actual.getIss()).isEqualTo(expectedIss),
            () -> assertThat(actual.getExp() - actual.getIat()).isEqualTo(expectedExpiresIn),
            () -> assertThat(actual.getAud()).isEqualTo(expectedAud),
            () -> assertThat(actual.getClientId()).isEqualTo(expectedClientId),
            () -> assertThat(actual.getScope()).isEqualTo(expectedScope)
        );
    }
}
