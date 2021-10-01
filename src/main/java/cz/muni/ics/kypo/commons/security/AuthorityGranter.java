package cz.muni.ics.kypo.commons.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface AuthorityGranter {
    List<GrantedAuthority> getAuthorities(Object userInfo);
}
