package cz.cyberrange.platform.commons.security.config;

import cz.cyberrange.platform.commons.security.impl.UserInfoAuthenticationProvider;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

public class SecuredWebSocketHandlerRegistry implements WebSocketHandlerRegistry {

  private final WebSocketHandlerRegistry delegate;
  private final WebsocketTokenValidationInterceptor authInterceptor;
  private final String[] allowedOrigins;

  public SecuredWebSocketHandlerRegistry(
      WebSocketHandlerRegistry delegate,
      UserInfoAuthenticationProvider authProvider,
      String[] allowedOrigins) {
    this.delegate = delegate;
    this.authInterceptor = new WebsocketTokenValidationInterceptor(authProvider);
    this.allowedOrigins = allowedOrigins;
  }

  public WebSocketHandlerRegistration addHandler(WebSocketHandler handler, String... paths) {
    // Automatically add authentication interceptor to every handler registration
    return delegate
        .addHandler(handler, paths)
        .addInterceptors(authInterceptor)
        .setAllowedOrigins(allowedOrigins);
  }
}
