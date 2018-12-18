package cz.muni.ics.kypo.commons.facade.config;

import cz.muni.ics.kypo.commons.service.config.SecurityServiceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Pavel Seda
 */
@Configuration
@EnableTransactionManagement
@Import(SecurityServiceConfig.class)
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons.facade.impl", "cz.muni.ics.kypo.commons.facade.interfaces",
        "cz.muni.ics.kypo.commons.facade.mapping"})
public class SecurityFacadeConfiguration {

}
