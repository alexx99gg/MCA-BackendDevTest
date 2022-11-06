package com.mca.similarproductsapi.application;

import com.mca.similarproductsapi.infrastructure.dto.ProductDetail;

public interface ProductInfoRetriever {

  ProductDetail doRetrieve(Integer productId);

}
