package com.petcare.common.asset.mapper;

import com.petcare.common.asset.domain.Asset;
import com.petcare.common.asset.dto.AssetDto;
import org.springframework.stereotype.Component;

@Component
public class AssetMapper {

  public AssetDto toDto(Asset asset) {
    return new AssetDto(asset.getId(), asset.getUrl());
  }
}
