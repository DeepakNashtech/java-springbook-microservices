package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long id;
    @NotBlank private String name;
    @NotBlank @Size(max=64) private String sku;
    @NotNull @DecimalMin("0.00") private BigDecimal price;
    @NotNull @Min(0) private Integer stock;
}
