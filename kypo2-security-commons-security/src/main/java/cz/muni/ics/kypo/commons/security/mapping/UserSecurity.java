package cz.muni.ics.kypo.commons.security.mapping;

import java.util.Set;

public class UserSecurity {

    private Long id;

    private String login;

    private Set<RolesSecurity> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Set<RolesSecurity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RolesSecurity> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserSecurity userSecurity = (UserSecurity) o;

        if (id != null ? !id.equals(userSecurity.id) : userSecurity.id != null) return false;
        return login != null ? login.equals(userSecurity.login) : userSecurity.login == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        return result;
    }
}
