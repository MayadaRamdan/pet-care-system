package com.petcare.admin.staffuser.domain;

import com.petcare.admin.security.domain.StaffUserRole;
import com.petcare.common.common.domain.Auditable;
import com.petcare.common.common.embeddable.PhoneNumber;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "staff_user")
public class StaffUser extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String email;
  private String username;
  private String password;

  private String name;

  @Column(name = "code", unique = true)
  private String code;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "countryCode", column = @Column(name = "phone_country_code")),
    @AttributeOverride(name = "number", column = @Column(name = "phone_number")),
    @AttributeOverride(name = "verified", column = @Column(name = "phone_verified"))
  })
  private PhoneNumber phoneNumber;

  @ManyToOne private StaffUserRole role;
}
