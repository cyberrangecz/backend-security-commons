package cz.muni.ics.kypo.commons.security.config;

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
    @Bean(name = "kypoSecurityCommonsRestTemplate")
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(userAndGroupEndpoint));
        return restTemplate;
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
