package com.mca.similarproductsapi.application;

import com.mca.similarproductsapi.infrastructure.dto.ProductIDs;

public interface SimilarProductIdRetriever {
  /**
   * @param productId the product's id
   * @return similar  product's id for the given productId
   */
  ProductIDs doRetrieve(Integer productId);
}
