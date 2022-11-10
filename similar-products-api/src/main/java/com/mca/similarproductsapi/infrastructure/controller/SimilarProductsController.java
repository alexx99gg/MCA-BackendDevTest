package com.mca.similarproductsapi.infrastructure.controller;

import com.mca.similarproductsapi.application.ProductInfoRetriever;
import com.mca.similarproductsapi.application.SimilarProductIdRetriever;
import com.mca.similarproductsapi.infrastructure.dto.ProductIDs;
import com.mca.similarproductsapi.infrastructure.dto.SimilarProducts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class SimilarProductsController {

  @Autowired
  ProductInfoRetriever productInfoRetriever;

  @Autowired
  SimilarProductIdRetriever similarProductIdRetrieverByHttp;

  @GetMapping(path = "/product/{productId}/similar")
  @Cacheable("similar-products")
  public ResponseEntity<SimilarProducts> getSimilarById(@PathVariable Integer productId) {
    // TODO: this logic should go to the application layer
    ProductIDs similarIds = similarProductIdRetrieverByHttp.doRetrieve(productId);
    SimilarProducts similarProducts = new SimilarProducts();
    for (var similarId : similarIds) {
      var productInfo = productInfoRetriever.doRetrieve(similarId);
      if (Objects.nonNull(productInfo.getId())){
        similarProducts.add(productInfo);
      }
    }
    return ResponseEntity.ok().body(similarProducts);
  }
}
