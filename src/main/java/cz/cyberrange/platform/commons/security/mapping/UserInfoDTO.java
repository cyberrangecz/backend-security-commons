package cz.cyberrange.platform.commons.security.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/** Encapsulates information about user and his roles. */
@Setter
@Getter
public class UserInfoDTO {

  @JsonProperty(value = "id")
  private Long userRefId;

  @JsonProperty("full_name")
  private String fullName;

  private String sub;

  private String mail;

  @JsonProperty(value = "given_name")
  private String givenName;

  @JsonProperty(value = "family_name")
  private String familyName;

  private String iss;

  @NonNull private Set<RoleDTO> roles = new HashSet<>();

  @Override
  public int hashCode() {
    return Objects.hash(getUserRefId(), getSub());
  }

  @Override
  public String toString() {
    return "UserInfoDTO{"
        + "userRefId="
        + userRefId
        + ", fullName='"
        + fullName
        + '\''
        + ", sub='"
        + sub
        + '\''
        + ", mail='"
        + mail
        + '\''
        + ", roles="
        + roles
        + '}';
  }
}
