package com.kbalazsworks.simple_oidc.common.services;

import org.springframework.stereotype.Component;

@Component
public class SystemFactory
{
    public long getCurrentTimeMillis()
    {
        return System.currentTimeMillis();
    }
}
