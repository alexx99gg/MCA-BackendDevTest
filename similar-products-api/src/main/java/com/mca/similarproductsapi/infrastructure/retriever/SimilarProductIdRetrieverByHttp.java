package com.mca.similarproductsapi.infrastructure.retriever;

import com.mca.similarproductsapi.application.SimilarProductIdRetriever;
import com.mca.similarproductsapi.infrastructure.dto.ProductIDs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

// TODO: add unit-test

@Service
@Slf4j
public class SimilarProductIdRetrieverByHttp implements SimilarProductIdRetriever {
  RestTemplate restTemplate = new RestTemplate();

  @Value("${application.similar-products-ids-server-address}")
  String serverAddress;

  @Override
  @Cacheable("similar-products-ids")
  public ProductIDs doRetrieve(Integer productId){
    try {
    ResponseEntity<ProductIDs> productIDsResponseEntity = restTemplate.getForEntity(serverAddress + "/product/" + productId + "/similarids", ProductIDs.class);
    return productIDsResponseEntity.getBody();
  } catch (RestClientException exception){
    log.error("Error retrieving similar products ids for product: {}", productId);
    // TODO: specify with PO error policy
    // When decided, use HttpClientErrorException, HttpServerErrorException and UnknownHttpStatusCodeException
      return new ProductIDs();
  }
  }

}
