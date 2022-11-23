package com.mca.similarproductsapi.application;

import com.mca.similarproductsapi.infrastructure.dto.ProductIDs;
import com.mca.similarproductsapi.infrastructure.dto.SimilarProducts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimilarProductsDetailsRetrieverImpl implements SimilarProductsDetailsRetriever {

  @Autowired
  ProductInfoRetriever productInfoRetriever;

  @Autowired
  SimilarProductIdRetriever similarProductIdRetrieverByHttp;

  public SimilarProducts getSimilarProductsDetails(Integer productId) {
    ProductIDs similarIds = similarProductIdRetrieverByHttp.doRetrieve(productId);

    return new SimilarProducts(similarIds.parallelStream().map(x -> productInfoRetriever.doRetrieve(x)).toList());
  }
}
