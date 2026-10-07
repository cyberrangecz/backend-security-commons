package cz.cyberrange.platform.commons.security.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

class CustomAuthenticationEntryPointTest {

  private final MockHttpServletRequest request = new MockHttpServletRequest();
  private final MockHttpServletResponse response = new MockHttpServletResponse();
  private final BadCredentialsException failure = new BadCredentialsException("bad");

  @Test
  @DisplayName("commence_resolverHandlesFailure_leavesResponseToResolver")
  void commence_resolverHandlesFailure_leavesResponseToResolver() throws Exception {
    HandlerExceptionResolver resolver = mock(HandlerExceptionResolver.class);
    when(resolver.resolveException(any(), any(), any(), any())).thenReturn(new ModelAndView());

    new CustomAuthenticationEntryPoint(resolver).commence(request, response, failure);

    assertThat(response.getErrorMessage()).isNull();
    assertThat(response.getStatus()).isEqualTo(200);
  }

  @Test
  @DisplayName("commence_resolverHandlesNothing_sendsUnauthorizedWithFailureMessage")
  void commence_resolverHandlesNothing_sendsUnauthorizedWithFailureMessage() throws Exception {
    HandlerExceptionResolver resolver = mock(HandlerExceptionResolver.class);

    new CustomAuthenticationEntryPoint(resolver).commence(request, response, failure);

    assertThat(response.getStatus()).isEqualTo(401);
    assertThat(response.getErrorMessage()).isEqualTo("bad");
  }

  @Test
  @DisplayName("commence_noResolver_sendsUnauthorized")
  void commence_noResolver_sendsUnauthorized() throws Exception {
    new CustomAuthenticationEntryPoint(null).commence(request, response, failure);

    assertThat(response.getStatus()).isEqualTo(401);
  }

  @Test
  @DisplayName("commence_resolverRethrowsAccessDenied_sendsUnauthorized")
  void commence_resolverRethrowsAccessDenied_sendsUnauthorized() throws Exception {
    HandlerExceptionResolver resolver = mock(HandlerExceptionResolver.class);
    when(resolver.resolveException(any(), any(), any(), any()))
        .thenThrow(new AccessDeniedException("denied"));

    new CustomAuthenticationEntryPoint(resolver).commence(request, response, failure);

    assertThat(response.getStatus()).isEqualTo(401);
  }
}
