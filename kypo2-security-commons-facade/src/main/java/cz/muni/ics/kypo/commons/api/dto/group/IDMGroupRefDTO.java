package cz.muni.ics.kypo.commons.api.dto.group;

import cz.muni.ics.kypo.commons.api.dto.user.UserRefDTO;
import cz.muni.ics.kypo.commons.api.dto.role.RoleDTO;

import java.util.List;
import java.util.Set;

public class IDMGroupRefDTO {

    private Long id;
    private Long idmGroupId;
    private Set<RoleDTO> roles;
    private List<UserRefDTO> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdmGroupId() {
        return idmGroupId;
    }

    public void setIdmGroupId(Long idmGroupId) {
        this.idmGroupId = idmGroupId;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }

    public List<UserRefDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserRefDTO> users) {
        this.users = users;
    }
}
