package com.petcare.common.asset.application;

import com.petcare.common.asset.domain.Asset;
import com.petcare.common.asset.repository.AssetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class CreateAbsoluteUrlAssetUseCase {

  private final AssetRepository assetRepository;

  public Asset execute(String url) {
    return assetRepository.save(Asset.absoluteOf(url));
  }
}
