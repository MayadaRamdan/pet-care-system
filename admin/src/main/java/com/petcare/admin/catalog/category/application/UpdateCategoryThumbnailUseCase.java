package com.petcare.admin.catalog.category.application;

import com.petcare.admin.catalog.category.repository.CategoryRepository;
import com.petcare.common.asset.application.UploadAssetUseCase;
import com.petcare.common.asset.domain.Asset;
import com.petcare.common.common.utils.StringUtils;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@AllArgsConstructor
public class UpdateCategoryThumbnailUseCase {

  private final CategoryRepository categoryRepository;
  private final UploadAssetUseCase uploadAssetUseCase;

  public void execute(Long categoryId, MultipartFile thumbnailMultipart) throws IOException {
    if (thumbnailMultipart == null || thumbnailMultipart.isEmpty()) {
      return;
    }

    String fileName = thumbnailMultipart.getName();
    if (fileName == null || fileName.isEmpty()) {
      fileName = StringUtils.EMPTY_STRING;
    }

    Asset thumbnail = uploadAssetUseCase.execute(thumbnailMultipart.getBytes(), fileName);
    categoryRepository.updateThumbnail(categoryId, thumbnail, thumbnail.getUrl());
  }
}
