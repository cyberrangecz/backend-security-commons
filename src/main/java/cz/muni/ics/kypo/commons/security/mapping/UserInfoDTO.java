package cz.muni.ics.kypo.commons.security.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Encapsulates information about user and his roles.
 *
 */
public class UserInfoDTO {

    @JsonProperty(value = "id")
    private Long userRefId;
    @JsonProperty("full_name")
    private String fullName;
    private String sub;
    private String mail;
    @JsonProperty(value = "given_name")
    private String givenName;
    @JsonProperty(value = "family_name")
    private String familyName;
    private String iss;
    private Set<RoleDTO> roles = new HashSet<>();

    /**
     * Gets ID of the user.
     *
     * @return the ID of the user.
     */
    public Long getUserRefId() {
        return userRefId;
    }

    /**
     * Sets ID of the user.
     *
     * @param id the ID of the user.
     */
    public void setUserRefId(Long id) {
        this.userRefId = id;
    }

    /**
     * Gets given name.
     *
     * @return the given name
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * Sets given name.
     *
     * @param givenName the given name
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    /**
     * Gets family name.
     *
     * @return the family name
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * Sets family name.
     *
     * @param familyName the family name
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    /**
     * Gets iss.
     *
     * @return the iss
     */
    public String getIss() {
        return iss;
    }

    /**
     * Sets iss.
     *
     * @param iss the iss
     */
    public void setIss(String iss) {
        this.iss = iss;
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
     * Gets sub of the user.
     *
     * @return the sub of the user.
     */
    public String getSub() {
        return sub;
    }

    /**
     * Sets sub of the user.
     *
     * @param sub the sub of the user.
     */
    public void setSub(String sub) {
        this.sub = sub;
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
    public int hashCode() {
        return Objects.hash(getUserRefId(), getSub());
    }

    @Override
    public String toString() {
        return "UserInfoDTO{" +
                "userRefId=" + userRefId +
                ", fullName='" + fullName + '\'' +
                ", sub='" + sub + '\'' +
                ", mail='" + mail + '\'' +
                ", roles=" + roles +
                '}';
    }
}
