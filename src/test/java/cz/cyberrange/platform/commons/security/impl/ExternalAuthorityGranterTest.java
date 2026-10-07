package cz.cyberrange.platform.commons.security.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

class ExternalAuthorityGranterTest {

  private static ExternalAuthorityGranter granterAnswering(ClientResponse.Builder answer) {
    WebClient webClient =
        WebClient.builder().exchangeFunction(request -> Mono.just(answer.build())).build();
    return new ExternalAuthorityGranter(webClient);
  }

  @Test
  @DisplayName("getAuthorities_nullToken_throwsSecurityException")
  void getAuthorities_nullToken_throwsSecurityException() {
    ExternalAuthorityGranter granter = granterAnswering(ClientResponse.create(HttpStatus.OK));

    assertThatThrownBy(() -> granter.getAuthorities(null))
        .isInstanceOf(SecurityException.class)
        .hasMessageContaining("missing or empty");
  }

  @Test
  @DisplayName("getAuthorities_emptyToken_throwsSecurityException")
  void getAuthorities_emptyToken_throwsSecurityException() {
    ExternalAuthorityGranter granter = granterAnswering(ClientResponse.create(HttpStatus.OK));

    assertThatThrownBy(() -> granter.getAuthorities(""))
        .isInstanceOf(SecurityException.class)
        .hasMessageContaining("missing or empty");
  }

  @Test
  @DisplayName("getAuthorities_serviceListsRoles_returnsOneAuthorityPerRole")
  void getAuthorities_serviceListsRoles_returnsOneAuthorityPerRole() {
    ExternalAuthorityGranter granter =
        granterAnswering(
            ClientResponse.create(HttpStatus.OK)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(
                    "{\"sub\":\"s\",\"roles\":[{\"id\":1,\"role_type\":\"ROLE_A\"},"
                        + "{\"id\":2,\"role_type\":\"ROLE_B\"}]}"));

    assertThat(granter.getAuthorities("token"))
        .extracting("authority")
        .containsExactlyInAnyOrder("ROLE_A", "ROLE_B");
  }

  @Test
  @DisplayName("getAuthorities_serviceAnswersError_throwsSecurityExceptionWithStatusAndNoToken")
  void getAuthorities_serviceAnswersError_throwsSecurityExceptionWithStatusAndNoToken() {
    ExternalAuthorityGranter granter =
        granterAnswering(ClientResponse.create(HttpStatus.FORBIDDEN));

    assertThatThrownBy(() -> granter.getAuthorities("secret-token-value"))
        .isInstanceOf(SecurityException.class)
        .hasMessageContaining("403")
        .hasMessageNotContaining("secret-token-value");
  }
}
