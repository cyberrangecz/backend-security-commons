package cz.muni.ics.kypo.commons.rest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import cz.muni.ics.kypo.commons.facade.config.SecurityFacadeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
@Import({SecurityFacadeConfiguration.class, SwaggerConfig.class})
@ComponentScan("cz.muni.ics.kypo.commons.rest")
public class WebConfigRestSecurityCommons {

    private static final Logger LOG = LoggerFactory.getLogger(WebConfigRestSecurityCommons.class);

}
