package com.petcare.common.elasticsearch.repository;

import com.petcare.common.elasticsearch.domain.ZoneDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneDocumentRepository extends ElasticsearchRepository<ZoneDocument, Long> {}
