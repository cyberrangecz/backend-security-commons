package cz.muni.ics.kypo.commons.rest.config;

import cz.muni.ics.kypo.commons.facade.config.SecurityFacadeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * @author Jan Duda & Pavel Seda
 */
@Configuration
@Import({SecurityFacadeConfiguration.class, SwaggerConfig.class})
@ComponentScan("cz.muni.ics.kypo.commons.rest")
public class WebConfigRestSecurityCommons {

    // To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigValues() {
        PropertySourcesPlaceholderConfigurer confPropertyPlaceholder = new PropertySourcesPlaceholderConfigurer();
        confPropertyPlaceholder.setIgnoreUnresolvablePlaceholders(true);
        return confPropertyPlaceholder;
    }

    private static final Logger LOG = LoggerFactory.getLogger(WebConfigRestSecurityCommons.class);

}
