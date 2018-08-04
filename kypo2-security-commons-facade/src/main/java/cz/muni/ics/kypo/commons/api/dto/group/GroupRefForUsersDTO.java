package cz.muni.ics.kypo.commons.api.dto.group;

public class GroupRefForUsersDTO {

    private Long id;
    private Long idmGroupId;

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
}
