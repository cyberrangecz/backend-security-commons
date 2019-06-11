package cz.muni.ics.kypo.commons.security.mapping;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Pavel Seda
 * @author Dominik Pilar
 */
public class RegisterMicroserviceDTO {

    private String name;
    private String endpoint;
    private Set<RegisterRoleDTO> roles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Set<RegisterRoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RegisterRoleDTO> roles) {
        this.roles = new HashSet<>(roles);
    }

    @Override
    public String toString() {
        return "RegisterMicroserviceDTO{" +
                "name='" + name + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", roles=" + roles +
                '}';
    }
}
