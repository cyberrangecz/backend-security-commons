package cz.muni.ics.kypo.commons.config;

import cz.muni.ics.kypo.commons.config.PersistenceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({PersistenceConfig.class})
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons.service"})
public class ServiceConfig {
}
