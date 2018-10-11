package cz.muni.ics.kypo.commons.security.config;


import com.google.gson.JsonObject;
import com.nimbusds.jwt.util.DateUtils;
import cz.muni.ics.kypo.commons.security.mapping.UserInfoDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import net.minidev.json.JSONObject;
import org.bouncycastle.util.encoders.Base64;
import org.mitre.oauth2.introspectingfilter.service.IntrospectionAuthorityGranter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.SchemaOutputResolver;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomAuthorityGranter implements IntrospectionAuthorityGranter {

	private static Logger LOG = LoggerFactory.getLogger(CustomAuthorityGranter.class);
	private RestTemplate restTemplate = new RestTemplate();
	private static final String USER_INFO_ENDPOINT = "/kypo2-rest-user-and-group/api/v1/users/rolin";

	@Value("${server.url}")
	private String serverUrl;

	@Value("${server.secret.key}")
	private String secretKey;

	@Autowired
	public CustomAuthorityGranter() {
	}

	@Override
	public List<GrantedAuthority> getAuthorities(JsonObject introspectionResponse) {
		/*String login = introspectionResponse.get("sub").getAsString().split("@")[0];
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.add("JWT", getJwt(login));
		} catch (UnsupportedEncodingException ex) {

		}
		//TODO add token value somehow
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<List<String>> response = restTemplate.exchange(serverUrl + USER_INFO_ENDPOINT, HttpMethod.GET, entity, new ParameterizedTypeReference<List<String>>(){});
		if (response.getStatusCode().isError()) {
			throw new SecurityException("Logged in user with sub " + login + " could not be found in database and has been created in database.");
		}
		Assert.notEmpty(response.getBody(), "No roles for user with login "+ login);
		return response.getBody().stream().map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());*/
		List<GrantedAuthority> roles = new ArrayList<>();
		roles.add(new SimpleGrantedAuthority("ADMIN"));
		return roles;
	}
/*
	private String getJwt(String login)  throws UnsupportedEncodingException{
		final long HOUR = 3600*1000;
		Key k =	Keys.hmacShaKeyFor(secretKey.getBytes("UTF-8"));
		String jwt = Jwts.builder()
				.setSubject("securityCommons/server")
				.setExpiration(new Date(new Date().getTime() + HOUR))
				.claim("name", "securityCommons")
				.claim("login", login)
				.claim("scope", "server")
				.signWith(k)
				.compact();
		return jwt;
	}*/
}
