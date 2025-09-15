package cz.cyberrange.platform.commons.security.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cz.cyberrange.platform.commons.security.IdentityProvidersService;
import cz.cyberrange.platform.commons.security.config.IdentityProvidersConfig;
import cz.cyberrange.platform.commons.security.model.WellKnownOpenIDConfiguration;
import cz.cyberrange.platform.commons.security.util.JsonUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class IdentityProvidersServiceImpl implements IdentityProvidersService {
  private final LoadingCache<String, WellKnownOpenIDConfiguration> providersConfiguration;
  private final Set<String> providersList;

  @Autowired
  public IdentityProvidersServiceImpl(IdentityProvidersConfig identityProvidersConfig) {
    providersList = identityProvidersConfig.getSetOfIssuers();
    HttpClient httpClient = HttpClientBuilder.create().useSystemProperties().build();
    providersConfiguration =
        CacheBuilder.newBuilder()
            .build(
                new OpenIDConnectServiceConfigurationFetcher(
                    httpClient, identityProvidersConfig.getUserInfoEndpointsMapping()));
  }

  public WellKnownOpenIDConfiguration getIdentityProviderConfiguration(String provider) {
    try {
      if (!this.providersList.isEmpty() && !this.providersList.contains(provider)) {
        throw new AuthenticationServiceException(
            "Identity provider: " + provider + " is not recognized.");
      } else {
        return providersConfiguration.get(provider);
      }
    } catch (ExecutionException | UncheckedExecutionException var3) {
      throw new IllegalStateException("Couldn't load configuration for " + provider, var3);
    }
  }

  @Setter
  @Getter
  private static class JwksResponse {
    @NonNull private List<Map<String, Object>> keys = new ArrayList<>(4);
  }

  private static class OpenIDConnectServiceConfigurationFetcher
      extends CacheLoader<String, WellKnownOpenIDConfiguration> {
    private static final String ISSUER_FIELD = "issuer";
    private final HttpComponentsClientHttpRequestFactory httpFactory;
    private final Map<String, String> userInfoEndpointsMap;

    OpenIDConnectServiceConfigurationFetcher(
        HttpClient httpClient, Map<String, String> userInfoEndpoints) {
      this.httpFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
      this.userInfoEndpointsMap = userInfoEndpoints;
    }

    private static List<Map<String, Object>> getJwks(String jwksUri) {
      if (jwksUri != null) {
        JwksResponse issuerKeys = new RestTemplate().getForObject(jwksUri, JwksResponse.class);
        if (issuerKeys != null) {
          return issuerKeys.getKeys();
        }
      }
      log.error("Could not obtain JSON Web Key Set from URL: {}", jwksUri);
      return null;
    }

    public @NonNull WellKnownOpenIDConfiguration load(@NonNull String provider) {
      RestTemplate restTemplate = new RestTemplate(this.httpFactory);
      WellKnownOpenIDConfiguration conf = new WellKnownOpenIDConfiguration();
      String url = provider + "/.well-known/openid-configuration";
      String jsonString = restTemplate.getForObject(url, String.class);
      if (jsonString == null) {
        throw new IllegalStateException("Failed to parse openid-configuration.");
      }
      JsonElement parsed = JsonParser.parseString(jsonString);
      if (parsed.isJsonObject()) {
        JsonObject o = parsed.getAsJsonObject();
        if (!o.has(ISSUER_FIELD)) {
          throw new IllegalStateException("Returned object did not have an 'issuer' field");
        }
        if (!provider.equals(o.get(ISSUER_FIELD).getAsString())) {
          log.info(
              "Issuer used for discover was {} but final issuer is {}",
              provider,
              o.get(ISSUER_FIELD).getAsString());
        }

        conf.setIssuer(o.get(ISSUER_FIELD).getAsString());
        conf.setAuthorizationEndpointUri(JsonUtils.getAsString(o, "authorization_endpoint"));
        conf.setTokenEndpointUri(JsonUtils.getAsString(o, "token_endpoint"));
        conf.setJwksUri(JsonUtils.getAsString(o, "jwks_uri"));
        conf.setUserInfoUri(
            userInfoEndpointsMap.getOrDefault(
                provider, JsonUtils.getAsString(o, "userinfo_endpoint")));
        conf.setRegistrationEndpointUri(JsonUtils.getAsString(o, "registration_endpoint"));
        conf.setIntrospectionEndpointUri(JsonUtils.getAsString(o, "introspection_endpoint"));
        conf.setCheckSessionIframe(JsonUtils.getAsString(o, "check_session_iframe"));
        conf.setEndSessionEndpoint(JsonUtils.getAsString(o, "end_session_endpoint"));
        conf.setAcrValuesSupported(JsonUtils.getAsStringList(o, "acr_values_supported"));
        conf.setClaimsLocalesSupported(JsonUtils.getAsStringList(o, "claims_locales_supported"));
        conf.setClaimsParameterSupported(JsonUtils.getAsBoolean(o, "claims_parameter_supported"));
        conf.setClaimsSupported(JsonUtils.getAsStringList(o, "claims_supported"));
        conf.setDisplayValuesSupported(JsonUtils.getAsStringList(o, "display_values_supported"));
        conf.setGrantTypesSupported(JsonUtils.getAsStringList(o, "grant_types_supported"));
        conf.setIdTokenSigningAlgValuesSupported(
            JsonUtils.getAsJwsAlgorithmList(o, "id_token_signing_alg_values_supported"));
        conf.setIdTokenEncryptionAlgValuesSupported(
            JsonUtils.getAsJweAlgorithmList(o, "id_token_encryption_alg_values_supported"));
        conf.setIdTokenEncryptionEncValuesSupported(
            JsonUtils.getAsEncryptionMethodList(o, "id_token_encryption_enc_values_supported"));
        conf.setOpPolicyUri(JsonUtils.getAsString(o, "op_policy_uri"));
        conf.setOpTosUri(JsonUtils.getAsString(o, "op_tos_uri"));
        conf.setRequestObjectEncryptionAlgValuesSupported(
            JsonUtils.getAsJweAlgorithmList(o, "request_object_encryption_alg_values_supported"));
        conf.setRequestObjectEncryptionEncValuesSupported(
            JsonUtils.getAsEncryptionMethodList(
                o, "request_object_encryption_enc_values_supported"));
        conf.setRequestObjectSigningAlgValuesSupported(
            JsonUtils.getAsJwsAlgorithmList(o, "request_object_signing_alg_values_supported"));
        conf.setRequestParameterSupported(JsonUtils.getAsBoolean(o, "request_parameter_supported"));
        conf.setRequestUriParameterSupported(
            JsonUtils.getAsBoolean(o, "request_uri_parameter_supported"));
        conf.setResponseTypesSupported(JsonUtils.getAsStringList(o, "response_types_supported"));
        conf.setScopesSupported(JsonUtils.getAsStringList(o, "scopes_supported"));
        conf.setSubjectTypesSupported(JsonUtils.getAsStringList(o, "subject_types_supported"));
        conf.setServiceDocumentation(JsonUtils.getAsString(o, "service_documentation"));
        conf.setTokenEndpointAuthMethodsSupported(
            JsonUtils.getAsStringList(o, "token_endpoint_auth_methods"));
        conf.setTokenEndpointAuthSigningAlgValuesSupported(
            JsonUtils.getAsJwsAlgorithmList(o, "token_endpoint_auth_signing_alg_values_supported"));
        conf.setUiLocalesSupported(JsonUtils.getAsStringList(o, "ui_locales_supported"));
        conf.setUserinfoEncryptionAlgValuesSupported(
            JsonUtils.getAsJweAlgorithmList(o, "userinfo_encryption_alg_values_supported"));
        conf.setUserinfoEncryptionEncValuesSupported(
            JsonUtils.getAsEncryptionMethodList(o, "userinfo_encryption_enc_values_supported"));
        conf.setUserinfoSigningAlgValuesSupported(
            JsonUtils.getAsJwsAlgorithmList(o, "userinfo_signing_alg_values_supported"));
        conf.setJwks(getJwks(conf.getJwksUri()));
        return conf;

      } else {
        throw new IllegalStateException("Couldn't parse server discovery results for " + url);
      }
    }
  }
}
