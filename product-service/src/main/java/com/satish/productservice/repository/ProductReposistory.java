package com.satish.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.satish.productservice.model.Product;

@Repository
public interface ProductReposistory  extends MongoRepository<Product, String>{

}
