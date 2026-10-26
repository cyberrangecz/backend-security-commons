package cz.cyberrange.platform.commons.security.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cz.cyberrange.platform.commons.security.config.IdentityProvidersConfig.IdentityProvider;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.test.util.ReflectionTestUtils;

class IdentityProvidersConfigTest {

  private static IdentityProvider provider(String issuer, String userInfoEndpoint) {
    IdentityProvider provider = new IdentityProvider();
    provider.setIssuer(issuer);
    provider.setUserInfoEndpoint(userInfoEndpoint);
    return provider;
  }

  private static IdentityProvidersConfig configWith(IdentityProvider... providers) {
    IdentityProvidersConfig config = new IdentityProvidersConfig();
    config.setProviders(List.of(providers));
    return config;
  }

  @Test
  @DisplayName("getSetOfIssuers_severalProviders_returnsEveryIssuer")
  void getSetOfIssuers_severalProviders_returnsEveryIssuer() {
    IdentityProvidersConfig config =
        configWith(provider("https://a", null), provider("https://b", "https://b/userinfo"));

    assertThat(config.getSetOfIssuers()).containsExactlyInAnyOrder("https://a", "https://b");
  }

  @Test
  @DisplayName("getUserInfoEndpointsMapping_providersWithAndWithoutEndpoint_mapsOnlyThoseWithOne")
  void getUserInfoEndpointsMapping_providersWithAndWithoutEndpoint_mapsOnlyThoseWithOne() {
    IdentityProvidersConfig config =
        configWith(
            provider("https://a", null),
            provider("https://b", "https://b/userinfo"),
            provider("https://c", "  "));

    assertThat(config.getUserInfoEndpointsMapping())
        .containsExactly(java.util.Map.entry("https://b", "https://b/userinfo"));
  }

  @Test
  @DisplayName("checkProviders_noProviders_throwsBeanCreationException")
  void checkProviders_noProviders_throwsBeanCreationException() {
    IdentityProvidersConfig config = new IdentityProvidersConfig();

    assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(config, "checkProviders"))
        .isInstanceOf(BeanCreationException.class)
        .hasMessageContaining("At least one identity provider");
  }

  @Test
  @DisplayName("checkProviders_blankIssuer_throwsBeanCreationException")
  void checkProviders_blankIssuer_throwsBeanCreationException() {
    IdentityProvidersConfig config = configWith(provider(" ", null));

    assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(config, "checkProviders"))
        .isInstanceOf(BeanCreationException.class)
        .hasMessageContaining("cannot be blank");
  }

  @Test
  @DisplayName("checkProviders_validProvider_completesWithoutException")
  void checkProviders_validProvider_completesWithoutException() {
    IdentityProvidersConfig config = configWith(provider("https://a", null));

    ReflectionTestUtils.invokeMethod(config, "checkProviders");

    assertThat(config.getProviders()).hasSize(1);
  }
}
