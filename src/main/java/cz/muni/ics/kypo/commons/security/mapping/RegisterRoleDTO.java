package cz.muni.ics.kypo.commons.security.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * @author Pavel Seda
 * @author Dominik Pilar
 */
public class RegisterRoleDTO {

    @JsonProperty(value = "role_type")
    private String roleType;
    @JsonProperty(value = "default")
    private boolean isDefault;
    @JsonProperty(value = "description")
    private String description;

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getDescription() {
        return description;
    }

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
