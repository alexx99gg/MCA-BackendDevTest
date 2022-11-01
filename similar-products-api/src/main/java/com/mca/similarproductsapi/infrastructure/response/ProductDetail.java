package com.mca.similarproductsapi.infrastructure.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ProductDetail {

  @NotBlank
  String id;

  @NotBlank
  String name;

  @NotNull
  Double price;

  @NotNull
  Boolean availability;
}
