package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    @NotNull
    private Long userId;

    @NotNull private List<OrderItemRequest> items;
}
