package cz.cyberrange.platform.commons.security.config;

import cz.cyberrange.platform.commons.security.impl.UserInfoAuthenticationProvider;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
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

    try {
      URI uri = request.getURI();
      String query = uri.getQuery();

      if (query == null) {
        log.warn("No query parameters provided for WebSocket connection");
        sendErrorResponse(response, HttpStatus.BAD_REQUEST, "Missing query parameters");
        return false;
      }

      Map<String, String> queryParams =
          UriComponentsBuilder.fromUri(uri).build().getQueryParams().toSingleValueMap();

      String authToken = queryParams.get("authToken");

      if (authToken == null || authToken.isEmpty()) {
        log.warn("No authToken provided in query parameters");
        sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Missing authentication token");
        return false;
      }

      Authentication authentication =
          userInfoAuthenticationProvider.authenticate(
              new BearerTokenAuthenticationToken(authToken));

      attributes.put("authentication", authentication);
      log.info("WebSocket authentication successful for user: {}", authentication.getName());
      return true;

    } catch (AuthenticationServiceException e) {
      log.error("WebSocket authentication failed: {}", e.getMessage());
      sendErrorResponse(
          response,
          HttpStatus.UNAUTHORIZED,
          "Invalid authentication token: %s".formatted(e.getMessage()));
      return false;
    } catch (Exception e) {
      log.error("WebSocket handshake error: {}", e.getMessage());
      sendErrorResponse(
          response,
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Handshake failed: %s".formatted(e.getMessage()));
      return false;
    }
  }

  private void sendErrorResponse(ServerHttpResponse response, HttpStatus status, String message)
      throws IOException {
    response.setStatusCode(status);
    response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
    response.getBody().write(message.getBytes(StandardCharsets.UTF_8));
    response.getBody().flush();
  }

  @Override
  public void afterHandshake(
      ServerHttpRequest request,
      ServerHttpResponse response,
      WebSocketHandler wsHandler,
      Exception exception) {}
}
