package cz.muni.ics.kypo.commons.security.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Encapsulates information about role of user.
 *
 */
public class RoleDTO {

    private Long id;
    @JsonProperty("role_type")
    private String roleType;
    @JsonProperty("name_of_microservice")
    private String nameOfMicroservice;

    /**
     * Gets ID of the role.
     *
     * @return the ID of the role.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets ID of the role.
     *
     * @param id the ID of the role.
     */
    public void setId(Long id) {
        this.id = id;
    }

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
     * Gets name of microservice in which is role used.
     *
     * @return the name of microservice in which is role used.
     */
    public String getNameOfMicroservice() {
        return nameOfMicroservice;
    }

    /**
     * Sets name of microservice in which is role used.
     *
     * @param nameOfMicroservice the name of microservice in which is role used.     */
    public void setNameOfMicroservice(String nameOfMicroservice) {
        this.nameOfMicroservice = nameOfMicroservice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleDTO roleDTO = (RoleDTO) o;
        return Objects.equals(getId(), roleDTO.getId()) &&
                Objects.equals(getRoleType(), roleDTO.getRoleType()) &&
                Objects.equals(getNameOfMicroservice(), roleDTO.getNameOfMicroservice());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getRoleType(), getNameOfMicroservice());
    }

    @Override
    public String toString() {
        return "RoleDTO{" +
                "id=" + id +
                ", roleType='" + roleType + '\'' +
                ", nameOfMicroservice='" + nameOfMicroservice + '\'' +
                '}';
    }
}
