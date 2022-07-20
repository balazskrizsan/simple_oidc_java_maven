package com.kbalazsworks.simple_oidc.common.services;

import lombok.RequiredArgsConstructor;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

@Service
@RequiredArgsConstructor
public class SslService
{
    private final ApplicationProperties applicationProperties;

    private KeyStore keyStore(String file, char[] password) throws Exception
    {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        File     key      = ResourceUtils.getFile(file);
        try (InputStream in = new FileInputStream(key))
        {
            keyStore.load(in, password);
        }

        return keyStore;
    }

    public SSLContext getSslContext() throws Exception
    {
        char[] password = applicationProperties.getSimpleOidcSdkCertP12KeystoreFilePassword().toCharArray();

        return SSLContextBuilder
            .create()
            .loadKeyMaterial(
                keyStore(applicationProperties.getSimpleOidcSdkCertP12KeystoreFilePath(), password),
                password
            )
            .loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
    }
}
