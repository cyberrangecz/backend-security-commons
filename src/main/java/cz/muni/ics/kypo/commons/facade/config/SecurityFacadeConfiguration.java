package cz.muni.ics.kypo.commons.facade.config;

import cz.muni.ics.kypo.commons.service.config.SecurityServiceConfig;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@Import(SecurityServiceConfig.class)
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons.facade.impl", "cz.muni.ics.kypo.commons.facade.interfaces",
        "cz.muni.ics.kypo.commons.facade.mapping"})
public class SecurityFacadeConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityFacadeConfiguration.class);

    @Bean
    public ModelMapper modelMapper() {
        LOG.debug("modelMapper()");
        return new ModelMapper();
    }

}
