package cz.cyberrange.platform.commons.security.config;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@EnableConfigurationProperties
@ConfigurationProperties("crczp.identity")
public class IdentityProvidersConfig {

  private List<IdentityProvider> providers = new ArrayList<>();

  @PostConstruct
  private void checkProviders() {
    if (this.providers.isEmpty()) {
      throw new BeanCreationException(
          "Error creating configuration bean with name 'identityProvidersConfig': At least one identity provider must be configured.");
    }
    for (IdentityProvider provider : providers) {
      if (provider.getIssuer().isBlank()) {
        throw new BeanCreationException(
            "Error creating configuration bean with name 'identityProvidersConfig': Property 'issuer' of the identity provider cannot be blank.");
      }
    }
  }

  public Map<String, String> getUserInfoEndpointsMapping() {
    return providers.stream()
        .filter(ip -> ip.getUserInfoEndpoint() != null && !ip.getUserInfoEndpoint().isBlank())
        .collect(
            Collectors.toMap(IdentityProvider::getIssuer, IdentityProvider::getUserInfoEndpoint));
  }

  public Set<String> getSetOfIssuers() {
    return providers.stream().map(IdentityProvider::getIssuer).collect(Collectors.toSet());
  }

  @Setter
  @Getter
  public static class IdentityProvider {
    private String issuer;
    private String userInfoEndpoint;
  }
}
