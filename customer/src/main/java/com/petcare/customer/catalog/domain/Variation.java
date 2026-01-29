package com.petcare.customer.catalog.domain;

import com.petcare.common.common.embeddable.DateTimePeriod;
import com.petcare.common.common.embeddable.LocalizableString;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Entity
@Table(name = "variation")
@Immutable
@Where(clause = "active = true")
public class Variation {

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

  private String sku;

  private boolean active;

  @ManyToOne
  @JoinColumn(name = "item_id", nullable = false)
  private Item item;

  @Column(name = "price", precision = 10, scale = 3)
  private BigDecimal price;

  @Column(name = "sale_price", precision = 10, scale = 3)
  private BigDecimal salePrice;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "start", column = @Column(name = "sale_price_start_date")),
    @AttributeOverride(name = "end", column = @Column(name = "sale_price_end_date"))
  })
  private DateTimePeriod salePricePeriod;

  private Integer stockQty;

  private Integer maxQtyPerOrder;

  private String thumbnailUrl;
}
