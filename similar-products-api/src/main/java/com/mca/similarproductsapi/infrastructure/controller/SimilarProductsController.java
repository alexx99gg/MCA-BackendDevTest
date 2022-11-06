package com.mca.similarproductsapi.infrastructure.controller;

import com.mca.similarproductsapi.application.ProductInfoRetriever;
import com.mca.similarproductsapi.application.SimilarProductIdRetriever;
import com.mca.similarproductsapi.infrastructure.dto.ProductIDs;
import com.mca.similarproductsapi.infrastructure.dto.SimilarProducts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class SimilarProductsController {

  @Autowired
  ProductInfoRetriever productInfoRetriever;

  @Autowired
  SimilarProductIdRetriever similarProductIdRetrieverByHttp;

  @GetMapping(path = "/product/{productId}/similar")
  public ResponseEntity<SimilarProducts> getSimilarById(@PathVariable Integer productId){


    ProductIDs similarIds = similarProductIdRetrieverByHttp.doRetrieve(productId);

    SimilarProducts similarProducts = new SimilarProducts();

    for (var similarId : similarIds){
      similarProducts.add(productInfoRetriever.doRetrieve(similarId));
    }

    return ResponseEntity.ok().body(similarProducts);
  }

}
