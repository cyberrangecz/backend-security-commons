package cz.cyberrange.platform.commons.security.impl;

import cz.cyberrange.platform.commons.security.AuthorityGranter;
import cz.cyberrange.platform.commons.security.config.constants.StringConstants;
import cz.cyberrange.platform.commons.security.mapping.UserInfoDTO;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Class is annotated with {@link Component}, so its mark as candidates for auto-detection when
 * using annotation-based configuration and classpath scanning. This class is responsible for
 * returning a set of Spring Security GrantedAuthority objects to be assigned to the token service's
 * resulting <i>Authentication</i> object.
 */
@Component
@Slf4j
public class ExternalAuthorityGranter implements AuthorityGranter {

  private final WebClient webClient;

  /**
   * Instantiates a new ExternalAuthorityGranter.
   *
   * @param webClient the WebClient instance
   */
  @Autowired
  public ExternalAuthorityGranter(
      @Qualifier(value = "userManagementServiceWebClientSecurityCommons") WebClient webClient) {
    this.webClient = webClient;
  }

  @Override
  public List<GrantedAuthority> getAuthorities(@Nullable String oidcToken) {
    if (oidcToken == null || oidcToken.isEmpty()) {
      throw new SecurityException("Authorization token is missing or empty");
    }

    try {
      UserInfoDTO userInfoResponse =
          webClient
              .get()
              .uri("/users/info")
              .header(StringConstants.AUTH_HEADER_KEY, "Bearer " + oidcToken)
              .retrieve()
              .bodyToMono(UserInfoDTO.class)
              .block();

      if (userInfoResponse == null) {
        throw new SecurityException(
            "Error while getting info about logged in user, user info is null.");
      }
      return userInfoResponse.getRoles().stream()
          .map(role -> new SimpleGrantedAuthority(role.getRoleType()))
          .collect(Collectors.toList());
    } catch (WebClientResponseException ex) { // Use WebClientResponseException for WebClient errors
      throw new SecurityException(
          "Error while getting info about logged in user: " + ex.getStatusCode());
    } catch (Exception ex) {
      throw new SecurityException(
          "Unexpected error while fetching user info: " + ex.getMessage(), ex);
    }
  }
}
