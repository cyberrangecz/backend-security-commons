package cz.cyberrange.platform.commons.security.impl;

import java.time.Instant;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

public class TokenCache {

  private final Map<String, TokenCacheItem> cache = new HashMap<>();
  @Setter @Getter private int defaultExpireTime = 300000;
  @Setter @Getter private boolean cacheTokens = true;
  @Setter @Getter private boolean cacheNonExpiringTokens = false;
  @Setter @Getter private boolean forceCacheExpireTime = false;

  public TokenCacheItem get(String key) {
    if (this.cacheTokens && this.cache.containsKey(key)) {
      TokenCacheItem tco = this.cache.get(key);
      if (tco != null && tco.cacheExpire != null && tco.cacheExpire.isAfter(Instant.now())) {
        return tco;
      }
      this.cache.remove(key);
    }
    return null;
  }

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

  public class TokenCacheItem {
    private final Instant cacheExpire;
    @Setter @Getter private OAuth2AccessToken token;
    @Setter @Getter private AbstractAuthenticationToken auth;

    private TokenCacheItem(OAuth2AccessToken token, AbstractAuthenticationToken auth) {
      this.token = token;
      this.auth = auth;
      if (this.token.getExpiresAt() == null
          || TokenCache.this.forceCacheExpireTime
              && this.token.getExpiresAt().getEpochSecond() - System.currentTimeMillis()
                  > (long) TokenCache.this.defaultExpireTime) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MILLISECOND, TokenCache.this.defaultExpireTime);
        this.cacheExpire = cal.getTime().toInstant();
      } else {
        this.cacheExpire = this.token.getExpiresAt();
      }
    }
  }
}
