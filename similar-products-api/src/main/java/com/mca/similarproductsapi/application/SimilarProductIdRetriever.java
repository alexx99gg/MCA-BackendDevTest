package com.mca.similarproductsapi.application;

import com.mca.similarproductsapi.infrastructure.dto.ProductIDs;

public interface SimilarProductIdRetriever {
  ProductIDs doRetrieve(Integer productId);
}
