package cz.muni.ics.kypo.commons.persistence.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.context.annotation.Configuration;

/**
 * @author Pavel Seda
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons.persistence.model", "cz.muni.ics.kypo.commons.persistence.repository"})
@Import(StartUpRunner.class)
@EntityScan(basePackages = "cz.muni.ics.kypo.commons.persistence.model")
@EnableJpaRepositories(basePackages = "cz.muni.ics.kypo.commons.persistence.repository")
@PropertySource("file:${path.to.config.file}")
public class SecurityPersistenceConfig {
}