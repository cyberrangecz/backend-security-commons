package cz.cyberrange.platform.commons.security.config;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration class for managing identity providers in the Cyber Range platform. Loads
 * configuration from properties with prefix 'crczp.identity'. Must contain at least one identity
 * provider configuration.
 */
@Setter
@Getter
@Component
@EnableConfigurationProperties
@ConfigurationProperties("crczp.identity")
@Slf4j
public class IdentityProvidersConfig {

  /**
   * List of configured identity providers. Must contain at least one provider after initialization.
   */
  private List<IdentityProvider> providers = new ArrayList<>(1);

  /**
   * Validates the identity providers configuration after bean construction. Checks if at least one
   * provider is configured and each provider has a non-blank issuer.
   *
   * @throws BeanCreationException if validation fails
   */
  @PostConstruct
  private void checkProviders() {
    if (providers.isEmpty()) {
      throw new BeanCreationException(
          "Error creating configuration bean with name 'identityProvidersConfig':"
              + " At least one identity provider must be configured.");
    }
    for (final IdentityProvider provider : providers) {
      if (provider.getIssuer().isBlank()) {
        throw new BeanCreationException(
            "Error creating configuration bean with name 'identityProvidersConfig':"
                + " Property 'issuer' of the identity provider cannot be blank.");
      }
    }
  }

  /**
   * Creates a mapping of issuer URLs to their corresponding user info endpoints. Only includes
   * providers that have a non-blank user info endpoint configured.
   *
   * @return Map of issuer URLs to user info endpoints
   */
  public Map<String, String> getUserInfoEndpointsMapping() {
    return providers.stream()
        .filter(ip -> ip.getUserInfoEndpoint() != null && !ip.getUserInfoEndpoint().isBlank())
        .collect(
            Collectors.toMap(IdentityProvider::getIssuer, IdentityProvider::getUserInfoEndpoint));
  }

  /**
   * Retrieves all configured issuer URLs.
   *
   * @return Set of issuer URLs from all configured providers
   */
  public final Set<String> getSetOfIssuers() {
    return this.providers.stream().map(IdentityProvider::getIssuer).collect(Collectors.toSet());
  }

  /** Configuration properties for a single identity provider. */
  @Setter
  @Getter
  public static class IdentityProvider {
    /** The issuer URL of the identity provider. Must not be blank. */
    private String issuer;

    /** The user info endpoint URL of the identity provider. Optional configuration property. */
    private String userInfoEndpoint;
  }
}
