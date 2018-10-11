package cz.muni.ics.kypo.commons.rest.config;

import cz.muni.ics.kypo.commons.persistence.config.SecurityPersistenceConfig;
import cz.muni.ics.kypo.commons.security.config.ResourceServerSecurityConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({SecurityPersistenceConfig.class, ResourceServerSecurityConfig.class})
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons.service"})
public class SecurityServiceConfig {
}
