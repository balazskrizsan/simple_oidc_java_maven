package com.kbalazsworks.services;

import com.kbalazsworks.test_helpers.AbstractTest;
import lombok.SneakyThrows;
import org.junit.Test;

import java.security.PublicKey;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenService_isVerifiedTest extends AbstractTest
{
    @Test
    @SneakyThrows
    public void validKeyData_returnTrue()
    {
        // Arrange
        PublicKey testedPublicKey  = getValidPublicKey();
        byte[]    testedSignedData = getValidSignedData();
        byte[]    testedSignature  = getValidSignature();

        // Act
        Boolean actual = getTokenService().isVerified(testedPublicKey, testedSignedData, testedSignature);

        // Assert
        assertThat(actual).isTrue();
    }
}
