package com.example.demo.service;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.dto.OrderResponse;
import com.example.demo.entity.*;
import com.example.demo.exception.IllegalArgumentException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public OrderResponse createOrder(CreateOrderRequest req) {
        User user = userRepository.findById(req.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found: " + req.getUserId()));
        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.NEW)
                .items(new ArrayList<>())
                .total(BigDecimal.ZERO)
                .build();
        BigDecimal total = BigDecimal.ZERO;

        for(var itemReq: req.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemReq.getProductId()));

            int newStock = product.getStock() -itemReq.getQuantity();
            if (newStock < 0) throw new IllegalArgumentException("Insufficient stock for SKU " + product.getSku());
            product.setStock(newStock);

            BigDecimal unitPrice = product.getPrice();
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(itemReq.getQuantity())).setScale(2, RoundingMode.HALF_UP);

            OrderItem oi = OrderItem.builder().order(order).product(product).quantity(itemReq.getQuantity()).unitPrice(unitPrice).lineTotal(lineTotal).build();

            order.getItems().add(oi);
            total = total.add(lineTotal);
        }

        order.setTotal(total.setScale(2, RoundingMode.HALF_UP));
        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    @Override
    public OrderResponse getById(Long id) {
        Order o = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
        return toResponse(o);
    }

    private OrderResponse toResponse(Order o) {
        var lines = o.getItems().stream()
                .map(oi -> OrderResponse.Line.builder()
                        .productId(oi.getProduct().getId())
                        .sku(oi.getProduct().getSku()).quantity(oi.getQuantity())
                        .unitPrice(oi.getUnitPrice())
                        .lineTotal(oi.getLineTotal())
                        .build())
                .toList();
        return OrderResponse.builder()
                .orderId(o.getId())
                .userId(o.getUser().getId())
                .status(o.getStatus().name())
                .total(o.getTotal())
                .lines(lines)
                .build();
    }
}
