package com.petcare.common.asset.repository;

import com.petcare.common.asset.domain.Asset;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

  @Query(value = "select distinct ast from Asset ast where ast.id in :assetsIds ")
  Set<Asset> getByIds(Set<Long> assetsIds);
}
