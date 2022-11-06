package com.mca.similarproductsapi.infrastructure.retriever;

import com.mca.similarproductsapi.application.ProductInfoRetriever;
import com.mca.similarproductsapi.infrastructure.dto.ProductDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductInfoRetrieverByHttp implements ProductInfoRetriever {

  @Override
  public ProductDetail doRetrieve(Integer productId) {
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<ProductDetail> productDetailResponseEntity = restTemplate.getForEntity("http://localhost:3001/product/" + productId, ProductDetail.class);
    return productDetailResponseEntity.getBody();
  }
}
