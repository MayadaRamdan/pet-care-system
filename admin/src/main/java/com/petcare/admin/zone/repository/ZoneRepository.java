package com.petcare.admin.zone.repository;

import com.petcare.admin.zone.domain.Zone;
import com.petcare.admin.zone.domain.ZoneListing;
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

  @Query("select z from Zone z where z.status <> 'DELETED'")
  List<Zone> findNotDeleted();
}
