package com.mca.similarproductsapi.application;

import com.mca.similarproductsapi.infrastructure.dto.SimilarProducts;

public interface SimilarProductsDetailsRetriever {
  /**
   *
   * @param productId the product's id
   * @return set of similar product's info for the given productId
   */
  public SimilarProducts getSimilarProductsDetails(Integer productId);
}
