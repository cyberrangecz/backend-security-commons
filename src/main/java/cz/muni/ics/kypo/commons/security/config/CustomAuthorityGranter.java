package cz.muni.ics.kypo.commons.security.config;


import com.google.gson.JsonObject;
import cz.muni.ics.kypo.commons.security.UserSecurity;
import org.mitre.oauth2.introspectingfilter.service.IntrospectionAuthorityGranter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomAuthorityGranter implements IntrospectionAuthorityGranter {

    private static Logger LOG = LoggerFactory.getLogger(CustomAuthorityGranter.class);

    private RestTemplate restTemplate;

    @Autowired
    public CustomAuthorityGranter(RestTemplate template) {
        this.restTemplate = template;
    }

    @Override
    public List<GrantedAuthority> getAuthorities(JsonObject introspectionResponse) {
        String login = introspectionResponse.get("sub").getAsString();
        ResponseEntity<UserSecurity> response = restTemplate.getForEntity("/kypo2-rest-training/api/v1/users/info", UserSecurity.class);
        if (response.getStatusCode().isError()) {
            throw new SecurityException("Logged in user with sub " + login + " could not be found in database");
        }
        return response.getBody().getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleType()))
                .collect(Collectors.toList());
    }
}
