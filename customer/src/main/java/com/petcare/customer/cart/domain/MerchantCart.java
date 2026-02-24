package com.petcare.customer.cart.domain;

import com.petcare.customer.catalog.domain.Merchant;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "merchant_cart")
public class MerchantCart {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany(mappedBy = "merchantCart")
  private List<MerchantCartItem> items;

  @ManyToOne private Merchant merchant;

  private BigDecimal subTotal;

  private BigDecimal total;
}
