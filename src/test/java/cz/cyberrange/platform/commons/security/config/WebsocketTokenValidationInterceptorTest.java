package cz.cyberrange.platform.commons.security.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cz.cyberrange.platform.commons.security.impl.UserInfoAuthenticationProvider;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.TestingAuthenticationToken;

class WebsocketTokenValidationInterceptorTest {

  private UserInfoAuthenticationProvider authenticationProvider;
  private WebsocketTokenValidationInterceptor interceptor;
  private MockHttpServletResponse servletResponse;
  private ServletServerHttpResponse response;
  private final Map<String, Object> attributes = new HashMap<>();

  @BeforeEach
  void setUp() {
    authenticationProvider = mock(UserInfoAuthenticationProvider.class);
    interceptor = new WebsocketTokenValidationInterceptor(authenticationProvider);
    servletResponse = new MockHttpServletResponse();
    response = new ServletServerHttpResponse(servletResponse);
  }

  private ServletServerHttpRequest requestTo(String uri) {
    MockHttpServletRequest servletRequest = new MockHttpServletRequest();
    servletRequest.setRequestURI(URI.create(uri).getPath());
    return new ServletServerHttpRequest(servletRequest) {
      @Override
      public URI getURI() {
        return URI.create(uri);
      }
    };
  }

  @Test
  @DisplayName("beforeHandshake_noQuery_rejectsWithBadRequest")
  void beforeHandshake_noQuery_rejectsWithBadRequest() throws Exception {
    boolean accepted =
        interceptor.beforeHandshake(requestTo("http://host/ws"), response, null, attributes);

    assertThat(accepted).isFalse();
    assertThat(servletResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  @DisplayName("beforeHandshake_missingAuthToken_rejectsWithUnauthorized")
  void beforeHandshake_missingAuthToken_rejectsWithUnauthorized() throws Exception {
    boolean accepted =
        interceptor.beforeHandshake(
            requestTo("http://host/ws?other=1"), response, null, attributes);

    assertThat(accepted).isFalse();
    assertThat(servletResponse.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
  }

  @Test
  @DisplayName("beforeHandshake_validToken_acceptsAndStoresAuthentication")
  void beforeHandshake_validToken_acceptsAndStoresAuthentication() throws Exception {
    TestingAuthenticationToken authentication = new TestingAuthenticationToken("alice", "x");
    when(authenticationProvider.authenticate(any())).thenReturn(authentication);

    boolean accepted =
        interceptor.beforeHandshake(
            requestTo("http://host/ws?authToken=abc"), response, null, attributes);

    assertThat(accepted).isTrue();
    assertThat(attributes).containsEntry("authentication", authentication);
  }

  @Test
  @DisplayName("beforeHandshake_authenticationServiceFailure_rejectsWithUnauthorized")
  void beforeHandshake_authenticationServiceFailure_rejectsWithUnauthorized() throws Exception {
    when(authenticationProvider.authenticate(any()))
        .thenThrow(new AuthenticationServiceException("rejected"));

    boolean accepted =
        interceptor.beforeHandshake(
            requestTo("http://host/ws?authToken=abc"), response, null, attributes);

    assertThat(accepted).isFalse();
    assertThat(servletResponse.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    assertThat(servletResponse.getContentAsString()).contains("rejected");
  }

  @Test
  @DisplayName("beforeHandshake_unexpectedFailure_rejectsWithInternalServerError")
  void beforeHandshake_unexpectedFailure_rejectsWithInternalServerError() throws Exception {
    when(authenticationProvider.authenticate(any())).thenThrow(new IllegalStateException("boom"));

    boolean accepted =
        interceptor.beforeHandshake(
            requestTo("http://host/ws?authToken=abc"), response, null, attributes);

    assertThat(accepted).isFalse();
    assertThat(servletResponse.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
  }
}
