package com.mca.similarproductsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SimilarProductsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimilarProductsApiApplication.class, args);
	}

}
