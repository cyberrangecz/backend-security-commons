package cz.cyberrange.platform.commons.security.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

@Configuration
public class DefaultExceptionResolverConfig {

  /**
   * Provides a default HandlerExceptionResolver that can be overridden by projects by defining
   * their own HandlerExceptionResolver bean.
   */
  @Bean
  @ConditionalOnMissingBean(HandlerExceptionResolver.class)
  public HandlerExceptionResolver defaultHandlerExceptionResolver() {
    return new DefaultHandlerExceptionResolver() {};
  }
}
