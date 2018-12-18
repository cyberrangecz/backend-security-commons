package cz.muni.ics.kypo.commons.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author Jan Duda & Pavel Seda
 */
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnoreProperties({"id"})
    private Long id;
    @Column(name = "role_type", unique = true, nullable = false)
    private String roleType;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;
        return Objects.equals(id, role.getId()) &&
                Objects.equals(roleType, role.getRoleType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleType);
    }

    @Override
    public String toString() {
        return "Role{" + "id=" + id + ", roleType='" + roleType + '\'' + '}';
    }
}
