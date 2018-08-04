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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRefDTO that = (UserRefDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return login != null ? login.equals(that.login) : that.login == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        return result;
    }
}
