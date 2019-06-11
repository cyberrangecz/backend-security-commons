package cz.muni.ics.kypo.commons.security.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * @author Dominik Pilar
 * @author Pavel Seda
 */
public class RoleDTO {

    private Long id;
    @JsonProperty("role_type")
    private String roleType;
    @JsonProperty("name_of_microservice")
    private String nameOfMicroservice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getNameOfMicroservice() {
        return nameOfMicroservice;
    }

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
