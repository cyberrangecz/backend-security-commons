package cz.cyberrange.platform.commons.security.config;

import cz.cyberrange.platform.commons.security.impl.UserInfoAuthenticationProvider;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configures security for the WebSocket message broker by registering a channel interceptor that
 * enforces authentication using Bearer tokens from the Authorization header.
 */
class MessageBrokerUserInfoAuthenticationConfigurer implements WebSocketMessageBrokerConfigurer {

  /** Provider for authenticating users based on Bearer tokens. */
  private final UserInfoAuthenticationProvider userInfoAuthenticationProvider;

  /**
   * Constructs a new MessageBrokerSecurityConfigurer with the specified authentication provider.
   *
   * @param userInfoAuthenticationProvider Provider for authenticating users based on Bearer tokens.
   */
  MessageBrokerUserInfoAuthenticationConfigurer(
      UserInfoAuthenticationProvider userInfoAuthenticationProvider) {
    this.userInfoAuthenticationProvider = userInfoAuthenticationProvider;
  }

  /**
   * Registers the ChannelUserInfoAuthenticationInterceptor to enforce security on inbound client
   * messages.
   *
   * @param registration Channel registration for inbound messages.
   */
  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(
        new ChannelUserInfoAuthenticationInterceptor(userInfoAuthenticationProvider));
  }
}
