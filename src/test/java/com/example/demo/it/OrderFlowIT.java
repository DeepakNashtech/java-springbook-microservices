package com.example.demo.it;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.dto.OrderItemRequest;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OrderService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class OrderFlowIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
        r.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
    }

    @Autowired
    OrderService orderService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    void endToEnd_createOrder_persist_andComputesTotals () {
        var user = new User();
        user.setName("Alice");
        user.setEmail("alice@example.com");
        userRepository.save(user);

        var pdt = new Product();
        pdt.setSku("L-001");
        pdt.setName("Laptop");
        pdt.setPrice(new BigDecimal("1500.00"));
        pdt.setStock(5);
        productRepository.save(pdt);

        var orderReq = new OrderItemRequest();
        orderReq.setProductId(pdt.getId());
        orderReq.setQuantity(2);
        var req = new CreateOrderRequest();
        req.setUserId(user.getId());
        req.setItems(List.of(orderReq));

        var resp = orderService.createOrder(req);

        assertThat(resp.getTotal()).isEqualByComparingTo("3000.00");
        assertThat(resp.getLines()).hasSize(1);
        assertThat(productRepository.findById(pdt.getId()).orElseThrow().getStock()).isEqualTo(3);
    }
}
