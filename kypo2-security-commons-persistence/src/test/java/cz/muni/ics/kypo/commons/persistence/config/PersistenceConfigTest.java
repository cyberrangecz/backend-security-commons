package cz.muni.ics.kypo.commons.persistence.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 *
 * @author Dominik Pilar (445537)
 *
 */
@Configuration
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons.persistence.model", "cz.muni.ics.kypo.commons.persistence.repository"})
@EntityScan(basePackages = "cz.muni.ics.kypo.commons.persistence.model", basePackageClasses = Jsr310JpaConverters.class)
@EnableJpaRepositories(basePackages = "cz.muni.ics.kypo.commons.persistence.repository")
public class PersistenceConfigTest {

}
