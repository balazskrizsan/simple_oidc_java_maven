package com.kbalazsworks.simple_oidc;

import com.google.inject.AbstractModule;
import com.kbalazsworks.simple_oidc.factories.SystemFactory;
import com.kbalazsworks.simple_oidc.services.CommunicationService;
import com.kbalazsworks.simple_oidc.services.GrantStoreService;
import com.kbalazsworks.simple_oidc.services.HttpClientService;
import com.kbalazsworks.simple_oidc.services.ICommunicationService;
import com.kbalazsworks.simple_oidc.services.IGrantStoreService;
import com.kbalazsworks.simple_oidc.services.IJwtValidationService;
import com.kbalazsworks.simple_oidc.services.IResponseValidatorService;
import com.kbalazsworks.simple_oidc.services.ISmartTokenStoreService;
import com.kbalazsworks.simple_oidc.services.IValidationService;
import com.kbalazsworks.simple_oidc.services.JwtValidationService;
import com.kbalazsworks.simple_oidc.services.ResponseValidatorService;
import com.kbalazsworks.simple_oidc.services.SmartTokenStoreService;
import com.kbalazsworks.simple_oidc.services.ValidationService;

public class DiConfigModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(SystemFactory.class).asEagerSingleton();
        bind(IValidationService.class).to(ValidationService.class).asEagerSingleton();
        bind(JwtValidationService.class).asEagerSingleton();
        bind(IGrantStoreService.class).to(GrantStoreService.class).asEagerSingleton();
        bind(ISmartTokenStoreService.class).to(SmartTokenStoreService.class).asEagerSingleton();
        bind(ICommunicationService.class).to(CommunicationService.class).asEagerSingleton();
        bind(HttpClientService.class).asEagerSingleton();
        bind(IResponseValidatorService.class).to(ResponseValidatorService.class).asEagerSingleton();
        bind(IJwtValidationService.class).to(JwtValidationService.class).asEagerSingleton();
    }
}
