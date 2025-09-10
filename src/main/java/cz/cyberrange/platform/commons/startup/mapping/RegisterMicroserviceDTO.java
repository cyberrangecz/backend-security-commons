package cz.cyberrange.platform.commons.startup.mapping;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/** Encapsulates information about microservice to be registered in <i>User-and-group</i>. */
@Getter
public class RegisterMicroserviceDTO {

  /** Name of microservice. */
  @Setter private String name;

  /** Endpoint of microservice. */
  @Setter private String endpoint;

  /** Roles which will be used in microservice. */
  private Set<RegisterRoleDTO> roles;

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
    return "RegisterMicroserviceDTO{"
        + "name='"
        + name
        + '\''
        + ", endpoint='"
        + endpoint
        + '\''
        + ", roles="
        + roles
        + '}';
  }
}
