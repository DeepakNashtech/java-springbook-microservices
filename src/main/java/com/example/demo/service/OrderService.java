package com.example.demo.service;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.dto.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest req);
    OrderResponse getById(Long id);
}
