package cz.muni.ics.kypo.commons.security.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Dominik Pilar & Pavel Seda
 */
public class UserInfoDTO {

    private Long id;
    @JsonProperty("full_name")
    private String fullName;
    private String login;
    private String mail;
    private Set<RoleDTO> roles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void convertScreenNameToLogin(String screenName) {
        this.login = screenName;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfoDTO that = (UserInfoDTO) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getLogin(), that.getLogin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLogin());
    }

    @Override
    public String toString() {
        return "UserInfoDTO{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", login='" + login + '\'' +
                ", mail='" + mail + '\'' +
                ", roles=" + roles +
                '}';
    }
}
