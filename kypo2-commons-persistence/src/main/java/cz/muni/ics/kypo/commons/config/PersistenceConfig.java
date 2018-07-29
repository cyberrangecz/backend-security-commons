package cz.muni.ics.kypo.commons.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "cz.muni.ics.kypo.commons.repository")
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons"})
@EntityScan(basePackages = "cz.muni.ics.kypo.commons.persistence.model")
@PropertySource("file:${path-to-config-file}")
public class PersistenceConfig {
}
