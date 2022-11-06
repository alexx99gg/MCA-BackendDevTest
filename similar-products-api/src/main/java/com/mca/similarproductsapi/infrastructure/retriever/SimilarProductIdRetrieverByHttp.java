package com.mca.similarproductsapi.infrastructure.retriever;

import com.mca.similarproductsapi.application.SimilarProductIdRetriever;
import com.mca.similarproductsapi.infrastructure.dto.ProductIDs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class SimilarProductIdRetrieverByHttp implements SimilarProductIdRetriever {

  RestTemplate restTemplate = new RestTemplate();


  @Override
  @Cacheable("similar-products-ids")
  public ProductIDs doRetrieve(Integer productId){
    try {
    ResponseEntity<ProductIDs> productIDsResponseEntity = restTemplate.getForEntity("http://localhost:3001/product/" + productId + "/similarids", ProductIDs.class);
    return productIDsResponseEntity.getBody();
  } catch (RestClientException exception){
    log.error("Error retrieving similar products ids for product: {}", productId);
    // TODO: specify with PO error policy
    return new ProductIDs();
  }
  }

}
