package cz.muni.ics.kypo.commons.security.config;


import com.google.gson.JsonObject;
import cz.muni.ics.kypo.commons.security.mapping.UserInfoDTO;
import org.mitre.oauth2.introspectingfilter.service.IntrospectionAuthorityGranter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
	private RestTemplate restTemplate = new RestTemplate();
	private static final String USER_INFO_ENDPOINT = "/kypo2-users-and-groups/api/v1/users/info";

	@Value("${server.url}")
	private String serverUrl;

	@Autowired
	public CustomAuthorityGranter() {
	}

	@Override
	public List<GrantedAuthority> getAuthorities(JsonObject introspectionResponse) {
		String login = introspectionResponse.get("sub").getAsString();
		HttpHeaders headers = new HttpHeaders();
		//TODO add token value somehow
		headers.add("Authorization", "Bearer " + "token value");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<UserInfoDTO> response = restTemplate.exchange(serverUrl + USER_INFO_ENDPOINT, HttpMethod.GET, entity, UserInfoDTO.class);
		if (response.getStatusCode().isError()) {
			throw new SecurityException("Logged in user with sub " + login + " could not be found in database and has been created in database.");
		}
		return response.getBody().getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getRole_type()))
				.collect(Collectors.toList());
	}
}
