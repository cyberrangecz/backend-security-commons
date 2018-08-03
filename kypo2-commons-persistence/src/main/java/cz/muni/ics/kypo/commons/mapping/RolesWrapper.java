package cz.muni.ics.kypo.commons.mapping;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cz.muni.ics.kypo.commons.model.Role;

import java.util.List;

public class RolesWrapper {

    private List<String> roles;

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
