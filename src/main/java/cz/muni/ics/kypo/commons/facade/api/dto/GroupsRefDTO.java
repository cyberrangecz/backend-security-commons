package cz.muni.ics.kypo.commons.facade.api.dto;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Pavel Seda
 */
public class GroupsRefDTO {

    private Long id;
    private long idmGroupId;
    private Set<RoleDTO> roles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getIdmGroupId() {
        return idmGroupId;
    }

    public void setIdmGroupId(long idmGroupId) {
        this.idmGroupId = idmGroupId;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "GroupsRefDTO{" +
                "id=" + id +
                ", idmGroupId=" + idmGroupId +
                ", roles=" + roles +
                '}';
    }
}
