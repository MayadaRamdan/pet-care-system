package com.petcare.admin.zone.repository;

import com.petcare.admin.zone.domain.Zone;
import com.petcare.admin.zone.domain.ZoneListing;
import com.petcare.common.common.dto.IdName;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {

  @Query(
      value =
          "select z.id as id, z.name.english as name, z.code as code, z.status as status"
              + " from Zone z where z.status <> 'DELETED'",
      countQuery = "select count(z) from Zone z")
  Page<ZoneListing> listZones(Pageable pageable);

  @Query(
      "select new com.petcare.common.common.dto.IdName(m.id,  m.name.english)"
          + " from Zone z join z.merchants m where z.id = :zoneId")
  List<IdName> findZoneMerchants(Long zoneId);

  @Query("select z from Zone z where z.status <> 'DELETED'")
  List<Zone> findNotDeleted();
}
