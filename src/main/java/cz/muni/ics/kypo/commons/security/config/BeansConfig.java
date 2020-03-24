package cz.muni.ics.kypo.commons.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class BeansConfig {

    @Value("${user-and-group-server.uri}")
    private String userAndGroupEndpoint;

    /**
     * Bean creates rest template.
     *
     * @return the rest template
     */
    @Bean("kypoSecurityCommonsRestTemplate")
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(userAndGroupEndpoint));
        return restTemplate;
    }

    @Bean("kypoSecurityCommonsObjectMapper")
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }

    /**
     * Bean creates property sources placeholder configurer that resolves ${...} placeholders within bean definition
     * property values and @Value annotations against the current Spring Environment and its set of PropertySources.
     *
     * @return the property sources placeholder configurer
     */
    @Bean
    @Primary
    public static PropertySourcesPlaceholderConfigurer propertyConfigValues() {
        PropertySourcesPlaceholderConfigurer confPropertyPlaceholder = new PropertySourcesPlaceholderConfigurer();
        confPropertyPlaceholder.setIgnoreUnresolvablePlaceholders(true);
        return confPropertyPlaceholder;
    }


}
