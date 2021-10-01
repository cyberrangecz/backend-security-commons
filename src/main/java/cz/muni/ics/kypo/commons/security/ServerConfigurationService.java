package cz.muni.ics.kypo.commons.security;

import cz.muni.ics.kypo.commons.security.model.WellKnownOpenIDConfiguration;

public interface ServerConfigurationService {
    WellKnownOpenIDConfiguration getServerConfiguration(String var1);
}
