package cz.cyberrange.platform.commons.security.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

/**
 * The UserInfo class.
 *
 * <p>Matches the object received from auth provider.
 */
@Setter
@Getter
public class UserInfo {
  private String sub;
  private String issuer;
  private String preferredUsername;
  private String name;
  private String givenName;
  private String familyName;
  private String middleName;
  private String nickname;
  private String profile;
  private String picture;
  private String website;
  private String email;
  private Boolean emailVerified;
  private String gender;
  private String zoneinfo;
  private String locale;
  private String phoneNumber;
  private Boolean phoneNumberVerified;
  private Address address;
  private String updatedTime;
  private String birthdate;
  private transient JsonObject src;

  @JsonCreator
  public UserInfo(@JsonProperty("sub") String sub) {
    Assert.notNull(sub, "Subject must not be null.");
    this.sub = sub;
  }
}
