package cz.cyberrange.platform.commons.startup.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * Encapsulates information about role used in microservice to be registered in
 * <i>User-and-group</i>.
 */
@Setter
@Getter
public class RegisterRoleDTO {

  /** Role type of the role. */
  @JsonProperty(value = "role_type")
  private String roleType;

  /** Whether role is default or not. */
  @JsonProperty(value = "default")
  private boolean isDefault;

  /** Description of the role. */
  @JsonProperty(value = "description")
  private String description;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RegisterRoleDTO that = (RegisterRoleDTO) o;
    return isDefault == that.isDefault && Objects.equals(roleType, that.roleType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roleType, isDefault);
  }

  @Override
  public String toString() {
    return "RegisterRoleDTO{"
        + "roleType='"
        + roleType
        + '\''
        + ", isDefault="
        + isDefault
        + ", description='"
        + description
        + '\''
        + '}';
  }
}
