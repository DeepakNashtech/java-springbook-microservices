package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private  Long orderId;
    private Long userId;
    private String status;
    private BigDecimal total;
    private List<Line> lines;

    @Data
    @Builder
    public static class Line {
        private Long productId;
        private String sku;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal lineTotal;
    }
}
