package cz.muni.ics.kypo.commons.rest.config;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "cz.muni.ics.kypo.commons.persistence.model")
@EnableJpaRepositories(basePackages = "cz.muni.ics.kypo.commons.persistence.repository")
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons.facade", "cz.muni.ics.kypo.commons.mapping", "cz.muni.ics.kypo.commons.service"})
public class FacadeTestConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(SecurityFacadeConfiguration.class);

	@Bean
	public ModelMapper modelMapper() {
		LOG.debug("modelMapper()");
		return new ModelMapper();
	}

}
