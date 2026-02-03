package com.petcare.admin.zone.dto;

import com.petcare.common.common.embeddable.LocalizableString;
import com.petcare.common.geo.domain.Point;
import java.util.List;

public record CreateZoneRequest(String code, LocalizableString name, List<Point> coordinates) {}
