package com.petcare.customer.catalog.domain;

import com.petcare.common.common.embeddable.LocalizableString;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Entity
@Table(name = "item")
@Immutable
@Where(clause = "active = true")
public class Item {

  protected boolean active;
  @Id private Long id;
  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "english", column = @Column(name = "english_name")),
    @AttributeOverride(name = "arabic", column = @Column(name = "arabic_name"))
  })
  private LocalizableString name;
  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "english", column = @Column(name = "english_description")),
    @AttributeOverride(name = "arabic", column = @Column(name = "arabic_description"))
  })
  private LocalizableString description;
  private String thumbnailUrl;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  //  @ManyToOne
  //  @JoinColumn(name = "merchant_id")
  //  private Merchant merchant;

  @OneToMany(
      mappedBy = "item",
      cascade = {CascadeType.ALL})
  @Where(clause = "active = true")
  private List<Variation> variations = new ArrayList<>();
}
