package com.mca.similarproductsapi.infrastructure.retriever;

import com.mca.similarproductsapi.application.ProductInfoRetriever;
import com.mca.similarproductsapi.infrastructure.dto.ProductDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ProductInfoRetrieverByHttp implements ProductInfoRetriever {

  @Override
  @Cacheable("product-info")
  public ProductDetail doRetrieve(Integer productId) {
    RestTemplate restTemplate = new RestTemplate();
    try {
      ResponseEntity<ProductDetail> productDetailResponseEntity = restTemplate.getForEntity("http://localhost:3001/product/" + productId, ProductDetail.class);
      return productDetailResponseEntity.getBody();
    } catch (RestClientException exception){
      log.error("Error retrieving product info for product: {}", productId);
      // TODO: specify with PO error policy
      return new ProductDetail();
    }

  }
}
