package com.petcare.admin.zonemerchant.repository;

import com.petcare.admin.zonemerchant.domain.ZoneMerchant;
import com.petcare.common.common.dto.IdName;
import com.petcare.common.zonemerchant.ZoneMerchantPK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneMerchantRepository extends JpaRepository<ZoneMerchant, ZoneMerchantPK> {

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  void deleteByZoneId(long zoneId);

  @Query(
      value =
          "select new com.petcare.common.common.dto.IdName(m.id,  m.name.english)"
              + " from ZoneMerchant zm join zm.merchant m where zm.zone.id = :zoneId")
  List<IdName> findMerchantsForZone(long zoneId);
}
