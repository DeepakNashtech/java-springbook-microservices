package com.example.demo.service;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.dto.OrderItemRequest;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {
    @Test
    void createOrder_calculatestTotal_andReduceStock() {
        var orderRepo = mock(OrderRepository.class);
        var productRepo = mock(ProductRepository.class);
        var userRepo = mock(UserRepository.class);

        var svc = new OrderServiceImpl(orderRepo, productRepo, userRepo);
        var user = new User();
        user.setId(1L);
        user.setName("Jane");
        user.setEmail("jane@example.com");
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        var p1 = new Product();
        p1.setId(10L);
        p1.setSku("SKU1");
        p1.setName("P1");
        p1.setPrice(new BigDecimal("100.00"));
        p1.setStock(5);
        when(productRepo.findById(10L)).thenReturn(Optional.of(p1));

        var orderItemRequest = new OrderItemRequest();
        orderItemRequest.setProductId(10L);
        orderItemRequest.setQuantity(2);
        var req = new CreateOrderRequest();
        req.setUserId(1L);
        req.setItems(List.of(orderItemRequest));

        when(orderRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        svc.createOrder(req);

        assertThat(p1.getStock()).isEqualTo(3);
        verify(orderRepo).save(any());

        ArgumentCaptor<com.example.demo.entity.Order> cap = ArgumentCaptor.forClass(com.example.demo.entity.Order.class);
        verify(orderRepo).save(cap.capture());
        assertThat(cap.getValue().getTotal()).isEqualByComparingTo("200.00");
        assertThat(cap.getValue().getItems()).hasSize(1);
    }
}
