package com.petcare.admin.catalog.category.domain;

import com.petcare.common.asset.domain.Asset;
import com.petcare.common.common.domain.Auditable;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

@Getter
@Setter
@Entity
@Table(name = "category")
@SQLDelete(
    sql = "UPDATE category SET deleted = true, active= false, deleted_at = now() WHERE id = ?")
public class Category extends Auditable {

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
    @AttributeOverride(name = "english", column = @Column(name = "english_path")),
    @AttributeOverride(name = "arabic", column = @Column(name = "arabic_path"))
  })
  private LocalizableString path;

  @Enumerated(EnumType.STRING)
  private CategoryStatus status;

  @ManyToOne
  @JoinColumn(name = "parent_id")
  private Category parent;

  @ManyToOne
  @JoinColumn(name = "thumbnail_id")
  private Asset thumbnail;

  private String thumbnailUrl;

  private int level;

  private int displayOrder;

  public void setName(LocalizableString name) {
    this.name = name;

    if (this.parent == null) {
      this.path = name;
      return;
    }

    this.path =
        LocalizableString.of(
            parent.path.getEnglish() + ">" + this.name.getEnglish(),
            parent.path.getArabic() + ">" + this.name.getArabic());
  }
}
