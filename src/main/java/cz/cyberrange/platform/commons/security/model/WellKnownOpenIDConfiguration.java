package cz.cyberrange.platform.commons.security.model;

import com.google.common.base.Objects;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWSAlgorithm;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
public class WellKnownOpenIDConfiguration {
  private String authorizationEndpointUri;
  private String tokenEndpointUri;
  private String registrationEndpointUri;
  private String issuer;
  private String jwksUri;
  private String userInfoUri;
  private String introspectionEndpointUri;
  private String revocationEndpointUri;
  private String checkSessionIframe;
  private String endSessionEndpoint;
  private List<String> scopesSupported;
  private List<String> responseTypesSupported;
  private List<String> grantTypesSupported;
  private List<String> acrValuesSupported;
  private List<String> subjectTypesSupported;
  private List<JWSAlgorithm> userinfoSigningAlgValuesSupported;
  private List<JWEAlgorithm> userinfoEncryptionAlgValuesSupported;
  private List<EncryptionMethod> userinfoEncryptionEncValuesSupported;
  private List<JWSAlgorithm> idTokenSigningAlgValuesSupported;
  private List<JWEAlgorithm> idTokenEncryptionAlgValuesSupported;
  private List<EncryptionMethod> idTokenEncryptionEncValuesSupported;
  private List<JWSAlgorithm> requestObjectSigningAlgValuesSupported;
  private List<JWEAlgorithm> requestObjectEncryptionAlgValuesSupported;
  private List<EncryptionMethod> requestObjectEncryptionEncValuesSupported;
  private List<String> tokenEndpointAuthMethodsSupported;
  private List<JWSAlgorithm> tokenEndpointAuthSigningAlgValuesSupported;
  private List<String> displayValuesSupported;
  private List<String> claimTypesSupported;
  private List<String> claimsSupported;
  private String serviceDocumentation;
  private List<String> claimsLocalesSupported;
  private List<String> uiLocalesSupported;
  private Boolean claimsParameterSupported;
  private Boolean requestParameterSupported;
  private Boolean requestUriParameterSupported;
  private Boolean requireRequestUriRegistration;
  private String opPolicyUri;
  private String opTosUri;
  private UserInfoTokenMethod userInfoTokenMethod;
  private List<Map<String, Object>> jwks;

  public WellKnownOpenIDConfiguration() {}

  @Override
  public int hashCode() {
    return Objects.hashCode(
        Arrays.stream(getClass().getDeclaredFields())
            .filter(field -> !Modifier.isStatic(field.getModifiers()))
            .map(
                field -> {
                  field.setAccessible(true);
                  try {
                    return field.get(this);
                  } catch (IllegalAccessException e) {
                    log.error(
                        "Could not access field '{}' of {}",
                        field.getName(),
                        this.getClass().getName());
                    throw new RuntimeException(e);
                  }
                }));
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj == null) {
      return false;
    } else if (this.getClass() != obj.getClass()) {
      return false;
    } else {
      WellKnownOpenIDConfiguration other = (WellKnownOpenIDConfiguration) obj;
      return this.hashCode() == other.hashCode();
    }
  }

  public enum UserInfoTokenMethod {
    HEADER,
    FORM,
    QUERY;

    UserInfoTokenMethod() {}
  }
}
