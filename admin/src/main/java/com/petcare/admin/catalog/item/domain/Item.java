package com.petcare.admin.catalog.item.domain;

import com.petcare.admin.catalog.category.domain.Category;
import com.petcare.admin.merchant.domain.Merchant;
import com.petcare.common.asset.domain.Asset;
import com.petcare.common.common.domain.Auditable;
import com.petcare.common.common.embeddable.LocalizableString;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Entity
@Table(name = "item")
@SQLDelete(sql = "UPDATE item SET deleted = true, active= false, deleted_at = now() WHERE id = ?")
public class Item extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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

  @Enumerated(EnumType.STRING)
  private ItemStatus status;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<CategoryProduct> categories;

  @ManyToOne
  @JoinColumn(name = "merchant_id")
  private Merchant merchant;

  @OneToMany(
      mappedBy = "item",
      cascade = {CascadeType.ALL})
  @Where(clause = "deleted = false")
  private List<Variation> variations = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "thumbnail_id")
  private Asset thumbnail;

  private String thumbnailUrl;

  public void setStatus(ItemStatus status) {
    this.status = status;
    this.deleted = ItemStatus.DELETED == status;
    this.active = ItemStatus.ACTIVE == status;
  }
}
