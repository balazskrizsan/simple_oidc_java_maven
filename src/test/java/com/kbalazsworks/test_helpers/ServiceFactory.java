package com.kbalazsworks.test_helpers;

import com.kbalazsworks.simple_oidc.factories.SystemFactory;
import com.kbalazsworks.simple_oidc.services.IGrantStoreService;
import com.kbalazsworks.simple_oidc.services.IJwtValidationService;
import com.kbalazsworks.simple_oidc.services.IResponseValidatorService;
import com.kbalazsworks.simple_oidc.services.ISmartTokenStoreService;
import com.kbalazsworks.simple_oidc.services.IValidationService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ServiceFactory
{
    private final IValidationService        validationService;
    private final SystemFactory             systemFactory;
    private final IResponseValidatorService responseValidatorService;
    private final IGrantStoreService        grantStoreService;
    private final ISmartTokenStoreService ISmartTokenStoreService;
    private final IJwtValidationService   IJwtValidationService;
}
