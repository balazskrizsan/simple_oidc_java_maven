package com.kbalazsworks.simple_oidc.factories;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.util.concurrent.TimeUnit;

@Log4j2
public class OkHttpFactory implements IOkHttpFactory
{
   private static final X509TrustManager TRUST_ALL_CERTS = new X509TrustManager() {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[] {};
        }
    };

    @SneakyThrows
    public OkHttpClient createOkHttpClient()
    {
        return createOkHttpClient(true);
    }

    @SneakyThrows
    public OkHttpClient createOkHttpClient(boolean isHttps)
    {
        SSLContext sslContext = SSLContext.getInstance("SSL");;
        if (isHttps)
        {
            sslContext.init(null, new TrustManager[]{TRUST_ALL_CERTS}, new java.security.SecureRandom());
        }

        log.info("OkHttpClient created");

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        if (isHttps)
        {
            builder = builder.sslSocketFactory(sslContext.getSocketFactory(), TRUST_ALL_CERTS);
        }

        return builder
            .readTimeout(1000, TimeUnit.MILLISECONDS)
            .writeTimeout(1000, TimeUnit.MILLISECONDS)
            .build();
    }
}
