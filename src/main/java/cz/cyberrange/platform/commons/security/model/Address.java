package cz.cyberrange.platform.commons.security.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Address implements Cloneable {
  private Long id;
  private String formatted;
  private String streetAddress;
  private String locality;
  private String region;
  private String postalCode;
  private String country;

  public Address() {}

  @Override
  public Address clone() {
    try {
      Address clone = (Address) super.clone();
      clone.setFormatted(this.getFormatted());
      clone.setStreetAddress(this.getStreetAddress());
      clone.setLocality(this.getLocality());
      clone.setRegion(this.getRegion());
      clone.setPostalCode(this.getPostalCode());
      clone.setCountry(this.getCountry());
      return clone;
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }
}
