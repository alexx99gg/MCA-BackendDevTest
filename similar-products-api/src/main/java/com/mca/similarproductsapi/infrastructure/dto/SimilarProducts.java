package com.mca.similarproductsapi.infrastructure.dto;

import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
public class SimilarProducts extends LinkedList<ProductDetail> {
  public SimilarProducts(List<ProductDetail> list){
    super(list);
  }
}
