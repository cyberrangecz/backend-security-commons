package cz.cyberrange.platform.commons.security.config;

import cz.cyberrange.platform.commons.security.AuthorityGranter;
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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

/** Configuration of Spring Security beans in production and developer mode. */
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

  private final UserInfoValidator userInfoValidator;
  private final AuthorityGranter authorityGranter;

  @Value("#{'${cors.allowed.origins:#{*}}'.split(',')}")
  private List<String> corsAllowedOrigins;

  /** Instantiates a new ResourceServerSecurityConfig. */
  public ResourceServerSecurityConfig(
      UserInfoValidator userInfoValidator, AuthorityGranter authorityGranter) {
    this.userInfoValidator = userInfoValidator;
    this.authorityGranter = authorityGranter;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public UserInfoAuthenticationProvider userInfoAuthenticationProvider() {
    return new UserInfoAuthenticationProvider(authorityGranter, userInfoValidator);
  }

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http,
      AuthenticationManager authenticationManager,
      HandlerExceptionResolver handlerExceptionResolver)
      throws Exception {
    http.sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .cors(cors -> cors.configure(http))
        .csrf(AbstractHttpConfigurer::disable)
        .addFilterBefore(
            new BearerTokenAuthenticationFilter(authenticationManager),
            UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(
            authz ->
                authz
                    .requestMatchers("/webjars/**", "/microservices")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(
            ex ->
                ex.authenticationEntryPoint(
                    customAuthenticationEntryPoint(handlerExceptionResolver)));
    return http.build();
  }

  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(false);
    config.setMaxAge(3600L);
    config.setExposedHeaders(List.of("authorization"));
    config.setAllowedOrigins(Collections.unmodifiableList(corsAllowedOrigins));
    config.setAllowedHeaders(List.of("content-type", "authorization", "x-auth-token"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

  @Bean
  public AuthenticationEntryPoint customAuthenticationEntryPoint(
      HandlerExceptionResolver handlerExceptionResolver) {
    return new CustomAuthenticationEntryPoint(handlerExceptionResolver);
  }
}
