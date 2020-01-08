package cz.muni.ics.kypo.commons.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeansConfig {

    /**
     * Bean creates rest template.
     *
     * @return the rest template
     */
    @Bean(name = "kypoSecurityCommonsRestTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Bean creates property sources placeholder configurer that resolves ${...} placeholders within bean definition
     * property values and @Value annotations against the current Spring Environment and its set of PropertySources.
     *
     * @return the property sources placeholder configurer
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigValues() {
        PropertySourcesPlaceholderConfigurer confPropertyPlaceholder = new PropertySourcesPlaceholderConfigurer();
        confPropertyPlaceholder.setIgnoreUnresolvablePlaceholders(true);
        return confPropertyPlaceholder;
    }


}
