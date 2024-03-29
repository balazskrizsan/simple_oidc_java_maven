package com.kbalazsworks.simple_oidc.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbalazsworks.simple_oidc.entities.BasicAuth;
import com.kbalazsworks.simple_oidc.exceptions.OidcApiException;
import com.kbalazsworks.simple_oidc.factories.IOkHttpFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Log4j2
public class OidcHttpClientService
{
    private final boolean isHttps;
    private final IOkHttpFactory okHttpFactory;

    private static final ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private OkHttpClient getClient()
    {
        return okHttpFactory.createOkHttpClient(isHttps); // @todo create bean
    }

    // @todo: test
    public <T> @NonNull T get(@NonNull String url, @NonNull Class<T> mapperClass) throws OidcApiException
    {
        return get(url, new HashMap<>(), new HashMap<>(), mapperClass);
    }

    // @todo: test
    public <T> @NonNull T get(
        @NonNull String url,
        @NonNull Map<String, String> queryParams,
        @NonNull Map<String, String> headers,
        @NonNull Class<T> mapperClass
    )
    throws OidcApiException
    {
        var urlBuilder = HttpUrl.parse(url).newBuilder();

        if (queryParams.size() > 0)
        {
            queryParams.forEach(urlBuilder::addQueryParameter);
        }

        String          builtUrl = urlBuilder.build().toString();
        Request.Builder requestBuilder  = new Request.Builder().url(builtUrl).get();

        if (headers.size() > 0)
        {
            headers.forEach(requestBuilder::addHeader);
        }

        Request request  = requestBuilder.build();

        try
        {
            String body = getClient().newCall(request).execute().body().string();

            return objectMapper.readValue(body, mapperClass);
        }
        catch (Exception e)
        {
            log.error("GET Response error: {}", e.getMessage(), e);

            throw new OidcApiException("GET Response error");
        }
    }

    // @todo: test
    public <T> @NonNull T post(
        @NonNull String url,
        @NonNull Map<String, String> postData,
        @NonNull Class<T> mapperClass
    ) throws OidcApiException
    {
        return post(url, postData, mapperClass, null);
    }

    // @todo: test
    public <T> T post(
        @NonNull String url,
        @NonNull Map<String, String> postData,
        @NonNull Class<T> mapperClass,
        @Nullable BasicAuth basicAuth
    ) throws OidcApiException
    {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        if (postData.size() > 0)
        {
            postData.forEach(formBodyBuilder::add);
        }

        Request.Builder requestBuilder = new Request
            .Builder()
            .url(url)
            .post(formBodyBuilder.build())
            .header("Content-Type", "application/x-www-form-urlencoded");

        if (null != basicAuth)
        {
            requestBuilder.header(
                "Authorization",
                Credentials.basic(basicAuth.getUserName(), basicAuth.getPassword())
            );
        }

        Request request = requestBuilder.build();

        try
        {
            String body = getClient().newCall(request).execute().body().string();

            return objectMapper.readValue(body, mapperClass);
        }
        catch (Exception e)
        {
            log.error("GET Response error: {}", e.getMessage(), e);

            throw new OidcApiException("GET Response error");
        }
    }
}
