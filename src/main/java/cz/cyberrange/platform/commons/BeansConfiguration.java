package cz.cyberrange.platform.commons;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
public class BeansConfiguration {

  /**
   * Bean creates a property sources placeholder configurer that resolves ${...} placeholders within
   * bean definition property values and @Value annotations against the current Spring Environment
   * and its set of PropertySources.
   *
   * @return the property sources placeholder configurer
   */
  @Bean
  @Primary
  public static PropertySourcesPlaceholderConfigurer propertyConfigValues() {
    PropertySourcesPlaceholderConfigurer confPropertyPlaceholder =
        new PropertySourcesPlaceholderConfigurer();
    confPropertyPlaceholder.setIgnoreUnresolvablePlaceholders(true);
    return confPropertyPlaceholder;
  }
}
