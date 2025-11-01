package cz.cyberrange.platform.commons.security.config;

import cz.cyberrange.platform.commons.security.AuthorityGranter;
import cz.cyberrange.platform.commons.security.config.constants.StringConstants;
import cz.cyberrange.platform.commons.security.impl.CustomAuthenticationEntryPoint;
import cz.cyberrange.platform.commons.security.impl.UserInfoAuthenticationProvider;
import cz.cyberrange.platform.commons.security.impl.UserInfoValidator;
import cz.cyberrange.platform.commons.webclient.config.WebClientConfigSecurityCommons;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * Configuration of Spring Security beans for resource server, supporting both production and
 * developer modes.
 *
 * <p>This class sets up authentication, authorization, CORS, CSRF, and exception handling for the
 * application. It also configures WebSocket security and integrates with custom authentication
 * providers and entry points.
 *
 * <ul>
 *   <li>Configures stateless session management and CORS policies.
 *   <li>Defines beans for authentication manager, authentication provider, and security filter
 *       chain.
 *   <li>Handles CSRF for Bearer token requests and exposes custom authentication entry point.
 *   <li>Sets up allowed origins, headers, and methods for CORS.
 *   <li>Integrates WebSocket security configuration.
 * </ul>
 */
@Configuration
@Import(WebClientConfigSecurityCommons.class)
@EnableMethodSecurity
@EnableWebSecurity
@ComponentScan(
    basePackages = {
      "cz.cyberrange.platform.commons.security.config",
      "cz.cyberrange.platform.commons.security.impl"
    })
public class ResourceServerSecurityConfig {

  private static final List<String> ALLOWED_METHODS =
      List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");

  private static final List<String> ALLOWED_HEADERS =
      List.of("content-type", "authorization", "x-auth-token");

  private static final List<String> EXPOSED_HEADERS = List.of("authorization");

  /** Validator for user information used in authentication. */
  private final UserInfoValidator userInfoValidator;

  /** Grants authorities to authenticated users. */
  private final AuthorityGranter authorityGranter;

  /** List of allowed origins for CORS configuration, injected from properties. */
  @Value("#{'${cors.allowed.origins:#{*}}'.split(',')}")
  private List<String> corsAllowedOrigins;

  /** Server servlet context path, injected from properties. */
  @Value("${server.servlet.context-path:}")
  private String contextPath;

  /**
   * Instantiates a new ResourceServerSecurityConfig.
   *
   * @param userInfoValidator Validator for user information used in authentication.
   * @param authorityGranter Grants authorities to authenticated users.
   */
  public ResourceServerSecurityConfig(
      UserInfoValidator userInfoValidator, AuthorityGranter authorityGranter) {
    this.userInfoValidator = userInfoValidator;
    this.authorityGranter = authorityGranter;
  }

  /**
   * Provides the authentication manager bean.
   *
   * @param config Spring authentication configuration
   * @return AuthenticationManager instance
   * @throws Exception if authentication manager cannot be created
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  /**
   * Provides the custom authentication provider bean.
   *
   * @return UserInfoAuthenticationProvider instance
   */
  @Bean
  public UserInfoAuthenticationProvider userInfoAuthenticationProvider() {
    return new UserInfoAuthenticationProvider(authorityGranter, userInfoValidator);
  }

  /**
   * Completely bypass Spring Security for WebSocket endpoints. This allows the WebSocket handshake
   * to proceed without interference.
   *
   * @return WebSecurityCustomizer that ignores WebSocket paths
   */
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    String websocketPath =
        (contextPath.isEmpty() ? "" : contextPath) + "/guacamole/api/v1/websocket/**";
    return (web) -> web.ignoring().requestMatchers(websocketPath);
  }

  /**
   * Configures the security filter chain for HTTP requests.
   *
   * @param http HttpSecurity configuration
   * @param authenticationManager Authentication manager bean
   * @param handlerExceptionResolver Exception resolver for authentication entry point
   * @return SecurityFilterChain instance
   * @throws Exception if filter chain cannot be built
   */
  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http,
      AuthenticationManager authenticationManager,
      HandlerExceptionResolver handlerExceptionResolver)
      throws Exception {
    http.sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .cors(cors -> cors.configure(http))
        .csrf(
            csrf ->
                csrf.ignoringRequestMatchers(
                    request -> {
                      String auth = request.getHeader(StringConstants.AUTH_HEADER_KEY);
                      return auth != null && auth.startsWith(StringConstants.BEARER_TOKEN_PREFIX);
                    }))
        .addFilterBefore(
            new BearerTokenAuthenticationFilter(authenticationManager),
            UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(
            authz ->
                authz
                    .requestMatchers("/webjars/**", "/microservices")
                    .permitAll()
                    .requestMatchers("/websocket/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(
            ex ->
                ex.authenticationEntryPoint(
                    this.customAuthenticationEntryPoint(handlerExceptionResolver)));

    return http.build();
  }

  /**
   * Provides the CORS filter bean for HTTP requests.
   *
   * @return CorsFilter instance
   */
  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(false);
    config.setMaxAge(3600L);
    config.setExposedHeaders(EXPOSED_HEADERS);
    config.setAllowedOrigins(Collections.unmodifiableList(corsAllowedOrigins));
    config.setAllowedHeaders(ALLOWED_HEADERS);
    config.setAllowedMethods(ALLOWED_METHODS);
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

  /**
   * Provides the custom authentication entry point bean.
   *
   * @param handlerExceptionResolver Exception resolver for authentication failures
   * @return AuthenticationEntryPoint instance
   */
  @Bean
  public AuthenticationEntryPoint customAuthenticationEntryPoint(
      HandlerExceptionResolver handlerExceptionResolver) {
    return new CustomAuthenticationEntryPoint(handlerExceptionResolver);
  }
}
