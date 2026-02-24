package com.petcare.customer.cart.domain;

import com.petcare.customer.catalog.domain.Variation;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "merchant_cart_item")
public class MerchantCartItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne private MerchantCart merchantCart;

  @ManyToOne private Variation variation;

  private Integer quantity;

  private BigDecimal unitPrice;

  private BigDecimal unitSalePrice;

  private BigDecimal totalPrice;
}
