package cz.muni.ics.kypo.commons.security.config;

import com.google.gson.JsonObject;
import cz.muni.ics.kypo.commons.security.enums.AuthenticatedUserOIDCItems;
import cz.muni.ics.kypo.commons.security.enums.SpringProfiles;
import cz.muni.ics.kypo.commons.security.mapping.UserInfoDTO;
import org.mitre.oauth2.introspectingfilter.service.IntrospectionAuthorityGranter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class is annotated with {@link Component}, so its mark as candidates for auto-detection when using annotation-based configuration and classpath scanning.
 * This class is responsible for returning a set of Spring Security GrantedAuthority objects to be assigned to the token service's resulting <i>Authentication</i> object.
 *
 * @author Pavel Seda
 * @author Dominik Pilar
 */
@Component
@PropertySource("file:${path.to.config.file}")
public class CustomAuthorityGranter {


    private static final String USER_INFO_ENDPOINT = "/users/info";

    @Value("${user-and-group-server.uri}")
    private String userAndGroupUrl;

    /**
     * The custom authority granter in production mode.
     */
    @Profile(SpringProfiles.PROD)
    @Component
    public class ProductionCustomAuthorityGranter implements IntrospectionAuthorityGranter {

        private final Logger LOG = LoggerFactory.getLogger(ProductionCustomAuthorityGranter.class);

        @Autowired
        private HttpServletRequest servletRequest;

        private RestTemplate restTemplate;

        /**
         * Instantiates a new ProductionCustomAuthorityGranter.
         *
         * @param restTemplate the rest template
         */
        @Autowired
        public ProductionCustomAuthorityGranter(@Qualifier(value = "kypoSecurityCommonsRestTemplate") RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        @Override
        public List<GrantedAuthority> getAuthorities(JsonObject introspectionResponse) {
            String login = introspectionResponse.get(AuthenticatedUserOIDCItems.SUB.getName()).getAsString();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", servletRequest.getHeader("Authorization"));
            HttpEntity<String> entity = new HttpEntity<>(headers);
            String userAndGroupUserInfoUri = userAndGroupUrl + USER_INFO_ENDPOINT;
            try {
                ResponseEntity<UserInfoDTO> response =
                        restTemplate.exchange(userAndGroupUserInfoUri, HttpMethod.GET, entity, UserInfoDTO.class);
                Assert.notEmpty(response.getBody().getRoles(), "No roles for user with login " + login);
                return response.getBody().getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRoleType()))
                        .collect(Collectors.toList());
            } catch (HttpClientErrorException ex) {
                throw new SecurityException("Error while getting info about logged in user: " + ex.getStatusCode());
            }
        }
    }

    /**
     * The custom authority granter in developer mode.
     */
    @Profile(SpringProfiles.DEV)
    @Component
    public class DevCustomAuthorityGranter implements IntrospectionAuthorityGranter {

        @Value("#{'${spring.profiles.dev.roles}'.split(',')}")
        private Set<String> roles;

        /**
         * Instantiates a new DevCustomAuthorityGranter.
         */
        @Autowired
        public DevCustomAuthorityGranter() {
        }

        @Override
        public List<GrantedAuthority> getAuthorities(JsonObject introspectionResponse) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            String role = roles.iterator().next();
            if (role.equals("") || role.equals("${spring.profiles.dev.roles}")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER_AND_GROUP_GUEST"));
            } else {
                for (String r : roles) {
                    authorities.add(new SimpleGrantedAuthority(r.toUpperCase()));
                }
            }
            return authorities;
        }
    }

}
