package cz.cyberrange.platform.commons.security.config;

import cz.cyberrange.platform.commons.security.impl.UserInfoAuthenticationProvider;
import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Component
public class WebSocketSecurityPostProcessor implements BeanPostProcessor {

  @Autowired private UserInfoAuthenticationProvider authProvider;

  @Value("#{'${cors.allowed.origins:#{*}}'.split(',')}")
  private List<String> corsAllowedOrigins;

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (bean instanceof WebSocketConfigurer) {
      return createSecuredWebSocketConfigurer((WebSocketConfigurer) bean);
    }
    return bean;
  }

  private WebSocketConfigurer createSecuredWebSocketConfigurer(WebSocketConfigurer original) {
    return new WebSocketConfigurer() {
      @Override
      public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        SecuredWebSocketHandlerRegistry securedRegistry =
            new SecuredWebSocketHandlerRegistry(
                registry, authProvider, corsAllowedOrigins.toArray(new String[0]));

        // Use reflection or a custom approach to ensure the original configurer
        // uses the secured registry
        original.registerWebSocketHandlers(securedRegistry);
      }
    };
  }
}
