package com.petcare.admin.catalog.item.repository;

import com.petcare.admin.catalog.item.domain.Variation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariationRepository extends JpaRepository<Variation, Long> {}
