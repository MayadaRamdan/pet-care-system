package com.petcare.common.common.embeddable;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class PhoneNumber {

  private String countryCode;
  private String number;
  private boolean verified;
}
