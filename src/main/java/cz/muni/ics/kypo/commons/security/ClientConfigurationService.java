package cz.muni.ics.kypo.commons.security;

import cz.muni.ics.kypo.commons.security.model.ClientConfiguration;

public interface ClientConfigurationService {
    ClientConfiguration getClientConfiguration(String issuer);
}
