package com.kbalazsworks.simple_oidc.common.services;

import org.springframework.beans.factory.annotation.Value;

public class ApplicationProperties
{

    public String getSimpleOidcSdkCertP12KeystoreFilePath()
    {
        return "classpath:keystore/sjdev.p12";
    }

    public String getSimpleOidcSdkCertP12KeystoreFilePassword()
    {
        return "password";
    }
}
