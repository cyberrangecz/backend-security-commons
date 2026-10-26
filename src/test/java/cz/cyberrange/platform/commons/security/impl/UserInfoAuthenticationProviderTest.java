package cz.cyberrange.platform.commons.security.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import cz.cyberrange.platform.commons.security.AuthorityGranter;
import cz.cyberrange.platform.commons.security.model.UserInfo;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;

class UserInfoAuthenticationProviderTest {

  private static final String ISSUER = "https://issuer.example/realm";

  private AuthorityGranter authorityGranter;
  private UserInfoValidator userInfoValidator;
  private UserInfoAuthenticationProvider provider;

  @BeforeEach
  void setUp() {
    authorityGranter = mock(AuthorityGranter.class);
    userInfoValidator = mock(UserInfoValidator.class);
    provider = new UserInfoAuthenticationProvider(authorityGranter, userInfoValidator);
  }

  private static String signedToken(Instant expiresAt) throws Exception {
    JWTClaimsSet claims =
        new JWTClaimsSet.Builder()
            .issuer(ISSUER)
            .subject("subject-1")
            .issueTime(Date.from(Instant.now().minusSeconds(10)))
            .expirationTime(Date.from(expiresAt))
            .build();
    SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
    jwt.sign(new MACSigner("0123456789abcdef0123456789abcdef"));
    return jwt.serialize();
  }

  @Test
  @DisplayName("supports_bearerTokenAuthentication_returnsTrue")
  void supports_bearerTokenAuthentication_returnsTrue() {
    assertThat(provider.supports(BearerTokenAuthenticationToken.class)).isTrue();
    assertThat(provider.supports(Authentication.class)).isFalse();
  }

  @Test
  @DisplayName("authenticate_validTokenSeenTwice_validatesOnlyOnceAndReturnsSameAuthentication")
  void authenticate_validTokenSeenTwice_validatesOnlyOnceAndReturnsSameAuthentication()
      throws Exception {
    String token = signedToken(Instant.now().plusSeconds(600));
    when(userInfoValidator.validate(token, ISSUER)).thenReturn(new UserInfo("subject-1"));
    when(authorityGranter.getAuthorities(token))
        .thenReturn(List.of(new SimpleGrantedAuthority("ROLE_TEST")));

    Authentication first = provider.authenticate(new BearerTokenAuthenticationToken(token));
    Authentication second = provider.authenticate(new BearerTokenAuthenticationToken(token));

    assertThat(first.getName()).isEqualTo("subject-1");
    assertThat(first.getAuthorities()).extracting("authority").containsExactly("ROLE_TEST");
    assertThat(second).isSameAs(first);
    verify(userInfoValidator, times(1)).validate(token, ISSUER);
  }

  @Test
  @DisplayName("authenticate_expiredToken_returnsNullWithoutCaching")
  void authenticate_expiredToken_returnsNullWithoutCaching() throws Exception {
    String token = signedToken(Instant.now().minusSeconds(5));
    when(userInfoValidator.validate(token, ISSUER)).thenReturn(new UserInfo("subject-1"));

    Authentication result = provider.authenticate(new BearerTokenAuthenticationToken(token));

    assertThat(result).isNull();
  }

  @Test
  @DisplayName("authenticate_malformedToken_throwsAuthenticationServiceWithoutValidatingRemotely")
  void authenticate_malformedToken_throwsAuthenticationServiceWithoutValidatingRemotely() {
    assertThatThrownBy(() -> provider.authenticate(new BearerTokenAuthenticationToken("not-a-jwt")))
        .isInstanceOf(AuthenticationServiceException.class);
    verify(userInfoValidator, never()).validate(any(), any());
  }

  @Test
  @DisplayName("modifyTimeClaims_expiryWithoutIssueTime_setsIssueTimeOneSecondBeforeExpiry")
  void modifyTimeClaims_expiryWithoutIssueTime_setsIssueTimeOneSecondBeforeExpiry() {
    Instant expiry = Instant.parse("2030-01-01T00:00:00Z");

    Map<String, Object> claims = provider.modifyTimeClaims(Map.of("exp", Date.from(expiry)));

    assertThat(claims.get("exp")).isEqualTo(expiry);
    assertThat(claims.get("iat")).isEqualTo(expiry.minusSeconds(1));
  }

  @Test
  @DisplayName("modifyTimeClaims_expiryAndIssueTime_convertsBothToInstants")
  void modifyTimeClaims_expiryAndIssueTime_convertsBothToInstants() {
    Instant expiry = Instant.parse("2030-01-01T00:00:00Z");
    Instant issued = Instant.parse("2029-12-31T00:00:00Z");

    Map<String, Object> claims =
        provider.modifyTimeClaims(Map.of("exp", Date.from(expiry), "iat", Date.from(issued)));

    assertThat(claims.get("exp")).isEqualTo(expiry);
    assertThat(claims.get("iat")).isEqualTo(issued);
  }

  @Test
  @DisplayName("modifyTimeClaims_noTimeClaims_leavesBothNull")
  void modifyTimeClaims_noTimeClaims_leavesBothNull() {
    Map<String, Object> claims = provider.modifyTimeClaims(Map.of("sub", "x"));

    assertThat(claims.get("exp")).isNull();
    assertThat(claims.get("iat")).isNull();
  }
}
