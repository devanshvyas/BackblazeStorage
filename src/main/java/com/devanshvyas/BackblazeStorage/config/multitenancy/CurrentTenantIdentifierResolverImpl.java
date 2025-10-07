package com.devanshvyas.BackblazeStorage.config.multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver<String> {
    @Override
    public String resolveCurrentTenantIdentifier() {
        System.out.println("Resolving tenant identifier: " + AppTenantContext.getCurrentTenant());
        return AppTenantContext.getCurrentTenant();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}