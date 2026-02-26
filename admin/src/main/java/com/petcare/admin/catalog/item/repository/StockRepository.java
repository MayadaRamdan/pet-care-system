package com.petcare.admin.catalog.item.repository;

import com.petcare.common.catalog.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {}
