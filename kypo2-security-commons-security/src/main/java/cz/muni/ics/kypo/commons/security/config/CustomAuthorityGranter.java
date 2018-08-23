package cz.muni.ics.kypo.commons.security.config;


import com.google.gson.JsonObject;
import cz.muni.ics.kypo.commons.security.mapping.UserSecurity;
import org.mitre.oauth2.introspectingfilter.service.IntrospectionAuthorityGranter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomAuthorityGranter implements IntrospectionAuthorityGranter {

    private static Logger LOG = LoggerFactory.getLogger(CustomAuthorityGranter.class);

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public CustomAuthorityGranter() {
    }

    @Override
    public List<GrantedAuthority> getAuthorities(JsonObject introspectionResponse) {
        String login = introspectionResponse.get("sub").getAsString();
        OAuth2AuthenticationDetails auth = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", auth.getTokenType() + " " + auth.getTokenValue());
        HttpEntity<String> entity = new HttpEntity<>( null, headers);
        ResponseEntity<UserSecurity> response = restTemplate.exchange("/kypo2-users-and-groups/api/v1/users/info", HttpMethod.GET, entity, UserSecurity.class);
        if (response.getStatusCode().isError()) {
            throw new SecurityException("Logged in user with sub " + login + " could not be found in database and has been created in database.");
        }
        return response.getBody().getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleType()))
                .collect(Collectors.toList());
    }
}
