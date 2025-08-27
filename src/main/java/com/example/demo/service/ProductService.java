package com.example.demo.service;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductService {
    ProductDTO create(ProductDTO dto);
    ProductDTO update(Long id, ProductDTO dto);
    Page<ProductDTO> list(Pageable pageable);
    ProductDTO get(Long id);
    void delete(Long id);
    void decreaseStock(Product product, int qty);
}
