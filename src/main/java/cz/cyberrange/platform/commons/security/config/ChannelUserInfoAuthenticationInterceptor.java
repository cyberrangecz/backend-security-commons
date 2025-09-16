package cz.cyberrange.platform.commons.security.config;

import cz.cyberrange.platform.commons.security.impl.UserInfoAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Intercepts messages on the channel to enforce security by validating Bearer tokens from the
 * Authorization header using the configured UserAndGroup service. Connections without valid tokens
 * are blocked.
 *
 * @param userInfoAuthenticationProvider Provider for authenticating users based on Bearer tokens.
 */
@Slf4j
record ChannelUserInfoAuthenticationInterceptor(
    UserInfoAuthenticationProvider userInfoAuthenticationProvider) implements ChannelInterceptor {

  /**
   * Constructs a ChannelSecurityInterceptor with the specified authentication provider.
   *
   * @param userInfoAuthenticationProvider the provider used to authenticate Bearer tokens
   */
  ChannelUserInfoAuthenticationInterceptor {}

  /**
   * @param message authorized message
   * @param channel communication channel
   * @return message with user from auth added in header
   * @throws AuthenticationException when the token is invalid or absent
   */
  @Override
  public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    String authHeader = accessor.getFirstNativeHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);

      Authentication authentication =
          userInfoAuthenticationProvider.authenticate(
              new org.springframework.security.oauth2.server.resource.authentication
                  .BearerTokenAuthenticationToken(token));
      accessor.setUser(authentication);
    } else {
      throw new InternalAuthenticationServiceException("Unable to parse user info response.");
    }

    return message;
  }
}
