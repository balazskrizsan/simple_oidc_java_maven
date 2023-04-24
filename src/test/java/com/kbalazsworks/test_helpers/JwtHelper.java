package com.kbalazsworks.test_helpers;

import org.apache.commons.lang3.StringUtils;

public class JwtHelper
{
    public static boolean isJwtChecker(String token)
    {
        if (!hasTokenExactPoints(token))
        {
            return false;
        }

        if (!isTokenStartsWitheyJ(token))
        {
            return false;
        }

        return true;
    }

    private static boolean hasTokenExactPoints(String token)
    {
        return StringUtils.countMatches(token, ".") != 2;
    }

    private static boolean isTokenStartsWitheyJ(String token)
    {
        return token.startsWith("eyJ");
    }
}
