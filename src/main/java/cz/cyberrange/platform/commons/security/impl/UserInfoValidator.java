package cz.cyberrange.platform.commons.security.impl;

import cz.cyberrange.platform.commons.security.IdentityProvidersService;
import cz.cyberrange.platform.commons.security.config.constants.StringConstants;
import cz.cyberrange.platform.commons.security.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

@Component
public class UserInfoValidator {

  private final IdentityProvidersService identityProvidersService;
  private final RestTemplate restTemplate;
  private final JsonMapper objectMapper;

  @Autowired
  public UserInfoValidator(IdentityProvidersService identityProvidersService) {
    this.identityProvidersService = identityProvidersService;
    this.restTemplate = new RestTemplate();
    this.objectMapper =
        JsonMapper.builder().propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).build();
  }

  public UserInfo validate(String accessToken, String issuerUrl) {
    String userInfoUrl =
        this.identityProvidersService.getIdentityProviderConfiguration(issuerUrl).getUserInfoUri();

    HttpEntity<String> request = new HttpEntity<>(headersWithBearerAuthorization(accessToken));
    try {
      String userInfoSrc =
          restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, String.class).getBody();
      UserInfo userInfo = objectMapper.readValue(userInfoSrc, UserInfo.class);
      userInfo.setIssuer(issuerUrl);
      return userInfo;
    } catch (JacksonException e) {
      throw new InternalAuthenticationServiceException(
          "Unable to parse user info response from issuer " + issuerUrl, e);
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
        throw new AuthenticationServiceException(
            "Access token rejected by issuer " + issuerUrl + " with status " + e.getStatusCode());
      }
      throw new AuthenticationServiceException(
          "User info request to issuer " + issuerUrl + " failed with status " + e.getStatusCode());
    }
  }

  private HttpHeaders headersWithBearerAuthorization(String bearerToken) {
    HttpHeaders headers = new HttpHeaders();
    headers.add(StringConstants.AUTH_HEADER_KEY, String.format("Bearer %s", bearerToken));
    return headers;
  }
}
