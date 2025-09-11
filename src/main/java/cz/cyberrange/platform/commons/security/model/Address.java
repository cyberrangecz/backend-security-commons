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
      clone.setFormatted(this.formatted);
      clone.setStreetAddress(this.streetAddress);
      clone.setLocality(this.locality);
      clone.setRegion(this.region);
      clone.setPostalCode(this.postalCode);
      clone.setCountry(this.country);
      return clone;
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }
}
