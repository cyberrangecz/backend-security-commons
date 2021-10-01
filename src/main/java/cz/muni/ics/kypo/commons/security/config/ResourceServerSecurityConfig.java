package cz.muni.ics.kypo.commons.security.config;

import cz.muni.ics.kypo.commons.security.AuthorityGranter;
import cz.muni.ics.kypo.commons.security.impl.CustomAuthenticationEntryPoint;
import cz.muni.ics.kypo.commons.security.impl.CustomCorsFilter;
import cz.muni.ics.kypo.commons.security.impl.DynamicServerConfigurationService;
import cz.muni.ics.kypo.commons.security.impl.UserInfoTokenService;
import cz.muni.ics.kypo.commons.webclient.config.WebClientConfigSecurityCommons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * Configuration of Spring Security beans in production and developer mode.
 */
@Configuration
@Import(WebClientConfigSecurityCommons.class)
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons.security.config", "cz.muni.ics.kypo.commons.security.impl"})
public class ResourceServerSecurityConfig extends ResourceServerConfigurerAdapter {

    private final CustomCorsFilter corsFilter;
    private final AuthorityGranter authorityGranter;
    private final IdentityProvidersConfig providersConfig;

    /**
     * Instantiates a new ResourceServerSecurityConfig.
     */
    @Autowired
    public ResourceServerSecurityConfig(CustomCorsFilter corsFilter,
                                        AuthorityGranter authorityGranter,
                                        IdentityProvidersConfig identityProvidersConfig) {
        this.corsFilter = corsFilter;
        this.authorityGranter = authorityGranter;
        this.providersConfig = identityProvidersConfig;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenServices(tokenServices());
        resources.authenticationEntryPoint(new CustomAuthenticationEntryPoint());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .addFilterBefore(corsFilter, BasicAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/webjars/**", "/microservices")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER);
    }

    @Bean
    public DynamicServerConfigurationService serverConfigurationService() {
        DynamicServerConfigurationService serverConfigurationService = new DynamicServerConfigurationService(providersConfig.getUserInfoEndpointsMapping());
        serverConfigurationService.setWhitelist(providersConfig.getSetOfIssuers());
        return serverConfigurationService;
    }

    @Bean
    public ResourceServerTokenServices tokenServices() {
        UserInfoTokenService tokenService = new UserInfoTokenService();
        tokenService.setServerConfigurationService(serverConfigurationService());
        tokenService.setCacheTokens(true);
        tokenService.setAuthorityGranter(authorityGranter);
        return tokenService;
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
