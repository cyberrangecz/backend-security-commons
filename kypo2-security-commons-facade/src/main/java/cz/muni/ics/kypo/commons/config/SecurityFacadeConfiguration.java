package cz.muni.ics.kypo.commons.config;

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
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons.facade", "cz.muni.ics.kypo.commons.mapping"})
public class SecurityFacadeConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityFacadeConfiguration.class);

    @Bean
    public ModelMapper modelMapper() {
        LOG.debug("modelMapper()");
        return new ModelMapper();
    }

}
