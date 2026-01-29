package com.petcare.admin.merchant.repository;

import com.petcare.admin.merchant.domain.Merchant;
import com.petcare.admin.merchant.domain.MerchantListing;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

  @Query(
      """
      select m.id as id, m.name.english as name, m.status as status
      from Merchant m
      where m.status <> com.petcare.admin.merchant.domain.MerchantStatus.DELETED
      order by m.id desc
      """)
  List<MerchantListing> findAllNotDeleted();
}
