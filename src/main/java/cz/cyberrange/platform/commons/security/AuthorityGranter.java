package cz.cyberrange.platform.commons.security;

import java.util.List;
import org.springframework.security.core.GrantedAuthority;

public interface AuthorityGranter {
  List<GrantedAuthority> getAuthorities(String token);
}
