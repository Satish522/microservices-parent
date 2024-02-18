package com.satish.productservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.satish.productservice.dto.ProductRequest;
import com.satish.productservice.dto.ProductResponse;
import com.satish.productservice.model.Product;
import com.satish.productservice.repository.ProductReposistory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
	
	@Autowired
	private final ProductReposistory productRepository;

	public void createProduct(ProductRequest productRequest) {
		Product product = Product.builder()
				.name(productRequest.getName())
				.description(productRequest.getDescription())
				.price(productRequest.getPrice())
				.build();
		
		productRepository.save(product);
		log.info("Product {} is saved...", product.getId());
		
	}

	public List<ProductResponse> getAllProducts() {
		// TODO Auto-generated method stub
		List<Product> products= productRepository.findAll();
		return products.stream().map(product -> mapToProductResponse(product)).toList();
		
	}

	private ProductResponse mapToProductResponse(Product product) {
		return ProductResponse.builder().id(product.getId())
								.name(product.getName())
								.description(product.getDescription())
								.price(product.getPrice())
								.build();
		
	}
}
