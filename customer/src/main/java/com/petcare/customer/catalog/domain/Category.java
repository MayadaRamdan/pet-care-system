package com.petcare.customer.catalog.domain;

import com.petcare.common.common.embeddable.LocalizableString;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Entity
@Table(name = "category")
@Immutable
@Where(clause = "active = true")
public class Category {

  protected boolean active;
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
  @ManyToOne
  @JoinColumn(name = "parent_id")
  private Category parent;
  private String thumbnailUrl;

  private int level;

  private int displayOrder;
}
