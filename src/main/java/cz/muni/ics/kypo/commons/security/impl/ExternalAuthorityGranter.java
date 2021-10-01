package cz.muni.ics.kypo.commons.security.impl;

import cz.muni.ics.kypo.commons.security.AuthorityGranter;
import cz.muni.ics.kypo.commons.security.mapping.UserInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class is annotated with {@link Component}, so its mark as candidates for auto-detection when using annotation-based configuration and classpath scanning.
 * This class is responsible for returning a set of Spring Security GrantedAuthority objects to be assigned to the token service's resulting <i>Authentication</i> object.
 */
@Component
public class ExternalAuthorityGranter implements AuthorityGranter {

    private final Logger LOG = LoggerFactory.getLogger(ExternalAuthorityGranter.class);

    private final HttpServletRequest servletRequest;
    private final WebClient webClient;

    /**
     * Instantiates a new ProductionCustomAuthorityGranter.
     *
     * @param webClient the rest template
     */
    @Autowired
    public ExternalAuthorityGranter(@Qualifier(value = "userManagementServiceWebClientSecurityCommons") WebClient webClient,
                                    HttpServletRequest httpServletRequest) {
        this.webClient = webClient;
        this.servletRequest = httpServletRequest;
    }

    @Override
    public List<GrantedAuthority> getAuthorities(Object introspectionResponse) {
        String oidcToken = servletRequest.getHeader("Authorization");
        try {
            UserInfoDTO userInfoResponse = webClient
                    .get()
                    .uri("/users/info")
                    .header("Authorization", oidcToken)
                    .retrieve()
                    .bodyToMono(UserInfoDTO.class)
                    .block();
            return userInfoResponse.getRoles()
                    .stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRoleType()))
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException ex) {
            throw new SecurityException("Error while getting info about logged in user: " + ex.getStatusCode());
        }
    }
}
