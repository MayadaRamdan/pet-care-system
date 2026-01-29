package com.petcare.admin.zone.domain;

import com.petcare.admin.merchant.domain.Merchant;
import com.petcare.common.common.domain.Auditable;
import com.petcare.common.common.embeddable.LocalizableString;
import com.petcare.common.geo.domain.Point;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import java.util.Set;

@Entity
public class Zone extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "CODE", unique = true)
  @Pattern(
      regexp =
          "^([A-Za-z0-9\\\\\\/_\\-]|([A-Za-z0-9\\\\\\/_\\-][A-Za-z0-9\\\\\\/_\\- ]{0,18}[A-Za-z0-9\\\\\\/_\\-]))$")
  private String code;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "english", column = @Column(name = "english_name")),
    @AttributeOverride(name = "arabic", column = @Column(name = "arabic_name"))
  })
  private LocalizableString name;

  @ElementCollection
  @CollectionTable(name = "zone_coordinate", joinColumns = @JoinColumn(name = "zone_id"))
  private List<Point> coordinates;

  @Enumerated(EnumType.STRING)
  private ZoneStatus status;

  @OneToMany private Set<Merchant> merchants;
}
