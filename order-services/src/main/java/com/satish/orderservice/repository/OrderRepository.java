package com.satish.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.satish.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
