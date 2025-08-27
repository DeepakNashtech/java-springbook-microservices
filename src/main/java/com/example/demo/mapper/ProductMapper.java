package com.example.demo.mapper;

import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.Product;

public class ProductMapper {
    public static ProductDTO toDTO(Product product) {
        ProductDTO p = new ProductDTO();
        p.setId(product.getId());
        p.setName(product.getName());
        p.setSku(product.getSku());
        p.setStock(product.getStock());
        p.setPrice(product.getPrice());
        return p;
    }
    public static Product toEntity(ProductDTO dto) {
        Product p = new Product();
        p.setId(dto.getId());
        p.setName(dto.getName());
        p.setSku(dto.getSku());
        p.setStock(dto.getStock());
        p.setPrice(dto.getPrice());
        return p;
    }
}
