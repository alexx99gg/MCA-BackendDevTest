package com.mca.similarproductsapi.infrastructure.retriever;

import com.mca.similarproductsapi.application.SimilarProductIdRetriever;
import com.mca.similarproductsapi.infrastructure.dto.ProductIDs;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SimilarProductIdRetrieverByHttp implements SimilarProductIdRetriever {

  @Override public ProductIDs doRetrieve(Integer productId){
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<ProductIDs> productIDsResponseEntity = restTemplate.getForEntity("http://localhost:3001/product/" + productId + "/similarids", ProductIDs.class);
    return productIDsResponseEntity.getBody();
  }

}
