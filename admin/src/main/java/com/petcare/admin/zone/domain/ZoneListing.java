package com.petcare.admin.zone.domain;

public interface ZoneListing {
  Long getId();

  String getCode();

  String getName();

  ZoneStatus getStatus();
}
