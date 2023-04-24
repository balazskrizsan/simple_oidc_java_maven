package com.kbalazsworks.test_helpers.asserts;

import org.assertj.core.api.AbstractAssert;

import static com.kbalazsworks.test_helpers.JwtHelper.isJwtChecker;

public class JwtAssert extends AbstractAssert<JwtAssert, String>
{
    public JwtAssert(String actual)
    {
        super(actual, JwtAssert.class);
    }

    public static JwtAssert assertThat(String actual)
    {
        return new JwtAssert(actual);
    }

    public JwtAssert isJwt()
    {
        isNotNull();
        if (isJwtChecker(actual))
        {
            failWithMessage("Token must have exactly 2 period: %s", actual);
        }

        return this;
    }
}
