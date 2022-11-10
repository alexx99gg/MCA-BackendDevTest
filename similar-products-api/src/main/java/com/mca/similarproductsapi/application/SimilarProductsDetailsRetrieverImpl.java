package com.mca.similarproductsapi.application;

import com.mca.similarproductsapi.infrastructure.dto.ProductIDs;
import com.mca.similarproductsapi.infrastructure.dto.SimilarProducts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SimilarProductsDetailsRetrieverImpl implements SimilarProductsDetailsRetriever {

  @Autowired
  ProductInfoRetriever productInfoRetriever;

  @Autowired
  SimilarProductIdRetriever similarProductIdRetrieverByHttp;

  public SimilarProducts getSimilarProductsDetails(Integer productId) {
    ProductIDs similarIds = similarProductIdRetrieverByHttp.doRetrieve(productId);
    SimilarProducts similarProducts = new SimilarProducts();
    for (var similarId : similarIds) {
      var productInfo = productInfoRetriever.doRetrieve(similarId);
      if (Objects.nonNull(productInfo.getId())){
        similarProducts.add(productInfo);
      }
    }
    return similarProducts;
  }
}
