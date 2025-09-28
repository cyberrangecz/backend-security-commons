package cz.cyberrange.platform.commons.security.config;

import cz.cyberrange.platform.commons.security.impl.UserInfoAuthenticationProvider;
import java.net.URI;
import java.util.Map;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

public class WebsocketTokenValidationInterceptor implements HandshakeInterceptor {
  private final UserInfoAuthenticationProvider userInfoAuthenticationProvider;

  public WebsocketTokenValidationInterceptor(
      UserInfoAuthenticationProvider userInfoAuthenticationProvider) {
    this.userInfoAuthenticationProvider = userInfoAuthenticationProvider;
  }

  @Override
  public boolean beforeHandshake(
      ServerHttpRequest request,
      ServerHttpResponse response,
      WebSocketHandler wsHandler,
      Map<String, Object> attributes)
      throws Exception {

    URI uri = request.getURI();
    String query = uri.getQuery();

    if (query == null) {
      return false; // No query parameters, reject connection
    }

    // Extract authToken from query parameters
    Map<String, String> queryParams =
        UriComponentsBuilder.fromUri(uri).build().getQueryParams().toSingleValueMap();

    String authToken = queryParams.get("authToken");

    Authentication authentication =
        userInfoAuthenticationProvider.authenticate(new BearerTokenAuthenticationToken(authToken));

    // Store the validated token in session attributes for later use if needed
    attributes.put("authToken", authToken);
    attributes.put("authenticated", true);
    return true;
  }

  @Override
  public void afterHandshake(
      ServerHttpRequest request,
      ServerHttpResponse response,
      WebSocketHandler wsHandler,
      Exception exception) {
    // Optional: Log successful handshake or perform cleanup
  }
}
