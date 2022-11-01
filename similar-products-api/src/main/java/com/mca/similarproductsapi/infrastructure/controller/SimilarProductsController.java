package com.mca.similarproductsapi.infrastructure.controller;

import com.mca.similarproductsapi.infrastructure.response.ProductDetail;
import com.mca.similarproductsapi.infrastructure.response.ProductIDs;
import com.mca.similarproductsapi.infrastructure.response.SimilarProducts;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class SimilarProductsController {

  @GetMapping(path = "/product/{productId}/similar")
  public ResponseEntity<SimilarProducts> getSimilarById(@PathVariable String productId){

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<ProductIDs> productIDsResponseEntity = restTemplate.getForEntity("http://localhost:3001/product/" + productId + "/similarids", ProductIDs.class);

    SimilarProducts similarProducts = new SimilarProducts();

    for (var similarId : productIDsResponseEntity.getBody()){
      ResponseEntity<ProductDetail> productDetailResponseEntity = restTemplate.getForEntity("http://localhost:3001/product/" + similarId , ProductDetail.class);
      similarProducts.add(productDetailResponseEntity.getBody());

    }
    return ResponseEntity.ok().body(similarProducts);
  }

}
