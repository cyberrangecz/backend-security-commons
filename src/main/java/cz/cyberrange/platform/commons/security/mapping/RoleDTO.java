package cz.cyberrange.platform.commons.security.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/** Encapsulates information about role of user. */
@Setter
@Getter
public class RoleDTO {

  /** ID of the role. */
  private Long id;

  /** Role type of the role. */
  @JsonProperty("role_type")
  private String roleType;

  /** Name of microservice in which is role used. */
  @JsonProperty("name_of_microservice")
  private String nameOfMicroservice;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RoleDTO roleDTO = (RoleDTO) o;
    return Objects.equals(getId(), roleDTO.getId())
        && Objects.equals(getRoleType(), roleDTO.getRoleType())
        && Objects.equals(getNameOfMicroservice(), roleDTO.getNameOfMicroservice());
  }

  @Override
  public int hashCode() {

    return Objects.hash(getId(), getRoleType(), getNameOfMicroservice());
  }

  @Override
  public String toString() {
    return "RoleDTO{"
        + "id="
        + id
        + ", roleType='"
        + roleType
        + '\''
        + ", nameOfMicroservice='"
        + nameOfMicroservice
        + '\''
        + '}';
  }
}
