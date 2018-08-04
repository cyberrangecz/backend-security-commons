package cz.muni.ics.kypo.commons.api.dto.user;

import cz.muni.ics.kypo.commons.api.dto.group.GroupRefForUsersDTO;

import java.util.List;

public class UserRefWithGroupsDTO extends  UserRefDTO{

    private List<GroupRefForUsersDTO> groups;

    public List<GroupRefForUsersDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupRefForUsersDTO> groups) {
        this.groups = groups;
    }
}
