package com.mca.similarproductsapi.infrastructure.retriever;

import com.mca.similarproductsapi.application.ProductInfoRetriever;
import com.mca.similarproductsapi.infrastructure.dto.ProductDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// TODO: add unit-test

@Service
@Slf4j
public class ProductInfoRetrieverByHttp implements ProductInfoRetriever {

  @Autowired
  private RestTemplate restTemplate;
  @Value("${application.product-info-server-address}")
  String serverAddress;

  @Override
  @Cacheable("product-info")
  public ProductDetail doRetrieve(Integer productId) {
    ResponseEntity<ProductDetail> productDetailResponseEntity = restTemplate.getForEntity(serverAddress + "/product/" + productId, ProductDetail.class);
    return productDetailResponseEntity.getBody();
  }
}
