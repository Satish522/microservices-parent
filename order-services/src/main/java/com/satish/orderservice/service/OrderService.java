package com.satish.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.satish.orderservice.dto.InventoryResponse;
import com.satish.orderservice.dto.OrderLineItemDto;
import com.satish.orderservice.dto.OrderRequest;
import com.satish.orderservice.event.OrderPlacedEvent;
import com.satish.orderservice.model.Order;
import com.satish.orderservice.model.OrderLineItem;
import com.satish.orderservice.repository.OrderRepository;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {


	private final OrderRepository orderRepository;
	
	private final WebClient.Builder webClientBuilder;
	
	private final Tracer tracer;
	
	private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
	
	public String placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		
		List<OrderLineItem> orderLineItems =  orderRequest.getOrderLineItemDto().stream()
				.map(this::mapToDto)
				.toList();
		order.setOrderLineitems(orderLineItems);
		
		List<String> skuCodes = order.getOrderLineitems().stream()
					.map(OrderLineItem::getSkuCode)
					.toList();
		
		log.info("Calling inventory service");
		
		Span inventoryServiceLookup =tracer.nextSpan().name("InventoryServiceLookup");
		
		try(Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceLookup.start())){
		//Call inventory service and place order if product is in stock
		InventoryResponse[] inventoryResponse = webClientBuilder.build().get().uri("http://inventory-service/api/inventory",
				uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
				.retrieve()
				.bodyToMono(InventoryResponse[].class)
				.block();
		
		boolean allProductInStocks = Arrays.stream(inventoryResponse).allMatch(InventoryResponse::getIsInStocks);
		
		
		
		if(allProductInStocks) {
			orderRepository.save(order);
			kafkaTemplate.send("notification", new OrderPlacedEvent(order.getOrderNumber()));
			return "Order placed successfully..";
		}else
			throw new IllegalArgumentException("Product is not the stock, please try again later");
		
		}finally {
			inventoryServiceLookup.end();
		}
				
	}

	private OrderLineItem mapToDto(OrderLineItemDto orderLineItemDto) {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setPrice(orderLineItemDto.getPrice());
		orderLineItem.setQuantity(orderLineItemDto.getQuantity());
		orderLineItem.setSkuCode(orderLineItemDto.getSkuCode());
		return orderLineItem;
	}
	
	
	
}
