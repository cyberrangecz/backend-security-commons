package cz.muni.ics.kypo.commons.api.dto.role;

public class RoleDTO {

    private Long id;
    private String roleType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }
}
