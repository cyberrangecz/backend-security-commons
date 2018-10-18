package cz.muni.ics.kypo.commons.security.config;


import com.google.gson.JsonObject;
import cz.muni.ics.kypo.commons.security.mapping.UserInfoDTO;
import org.mitre.oauth2.introspectingfilter.service.IntrospectionAuthorityGranter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomAuthorityGranter implements IntrospectionAuthorityGranter {

	private static Logger LOG = LoggerFactory.getLogger(CustomAuthorityGranter.class);
	private static final String USER_INFO_ENDPOINT = "/kypo2-rest-user-and-group/api/v1/users/info";

	@Autowired
	private HttpServletRequest servletRequest;

	@Value("${server.url}")
	private String serverUrl;

	@Autowired
	public CustomAuthorityGranter() {
	}

	@Override
	public List<GrantedAuthority> getAuthorities(JsonObject introspectionResponse) {
		String login = introspectionResponse.get("sub").getAsString().split("@")[0];
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", servletRequest.getHeader("Authorization"));
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<UserInfoDTO> response = restTemplate().exchange(serverUrl + USER_INFO_ENDPOINT, HttpMethod.GET, entity, UserInfoDTO.class);
		if (response.getStatusCode().isError()) {
			throw new SecurityException("Logged in user with sub " + login + " could not be found in database and has been created in database.");
		}
		Assert.notEmpty(response.getBody().getRoles(), "No roles for user with login "+ login);
		return response.getBody().getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRole_type()))
		.collect(Collectors.toList());
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
