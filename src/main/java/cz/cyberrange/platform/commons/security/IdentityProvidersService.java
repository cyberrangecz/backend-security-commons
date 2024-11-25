package cz.cyberrange.platform.commons.security;


import cz.cyberrange.platform.commons.security.model.WellKnownOpenIDConfiguration;

public interface IdentityProvidersService {
    WellKnownOpenIDConfiguration getIdentityProviderConfiguration(String provider);
}
