package cz.muni.ics.kypo.commons.rest.config;

import cz.muni.ics.kypo.commons.facade.config.SecurityFacadeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;

/**
 * @author Jan Duda & Pavel Seda
 */
@Configuration
@Import({SecurityFacadeConfiguration.class, SwaggerConfig.class})
@ComponentScan("cz.muni.ics.kypo.commons.rest")
public class WebConfigRestSecurityCommons {

    private static final Logger LOG = LoggerFactory.getLogger(WebConfigRestSecurityCommons.class);

}
