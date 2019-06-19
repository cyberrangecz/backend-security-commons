package cz.muni.ics.kypo.commons.security.mapping;

import java.util.HashSet;
import java.util.Set;

/**
 * Encapsulates information about microservice to be registered in <i>User-and-group</i>.
 *
 * @author Pavel Seda
 * @author Dominik Pilar
 */
public class RegisterMicroserviceDTO {

    private String name;
    private String endpoint;
    private Set<RegisterRoleDTO> roles;

    /**
     * Gets name of microservice.
     *
     * @return the name of microservice.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of microservice.
     *
     * @param name the name of microservice.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets endpoint of microservice.
     *
     * @return the endpoint of microservice.
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * Sets endpoint of microservice.
     *
     * @param endpoint the endpoint of microservice.
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * Gets {@link RegisterRoleDTO}s which will be used in microservice.
     *
     * @return the roles of microservice.
     */
    public Set<RegisterRoleDTO> getRoles() {
        return roles;
    }

    /**
     * Sets {@link RegisterRoleDTO}s which will be used in microservice.
     *
     * @param roles the roles of microservice.
     */
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
