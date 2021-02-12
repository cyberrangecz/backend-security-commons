package cz.muni.ics.kypo.commons.startup.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Encapsulates information about role used in microservice to be registered in <i>User-and-group</i>.
 *
 */
public class RegisterRoleDTO {

    @JsonProperty(value = "role_type")
    private String roleType;
    @JsonProperty(value = "default")
    private boolean isDefault;
    @JsonProperty(value = "description")
    private String description;

    /**
     * Gets role type of the role.
     *
     * @return the role type of the role.
     */
    public String getRoleType() {
        return roleType;
    }

    /**
     * Sets role type of the role.
     *
     * @param roleType the role type of the role.
     */
    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    /**
     * Mark if role is default or not.
     *
     * @return true if role is default, false otherwise.
     */
    public boolean isDefault() {
        return isDefault;
    }

    /**
     * Sets mark if role is default or not.
     *
     * @param aDefault true if role is default, false otherwise.
     */
    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    /**
     * Gets description of the role.
     *
     * @return the description of the role.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description of the role.
     *
     * @param description the description of the role.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterRoleDTO that = (RegisterRoleDTO) o;
        return isDefault == that.isDefault &&
                Objects.equals(roleType, that.roleType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleType, isDefault);
    }

    @Override
    public String toString() {
        return "RegisterRoleDTO{" +
                "roleType='" + roleType + '\'' +
                ", isDefault=" + isDefault +
                ", description='" + description + '\'' +
                '}';
    }
}
