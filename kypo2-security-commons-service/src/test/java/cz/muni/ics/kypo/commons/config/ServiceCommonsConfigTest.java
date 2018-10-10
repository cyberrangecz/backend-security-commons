package cz.muni.ics.kypo.commons.rest.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
/**
 *
 * @author Dominik Pilar (445537)
 *
 */
@Configuration
@EntityScan(basePackages = "cz.muni.ics.kypo.commons.persistence.model")
@EnableJpaRepositories(basePackages = "cz.muni.ics.kypo.commons.persistence.repository")
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons.service"})
public class ServiceCommonsConfigTest {

}
