package com.petcare.customer.catalog.domain;

import com.petcare.common.common.embeddable.LocalizableString;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "merchant")
public class Merchant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "english", column = @Column(name = "english_name")),
    @AttributeOverride(name = "arabic", column = @Column(name = "arabic_name"))
  })
  private LocalizableString name;

  @Enumerated(EnumType.STRING)
  private MerchantStatus status;
}
