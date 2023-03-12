package com.kbalazsworks.simple_oidc.factories;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.util.concurrent.TimeUnit;

@Log4j2
public class OkHttpFactory implements IOkHttpFactory
{
    private static final X509TrustManager TRUST_ALL_CERTS = new X509TrustManager()
    {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
        {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
        {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers()
        {
            return new java.security.cert.X509Certificate[]{};
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
        SSLContext sslContext = SSLContext.getInstance("SSL");
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

        builder = builder
//            .connectTimeout(10000, TimeUnit.MILLISECONDS)
//            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .callTimeout(10000, TimeUnit.MILLISECONDS)
//            .writeTimeout(5000, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true);
//
//        builder.networkInterceptors().add(chain -> {
//            Request request1 = chain.request();
//
//            // try the request
//            Response response = chain.proceed(request1);
//
//            int tryCount = 0;
//            while (!response.isSuccessful() && tryCount < 10)
//            {
//                log.error("OkHttpCustom - network intercept - Request is not successful - " + tryCount);
//
//                tryCount++;
//
//                // retry the request
//                response.close();
//                response = chain.proceed(request1);
//            }
//
//            // otherwise just pass the original response on
//            return response;
//        });
//
//        builder.interceptors().add((Interceptor.Chain chain) -> {
//            log.error("API call");
//            Request request1 = chain.request();
//            log.error("API call2");
//            int tryCount = 0;
//
//            // try the request
//            Response response = chain.proceed(request1);
//
//            while (!response.isSuccessful() && tryCount < 10)
//            {
//                log.error("OkHttpCustom - intercept - Request is not successful - " + tryCount);
//
//                tryCount++;
//
//                // retry the request
//                response.close();
//                response = chain.proceed(request1);
//            }
//
//            // otherwise just pass the original response on
//            return response;
//        });

        return builder.build();
    }
}
