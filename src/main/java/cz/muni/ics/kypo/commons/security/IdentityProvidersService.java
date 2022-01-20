package cz.muni.ics.kypo.commons.security;


import cz.muni.ics.kypo.commons.security.model.WellKnownOpenIDConfiguration;

public interface IdentityProvidersService {
    WellKnownOpenIDConfiguration getIdentityProviderConfiguration(String provider);
}
