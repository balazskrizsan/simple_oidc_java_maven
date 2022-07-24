package com.kbalazsworks.simple_oidc.factories;

import okhttp3.OkHttpClient;

public interface IOkHttpFactory
{
    OkHttpClient createOkHttpClient();
}
