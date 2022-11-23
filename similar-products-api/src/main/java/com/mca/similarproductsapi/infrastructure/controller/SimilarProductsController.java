package com.mca.similarproductsapi.infrastructure.controller;

import com.mca.similarproductsapi.application.SimilarProductsDetailsRetriever;
import com.mca.similarproductsapi.infrastructure.dto.SimilarProducts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class SimilarProductsController {

  @Autowired
  SimilarProductsDetailsRetriever similarProductsDetailsRetriever;

  @GetMapping(path = "/product/{productId}/similar")
  @Cacheable("similar-products")
  public ResponseEntity<SimilarProducts> getSimilarById(@PathVariable Integer productId) {
    try {
      SimilarProducts similarProducts = similarProductsDetailsRetriever.getSimilarProductsDetails(productId);
      return ResponseEntity.ok().body(similarProducts);
    } catch (RestClientResponseException exception){
      log.error("Error retrieving similar products for product: {}", productId);
      return ResponseEntity.status(exception.getRawStatusCode()).build();
    }
  }
}
