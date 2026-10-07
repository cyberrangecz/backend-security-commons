package cz.cyberrange.platform.commons.security.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

/**
 * Thread-safe cache of the authentication resolved for a bearer token, keyed by the token's value,
 * so a token seen again before its cache entry expires skips re-validation.
 */
public class TokenCache {

  private final Map<String, TokenCacheItem> cache = new ConcurrentHashMap<>();

  /** Lifetime of an entry whose token has no expiry, or whose lifetime is capped. */
  @Setter @Getter private volatile Duration defaultExpireTime = Duration.ofMinutes(5);

  @Setter @Getter private volatile boolean cacheTokens = true;
  @Setter @Getter private volatile boolean cacheNonExpiringTokens = false;

  /** Caps an entry's lifetime to the default expire time even when the token expires later. */
  @Setter @Getter private volatile boolean forceCacheExpireTime = false;

  /**
   * Returns the cached entry for the given token value, evicting it when it has expired.
   *
   * @param key the token value used as the cache key
   * @return the live entry, or null when caching is off, nothing is cached or the entry expired
   */
  public TokenCacheItem get(String key) {
    if (!this.cacheTokens) {
      return null;
    }
    TokenCacheItem item = this.cache.get(key);
    if (item == null) {
      return null;
    }
    if (item.cacheExpire.isAfter(Instant.now())) {
      return item;
    }
    this.cache.remove(key, item);
    return null;
  }

  /**
   * Wraps the access token and its authentication into a cache entry, and stores the entry keyed by
   * the token value when caching is on and the token has an expiry or non-expiring tokens are
   * cached too.
   *
   * @param accessToken the access token to cache
   * @param authToken the authentication resolved for that token
   * @return the wrapped entry, or null when the access token has already expired
   */
  public TokenCacheItem put(
      @NonNull OAuth2AccessToken accessToken, @NonNull AbstractAuthenticationToken authToken) {

    if (accessToken.getExpiresAt() == null || accessToken.getExpiresAt().isAfter(Instant.now())) {
      TokenCacheItem tco = new TokenCacheItem(accessToken, authToken);
      if (this.cacheTokens && (this.cacheNonExpiringTokens || accessToken.getExpiresAt() != null)) {
        this.cache.put(accessToken.getTokenValue(), tco);
      }
      return tco;
    }
    return null;
  }

  /** A cached authentication result for one access token, together with its cache expiry. */
  public class TokenCacheItem {
    private final Instant cacheExpire;
    @Setter @Getter private volatile OAuth2AccessToken token;
    @Setter @Getter private volatile AbstractAuthenticationToken auth;

    private TokenCacheItem(
        @NonNull OAuth2AccessToken token, @NonNull AbstractAuthenticationToken auth) {
      this.token = token;
      this.auth = auth;
      Instant cappedExpiry = Instant.now().plus(TokenCache.this.defaultExpireTime);
      Instant tokenExpiry = token.getExpiresAt();
      this.cacheExpire =
          tokenExpiry == null
                  || TokenCache.this.forceCacheExpireTime && tokenExpiry.isAfter(cappedExpiry)
              ? cappedExpiry
              : tokenExpiry;
    }
  }
}
