package cz.muni.ics.kypo.commons.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@SpringBootApplication
@EnableSpringDataWebSupport
@Import({FacadeConfiguration.class, SwaggerConfig.class})
public class WebConfigRestSecurityCommons extends SpringBootServletInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(WebConfigRestSecurityCommons.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebConfigRestSecurityCommons.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(WebConfigRestSecurityCommons.class, args);
    }

    // REST settings

    /**
     * Provides localized messages.
     */
    @Bean
    public MessageSource messageSource() {
        LOG.debug("messageSource()");
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    @Primary
    public MappingJackson2HttpMessageConverter jacksonHTTPMessageConverter() {
        LOG.debug("jacksonHTTPMessageConverter()");
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(objectMapperForRestAPI());
        return jsonConverter;
    }

    @Bean(name = "objMapperRESTApi")
    @Primary
    public ObjectMapper objectMapperForRestAPI() {
        LOG.debug("objectMapperForRestAPI()");
        ObjectMapper obj = new ObjectMapper();
        obj.setPropertyNamingStrategy(snakeCase());
        return obj;
    }

    /**
     * Naming strategy for returned JSONs.
     *
     * @return Naming Strategy for JSON properties
     */
    @Bean(name = "properyNamingSnakeCase")
    public PropertyNamingStrategy snakeCase() {
        LOG.debug("properyNamingSnakeCase -> snakeCase()");
        return PropertyNamingStrategy.SNAKE_CASE;
    }
}
