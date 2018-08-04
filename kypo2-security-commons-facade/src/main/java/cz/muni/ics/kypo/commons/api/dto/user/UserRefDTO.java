package cz.muni.ics.kypo.commons.api.dto.user;

import cz.muni.ics.kypo.commons.api.dto.group.GroupRefForUsersDTO;
import cz.muni.ics.kypo.commons.api.dto.group.IDMGroupRefDTO;

import java.util.List;

public class UserRefDTO {

    private Long id;
    private String login;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
