package cz.muni.ics.kypo.commons.security.config;

import org.mitre.oauth2.introspectingfilter.IntrospectingTokenService;
import org.mitre.oauth2.introspectingfilter.service.IntrospectionConfigurationService;
import org.mitre.oauth2.introspectingfilter.service.impl.JWTParsingIntrospectionConfigurationService;
import org.mitre.oauth2.model.RegisteredClient;
import org.mitre.openid.connect.client.service.ClientConfigurationService;
import org.mitre.openid.connect.client.service.ServerConfigurationService;
import org.mitre.openid.connect.client.service.impl.DynamicServerConfigurationService;
import org.mitre.openid.connect.client.service.impl.StaticClientConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Configuration of Spring Security beans in production and developer mode.
 */
@Configuration
@Import(BeansConfig.class)
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = {"cz.muni.ics.kypo.commons.security"})
public class ResourceServerSecurityConfig extends ResourceServerConfigurerAdapter {

    @Value("#{'${kypo.idp.4oauth.issuers}'.split(',')}")
    private List<String> issuers;
    @Value("#{'${kypo.idp.4oauth.resource.clientIds}'.split(',')}")
    private List<String> clientIdsOfResources;
    @Value("#{'${kypo.idp.4oauth.resource.clientSecrets}'.split(',')}")
    private List<String> clientSecretResources;
    @Value("#{'${kypo.idp.4oauth.scopes}'.split(',')}")
    private Set<String> scopes;

    private CustomAuthorityGranter customAuthorityGranter;
    private CustomCorsFilter corsFilter;

    /**
     * Instantiates a new ResourceServerSecurityConfig.
     */
    @Autowired
    public ResourceServerSecurityConfig(CustomCorsFilter customCorsFilter, CustomAuthorityGranter customAuthorityGranter) {
        this.corsFilter = customCorsFilter;
        this.customAuthorityGranter = customAuthorityGranter;
    }

    @Bean
    public IntrospectionConfigurationService introspectionConfigurationService() {
        JWTParsingIntrospectionConfigurationService introspectionConfigurationService = new JWTParsingIntrospectionConfigurationService();
        introspectionConfigurationService.setServerConfigurationService(serverConfigurationService());
        introspectionConfigurationService.setClientConfigurationService(clientConfigurationService());
        return introspectionConfigurationService;
    }

    @Bean
    public ServerConfigurationService serverConfigurationService() {
        DynamicServerConfigurationService serverConfigurationService = new DynamicServerConfigurationService();
        serverConfigurationService.setWhitelist(issuers.stream()
                .map(String::trim)
                .collect(Collectors.toSet()));
        return serverConfigurationService;
    }

    @Bean
    public ClientConfigurationService clientConfigurationService() {
        Map<String, RegisteredClient> clients = new HashMap<>();
        for (int i = 0; i < issuers.size(); i++) {
            RegisteredClient client = new RegisteredClient();
            client.setClientId(clientIdsOfResources.get(i).trim());
            client.setClientSecret(clientSecretResources.get(i).trim());
            client.setScope(scopes);
            clients.put(issuers.get(i).trim(), client);
        }
        StaticClientConfigurationService clientConfigurationService = new StaticClientConfigurationService();
        clientConfigurationService.setClients(clients);
        return clientConfigurationService;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenServices(tokenServices());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .addFilterBefore(corsFilter, BasicAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs/**", "/webjars/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER);
    }

    @Bean
    public ResourceServerTokenServices tokenServices() {
        IntrospectingTokenService tokenService = new IntrospectingTokenService();
        tokenService.setIntrospectionConfigurationService(introspectionConfigurationService());
        tokenService.setCacheTokens(true);
        tokenService.setIntrospectionAuthorityGranter(customAuthorityGranter);
        return tokenService;
    }

}
