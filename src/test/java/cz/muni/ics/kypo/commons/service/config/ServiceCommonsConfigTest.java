package cz.muni.ics.kypo.commons.service.config;

import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;

/**
 *
 * @author Dominik Pilar (445537)
 *
 */
@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = "cz.muni.ics.kypo.commons.persistence.model")
@EnableJpaRepositories(basePackages = "cz.muni.ics.kypo.commons.persistence.repository")
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons.service.impl", "cz.muni.ics.kypo.commons.service.interfaces"})
@TestPropertySource(locations="classpath:application.properties")
public class ServiceCommonsConfigTest {

}
