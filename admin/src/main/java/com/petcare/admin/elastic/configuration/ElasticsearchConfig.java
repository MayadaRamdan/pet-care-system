package com.petcare.admin.elastic.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.petcare.common.elasticsearch")
public class ElasticsearchConfig extends ElasticsearchConfiguration {

  @Value("${spring.elasticsearch.uris}")
  private String elasticsearchHostAndPort;

  @Value("${spring.elasticsearch.socket-timeout}")
  private Long elasticsearchSocketTimeout;

  @Value("${spring.elasticsearch.connection-timeout}")
  private Long elasticsearchConnectTimeout;

  @Override
  public ClientConfiguration clientConfiguration() {
    return ClientConfiguration.builder().connectedTo(elasticsearchHostAndPort)
            .withSocketTimeout(elasticsearchSocketTimeout)
            .withConnectTimeout(elasticsearchSocketTimeout)
            .build();
  }
}
