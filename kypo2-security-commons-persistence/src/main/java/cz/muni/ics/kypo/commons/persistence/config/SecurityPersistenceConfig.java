package cz.muni.ics.kypo.commons.persistence.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons.persistence.model", "cz.muni.ics.kypo.commons.persistence.repository"})
@EntityScan(basePackages = "cz.muni.ics.kypo.commons.persistence.model")
@EnableJpaRepositories(basePackages = "cz.muni.ics.kypo.commons.persistence.repository")
@PropertySource("file:${path.to.config.file}")
public class SecurityPersistenceConfig {
	private Logger LOG = LoggerFactory.getLogger(SecurityPersistenceConfig.class);
}
