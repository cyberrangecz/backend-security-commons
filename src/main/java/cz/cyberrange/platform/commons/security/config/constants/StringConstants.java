package cz.cyberrange.platform.commons.security.config.constants;

public class StringConstants {
  /** Key for the Authorization header used in Bearer token authentication. */
  public static final String AUTH_HEADER_KEY = "Authorization";

  /** Prefix for Bearer tokens in the Authorization header. Use example "Bearer &lt;token&gt;" */
  public static final String BEARER_TOKEN_PREFIX = "Bearer ";

  /** Key for the auth token parameter in WebSocket connection URLs. */
  public static final String WS_AUTH_TOKEN_PARAM_KEY = "authToken";
}
