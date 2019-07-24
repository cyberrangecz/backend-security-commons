package cz.muni.ics.kypo.commons.security.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Encapsulates information about user and his roles.
 *
 * @author Dominik Pilar
 * @author Pavel Seda
 */
public class UserInfoDTO {

    private Long id;
    @JsonProperty("full_name")
    private String fullName;
    private String login;
    private String mail;
    private Set<RoleDTO> roles = new HashSet<>();

    /**
     * Gets ID of the user.
     *
     * @return the ID of the user.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets ID of the user.
     *
     * @param id the ID of the user.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets full name of the user.
     *
     * @return the full name of the user.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets full name of the user.
     *
     * @param fullName the full name of the user.
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Gets login of the user.
     *
     * @return the login of the user.
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets login of the user.
     *
     * @param login the login of the user.
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Gets mail of the user.
     *
     * @return the mail of the user.
     */
    public String getMail() {
        return mail;
    }

    /**
     * Sets mail of the user.
     *
     * @param mail the mail of the user.
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Gets {@link RoleDTO}s of the user.
     *
     * @return the roles of the user.
     */
    public Set<RoleDTO> getRoles() {
        return roles;
    }

    /**
     * Sets {@link RoleDTO}s of the user.
     *
     * @param roles the roles of the user.
     */
    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserInfoDTO)) return false;
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
