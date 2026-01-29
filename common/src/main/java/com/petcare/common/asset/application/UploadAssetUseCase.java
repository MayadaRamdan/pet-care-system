package com.petcare.common.asset.application;

import com.petcare.common.asset.domain.Asset;
import com.petcare.common.asset.repository.AssetRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class UploadAssetUseCase {

  @Autowired private AssetRepository assetRepository;

  @Value("${pet.care.asset.dir.base}")
  private String assetDirBase;

  @Value("${pet.care.asset.assets.base-url}")
  private String urlBase;

  public Asset execute(byte[] file, String fileName) throws IOException {
    String url = upload(file, fileName);
    return assetRepository.save(Asset.relativeOf(url));
  }

  private String upload(byte[] bytes, String fileName) throws IOException {
    String url;
    try {

      if (fileName == null || fileName.isEmpty()) {
        fileName = UUID.randomUUID() + ".jpg";
      }

      Path path = Paths.get(assetDirBase + fileName);
      Files.write(path, bytes);
      url = urlBase + assetDirBase + fileName;
    } catch (IOException e) {
      log.error("Error Uploading File", e);
      throw e;
    }
    return url;
  }
}
