package cz.cyberrange.platform.commons.security.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.HandlerExceptionResolver;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final HandlerExceptionResolver handlerExceptionResolver;

  public CustomAuthenticationEntryPoint(HandlerExceptionResolver handlerExceptionResolver) {
    this.handlerExceptionResolver = handlerExceptionResolver;
  }

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    if (handlerExceptionResolver != null) {
      // it allows to use ResponseEntityExceptionHandler (ControllerAdvice) even for exceptions from
      // the filters
      handlerExceptionResolver.resolveException(request, response, null, authException);
    } else {
      // Fallback: send 401 Unauthorized directly
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
  }
}
