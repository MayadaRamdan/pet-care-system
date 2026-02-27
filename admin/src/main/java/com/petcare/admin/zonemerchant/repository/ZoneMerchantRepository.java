package com.petcare.admin.zonemerchant.repository;

import com.petcare.admin.zonemerchant.domain.ZoneMerchant;
import com.petcare.common.zonemerchant.ZoneMerchantPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneMerchantRepository extends JpaRepository<ZoneMerchant, ZoneMerchantPK> {

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  void deleteByZoneId(long zoneId);
}
