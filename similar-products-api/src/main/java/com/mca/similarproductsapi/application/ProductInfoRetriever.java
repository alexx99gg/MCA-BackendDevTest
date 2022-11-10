package com.mca.similarproductsapi.application;

import com.mca.similarproductsapi.infrastructure.dto.ProductDetail;

public interface ProductInfoRetriever {

  /**
   * @param productId the product's id
   * @return product's details for the given productId
   */
  ProductDetail doRetrieve(Integer productId);

}
